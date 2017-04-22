import java.util.Scanner;
import java.util.Set;
import java.io.IOException;
import java.util.List;
import java.util.HashSet;
import java.util.LinkedList;
import web.crawler.SpiderLeg;


public class Spider
{
    private static final int MAX_PAGES_TO_SEARCH=15;
    private Set<String> pagesVisited=new HashSet<String>();
    private List<String> pagesToVisit=new LinkedList<String>();
    
    private String nextUrl()
    {
        String nextUrl;
        do
        {
            nextUrl=this.pagesToVisit.remove(0);
        }while(this.pagesVisited.contains(nextUrl));
        return nextUrl;
    }
    public void search(String url, String searchWord)
    {
        while(this.pagesVisited.size()<MAX_PAGES_TO_SEARCH)
        {
            String currentUrl;
            SpiderLeg leg=new SpiderLeg();
            if(this.pagesToVisit.isEmpty())
            {
                currentUrl=url;
                this.pagesVisited.add(url);
            }
            else
            {
                currentUrl=this.nextUrl();
            }
            leg.crawl(currentUrl);
            boolean success=leg.searchForWord(searchWord);
            if(success)
            {
                System.out.println(String.format("Success! word %s found at url: %s ",searchWord, currentUrl));
                break;
            }
            this.pagesToVisit.addAll(leg.getLinks());
        }
        System.out.println(String.format("Task completed! %s pages visited",this.pagesVisited.size()));
    }
}
