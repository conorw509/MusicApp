/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.bank.objects;

import java.util.List;

/**
 *
 * @author conor
 */
public class account {
    
  private String account_type;
  private  int account_Number;
  private int customer_id;

   
 
    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public int getAccount_Number() {
        return account_Number;
    }

    public void setAccount_Number(int account_Number) {
        this.account_Number = account_Number;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }
 

  
}
