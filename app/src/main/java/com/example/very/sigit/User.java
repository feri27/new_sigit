package com.example.very.sigit;

//this is model class
public class User {

    //variables
    String number;
    String password;

    //Parameter constructor containing all three parameters
    public User(String number,String password)
    {
        this.number=number;
        this.password=password;

    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}