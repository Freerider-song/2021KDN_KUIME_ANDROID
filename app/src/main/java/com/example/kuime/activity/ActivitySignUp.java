package com.example.kuime.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kuime.CaResult;
import com.example.kuime.IaResultHandler;
import com.example.kuime.R;

public class ActivitySignUp extends AppCompatActivity implements IaResultHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {
                finish();

            }
            break;
            case R.id.btn_next: {
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

    }
}