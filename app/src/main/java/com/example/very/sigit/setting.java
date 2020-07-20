package com.example.very.sigit;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class setting extends AppCompatActivity {

    Button resset,get_loc,location,cancel;
    String reg_number,substation_id;
    private DatabaseHelper db;
    protected Cursor cursor;
    GPSTracker gps;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        db = new DatabaseHelper(this);

        gps = new GPSTracker(setting.this);
        gps.canGetLocation();

        //BUTTON MENU
        resset  = (Button)findViewById(R.id.resset_db);
        get_loc   = (Button) findViewById(R.id.mark_loc);
        location = (Button) findViewById(R.id.daftar_loc);
        cancel = (Button) findViewById(R.id.kembali);

        Intent intent = getIntent();
        reg_number = intent.getStringExtra(Server.REG_NUMBER);
        substation_id = intent.getStringExtra(Server.SUBSTATION_ID);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        resset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog download_dialog = new Dialog(setting.this);
                download_dialog.show();
                download_dialog.setContentView(R.layout.dialog_resset_db);
                download_dialog.setCancelable(false);
                final Button excute = (Button) download_dialog.findViewById(R.id.ok);
                final Button cancel = (Button) download_dialog.findViewById(R.id.no);

                excute.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.GET_DELETE_INSPEKSI();
                        db.GET_DELETE_DETAIL_INSPEKSI();
                        db.GET_DELETE_LOCATION();
                        Toast.makeText(setting.this,"DATA INSPEKSI DI RESSET!",Toast.LENGTH_SHORT).show();
                        download_dialog.hide();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        download_dialog.hide();
                    }
                });
            }

        });

        get_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(setting.this, addLocation.class);
                intent.putExtra(Server.REG_NUMBER, reg_number);
                intent.putExtra(Server.SUBSTATION_ID, substation_id);
                startActivity(intent);
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(setting.this, data_location.class);
                intent.putExtra(Server.SUBSTATION_ID, substation_id);
                startActivity(intent);
            }
        });


    }


}
