package com.example.bill_management_app;

import java.io.Serializable;
import java.util.ArrayList;

public class Biller implements Serializable {
    private String billerID;
    private String billerName;
    private ArrayList<Transaction> listOfTransactions;

    public Biller() {
        billerID = "";
        billerName = "";
        listOfTransactions = null;
    }

    public Biller(String billerID, String companyName, ArrayList<Transaction> listOfTransactions) {
        this.billerID = billerID;
        this.billerName = companyName;
        this.listOfTransactions = listOfTransactions;
    }

    public String getBillerID() {
        return billerID;
    }

    public void setBillerID(String billerID) {
        this.billerID = billerID;
    }

    public String getBillerName() {
        return billerName;
    }

    public void setBillerName(String billerName) {
        this.billerName = billerName;
    }

    public ArrayList<Transaction> getListOfTransactions() {
        return listOfTransactions;
    }

    public void setListOfTransactions(ArrayList<Transaction> listOfTransactions) {
        this.listOfTransactions = listOfTransactions;
    }
}
