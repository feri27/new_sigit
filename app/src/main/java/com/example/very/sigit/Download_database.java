package com.example.very.sigit;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Download_database extends AppCompatActivity {

    TextView Log,sync;
    Button finish_btn;
    ProgressBar progres;
    private DatabaseHelper db;
    private static final String TAG = Download_database.class.getSimpleName();

    Context context;
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
    String Unit,key_id;

    SharedPreferences sharedpreferences;
    public static final String my_shared_preferences = "MY_PREFERENCE";
    public static final String session_status = "SESSION_STATUS";
    Boolean session = false;
    int count_1 = 0;
    int count_2 = 0;
    int count_3 = 0;
    int count_4 = 0;
    int count_5 = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_database);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkPermission();
        }

        db = new DatabaseHelper(this);
        Log = (TextView) findViewById(R.id.Log);
        sync = (TextView) findViewById(R.id.sync_text);
        progres = (ProgressBar) findViewById(R.id.bar);
        finish_btn = (Button) findViewById(R.id.btn_finish);

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        key_id = sharedpreferences.getString(Server.ID_UNIT, null);
        if (session) {
            Intent intent = new Intent(Download_database.this, Login.class);
            finish();
            startActivity(intent);
        }

        cehck_user();
    }


    protected void checkPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                + ContextCompat.checkSelfPermission(
                this,Manifest.permission.ACCESS_COARSE_LOCATION)
                + ContextCompat.checkSelfPermission(
                this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){

            // Do something, when permissions not granted
            if(ActivityCompat.shouldShowRequestPermissionRationale(
                    this,Manifest.permission.CAMERA)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this,Manifest.permission.ACCESS_COARSE_LOCATION)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                // If we should give explanation of requested permissions

                // Show an alert dialog here with request explanation
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Camera, Read Contacts and Write External" +
                        " Storage permissions are required to do the task.");
                builder.setTitle("Please grant those permissions");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(
                                Download_database.this,
                                new String[]{
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.ACCESS_COARSE_LOCATION,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                },
                                MY_PERMISSIONS_REQUEST_CODE
                        );
                    }
                });
                builder.setNeutralButton("Cancel",null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                // Directly request for required permissions, without explanation
                ActivityCompat.requestPermissions(
                        Download_database.this,
                        new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        MY_PERMISSIONS_REQUEST_CODE
                );
            }
        }else {
            // Do something, when permissions are already granted
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_CODE:{
                // When request is cancelled, the results array are empty
                if(
                        (grantResults.length >0) &&
                                (grantResults[0]
                                        + grantResults[1]
                                        + grantResults[2]
                                        == PackageManager.PERMISSION_GRANTED
                                )
                ){
                    // Permissions are granted
                }else {
                    // Permissions are denied
                }
                return;
            }
        }
    }

    public void cehck_user(){

        final Dialog download_dialog = new Dialog(this);
        download_dialog.show();
        download_dialog.setContentView(R.layout.dialog_download_db);
        download_dialog.setCancelable(false);

        final Button download = (Button) download_dialog.findViewById(R.id.download);
        final EditText unit = (EditText) download_dialog.findViewById(R.id.id_unit);

        //Action Download Database
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Unit = unit.getText().toString();

                if (Unit.equals("")){
                    Toast.makeText(Download_database.this,"INPUT UNIT NUMBER!!",Toast.LENGTH_SHORT).show();
                }else {

                    Log.append("info : "+"Connecting to : "+Server.URL+"\n");
                    Date today = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    final String dateToStr = format.format(today);
                    Log.append("info : "+"Connection time : "+ dateToStr+"\n");

                    JsonArrayRequest jArr = new JsonArrayRequest(Server.DOWNLOAD_DATA_USER+Unit,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    android.util.Log.d(TAG, response.toString());
                                    if (response.toString().equals("[]")) {

                                        Toast.makeText(Download_database.this,"INCORRECT UNIT NUMBER !!",Toast.LENGTH_SHORT).show();

                                    }
                                    // Parsing json
                                    for (int i = 0; i < response.length(); i++) {
                                        try {
                                            JSONObject obj = response.getJSONObject(i);

                                            download_dialog.dismiss();
                                            Log.setVisibility(View.VISIBLE);
                                            sync.setVisibility(View.VISIBLE);
                                            progres.setVisibility(View.VISIBLE);

                                            count_1 = count_1 + 1;

                                            saveUser(obj.getString("ID_USER"),
                                                    obj.getString("NAME_USER"),
                                                    obj.getString("REGISTRATION_NUMBER"),
                                                    obj.getString("PASSWORD"),
                                                    obj.getString("POSITION"),
                                                    obj.getString("LEVEL"),
                                                    obj.getString("SUBSTATION_ID"),
                                                    obj.getString("SUBSTATION_NAME"));



                                        } catch (JSONException e) {
                                            e.printStackTrace();

                                        }

                                    }

                                    Log.append("info : "+count_1+" | User data downloaded\n");
                                    //Log.append(response.toString());
                                    download_location();

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                            Toast.makeText(Download_database.this,"NO RESPON SERVER!!",Toast.LENGTH_SHORT).show();
                        }
                    });
                    AppController.getInstance().addToRequestQueue(jArr);

                }

            }
        });




    }

    private void download_location(){

        JsonArrayRequest jArr = new JsonArrayRequest(Server.DOWNLOAD_DATA_LOCATION+Unit,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        android.util.Log.d(TAG, response.toString());
                        if (response.toString().equals("[]")) {

                        }
                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);

                                count_2 = count_2 + 1;
                                saveLocation(obj.getString("ID_LOCATION"),
                                        obj.getString("UNIT_ID"),
                                        obj.getString("SUBSTATION_ID"),
                                        obj.getString("LOCATION_NAME"),
                                        obj.getString("EQUIPMENT_ID"),
                                        obj.getDouble("LAT"),
                                        obj.getDouble("LNG"));



                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }

                        Log.append("info : "+count_2+" | Location data downloaded\n");
                        //Log.append(response.toString()+"\n");
                        download_equipment();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(jArr);

    }

    private void download_equipment(){

        JsonArrayRequest jArr = new JsonArrayRequest(Server.DOWNLOAD_DATA_EQUIPMENT,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        android.util.Log.d(TAG, response.toString());
                        if (response.toString().equals("[]")) {

                            Log.append("Error Downloading Equipment Database.....\n");
                        }
                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);

                                count_3 = count_3 + 1;
                                saveEquipment(obj.getString("ID_EQUIPMENT"),
                                        obj.getString("EQUIPMENT_NAME"),
                                        obj.getString("QUESTION_ID"));



                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }

                        Log.append("info : "+count_3+" | Equipment data downloaded\n");
                        //Log.append(response.toString()+"\n");
                        download_question();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(jArr);

    }

    private void download_question(){

        JsonArrayRequest jArr = new JsonArrayRequest(Server.DOWNLOAD_DATA_QUESTION,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        android.util.Log.d(TAG, response.toString());
                        if (response.toString().equals("[]")) {


                        }
                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                count_4 = count_4 + 1;
                                saveQuestion(obj.getString("ID_QST"),
                                        obj.getString("ID_QUESTION"),
                                        obj.getString("QUESTION_NAME"),
                                        obj.getString("QUESTION_TYPE"),
                                        obj.getString("QUESTION_ANSWER"),
                                        obj.getString("SATUAN"),
                                        obj.getString("CONDITION"));


                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }

                        Log.append("info : "+count_4+" | Question Form downloaded \n");
                        //Log.append(response.toString()+"\n");
                        int db_v = count_3+count_4;
                        Log.append("info : "+"DATABASE | V."+db_v+"\n");
                        Log.append("info : "+"Ok!");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }


        });


        AppController.getInstance().addToRequestQueue(jArr);

    }




    private void saveUser(String id_user, String name_user, String registration_number, String password, String position,String level,String substation_id, String substation_name) {

        int id = db.checkUser_download(new User(registration_number,password));
        if(id==-1)

        {
            db.addUser(id_user,name_user,registration_number,password,position,level,substation_id,substation_name);
        }else {
        }


    }

    private void saveLocation(String id_location, String unit_id, String substation_id, String location_name, String equipment_id,Double lat,Double lng) {

            db.addLocation(id_location,unit_id,substation_id,location_name,equipment_id,lat,lng);



    }

    private void saveEquipment(String id_equipment, String equipment_name, String question_id) {

        db.addEquipment(id_equipment,equipment_name,question_id);



    }

    private void saveQuestion(String id_qst,String id_question, String question_name, String question_type,String question_answer,String satuan,String condition) {

        db.addQuestion(id_qst,id_question,question_name,question_type,question_answer,satuan,condition);
        finish_download();


    }

    private void finish_download(){

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(session_status, true);
        editor.putString(Server.ID_UNIT, Unit);
        editor.commit();
        progres.setVisibility(View.GONE);
        finish_btn.setVisibility(View.VISIBLE);
        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Download_database.this, Login.class);
                finish();
                startActivity(intent);

            }
        });

    }

}
