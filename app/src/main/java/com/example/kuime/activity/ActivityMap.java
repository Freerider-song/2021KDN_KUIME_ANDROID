package com.example.kuime.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.example.kuime.ActivityLogin;
import com.example.kuime.CaApplication;
import com.example.kuime.CaEngine;
import com.example.kuime.CaPref;
import com.example.kuime.CaResult;
import com.example.kuime.IaResultHandler;
import com.example.kuime.R;
import com.example.kuime.model.CaStation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.example.kuime.CaApplication.m_Context;
import static com.example.kuime.R.drawable.v2g_marker;

public class ActivityMap extends AppCompatActivity implements IaResultHandler, OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap mMap;
    private Marker currentMarker = null;
    public ArrayList<CaStation> alStation = new ArrayList<>();

    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1???
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5???

    private static final int PERMISSIONS_REQUEST_CODE = 100;
    boolean needRequest = false;

    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};  // ?????? ?????????


    Location mCurrentLocatiion;
    LatLng currentPosition;


    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location location;


    private View mLayout;  // Snackbar ???????????? ???????????? View??? ???????????????.
    // (????????? Toast????????? Context??? ??????????????????.)

    private EditText etLocation;
    private Button btnSearch;
    Fragment mapBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //CaApplication.m_Engine.GetStationInfo(this,this);
        alStation = CaApplication.m_Info.alStation;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;
        //marker size ??????
        int height = 150;
        int width = 150;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.electricity_marker);
        BitmapDrawable bitmapdraw2 = (BitmapDrawable)getResources().getDrawable(v2g_marker);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap b2=bitmapdraw2.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        Bitmap v2gMarker = Bitmap.createScaledBitmap(b2, width, height, false);


        MarkerOptions markerOptions = new MarkerOptions();
        LatLng JEJU = new LatLng(33.495, 126.4711157);
        /*
        markerOptions.position(JEJU);
        markerOptions.title("?????? ?????? 1 ?????????");
        markerOptions.snippet("?????? ??????");
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(v2gMarker));
        mMap.addMarker(markerOptions);*/

        Log.i("MAP", "??????????????? alStation ?????? " + alStation.size());

        for(int i = 0; i<alStation.size(); i++){
            CaStation station = alStation.get(i);
            markerOptions
                    .position(new LatLng(station.dx, station.dy))
                    .title(station.strStationName)
                    .snippet("?????? "+station.nFastCharger + " ?????? " + station.nSlowCharger + " V2G " + station.nV2gCharger);
            if(station.nV2gCharger>0){
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            }
            else{
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(v2gMarker));
            }
            mMap.addMarker(markerOptions);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(JEJU, 16));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull @NotNull Marker marker) {
                MapBottom bottomSheet = new MapBottom();
                Bundle bundle = new Bundle();
                bundle.putString("station_name", marker.getTitle());
                for(int i = 0; i<alStation.size(); i++){
                    CaStation station = alStation.get(i);
                    //Log.i("Map", "????????? ???????????? " + marker.getTitle() + " = " + station.strStationName);
                    if(station.strStationName.equals(marker.getTitle())){
                        CaApplication.m_Info.strStationName = station.strStationName;
                        CaApplication.m_Info.nStationId = station.nStationId;
                        CaApplication.m_Info.nSlowCharger = station.nSlowCharger;
                        CaApplication.m_Info.nFastCharger = station.nFastCharger;
                        CaApplication.m_Info.nV2gCharger = station.nV2gCharger;

                        Log.i("Map", "????????? ????????? " + CaApplication.m_Info.nFastCharger + " " + CaApplication.m_Info.nV2gCharger +
                                " " + CaApplication.m_Info.nSlowCharger);
                    }
                }
                bundle.putString("charger_num", marker.getSnippet());

                bottomSheet.setArguments(bundle);
                bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
                return false;
            }
        });
       //?????? https://codinginflow.com/tutorials/android/modal-bottom-sheet
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {
                finish();
            }
            break;

            case R.id.btn_map_search: {
                etLocation = findViewById(R.id.et_map_search);
                String searchText = etLocation.getText().toString();

                Geocoder geocoder = new Geocoder(getBaseContext());
                List<Address> addresses = null;

                try {
                    addresses = geocoder.getFromLocationName(searchText, 3);
                    if (addresses != null && !addresses.equals(" ")) {
                        search(addresses);
                    }
                } catch(Exception e) {

                }

            }
            break;


        }
    }

    // ????????? ?????? ?????? ?????????
    protected void search(List<Address> addresses) {
        Address address = addresses.get(0);
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

        String addressText = String.format(
                "%s, %s",
                address.getMaxAddressLineIndex() > 0 ? address
                        .getAddressLine(0) : " ", address.getFeatureName());

        //locationText.setVisibility(View.VISIBLE);
        //locationText.setText("Latitude" + address.getLatitude() + "Longitude" + address.getLongitude() + "\n" + addressText);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(addressText);

        //mMap.clear();
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
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

                    alStation.clear();

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
                        alStation.add(station);
                    }


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

    @Override
    public void onBackPressed() {
        finish();

    }

}