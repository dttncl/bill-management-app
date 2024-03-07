package com.example.bill_management_app;

import java.util.ArrayList;

public class Admin extends User {
    private ArrayList<Client> listOfClients;
    private ArrayList<Biller> listOfBillers;

    public Admin() {
        super();
        listOfClients = null;
        listOfBillers = null;
    }

    public Admin(String userID, String firstName, String lastName, String email, String phone, String password, ArrayList<Client> listOfClients, ArrayList<Biller> listOfBillers) {
        super(userID, firstName, lastName, email, phone, password);
        this.listOfClients = listOfClients;
        this.listOfBillers = listOfBillers;
    }

    public ArrayList<Client> getListOfClients() {
        return listOfClients;
    }

    public void setListOfClients(ArrayList<Client> listOfClients) {
        this.listOfClients = listOfClients;
    }

    public ArrayList<Biller> getListOfBillers() {
        return listOfBillers;
    }

    public void setListOfBillers(ArrayList<Biller> listOfBillers) {
        this.listOfBillers = listOfBillers;
    }
}