package com.example.kuime.activity;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kuime.ActivityLogin;
import com.example.kuime.CaApplication;
import com.example.kuime.CaResult;
import com.example.kuime.IaResultHandler;
import com.example.kuime.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ActivityHome extends AppCompatActivity implements IaResultHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_map: {
                    Intent it = new Intent(this, ActivityMap.class);
                    startActivity(it);
            }
            break;

            case R.id.btn_menu: {
                Intent it = new Intent(this, ActivityMyPage.class);
                startActivity(it);
            }
            break;

            case R.id.cl_charge_history: {
                Intent it = new Intent(this, ActivityChargeHistory.class);
                startActivity(it);
            }
            break;

            case R.id.cl_charge_page: {
                Intent it = new Intent(this, ActivityCharge.class);
                startActivity(it);
            }
            break;

        }
    }


    @Override
    public void onResult(CaResult Result) {

    }
}