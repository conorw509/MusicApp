/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.bank.service;

import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.ws.rs.core.Response;

/**
 *
 * @author conor
 */
public class customerService {
    Gson gson = new Gson();
      String url= "jdbc:derby://localhost:1527/bank";
String userN = "root";
String pWord = "Lola.1.2.3";
 Connection conn = null;
 int count = 0;
    
    public Response addCustomer(String name,String address,String email, String password) throws ClassNotFoundException, SQLException{
        
        try{
        Class.forName("org.apache.derby.jdbc.ClientDriver");   //accounts.status
        conn = DriverManager.getConnection(url, userN, pWord);
          Statement st = conn.createStatement();
          int rs = st.executeUpdate("INSERT INTO customers (name,address,email,password) values ("+name+","+address+","+email+","+password+")");
          
          if(rs ==1){
              
               return Response.status(200).entity(gson.toJson("Customer added")).build();
          }
          st.close();
          conn.close();
    }
    catch(Exception e){
    System.out.println(e);
}
  return Response.status(200).entity(gson.toJson("failed")).build();
}
    
    
    
    
}//end of class
