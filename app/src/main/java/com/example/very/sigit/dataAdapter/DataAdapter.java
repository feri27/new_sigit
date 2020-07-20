package com.example.very.sigit.dataAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.very.sigit.R;
import com.example.very.sigit.Server;
import com.example.very.sigit.data_inspeksi;
import com.example.very.sigit.detail_inspeksi;
import com.example.very.sigit.menu;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DataAdapter  extends ArrayAdapter<Data> {

    //storing all the names in the list
    private List<Data> datas;
    //context object
    private Context context;

    //constructor
    public DataAdapter(Context context, int resource, List<Data> datas) {
        super(context, resource, datas);
        this.context = context;
        this.datas = datas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listViewItem = inflater.inflate(R.layout.data_adapter, null, true);
        TextView location_name = (TextView) listViewItem.findViewById(R.id.location_name);
        TextView date = (TextView) listViewItem.findViewById(R.id.date);
        final TextView duration = (TextView) listViewItem.findViewById(R.id.duration);
        final TextView status = (TextView) listViewItem.findViewById(R.id.status);
        Button detail = (Button) listViewItem.findViewById(R.id.detail);
        //getting the current name
        final Data data = datas.get(position);

        String time1 = data.getTime_end();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date jam1 = null;
        try {
            jam1 = sdf.parse(time1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String time2 = data.getTime_start();
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
        Date jam2 = null;
        try {
            jam2 = sdf1.parse(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long mills = jam1.getTime() - jam2.getTime();
        int hours = (int) (mills/(1000 * 60 * 60));
        final int mins = (int) ((mills/(1000*60)) % 60);


        location_name.setText(position+1 +". "+data.getLocation_name());
        date.setText("TANGGAL : "+data.getDate()+" "+data.getTime_start());
        duration.setText("DURASI : "+mins+" Menit");


        if (data.getStatus() == 0) {
            status.setText("local");
            status.setBackgroundResource(R.drawable.adapter_status_anomali);
            status.setTextColor(Color.WHITE);

        }else {
            status.setText("server");
            status.setBackgroundResource(R.drawable.adapter_status_normal);
            status.setTextColor(Color.WHITE);

        }

        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, detail_inspeksi.class);
                intent.putExtra(Server.ID_INSPEKSI,data.getId_inspeksi());
                intent.putExtra(Server.EQUIPMENT_NAME,data.getLocation_name());
                intent.putExtra(Server.DATE,data.getDate()+" "+data.getTime_start());
                intent.putExtra(Server.DURASI,duration.getText());
                intent.putExtra(Server.STATUS,status.getText());
                intent.putExtra(Server.NOTE,data.getNote());
                context.startActivity(intent);
            }
        });

        return listViewItem;
    }
}
