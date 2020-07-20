package com.example.very.sigit.DetailAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.very.sigit.R;

import java.util.List;

public class DetailAdapter extends ArrayAdapter<Detail> {

    private List<Detail> datas;
    private Context context;

    public DetailAdapter(Context context, int resource, List<Detail> datas) {
        super(context, resource, datas);
        this.context = context;
        this.datas = datas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View listViewItem = inflater.inflate(R.layout.detail_inspeksi, null, true);
        TextView question = (TextView) listViewItem.findViewById(R.id.question);
        TextView answer = (TextView) listViewItem.findViewById(R.id.answer);
        TextView satuan = (TextView) listViewItem.findViewById(R.id.satuan);
        TextView condisi = (TextView) listViewItem.findViewById(R.id.condition);

        Detail data = datas.get(position);
        question.setText(position+1 +". "+data.getQuestion());
        answer.setText(data.getAnswer());
        satuan.setText(data.getSatuan());
        condisi.setText(data.getCondition());
        if (data.getCondition().equals("Normal")){

            condisi.setBackgroundResource(R.drawable.adapter_status_normal);
            condisi.setTextColor(Color.WHITE);
        }else if(data.getCondition().equals("")){

            condisi.setVisibility(View.GONE);

        }else {
            condisi.setBackgroundResource(R.drawable.adapter_status_anomali);
            condisi.setTextColor(Color.WHITE);
        }


        return listViewItem;
    }
}
