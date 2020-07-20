package com.example.very.sigit.Location;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.very.sigit.R;
import com.example.very.sigit.dataEquipment.Equipment;

import java.util.List;

public class LocationAdapter  extends ArrayAdapter<Location> {

    private List<Location> datas;
    private Context context;

    public LocationAdapter(Context context, int resource, List<Location> datas) {
        super(context, resource, datas);
        this.context = context;
        this.datas = datas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View listViewItem = inflater.inflate(R.layout.data_location, null, true);
        TextView location_name= (TextView) listViewItem.findViewById(R.id.location_name);
        TextView lat = (TextView) listViewItem.findViewById(R.id.lat);
        TextView lng = (TextView) listViewItem.findViewById(R.id.lng);
        TextView ket = (TextView) listViewItem.findViewById(R.id.ket);
        TextView stat = (TextView) listViewItem.findViewById(R.id.status);

        Location data = datas.get(position);
        location_name.setText(position+1 +". "+data.getLocation_name());
        lat.setText("LAT : "+data.getLat());
        lng.setText("LNG : "+data.getLng());
        ket.setText("KETERANGAN : "+data.getKeterangan());

        if (data.getStatus() == 0) {
            stat.setText("local");
            stat.setBackgroundResource(R.drawable.adapter_status_anomali);
            stat.setTextColor(Color.WHITE);

        }else {
            stat.setText("server");
            stat.setBackgroundResource(R.drawable.adapter_status_normal);
            stat.setTextColor(Color.WHITE);

        }
        return listViewItem;
    }
}