package com.example.very.sigit.dataEquipment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.very.sigit.R;

import java.util.List;

public class EquipmentAdapter extends ArrayAdapter<Equipment> {

    //storing all the names in the list
    private List<Equipment> datas;
    //context object
    private Context context;

    //constructor
    public EquipmentAdapter(Context context, int resource, List<Equipment> datas) {
        super(context, resource, datas);
        this.context = context;
        this.datas = datas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //getting the layoutinflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //getting listview itmes
        View listViewItem = inflater.inflate(R.layout.data_equipment, null, true);
        TextView location_name= (TextView) listViewItem.findViewById(R.id.location_name);

        //getting the current name
        Equipment data = datas.get(position);
        location_name.setText(position+1 +". "+data.getLocation_name());
        return listViewItem;
    }
}
