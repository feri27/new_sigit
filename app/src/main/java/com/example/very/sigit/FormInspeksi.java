package com.example.very.sigit;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.very.sigit.ExpandableAdapter.FormExpandable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class FormInspeksi extends AppCompatActivity {

    private ExpandableListView expandableListView;
    private FormExpandable expandableListViewAdapter;

    private List<String> listDataGroup;
    private HashMap<String, List<String>> listDataChild;

    protected Cursor cursor,cursor1,cursor2;
    private DatabaseHelper db;
    private int id_inspek;
    EditText note;
    private ProgressDialog dialog;
    String reg_number,substation_id,equipment_id,equipment_name,id_location;
    GPSTracker gps;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.form_list_inspeksi);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);

        db = new DatabaseHelper(this);
        Intent nip_user = getIntent();
        reg_number = nip_user.getStringExtra(Server.REG_NUMBER);
        substation_id = nip_user.getStringExtra(Server.SUBSTATION_ID);
        equipment_id = nip_user.getStringExtra(Server.EQUIPMENT_ID);
        equipment_name = nip_user.getStringExtra(Server.EQUIPMENT_NAME);
        id_location = nip_user.getStringExtra(Server.ID_LOCATION);

        View view = getLayoutInflater().inflate(R.layout.no_data, null);
        ViewGroup viewGroup = (ViewGroup) expandableListView.getParent();
        viewGroup.addView(view);
        expandableListView.setEmptyView(view);

        ViewGroup header = (ViewGroup)getLayoutInflater().inflate(R.layout.header_list,expandableListView,false);
        expandableListView.addHeaderView(header);
        TextView eqp_name = header.findViewById(R.id.equipment_name);
        TextView reg_num = header.findViewById(R.id.reg_number);
        TextView sub_id = header.findViewById(R.id.sub_id);
        TextView date = header.findViewById(R.id.date);

        gps = new GPSTracker(FormInspeksi.this);
        gps.canGetLocation();

        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
        final String dateToStr = format.format(gps.getTime());


        final SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
        final String start_time = format1.format(gps.getTime());

        date.setText("DATE : "+dateToStr);
        eqp_name.setText("EQUIPMENT : "+equipment_name);
        reg_num.setText("REG NUMBER : "+reg_number);
        sub_id.setText("SUBSTATION ID : "+substation_id);

        View footer = View.inflate(this, R.layout.footer_list, null);
        expandableListView.addFooterView(footer, null, false);
        note = footer.findViewById(R.id.note);

        Button forward = footer.findViewById(R.id.save);
        Button cancel = footer.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //CEK JAWABAN
                int id = db.GET_CEK_ANSWER(new Location_cursor(""));
                if(id==-1)

                {

                    //DURATION LOGIC
                    gps = new GPSTracker(FormInspeksi.this);
                    gps.canGetLocation();
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                    final String end_time = format.format(gps.getTime());

                    dialog = new ProgressDialog(FormInspeksi.this);
                    dialog.setMessage("Menyimpan Data Inspeksi..");
                    dialog.show();

                    String notes = note.getText().toString();
                    db.addInspection(reg_number,id_location,notes,start_time,end_time,dateToStr,0);
                    cursor = db.GET_ID_INSPECTION();
                    cursor.moveToFirst();
                    int count = cursor.getInt(0);
                    id_inspek = count;
                    SaveForm(id_inspek,equipment_id);
                }
                else
                {
                    Toast_control();
                }


            }
        });



        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()
        {
            public boolean onGroupClick(ExpandableListView arg0, View itemView, int itemPosition, long itemId)
            {
                expandableListView.expandGroup(itemPosition);
                return true;
            }
        });


        listDataGroup = new ArrayList<>();
        listDataChild = new HashMap<>();
        loadData(equipment_id);



    }

    public void SaveForm(int id_inspeksi,String eqp_id){

        //GET DATA FORM CONTROL
        String id = String.valueOf(id_inspeksi);
        cursor2 = db.GET_FORM_CONTROL(new Location_cursor(eqp_id));
        if (cursor2.moveToFirst()) {
            do {

                db.addStoreQuestion(
                cursor2.getString(cursor2.getColumnIndex(DatabaseHelper.ID_QST)),
                cursor2.getString(cursor2.getColumnIndex(DatabaseHelper.QUESTION_ID)),
                cursor2.getString(cursor2.getColumnIndex(DatabaseHelper.EQUIPMENT_ID)),
                cursor2.getString(cursor2.getColumnIndex(DatabaseHelper.ANSWER)),
                cursor2.getString(cursor2.getColumnIndex(DatabaseHelper.SATUAN)),
                cursor2.getString(cursor2.getColumnIndex(DatabaseHelper.CONDITION)),id,0);

            } while (cursor2.moveToNext());


            new CountDownTimer(3000, 1000){
                public void onTick(long millisUntilFinished) {


                }
                @SuppressLint("ResourceAsColor")
                public  void onFinish(){

                    finish();

                }
            }.start();

        }


    }

    public void Toast_control(){

        LayoutInflater inflater = getLayoutInflater();
        final View custom_toast = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.toast_root_view));
        TextView header_toast = (TextView) custom_toast.findViewById(R.id.toast_header);
        header_toast.setText("FORMULIR TIDAK BOLEH KOSONG");
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(custom_toast);
        toast.show();


    }

    private void loadData(String equipment_id) {
        //GET FORM

        cursor = db.GET_EQUIPMENT(new Location_cursor(equipment_id));
        if (cursor.moveToFirst()) {
            do {

                listDataGroup.add(cursor.getString(cursor.getColumnIndex(DatabaseHelper.EQUIPMENT_NAME)));

                List<String> chilData = new ArrayList<>();
                cursor1 = db.GET_QUESTION(new Location_cursor(cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUESTION_ID))));
                if (cursor1.moveToFirst()) {
                    do {

                        //STRORE DEFAUL ANSWER USING ITEM ARRAY
                        String Answer = cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.QUESTION_ANSWER));
                        final List<String> items = Arrays.asList(Answer.split("\\s*,\\s*"));
                        String jawaban = items.get(0);

                        //CREATE DB FORM CONTROL
                        db.addFormControl(cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.ID_QST)),cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUESTION_ID)),equipment_id,jawaban,"","");
                        chilData.add(cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.ID_QST)));

                    } while (cursor1.moveToNext());

                }

                listDataChild.put(listDataGroup.get(cursor.getPosition()), chilData);

            }
            while (cursor.moveToNext());

            expandableListViewAdapter = new FormExpandable(this, listDataGroup, listDataChild);
            expandableListView.setAdapter(expandableListViewAdapter);

        }

    }

}