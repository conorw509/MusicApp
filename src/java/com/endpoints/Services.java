/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.endpoints;


import com.Controllers.controllerUser;
import com.DB.databaseConnection;
import com.model.User;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;


/**
 *
 * @author conor
 */
@Path("/services")
public class Services {
  
 private databaseConnection conn;
    
    
        @GET
    @Path("/hello")
    @Produces("application/Json")
    
    public Response hello(){
        
      return Response.ok().entity("hello").build();
        
    }
    
    
    
    @POST
    @Path("/register")
    @Consumes("application/x-www-form-urlencoded")
    public Response register(
      @FormParam("fName") String fName, 
      @FormParam("lName") String lName,  
      @FormParam("email") String email, 
      @FormParam("password") String password,
      @FormParam("repeatPassword") String repeatPassword,  
      @Context HttpServletResponse servletResponse) throws SQLException, ClassNotFoundException, NamingException, IOException {
       
        
              boolean valid = true;
               controllerUser userController = new controllerUser();
              
        // name decloaration                 
        if (fName.length() > 15 || fName.length() < 3){
            valid = false;
               return Response.status(Response.Status.UNAUTHORIZED).entity("Please enter a valid First Name, Incorrect details").build();
        }
        
        if(fName.equals("")){
            valid = false;
               return Response.status(Response.Status.UNAUTHORIZED).entity("\"Name can't be empty\", \"Incorrect details").build();
        }
        
        if (lName.length() > 20 || lName.length() < 3){
           valid = false;
            return Response.status(Response.Status.UNAUTHORIZED).entity("Please enter a valid Last name,Incorrect details").build();
        }
        
        if(lName.equals("")){
            valid = false;
             return Response.status(Response.Status.UNAUTHORIZED).entity("Please enter a valid Last name,Incorrect details").build();
        }
        //Declaraction of email //using patterns for regegular expression
        if (!(Pattern.matches("^[a-zA-Z0-9-_.]+[@]{1}+[gmail]{5}+[.]{1}+[com]{3}+$", email))){
            valid = false;
            return Response.status(Response.Status.UNAUTHORIZED).entity("Please enter a Gmail email address, Incorrect details").build();
        }
        //Declaraction of password / repeat password
        if(password.length() > 15 || password.length() < 8){
            valid = false;
             return Response.status(Response.Status.UNAUTHORIZED).entity("Password should be less than 15 and more than 8 characters in length. Incorrect details").build();
        
        }
        
        if(password.contains(fName)){
            valid = false;
             return Response.status(Response.Status.UNAUTHORIZED).entity("Password Should not contain same words as your name,Incorrect details").build();
        
        }
        
        //more regular expression
        String upperCaseChars = "(.*[A-Z].*)";
        if(!password.matches(upperCaseChars )){
            valid = false;
            return Response.status(Response.Status.UNAUTHORIZED).entity("Password should contain atleast one upper case alphabet, Incorrect details").build();
        
        }
        
        String lowerCaseChars = "(.*[a-z].*)";
        if(!password.matches(lowerCaseChars )){
            valid = false;
             return Response.status(Response.Status.UNAUTHORIZED).entity("Password should contain atleast one lower case alphabet, Incorrect details").build();
        
        }
        
        String numbers = "(.*[0-9].*)";
        if (!password.matches(numbers )){
            valid = false;
             return Response.status(Response.Status.UNAUTHORIZED).entity("Password should contain atleast one number. Incorrect details").build();
        
        }
        
        if (!password.matches(repeatPassword)){
            valid = false;
            return Response.status(Response.Status.UNAUTHORIZED).entity("Passwords dont match. Incorrect details").build();
        
        }
        
        //get email checking for email in users table
       /* String getEmail = ("SELECT * FROM users WHERE email =?");
        
         PreparedStatement st = conn.prepareStatement(getEmail);
                st.setString(1, email);
                ResultSet rs2 = st.executeQuery();

                String getEmailI = " ";
                if (rs2.next()) {
                   
                    //assign got customer id from account table to variable
                    getEmailI = rs2.getString(1);
                }
                System.out.println(getEmailI);*/
    
       User user = new User();
       user.setEmail(email);
       boolean getEmail = userController.checkEmail(user);
                
                if(getEmail){
                         
                    return Response.status(Response.Status.UNAUTHORIZED).entity("Email Already Exists. Incorrect details").build();
                }
                
                
        if(valid){
         
            user.setID(0);
            user.setName(fName);
            user.setLastname(lName);
            user.setEmail(email);
            user.setPassword(password);
            user.setRepassword(repeatPassword);
                        
            

            int resgistered = userController.createAccount(user);

            if (resgistered > 0) {
            
                servletResponse.sendRedirect("http://localhost:8080/MusicApp/loggedIn.html"); //example
           
            }
            else {
                 return Response.status(Response.Status.UNAUTHORIZED).entity("Server Failed").build();
            }
        }
            return null;
           
          
    }
    
   @POST
    @Path("/userLogin")
    @Consumes("application/x-www-form-urlencoded")
    public Response userLogin(
      
      @FormParam("email") String email, 
      @FormParam("password") String password, 
      @Context HttpServletResponse servletResponse) throws SQLException, ClassNotFoundException, NamingException, IOException {
           
        User u = new User();
        u.setEmail(email);
        u.setPassword(password);
        
        controllerUser userController = new controllerUser();
        
        boolean log = userController.checkLogin(u);
        
        if (log) {
              
            servletResponse.sendRedirect("http://localhost:8080/MusicApp/loggedIn.html");
              
            
        } else {
           
            return Response.status(Response.Status.UNAUTHORIZED).entity("Either email or password is wrong Incorrect details").build();
            
        } 
        
    return Response.status(Response.Status.UNAUTHORIZED).entity("Server Failed").build();
    }
    
    
    
    
    
}//end of class
