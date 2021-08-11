package com.example.kuime.activity;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kuime.CaEngine;
import com.example.kuime.CaApplication;
import com.example.kuime.CaPref;
import com.example.kuime.CaResult;
import com.example.kuime.IaResultHandler;
import com.example.kuime.R;
import com.example.kuime.model.CaStation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
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
    protected  void onResume() {

        super.onResume();
        m_Context = getApplicationContext();
        m_Pref = new CaPref(m_Context);
        now = System.currentTimeMillis();
        mNow = new Date(now);

        if(CaApplication.m_Info.dtStart != null && CaApplication.m_Info.dtEnd != null){
            long calDate = CaApplication.m_Info.dtEnd.getTime() - CaApplication.m_Info.dtStart.getTime();
            long calNow = now-CaApplication.m_Info.dtStart.getTime();
            CaApplication.m_Info.dReserveTimeRatio = calNow / (double) calDate;
            Log.d("HOME", "ReserveTimeRatio is " + CaApplication.m_Info.dReserveTimeRatio);
            CaApplication.m_Info.dtStart = mNow; //시작시간을 현재시간으로 바꿔주어서 나중에 다시 이 화면에 들어오게 되었을 때 RTRatio 가 현실 반영하게끔 바꿔줌
        }
        //m_Pref.setValue(PREF_CURRENT_CAP, 45);
        int nCurrentCap = m_Pref.getValue(PREF_CURRENT_CAP, 45);
        if(nCurrentCap ==0){
            m_Pref.setValue(PREF_CURRENT_CAP, 45);
            nCurrentCap = m_Pref.getValue(PREF_CURRENT_CAP, 45);
        }
        Log.i("HOME", "current cap is " +nCurrentCap);

        tvName = findViewById(R.id.tv_name);
        tvStation = findViewById(R.id.tv_station);
        tvCar = findViewById(R.id.tv_car_company);
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
        if(CaApplication.m_Info.bPaid == 1 || CaApplication.m_Info.bPaid == -1){ //이용중인 서비스가 없을 때
            tvName.setVisibility(View.INVISIBLE);
            tvStation.setVisibility(View.INVISIBLE);
            tvCar.setVisibility(View.INVISIBLE);
            tvReserveType.setVisibility(View.INVISIBLE);
            tvMargin.setVisibility(View.INVISIBLE);
            tvCurrentCap.setVisibility(View.INVISIBLE);
            ivNext.setVisibility(View.INVISIBLE);
            ivBattery.setVisibility(View.INVISIBLE);

            tvEmpty.setVisibility(View.VISIBLE);

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

            if(CaApplication.m_Info.dReserveTimeRatio <=1){ //1 이상이면 아직 충전 시작 시간이 되지 않았다는 것
                nCurrentCap = (int)Math.round((100-nCurrentCap)*CaApplication.m_Info.dReserveTimeRatio + nCurrentCap);
                m_Pref.setValue(PREF_CURRENT_CAP, nCurrentCap);
            }

            tvCurrentCap.setText(Integer.toString(nCurrentCap)+ "%");
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        m_Context = getApplicationContext();
        m_Pref = new CaPref(m_Context);
        now = System.currentTimeMillis();
        mNow = new Date(now);

        if(CaApplication.m_Info.dtStart != null && CaApplication.m_Info.dtEnd != null){
            long calDate = CaApplication.m_Info.dtEnd.getTime() - CaApplication.m_Info.dtStart.getTime();
            long calNow = now-CaApplication.m_Info.dtStart.getTime();
            CaApplication.m_Info.dReserveTimeRatio = calNow / (double) calDate;
            Log.d("HOME", "ReserveTimeRatio is " + CaApplication.m_Info.dReserveTimeRatio);
            CaApplication.m_Info.dtStart = mNow; //시작시간을 현재시간으로 바꿔주어서 나중에 다시 이 화면에 들어오게 되었을 때 RTRatio 가 현실 반영하게끔 바꿔줌
        }

        //m_Pref.setValue(PREF_CURRENT_CAP, 45);
        int nCurrentCap = m_Pref.getValue(PREF_CURRENT_CAP, 45);
        if(nCurrentCap ==0){
            m_Pref.setValue(PREF_CURRENT_CAP, 45);
            nCurrentCap = m_Pref.getValue(PREF_CURRENT_CAP, 45);
        }
        Log.i("HOME", "current cap is " +nCurrentCap);


        tvName = findViewById(R.id.tv_name);
        tvStation = findViewById(R.id.tv_station);
        tvCar = findViewById(R.id.tv_car_company);
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
        if(CaApplication.m_Info.bPaid == 1 || CaApplication.m_Info.bPaid == -1){ //이용중인 서비스가 없을 때
            tvName.setVisibility(View.INVISIBLE);
            tvStation.setVisibility(View.INVISIBLE);
            tvCar.setVisibility(View.INVISIBLE);
            tvReserveType.setVisibility(View.INVISIBLE);
            tvMargin.setVisibility(View.INVISIBLE);
            tvCurrentCap.setVisibility(View.INVISIBLE);
            ivNext.setVisibility(View.INVISIBLE);
            ivBattery.setVisibility(View.INVISIBLE);

            tvEmpty.setVisibility(View.VISIBLE);

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

            if(CaApplication.m_Info.dReserveTimeRatio <=1){ //1 이상이면 아직 충전 시작 시간이 되지 않았다는 것
                nCurrentCap = (int)Math.round((100-nCurrentCap)*CaApplication.m_Info.dReserveTimeRatio + nCurrentCap);
                m_Pref.setValue(PREF_CURRENT_CAP, nCurrentCap);
            }

            tvCurrentCap.setText(Integer.toString(nCurrentCap)+ "%");
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
                    CaApplication.m_Engine.GetStationInfo(this,this);

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
                if(CaApplication.m_Info.bPaid == 0 && CaApplication.m_Info.dtEnd.before(mNow)){ //아직 결제 전이며, 서비스 이용 시간 이후인 경우
                    Intent it = new Intent(this, ActivityChargeResult.class);
                    startActivity(it);
                }
                else if(CaApplication.m_Info.bPaid == 0 && mNow.before(CaApplication.m_Info.dtEnd)){ //아직 결제 전이며, 서비스 이용 시간인 경우
                    Intent it = new Intent(this, ActivityCharge.class);
                    startActivity(it);
                }

            }
            break;

        }
    }


    @Override
    public void onResult(CaResult Result) {
        if (Result.object == null) {
            Toast.makeText(m_Context, "Check Network", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (Result.m_nCallback) {
            case CaEngine.GET_STATION_INFO: {
                Log.i("MAP" , "GetStationInfo called");
                try {
                    JSONObject jo = Result.object;
                    JSONArray ja = jo.getJSONArray("features");

                    CaApplication.m_Info.alStation.clear();

                    for(int i=0;i<ja.length();i++){
                        JSONObject joStation = ja.getJSONObject(i);
                        CaStation station = new CaStation();
                        JSONObject joGeometry = joStation.getJSONObject("geometry");
                        JSONObject joProperties = joStation.getJSONObject("properties");

                        //JSONObject joGeometry = jaGeometry.getJSONObject(0);
                        JSONArray jaDxy =joGeometry.getJSONArray("coordinates");
                        station.dx = jaDxy.getDouble(0);
                        station.dy = jaDxy.getDouble(1);


                        //JSONObject joProperties = jaProperties.getJSONObject(0);
                        station.nFastCharger = joProperties.getInt("fast_charger");
                        station.nSlowCharger = joProperties.getInt("slow_charger");
                        station.nStationId = joProperties.getInt("station_id");
                        station.strStationName = joProperties.getString("station_name");
                        station.nV2gCharger = joProperties.getInt("v2g");
                        Log.i("MAP", "StationNAme = " +station.strStationName  + " " + station.dx + " " +station.dy);
                        CaApplication.m_Info.alStation.add(station);
                    }
                    Intent it = new Intent(this, ActivityMap.class);
                    startActivity(it);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;

            default: {
                //Log.i(TAG, "Unknown type result received");
            }
            break;

        }

    }
}