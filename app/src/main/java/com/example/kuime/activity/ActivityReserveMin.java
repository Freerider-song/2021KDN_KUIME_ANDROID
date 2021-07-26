package com.example.kuime.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kuime.R;

import java.util.ArrayList;

public class ActivityReserveMin extends AppCompatActivity {

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_min);


        ArrayAdapter arrayAdapter= ArrayAdapter.createFromResource(this, R.array.min_cap, R.layout.spinner_layout);

        spinner = findViewById(R.id.sp_min_capacity);

        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
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
                Intent it = new Intent(this, ActivityReserveEnd.class);
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