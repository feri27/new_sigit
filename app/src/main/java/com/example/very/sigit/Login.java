package com.example.very.sigit;

import android.Manifest;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class Login extends AppCompatActivity {

    Button login_btn;
    EditText nip,password;
    private DatabaseHelper db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        db = new DatabaseHelper(this);

        login_btn = (Button) findViewById(R.id.login_btn);
        nip = (EditText) findViewById(R.id.nip_petugas);
        password = (EditText) findViewById(R.id.password);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String reg_number = nip.getText().toString();
                String password_user = password.getText().toString();

                int id = db.LOGIN_USER(new User(reg_number,password_user));
                if(id==-1)

                {
                    Toast.makeText(Login.this,"INCORRECT USER OR PASSWORD!!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(Login.this, menu.class);
                    intent.putExtra(Server.REG_NUMBER,reg_number);
                    intent.putExtra(Server.PASSWORD,password_user);
                    startActivity(intent);
                    finish();

                }

            }
        });

    }

}
