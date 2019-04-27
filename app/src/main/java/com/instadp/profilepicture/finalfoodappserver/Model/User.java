package com.instadp.profilepicture.finalfoodappserver.Model;

/**
 * Created by gaurav on 8/2/2018.
 */

public class User {

    String Name;
    String Password;
    String Phone;
    String IsStaff;
    public User(String password, String phone) {
        Password = password;
        Phone = phone;
    }
    public User() {

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }
}
