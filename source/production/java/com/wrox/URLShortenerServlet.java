package com.wrox;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet(
        name = "URLShortener",
        urlPatterns = {"/index.jsp", "/signup.jsp", "/userConsole.jsp"}, /*this specifies the first page to load*/
        loadOnStartup = 1 /* A positive integer says that the servlet must load on start up. Lower number servrlets start first.  */
        				  /* Negative numbers indicate that the servlet may run whenver, not neccessarily on start up */
)
@MultipartConfig(
        fileSizeThreshold = 5_242_880, //5MB
        maxFileSize = 20_971_520L, //20MB
        maxRequestSize = 41_943_040L //40MB
)
public class URLShortenerServlet extends HttpServlet
{
    
    //===========================================================================================================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	String action = request.getParameter("action");
    	
    	System.out.println("doGet(...) called action: "+action); // prints null on start up because no get request made?
    	
    	
    	//request.getCookies();
    	
//    	if(action == null)    /*TODO*/
//        switch(action)
//        {
//            /*TODO*/
//        } 
    	
    }
    //===========================================================================================================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String action = request.getParameter("action");
//        if(action == null)
//        switch(action)
//        {
//        	/*TODO*/ 
//        }
    }
    
    //==========================================================================================================
    /*
     *  This might be useful for something.
     */
    private void downloadAttachment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
//        String idString = request.getParameter("ticketId");
//        Ticket ticket = this.getTicket(idString, response);
//        if(ticket == null)
//            return;
//
//        String name = request.getParameter("attachment");
//        if(name == null)
//        {
//            response.sendRedirect("tickets?action=view&ticketId=" + idString);
//            return;
//        }
//
//        Attachment attachment = ticket.getAttachment(name);
//        if(attachment == null)
//        {
//            response.sendRedirect("tickets?action=view&ticketId=" + idString);
//            return;
//        }
//
//        response.setHeader("Content-Disposition",
//                "attachment; filename=" + attachment.getName());
//        response.setContentType("application/octet-stream");
//
//        ServletOutputStream stream = response.getOutputStream();
//        stream.write(attachment.getContents());
    }
    
    //===========================================================================================================
    private PrintWriter writeHeader(HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        PrintWriter writer = response.getWriter();
        writer.append("<!DOCTYPE html>\r\n")
              .append("<html>\r\n")
              .append("    <head>\r\n")
              .append("        <title> Herp Derp this is a URL Shortener</title>\r\n")
              .append("    </head>\r\n")
              .append("    <body>\r\n");

        return writer;
    }
    
    //===========================================================================================================
    private void writeFooter(PrintWriter writer)
    {
        writer.append("</body>\r\n").append("</html>\r\n");
    }
}
