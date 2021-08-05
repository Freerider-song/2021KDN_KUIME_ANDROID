package com.example.kuime.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.example.kuime.CaApplication;
import com.example.kuime.CaPref;
import com.example.kuime.CaResult;
import com.example.kuime.IaResultHandler;
import com.example.kuime.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

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

        tvFee.setText(CaApplication.m_Info.m_dfWon.format(CaApplication.m_Info.nExpectedFee * CaApplication.m_Info.dReserveTimeRatio));
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

            }
            break;


        }
    }

    @Override
    public void onResult(CaResult Result) {

    }


}