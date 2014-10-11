package com.ir.hw1;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebCrawl {
	
	final static private String MAIN_PAGE = "http://en.wikipedia.org/wiki/Main_Page";
	final static private String PAGE_KEYWORD = "http://en.wikipedia.org/wiki/";
	final static private int DEPTH = 3;
	static private int level = 0;
	Queue<String> queue = new LinkedList<String>();
	
	private Set<String> uniqueUrl = new HashSet<String>();
	private Set<String> urlWithKeyPhrase = new HashSet<String>();
	
	
	public void crawlPage(String URL) {
		crawlPage(URL, "");
	}
	
	public void crawlPage(String URL, String keyPhrase)  {
		queue.add(URL);
		queue.add("changeLevel");
//		level = 1;

		while(queue.size() > 1) {
			String url = queue.remove();
			if(url.equals("changeLevel")) {
				level+=1;
				queue.add("changeLevel");
			} else {
			Document doc = null;
			String htmlDoc = "";
			
			try {
				doc = Jsoup.connect(url).timeout(10*1000).get();
				htmlDoc = doc.toString();
			} catch (Exception e) {
				System.out.println(e.getLocalizedMessage());
			}
			String canonicalURL="";
			if(doc !=null) {
				canonicalURL = doc.select("[rel=canonical]").attr("abs:href");
				if(!uniqueUrl.contains(canonicalURL)) {
					
					uniqueUrl.add(canonicalURL);
					
					if(keyPhrase.equals("")) {
						System.out.println(canonicalURL);
					}
					
					if(checkKeyPhrase(htmlDoc, keyPhrase)) {
						if(!keyPhrase.equals("")) {
							urlWithKeyPhrase.add(canonicalURL);
							System.out.println(canonicalURL);
						}

						Elements links = doc.select("a");
						for (Element link : links) {
							String urlAbs = link.attr("abs:href");
							if (!urlAbs.equals(MAIN_PAGE) && urlAbs.contains(PAGE_KEYWORD) && !link.toString().contains(":")) {

								if(link.toString().contains("#")) {
									urlAbs = urlAbs.split("#")[0];
								}

								if(!uniqueUrl.contains(urlAbs) && level < DEPTH - 1) {
									queue.add(urlAbs);
								} 
							}
						}
					}
				}}}
		}

		System.out.println("Total links: " + uniqueUrl.size());
		
		if(!keyPhrase.equals(""))
		System.out.println("Links with the keyphrase: " + urlWithKeyPhrase.size());
	}
	
	
	/**
	 * checks if string html contains keyPhrase
	 * @param html
	 * @param keyPhrase
	 * @return
	 */
	private boolean checkKeyPhrase(String html,String keyPhrase) {
		return Pattern.compile(Pattern.quote(keyPhrase), 
				Pattern.CASE_INSENSITIVE).matcher(html).find();
	}
	
}