package com.example.bill_management_app;

import java.io.Serializable;
import java.util.ArrayList;

public class Admin extends User implements Serializable {
    private ArrayList<String> listOfClients;
    private ArrayList<String> listOfBillers;

    public Admin() {
        super();
        listOfClients = null;
        listOfBillers = null;
    }

    public Admin(String userID, String firstName, String lastName, String email, String phone, String password, EnumUserType type, ArrayList<String> listOfClients, ArrayList<String> listOfBillers) {
        super(userID, firstName, lastName, email, phone, password, type);
        this.listOfClients = listOfClients;
        this.listOfBillers = listOfBillers;
    }

    public ArrayList<String> getListOfClients() {
        return listOfClients;
    }

    public void setListOfClients(ArrayList<String> listOfClients) {
        this.listOfClients = listOfClients;
    }

    public ArrayList<String> getListOfBillers() {
        return listOfBillers;
    }

    public void setListOfBillers(ArrayList<String> listOfBillers) {
        this.listOfBillers = listOfBillers;
    }
}
