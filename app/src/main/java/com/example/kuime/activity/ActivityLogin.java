package com.example.kuime;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.kuime.activity.ActivityAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ActivityLogin extends AppCompatActivity implements IaResultHandler{

    private EditText m_etUserId;
    private EditText m_etPassword;

    private Context m_Context;
    private CaPref m_Pref;

    String m_strMemberId;
    String m_strPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        m_Context = getApplicationContext();
        m_Pref = new CaPref(m_Context);

        m_etUserId = findViewById(R.id.input_id);
        m_etPassword = findViewById(R.id.input_pw);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login: {

                m_strMemberId = m_etUserId.getText().toString();
                m_strPassword = m_etPassword.getText().toString();

                if (m_strMemberId.isEmpty() || m_strPassword.isEmpty()) {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(ActivityLogin.this);
                    //dlg.setTitle("경고"); //제목
                    dlg.setMessage("아이디와 비밀번호를 입력해주세요"); // 메시지
                    //dlg.setIcon(R.drawable.deum); // 아이콘 설정
//                버튼 클릭시 동작

                    dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dlg.show();
                }
                else {
                    Calendar calToday = Calendar.getInstance();
                    calToday.add(Calendar.DATE, 0);
                    SimpleDateFormat myyyyMMddFormat = new SimpleDateFormat("yyMMdd");
                    String m_dtToday = myyyyMMddFormat.format(calToday.getTime())+"1";

                    CaApplication.m_Engine.CheckBldLogin(m_strMemberId, m_strPassword, "android", CaApplication.m_Info.m_strPushId, m_dtToday, this, this);
                }
            }
            break;

            case R.id.btn_sign_up: {
                Intent it = new Intent(this, ActivityAuth.class);
                startActivity(it);
            }


        }
    }


    @Override
    public void onResult(CaResult Result) {

    }
}