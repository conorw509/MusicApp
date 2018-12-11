/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author conor
 */
public class getSecureConnection {
    
    
        Connection con;
    public getSecureConnection(){
        try{
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/bank", "root", "Lola.1.2.3");
        }
        catch(SQLException ex){
            Logger.getLogger(getSecureConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public Connection returnConnection(){
        return con;
    }
    
}
