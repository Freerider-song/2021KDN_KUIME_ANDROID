package com.example.kuime.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.kuime.ActivityLogin;
import com.example.kuime.CaApplication;
import com.example.kuime.CaResult;
import com.example.kuime.IaResultHandler;
import com.example.kuime.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ActivityAuth extends AppCompatActivity implements IaResultHandler {

    EditText etName, etPhone;
    String strName, strPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {
                finish();

            }
            break;
            case R.id.btn_next: {

                strName = etName.getText().toString();
                strPhone = etPhone.getText().toString();

                if (strName.isEmpty() || strPhone.isEmpty()) {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(ActivityAuth.this);
                    //dlg.setTitle("경고"); //제목
                    dlg.setMessage("이름과 번호를 입력해주세요"); // 메시지
                    //dlg.setIcon(R.drawable.deum); // 아이콘 설정
//                버튼 클릭시 동작

                    dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dlg.show();
                }
                else {
                    Intent it = new Intent(this, ActivitySignUp.class);
                    startActivity(it);
                }


            }
            break;
            case R.id.btn_auth: {
                AlertDialog.Builder dlg = new AlertDialog.Builder(ActivityAuth.this);
                //dlg.setTitle("경고"); //제목
                dlg.setMessage("아직 준비 중입니다. 다음 단계로 진행해주세요"); // 메시지
                //dlg.setIcon(R.drawable.deum); // 아이콘 설정
//                버튼 클릭시 동작

                dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dlg.show();
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