package com.example.kuime.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kuime.ActivityLogin;
import com.example.kuime.CaApplication;
import com.example.kuime.CaPref;
import com.example.kuime.CaResult;
import com.example.kuime.IaResultHandler;
import com.example.kuime.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivitySignUpCard extends AppCompatActivity implements IaResultHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_card);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {
                finish();
            }
            break;
            case R.id.btn_next: {

                CaApplication.m_Engine.SetSignUpInfo(CaApplication.m_Info.strName, CaApplication.m_Info.strId,
                        CaApplication.m_Info.strPassword, CaApplication.m_Info.nModelId, this, this);

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

        try {
            JSONObject jo = Result.object;
            int nResultCode = jo.getInt("result_code");

            if (nResultCode == 1) {

                Intent it = new Intent(this, ActivitySignUpComplete.class);
                startActivity(it);

            } else {
                AlertDialog.Builder dlg = new AlertDialog.Builder(ActivitySignUpCard.this);
                dlg.setMessage("회원가입이 정상적으로 진행되지 않았습니다. 처음부터 다시 진행해주세요");
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dlg.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}