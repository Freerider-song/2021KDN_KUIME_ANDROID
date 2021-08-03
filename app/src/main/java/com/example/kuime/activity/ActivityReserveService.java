package com.example.kuime.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.kuime.CaApplication;
import com.example.kuime.R;

public class ActivityReserveService extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_service);

        TextView tvCurrent = findViewById(R.id.tv_current_capacity);
        tvCurrent.setText("현재 배터리는 \n" + CaApplication.m_Info.nCurrentCapacity +
                "입니다");
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
                Intent it = new Intent(this, ActivityReserveMin.class);
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