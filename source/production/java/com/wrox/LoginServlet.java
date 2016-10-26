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
import java.net.HttpCookie;
import java.util.LinkedHashMap;
import java.util.Map;


// @WebServlet annotation eliminates the need to set the following information inside of web.xml, If you think you need to put
// something inside of web.xml, check here first. 
@WebServlet(
        name = "LoginServlet",
        urlPatterns = {"/index", "/signup"}, /*this specifies the first page to load*/
        loadOnStartup = 1 /* A positive integer says that the servlet must load on start up. Lower number servrlets start first.  */
        				  /* Negative numbers indicate that the servlet may run whenver, not neccessarily on start up */
)
public class LoginServlet extends HttpServlet
{   	
	Users users = new Users();

    //===========================================================================================================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	
    	
    	// Get session
    	HttpSession session = request.getSession(false);
    	
    	if(session != null) // if there is current session, redirect to the console page. 
    	{
    		if(session.getAttribute("userName") != null){
    			System.out.println("User Name: " + (String)session.getAttribute("userName"));
    			System.out.println("Redirecting index||signup -> userConsole");
        	
    			// A user is already logged in, so direct them to the console. 
    			response.sendRedirect("/support/userConsole");
    		}
    		else
    		{
    			String action = request.getServletPath();
    			System.out.println(request.getServletPath());
			
    			switch(action)
    			{
    			case "/index": request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
    			case "/signup": request.getRequestDispatcher("/WEB-INF/signup.jsp").forward(request, response);		
    			}
    		}
		}
    	else
    	{
    			String action = request.getServletPath();
    			System.out.println(request.getServletPath());
			
    			switch(action)
    			{
    			case "/index": request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
    			case "/signup": request.getRequestDispatcher("/WEB-INF/signup.jsp").forward(request, response);		
    			}
    	}
    	
    }
    //===========================================================================================================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    /*
     *  This method can receive three different sets of POST data.
     *  From /index it will receive user login credentials or a long URL to be turned into a short URL.
     *  From /signUp it will receive new user credentials. 
     *  
     */
    {	
    	System.out.println("Hello from doPost(...) in Login Servlet");
    	// Get all parameters associated with POST request.
    	Map<String, String[]> parameters = request.getParameterMap();
    	
    	// Determine which parameters are present and take appropriate action.
    	if(parameters.containsKey("userName") && parameters.containsKey("password") && parameters.containsKey("passwordConfirm")) // Signup
    	{	
    		System.out.println("Received sign up data ...");
    		
    		// get the user data
			String userName = request.getParameter("userName");
			String password = request.getParameter("password");
			String passwordConfirm = request.getParameter("passwordConfirm");
    		
    		// add the user if credentials are valid.
    		if(password.equals(passwordConfirm)){
    		
    			if( isUserNameValid(userName) )
    			{
    				System.out.println("Sign up data is valid");
    			
    				// add the user data to storage
    				User newUser = new User(userName, password);
    				Users.userData.put(userName, newUser);
    				
    				// Log the user in, send them to userConsole.jsp where their links are displayed. 
            		login(request, response, userName, password);
    			}
    			else
    			{
    				// Tell user the name already exists or is invalid.  Handle appropriately. 
            		/* TODO */ 
    				System.out.println("Username is invalid");
    				request.getRequestDispatcher("/WEB-INF/signup.jsp").forward(request, response);		
    			}
    			
    		}
    		else
    		{
    			// Tell the user the passwords don't match. 
    			/* TODO */
    			System.out.println("Passwords do not match: \""+ passwordConfirm +"\" \""+password+"\"" );
				request.getRequestDispatcher("/WEB-INF/signup.jsp").forward(request, response);		
    		}
    	}
    	else if(parameters.containsKey("userName") && parameters.containsKey("password")) // login
    	{
    		System.out.println("received login data ...");
    		
    		// Get username and password parameters
        	String userName = request.getParameter("userName");
        	String password = request.getParameter("password");
        	System.out.println("username:" + userName);
        	System.out.println("password:" + password);    	
        	System.out.println("parameters: " + request.getParameterNames());
        
        	// Validate user credentials
        	if(isUserValid(userName, password))
        	{
        		System.out.println("Loggin in user: " + userName);
        		// Log the user in, send them to userConsole.jsp where their links are displayed. 
        		login(request, response, userName, password);
        	}
        	else
        	{
        		// Tell user the credentials are incorrect. 
        		/* TODO */ 
		    	request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
        		return;
        	}
    	}
    	else if(parameters.containsKey("shortURL")) // get long url from short
    	{	
    		System.out.println("Fetching long URL ...");
    		String shortURL = request.getParameter("shortURL");
    		
    		if(shortURL.length() >= 6 ) // let the user enter any or no string preceding the unique link id. 
    		{
    			/* For substring method, always remember:
        		 * start index is inclusive, end index is exclusive
        		 */
    			int start = shortURL.length()-6;
    			int end = shortURL.length();
    			String uniqueURLIdentifier = shortURL.substring(start, end);
    			
    			if(Links.linkData.size() >0)
    			{
    				if(Links.linkData.containsKey(uniqueURLIdentifier))
    				{
    					String longURL = Links.linkData.get(uniqueURLIdentifier).longURL;
    					System.out.println("Here is the long link: " + longURL);
    			    	request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
    				}
    			}
    			else
    			{
    				System.out.println("There are no links.");
    		    	request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
    			}
    		}
    		else
    		{
    			System.out.println("Not a valid short URL, too short");
    	    	request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
    		}
    	}
    	else
    	{
    		System.out.println("No valid post paramaters ...");
    	}
    }
    
	//==========================================================================================================
    private void login(HttpServletRequest request, HttpServletResponse response, String userName, String password) {
		/* 
		 * 	Verify user data. If the login credentials are correct, load the userConsole.jsp and display their user data.
		 * 	If the data is incorrect, remain on the current page. 
    	*/
    	try{	
    		// create session for username
    		HttpSession session = request.getSession(true); // true says create new session if one is not present.
    		session.setAttribute("userName", userName);
    			   			
   			// send to the userConsole
    		System.out.println("Redirecting signup/index -> userConsole");
   			response.sendRedirect("/support/userConsole");    				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		
    }
    
	//==========================================================================================================
    private boolean isUserValid(String userName, String password)
    {    
    	System.out.println("Validating user data, userName="+userName+"password="+password);
    	//Look up user name and password 
    	if(Users.userData.containsKey(userName))
    	{
    		if( password.equals(Users.userData.get(userName).password))
    		{
    			
    			System.out.println("valid userName: " + Users.userData.get(userName));
    			System.out.println("valid password: " + Users.userData.get(password));
    			return true;
    		}
    		else
    		{
    			System.out.println("Failed Login, invalid password");
    			return false;
    		}
    	}
    	else
    	{
			System.out.println("Failed Login, invalid username");
    		return false;
    	}
    }

	//==========================================================================================================
    
    private boolean isUserNameValid(String userName)
    {    
    	// Make sure that no user name with the given string already exist in database or map or whatever storage container we use. 
    	/* TODO */
    	
    	return true;
    }
	//==========================================================================================================

}
	//==========================================================================================================
    
    
    
    
    
    
    
    
    
    
    
    