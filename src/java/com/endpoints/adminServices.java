/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.endpoints;

import com.Controllers.controllerAdmin;
import com.DB.databaseConnection;
import com.model.User;
import java.awt.HeadlessException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 *
 * @author conor
 */
@Path("/adminServices")
public class adminServices {
    
    private String url = "jdbc:derby://localhost:1527/bank";
   private String userN = "root";
    private String pWord = "Lola.1.2.3";
       private    Connection con;
          // databaseConnection db;
        private   PreparedStatement pst;
        
 
    @POST
    @Path("/add")
    @Consumes("application/x-www-form-urlencoded")
    public Response addMusic(       
      @FormParam("id") int id, 
      @FormParam("title") String title, 
      @FormParam("artist") String artist, 
      @FormParam("album") String album, 
      @FormParam("genre") String genre,
      @Context HttpServletResponse servletResponse) throws SQLException, ClassNotFoundException, NamingException, IOException {
            
       User u = new User();
       controllerAdmin controller =  new controllerAdmin();
       boolean getAdId = controller.checkId(u);
                
                if(getAdId){
                         
                    return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid Input").build();
                }
        
          if(title.equals("") || artist.equals("") || album.equals("") || genre.equals("")){
     
               return Response.status(Response.Status.UNAUTHORIZED).entity("\"Fields can't be empty\", \"Incorrect details").build();
        }
          
             
        try{
            
            
         
            Class.forName("org.apache.derby.jdbc.ClientDriver"); 
           con = DriverManager.getConnection(url, userN, pWord);
           
            //con = db.getConnection();
             String sql="INSERT INTO MUSIC (id,title, artist, album, genre) VALUES(?, ?, ?, ?, ?)";
              pst = con.prepareStatement(sql);
             pst = con.prepareStatement(sql);
           
             pst.setInt(1, id);
             pst.setString(2, title);
             pst.setString(3, artist);
             pst.setString(4,album);
             pst.setString(5, genre);        
             pst.executeUpdate();
            
              return Response.status(Response.Status.UNAUTHORIZED).entity("Added").build();
           }
           catch(HeadlessException | SQLException e){
              e.getMessage();
           }
     return Response.status(Response.Status.FORBIDDEN).entity("failed").build();
    }
    
     
    
   
     @POST
    @Path("/delete")
    @Consumes("application/x-www-form-urlencoded")
    public Response deleteMusic(       
      @FormParam("id") int id, 
      @FormParam("title") String title, 
      @FormParam("artist") String artist, 
      @FormParam("album") String album, 
      @FormParam("genre") String genre,
      @Context HttpServletResponse servletResponse) throws SQLException, ClassNotFoundException, NamingException, IOException {
              
           if(title.equals("") || artist.equals("") || album.equals("") || genre.equals("")){
     
               return Response.status(Response.Status.UNAUTHORIZED).entity("\"Fields can't be empty\", \"Incorrect details").build();
        } 
        
        try{
          Class.forName("org.apache.derby.jdbc.ClientDriver");
         con =DriverManager.getConnection(url, userN, pWord);
        // con = db.getConnection();
         String sql="DELETE from MUSIC where id=?";
         PreparedStatement pst= con.prepareStatement(sql);
         pst = con.prepareStatement(sql);
         pst.setInt(1, id);
         pst.executeUpdate();
         return Response.status(Response.Status.OK).entity("deleted").build();
      
        
       }
       catch(HeadlessException | SQLException e){
      e.getMessage();
       }
                  return Response.status(Response.Status.FORBIDDEN).entity("failed").build();
    }
    
     @GET
    @Path("/view")
    protected void viewData(@Context HttpServletResponse response) throws ClassNotFoundException, SQLException, IOException{
                  
       response.setContentType("text/html");
       PrintWriter out = response.getWriter();
       Class.forName("org.apache.derby.jdbc.ClientDriver");
       con =DriverManager.getConnection(url, userN, pWord);
          
        
        try{
        
     
            String sql= ("SELECT id,title,artist,album,genre FROM music");
            pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            String table ="<table><tr>/<th> Id </th><th>Title </th> <th> Artist</th> <th> Album</th> <th> Genre</th> </tr></table>";
            
            while(rs.next()){
            table = "<tr><td>" + rs.getString(1)+ "</td><td>"  + rs.getString(2)+ "</td><td>"   + rs.getString(3)+  "</td><td>"
                    + rs.getString(4)+ "</td><td>"+ rs.getString(5)+  "</td> </tr>";
               
            
        }
            table += "</table>";
            out.println(table);
            con.close();
            
        } catch(HeadlessException | SQLException e){
      e.getMessage();
       }
        
        
    }
}
