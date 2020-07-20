package com.example.very.sigit;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.very.sigit.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.provider.Settings.Secure.LOCATION_MODE_HIGH_ACCURACY;

public class menu extends AppCompatActivity implements LocationAssistant.Listener {

    Button inspeksi, history, btn_sync, equipments,setting,log_out;

    TextView name,reg,position,db_version;

    String reg_number,password,substation_id,petugas;
    private DatabaseHelper db;
    protected Cursor cursor;
    GPSTracker gps;
    private LocationAssistant assistant;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        assistant = new LocationAssistant(this, this, LocationAssistant.Accuracy.HIGH, 5000, false);
        assistant.setVerbose(true);

        db = new DatabaseHelper(this);

        gps = new GPSTracker(menu.this);
        gps.canGetLocation();


        //TEXT VIEW USER ACOUNT
        name     = (TextView) findViewById(R.id.name) ;
        position = (TextView) findViewById(R.id.position);
        db_version = (TextView) findViewById(R.id.db);


        //BUTTON MENU
        inspeksi  = (Button)findViewById(R.id.inspeksi);
        history   = (Button) findViewById(R.id.history);
        btn_sync  = (Button) findViewById(R.id.sync);
        equipments = (Button) findViewById(R.id.equipment);
        setting   = (Button) findViewById(R.id.seting);
        log_out   = (Button) findViewById(R.id.log_out);


        Intent intent = getIntent();
        reg_number = intent.getStringExtra(Server.REG_NUMBER);
        password = intent.getStringExtra(Server.PASSWORD);

        int equipment = (int) db.COUNT_EQUIPMENT();
        int question = (int) db.COUNT_QUESTION();
        int version = equipment+question;
        db_version.setText("DATABASE | V."+version);



        //GET USER ACOUNT
        cursor = db.GET_USER(new User(reg_number,password));
        cursor.moveToFirst();
        if (cursor.getCount()>0)
        {
            cursor.moveToPosition(0);
            name.setText(cursor.getString(1).toString()+" | "+cursor.getString(2).toString());
            petugas = cursor.getString(1).toString();
            position.setText(cursor.getString(4)+" | "+cursor.getString(6));
            substation_id = cursor.getString(5);
        }

        inspeksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(menu.this, inspeksi.class);
                intent.putExtra(Server.REG_NUMBER,reg_number);
                intent.putExtra(Server.SUBSTATION_ID,substation_id);
                startActivity(intent);

            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(menu.this, data_inspeksi.class);
                intent.putExtra(Server.REG_NUMBER,reg_number);
                intent.putExtra(Server.OFFICER_NAME,petugas);
                startActivity(intent);

            }
        });

        equipments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(menu.this, data_equipment.class);
                intent.putExtra(Server.SUBSTATION_ID,substation_id);
                startActivity(intent);
            }
        });

        btn_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(menu.this, sync_inspeksi.class);
                startActivity(intent);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(menu.this, setting.class);
                intent.putExtra(Server.REG_NUMBER,reg_number);
                intent.putExtra(Server.SUBSTATION_ID,substation_id);
                startActivity(intent);
            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(menu.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        assistant.start();
    }

    @Override
    protected void onPause() {
        assistant.stop();
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        assistant.onActivityResult(requestCode, resultCode);
    }


    @Override
    public void onNeedLocationPermission() {

    }

    @Override
    public void onExplainLocationPermission() {

    }

    @Override
    public void onLocationPermissionPermanentlyDeclined(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {

    }

    @Override
    public void onNeedLocationSettingsChange() {

    }

    @Override
    public void onFallBackToSystemSettings(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {

    }

    @Override
    public void onNewLocationAvailable(Location location) {

    }

    @Override
    public void onMockLocationsDetected(View.OnClickListener fromView, DialogInterface.OnClickListener fromDialog) {

        final Dialog erord = new Dialog(this);
        erord.show();
        erord.setContentView(R.layout.activity_dialog_mock);
        erord.setCancelable(false);
        Button dialogButton = (Button) erord.findViewById(R.id.dialogButtonOK);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onError(LocationAssistant.ErrorType type, String message) {

    }
}
