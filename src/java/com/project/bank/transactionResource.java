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
import java.sql.Date;
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
    public Response addTrans(@FormParam("account_id") int account_id, 
            @FormParam("trans_type") String trans_type, 
            @FormParam("amount") Double amount, @Context UriInfo response) throws SQLException, ClassNotFoundException, NamingException {

        System.out.println("account_id: " + account_id);
        System.out.println("trans_type type: " + trans_type);
            System.out.println("amount: " + amount);

        String withdraw = ("withdrawal");
        String lodge = ("lodgment");

        if (trans_type.equals(withdraw) || trans_type.equals(lodge)) {

            try {
                Class.forName("org.apache.derby.jdbc.ClientDriver");   //accounts.status
                conn = DriverManager.getConnection(url, userN, pWord);

                //getting accounr Id id from account table
                String findCusId = ("SELECT (account_number) FROM account WHERE account_number =?");

                PreparedStatement st = conn.prepareStatement(findCusId);
                st.setInt(1, account_id);
                ResultSet rs2 = st.executeQuery();

                int getAccId = 0;
                if (rs2.next()) {
                    //assign got account id from account table to variable
                    getAccId = rs2.getInt(1);
                }

                System.out.println("got accout id:" +getAccId);
               
                //if the id being passed is isnt the same as the one in account table or is null invalid
                if (getAccId != account_id || account_id == 0) {

                    return Response.status(200).entity(gson.toJson("Failed Account ID is invalid")).build();

                }
                
                //Getting balance
                  String getBal = ("SELECT max(balance) from transactions1");
                  Statement sto = conn.createStatement();

             
                ResultSet rs3 = sto.executeQuery(getBal);

                Double gotTheBal =0.0;
                
                if (rs3.next()) {
                    //assign got balance to variable
                    gotTheBal = rs3.getDouble(1);
                }

                System.out.println("got the balance:" +gotTheBal);

            if((trans_type.equals(lodge))){
             
             Double added =0.0;
             added =   gotTheBal + amount;
           
             
            
                
            
                String addLodgment = ("INSERT INTO transactions1(account_id,amount,trans_type,balance) VALUES(?,?,?,?)");

                st = conn.prepareStatement(addLodgment);
               // st.setDate(1, new Date());
                st.setInt(1, account_id);
                  st.setDouble(2, amount);
                st.setString(3, trans_type);
             
                   st.setDouble(4, added);

                st.executeUpdate();

           

                return Response.status(200).entity(gson.toJson("Lodgment made")).build();
            }else{
                if((trans_type.equals(withdraw))){
                   
                
             Double subtract = 0.0;
             subtract = gotTheBal - amount;
           
                String addLodgment = "INSERT INTO transactions1"
                        + "(account_id,amount,trans_type,balance) VALUES"
                        + "(?,?,?,?)";

                st = conn.prepareStatement(addLodgment);
               // st.setDate(1, new Date());
                st.setInt(1, account_id);
          
                 st.setDouble(2, amount);
                    st.setString(3, trans_type);
                   st.setDouble(4, subtract);

                st.executeUpdate();
                   return Response.status(200).entity(gson.toJson("Withdraw made")).build();

                }
            }

            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            return Response.status(200).entity(gson.toJson("failed to add specify transaction type withdrawal or lodgment or some fields were left empty")).build();
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
