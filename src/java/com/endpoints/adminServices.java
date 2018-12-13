/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.endpoints;

import com.DB.databaseConnection;
import java.awt.HeadlessException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
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
    
     
    /*
       @POST
    @Path("/update")
    @Consumes("application/x-www-form-urlencoded")
    public Response updateMusic(       

      @FormParam("title") String title, 
      @FormParam("artist") String artist, 
      @FormParam("album") String album, 
      @FormParam("genre") String genre,
      @Context HttpServletResponse servletResponse) throws SQLException, ClassNotFoundException, NamingException, IOException {
                 
        try{
            Class.forName("org.apache.derby.jdbc.ClientDriver");
             conn=DriverManager.getConnection(url, userN, pWord);
             String sql="UPDATE MUSCICLIST set(id,title, artist, album, genre) VALUES(?, ?, ?, ?)";
             PreparedStatement pst=conn.prepareStatement(sql);
             pst = conn.prepareStatement(sql);
              pst.setString(1, title);
             pst.setString(2, artist);
             pst.setString(3,album);
             pst.setString(4, genre);          
             pst.executeUpdate();
            
              return Response.status(Response.Status.OK).entity("updated").build();
           }
           catch(HeadlessException | SQLException e){
              e.getMessage();
           }
                  return Response.status(Response.Status.FORBIDDEN).entity("failed").build();
    }*/
    
   
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
    
}
