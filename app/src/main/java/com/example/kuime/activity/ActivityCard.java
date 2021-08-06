package com.example.kuime.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.kuime.R;

public class ActivityCard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
    }

    @Override
    public void onBackPressed() {
        finish();

    }
}