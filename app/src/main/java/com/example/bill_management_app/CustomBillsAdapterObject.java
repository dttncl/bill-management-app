package com.example.bill_management_app;

public class CustomBillsAdapterObject {
    private String billerName;
    private DateModel dueDate;
    private EnumPaymentStatus status;

    public CustomBillsAdapterObject() {
    }

    public String getBillerName() {
        return billerName;
    }

    public void setBillerName(String billerName) {
        this.billerName = billerName;
    }

    public DateModel getDueDate() {
        return dueDate;
    }

    public void setDueDate(DateModel dueDate) {
        this.dueDate = dueDate;
    }

    public EnumPaymentStatus getStatus() {
        return status;
    }

    public void setStatus(EnumPaymentStatus status) {
        this.status = status;
    }
}
