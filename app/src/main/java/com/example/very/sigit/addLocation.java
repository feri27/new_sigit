package com.example.very.sigit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.very.sigit.LocationSpinner.LocationSpinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class addLocation extends AppCompatActivity {


    private DatabaseHelper db;
    String reg_number, substation_id,id_location;
    GPSTracker gps;
    public int counter;
    TextView count,acuracy,lat,lng;
    Button cancel,save;
    protected Cursor cursor;
    View view;
    LinearLayout Countdown;
    CountDownTimer clock;
    LinearLayout form;
    EditText note;
    Spinner location_bay;
    LocationManager  locationManager;

    private List<LocationSpinner> datas;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_location);
        db = new DatabaseHelper(this);
        datas = new ArrayList<>();

        Intent nip_user = getIntent();
        reg_number = nip_user.getStringExtra(Server.REG_NUMBER);
        substation_id = nip_user.getStringExtra(Server.SUBSTATION_ID);

        count = (TextView) findViewById(R.id.count);
        lat = (TextView) findViewById(R.id.lat);
        lng = (TextView) findViewById(R.id.lng);
        acuracy = (TextView) findViewById(R.id.acuracy);
        cancel = (Button) findViewById(R.id.cancel);
        view = (View) findViewById(R.id.view);
        Countdown = (LinearLayout) findViewById(R.id.countdown_card);
        form = (LinearLayout) findViewById(R.id.form);
        save = (Button) findViewById(R.id.save);
        note = (EditText) findViewById(R.id.note);
        location_bay = (Spinner) findViewById(R.id.spinner);
        //Typeface customfont = Typeface.createFromAsset(getAssets(), "font/SevenSegment.ttf");
        //count.setTypeface(customfont);

        loadLocation(substation_id);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.sendExtraCommand(LocationManager.GPS_PROVIDER,"delete_aiding_data", null);

        location_bay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                id_location = datas.get(position).getId_location();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        clock = new CountDownTimer(62000, 1000) {
            @SuppressLint("ResourceAsColor")
            public void onTick(long millisUntilFinished) {
                count.setText(String.valueOf(counter));
                counter++;

                gps = new GPSTracker(addLocation.this);
                gps.canGetLocation();
                final double latitude = gps.getLatitude();
                final double longitude = gps.getLongitude();

                Double lat1 = latitude;
                Double lng1 = longitude;

                String data = String.format(Locale.ENGLISH, "%f",gps.getAcuracy());
                double amount = Double.parseDouble(data);
                acuracy.setText("AKURASI : "+data+" Meter");
                lat.setText(String.valueOf(lat1));
                lng.setText(String.valueOf(lng1));


                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String latitude = lat.getText().toString();
                        String longitude = lng.getText().toString();
                        String notes = note.getText().toString();

                        if(notes.equals("")){
                            Toast.makeText(addLocation.this,"NOTE BOLEH KOSONG!!",Toast.LENGTH_SHORT).show();
                        }else {
                            db.addRequestLocation(id_location,latitude,longitude,notes,0);
                            Toast.makeText(addLocation.this,"LOKASI BARU TERSIMPAN!!",Toast.LENGTH_SHORT).show();
                            finish();
                        }


                    }
                });


                if (amount<8 && amount>2) {
                    clock.cancel();
                    count.setText("PASS");
                    count.setTextColor(R.color.blue_500);
                    count.setTextSize(50);
                    form.setVisibility(View.VISIBLE);
                    save.setVisibility(View.VISIBLE);

                }


            }

            @SuppressLint("ResourceAsColor")
            public void onFinish() {

                count.setText("NOT\nPASS");
                count.setTextColor(R.color.red);
                count.setTextSize(80);

            }
        };

        clock.start();


    }


    private void loadLocation(String id_gardu) {

        Cursor cursor = db.GET_LOCATION_SPINNER(new Location_cursor(id_gardu));
        if (cursor.moveToFirst()) {
            do {
                LocationSpinner data = new LocationSpinner(
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.ID_LOCATION)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.LOCATION_NAME))
                );
                datas.add(data);
            } while (cursor.moveToNext());
        }

        List<LocationSpinner> list = datas;
        String[] nameList=new String[list.size()];

        for(int i=0;i<list.size();i++){
            nameList[i]=list.get(i).getLocation_name();
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nameList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        location_bay.setAdapter(dataAdapter);
    }


}