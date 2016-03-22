package SingleProcess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class Indexing 
{
	public static ArrayList<String> common_word(String query) throws IOException
	{
		String[] splitted_query = query.split("\\W+");
		
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> keyword = new ArrayList<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader("stopwords.txt"));
		
		while (true)
		{
		    String line = reader.readLine();
		    if (line == null) 
		    {
			break;
		    }
		    list.add(line);
		}
		 
		for(String word : splitted_query)
	    {
	        String wordCompare = word.toLowerCase();
	        if(!list.contains(wordCompare))
	        {
	        	if(word==" ")
	        		word=null;
	            keyword.add(word);
	        }
	    }
		reader.close();
		return keyword;
		       
	}

	public static void add_page_to_index(String url,Document doc) 
	{
		ArrayList<String> words=new ArrayList<String>();
		try
		{
		for(org.jsoup.nodes.Element meta :doc.select("meta[name=keywords]"))
		{
			
			words.add(meta.attr("content"));
			words=common_word(words.toString());
			
		}
		}
		catch(Exception e)
		{
	try{
		if(words.isEmpty())
		{
			for(org.jsoup.nodes.Element meta :doc.select("meta[name=description]")) 
			{
				words.add(meta.attr("content"));
				
			}
			words=common_word(words.toString());
		}
	}
	catch(Exception e1)
	{
		int pos=url.indexOf(".");
			int pos2=url.lastIndexOf(".");
			words.add(url.substring(pos+1,pos2));
        
	}
		}
		

		for(String word : words)
		{

			try {
				add_to_index(word,url);
			} catch (Throwable e) {
			
				e.printStackTrace();
			}
			
		}	
	}
	public static void add_to_index(String key,String url) throws Throwable
	{
		DataBase db = new DataBase();
		String sql = "select RecordID from record where keyword ='"+key+"'";
		ResultSet rs = db.runSql(sql);
		int rid=0;
		
		String str = "SELECT Record_Id FROM urls where URL = '"+url+"'";
		ResultSet rs1 = db.runSql(str);
			
		if((rs.next()))
			{
			rid=rs.getInt("RecordID");
			if(!(rs1.next()))
				{
				String sql1 = "INSERT IGNORE INTO crawler.urls(URL,Record_Id)" + "VALUES ('"+url+"','"+rid+"');";
				PreparedStatement stmt = db.conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
				stmt.execute();
				}
			else
				{
				String qur = "SELECT Record_Id FROM urls where URL = '"+url+"'";
				ResultSet check = db.runSql(qur);
				int cid = 0;
				while(check.next())
					cid=check.getInt("Record_Id");
				if(rid!=cid)
					{
					String sql1 = "INSERT IGNORE INTO crawler.urls(URL,Record_Id)" + "VALUES ('"+url+"','"+rid+"');";
					PreparedStatement stmt = db.conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
					stmt.execute();
					}
				}
			}
		else
			{
			String sql2 = "INSERT IGNORE INTO  `crawler`.`record` " + "(`keyword`) VALUES " + "(?);";
			PreparedStatement stmt = db.conn.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, key);
			stmt.execute();
			String sql3 = "select RecordID from record where KEYWORD = '"+key+"'";
			ResultSet rs2 = db.runSql(sql3);
			int id = 0;
			while (rs2.next())
				{
				id = rs2.getInt("RecordID");
				}
		
			String sql4 = "INSERT IGNORE INTO crawler.urls(URL,Record_Id)" + "VALUES ('"+url+"','"+id+"');";
			stmt = db.conn.prepareStatement(sql4, Statement.RETURN_GENERATED_KEYS);
			stmt.execute();
			}db.finalize();
	}

	public static void main(String args[]) throws Throwable
	{
		Document doc= Jsoup.connect("https://www.computer.org/").timeout(10*1000).get(); 
		add_page_to_index("https://www.computer.org/",doc);
	}
}
