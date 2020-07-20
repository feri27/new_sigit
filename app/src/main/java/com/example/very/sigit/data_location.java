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

import com.example.very.sigit.Location.Location;
import com.example.very.sigit.Location.LocationAdapter;

import java.util.ArrayList;

public class data_location extends AppCompatActivity {

    private DatabaseHelper db;
    private ListView List;
    String substation_id;
    protected Cursor cursor;
    private int counts;

    private java.util.List<Location> datas;
    private LocationAdapter inspeksiAdapter;

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
        substation_id = intent.getStringExtra(Server.SUBSTATION_ID);


        ViewGroup header = (ViewGroup)getLayoutInflater().inflate(R.layout.header_equipment,List,false);
        List.addHeaderView(header);
        TextView header_text = header.findViewById(R.id.label);
        header_text.setText("LOKASI TERSIMPAN");

        View footer = View.inflate(this, R.layout.footer_back, null);
        List.addFooterView(footer, null, false);
        Button back = footer.findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loadEquipment(substation_id);

    }


    private void loadEquipment(String id) {
        Cursor cursor = db.GET_DATA_LOCATION(new Location_cursor(id));
        if (cursor.moveToFirst()) {
            do {
                Location data = new Location(
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.ID_LOCATION)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.LOCATION_NAME)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.LAT)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.LNG)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.KETERANGAN)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_STATUS))
                );
                datas.add(data);
            } while (cursor.moveToNext());
        }

        inspeksiAdapter = new LocationAdapter(this, R.layout.data_adapter, datas);
        List.setAdapter(inspeksiAdapter);
    }

    private void refreshList() {
        inspeksiAdapter.notifyDataSetChanged();
    }


}
