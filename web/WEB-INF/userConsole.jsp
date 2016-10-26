<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>URL Shortener User Console</title>
  </head>
  <body>
  		
  		
  	<h1>URL Shortener Service</h1>
  	<p>Date:<%= new java.util.Date()  %></p>  
  	
	<form method="post" action="" >
    	 <input type="submit" name="logout" value="Log Out">
    </form>
      
      
	 
	 <form method="post" action="">
     Long URL:  <input type="text" name="longURL">
    		   <input type="submit" value="Get Short URL">
   </form>		
			
	
	        
			
  </body>
</html>