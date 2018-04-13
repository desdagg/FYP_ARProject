package com.dit.des.arproject;


import com.orm.SugarRecord;

public class SavedLocation  extends SugarRecord {
    private int user_id;
    private String name;
    private double latitude;
    private double longitude;
    private double altitude;
    private String description;

    // Empty Constructor - Required by SugarORM
    public SavedLocation(){}


    // constructor
    public SavedLocation( String name, int id, double latitude , double longitude , double altitude, String description ) {
        this.user_id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.description = description;

    }

    // getter
    public int getUserId() { return user_id;}
    public String getName() { return name; }
    public double getLat() { return latitude; }
    public double getLong() { return longitude; }
    public double getAlt() { return altitude; }
    public String getDesc() { return description; }


    // setter
    public void setId(int user_id) { this.user_id = user_id; }
    public void setName(String name) { this.name = name; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setPassword(double longitude) { this.longitude = longitude; }
    public void setAltitude(double altitude) { this.altitude = altitude; }
    public void setDescription(String description) { this.description = description; }
}