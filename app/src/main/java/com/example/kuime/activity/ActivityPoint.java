package com.example.kuime.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.kuime.CaApplication;
import com.example.kuime.R;

public class ActivityPoint extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);

        TextView tvPoint = findViewById(R.id.tv_point);
        tvPoint.setText("P " + CaApplication.m_Info.nPoint);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {
                finish();
            }
            break;

        }
    }

    @Override
    public void onBackPressed() {
        finish();

    }
}