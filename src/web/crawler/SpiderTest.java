/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.crawler;

import java.util.Scanner;

/**
 *
 * @author prate
 */
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
