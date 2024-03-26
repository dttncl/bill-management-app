package com.example.bill_management_app;

import java.io.Serializable;
import java.util.Date;

public class Bill implements Serializable {
    private int billID;
    private String billerID;
    private int accountNumber;
    private DateModel dateDue;
    private double amount;
    private EnumPaymentStatus status;

    public Bill() {
        billID = 0;
        billerID = "";
        accountNumber = 0;
        dateDue = null;
        amount = 0;
        status = EnumPaymentStatus.Unpaid;
    }

    public Bill(int billID, String billerID, int accountNumber, DateModel dateDue, double amount, EnumPaymentStatus status) {
        this.billID = billID;
        this.billerID = billerID;
        this.accountNumber = accountNumber;
        this.dateDue = dateDue;
        this.amount = amount;
        this.status = status;
    }

    public int getBillID() {
        return billID;
    }

    public void setBillID(int billID) {
        this.billID = billID;
    }

    public String getBillerID() {
        return billerID;
    }

    public void setBillerID(String billerID) {
        this.billerID = billerID;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public DateModel getDateDue() {
        return dateDue;
    }

    public void setDateDue(DateModel dateDue) {
        this.dateDue = dateDue;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public EnumPaymentStatus getStatus() {
        return status;
    }

    public void setStatus(EnumPaymentStatus status) {
        this.status = status;
    }
}
