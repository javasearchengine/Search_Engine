package ParallelProcess;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ForkJoinPool;
import java.util.HashSet;


public class WebCrawler implements LinkHandler {

    private final Collection<String> visitedLinks = Collections.synchronizedSet(new HashSet<String>());
    
    private String url;
    private int max;
    private ForkJoinPool mainPool;

    public WebCrawler(String startingURL, int max,int maxThreads) {
        this.url = startingURL;
        this.max=max;
        mainPool = new ForkJoinPool(maxThreads);
    }


	private void startCrawling() {
        mainPool.invoke(new LinkFinderAction(this.url,this.max, this));
    }


    public int size() {
        return visitedLinks.size();
    }

   
    public void addVisited(String s) {
        visitedLinks.add(s);
    }

    public boolean visited(String s) {
        return visitedLinks.contains(s);
    }

    public static void main(String[] args) throws Exception {
    	 long t0 = System.nanoTime();

        new WebCrawler("http://www.javaworld.com",5, 64).startCrawling();
        System.out.println( (System.nanoTime()- t0)+"ns");  
    }

	
}