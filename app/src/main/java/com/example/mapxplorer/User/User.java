package com.example.mapxplorer.User;

import com.example.mapxplorer.DataBase;
import com.example.mapxplorer.Market;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name,pass,email;
    private ArrayList<Market> markets;
    public User(){
        markets = new ArrayList<>();
    }

    public User(String name, String pass, String email) {
        this.name = name;
        this.pass = pass;
        this.email = email;
        markets = new ArrayList<>();
    }

    public User(String name, String pass, String email, ArrayList<Market> markets) {
        this.name = name;
        this.pass = pass;
        this.email = email;
        this.markets = markets;
    }

    public ArrayList<Market> getMarkets() { return markets; }
    public void setMarkets(ArrayList<Market> markets) { this.markets = markets; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPassword() { return pass; }
    public void setPassword(String pass) { this.pass = pass; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "\nUser{" +
                "\nname='" + name + '\n' +
                ", pass='" + pass + '\n' +
                ", email='" + email + '\n' +
                ", market=" + markets + '\n' +
                '}';
    }
}
