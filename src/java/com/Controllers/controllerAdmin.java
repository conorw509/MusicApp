/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controllers;

import com.DB.databaseConnection;
import com.encrypted.hash.PasswordEncrypt;
import com.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author conor
 */
public class controllerAdmin {
    
    
     databaseConnection db;
    Connection con;
    PreparedStatement pst;
    
    public controllerAdmin() {
        super();
        db = new databaseConnection();
        con = db.getConnection();
    }      
    
    public boolean checkLogin(User a) {
        
        String sql = "";
        ResultSet rs = null;
        
        try {
            sql = "SELECT * FROM admin1 WHERE email = ? and password = ?";
            pst = con.prepareStatement(sql); 
            
            pst.setString(1, a.getEmail());
            pst.setString(2, PasswordEncrypt.encryptPassword(a.getPassword()));
            
            rs = pst.executeQuery();
            
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
            
            
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        
        
        return false;
    }
    
}
