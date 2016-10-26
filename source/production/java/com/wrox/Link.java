package com.wrox;

public class Link {
	
	public String shortURL;
	public String longURL;
	public int clicks;
	
	Link()
	{
		shortURL = new String();
		longURL = new String();
		clicks = 0;
	}
	
	Link(String tShortURL, String tLongURL)
	{
		shortURL = tShortURL;
		longURL = tLongURL;
		clicks = 0;
	}
	
	
	
}
