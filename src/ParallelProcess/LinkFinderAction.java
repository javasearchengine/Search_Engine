package ParallelProcess;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveAction;

import org.jsoup.nodes.Document;

import SingleProcess.DataBase;
import SingleProcess.Indexing;
import SingleProcess.LinkAnalyzer;


@SuppressWarnings("serial")
public class LinkFinderAction extends RecursiveAction {

	static Map<String, ArrayList<String>> graph = new HashMap<String, ArrayList<String>>();
	static ArrayList<String> tocrawl=new ArrayList<String>();
	
    private String url;
    private int max;
    private LinkHandler cr;
    /**
     * Used for statistics
     */
    
    public LinkFinderAction(String url, int max,LinkHandler cr) {
        this.url = url;
        this.max=max;
        this.cr = cr;
        
    }

    public static int union(ArrayList<String> tocrawl,Collection<String> all_links)
	{
		
		for (String link : all_links) 
        {
			if(tocrawl.contains(link))
				continue;
				else
					tocrawl.add(link);
		}
	
		return tocrawl.size();
    }
    public void crawlWeb() {
            try {
            	DataBase db=new DataBase();
            	List<RecursiveAction> actions = new ArrayList<RecursiveAction>();
        		
        		ArrayList<String> crawled=new ArrayList<String>();
        		
        		 int max_depth = 6;
        		String page=null;
        		
        		tocrawl.add(url);
        		
        		int size=tocrawl.size();
        		
        		
        		while(!(tocrawl.isEmpty()))
        		{
        			--size;
        			page=tocrawl.get(size);
        		
        			tocrawl.remove(size);
        			if(!(crawled.contains(page))&&(crawled.size()<max))
        			{
        				System.out.println(page);
        				max_depth=max_depth-1;
        				System.out.println(max_depth);
        				if(max_depth<0)
        					break;			
        		
        		Document doc=LinkAnalyzer.getPage(page);
        		Indexing.add_page_to_index(page, doc);
        		ArrayList<String> links = LinkAnalyzer.getAllLinks(doc);
        		size=union(tocrawl,links);
        		graph.put(page, links);
        		if(!(graph.isEmpty()))
        				{
        			String sql4 = "INSERT IGNORE INTO crawler.graph(URL,RE_URL)" + "VALUES ('"+page+"','"+links+"');";
        			PreparedStatement stmt = db.conn.prepareStatement(sql4, Statement.RETURN_GENERATED_KEYS);
        			stmt.execute();
        			
        				}
        		crawled.add(page);
        		actions.add(new LinkFinderAction(page,0, cr));
        		
        		
        		if(size==0)
        		{
        			break;
        		}
        			}
        			
        		}
                //invoke recursively
                invokeAll(actions);
                
            } catch (Exception e) {
            	
                           }
           
        }
               

	@Override
	protected void compute() {
		crawlWeb();
		 
	
	}
}