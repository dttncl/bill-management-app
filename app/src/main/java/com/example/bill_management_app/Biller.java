package com.example.bill_management_app;

import java.util.ArrayList;

public class Biller {
    private String billerID;
    private String companyName;
    private ArrayList<Transaction> listOfTransactions;

    public Biller() {
        billerID = "";
        companyName = "";
        listOfTransactions = null;
    }

    public Biller(String billerID, String companyName, ArrayList<Transaction> listOfTransactions) {
        this.billerID = billerID;
        this.companyName = companyName;
        this.listOfTransactions = listOfTransactions;
    }

    public String getBillerID() {
        return billerID;
    }

    public void setBillerID(String billerID) {
        this.billerID = billerID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public ArrayList<Transaction> getListOfTransactions() {
        return listOfTransactions;
    }

    public void setListOfTransactions(ArrayList<Transaction> listOfTransactions) {
        this.listOfTransactions = listOfTransactions;
    }
}
