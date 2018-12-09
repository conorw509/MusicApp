/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.bank;

import com.google.gson.Gson;
import com.project.bank.objects.account;
import com.project.bank.objects.customer;
import com.project.bank.objects.transaction;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import static org.eclipse.persistence.sessions.SessionProfiler.Transaction;

/**
 *
 * @author conor
 */
@Path("/trans")
public class transactionResource {
    
     String url = "jdbc:derby://localhost:1527/bank";
    String userN = "root";
    String pWord = "Lola.1.2.3";
    Connection conn = null;
    int count = 0;

    Gson gson = new Gson();
    
    
    
      @GET //done
    @Path("/getAllAccounts")
    @Produces({javax.ws.rs.core.MediaType.APPLICATION_JSON})
    public ArrayList<account> getAllAccounts() throws ClassNotFoundException, SQLException {

        ArrayList<account> arr = new ArrayList<>();

        Class.forName("org.apache.derby.jdbc.ClientDriver");   //accounts.status
        conn = DriverManager.getConnection(url, userN, pWord);
        Statement st = conn.createStatement();
        //query joins two tables account and customers displays all customers by customer id in account and customers table
        ResultSet rs = st.executeQuery("SELECT * FROM account");

        while (rs.next()) {
            account ts = new account();
            ts.setCustomer_id(rs.getInt("customer_id"));
            ts.setAccount_Number(rs.getInt("account_number"));
            ts.setAccount_type(rs.getString("account_type"));
         
              arr.add(ts);
            

        }

        return arr;
    }
   
    
     @GET
    @Path("/getAllTrans") //done
    @Produces({javax.ws.rs.core.MediaType.APPLICATION_JSON})
    public ArrayList<transaction> getAllTrans() throws ClassNotFoundException, SQLException {

        ArrayList<transaction> arr = new ArrayList<>();

        Class.forName("org.apache.derby.jdbc.ClientDriver");   //accounts.status
        conn = DriverManager.getConnection(url, userN, pWord);
        Statement st = conn.createStatement();
        //query joins two tables account and customers displays all customers by customer id in account and customers table
        ResultSet rs = st.executeQuery("SELECT * FROM transactions1");

        while (rs.next()) {
            transaction ts = new transaction();
            ts.setTrans_Type(rs.getString("trans_type"));
            ts.setDate(rs.getDate("date_Ob"));
              ts.setAccountId(rs.getInt("account_Id"));
               ts.setAmount(rs.getInt("amount"));
               ts.setBalance(rs.getString("balance"));
         
            
         
              arr.add(ts);
            

        }

        return arr;
    }
    
    
     @POST
    @Path("/addTransaction")
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
    
    
     @Path("/getbalance")
    @POST
    @Consumes("application/x-www-form-urlencoded") //done
    @Produces({javax.ws.rs.core.MediaType.TEXT_PLAIN})

    public Response getAccountBal(@FormParam("account_id") int id) throws SQLException, NamingException, ClassNotFoundException {

        Class.forName("org.apache.derby.jdbc.ClientDriver");
        conn = DriverManager.getConnection(url, userN, pWord);
        Gson gson = new Gson();

        String er = "Error The account has been removed,doesnt exist or you have not made a transaction";
        String verifyAPI = "SELECT balance FROM transactions1 WHERE account_id = ?";
        PreparedStatement st = conn.prepareStatement(verifyAPI);
        st.setInt(1, id);
        ResultSet rs = st.executeQuery();
        List events = new ArrayList<>();
        if (rs.next()) {
            transaction acc = new transaction();
            acc.setBalance(rs.getString("balance"));
            events.add(acc);
            conn.close();
            return Response.status(200).entity(gson.toJson(events)).build();

        } else {
            return Response.status(200).entity(gson.toJson(er)).build();
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}//end of class
