import java.util.Scanner;
import java.util.Set;
import java.io.IOException;
import java.util.List;
import java.util.HashSet;
import java.util.LinkedList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
public class SpiderLeg
{
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    
    private List<String> links=new LinkedList<String>();
    private Document htmlDocument;
    public boolean crawl(String url)
    {
        try{
            Connection connection=Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDocument=connection.get();
            this.htmlDocument=htmlDocument;
            if(connection.response().statusCode() == 200)
            {
                System.out.println("Recieved Webpage from :"+url);
            }
            if(!connection.response().contentType().contains("text/html"))
            {
                System.out.println("**Failure** Retrieved something other than HTML");
                return false;
            }
            Elements linksOnPage = htmlDocument.select("a[href]");
            System.out.println("Found (" + linksOnPage.size() + ") links");
            for(Element link : linksOnPage)
            {
                this.links.add(link.absUrl("href"));
            }
        }
        catch(IOException ioe)
        {
            System.out.println("Error in out HTTP request " + ioe);
        }
    }
    public boolean searchForWord(String searchWord)
    {
          if(this.htmlDocument == null)
        {
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
            return false;
        }
        
        System.out.println("Searching for word "+searchWord+"...");
        String bodyText=this.htmlDocument.body().text();
        return bodyText.toLowerCase().contains(searchWord.toLowerCase());
    }
     public List<String> getLinks()
     {
         return this.links;
     }
  }

public class SpiderTest
{
    public static void main(String[] args)
    {
        Spider spider=new Spider();
        System.out.println("Enter start url, Search word");
        Scanner s=new Scanner(System.in);
        String url=s.nextLine();
        String word=s.nextLine();
        spider.search(url, word);
    }
}