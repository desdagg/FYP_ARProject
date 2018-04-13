package com.dit.des.arproject;

import com.orm.SugarRecord;


public class GooglePlace  extends SugarRecord {
    private String type;
    private String name;
    private double latitude;
    private double longitude;
    private double altitude;
    private String address;
    private String description;

    // Empty Constructor - Required by SugarORM
    public GooglePlace(){}


    // constructor
    public GooglePlace( String type, String name, double latitude , double longitude , double altitude, String address, String description ) {
        this.type= type;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.address = address;
        this.description = description;
        
    }

    // getter
    public String getPlaceId() { return type;}
    public String getName() { return name; }
    public double getLat() { return latitude; }
    public double getLong() { return longitude; }
    public double getAlt() { return altitude; }
    public String getAddress() { return address; }
    public String getDesc() { return description; }


    // setter
    public void setId(String type) { this.type = type; }
    public void setName(String name) { this.name = name; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public void setAltitude(double altitude) { this.altitude = altitude; }
    public void setAddress(String address) { this.address = address; }
    public void setDescription(String description) { this.description = description; }
}