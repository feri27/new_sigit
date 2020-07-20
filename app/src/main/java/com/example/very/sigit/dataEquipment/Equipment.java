package com.example.very.sigit.dataEquipment;

public class Equipment {
    private String id_location;
    private String location_name;

    public Equipment(String id_location, String location_name) {
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