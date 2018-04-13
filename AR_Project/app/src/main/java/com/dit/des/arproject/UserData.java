package com.dit.des.arproject;

import com.orm.SugarRecord;

public class UserData extends SugarRecord {
    private int userId;
    private String name;
    private String email;
    private String password;
    private String created_at;
    private int loggedIn;

    // Empty Constructor - Required by SugarORM
    public UserData(){}


    // constructor
    public UserData(int id, String name, String email , String password , String created_at, int loggedIn) {
        this.userId = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.created_at = created_at;
        this.loggedIn = loggedIn;

    }

   /* public UserData(int id, String name, String email ,String password , int loggedIn) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.created_at = "undefined";
        this.loggedIn = loggedIn;

    }*/

    // getter
    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getCreated_at() { return created_at; }
    public int getLoggedIn() { return loggedIn; }


    // setter
    public void setId(int id) { this.userId = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }
    public void setLoggedIn(int loggedIn) { this.loggedIn = loggedIn; }
}