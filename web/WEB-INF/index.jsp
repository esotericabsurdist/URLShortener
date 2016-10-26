<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Fantastic URL Shortener</title>
    
   
    
  </head>
  <body>
  
    <h1>URL Shortener Service</h1>
    
   <form method="post" action="">
    User Name: <input type="text" name="userName">
    Password:  <input type="password" name="password">
    		   <input type="submit" value="Login">
   </form>
  


  	<p>Date:<%= new java.util.Date()  %></p>  
    <a href="signup">Sign Up</a> 
     
     
    <form method="post">
    Check your URL: <input type="text" name="shortURL">
    		   <input type="submit" value="View Long URL">
   </form>  
   
   
  
   
      
      
  </body>
</html>