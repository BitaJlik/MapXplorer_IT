package com.example.mapxplorer.User;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

public class Comment {
    private String user;
    private String comment;
    private String email;
    private String date;
    private int warns;

    public Comment(){
        user = "NULL";
        comment = "NULL";
        email = "NULL";
        date = "NULL";
        warns = 0;
    }

    public int getWarns() { return warns; }
    public void setWarns(int warns) { this.warns = warns; }
    public String getUser() { return user; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public void setUser(String user) {
        this.user = user;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
}
