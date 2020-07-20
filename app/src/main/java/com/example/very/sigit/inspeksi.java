package com.example.very.sigit;

import android.accessibilityservice.GestureDescription;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class inspeksi extends AppCompatActivity{


    private DatabaseHelper db;
    String reg_number,substation_id;
    GPSTracker gps;
    public int counter;
    TextView count;
    Button cancel;
    protected Cursor cursor;
    View view;
    LinearLayout Countdown;
    CountDownTimer clock;
    LocationManager  locationManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inspeksi);

        db = new DatabaseHelper(this);
        Intent nip_user = getIntent();
        reg_number = nip_user.getStringExtra(Server.REG_NUMBER);
        substation_id = nip_user.getStringExtra(Server.SUBSTATION_ID);

        count = (TextView) findViewById(R.id.count);
        cancel = (Button) findViewById(R.id.cancel);
        view = (View) findViewById(R.id.view);
        Countdown = (LinearLayout) findViewById(R.id.countdown_card);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //DELETE FORM CONTROLL AND DISTANCE
        db.GET_DELETE_FORM();
        db.GET_DELETE_DISTANCE();
        db.close();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.sendExtraCommand(LocationManager.GPS_PROVIDER,"delete_aiding_data", null);

       clock =  new CountDownTimer(62000, 1000){
            public void onTick(long millisUntilFinished){
                count.setText(String.valueOf(counter));
                counter++;

                GET_LOCATION();

            }
            @SuppressLint("ResourceAsColor")
            public  void onFinish(){

                clock.cancel();
                count.setText("NOT\nFOUND");
                count.setTextColor(R.color.red);
                count.setTextSize(80);

            }
        };

       clock.start();

    }

    public void GET_LOCATION(){

        //GET LOCATION
        cursor = db.GET_LOCATION(new Location_cursor(substation_id));
        if (cursor.moveToFirst()) {
            do {

                gps = new GPSTracker(inspeksi.this);
                gps.canGetLocation();
                final double latitude = gps.getLatitude();
                final double longitude = gps.getLongitude();

                Double lat1          = latitude;
                Double lng1          = longitude;
                Double lat2          = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.LAT));
                Double lng2          = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.LNG));
                final String location_name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LOCATION_NAME));
                final String equipment_id  = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EQUIPMENT_ID));
                final String id_location = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ID_LOCATION));

                double theta = lng1 - lng2;
                double dist = Math.sin(deg2rad(lat1))
                        * Math.sin(deg2rad(lat2))
                        + Math.cos(deg2rad(lat1))
                        * Math.cos(deg2rad(lat2))
                        * Math.cos(deg2rad(theta));
                dist = Math.acos(dist);
                dist = rad2deg(dist);
                dist = dist * 60 * 1.1515;

                //Toast.makeText(inspeksi.this,String.valueOf(dist),Toast.LENGTH_SHORT).show();

                if(dist<0.008){

                    //CEK INSPEKSI
                    SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
                    final String dateToStr = format.format(gps.getTime());
                    int id = db.GET_CEK_INSPECTION(new CheckInspeksi_cursor(id_location,dateToStr));
                    if(id==-1)

                    {
                    clock.cancel();
                    db.addDistance(String.valueOf(dist),equipment_id,location_name,id_location);
                    MIN_DISTANCE();

                    }else{


                    }

                }

            } while (cursor.moveToNext());

        }

    }

    public void MIN_DISTANCE(){

        cursor = db.GET_DISTANCE();
        cursor.moveToFirst();
        if (cursor.getCount()>0)
        {
                Intent intent = new Intent(inspeksi.this, FormInspeksi.class);
                intent.putExtra(Server.EQUIPMENT_ID,cursor.getString(0));
                intent.putExtra(Server.SUBSTATION_ID,substation_id);
                intent.putExtra(Server.REG_NUMBER,reg_number);
                intent.putExtra(Server.EQUIPMENT_NAME,cursor.getString(1));
                intent.putExtra(Server.ID_LOCATION,cursor.getString(2));
                startActivity(intent);
                finish();
        }
    }


    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}
