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
    private int debit;
    private int credit;
    private Date date;
    private String description;
    private int postBalance;

    public int getTransId() {
        return transId;
    }

    public void setTransId(int transId) {
        this.transId = transId;
    }

    public int getDebit() {
        return debit;
    }

    public void setDebit(int debit) {
        this.debit = debit;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPostBalance() {
        return postBalance;
    }

    public void setPostBalance(int postBalance) {
        this.postBalance = postBalance;
    }
}
