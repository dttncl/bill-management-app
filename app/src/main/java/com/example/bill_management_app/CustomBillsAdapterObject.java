package com.example.bill_management_app;

public class CustomBillsAdapterObject {

    private Bill oneBill;
    private String billerName;

    public CustomBillsAdapterObject() {
    }

    public Bill getOneBill() {
        return oneBill;
    }

    public void setOneBill(Bill oneBill) {
        this.oneBill = oneBill;
    }

    public String getBillerName() {
        return billerName;
    }

    public void setBillerName(String billerName) {
        this.billerName = billerName;
    }

}
