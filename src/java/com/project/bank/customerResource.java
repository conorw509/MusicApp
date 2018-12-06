/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.bank;


import com.google.gson.Gson;
import com.project.bank.objects.customer;
import com.project.bank.service.customerService;
import static com.sun.corba.se.impl.presentation.rmi.StubConnectImpl.connect;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static javax.management.remote.JMXConnectorFactory.connect;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import static javax.ws.rs.HttpMethod.POST;
import static javax.ws.rs.HttpMethod.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
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
 customerService service = new customerService();

 
  public customer getFromResultSet(ResultSet rs) throws SQLException {
        customer customer = new customer();
        customer.setId(rs.getInt("customer_id"));
        customer.setEmail(rs.getString("email"));
        customer.setName(rs.getString("name"));
        customer.setPassword("password");
        customer.setAddress("address");
        return customer;
    }
 
  
  
  @GET
   @Path("/getAllCustomers")
 @Produces({javax.ws.rs.core.MediaType.APPLICATION_JSON})
    public ArrayList<customer> getAllCustomers() throws ClassNotFoundException, SQLException{
        
        
        ArrayList<customer> arr = new ArrayList<>();
       
        Class.forName("org.apache.derby.jdbc.ClientDriver");   //accounts.status
        conn = DriverManager.getConnection(url, userN, pWord);
        Statement st = conn.createStatement();
       // ResultSet rs = st.executeQuery("SELECT * FROM customers");
      // ResultSet rs = st.executeQuery("SELECT customers.customer_id,customers.name, accounts.customer_id")
       ResultSet rs = st.executeQuery( "SELECT customers.name, customers.address, customers.email,customers.password FROM customers INNER JOIN accounts ON customers.customer_id = accounts.customer_id");
       //gets back all customers with same customerID
       while(rs.next()){
            customer ts = new customer(); 
          //ts.setId(rs.getInt("id"));
            ts.setName(rs.getString("name"));
                   ts.setAddress(rs.getString("address"));
                          ts.setEmail(rs.getString("email"));
                             ts.setPassword(rs.getString("password"));
                           
                             arr.add(ts);    
            
        }
        
        return arr;     
    }
    
    @POST
    @Path("/addCustomer") 
    //   @Consumes(MediaType.MULTIPART_FORM_DATA)
   @Consumes({MediaType.APPLICATION_JSON})
    @Produces({javax.ws.rs.core.MediaType.APPLICATION_JSON})
    public Response addCustomer( 
            
            @FormParam ("name")String name,
            @FormParam("address")String add,
            @FormParam("email")String emaail,
            @FormParam("password")String pass,
            @Context HttpServletResponse response
    ) throws IOException, ClassNotFoundException, SQLException{
        
        return service.addCustomer(name, add, emaail, pass);
        
        
    }
    
   @POST
   @Consumes({MediaType.APPLICATION_JSON})
    @Produces({javax.ws.rs.core.MediaType.APPLICATION_JSON})
    @Path("/createCustomer")
       public Response createCust(   @QueryParam ("name")String name,
            @QueryParam("address")String add,
            @QueryParam("email")String email,
            @QueryParam("password")String pass,@Context HttpServletResponse response) throws UnsupportedEncodingException, SQLException, ClassNotFoundException{
           
           Gson gson = new Gson();
           
            
           
//               String name = java.net.URLDecoder.decode(info.getQueryParameters().getFirst("name"));
//        String email = java.net.URLDecoder.decode(info.getQueryParameters().getFirst("email"));
//        String address = java.net.URLDecoder.decode(info.getQueryParameters().getFirst("address"));
//        String password = java.net.URLDecoder.decode(info.getQueryParameters().getFirst("password"),);
//       // String apiKey = java.net.URLDecoder.decode(info.getQueryParameters().getFirst("api_key"), "UTF-8");
//        

       


           try{
        Class.forName("org.apache.derby.jdbc.ClientDriver");   //accounts.status
        conn = DriverManager.getConnection(url, userN, pWord);
        
         String insertCustomer = "INSERT INTO customers"
                        + "(name, email, address, password) VALUES"
                        + "(?,?,?,?)";
         
         PreparedStatement st = conn.prepareStatement(insertCustomer);
                st.setString(1, name);
                st.setString(2, email);
                st.setString(3, add);
                st.setString(4, pass);
              
               int rs = st.executeUpdate();
                
                   if(rs ==1){
              
               return Response.status(200).entity(gson.toJson("Customer added")).build();
          }
          st.close();
          conn.close();
    } catch(Exception e){
    System.out.println(e);
}
  return Response.status(200).entity(gson.toJson("failed")).build();
       
       }
           
            
        
    
    
    
    
           @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response getCustomerById(@PathParam("id") int id) throws SQLException, NamingException, ClassNotFoundException {
          
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        conn = DriverManager.getConnection(url, userN, pWord);
        Gson gson = new Gson(); 
 
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
    
       /*  String createAccount = "INSERT INTO account"
                        + "(sort_code, account_number,account_type, customer_id) VALUES"
                        + "(?,?,?,?)";*/


}//end of class