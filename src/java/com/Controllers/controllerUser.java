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
public class controllerUser {
    
    databaseConnection db;
    Connection con;
    PreparedStatement pst;
    
    public controllerUser() {
        super();
        db = new databaseConnection();
        con = db.getConnection();
    }
    
    public int createAccount(User u) {
        int res = 0;
        String sql = "";
        
        try {
            sql = "INSERT INTO users(name,lastname,email,password,repassword) VALUES(?, ?, ?, ?, ?)";
            pst = con.prepareStatement(sql);
            
            pst.setString(1, u.getName());
            pst.setString(2, u.getLastname());
            pst.setString(3, u.getEmail());
            pst.setString(4, PasswordEncrypt.encryptPassword(u.getPassword()));
            pst.setString(5, PasswordEncrypt.encryptPassword(u.getRepassword()));
            
            res = pst.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return res;
    }    
       
    
    public boolean checkLogin(User u) {
        
        String sql = "";
        ResultSet rs = null;
        
        try {
            sql = "SELECT * FROM users WHERE email = ? and password = ?";
            pst = con.prepareStatement(sql);
            
            pst.setString(1, u.getEmail());
            pst.setString(2, PasswordEncrypt.encryptPassword(u.getPassword()));
            
            rs = pst.executeQuery();
            
            if (rs.next()) {
                return true;
            } 
            else {
                return false;
            }
            
            
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        
        
        return false;
    }
    
    
    
    
        public boolean checkEmail(User u) {
        
        String sql = "";
        ResultSet rs = null;
        
        try {
            sql = "SELECT * FROM users WHERE email = ?";
            pst = con.prepareStatement(sql);
            
            pst.setString(1, u.getEmail());
            
            rs = pst.executeQuery();
            
            if (rs.next()) {
                return true;
            } 
            else {
                return false;
            }
            
            
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        
        
        return false;
    }
    
    
    
}
