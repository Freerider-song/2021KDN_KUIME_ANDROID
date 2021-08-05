package com.example.kuime.activity;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kuime.ActivityLogin;
import com.example.kuime.CaApplication;
import com.example.kuime.CaPref;
import com.example.kuime.CaResult;
import com.example.kuime.IaResultHandler;
import com.example.kuime.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.kuime.CaApplication.m_Context;
import static com.example.kuime.CaPref.PREF_CURRENT_CAP;

public class ActivityHome extends AppCompatActivity implements IaResultHandler {

    CaPref m_Pref;
    TextView tvName, tvStation, tvCar, tvReserveType, tvMargin, tvCurrentCap, tvEmpty;
    ImageView ivBattery, ivNext;

    long now;
    Date mNow;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        m_Context = getApplicationContext();
        m_Pref = new CaPref(m_Context);
        now = System.currentTimeMillis();
        mNow = new Date(now);

        long calDate = CaApplication.m_Info.dtEnd.getTime() - CaApplication.m_Info.dtStart.getTime();
        long calNow = CaApplication.m_Info.dtEnd.getTime() - now;
        CaApplication.m_Info.dReserveTimeRatio = calNow / (double) calDate;
        Log.d("HOME", "ReserveTimeRatio is " + CaApplication.m_Info.dReserveTimeRatio);

        int nCurrentCap = m_Pref.getValue(PREF_CURRENT_CAP, 45);

        tvName = findViewById(R.id.tv_name);
        tvStation = findViewById(R.id.tv_station);
        tvCar = findViewById(R.id.tv_car_model);
        tvReserveType = findViewById(R.id.tv_reserve_type);
        tvMargin = findViewById(R.id.tv_margin);
        tvCurrentCap = findViewById(R.id.tv_current_capacity);
        tvEmpty = findViewById(R.id.tv_empty);
        ivNext = findViewById(R.id.iv_next);
        ivBattery = findViewById(R.id.iv_battery);
        tvName.setText(CaApplication.m_Info.strName +"님, 환영합니다");
        tvStation.setText(CaApplication.m_Info.strStationName);
        tvCar.setText(CaApplication.m_Info.strCarModel);
        //tvMargin
        if(CaApplication.m_Info.bPaid == 1){
            tvName.setVisibility(View.INVISIBLE);
            tvStation.setVisibility(View.INVISIBLE);
            tvCar.setVisibility(View.INVISIBLE);
            tvReserveType.setVisibility(View.INVISIBLE);
            tvMargin.setVisibility(View.INVISIBLE);
            tvCurrentCap.setVisibility(View.INVISIBLE);
            ivNext.setVisibility(View.INVISIBLE);
            ivBattery.setVisibility(View.INVISIBLE);

        }
        else{
            tvEmpty.setVisibility(View.INVISIBLE);
            if(CaApplication.m_Info.dtEnd.before(mNow)){
                tvReserveType.setText("서비스가 완료되었어요!");
            }
            else if(mNow.before(CaApplication.m_Info.dtStart)){
                tvReserveType.setText("서비스 이용 대기중이에요");
            }
            else if(CaApplication.m_Info.nReserveType == 2){
                tvReserveType.setText("스마트한 방전 중이에요!");
            }
            else{
                tvReserveType.setText("스마트한 충전 중이에요!");
            }

            nCurrentCap = (int)Math.round((100-nCurrentCap)*CaApplication.m_Info.dReserveTimeRatio + nCurrentCap);
            m_Pref.setValue(PREF_CURRENT_CAP, nCurrentCap);
            tvCurrentCap.setText(Integer.toString(nCurrentCap));
            if(nCurrentCap <20) {
                ivBattery.setImageDrawable(getDrawable(R.drawable.battery1));
            }
            else if(nCurrentCap <40) {
                ivBattery.setImageDrawable(getDrawable(R.drawable.battery2));
            }
            else if(nCurrentCap <60) {
                ivBattery.setImageDrawable(getDrawable(R.drawable.battery3));
            }
            else if(nCurrentCap <90) {
                ivBattery.setImageDrawable(getDrawable(R.drawable.battery4));
            }
            else if(nCurrentCap <=100) {
                ivBattery.setImageDrawable(getDrawable(R.drawable.battery5));
            }
        }



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