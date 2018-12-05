/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.NamingException;

/**
 *
 * @author conor
 */
public class validation {
    
        String url= "jdbc:derby://localhost:1527/bank";
String userN = "root";
String pWord = "Lola.1.2.3";
 Connection conn = null;
    
      public boolean isValidAPI(String api) throws SQLException, NamingException, ClassNotFoundException {
        String verifyAPI = "SELECT * FROM api_keys WHERE api_key = ?";
      Class.forName("org.apache.derby.jdbc.ClientDriver");   //accounts.status
        conn = DriverManager.getConnection(url, userN, pWord);
        PreparedStatement st = conn.prepareStatement(verifyAPI);
        st.setString(1, api);
        ResultSet rs = st.executeQuery();
        boolean isValid = rs.next();
        rs.close();
        return isValid;
    }
    
}
