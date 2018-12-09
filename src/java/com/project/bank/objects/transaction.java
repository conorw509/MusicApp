/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.bank.objects;

import java.sql.Date;

/**
 *
 * @author conor
 */
public class transaction {

    private int transId;
    private String trans_Type;
    private Date date;
    private int accountId;
    private int amount;
    private String balance;
    
    
    
    
      public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getTransId() {
        return transId;
    }

    public void setTransId(int transId) {
        this.transId = transId;
    }

    public String getTrans_Type() {
        return trans_Type;
    }

    public void setTrans_Type(String trans_Type) {
        this.trans_Type = trans_Type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    

  

}
