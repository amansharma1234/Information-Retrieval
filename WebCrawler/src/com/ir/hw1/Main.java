package com.ir.hw1;
public class Main {
	public static void main(String[] args) {
		WebCrawl w = new WebCrawl();
		if(args.length == 2) {
			w.crawlPage(args[0],args[1]);
		} else if(args.length == 1) {
			w.crawlPage(args[0]);
		}
		else {
			System.out.println("Please enter seed url (http://en.wikipedia.org/wiki/Gerard_Salton) and keyphrase as arguments"); 
		}
		
	}
}
