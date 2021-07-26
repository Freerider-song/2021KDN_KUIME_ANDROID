package com.example.kuime.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kuime.CaApplication;
import com.example.kuime.R;

public class ActivityReserveService extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_service);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {
                finish();

            }
            break;
            case R.id.btn_charge_only: {
                CaApplication.m_Info.nReserveType = 1;
                Intent it = new Intent(this, ActivityReserveTime.class);
                startActivity(it);
            }
            break;

            case R.id.btn_discharge_only: {
                CaApplication.m_Info.nReserveType = 2;
                Intent it = new Intent(this, ActivityReserveTime.class);
                startActivity(it);
            }

            break; case R.id.btn_charge_both: {
                CaApplication.m_Info.nReserveType = 3;
                Intent it = new Intent(this, ActivityReserveTime.class);
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