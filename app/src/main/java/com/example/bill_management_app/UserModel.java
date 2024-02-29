package com.example.bill_management_app;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class UserModel  extends User {

    private List<User> userList;
    private static UserModel instance;

    public UserModel(){
        userList = new ArrayList<>();

        userList.add(new User("1", "Bonnie","Nunes","bonnie@mail.com","+1 438 722 5006","12341234"));
        userList.add(new User("2", "Bernadette","Fernando","bernadette@mail.com","+1 438 722 1234","12341234"));
    }
    public List<User> getUserList(){ return userList; }
    public void setUserList(List<User> userList) {this.userList = userList;}

    public void addUser(User user) {
        userList.add(user);
    }

    public static synchronized UserModel getInstance() {
        if (instance == null) {
            instance = new UserModel();
        }
        return instance;
    }

    public User getUserByEmail(String email) {
        for (User user : userList) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    public boolean isValidCredentials(String email, String password) {

        for (User user : userList) {
            if (email.equals(user.getEmail()) && password.equals(user.getPassword())) {
                return true;
            }
        }
        return false;
    }
}
