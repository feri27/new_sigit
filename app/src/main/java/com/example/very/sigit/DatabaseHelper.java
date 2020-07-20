package com.example.very.sigit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.PointF;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Created by very on 19/03/2020.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //Constants for Database name, table name, and column names
    public static final String DB_NAME = "larasoft_sigit.db";

    //DATA SYNC STATUS FOR ALL TABLE
    public static final String COLUMN_STATUS = "COLUMN_STATUS";

    //TBL_USER
    public static final String TABEL_USER = "TABEL_USER";
    public static final String ID_USER = "ID_USER";
    public static final String NAME_USER = "NAME_USER";
    public static final String REGISTRATION_NUMBER = "REGISTRATION_NUMBER";
    public static final String PASSWORD = "PASSWORD";
    public static final String POSITION = "POSITION";
    public static final String LEVEL = "LEVEL";
    public static final String SUBSTATION_NAME = "SUBSTATION_NAME";


    //TBL_LOCATION
    public static String TABEL_LOCATION = "TABEL_LOCATION";
    public static String ID_LOCATION = "ID_LOCATION";
    public static String UNIT_ID = "UNIT_ID";
    public static String SUBSTATION_ID = "SUBSTATION_ID";
    public static String LOCATION_NAME = "LOCATION_NAME";
    public static String EQUIPMENT_ID = "EQUIPMENT_ID";
    public static String LAT = "LAT";
    public static String LNG = "LNG";

    //TBL_EQUIPMENT
    public static String TABEL_EQUIPMENT = "TABEL_EQUIPMENT";
    public static String ID_EQUIPMENT = "ID_EQUIPMENT";
    public static String EQUIPMENT_NAME = "EQUIPMENT_NAME";
    public static String QUESTION_ID = "QUESTION_ID";

    //TBL_QUESTION
    public static String TABEL_QUESTION = "TABEL_QUESTION";
    public static String ID_QST = "ID_QST";
    public static String ID_QUESTION = "ID_QUESTION";
    public static String QUESTION_NAME = "QUESTION_NAME";
    public static String QUESTION_TYPE = "QUESTION_TYPE";
    public static String QUESTION_ANSWER = "QUESTION_ANSWER";
    public static String SATUAN = "SATUAN";
    public static String CONDITION = "CONDITION";

    //TBL_FORM_CONTROL
    public static String TABEL_FORM_CONTROL = "TABEL_FORM_CONTROL";
    public static String ANSWER = "ANSWER";

    //TBL_STORE_QUESTION
    public static String TABEL_STORE_QUESTION = "TABEL_STORE_QUESTION";
    public static String ID_STORE_QUESTION = "ID_STORE_QUESTION";


    //TBL_INSPECTION
    public static String TABEL_INSPECTION = "TABEL_INSPECTION";
    public static String ID_INSPECTION = "ID_INSPECTION";
    public static String NOTE = "NOTE";
    public static String TIME_START = "TIME_START";
    public static String TIME_END = "TIME_END";
    public static String DATE_TIME = "DATE_TIME";

    //TBL_REQUEST_LOCATION
    public static String TABEL_REQUEST_LOCATION = "TABEL_REQUEST_LOCATION";
    public static String ID_REQ = "ID_REQ";
    public static String KETERANGAN = "KETERANGAN";

    //TBL_DISTANCE_CONTROL
    public static String TABEL_DISTANCE = "TABEL_DISTANCE";
    public static String ID_DISTANCE = "ID_DISTANCE";
    public  static String DISTANCE = "DISTANCE";


    //database version
    private static final int DB_VERSION = 1;

    //Constructor
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //creating the database
    @Override
    public void onCreate(SQLiteDatabase db) {

        //CREATE TABLE USER
        String tbl_user = "CREATE TABLE " + TABEL_USER
                + "(" + ID_USER+
                " VARCHAR," + NAME_USER +
                " VARCHAR, " + REGISTRATION_NUMBER +
                " VARCHAR,"+ PASSWORD +
                " VARCHAR,"+ POSITION+
                " VARCHAR, " + SUBSTATION_ID +
                " VARCHAR, " + SUBSTATION_NAME +
                " VARCHAR,"+ LEVEL +
                " VARCHAR);";
        db.execSQL(tbl_user);

        //CREATE TABLE LOCATION
        String tbl_location = "CREATE TABLE " + TABEL_LOCATION
                + "(" + ID_LOCATION+
                " VARCHAR, " + UNIT_ID +
                " VARCHAR, " + SUBSTATION_ID +
                " VARCHAR,"+ LOCATION_NAME +
                " VARCHAR,"+ EQUIPMENT_ID +
                " VARCHAR,"+ LAT +
                " DOUBLE,"+ LNG +
                " DOUBLE);";
        db.execSQL(tbl_location);

        //CREATE TABLE EQUIPMENT
        String tbl_equipment = "CREATE TABLE " + TABEL_EQUIPMENT
                + "(" + ID_EQUIPMENT+
                " VARCHAR, " + EQUIPMENT_NAME +
                " VARCHAR, " + QUESTION_ID +
                " VARCHAR);";
        db.execSQL(tbl_equipment);

        //CREATE TABLE QUESTION
        String tbl_question = "CREATE TABLE " + TABEL_QUESTION
                + "(" + ID_QST+
                " VARCHAR, " + ID_QUESTION +
                " VARCHAR, " + QUESTION_NAME +
                " VARCHAR, " + QUESTION_TYPE +
                " VARCHAR,"+ QUESTION_ANSWER +
                " VARCHAR,"+ SATUAN +
                " VARCHAR,"+ CONDITION +
                " VARCHAR);";
        db.execSQL(tbl_question);

        //CREATE TABLE QUESTION
        String tbl_form_control = "CREATE TABLE " + TABEL_FORM_CONTROL
                + "(" + ID_QST+
                " VARCHAR, " + QUESTION_ID+
                " VARCHAR, " + EQUIPMENT_ID +
                " VARCHAR,"+ ANSWER +
                " VARCHAR,"+ SATUAN +
                " VARCHAR,"+ CONDITION +
                " VARCHAR);";
        db.execSQL(tbl_form_control);

        //CREATE TABLE INSPECTION
        String tbl_inspection = "CREATE TABLE " + TABEL_INSPECTION
                + "(" + ID_INSPECTION+
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + REGISTRATION_NUMBER +
                " VARCHAR, " + ID_LOCATION +
                " VARCHAR,"+ NOTE +
                " VARCHAR,"+ TIME_START+
                " VARCHAR,"+ TIME_END+
                " DATE, " + DATE_TIME+
                " VARCHAR, " + COLUMN_STATUS +
                " TINYINT);";
        db.execSQL(tbl_inspection);

        //CREATE TABLE STORE QUESTION
        String tbl_store_question = "CREATE TABLE " + TABEL_STORE_QUESTION
                + "(" + ID_STORE_QUESTION+
                " INTEGER PRIMARY KEY AUTOINCREMENT,  " + ID_QST +
                " VARCHAR, " + QUESTION_ID +
                " VARCHAR,"+ EQUIPMENT_ID +
                " VARCHAR,"+ ANSWER+
                " VARCHAR,"+ SATUAN+
                " VARCHAR,"+ CONDITION+
                " VARCHAR, " + ID_INSPECTION+
                " VARCHAR, " + COLUMN_STATUS +
                " TINYINT);";
        db.execSQL(tbl_store_question);

        //CREATE TABLE REQUEST LOCATION
        String tbl_request_location = "CREATE TABLE " + TABEL_REQUEST_LOCATION
                + "(" + ID_REQ+
                " INTEGER PRIMARY KEY AUTOINCREMENT,  " + ID_LOCATION +
                " VARCHAR, " + LAT +
                " VARCHAR,"+ LNG +
                " VARCHAR,"+ KETERANGAN+
                " VARCHAR, " + COLUMN_STATUS +
                " TINYINT);";
        db.execSQL(tbl_request_location);


        //CREATE TABLE DISTANCE CONTROL
        String tbl_distance = "CREATE TABLE " + TABEL_DISTANCE
                + "(" + ID_DISTANCE+
                " INTEGER PRIMARY KEY AUTOINCREMENT,  " + DISTANCE +
                " VARCHAR, " + EQUIPMENT_ID +
                " VARCHAR,"+ EQUIPMENT_NAME +
                " VARCHAR,"+ ID_LOCATION+
                " VARCHAR);";
        db.execSQL(tbl_distance);

    }

    //upgrading the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS Persons";
        db.execSQL(sql);
        onCreate(db);
    }

    /*
    * This method is taking two arguments
    * first one is the name that is to be saved
    * second one is the status
    * 0 means the name is synced with the server
    * 1 means the name is not synced with the server
    * */

    public boolean addUser(String id_user,String name_user,String registration_number,String password,String position,String level,String substation_id, String substation_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ID_USER, id_user);
        contentValues.put(NAME_USER, name_user);
        contentValues.put(REGISTRATION_NUMBER, registration_number);
        contentValues.put(PASSWORD, password);
        contentValues.put(POSITION, position);
        contentValues.put(SUBSTATION_ID, substation_id);
        contentValues.put(SUBSTATION_NAME, substation_name);
        contentValues.put(LEVEL, level);
        db.insert(TABEL_USER, null, contentValues);
        db.close();
        return true;
    }

    public boolean addLocation(String id_location,String unit_id,String substation_id,String location_name,String equipment_id,double lat,double lng) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_LOCATION, id_location);
        contentValues.put(UNIT_ID, unit_id);
        contentValues.put(SUBSTATION_ID, substation_id);
        contentValues.put(LOCATION_NAME, location_name);
        contentValues.put(EQUIPMENT_ID, equipment_id);
        contentValues.put(LAT, lat);
        contentValues.put(LNG, lng);
        db.insert(TABEL_LOCATION, null, contentValues);
        db.close();
        return true;

    }

    public boolean addEquipment(String id_equipment,String equipment_name,String question_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_EQUIPMENT, id_equipment);
        contentValues.put(EQUIPMENT_NAME, equipment_name);
        contentValues.put(QUESTION_ID, question_id);
        db.insert(TABEL_EQUIPMENT, null, contentValues);
        db.close();
        return true;

    }

    public boolean addQuestion(String id_qst,String id_question,String question_name,String question_type,String question_answer,String satuan, String condition) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_QST, id_qst);
        contentValues.put(ID_QUESTION, id_question);
        contentValues.put(QUESTION_NAME, question_name);
        contentValues.put(QUESTION_TYPE, question_type);
        contentValues.put(QUESTION_ANSWER, question_answer);
        contentValues.put(SATUAN, satuan);
        contentValues.put(CONDITION, condition);
        db.insert(TABEL_QUESTION, null, contentValues);
        db.close();
        return true;

    }

    public boolean addFormControl(String id_qst,String question_id,String equipment_id,String answer, String satuan, String condition) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_QST, id_qst);
        contentValues.put(QUESTION_ID, question_id);
        contentValues.put(EQUIPMENT_ID, equipment_id);
        contentValues.put(ANSWER, answer);
        contentValues.put(SATUAN, satuan);
        contentValues.put(CONDITION, condition);
        db.insert(TABEL_FORM_CONTROL, null, contentValues);
        db.close();
        return true;

    }

    //UPDATE FORM CONTROLL LIST
    public boolean GET_UPDATE_FORM(String id_qst,String answer, String satuan, String condition) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ANSWER, answer);
        contentValues.put(SATUAN, satuan);
        contentValues.put(CONDITION, condition);
        db.update(TABEL_FORM_CONTROL,contentValues,"ID_QST="+"'"+id_qst+"'" ,null);
        db.close();
        return true;

    }

    public boolean addInspection(String reg_number,String id_location,String note, String time_start,String time_end, String date_time, int status) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(REGISTRATION_NUMBER, reg_number);
        contentValues.put(ID_LOCATION, id_location);
        contentValues.put(NOTE, note);
        contentValues.put(TIME_START, time_start);
        contentValues.put(TIME_END, time_end);
        contentValues.put(DATE_TIME, date_time);
        contentValues.put(COLUMN_STATUS, status);

        db.insert(TABEL_INSPECTION, null, contentValues);
        db.close();
        return true;

    }

    public boolean addStoreQuestion(String id_qst,String question_id,String equipment_id, String answer,String satuan, String condition, String id_inspection, int status) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_QST, id_qst);
        contentValues.put(QUESTION_ID, question_id);
        contentValues.put(EQUIPMENT_ID, equipment_id);
        contentValues.put(ANSWER, answer);
        contentValues.put(SATUAN, satuan);
        contentValues.put(CONDITION, condition);
        contentValues.put(ID_INSPECTION, id_inspection);
        contentValues.put(COLUMN_STATUS, status);
        db.insert(TABEL_STORE_QUESTION, null, contentValues);
        db.close();
        return true;

    }

    public boolean addRequestLocation(String id_location,String lat,String lng, String keterangan, int status) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_LOCATION, id_location);
        contentValues.put(LAT, lat);
        contentValues.put(LNG, lng);
        contentValues.put(KETERANGAN, keterangan);
        contentValues.put(COLUMN_STATUS, status);

        db.insert(TABEL_REQUEST_LOCATION, null, contentValues);
        db.close();
        return true;

    }

    public boolean addDistance(String distance,String equipment_id,String equipment_name, String id_location) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DISTANCE, distance);
        contentValues.put(EQUIPMENT_ID, equipment_id);
        contentValues.put(EQUIPMENT_NAME, equipment_name);
        contentValues.put(ID_LOCATION, id_location);

        db.insert(TABEL_DISTANCE, null, contentValues);
        db.close();
        return true;

    }


    public Cursor GET_USER(User user)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c =db.rawQuery("SELECT * FROM  TABEL_USER  WHERE REGISTRATION_NUMBER=? AND PASSWORD=?",new String[]{user.getNumber(),user.getPassword()});
        return c;
    }

    public Cursor GET_LOCATION(Location_cursor cursor)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c =db.rawQuery("SELECT * FROM  TABEL_LOCATION  WHERE SUBSTATION_ID=?",new String[]{cursor.getSubstation_id()});
        return c;
    }

    public Cursor GET_DATA_LOCATION(Location_cursor cursor)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c =db.rawQuery("SELECT TABEL_REQUEST_LOCATION.ID_REQ,TABEL_REQUEST_LOCATION.ID_LOCATION,TABEL_REQUEST_LOCATION.LAT,TABEL_REQUEST_LOCATION.LNG,TABEL_REQUEST_LOCATION.KETERANGAN,TABEL_REQUEST_LOCATION.COLUMN_STATUS,TABEL_LOCATION.LOCATION_NAME,TABEL_LOCATION.ID_LOCATION,TABEL_LOCATION.SUBSTATION_ID FROM TABEL_REQUEST_LOCATION INNER JOIN TABEL_LOCATION ON TABEL_LOCATION.ID_LOCATION=TABEL_REQUEST_LOCATION.ID_LOCATION WHERE TABEL_LOCATION.SUBSTATION_ID = ? ORDER BY ID_REQ DESC",new String[]{cursor.getSubstation_id()});
        return c;
    }

    public Cursor GET_LOCATION_SPINNER(Location_cursor cursor)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c =db.rawQuery("SELECT * FROM  TABEL_LOCATION  WHERE SUBSTATION_ID=?",new String[]{cursor.getSubstation_id()});
        return c;
    }

    public Cursor GET_EQUIPMENT(Location_cursor cursor)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c =db.rawQuery("SELECT * FROM  TABEL_EQUIPMENT  WHERE ID_EQUIPMENT=?",new String[]{cursor.getSubstation_id()});
        return c;
    }

    public Cursor GET_QUESTION(Location_cursor cursor)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c =db.rawQuery("SELECT * FROM  TABEL_QUESTION WHERE ID_QUESTION=?",new String[]{cursor.getSubstation_id()});
        return c;
    }

    public Cursor GET_QUESTION_ANSWER(Location_cursor cursor)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c =db.rawQuery("SELECT * FROM  TABEL_QUESTION WHERE ID_QST=?",new String[]{cursor.getSubstation_id()});
        return c;
    }

    public Cursor GET_FORM_CONTROL(Location_cursor cursor)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c =db.rawQuery("SELECT * FROM TABEL_FORM_CONTROL WHERE EQUIPMENT_ID=?",new String[]{cursor.getSubstation_id()});
        return c;
    }

    public Cursor GET_FORM_CONTROL_ID_QST(Location_cursor cursor)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c =db.rawQuery("SELECT * FROM TABEL_FORM_CONTROL WHERE ID_QST=?",new String[]{cursor.getSubstation_id()});
        return c;
    }

    public Cursor GET_ID_INSPECTION() {
        SQLiteDatabase db = this.getReadableDatabase();
        String tb_inspeksi = "SELECT * FROM "+TABEL_INSPECTION+" WHERE ID_INSPECTION = (SELECT MAX(ID_INSPECTION) FROM TABEL_INSPECTION)";
        Cursor c = db.rawQuery(tb_inspeksi, null);
        return c;
    }

    public Cursor GET_DATA_INSPECTION(Location_cursor cursor)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c =db.rawQuery("SELECT TABEL_INSPECTION.ID_INSPECTION,TABEL_INSPECTION.REGISTRATION_NUMBER,TABEL_INSPECTION.NOTE,TABEL_INSPECTION.DATE_TIME,TABEL_INSPECTION.TIME_START,TABEL_INSPECTION.TIME_END,TABEL_INSPECTION.COLUMN_STATUS,TABEL_LOCATION.ID_LOCATION,TABEL_LOCATION.LOCATION_NAME FROM TABEL_INSPECTION INNER JOIN TABEL_LOCATION ON TABEL_LOCATION.ID_LOCATION=TABEL_INSPECTION.ID_LOCATION WHERE TABEL_INSPECTION.REGISTRATION_NUMBER = ? ORDER BY ID_INSPECTION DESC",new String[]{cursor.getSubstation_id()});
        return c;
    }

    public Cursor GET_DATA_INSPECTION_FILTER(Location_cursor cursor)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c =db.rawQuery("SELECT TABEL_INSPECTION.ID_INSPECTION,TABEL_INSPECTION.REGISTRATION_NUMBER,TABEL_INSPECTION.NOTE,TABEL_INSPECTION.DATE_TIME,TABEL_INSPECTION.TIME_START,TABEL_INSPECTION.TIME_END,TABEL_INSPECTION.COLUMN_STATUS,TABEL_LOCATION.ID_LOCATION,TABEL_LOCATION.LOCATION_NAME FROM TABEL_INSPECTION INNER JOIN TABEL_LOCATION ON TABEL_LOCATION.ID_LOCATION=TABEL_INSPECTION.ID_LOCATION WHERE TABEL_INSPECTION.DATE_TIME = ? ORDER BY ID_INSPECTION DESC",new String[]{cursor.getSubstation_id()});
        return c;
    }

    public Cursor GET_DETAIL_INSPECTION(Location_cursor cursor)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c =db.rawQuery("SELECT TABEL_STORE_QUESTION.ID_INSPECTION,TABEL_STORE_QUESTION.ID_QST,TABEL_STORE_QUESTION.SATUAN,TABEL_STORE_QUESTION.CONDITION,TABEL_STORE_QUESTION.ANSWER,TABEL_QUESTION.ID_QST,TABEL_QUESTION.QUESTION_NAME FROM TABEL_STORE_QUESTION INNER JOIN TABEL_QUESTION ON TABEL_STORE_QUESTION.ID_QST=TABEL_QUESTION.ID_QST WHERE TABEL_STORE_QUESTION.ID_INSPECTION = ?",new String[]{cursor.getSubstation_id()});
        return c;
    }


    public Cursor GET_DISTANCE()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c =db.rawQuery("SELECT EQUIPMENT_ID,EQUIPMENT_NAME,ID_LOCATION,MIN(DISTANCE) FROM TABEL_DISTANCE ORDER BY DISTANCE DESC LIMIT 1",null);
        return c;
    }


    //DELETE FORM CONTROLL LIST

    public void GET_DELETE_DISTANCE()
    {
        SQLiteDatabase db =this.getWritableDatabase();
        db.execSQL("DELETE FROM TABEL_DISTANCE");

    }

    public void GET_DELETE_FORM()
    {
        SQLiteDatabase db =this.getWritableDatabase();
        db.execSQL("DELETE FROM TABEL_FORM_CONTROL");

    }

    public void GET_DELETE_INSPEKSI()
    {
        SQLiteDatabase db =this.getWritableDatabase();
        db.execSQL("DELETE FROM TABEL_INSPECTION");

    }

    //DELETE FORM CONTROLL LIST
    public void GET_DELETE_DETAIL_INSPEKSI()
    {
        SQLiteDatabase db =this.getWritableDatabase();
        db.execSQL("DELETE FROM TABEL_STORE_QUESTION");

    }

    //DELETE LOCATION
    public void GET_DELETE_LOCATION()
    {
        SQLiteDatabase db =this.getWritableDatabase();
        db.execSQL("DELETE FROM TABEL_REQUEST_LOCATION");

    }

    public long COUNT_USER() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABEL_USER);
        db.close();
        return count;
    }

    public long COUNT_LOCATION() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABEL_LOCATION);
        db.close();
        return count;
    }

    public long COUNT_EQUIPMENT() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABEL_EQUIPMENT);
        db.close();
        return count;
    }

    public long COUNT_QUESTION() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABEL_QUESTION);
        db.close();
        return count;
    }

    public Cursor getLocalInspection() {
        SQLiteDatabase db = this.getReadableDatabase();
        String tb_inspeksi = "SELECT * FROM " + TABEL_INSPECTION+ " WHERE " + COLUMN_STATUS + " = 0;";
        Cursor c = db.rawQuery(tb_inspeksi, null);
        return c;
    }

    public Cursor getLocalRequestLocation() {
        SQLiteDatabase db = this.getReadableDatabase();
        String tb_inspeksi = "SELECT * FROM " + TABEL_REQUEST_LOCATION+ " WHERE " + COLUMN_STATUS + " = 0;";
        Cursor c = db.rawQuery(tb_inspeksi, null);
        return c;
    }

    public boolean updateLocalRequestLocation(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATUS, status);
        db.update(TABEL_REQUEST_LOCATION, contentValues, ID_REQ + "=" + id, null);
        db.close();
        return true;

    }

    public boolean updateLocalInspection(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATUS, status);
        db.update(TABEL_INSPECTION, contentValues, ID_INSPECTION + "=" + id, null);
        db.close();
        return true;

    }

    public Cursor getLocalInspectiondetail(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String tb_inspeksi = "SELECT * FROM " + TABEL_STORE_QUESTION+ " WHERE " + COLUMN_STATUS + " = 0 AND "+ID_INSPECTION+"="+id;
        Cursor c = db.rawQuery(tb_inspeksi, null);
        return c;
    }

    public boolean updateLocalInspectiondetail(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATUS, status);
        db.update(TABEL_STORE_QUESTION, contentValues, ID_STORE_QUESTION + "=" + id, null);
        db.close();
        return true;

    }


    /*
    * This method taking two arguments
    * first one is the id of the name for which
    * we have to update the sync status
    * and the second one is the status that will be changed
    * */



    public int LOGIN_USER(User user)
    {
        int id=-1;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT ID_USER FROM TABEL_USER WHERE REGISTRATION_NUMBER=? AND PASSWORD=?",new String[]{user.getNumber(),user.getPassword()});
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            id=cursor.getInt(0);
            cursor.close();
        }
        return id;
    }

    public int GET_CEK_ANSWER(Location_cursor answer)
    {
        int id=-1;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT ID_QST FROM TABEL_FORM_CONTROL WHERE ANSWER=?",new String[]{answer.getSubstation_id()});
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            id=cursor.getInt(0);
            cursor.close();
        }
        return id;
    }

    public int GET_CEK_INSPECTION(CheckInspeksi_cursor answer)
    {
        int id=-1;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT ID_INSPECTION FROM TABEL_INSPECTION WHERE ID_LOCATION=? AND DATE_TIME=?",new String[]{answer.getId_location(),answer.getDate()});
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            id=cursor.getInt(0);
            cursor.close();
        }
        return id;
    }

    public int checkUser_download(User user)
    {
        int id=-1;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT ID_USER FROM TABEL_USER WHERE REGISTRATION_NUMBER=? AND PASSWORD=?",new String[]{user.getNumber(),user.getPassword()});
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            id=cursor.getInt(0);
            cursor.close();
        }
        return id;
    }

}
