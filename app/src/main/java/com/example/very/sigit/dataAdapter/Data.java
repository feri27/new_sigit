package com.example.very.sigit.dataAdapter;

public class Data {
    private String id_inspeksi;
    private String location_name;
    private String date;
    private String time_start;
    private String time_end;
    private String note;
    private int status;

    public Data(String id_inspeksi,String location_name,String date,String time_start,String time_end,String note, int status) {
        this.id_inspeksi = id_inspeksi;
        this.location_name = location_name;
        this.date = date;
        this.time_start = time_start;
        this.time_end = time_end;
        this.note = note;
        this.status = status;
    }

    public String getId_inspeksi() {
        return id_inspeksi;
    }
    public String getLocation_name() {
        return location_name;
    }
    public String getDate() {
        return date;
    }
    public String getTime_start() {
        return time_start;
    }
    public String getTime_end() {
        return time_end;
    }

    public String getNote() {
        return note;
    }

    public int getStatus() {
        return status;
    }
}