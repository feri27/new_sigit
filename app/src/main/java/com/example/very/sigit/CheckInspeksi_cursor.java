package com.example.very.sigit;

public class CheckInspeksi_cursor {

    //variables
    String id_location;
    String date;

    //Parameter constructor containing all three parameters
    public CheckInspeksi_cursor (String id_location, String date)
    {
        this.id_location=id_location;
        this.date=date;

    }

    public String getId_location() {
        return id_location;
    }

    public void setId_location(String id_location) {
        this.id_location = id_location;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}