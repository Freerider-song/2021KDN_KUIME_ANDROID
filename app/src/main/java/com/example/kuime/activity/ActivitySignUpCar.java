package com.example.kuime.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.kuime.ActivityLogin;
import com.example.kuime.CaApplication;
import com.example.kuime.CaEngine;
import com.example.kuime.CaPref;
import com.example.kuime.CaResult;
import com.example.kuime.IaResultHandler;
import com.example.kuime.R;
import com.example.kuime.model.CaCarModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivitySignUpCar extends AppCompatActivity implements IaResultHandler {

    public ArrayList<String> alCompany=new ArrayList<>();
    public ArrayList<CaCarModel> alModel=new ArrayList<>();
    public ArrayList<String> alStrModel=new ArrayList<>();

    public Spinner spCompany;
    public Spinner spModel;

    int nCompany = 0;
    int nModel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_car);

        CaApplication.m_Engine.GetCarCompanyInfo(this,this);
        spCompany = findViewById(R.id.sp_car_company);
        spModel = findViewById(R.id.sp_car_model);


        ArrayAdapter AdapterModel = new ArrayAdapter<String>(this, R.layout.spinner_layout, alStrModel);

        ArrayAdapter<String> AdapterCompany = new ArrayAdapter<String>(this, R.layout.spinner_layout, alCompany);

        spCompany.setEnabled(true);
        spModel.setEnabled(true);

        spCompany.setAdapter(AdapterCompany);
        spModel.setAdapter(AdapterModel);

        //AdapterMeter.setDropDownViewResource(R.layout.eg_spinner_item_style);

        spCompany.setSelection(nCompany);
        spModel.setSelection(nModel);


        spCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nCompany=position;
                CaApplication.m_Info.strCarCompany = alCompany.get(nCompany);
                getCarModelInfo(nCompany);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }

    public void getCarModelInfo(int nCompany){

        CaApplication.m_Engine.GetCarModelInfo(alCompany.get(nCompany), this, this);
        spModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nModel=position;
                CaCarModel model = alModel.get(nModel);
                CaApplication.m_Info.strCarModel = model.strModelName;
                CaApplication.m_Info.nModelId = model.nModelId;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {
                finish();

            }
            break;
            case R.id.btn_next: {
                CaApplication.m_Info.bCarRegistered = true;
                Intent it = new Intent(this, ActivitySignUpCard.class);
                startActivity(it);
            }
            break;


        }
    }

    @Override
    public void onBackPressed() {
        finish();

    }

    @Override
    public void onResult(CaResult Result) {
        if (Result.object == null) {
            Toast.makeText(getApplicationContext(), "Check Network", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (Result.m_nCallback) {
            case CaEngine.GET_CAR_COMPANY_INFO: {

                try {
                    JSONObject jo = Result.object;
                    JSONArray ja = jo.getJSONArray("manufacturers");

                    for (int i=0; i<ja.length(); i++) {
                        JSONObject joCompany=ja.getJSONObject(i);

                        alCompany.add(joCompany.getString("manufacturer"));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;

            case CaEngine.GET_CAR_MODEL_INFO: {

                try {
                    JSONObject jo = Result.object;
                    JSONArray ja = jo.getJSONArray("models");

                    for (int i=0; i<ja.length(); i++) {

                        JSONObject joModel=ja.getJSONObject(i);
                        CaCarModel model=new CaCarModel();
                        model.nModelId = joModel.getInt("model_id");
                        model.strModelName = joModel.getString("model_name");
                        alStrModel.add(joModel.getString("model_name"));

                        alModel.add(model);
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
}