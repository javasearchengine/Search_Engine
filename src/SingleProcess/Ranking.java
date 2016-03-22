package SingleProcess;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.temesoft.google.pr.PageRankService;

public class Ranking {
	public static ArrayList<String> compute_rank(ArrayList<String> urls)
	{
		PageRankService pr=new PageRankService();
		Map<Integer,String> ranks = new HashMap<Integer,String>();
		int val=0;
		String temp;
		
		for(int i=0;i<urls.size();i++)
		{
			ranks.put(pr.getPR(urls.get(i)),urls.get(i));
			if(pr.getPR(urls.get(i))<val)
					{
				val=pr.getPR(urls.get(i));
				temp=urls.get(i);
				urls.add(i,urls.get(i+1));
				urls.add(i+1,temp);
					}
		}
		System.out.println(urls);
		return urls;
	}
	public static void main(String args[])
	{
		ArrayList<String> list=new ArrayList<String>();
		try {
			list=LookUp.look_up("php");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		list=Ranking.compute_rank(list);
		
		System.out.println(list);
	}

}
