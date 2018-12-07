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
import static javax.ws.rs.HttpMethod.DELETE;
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

    String url = "jdbc:derby://localhost:1527/bank";
    String userN = "root";
    String pWord = "Lola.1.2.3";
    Connection conn = null;
    int count = 0;

    Gson gson = new Gson();

    private boolean isSavingsAccount(String accountNumber) throws SQLException, NamingException, ClassNotFoundException {
        PreparedStatement st;
        Class.forName("org.apache.derby.jdbc.ClientDriver");   //accounts.status
        conn = DriverManager.getConnection(url, userN, pWord);
        st = conn.prepareStatement("SELECT * FROM customers AS c JOIN accounts AS a ON c.customer_id = a.customer_id WHERE account_type = 2 AND account_Id= ?");
        st.setString(1, accountNumber);
        ResultSet rs2 = st.executeQuery();
        boolean isValid = rs2.next();
        conn.close();
        return isValid;
    }

    private boolean isCurrentAccount(String accountNumber) throws SQLException, NamingException, ClassNotFoundException {
        PreparedStatement st;
        Class.forName("org.apache.derby.jdbc.ClientDriver");   //accounts.status
        conn = DriverManager.getConnection(url, userN, pWord);
        st = conn.prepareStatement("SELECT * FROM customers AS c JOIN accounts AS a ON c.customer_id = a.customer_id WHERE account_type = 1 AND account_Id = ?");
        st.setString(1, accountNumber);
        ResultSet rs2 = st.executeQuery();
        boolean isValid = rs2.next();
        conn.close();
        return isValid;
    }

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
        // ResultSet rs = st.executeQuery("SELECT * FROM customers");
        // ResultSet rs = st.executeQuery("SELECT customers.customer_id,customers.name, accounts.customer_id")
        ResultSet rs = st.executeQuery("SELECT customers.name, customers.address, customers.email,customers.password FROM customers INNER JOIN accounts ON customers.customer_id = accounts.customer_id");
        //gets back all customers with same customerID
        while (rs.next()) {
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
    @Path("/addAcc")
    @Consumes("application/x-www-form-urlencoded")
    public Response addAccount(@FormParam("numbers") int numbers) throws SQLException, ClassNotFoundException, NamingException {

        //@FormParam ("account_type")String account_type,@FormParam("accounts")String account, @FormParam("")
        //  accountType = response.getQueryParameters().getFirst("account_type");
        // if (accountType == ("Current") || accountType ==("Student") ) {
        String insertNewAccount = "INSERT INTO test (numbers) VALUES(?)";

        Class.forName("org.apache.derby.jdbc.ClientDriver");
        conn = DriverManager.getConnection(url, userN, pWord);

        PreparedStatement stm = conn.prepareStatement(insertNewAccount);
        stm.setInt(1, numbers);

        int rs = stm.executeUpdate();

        if (rs == 1) {

            return Response.status(200).entity(gson.toJson(" success")).build();

        } //}
        else {

            return Response.status(200).entity(gson.toJson("failed")).build();
        }

    }

    @POST
    @Path("/addAccount")
    @Consumes("application/x-www-form-urlencoded")
    public Response addAccount(@FormParam("sort_code") String sort_code, @FormParam("customer_id") int customer_Id, @FormParam("account_type") String accountType, @Context UriInfo response) throws SQLException, ClassNotFoundException, NamingException {

        //@FormParam ("account_type")String account_type,@FormParam("accounts")String account, @FormParam("")
        accountType = response.getQueryParameters().getFirst("account_type");
        // if (accountType == ("Current") || accountType ==("Student") ) {

        String insertNewAccount = "INSERT INTO accounts (customer_id,sort_code, account_type) VALUES(?,?,?)";

        Class.forName("org.apache.derby.jdbc.ClientDriver");
        conn = DriverManager.getConnection(url, userN, pWord);

        PreparedStatement stm = conn.prepareStatement(insertNewAccount);
        stm.setInt(1, customer_Id);
        stm.setString(2, accountType);
        stm.setString(3, sort_code);

        int rs = stm.executeUpdate();

        if (rs == 1) {

            return Response.status(200).entity(gson.toJson("Account added")).build();

        } //}
        else {

            return Response.status(200).entity(gson.toJson("Not a valid account type please enter Student or Current Account")).build();
        }

    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Path("/createCustomer")
    public Response createCust(@FormParam("name") String name,
            @FormParam("address") String add,
            @FormParam("email") String email,
            @FormParam("password") String pass,
            @FormParam("account_type") String account_type, @Context HttpServletResponse response) throws UnsupportedEncodingException, SQLException, ClassNotFoundException {

        // String insertAcc ="INSERT INTO accounts(customer_id, sort_code, account_number ,account_tyoe) VALUES(?,?,?,?)";
        try {
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

            st.executeUpdate();
            System.out.println("inserted into customer");

            int max = 0;
            Statement statement = conn.createStatement();

            ResultSet rs2 = statement.executeQuery("SELECT max(customer_id) from customers");

            if (rs2.next()) {
                max = rs2.getInt(1);

            }
            
                 String insertAccount = "INSERT INTO accounts"
                    + "(customer_id, account_type) VALUES"
                    + "(?,?)";

            st = conn.prepareStatement(insertAccount);
            st.setInt(1, max);
            st.setString(2, account_type);

          int rs =  st.executeUpdate();
            System.out.println("inserted into account");
            
            if(rs ==1){
           
                return Response.status(200).entity(gson.toJson("Customer and Account Created")).build();
            }
            
            st.close();
            conn.close();

        } catch (Exception e) {
            System.out.println(e);
        }
        return Response.status(200).entity(gson.toJson("failed to add")).build();

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

        } else {
            return Response.status(200).entity(gson.toJson(er)).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    public Response deleteCustomerById(@PathParam("id") int id, @Context UriInfo info) throws SQLException, NamingException, ClassNotFoundException {
        Gson gson = new Gson();
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        conn = DriverManager.getConnection(url, userN, pWord);

        /*  PreparedStatement p = conn.prepareStatement("SELECT cusomer_id from customers where customer_id = ?");
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();*/
        String deleteCustomer = "DELETE FROM customers WHERE customer_id = ?";
        PreparedStatement st = conn.prepareStatement(deleteCustomer);
        st.setInt(1, id);
        int rs = st.executeUpdate();
// int rs = st.executeUpdate();

        if (rs == 1) {

            return Response.status(200).entity(gson.toJson("Account has been removed")).build();

        } else {
            return Response.status(200).entity(gson.toJson("This account has already been removed.")).build();
        }

    }

    @POST
    @Path("/deleteCustomer")
    @Consumes("application/x-www-form-urlencoded")
    public Response deleteCustomerByIdFORM(@FormParam("customer_id") int id, @Context HttpServletResponse response) throws SQLException, NamingException, ClassNotFoundException {
        Gson gson = new Gson();

        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection(url, userN, pWord);

            /*  PreparedStatement p = conn.prepareStatement("SELECT cusomer_id from customers where customer_id = ?");
            p.setInt(1, id);
            ResultSet rs = p.executeQuery();*/
            String deleteCustomer = "DELETE FROM customers WHERE customer_id = ?";
            PreparedStatement st = conn.prepareStatement(deleteCustomer);
            st.setInt(1, id);
            int rs = st.executeUpdate();

            if (rs == 1) {

                return Response.status(200).entity(gson.toJson("Account has been removed")).build();

            }

            st.close();
            conn.close();

        } catch (Exception e) {
            System.out.println(e);
        }

        return Response.status(200).entity(gson.toJson("This account has already been removed.")).build();

    }

}//end of class
