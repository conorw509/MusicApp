/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.bank;

import com.google.gson.Gson;
import com.project.bank.objects.account;
import com.project.bank.objects.customer;

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
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author conor
 */
@Path("/customer")
public class customerResource {

    String url = "jdbc:derby://localhost:1527/bank";
    String userN = "root";
    String pWord = "Lola.1.2.3";
    Connection conn = null;
    int count = 0;

    Gson gson = new Gson();

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
    public ArrayList<customer> getAllCustomers() throws ClassNotFoundException, SQLException {

        ArrayList<customer> arr = new ArrayList<>();

        Class.forName("org.apache.derby.jdbc.ClientDriver");   //accounts.status
        conn = DriverManager.getConnection(url, userN, pWord);
        Statement st = conn.createStatement();
        //query joins two tables account and customers displays all customers by customer id in account and customers table
        ResultSet rs = st.executeQuery("SELECT customers.name, customers.address, customers.email,customers.password FROM customers INNER JOIN account ON customers.customer_id = account.customer_id");

        while (rs.next()) {
            customer ts = new customer();
            ts.setName(rs.getString("name"));
            ts.setAddress(rs.getString("address"));
            ts.setEmail(rs.getString("email"));
            ts.setPassword(rs.getString("password"));

            arr.add(ts);

        }

        return arr;
    }

    @POST
    @Path("/addAccount")
    @Consumes("application/x-www-form-urlencoded")
    public Response addAccount(@FormParam("customer_id") int customer_Id, @FormParam("account_type") String accountType, @Context UriInfo response) throws SQLException, ClassNotFoundException, NamingException {

        System.out.println(" customerid: " + customer_Id);
        System.out.println("account type: " + accountType);

        String savings = ("savings");
        String current = ("current");

        if (accountType.equals(savings) || accountType.equals(current)) {

            try {
                Class.forName("org.apache.derby.jdbc.ClientDriver");   //accounts.status
                conn = DriverManager.getConnection(url, userN, pWord);

                //getting customer id from account table
                String findCusId = ("SELECT (customer_id) from account WHERE customer_id =?");

                PreparedStatement st = conn.prepareStatement(findCusId);
                st.setInt(1, customer_Id);
                ResultSet rs2 = st.executeQuery();

                int getCustomerId = 0;
                if (rs2.next()) {
                    //assign got customer id from account table to variable
                    getCustomerId = rs2.getInt(1);
                }

                System.out.println(getCustomerId);

                //if the id being passed is isnt the same as the one in account table or is null invalid
                if (getCustomerId != customer_Id || customer_Id == 0) {

                    return Response.status(200).entity(gson.toJson("Failed to add new Account customerId is invalid")).build();

                }

                String addAccount = "INSERT INTO account"
                        + "(customer_id,account_type) VALUES"
                        + "(?,?)";

                st = conn.prepareStatement(addAccount);
                st.setInt(1, getCustomerId);
                st.setString(2, accountType);

                st.executeUpdate();

                //inserted into accounts
                System.out.println(addAccount);

                return Response.status(200).entity(gson.toJson("Added Account to Customer")).build();

            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            return Response.status(200).entity(gson.toJson("failed to add specify account type current or savings or some fields were left empty")).build();
        }

        return Response.status(200).entity(gson.toJson("Failed")).build();

    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Path("/createCustomer")
    public Response createCust(@FormParam("name") String name,
            @FormParam("address") String add,
            @FormParam("email") String email,
            @FormParam("password") String pass,
            @FormParam("account_type") String account_type,
            @Context HttpServletResponse response) throws UnsupportedEncodingException, SQLException, ClassNotFoundException {

        System.out.println(account_type);

        String savings = ("savings");
        String current = ("current");

        //check to see if params being passed in is current or savings account if not invalid
        if (account_type.equals(savings) || account_type.equals(current)) {

            try {

                Class.forName("org.apache.derby.jdbc.ClientDriver");   //accounts.status
                conn = DriverManager.getConnection(url, userN, pWord);

                //insert into customer
                String insertCustomer = "INSERT INTO customers"
                        + "(name, email, address, password) VALUES"
                        + "(?,?,?,?)";

                PreparedStatement st = conn.prepareStatement(insertCustomer);
                st.setString(1, name);
                st.setString(2, email);
                st.setString(3, add);
                st.setString(4, pass);

                st.executeUpdate();
                System.out.println("inserted into customer");

                //get max value of the final row in the customer table to get the last entered customer_id
                //and assign to variable 
                int max = 0;
                Statement statement = conn.createStatement();
                ResultSet rs2 = statement.executeQuery("SELECT max(customer_id) from customers");

                if (rs2.next()) {

                    max = rs2.getInt(1);

                }

                System.out.println(max);

                //when max is found we are inserting that into the account table this makes one to many relationship so account is added with the customer
                String insertAccount = "INSERT INTO account(customer_id, account_type) VALUES(?,?)";

                st = conn.prepareStatement(insertAccount);
                st.setInt(1, max);
                st.setString(2, account_type);
                int rs = st.executeUpdate();

                System.out.println("inserted into account");

                if (rs == 1) {
                    return Response.status(200).entity(gson.toJson("Customer added and Account Created")).build();
                }

            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            return Response.status(200).entity(gson.toJson("failed to add specify account type current or savings or some fields were left empty")).build();
        }

        return Response.status(200).entity(gson.toJson("failed to add")).build();

    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces({javax.ws.rs.core.MediaType.APPLICATION_JSON})

    public Response getCustomerById(@FormParam("customer_id") int id) throws SQLException, NamingException, ClassNotFoundException {

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

        } else {
            return Response.status(200).entity(gson.toJson(er)).build();
        }
    }

    @POST
    @Path("/deleteCustomer")
    @Produces("application/json")
    public Response deleteCustomerById(@FormParam("customer_id") int id, //@FormParam("accCusId") int accountCusId, 
            @Context UriInfo info) throws SQLException, NamingException, ClassNotFoundException {

        Gson gson = new Gson();
  

        if (id != 0) {
                  System.out.println("id passed in:" +id);
            try {
                Class.forName("org.apache.derby.jdbc.ClientDriver");   //accounts.status
                conn = DriverManager.getConnection(url, userN, pWord);

                //deleting from customer table
                String deleteCustomer = ("DELETE FROM customers WHERE customer_id =?");

                PreparedStatement st = conn.prepareStatement(deleteCustomer);
                st.setInt(1, id);
                st.executeUpdate();

                String getCustomerAcc = ("SELECT (customer_id) from account WHERE customer_id =?");

                st = conn.prepareStatement(getCustomerAcc);
                st.setInt(1, id);
                ResultSet rs2 = st.executeQuery();
                
             

                int getCustomerId = 0;
                if (rs2.next()) {

                    //assign got customer id from account table to variable
                    getCustomerId = rs2.getInt(1);
                }

                System.out.println("got id: " +getCustomerId);

                //if the id being passed is isnt the same as the one in account table or is null invalid
                if (getCustomerId != id || id == 0) {

                    return Response.status(200).entity(gson.toJson("Failed to delete Customer and Accounts customer Id is invalid")).build();

                }

                String deleteAccount = ("DELETE FROM account WHERE customer_id =?");
                st = conn.prepareStatement(deleteAccount);
                st.setInt(1, getCustomerId);
                int rs = st.executeUpdate();

                //deleted from accounts
                System.out.println(deleteAccount);

              /*  if (rs <= 1) {

                    return Response.status(200).entity(gson.toJson(" Customer and Accounts deleted ")).build();

                }*/

            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            return Response.status(200).entity(gson.toJson("Failed to delete fields were left empty")).build();
        }

     return Response.status(200).entity(gson.toJson(" Customer and Accounts deleted ")).build();

    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    public Response deleteCustomerByIdNoForm(@PathParam("id") int id, @Context UriInfo info) throws SQLException, NamingException, ClassNotFoundException {
        Gson gson = new Gson();
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        conn = DriverManager.getConnection(url, userN, pWord);

        String deleteCustomer = "DELETE FROM customers WHERE customer_id = ?";
        PreparedStatement st = conn.prepareStatement(deleteCustomer);
        st.setInt(1, id);
        int rs = st.executeUpdate();

        if (rs == 1) {

            return Response.status(200).entity(gson.toJson("Account has been removed")).build();

        } else {
            return Response.status(200).entity(gson.toJson("This account has already been removed.")).build();
        }

    }

}//end of class
