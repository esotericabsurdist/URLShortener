package com.wrox;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

// @WebServlet annotation eliminates the need to set the following information inside of web.xml, If you think you need to put
// something inside of web.xml, check here first. 
@WebServlet(
        name = "ConsoleServlet",
        urlPatterns = {"/userConsole", "/s/*"}, 
        loadOnStartup = 1 /* A positive integer says that the servlet must load on start up. Lower number servrlets start first.  */
        				  /* Negative numbers indicate that the servlet may run whenver, not neccessarily on start up */
)
public class ConsoleServlet extends HttpServlet
{   
	
	Users users = new Users();
	Links links = new Links();
	
    //===========================================================================================================
    @SuppressWarnings("unused")
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {	
    	// if there is no data, why are we here? Sessions persist after restarts in the eclipse browser.
    	// don't believe me? I'll wager 100 dollars. 
    	if(Links.linkData == null || Users.userData == null) 
    	{
    		response.sendRedirect("/support/index");
    		return;
    	}
    	
    
    	// Filter GET request for shortened URLS -> it would be 
    	// better to move this to a different servlet and implement real servlet filters and filter chain. 
    	try{
    		
    	
	    	System.out.println("Number of Links: "+Links.linkData.size());
	    	String uniqueURLPath = request.getPathInfo();
	    	String uniqueURLIdentifier = uniqueURLPath.replace("/", "");
	    	System.out.println("here's the path " + uniqueURLPath);
	    	System.out.println("here's the id " + uniqueURLIdentifier);
	    	System.out.println("Number of Links: "+Links.linkData.size());
	
	    	if(Links.linkData.containsKey(uniqueURLIdentifier))
	    	{
	    		
	    		// update public store clicks.
	    		Links.linkData.get(uniqueURLIdentifier).clicks++;
	    		System.out.println("Link data clicks: "+Links.linkData.get(uniqueURLIdentifier).clicks);
	    		
	    		// update the user's clicks. 
	    		updateUserClicks(uniqueURLIdentifier);
	    			
	    		System.out.println("Here is the full link from data: "+Links.linkData.get(uniqueURLIdentifier).longURL);
	    		
	    		// send the user on their merry way to the their link.
				response.sendRedirect(Links.linkData.get(uniqueURLIdentifier).longURL);
				
				// this return is necessary subsequent use of response object is not allowed after
				// the response object has been used for redirect. 
				return;
	    	}
	    	else
	    	{
	    		System.out.println("Link not found");
	    	}
    	}
    	catch (Exception e)
    	{
    		System.out.println("Invalid Short Link, or other.");
    	}
    	
    	
   
    	// Get session and take appropriate action
    	HttpSession session = request.getSession(false);
     	
    	if(session != null)// if there is a session, load the user console. 
    	{
    		
        	if(session.getAttribute("userName") != null )
        	{
        			System.out.println("User Name: " + (String)session.getAttribute("userName"));
        			request.getRequestDispatcher("/WEB-INF/userConsole.jsp").forward(request, response);
        	}
        	else
        	{
        		System.out.println("redirected userConsole->index");
    			response.sendRedirect("/support/index");
        	}	
    	}
		else // if there is not a session send them to the home page. 
		{
			System.out.println("redirected userConsole->index");
			response.sendRedirect("/support/index");
		}
    }
    
    
    //===========================================================================================================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {	
    	
    	
    	
    	// Get all parameters associated with POST request.
    	Map<String, String[]> parameters = request.getParameterMap();
    	
    	if(parameters.containsKey("logout")) // log out
    	{
    		  logOut(request, response);
    	}
    	else if(parameters.containsKey("longURL")) // create short url. 
    	{
    		if(parameters.get("longURL").length > 0)
    		{	
    			// create the short url and unique string 
    			String longURL = parameters.get("longURL")[0];
    			System.out.println("Long url: " + longURL);
    			String uniqueURLIdentifier = getUniqueURLIdentifier();
    			
    			// add the url to the current user's list.
    			Link newLink = new Link(uniqueURLIdentifier, longURL);
    			Link newUserLink = new Link(uniqueURLIdentifier, longURL);
    			Users.userData.get(request.getSession().getAttribute("userName")).userLinks.put(uniqueURLIdentifier, newUserLink);
    			Links.linkData.put(uniqueURLIdentifier, newLink);
    			
    			// reload page? no, update page. 
    			request.getRequestDispatcher("/WEB-INF/userConsole.jsp").forward(request, response);
    		}
    		  		
    	}
    	 	
    }    
	//==========================================================================================================   

    private void logOut(HttpServletRequest request, HttpServletResponse response)
    {
    	System.out.println("Logging out");
		request.getSession().invalidate();
		//SessionRegistry.removeSession(request.getSession());
		System.out.println("redirected userConsole->index");
		try {
			response.sendRedirect("/support/index");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	//==========================================================================================================   
    private String getUniqueURLIdentifier()
    {
    	//TODO put this in a loop, that generates unique strings in case a duplicate is made. 
    	// 	   If this produces a duplicate link, buy a lottery ticket. 
    	
    	// create random string 
    	String charSpace = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-.";
    	int charSpaceLength = charSpace.length();
    	SecureRandom random = new SecureRandom(); // random would probably work too.
    	int stringLength = 6; 
    	char randomChar;
    	int randomInt; 
    	char[] randomChars = new char[stringLength];

    	for( int i = 0; i< stringLength; i++)
    	{
    		randomInt = random.nextInt(charSpaceLength);
    		randomChar = charSpace.charAt(randomInt);
    		randomChars[i] = randomChar;
    	}
    	String randomString = new String(randomChars);
    	System.out.println("random url: " + randomString);
    	
    	// create url from random string
    	//String shortURL = "localhost:8080/support/s/"+randomString;
    	//System.out.println("random url: " + shortURL);

    	
    	return randomString;
    	
    }
	//==========================================================================================================   

    private void updateUserClicks(String uniqueURLIdentifier)
    {
    	System.out.println("updateUserClicks(...) called");
    	//!!! Warning!!!! the following code is disgusting. Avert your eyes. Better design and persitent data would eliminate this.
		HashMap<String, User> userData = Users.userData;
		HashMap<String, Link> userLinks;
		
		for(Map.Entry<String, User> userEntry : userData.entrySet()) 
		{	
			String userName = userEntry.getKey();
			userLinks = userData.get(userName).userLinks;
			
			System.out.println("iterating over users, user key: "+ userEntry.getKey());

			
			for(Map.Entry<String, Link> linkEntry : userLinks.entrySet())
			{
				System.out.println("iterating over user links, link key: "+ linkEntry.getKey() );

				if(uniqueURLIdentifier.equals(linkEntry.getKey()))
				{
					System.out.println("Click count for user link: " + Users.userData.get(userName).userLinks.get(uniqueURLIdentifier).clicks );
					userLinks.get(uniqueURLIdentifier).clicks++;
					Users.userData.get(userName).userLinks = userLinks; // don't need. 
					System.out.println("Updated click count for user link: " + Users.userData.get(userName).userLinks.get(uniqueURLIdentifier).clicks );
					return;
				}
			}
		}
    }
    
	//==========================================================================================================   
}