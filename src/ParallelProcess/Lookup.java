package ParallelProcess;



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



public class Lookup extends Thread{
	private static final long t0 = System.nanoTime();
	public static ArrayList<String> result=new ArrayList<String>();
	public static ArrayList<String> match=new ArrayList<String>();
	public static void look_up(String keyword) throws SQLException, IOException 
	{
		 DataBase db = new DataBase();
			int id=0;
		
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
	}
	public static void no_result(String keyword) throws IOException {
		DataBase db=new DataBase();
		
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
				result.add(results.getResponseData().getResults().get(m).getUrl());
				
			}
	
		}
		
			
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
		for(String url:result)
		{
			String sql4 = "INSERT IGNORE INTO crawler.urls(URL,Record_Id)" + "VALUES ('"+url+"','"+reid+"');";
			stmt = db.conn.prepareStatement(sql4, Statement.RETURN_GENERATED_KEYS);
			stmt.execute();
		}
		}
		catch(Exception e)
		{
			System.out.println("Check your Internet Connection and try Again!!");
		}
	}
	public static void main(String args[]) throws SQLException, IOException, InterruptedException
	{
		final String key="Java";
		Thread t1 = new Thread(new Runnable() {
		    
		    public void run() {
		         try {
					look_up(key);
				} catch (SQLException e) {
					
					System.out.println("DataBase Error");
					e.printStackTrace();
				} catch (IOException e) {
					
					System.out.println("Check Internet Connection and Try Again!!");
				}
		    }
		});

		Thread t2 = new Thread(new Runnable() {
		
		    public void run() {
		        try {
					no_result(key);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Check Internet Connection and Try Again!!");
				}
		    }
		});

		t1.start();
		t2.start();
		t2.join();
	
		if(! match.isEmpty()){
			for(int i=0;i<match.size();i++)
				System.out.println(match.get(i));
		}
		else
			{
				for(int i=0;i<result.size();i++)
				{
					System.out.println("inside no result");
				System.out.println(match.get(i));
				}
			}
		
			 System.out.println( (System.nanoTime()- t0)+"ns");  
			 

	}
	
}
