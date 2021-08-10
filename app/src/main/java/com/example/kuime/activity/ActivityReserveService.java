package com.example.kuime.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kuime.CaApplication;
import com.example.kuime.CaPref;
import com.example.kuime.R;

import static com.example.kuime.CaApplication.m_Context;

public class ActivityReserveService extends AppCompatActivity {

    CaPref m_Pref;
    Button btnCharge,btnDischarge, btnBoth;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_service);

       m_Context = getApplicationContext();
       m_Pref = new CaPref(m_Context);

       int nCurrentCap = m_Pref.getValue(CaPref.PREF_CURRENT_CAP, 45);

        TextView tvCurrent = findViewById(R.id.tv_current_capacity);
        tvCurrent.setText("현재 배터리는 \n" + nCurrentCap+
                "% 입니다");

        btnBoth = findViewById(R.id.btn_charge_both);
        btnCharge = findViewById(R.id.btn_charge_only);
        btnDischarge = findViewById(R.id.btn_discharge_only);

        if(CaApplication.m_Info.strChargerName !="V2G 충전기"){
            btnDischarge.setEnabled(false);
            btnBoth.setEnabled(false);
        }
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