package com.example.bill_management_app;

import java.util.Date;

public class Transaction {

    private String transactionID;
    private String billerID;
    private int billID;
    private DateModel dateUpdated;
    private double amount;
    private EnumTransactionStatus status;

    public Transaction() {
        transactionID = "";
        billerID = "";
        billID = 0;
        dateUpdated = null;
        amount = 0;
        status = EnumTransactionStatus.Failed;
    }
    public Transaction(String transactionID, String billerID, int billID, DateModel dateUpdated, double amount, EnumTransactionStatus status) {
        this.transactionID = transactionID;
        this.billerID = billerID;
        this.billID = billID;
        this.dateUpdated = dateUpdated;
        this.amount = amount;
        this.status = status;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getBillerID() {
        return billerID;
    }

    public void setBillerID(String billerID) {
        this.billerID = billerID;
    }

    public int getBillID() {
        return billID;
    }

    public void setBillID(int billID) {
        this.billID = billID;
    }

    public DateModel getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(DateModel dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public EnumTransactionStatus getStatus() {
        return status;
    }

    public void setStatus(EnumTransactionStatus status) {
        this.status = status;
    }
}
