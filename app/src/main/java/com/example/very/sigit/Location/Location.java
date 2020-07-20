package com.example.very.sigit.Location;

public class Location {
    private String id_location;
    private String location_name;
    private String lat;
    private String lng;
    private String keterangan;
    private int status;

    public Location(String id_location, String location_name, String lat,String lng,String keterangan,int status) {
        this.id_location = id_location;
        this.location_name = location_name;
        this.lat = lat;
        this.lng = lng;
        this.keterangan = keterangan;
        this.status = status;
    }

    public String getId_location() {
        return id_location;
    }

    public String getLocation_name() {
        return location_name;
    }

    public String getLat(){
        return lat;
    }

    public  String getLng(){
        return lng;
    }

    public String getKeterangan(){
        return keterangan;
    }

    public int getStatus(){
        return status;
    }
}