package com.example.kuime.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dinuscxj.progressbar.CircleProgressBar;
//import com.example.kuime.ActivityLogin;
import com.example.kuime.CaApplication;
import com.example.kuime.CaEngine;
import com.example.kuime.CaPref;
import com.example.kuime.CaResult;
import com.example.kuime.IaResultHandler;
import com.example.kuime.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Cap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.kuime.CaApplication.m_Context;
import static com.example.kuime.CaPref.PREF_CURRENT_CAP;

public class ActivityCharge extends AppCompatActivity implements IaResultHandler, OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback  {

    private GoogleMap mMap;
    private Marker currentMarker = null;
    public CircleProgressBar pbBattery;

    Date mNow;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    CaPref m_Pref;

    TextView tvFee, tvStation, tvCompleteUntil, tvEfficiency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);

        CaApplication.m_Engine.GetChargeInfo(CaApplication.m_Info.nServiceReservation, this, this);

        m_Context = getApplicationContext();
        m_Pref = new CaPref(m_Context);
        mNow = new Date(System.currentTimeMillis());


        int nCurrentCap = m_Pref.getValue(PREF_CURRENT_CAP, 45);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        pbBattery = findViewById(R.id.pb_battery);
        pbBattery.setProgress(nCurrentCap);

        tvFee = findViewById(R.id.tv_fee);
        tvStation = findViewById(R.id.tv_station);
        tvCompleteUntil = findViewById(R.id.tv_complete_until);
        tvEfficiency = findViewById(R.id.tv_efficiency);

        tvFee.setText(CaApplication.m_Info.m_dfWon.format(CaApplication.m_Info.nExpectedFee * CaApplication.m_Info.dReserveTimeRatio)+ "원");
        tvStation.setText(CaApplication.m_Info.strStationName + " / " + CaApplication.m_Info.strChargerName);

        long calDate = CaApplication.m_Info.dtEnd.getTime() - mNow.getTime();
        long calMin = calDate / (60*1000);
        long calHour = calMin / 60;
        long calMinute = calMin % 60;
        tvCompleteUntil.setText("서비스 완료까지 " + calHour + ":" + calMinute + " 남았어요.");
        tvCompleteUntil.setTextColor(getResources().getColor(R.color.eg_menu_blue));

        if(CaApplication.m_Info.dEfficiency !=0.1){
            tvEfficiency.setText(String.format("%.0f", CaApplication.m_Info.dBatteryCapacity
                    * (double)nCurrentCap
                    / 100 * CaApplication.m_Info.dEfficiency)+ "km를 달릴 수 있어요!");
            tvEfficiency.setTextColor(getResources().getColor(R.color.bright_red));
        }
        else{
            tvEfficiency.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {

        mMap = googleMap;

        LatLng Station = new LatLng(CaApplication.m_Info.dDx, CaApplication.m_Info.dDy);

        //marker size 조절
        int height = 150;
        int width = 150;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.electricity_marker);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(Station);
        markerOptions.title(CaApplication.m_Info.strStationName);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Station, 18));

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {
                finish();
            }
            break;

            case R.id.btn_stop: {

                AlertDialog.Builder dlg = new AlertDialog.Builder(ActivityCharge.this);
                dlg.setTitle("확인"); //제목
                dlg.setMessage("서비스 이용을 중단하시겠습니까?"); // 메시지

                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        stopCharge();

                    }
                });

                dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                dlg.show();

            }
            break;


        }
    }

    public void stopCharge(){
        CaApplication.m_Engine.StopCharge(CaApplication.m_Info.nServiceReservation,this,this);
    }

    @Override
    public void onBackPressed() {
        finish();

    }

    @Override
    public void onResult(CaResult Result) throws JSONException {
        if (Result.object == null) {
            Toast.makeText(m_Context, "Check Network", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (Result.m_nCallback) {
            case CaEngine.GET_CHARGE_INFO: {

                try {
                    JSONObject jo = Result.object;
                    CaApplication.m_Info.nReserveType = jo.getInt("reserve_type");
                    CaApplication.m_Info.dtEnd = CaApplication.m_Info.parseDate(jo.getString("finish_time"));
                    CaApplication.m_Info.nExpectedFee = jo.getInt("expected_fee");
                    CaApplication.m_Info.dDx = jo.getDouble("dx");
                    CaApplication.m_Info.dDy = jo.getDouble("dy");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;

            case CaEngine.STOP_CHARGE: {
                JSONObject jo = Result.object;
                int nResultCode = jo.getInt("result_code");
                Log.i("Charge", "StopCharge Result arrived...");

                if(nResultCode == 0 ){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(ActivityCharge.this);
                    dlg.setTitle("실패"); //제목
                    dlg.setMessage("서비스를 중단하지 못했습니다."); // 메시지

                    dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();

                        }
                    });

                    dlg.show();
                }
                else if(nResultCode == 1){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(ActivityCharge.this);
                    dlg.setTitle("확인"); //제목
                    dlg.setMessage("중단 완료되었습니다. 결제 화면으로 넘어갑니다."); // 메시지

                    dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            Intent it = new Intent(ActivityCharge.this, ActivityChargeResult.class);
                            startActivity(it);

                        }
                    });

                    dlg.show();
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