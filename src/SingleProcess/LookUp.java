package SingleProcess;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gson.Gson;

import SingleProcess.DataBase;
import SingleProcess.HelperSearch;



public class LookUp {
	private static final long t0 = System.nanoTime();
	
	public static ArrayList<String> look_up(String keyword) throws SQLException, IOException 
	{
		 DataBase db = new DataBase();
			int id=0;
		 ArrayList<String> match=new ArrayList<String>();
		 String sql = "select RecordID from record where keyword ='"+keyword+"'";
		ResultSet rs = db.runSql(sql);
	
		while(rs.next())
			id=rs.getInt("RecordID");
		
		String sql1 = "select URL from urls where Record_Id ='"+id+"'";
		ResultSet result = db.runSql(sql1);
		
		while(result.next())
		{
			match.add(result.getString("URL"));
		}
		if(match.isEmpty())
		{
			match=no_result(keyword);
			
			String sql2 = "INSERT IGNORE INTO  `crawler`.`record` " + "(`keyword`) VALUES " + "(?);";
			PreparedStatement stmt = db.conn.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, keyword);
			stmt.execute();
			String sql3 = "select RecordID from record where KEYWORD = '"+keyword+"'";
			ResultSet rs2 = db.runSql(sql3);
			int reid = 0;
			while (rs2.next())
				{
				reid = rs2.getInt("RecordID");
				}
		for(String url:match)
		{
			String sql4 = "INSERT IGNORE INTO crawler.urls(URL,Record_Id)" + "VALUES ('"+url+"','"+reid+"');";
			stmt = db.conn.prepareStatement(sql4, Statement.RETURN_GENERATED_KEYS);
			stmt.execute();
			}
		}
			
	
		return match;
	}
	public static ArrayList<String> no_result(String keyword) throws IOException {
		ArrayList<String> match=new ArrayList<String>();
		try{
		for (int i = 0; i < 20; i = i + 4) {
			String address = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&start="+i+"&q=";
		 
			String query = keyword;
			String charset = "UTF-8";
			
			URL url = new URL(address + URLEncoder.encode(query, charset));
			Reader reader = new InputStreamReader(url.openStream(), charset);
			HelperSearch results = new Gson().fromJson(reader, HelperSearch.class);
		 
			for (int m = 0; m <= 3; m++) 
			{
				 match.add(results.getResponseData().getResults().get(m).getUrl());
			}
	
		}
		}
		catch(Throwable e)
		{
			System.out.println("Check Internet Connection and Try Again!!");
		}
		return match;
	}
	public static void main(String args[]) throws SQLException, IOException
	{
		ArrayList<String>match=look_up("Technology");
		for(int i=0;i<match.size();i++)
			System.out.println(match.get(i));
		 System.out.println( (System.nanoTime()- t0)+"ns");  
	}

}
