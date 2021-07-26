package com.example.kuime.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kuime.R;

public class ActivityReserveConnect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_connect);
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {
                finish();

            }
            break;
            case R.id.btn_next: {
                Intent it = new Intent(this, ActivityReserveService.class);
                startActivity(it);
            }
            break;


        }
    }

    @Override
    public void onBackPressed() {
        finish();

    }

}