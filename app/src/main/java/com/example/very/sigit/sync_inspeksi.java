package com.example.very.sigit;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

public class sync_inspeksi extends AppCompatActivity {

    Button sync_inspeksi,cancel;
    TextView log;
    private DatabaseHelper db;
    RequestQueue requestQueue;
    public static final int SERVER = 1;
    boolean check = true;
    protected Cursor cursor;
    ScheduledThreadPoolExecutor myTimer;
    ProgressBar prog;
    private int counts;
    private int counts_loc;
    private int counts_detail;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sync_inspeksi);
        db = new DatabaseHelper(this);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        sync_inspeksi = (Button) findViewById(R.id.sync_btn);
        cancel = (Button) findViewById(R.id.kembali);
        log = (TextView) findViewById(R.id.log);
        prog = (ProgressBar) findViewById(R.id.prog);

        sync_inspeksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                connection();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }


    public void connection() {

        log.append("info : "+"Connecting to : "+Server.URL+"\n");
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        final String dateToStr = format.format(today);
        log.append("info : "+"Connection time : "+ dateToStr+"\n");
        myTimer = new ScheduledThreadPoolExecutor(1);
        myTimer.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                try {
                    HttpURLConnection.setFollowRedirects(false);
                    HttpURLConnection con =  (HttpURLConnection) new URL(Server.URL).openConnection();
                    con.setRequestMethod("HEAD");
                    System.out.println(con.getResponseCode());

                    if(con.getResponseCode() == HttpURLConnection.HTTP_OK){

                       log.append("info : "+"OK\n");
                       myTimer.shutdownNow();

                    }else{

                        log.append("info : "+"Connection : Error\n");

                    }

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            myTimer.shutdown();
                            sync_inspeksi.setEnabled(false);
                            cancel.setVisibility(View.VISIBLE);
                            sync_inspeksi.setBackgroundResource(R.drawable.btn_orange);
                            Toast.makeText(sync_inspeksi.this,"SINKRONISASI SELESAI!",Toast.LENGTH_SHORT).show();
                        }

                    });
                }
                catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

            }
        }, 0,4000, TimeUnit.MILLISECONDS);

        cursor = db.getLocalInspection();
        cursor.moveToFirst();
        int count = cursor.getCount();
        counts = count;

        if (counts==0){

            sync_inspeksi.setEnabled(false);
            sync_inspeksi.setBackgroundResource(R.drawable.btn_disable);
            log.append("info : Data ("+counts+") tidak ada data untuk di sinkronisasi..\n");

        }else {

            send();
        }

        cursor = db.getLocalRequestLocation();
        cursor.moveToFirst();
        int count_loc = cursor.getCount();
        counts_loc = count_loc;

        if (counts_loc==0){

        }else {

            kirim_location();
        }

    }

    private void send (){

        cancel.setVisibility(View.GONE);
        sync_inspeksi.setEnabled(false);
        sync_inspeksi.setBackgroundResource(R.drawable.btn_disable);
        Cursor cursor = db.getLocalInspection();
        if (cursor.moveToFirst()) {
            do {
                saveData(
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ID_INSPECTION)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.REGISTRATION_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.ID_LOCATION)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.NOTE)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.TIME_START)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.TIME_END)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.DATE_TIME))
                );
            } while (cursor.moveToNext());

            log.append("info : "+"Mengirim "+cursor.getCount()+" data inspeksi..\n");

        }

    }



    private void saveData(final int id_inspeksi,final String registration_number, final String id_location, final String note, final String time_start, final String time_end, final String date_time) {

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onPostExecute(String string1) {
                super.onPostExecute(string1);

                //Toast.makeText(sync_inspeksi.this,string1,Toast.LENGTH_SHORT).show();

                if (string1.equals("")){

                    log.append("info : "+"Gagal terhubung dengan Server\n");

                }else {

                    log.append("info : "+"=================================\n");
                    log.append("info : "+"ID_INSPEKSI : "+string1+"\n");

                    db.updateLocalInspection(id_inspeksi, SERVER);
                    kirim_detail(id_inspeksi,string1);

                }

            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();
                HashMap<String,String> parameters = new HashMap<String,String>();

                parameters.put("REGISTRATION_NUMBER", registration_number);
                parameters.put("ID_LOCATION", id_location);
                parameters.put("NOTE", note);
                parameters.put("TIME_START", time_start);
                parameters.put("TIME_END", time_end);
                parameters.put("DATE_TIME",date_time);

                String FinalData = imageProcessClass.ImageHttpRequest(Server.SAVE_INSPEKSI, parameters);
                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
    }


    public void kirim_detail(int id_inspeksi,String id_server) {

        Cursor cursor = db.getLocalInspectiondetail(id_inspeksi);
        if (cursor.moveToFirst()) {
            do {
                saveDetail(
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ID_STORE_QUESTION)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.ID_QST)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUESTION_ID)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.EQUIPMENT_ID)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.ANSWER)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.SATUAN)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.CONDITION)),id_server
                );
            } while (cursor.moveToNext());

            log.append("info : "+"Mengirim "+cursor.getCount()+" baris detail inspeksi..\n");
            log.append("info : "+"Terkirim.\n");
        }

    }


    private void saveDetail(final int id_store_question,final String id_qst, final String question_id, final String equipment_id, final String answer, final String satuan,final String condition,final String id_server) {

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onPostExecute(String string1) {
                super.onPostExecute(string1);

                if (string1.equals("ok")){
                    db.updateLocalInspectiondetail(id_store_question, SERVER);
                }else {
                    log.append("info : "+"Gagal.\n");
                }

            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass ProcessClass = new ImageProcessClass();
                HashMap<String,String> parameters = new HashMap<String,String>();

                parameters.put("ID_QST", id_qst);
                parameters.put("QUESTION_ID", question_id);
                parameters.put("EQUIPMENT_ID", equipment_id);
                parameters.put("ANSWER", answer);
                parameters.put("SATUAN", satuan);
                parameters.put("CONDITION", condition);
                parameters.put("ID_INSPEKSI",id_server);

                String FinalData = ProcessClass.ImageHttpRequest(Server.SAVE_DETAIL_INSPEKSI, parameters);
                return FinalData;
            }


        }


        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

        AsyncTaskUploadClassOBJ.execute();
    }

    public void kirim_location() {

        Cursor cursor = db.getLocalRequestLocation();
        if (cursor.moveToFirst()) {
            do {
                saveLocation(
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ID_REQ)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.ID_LOCATION)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.LAT)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.LNG)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.KETERANGAN))
                );
            } while (cursor.moveToNext());

            log.append("info : "+"Mengirim "+cursor.getCount()+" request location\n");
            log.append("info : "+"Terkirim.\n");

        }
    }

    private void saveLocation(final int id_req, final String id_location,final String lat, final String lng, final String keterangan) {

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onPostExecute(String string1) {
                super.onPostExecute(string1);

                if (string1.equals("ok")) {
                    db.updateLocalRequestLocation(id_req, SERVER);
                }else {
                    log.append("info : "+"Gagal.\n");
                }

            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass ProcessClass = new ImageProcessClass();
                HashMap<String,String> parameters = new HashMap<String,String>();

                parameters.put("ID_LOCATION", id_location);
                parameters.put("LAT", lat);
                parameters.put("LNG", lng);
                parameters.put("KETERANGAN", keterangan);

                String FinalData = ProcessClass.ImageHttpRequest(Server.REQUEST_LOCATION, parameters);
                return FinalData;
            }


        }

        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProcessClass{

        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {

                URL url;
                HttpURLConnection httpURLConnectionObject ;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject ;
                BufferedReader bufferedReaderObject ;
                int RC ;

                url = new URL(requestURL);

                httpURLConnectionObject = (HttpURLConnection) url.openConnection();

                httpURLConnectionObject.setReadTimeout(19000);

                httpURLConnectionObject.setConnectTimeout(19000);

                httpURLConnectionObject.setRequestMethod("POST");

                httpURLConnectionObject.setDoInput(true);

                httpURLConnectionObject.setDoOutput(true);

                OutPutStream = httpURLConnectionObject.getOutputStream();

                bufferedWriterObject = new BufferedWriter(

                        new OutputStreamWriter(OutPutStream, "UTF-8"));

                bufferedWriterObject.write(bufferedWriterDataFN(PData));

                bufferedWriterObject.flush();

                bufferedWriterObject.close();

                OutPutStream.close();

                RC = httpURLConnectionObject.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReaderObject.readLine()) != null){

                        stringBuilder.append(RC2);
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();

        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            StringBuilder stringBuilderObject;

            stringBuilderObject = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {

                if (check)

                    check = false;
                else
                    stringBuilderObject.append("&");

                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilderObject.append("=");

                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }
            return stringBuilderObject.toString();
        }



    }


}
