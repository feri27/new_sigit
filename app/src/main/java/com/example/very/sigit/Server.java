package com.example.very.sigit;

public class Server {

    //END POINT API
    public static final String URL = "http://10.43.203.30/SIGIT_API/";
    public static final String DOWNLOAD_DATA_USER = Server.URL+"GetData/user?id_unit=";
    public static final String DOWNLOAD_DATA_LOCATION = Server.URL+"GetData/location?id_unit=";
    public static final String DOWNLOAD_DATA_EQUIPMENT = Server.URL+"GetData/equipment";
    public static final String DOWNLOAD_DATA_QUESTION = Server.URL+"GetData/question";
    public static final String SAVE_INSPEKSI = Server.URL+"GetData/inspeksi";
    public static final String SAVE_DETAIL_INSPEKSI = Server.URL+"GetData/detail_inspeksi";
    public static final String REQUEST_LOCATION = Server.URL+"GetData/request_location";


    //TAG KEY
    public final static String ID_UNIT = "ID_UNIT";
    public final static String REG_NUMBER = "reg_number";
    public final static String PASSWORD = "password";
    public final static String SUBSTATION_ID = "SUBSTATION_ID";
    public final static String EQUIPMENT_ID = "EQUIPMENT_ID";
    public final static String EQUIPMENT_NAME = "EQUIPMENT_NAME";
    public final static String ID_LOCATION = "ID_LOCATION";
    public final static String OFFICER_NAME = "OFFICER_NAME";
    public final static String DURASI = "DURASI";
    public final static String DATE = "DATE";
    public final static String STATUS = "STATUS";
    public final static String ID_INSPEKSI = "ID_INSPEKSI";
    public final static String NOTE = "NOTE";


}
