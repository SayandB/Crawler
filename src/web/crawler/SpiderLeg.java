/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.crawler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author prate
 */
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
        return true;
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