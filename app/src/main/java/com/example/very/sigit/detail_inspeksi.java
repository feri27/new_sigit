package com.example.very.sigit;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.example.very.sigit.DetailAdapter.Detail;
import com.example.very.sigit.DetailAdapter.DetailAdapter;

import java.util.ArrayList;

public class detail_inspeksi extends AppCompatActivity {

    private DatabaseHelper db;
    private ListView List;
    TextView equipment,date,durasi,stat,catatan;
    String id_inspeksi;
    protected Cursor cursor;
    private int counts;
    Button back;
    private java.util.List<Detail> datas;
    private DetailAdapter inspeksiAdapter;

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

        ViewGroup header = (ViewGroup)getLayoutInflater().inflate(R.layout.header_detail,List,false);
        List.addHeaderView(header);

        equipment = header.findViewById(R.id.equipment_name);
        date = header.findViewById(R.id.date);
        durasi = header.findViewById(R.id.durasi);
        stat = header.findViewById(R.id.status);

        Intent intent = getIntent();
        id_inspeksi = intent.getStringExtra(Server.ID_INSPEKSI);
        equipment.setText("EQUIPMENT : "+intent.getStringExtra(Server.EQUIPMENT_NAME));
        date.setText("DATE : "+intent.getStringExtra(Server.DATE));
        durasi.setText(intent.getStringExtra(Server.DURASI));
        stat.setText("STATUS DATA : "+intent.getStringExtra(Server.STATUS));

        View footer = View.inflate(this, R.layout.footer_detail, null);
        List.addFooterView(footer, null, false);
        catatan = footer.findViewById(R.id.note);
        back = footer.findViewById(R.id.back);
        catatan.setText(intent.getStringExtra(Server.NOTE));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loadDetail(id_inspeksi);

    }

    private void loadDetail(String id) {

        Cursor cursor = db.GET_DETAIL_INSPECTION(new Location_cursor(id));
        if (cursor.moveToFirst()) {
            do {
                Detail data = new Detail(
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUESTION_NAME)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.ANSWER)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.SATUAN)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.CONDITION))
                );
                datas.add(data);
            } while (cursor.moveToNext());
        }

        inspeksiAdapter = new DetailAdapter(this, R.layout.data_adapter, datas);
        List.setAdapter(inspeksiAdapter);
    }

    private void refreshList() {
        inspeksiAdapter.notifyDataSetChanged();
    }




}
