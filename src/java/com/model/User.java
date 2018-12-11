/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.model;

/**
 *
 * @author conor
 */
public class User {
    
    private int id;
    private String name;
    private String lastname;
    private String email;
    private String password;
    private String repassword;
    
    public void setID(int id) {
        this.id = id;
    }
    
    public int getID() {
        return id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    
    public String getLastname() {
        return lastname;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPassword() {
        return password;
    }
     public void setRepassword(String repassword) {
        this.repassword = repassword;
    }
    
    public String getRepassword() {
        return repassword;
    }

}

