package com.example.kuime.activity;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kuime.CaEngine;
import com.example.kuime.CaApplication;
import com.example.kuime.CaPref;
import com.example.kuime.CaResult;
import com.example.kuime.IaResultHandler;
import com.example.kuime.R;
import com.example.kuime.model.CaHistory;
import com.example.kuime.model.CaStation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.kuime.CaApplication.m_Context;
import static com.example.kuime.CaPref.PREF_CURRENT_CAP;

public class ActivityHome extends AppCompatActivity implements IaResultHandler {

    CaPref m_Pref;
    TextView tvName, tvStation, tvCar, tvReserveType, tvMargin, tvCurrentCap, tvEmpty;
    ImageView ivBattery, ivNext;
    Button btnMap;

    long now;
    Date mNow;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    ArrayList<CaHistory> alHistory = new ArrayList<>();

    int nCurrentCap;

    SimpleDateFormat mYearMonth = new SimpleDateFormat("yyyy-MM");
    SimpleDateFormat mMonthDay = new SimpleDateFormat("MM-dd");

    String strYearMonth;

    public void calRatio(){
        if(CaApplication.m_Info.dtStart != null && CaApplication.m_Info.dtEnd != null){
            long calDate = CaApplication.m_Info.dtEnd.getTime() - CaApplication.m_Info.dtStart.getTime();
            long calNow = now-CaApplication.m_Info.dtStart.getTime();
            if(calNow>=0){
                CaApplication.m_Info.dReserveTimeRatio = calNow / (double) calDate;
                Log.d("HOME", "ReserveTimeRatio is " + CaApplication.m_Info.dReserveTimeRatio);
                if(mNow.before(CaApplication.m_Info.dtEnd)){
                    CaApplication.m_Info.dtStart = mNow; //??????????????? ?????????????????? ??????????????? ????????? ?????? ??? ????????? ???????????? ????????? ??? RTRatio ??? ?????? ??????????????? ?????????
                }
            }

        }
        nCurrentCap = CaApplication.m_Info.nCurrentCap; //nCurrentCap = 45
        Log.i("HOME", "current cap is " +nCurrentCap + "ratio is " + CaApplication.m_Info.dReserveTimeRatio);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        m_Context = getApplicationContext();
        m_Pref = new CaPref(m_Context);
        now = System.currentTimeMillis();
        mNow = new Date(now);



        tvName = findViewById(R.id.tv_name);
        tvStation = findViewById(R.id.tv_station);
        tvCar = findViewById(R.id.tv_car_company);
        tvReserveType = findViewById(R.id.tv_reserve_type);
        tvMargin = findViewById(R.id.tv_margin);
        tvCurrentCap = findViewById(R.id.tv_current_capacity);
        tvEmpty = findViewById(R.id.tv_empty);
        ivNext = findViewById(R.id.iv_next);
        ivBattery = findViewById(R.id.iv_battery);
        tvName.setText(CaApplication.m_Info.strName +"???, ???????????????");
        tvStation.setText(CaApplication.m_Info.strStationName);
        tvCar.setText(CaApplication.m_Info.strCarModel);
        //btnMap.findViewById(R.id.btn_map);

        calRatio();

    }

    @Override
    protected  void onResume() {

        super.onResume();

        CaApplication.m_Engine.GetHomeInfo(CaApplication.m_Info.strId, this,this);
        CaApplication.m_Engine.GetChargeHistory(CaApplication.m_Info.strId, this,this);



    }


    public void viewSetting(){

        now = System.currentTimeMillis();
        mNow = new Date(now);

        Log.i("HOME", "bPaid is " + CaApplication.m_Info.bPaid);
        //tvMargin
        if(CaApplication.m_Info.bPaid == 1 || CaApplication.m_Info.bPaid == -1){ //???????????? ???????????? ?????? ???
            Log.i("Home", "?????? ?????? ???????????? ????????????");

            tvStation.setVisibility(View.INVISIBLE);
            tvCar.setVisibility(View.INVISIBLE);
            tvReserveType.setVisibility(View.INVISIBLE);
            tvCurrentCap.setVisibility(View.INVISIBLE);
            ivNext.setVisibility(View.INVISIBLE);
            ivBattery.setVisibility(View.INVISIBLE);

            tvEmpty.setVisibility(View.VISIBLE);


        }
        else{
            tvEmpty.setVisibility(View.INVISIBLE);
            tvStation.setVisibility(View.VISIBLE);
            tvCar.setVisibility(View.VISIBLE);
            tvReserveType.setVisibility(View.VISIBLE);
            tvCurrentCap.setVisibility(View.VISIBLE);
            ivNext.setVisibility(View.VISIBLE);
            ivBattery.setVisibility(View.VISIBLE);

            if(CaApplication.m_Info.dtEnd.before(mNow)){
                tvReserveType.setText("???????????? ??????????????????!");
            }
            else if(mNow.before(CaApplication.m_Info.dtStart)){
                tvReserveType.setText("????????? ?????? ??????????????????");
            }
            else if(CaApplication.m_Info.nReserveType == 2){
                tvReserveType.setText("???????????? ?????? ????????????!");
            }
            else if(CaApplication.m_Info.nReserveType == 1){
                tvReserveType.setText("???????????? ?????? ????????????!");
            }
            else if(CaApplication.m_Info.nReserveType == 3){
                tvReserveType.setText("???????????? ??????????? ????????????!");
            }

            if(CaApplication.m_Info.dReserveTimeRatio <=1){ //1 ???????????? ?????? ?????? ?????? ????????? ?????? ???????????? ???
                if(CaApplication.m_Info.nReserveType !=2){
                    nCurrentCap = (int)Math.round((100-nCurrentCap)*CaApplication.m_Info.dReserveTimeRatio + nCurrentCap);
                }
                else{ //????????? ??????
                    nCurrentCap = (int)Math.round(nCurrentCap- (nCurrentCap- CaApplication.m_Info.nMinCapacity)*CaApplication.m_Info.dReserveTimeRatio);
                }
                CaApplication.m_Info.nCurrentCap = nCurrentCap;

                //m_Pref.setValue(PREF_CURRENT_CAP, nCurrentCap);
            }
            if(CaApplication.m_Info.dReserveTimeRatio >=1){
                if(CaApplication.m_Info.nReserveType !=2){
                    nCurrentCap = 100;
                }
                else{ //????????? ??????
                    nCurrentCap = CaApplication.m_Info.nMinCapacity;
                }
                CaApplication.m_Info.nCurrentCap = nCurrentCap;
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
    public void onBackPressed() {

        AlertDialog.Builder dlg = new AlertDialog.Builder(ActivityHome.this);
        dlg.setTitle("??????"); //??????
        dlg.setMessage("?????? ?????????????????????????"); // ?????????
        //dlg.setIcon(R.drawable.deum); // ????????? ??????
//                ?????? ????????? ??????

        dlg.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        dlg.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // kill app
                finishAffinity();
                System.runFinalization();
                System.exit(0);
            }
        });

        dlg.show();

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_map: {
                if(CaApplication.m_Info.bPaid == 1 || CaApplication.m_Info.bPaid == -1){
                    CaApplication.m_Engine.GetStationInfo(this,this);
                }
                else{
                    AlertDialog.Builder dlg = new AlertDialog.Builder(ActivityHome.this);
                    dlg.setMessage("????????? ??????????????????.");
                    dlg.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dlg.show();

                }
                    //CaApplication.m_Engine.GetStationInfo(this,this);

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
                if(CaApplication.m_Info.bPaid == 0 && CaApplication.m_Info.dtEnd.before(mNow)){ //?????? ?????? ?????????, ????????? ?????? ?????? ????????? ??????
                    Intent it = new Intent(this, ActivityChargeResult.class);
                    startActivity(it);
                }
                else if(CaApplication.m_Info.bPaid == 0 && mNow.before(CaApplication.m_Info.dtEnd)){ //?????? ?????? ?????????, ????????? ?????? ????????? ??????
                    CaApplication.m_Engine.GetChargeInfo(CaApplication.m_Info.nServiceReservation, this, this);

                }

            }
            break;

            case R.id.cl_fee: {
                Intent it = new Intent(this, ActivityFee.class);
                startActivity(it);
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

            case CaEngine.GET_CHARGE_INFO: {

                try {
                    JSONObject jo = Result.object;
                    CaApplication.m_Info.nReserveType = jo.getInt("reserve_type");
                    CaApplication.m_Info.dtEnd = CaApplication.m_Info.parseDate(jo.getString("finish_time"));
                    CaApplication.m_Info.nExpectedFee = jo.getInt("expected_fee");
                    CaApplication.m_Info.dDy = jo.getDouble("dx");
                    CaApplication.m_Info.dDx = jo.getDouble("dy");

                    Intent it = new Intent(this, ActivityCharge.class);
                    startActivity(it);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;

            case CaEngine.GET_HOME_INFO: {

                try {
                    Log.i("LOGIN", "GetHomeInfo Called...");
                    JSONObject jo = Result.object;
                    CaApplication.m_Info.strName = jo.getString("name");
                    CaApplication.m_Info.strCarModel = jo.getString("car_model_name");
                    if(!jo.getString("efficiency").equals("????????????")){
                        CaApplication.m_Info.dEfficiency = jo.getDouble("efficiency");
                    }
                    CaApplication.m_Info.dBatteryCapacity = jo.getDouble("battery_capacity");
                    CaApplication.m_Info.bPaid = jo.getInt("is_paid");

                    if(CaApplication.m_Info.bPaid != -1){
                        CaApplication.m_Info.nServiceReservation = jo.getString("service_reservation_id");
                        CaApplication.m_Info.dtStart = CaApplication.m_Info.parseDate(jo.getString("start_time"));
                        CaApplication.m_Info.dtEnd = CaApplication.m_Info.parseDate(jo.getString("finish_time"));
                        CaApplication.m_Info.strStationName = jo.getString("station_name");
                        CaApplication.m_Info.nReserveType = jo.getInt("reserve_type");
                        if (jo.isNull("minimum_capacity")) {
                            //Usage.m_dUsage=0.0;
                            CaApplication.m_Info.nMinCapacity = 0;
                        } else {
                            CaApplication.m_Info.nMinCapacity = jo.getInt("minimum_capacity");
                        }
                    }

                    viewSetting();
                    calRatio();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;

            case CaEngine.GET_CHARGE_HISTORY: {

                try {
                    JSONObject jo = Result.object;
                    if(jo.getJSONArray("list_history").length() == 0){
                    }
                    else{
                        JSONArray ja = jo.getJSONArray("list_history");

                        alHistory.clear();
                        Log.i("ChargeHistory" , "ja size" + ja.length());

                        Calendar calToday = Calendar.getInstance();

                        strYearMonth = mYearMonth.format(calToday.getTime());

                        for(int i=0;i<ja.length();i++){
                            JSONObject joHistory = ja.getJSONObject(i);
                            CaHistory history = new CaHistory();

                            history.dtReserve = CaApplication.m_Info.parseDate(joHistory.getString("reserve_time"));
                            history.nReserveType = joHistory.getInt("reserve_type");
                            history.nFee = joHistory.getInt("expected_fee");


                            if(mYearMonth.format(history.dtReserve).equals(strYearMonth)){
                                alHistory.add(history);
                            }


                        }
                        Log.i("ChargeHistory" , "alHistory size" + alHistory.size());

                    }
                    tvMargin.setText(CaApplication.m_Info.m_dfWon.format(alHistory.size() * 1562) +" ?????? ????????????");


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