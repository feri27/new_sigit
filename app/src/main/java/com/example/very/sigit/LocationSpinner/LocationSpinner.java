package com.example.very.sigit.LocationSpinner;

public class LocationSpinner {
    private String id_location;
    private String location_name;

    public LocationSpinner(String id_location, String location_name) {
        this.id_location = id_location;
        this.location_name = location_name;
    }

    public String getId_location() {
        return id_location;
    }

    public String getLocation_name() {
        return location_name;
    }

}