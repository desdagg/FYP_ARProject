package com.dit.des.arproject;

import com.orm.SugarRecord;

public class CurrentLocation extends SugarRecord{


    private int user_id;
    private String name;
    private double latitude;
    private double longitude;
    private double altitude;
    private String description;



    //Empty constructor
    public CurrentLocation(){}

    public CurrentLocation(String name, int user_id, double user_lat, double user_long, double user_alt, String user_status){
        this.name = name;
        this.user_id = user_id;
        this.latitude = user_lat;
        this.longitude = user_long;
        this.altitude = user_alt;
        this.description = user_status;
    }


    // setter
    public void setId(int user_id) { this.user_id = user_id; }
    public void setName(String name) { this.name = name; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setPassword(double longitude) { this.longitude = longitude; }
    public void setAltitude(double altitude) { this.altitude = altitude; }
    public void setDescription(String description) { this.description = description; }

    // getter
    public int getUserId() { return user_id;}
    public String getName() { return name; }
    public double getLat() { return latitude; }
    public double getLong() { return longitude; }
    public double getAlt() { return altitude; }
    public String getDesc() { return description; }
}
