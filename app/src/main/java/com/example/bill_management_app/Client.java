package com.example.bill_management_app;

import java.io.Serializable;
import java.util.ArrayList;

public class Client extends User implements Serializable {

    private double credit;
    private ArrayList<String> listOfBills;

    public Client() {
        super();
        credit = 0.00;
        listOfBills = null;
    }

    public Client(String userID, String firstName, String lastName, String email, String phone, String password, EnumUserType type, double credit, ArrayList<String> listOfBills) {
        super(userID, firstName, lastName, email, phone, password, type);
        this.credit = credit;
        this.listOfBills = listOfBills;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public ArrayList<String> getListOfBills() {
        return listOfBills;
    }

    public void setListOfBills(ArrayList<String> listOfBills) {
        this.listOfBills = listOfBills;
    }


}
