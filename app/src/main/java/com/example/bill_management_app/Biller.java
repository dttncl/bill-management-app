package com.example.bill_management_app;

import java.io.Serializable;
import java.util.ArrayList;

public class Biller implements Serializable {
    private String billerID;
    private String billerName;
    private ArrayList<String> listOfTransactions;

    public Biller() {
        billerID = "";
        billerName = "";
        listOfTransactions = null;
    }

    public Biller(String billerID, String companyName, ArrayList<String> listOfTransactions) {
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

    public ArrayList<String> getListOfTransactions() {
        return listOfTransactions;
    }

    public void setListOfTransactions(ArrayList<String> listOfTransactions) {
        this.listOfTransactions = listOfTransactions;
    }
}
