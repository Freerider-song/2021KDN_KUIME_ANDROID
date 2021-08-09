package com.example.kuime.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kuime.activity.ActivityLogin;
import com.example.kuime.CaApplication;
import com.example.kuime.CaEngine;
import com.example.kuime.CaPref;
import com.example.kuime.CaResult;
import com.example.kuime.IaResultHandler;
import com.example.kuime.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.kuime.CaApplication.m_Context;

public class ActivityChargeResult extends AppCompatActivity implements IaResultHandler {

    CaPref m_Pref;
    TextView tvTitle, tvFeeTitle, tvFee, tvPay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_result);

        m_Context = getApplicationContext();
        m_Pref = new CaPref(m_Context);


        CaApplication.m_Engine.GetChargeResult(CaApplication.m_Info.nServiceReservation, this,this);

        tvTitle = findViewById(R.id.tv_final_reserve_type);
        tvFeeTitle = findViewById(R.id.tv_final_fee_title);
        tvFee = findViewById(R.id.tv_final_fee);
        tvPay = findViewById(R.id.tv_pay);

        if(CaApplication.m_Info.nReserveType == 2){ // 방전이라면
            tvTitle.setText("쿠이미가 똑똑한 \n 방전을 완료했어요");
            tvFeeTitle.setText("최종 금액");
            tvPay.setText("포인트로 전환됩니다.\n 언제든지 지갑으로 옮길 수 있어요");
        }

        tvFee.setText(CaApplication.m_Info.nExpectedFee + " 원");
    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_back: {
                finish();
            }
            break;

            case R.id.btn_next: {

                CaApplication.m_Engine.SetServicePaid(CaApplication.m_Info.nServiceReservation, this,this);

            }
            break;


        }
    }

    @Override
    public void onResult(CaResult Result) {
        if (Result.object == null) {
            Toast.makeText(m_Context, "Check Network", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (Result.m_nCallback) {
            case CaEngine.GET_CHARGE_RESULT: {

                try {
                    JSONObject jo = Result.object;
                    CaApplication.m_Info.nExpectedFee = jo.getInt("expected_fee");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;

            case CaEngine.SET_SERVICE_PAID: {

                try {
                    JSONObject jo = Result.object;
                    int nResultCode = jo.getInt("result_code");

                    if(nResultCode == 1){
                        AlertDialog.Builder dlg = new AlertDialog.Builder(ActivityChargeResult.this);
                        dlg.setMessage("서비스가 완료되었습니다"); // 메시지

                        dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {
                                m_Pref.setValue(CaPref.PREF_CURRENT_CAP, 45);
                            }
                        });
                        dlg.show();
                    }

                    else{
                        AlertDialog.Builder dlg = new AlertDialog.Builder(ActivityChargeResult.this);
                        dlg.setMessage("결제에 실패했습니다"); // 메시지

                        dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dlg.show();
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