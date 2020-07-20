package com.example.very.sigit;

public class Location_cursor {

    //variables
    String substation_id;

    //Parameter constructor containing all three parameters
    public Location_cursor (String substation_id)
    {
        this.substation_id=substation_id;

    }

    public String getSubstation_id() {
        return substation_id;
    }

    public void setSubstation_id(String substation_id) {
        this.substation_id = substation_id;
    }

}