package com.example.very.sigit;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.very.sigit.dataAdapter.Data;
import com.example.very.sigit.dataAdapter.DataAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class data_inspeksi  extends AppCompatActivity {

    private DatabaseHelper db;
    private ListView List;
    String nip,petugas;
    private List<Data> datas;
    //a broadcast to know weather the data is synced or not
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";

    //adapterobject for list view
    private DataAdapter inspeksiAdapter;

    TextView filter;
    Button btn_filter;

    protected Cursor cursor;
    Calendar myCalendar = Calendar.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_inspeksi);
        db = new DatabaseHelper(this);
        datas = new ArrayList<>();

        List = (ListView) findViewById(R.id.list);

        View view = getLayoutInflater().inflate(R.layout.no_data, null);
        ViewGroup viewGroup = (ViewGroup) List.getParent();
        viewGroup.addView(view);
        List.setEmptyView(view);

        Intent intent = getIntent();
        nip = intent.getStringExtra(Server.REG_NUMBER);
        petugas = intent.getStringExtra(Server.OFFICER_NAME);

        ViewGroup header = (ViewGroup)getLayoutInflater().inflate(R.layout.header_data,List,false);
        List.addHeaderView(header);

        TextView officer = (TextView)header.findViewById(R.id.officer);
        TextView ofline = (TextView) header.findViewById(R.id.data_offline);
        filter = (TextView) header.findViewById(R.id.filter);
        btn_filter = (Button) header.findViewById(R.id.btn_filter);

        View footer = View.inflate(this, R.layout.footer_back, null);
        List.addFooterView(footer, null, false);
        Button back = footer.findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        cursor = db.getLocalInspection();
        cursor.moveToFirst();
        int  count = cursor.getCount();

        officer.setText("PETUGAS : "+petugas);
        ofline.setText("DATA OFFLINE : "+count);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
               updateLabel();
            }

        };

        btn_filter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(data_inspeksi.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        loadInspeksi();

    }

    private void updateLabel() {
        String myFormat = "dd-MM-YYYY"; //In which you need put here
        SimpleDateFormat tg = new SimpleDateFormat(myFormat, Locale.US);

        String date = "dd"; //In which you need put here
        SimpleDateFormat dt = new SimpleDateFormat(date, Locale.US);
        String tgl = dt.format(myCalendar.getTime());

        String mount = "MM"; //In which you need put here
        SimpleDateFormat mn = new SimpleDateFormat(mount, Locale.US);
        String bln = mn.format(myCalendar.getTime());

        String year = "YYYY"; //In which you need put here
        SimpleDateFormat yr = new SimpleDateFormat(year, Locale.US);
        String thn = yr.format(myCalendar.getTime());

        filter.setText(tgl+"-"+bln+"-"+thn);
        inspeksiAdapter.clear();
        loadInspeksi_filter(thn+"-"+bln+"-"+tgl);

    }


    private void loadInspeksi() {
        Cursor cursor = db.GET_DATA_INSPECTION(new Location_cursor(nip));
        if (cursor.moveToFirst()) {
            do {
                Data data = new Data(
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.ID_INSPECTION)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.LOCATION_NAME)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.DATE_TIME)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.TIME_START)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.TIME_END)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.NOTE)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_STATUS))
                );
                datas.add(data);
            } while (cursor.moveToNext());
        }

        inspeksiAdapter = new DataAdapter(this, R.layout.data_adapter, datas);
        List.setAdapter(inspeksiAdapter);
    }


    private void loadInspeksi_filter(String date) {
        Cursor cursor = db.GET_DATA_INSPECTION_FILTER(new Location_cursor(date));
        if (cursor.moveToFirst()) {
            do {
                Data data = new Data(
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.ID_INSPECTION)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.LOCATION_NAME)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.DATE_TIME)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.TIME_START)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.TIME_END)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.NOTE)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_STATUS))
                );
                datas.add(data);
            } while (cursor.moveToNext());
        }

        inspeksiAdapter = new DataAdapter(this, R.layout.data_adapter, datas);
        List.setAdapter(inspeksiAdapter);
    }

    private void refreshList() {
        inspeksiAdapter.notifyDataSetChanged();
    }




}
