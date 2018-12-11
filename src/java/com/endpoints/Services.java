/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.endpoints;


import com.Controllers.controllerUser;
import com.model.User;
import java.io.IOException;
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
        // name decloaration                 
        if (fName.length() > 15 || fName.length() < 3){
            //JOptionPane.showMessageDialog(null, "Please enter a valid First Name", "Incorrect details", JOptionPane.ERROR_MESSAGE);
        
            valid = false;
               return Response.status(Response.Status.UNAUTHORIZED).entity("Please enter a valid First Name, Incorrect details").build();
        }
        
        if(fName.equals("")){
            //JOptionPane.showMessageDialog(null, "Name can't be empty", "Incorrect details", JOptionPane.ERROR_MESSAGE);
            valid = false;
               return Response.status(Response.Status.UNAUTHORIZED).entity("\"Name can't be empty\", \"Incorrect details").build();
        }
        
        if (lName.length() > 20 || lName.length() < 3){
          //  JOptionPane.showMessageDialog(null, "Please enter a valid Last name", "Incorrect details", JOptionPane.ERROR_MESSAGE);
            valid = false;
            return Response.status(Response.Status.UNAUTHORIZED).entity("Please enter a valid Last name,Incorrect details").build();
        }
        
        if(lName.equals("")){
           // JOptionPane.showMessageDialog(null, "Last name can't be empty", "Incorrect details", JOptionPane.ERROR_MESSAGE);
            valid = false;
             return Response.status(Response.Status.UNAUTHORIZED).entity("Please enter a valid Last name,Incorrect details").build();
        }
        //Declaraction of email //using patterns for regegular expression
        if (!(Pattern.matches("^[a-zA-Z0-9-_.]+[@]{1}+[gmail]{5}+[.]{1}+[com]{3}+$", email))){
           // JOptionPane.showMessageDialog(null, "Please enter a Gmail email address", "Incorrect details", JOptionPane.ERROR_MESSAGE);
            valid = false;
            return Response.status(Response.Status.UNAUTHORIZED).entity("Please enter a Gmail email address, Incorrect details").build();
        }
        //Declaraction of password / repeat password
        if(password.length() > 15 || password.length() < 8){
          //  JOptionPane.showMessageDialog(null, "Password should be less than 15 and more than 8 characters in length.", "Incorrect details", JOptionPane.ERROR_MESSAGE);
            valid = false;
             return Response.status(Response.Status.UNAUTHORIZED).entity("Password should be less than 15 and more than 8 characters in length. Incorrect details").build();
        
        }
        
        if(password.contains(fName)){
            //JOptionPane.showMessageDialog(null, "Password Should not contain same words as your name", "Incorrect details", JOptionPane.ERROR_MESSAGE);
            valid = false;
             return Response.status(Response.Status.UNAUTHORIZED).entity("Password Should not contain same words as your name,Incorrect details").build();
        
        }
        
        //more regular expression
        String upperCaseChars = "(.*[A-Z].*)";
        if(!password.matches(upperCaseChars )){
           // JOptionPane.showMessageDialog(null, "Password should contain atleast one upper case alphabet", "Incorrect details", JOptionPane.ERROR_MESSAGE);
            valid = false;
            return Response.status(Response.Status.UNAUTHORIZED).entity("Password should contain atleast one upper case alphabet, Incorrect details").build();
        
        }
        
        String lowerCaseChars = "(.*[a-z].*)";
        if(!password.matches(lowerCaseChars )){
           // JOptionPane.showMessageDialog(null, "Password should contain atleast one lower case alphabet", "Incorrect details", JOptionPane.ERROR_MESSAGE);
            valid = false;
             return Response.status(Response.Status.UNAUTHORIZED).entity("Password should contain atleast one lower case alphabet, Incorrect details").build();
        
        }
        
        String numbers = "(.*[0-9].*)";
        if (!password.matches(numbers )){
           // JOptionPane.showMessageDialog(null, "Password should contain atleast one number.", "Incorrect details", JOptionPane.ERROR_MESSAGE);
            valid = false;
             return Response.status(Response.Status.UNAUTHORIZED).entity("Password should contain atleast one number. Incorrect details").build();
        
        }
        
        if (!password.matches(repeatPassword)){
           // JOptionPane.showMessageDialog(null, "Passwords dont match.", "Incorrect details", JOptionPane.ERROR_MESSAGE);
            valid = false;
            return Response.status(Response.Status.UNAUTHORIZED).entity("Passwords dont match. Incorrect details").build();
        
        }
        
        if(valid){
            User user = new User();
            user.setID(0);
            user.setName(fName);
            user.setLastname(lName);
            user.setEmail(email);
            user.setPassword(password);
            user.setRepassword(repeatPassword);
                        
             controllerUser userController = new controllerUser();

            int resgistered = userController.createAccount(user);

            if (resgistered > 0) {
              //  JOptionPane.showMessageDialog(null, "You have been Registered");
               servletResponse.sendRedirect("Logged.html"); //example
           
            }
            else {
               // JOptionPane.showMessageDialog(null, "Email is already taken", "Incorrect details", JOptionPane.ERROR_MESSAGE);
                 return Response.status(Response.Status.UNAUTHORIZED).entity("Email is already taken").build();
            }
        }
            return null;
           
          
    }
    
    
    
    
    
    
    
}//end of class
