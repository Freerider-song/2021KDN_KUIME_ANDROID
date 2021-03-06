package com.example.kuime.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

//import com.example.kuime.ActivityLogin;
import com.example.kuime.CaApplication;
import com.example.kuime.CaResult;
import com.example.kuime.IaResultHandler;
import com.example.kuime.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ActivitySignUp extends AppCompatActivity implements IaResultHandler {

    TextView tvCheck;
    String strId, strPassword, strPasswordCheck;
    private EditText etId;
    private EditText etPassword, etPasswordCheck;
    String preText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etId = findViewById(R.id.et_id);
        etPassword = findViewById(R.id.et_password);
        etPasswordCheck = findViewById(R.id.et_password_check);
        tvCheck = findViewById(R.id.tv_check);

        strPassword = etPassword.getText().toString();
        strPasswordCheck = etPasswordCheck.getText().toString();

        if(!strPassword.equals(strPasswordCheck)){
            tvCheck.setVisibility(View.VISIBLE);
        }
        else{
            tvCheck.setVisibility(View.INVISIBLE);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {
                finish();

            }
            break;
            case R.id.btn_next: {
                strId = etId.getText().toString();
                strPassword = etPassword.getText().toString();
                strPasswordCheck = etPasswordCheck.getText().toString();

                if (strId.isEmpty() || strPassword.isEmpty() || strPasswordCheck.isEmpty()) {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(ActivitySignUp.this);
                    dlg.setMessage("???????????? ??????????????? ??????????????????"); // ?????????

                    dlg.setPositiveButton("??????",new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dlg.show();
                }

                else if(!strPassword.equals(strPasswordCheck)){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(ActivitySignUp.this);
                    dlg.setMessage("??????????????? ???????????? ????????????."); // ?????????

                    dlg.setPositiveButton("??????",new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dlg.show();
                }
                else {
                    CaApplication.m_Info.strId = strId;
                    CaApplication.m_Info.strPassword = strPassword;
                    Intent it = new Intent(this, ActivitySignUpCar.class);
                    startActivity(it);
                }
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