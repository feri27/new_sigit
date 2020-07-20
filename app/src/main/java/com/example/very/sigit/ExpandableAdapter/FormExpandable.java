package com.example.very.sigit.ExpandableAdapter;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.annotation.DrawableRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.very.sigit.DatabaseHelper;
import com.example.very.sigit.FormInspeksi;
import com.example.very.sigit.Location_cursor;
import com.example.very.sigit.R;
import com.example.very.sigit.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FormExpandable extends BaseExpandableListAdapter {

    private Context context;
    protected Cursor cursor1,cursor;
    private DatabaseHelper db;

    // group titles
    private List<String> listDataGroup;

    // child data in format of header title, child title
    private HashMap<String, List<String>> listDataChild;

    public FormExpandable(Context context, List<String> listDataGroup,
                                     HashMap<String, List<String>> listChildData) {

        this.context = context;
        this.listDataGroup = listDataGroup;
        this.listDataChild = listChildData;

    }


    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataGroup.get(groupPosition))
                .get(childPosititon);

    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {


        final String childText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.form_list_row_child, null);

        }

            final TextView textViewChild = convertView.findViewById(R.id.textViewChild);
            final TextView satuan = convertView.findViewById(R.id.satuan);
            final TextView condisi = convertView.findViewById(R.id.condition);
            final Button option  = convertView.findViewById(R.id.option);

            String number = groupPosition+1 +"."+childPosition+". ";

        db = new DatabaseHelper(context);
            cursor = db.GET_FORM_CONTROL_ID_QST(new Location_cursor(childText));
            if (cursor.moveToFirst()) {
                do {

                            option.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ANSWER)));
                            satuan.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.SATUAN)));
                            condisi.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CONDITION)));



                } while (cursor.moveToNext());


        }db.close();

        db = new DatabaseHelper(context);
        cursor1 = db.GET_QUESTION_ANSWER(new Location_cursor(childText));
        if (cursor1.moveToFirst()) {
            do {


                textViewChild.setText(number+cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.QUESTION_NAME)));
                final String id = cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.ID_QST));


                final int QuestionType = cursor1.getInt(cursor1.getColumnIndex(DatabaseHelper.QUESTION_TYPE));
                String Answer = cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.QUESTION_ANSWER));
                String sat = cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.SATUAN));
                String con = cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.CONDITION));


                final String QuestionName = cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.QUESTION_NAME));
                final List<String> items = Arrays.asList(Answer.split("\\s*,\\s*"));
                final List<String> satuan_lis = Arrays.asList(sat.split("\\s*,\\s*"));
                final List<String> condition_list = Arrays.asList(con.split("\\s*,\\s*"));
                option.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final Dialog download_dialog = new Dialog(context);
                        download_dialog.show();
                        download_dialog.setContentView(R.layout.dialog_option);
                        download_dialog.setCancelable(false);

                        final Button submit = (Button) download_dialog.findViewById(R.id.submit);
                        final RadioGroup condition = (RadioGroup) download_dialog.findViewById(R.id.rg);
                        final RadioGroup rg_satuan = (RadioGroup) download_dialog.findViewById(R.id.rg_satuan);
                        final RadioGroup rg_condition= (RadioGroup) download_dialog.findViewById(R.id.rg_condition);

                        final TextView quest = (TextView) download_dialog.findViewById(R.id.question);
                        final EditText value = (EditText) download_dialog.findViewById(R.id.value);
                        quest.setText(QuestionName);


                        if (QuestionType==1){

                            condition.setVisibility(View.GONE);
                            rg_satuan.setVisibility(View.VISIBLE);
                            rg_satuan.setOrientation(RadioGroup.HORIZONTAL);
                            for (int i = 0; i < satuan_lis.size(); i++) {
                                RadioButton rb = new RadioButton(context);
                                rb.setText(satuan_lis.get(i) + "");
                                rg_satuan.addView(rb);
                                rb.isChecked();
                            }

                            rg_condition.setVisibility(View.VISIBLE);
                            rg_condition.setOrientation(RadioGroup.VERTICAL);
                            for (int i = 0; i < condition_list.size(); i++) {
                                RadioButton rb = new RadioButton(context);
                                rb.setText(condition_list.get(i) + "");
                                rg_condition.addView(rb);
                                rb.isChecked();
                            }

                            submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    int satuan_id = rg_satuan.getCheckedRadioButtonId();
                                    int condition_id = rg_condition.getCheckedRadioButtonId();
                                    RadioButton sat_rb,con_rb;
                                    String text = value.getText().toString();

                                    if (text.equals("")){
                                        Toast toast = Toast.makeText(context,"TIDAK BOLEH KOSONG!", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    }else if(rg_satuan.getCheckedRadioButtonId() == -1){
                                        Toast toast = Toast.makeText(context,"TIDAK BOLEH KOSONG!", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    }else if(rg_condition.getCheckedRadioButtonId() == -1){
                                        Toast toast = Toast.makeText(context,"TIDAK BOLEH KOSONG!", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    }else{

                                        sat_rb = (RadioButton) download_dialog.findViewById(satuan_id);
                                        String satuan_in = sat_rb.getText().toString();

                                        con_rb = (RadioButton) download_dialog.findViewById(condition_id);
                                        String con_in = con_rb.getText().toString();

                                        db.GET_UPDATE_FORM(id, text,satuan_in,con_in);
                                        db.close();
                                        option.setText(text);
                                        download_dialog.hide();
                                    }




                                }
                            });
                        }else if (QuestionType==2) {

                            condition.setVisibility(View.VISIBLE);
                            value.setVisibility(View.GONE);
                            rg_condition.setVisibility(View.GONE);
                            rg_satuan.setVisibility(View.GONE);
                            condition.setOrientation(RadioGroup.VERTICAL);
                            for (int i = 0; i < items.size(); i++) {
                                RadioButton rb = new RadioButton(context);
                                rb.setText(items.get(i) + "");
                                condition.addView(rb);
                                rb.isChecked();
                            }

                            submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    int selectedId = condition.getCheckedRadioButtonId();
                                    RadioButton rb;

                                    //UPDATE FORM CONTROLL RADIOGOUP
                                    if (condition.getCheckedRadioButtonId() == -1){
                                        Toast toast = Toast.makeText(context,"TIDAK BOLEH KOSONG!", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    }else{
                                        rb = (RadioButton) download_dialog.findViewById(selectedId);
                                        String text = rb.getText().toString();
                                        db.GET_UPDATE_FORM(id, text,"","");
                                        db.close();
                                        option.setText(text);
                                        download_dialog.hide();
                                    }
                                }
                            });
                        } else if (QuestionType==3){

                            condition.setVisibility(View.GONE);
                            rg_satuan.setVisibility(View.GONE);
                            rg_condition.setVisibility(View.VISIBLE);
                            rg_condition.setOrientation(RadioGroup.VERTICAL);
                            for (int i = 0; i < condition_list.size(); i++) {
                                RadioButton rb = new RadioButton(context);
                                rb.setText(condition_list.get(i) + "");
                                rg_condition.addView(rb);
                                rb.isChecked();
                            }

                            submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    int condition_id = rg_condition.getCheckedRadioButtonId();
                                    RadioButton con_rb;
                                    String text = value.getText().toString();

                                    if (text.equals("")){
                                        Toast toast = Toast.makeText(context,"TIDAK BOLEH KOSONG!", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    }else if(rg_condition.getCheckedRadioButtonId() == -1 ){
                                        Toast toast = Toast.makeText(context,"TIDAK BOLEH KOSONG!", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    }else{

                                        con_rb = (RadioButton) download_dialog.findViewById(condition_id);
                                        String con_in = con_rb.getText().toString();

                                        db.GET_UPDATE_FORM(id, text,"",con_in);
                                        db.close();
                                        option.setText(text);
                                        download_dialog.hide();
                                    }




                                }
                            });
                        }else if (QuestionType==4){

                            condition.setVisibility(View.GONE);
                            rg_satuan.setVisibility(View.GONE);
                            rg_condition.setVisibility(View.GONE);

                            submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String text = value.getText().toString();

                                    if (text.equals("")){
                                        Toast toast = Toast.makeText(context,"TIDAK BOLEH KOSONG!", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    }else{

                                        db.GET_UPDATE_FORM(id, text,"","");
                                        db.close();
                                        option.setText(text);
                                        download_dialog.hide();
                                    }




                                }
                            });
                        }


                    }
                });


            } while (cursor1.moveToNext());

        }db.close();


        return convertView;
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataGroup.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataGroup.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataGroup.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.form_list_row_group, null);
        }

        TextView textViewGroup = convertView.findViewById(R.id.textViewGroup);
        textViewGroup.setTypeface(null, Typeface.BOLD);
        textViewGroup.setText(groupPosition+1 +". "+headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
