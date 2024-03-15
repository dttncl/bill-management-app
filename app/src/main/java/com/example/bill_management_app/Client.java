package com.example.bill_management_app;

import java.io.Serializable;
import java.util.ArrayList;

public class Client extends User implements Serializable {

    private double credit;
    private ArrayList<Bill> listOfBills;

    public Client() {
        super();
        credit = 0;
        listOfBills = null;
    }

    public Client(String userID, String firstName, String lastName, String email, String phone, String password, EnumUserType type, double credit, ArrayList<Bill> listOfBills) {
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

    public ArrayList<Bill> getListOfBills() {
        return listOfBills;
    }

    public void setListOfBills(ArrayList<Bill> listOfBills) {
        this.listOfBills = listOfBills;
    }


}
