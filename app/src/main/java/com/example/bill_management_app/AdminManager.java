package com.example.bill_management_app;

public class AdminManager {
    private static AdminManager instance;
    private Admin admin;
    private AdminManager() {

    }

    public static synchronized AdminManager getInstance() {
        if (instance == null) {
            instance = new AdminManager();
        }
        return instance;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}
