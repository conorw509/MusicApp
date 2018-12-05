/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.bank;


import com.google.gson.Gson;
import com.project.bank.objects.customer;
import static com.sun.corba.se.impl.presentation.rmi.StubConnectImpl.connect;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import static javax.management.remote.JMXConnectorFactory.connect;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import static javax.ws.rs.HttpMethod.POST;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import static org.apache.tomcat.jni.Local.connect;

/**
 *
 * @author conor
 */
@Path("/customer")
public class customerResource {
    
    String url= "jdbc:derby://localhost:1527/bank";
String userN = "root";
String pWord = "Lola.1.2.3";
 Connection conn = null;
 int count = 0;

 
  public customer getFromResultSet(ResultSet rs) throws SQLException {
        customer customer = new customer();
        customer.setId(rs.getInt("customer_id"));
        customer.setEmail(rs.getString("email"));
        customer.setName(rs.getString("name"));
        return customer;
    }
 
    @GET
   @Path("/getAllCustomers")
 @Produces({javax.ws.rs.core.MediaType.APPLICATION_JSON})
    public ArrayList<customer> getAllCustomers() throws ClassNotFoundException, SQLException{
        
        
        ArrayList<customer> arr = new ArrayList<>();
       
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        conn = DriverManager.getConnection(url, userN, pWord);
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM customers");
        while(rs.next()){
            customer ts = new customer();
         //  ts.setId(rs.getInt("customer_id "));
            ts.setName(rs.getString("name"));
                   ts.setAddress(rs.getString("address"));
                          ts.setEmail(rs.getString("email"));
                             ts.setPassword(rs.getString("password"));
                           
                             arr.add(ts);    
            
        }
        
        return arr;     
    }
    
    
    
    
           @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response getCustomerById(@PathParam("id") int id) throws SQLException, NamingException, ClassNotFoundException {
          
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        conn = DriverManager.getConnection(url, userN, pWord);
        Gson gson = new Gson();
        
        
           /*PreparedStatement p = conn.prepareStatement("SELECT status from account where customer_id = ?");
           
           p.setInt(1, id);
            ResultSet s = p.executeQuery();

            if (s.next()) {
                int status = s.getInt("status");
                if (status == 1) {
                    */
           //hellokpedj
                 String er = "Error The account has been removed or doesnt exist";
                    String verifyAPI = "SELECT * FROM customers WHERE customer_id = ?";
                    PreparedStatement st = conn.prepareStatement(verifyAPI);
                    st.setInt(1, id);
                    ResultSet rs = st.executeQuery();
                    List events = new ArrayList<>();
                    if (rs.next()) {
                        customer e = getFromResultSet(rs);
                        events.add(e);
                         conn.close();
 return Response.status(200).entity(gson.toJson(events)).build();
  
                    }
                    
                    else {
                    return Response.status(200).entity(gson.toJson(er)).build();
                }
    }
         
       
}
