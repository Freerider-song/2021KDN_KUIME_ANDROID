package com.example.kuime.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.kuime.CaApplication;
import com.example.kuime.CaEngine;
import com.example.kuime.CaResult;
import com.example.kuime.IaResultHandler;
import com.example.kuime.R;
import com.example.kuime.model.CaStation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static com.example.kuime.CaApplication.m_Context;

public class ActivityReserveTime extends AppCompatActivity implements IaResultHandler{

    private TextView tvDateStart;
    private TextView tvDateEnd;
    private TextView tvTimeStart;
    private TextView tvTimeEnd;

    private DatePickerDialog.OnDateSetListener callbackMethod1;
    private DatePickerDialog.OnDateSetListener callbackMethod2;
    private TimePickerDialog.OnTimeSetListener callbackMethod3;
    private TimePickerDialog.OnTimeSetListener callbackMethod4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_time);
        tvDateStart = findViewById(R.id.tv_date_picker1);
        tvDateEnd = findViewById(R.id.tv_date_picker2);
        tvTimeStart = findViewById(R.id.tv_time_picker1);
        tvTimeEnd = findViewById(R.id.tv_time_picker2);


    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {
                finish();

            }
            break;
            case R.id.tv_date_picker1: {
                DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod1, 2021, 6, 26);
                dialog.show();

                callbackMethod1 = new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        if(monthOfYear>8 && dayOfMonth>=10){
                            tvDateStart.setText(monthOfYear+1 + "/" + dayOfMonth);
                        }
                        else if(monthOfYear<=8 && dayOfMonth>=10){
                            tvDateStart.setText("0"+monthOfYear+1 + "/" + dayOfMonth);
                        }
                        else if(monthOfYear>8 && dayOfMonth<10){
                            tvDateStart.setText(monthOfYear+1 + "/0" + dayOfMonth);
                        }
                        else{
                            tvDateStart.setText("0"+monthOfYear+1 + "/0" + dayOfMonth);
                        }

                    }
                };
            }
            break;

            case R.id.tv_date_picker2: {
                DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod2, 2021, 6, 26);
                dialog.show();

                callbackMethod2 = new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        if(monthOfYear>8 && dayOfMonth>=10){
                            tvDateEnd.setText(monthOfYear+1 + "/" + dayOfMonth);
                        }
                        else if(monthOfYear<=8 && dayOfMonth>=10){
                            tvDateEnd.setText("0"+monthOfYear+1 + "/" + dayOfMonth);
                        }
                        else if(monthOfYear>8 && dayOfMonth<10){
                            tvDateEnd.setText(monthOfYear+1 + "/0" + dayOfMonth);
                        }
                        else{
                            tvDateEnd.setText("0"+monthOfYear+1 + "/0" + dayOfMonth);
                        }
                    }
                };
            }
            break;

            case R.id.tv_time_picker1: {
                TimePickerDialog dialog = new TimePickerDialog(this, callbackMethod3, 8, 10, true);

                dialog.show();
                callbackMethod3 = new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {
                        if(hourOfDay>=10 & minute >=10){
                            tvTimeStart.setText(hourOfDay + ":" + minute);
                        }
                        else if(hourOfDay >=10 && minute <10){
                            tvTimeStart.setText(hourOfDay + ":0" + minute);
                        }
                        else if(hourOfDay<10 && minute>=10){
                            tvTimeStart.setText("0"+hourOfDay + ":" + minute);
                        }
                        else {
                            tvTimeStart.setText("0"+hourOfDay + ":0" + minute);
                        }

                    }
                };
            }
            break;

            case R.id.tv_time_picker2: {
                TimePickerDialog dialog = new TimePickerDialog(this, callbackMethod4, 8, 10, true);

                dialog.show();
                callbackMethod4 = new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {
                        if(hourOfDay>=10 & minute >=10){
                            tvTimeEnd.setText(hourOfDay + ":" + minute);
                        }
                        else if(hourOfDay >=10 && minute <10){
                            tvTimeEnd.setText(hourOfDay + ":0" + minute);
                        }
                        else if(hourOfDay<10 && minute>=10){
                            tvTimeEnd.setText("0"+hourOfDay + ":" + minute);
                        }
                        else {
                            tvTimeEnd.setText("0"+hourOfDay + ":0" + minute);
                        }
                    }
                };
            }
            break;

            case R.id.btn_next: {

                String from = "2021-"+tvDateStart.getText().toString().substring(0,2)+"-"
                        +tvDateStart.getText().toString().substring(3,5) + "-"+ tvTimeStart.getText().toString().substring(0,2)+
                        "-" + tvTimeStart.getText().toString().substring(3,5)+"-00";
                Log.d("ReserveTime", "from is: " +from);
                SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    CaApplication.m_Info.dtStart = transFormat.parse(from);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String to = "2021-"+tvDateEnd.getText().toString().substring(0,2)+"-"
                        +tvDateEnd.getText().toString().substring(3,5) + "-"+ tvTimeEnd.getText().toString().substring(0,2)+
                        "-" + tvTimeEnd.getText().toString().substring(3,5)+"-00";
                Log.d("ReserveTime", "to is: " +to);
                try {
                    CaApplication.m_Info.dtEnd = transFormat.parse(to);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                CaApplication.m_Engine.SetReserveInfo(CaApplication.m_Info.strId, CaApplication.m_Info.nStationId, CaApplication.m_Info.nReserveType, CaApplication.m_Info.dtStart, CaApplication.m_Info.dtEnd
                , CaApplication.m_Info.nMinCapacity, 45, this, this);



            }
            break;


            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    @Override
    public void onBackPressed() {
        finish();

    }

    @Override
    public void onResult(CaResult Result) {
        if (Result.object == null) {
            Toast.makeText(m_Context, "Check Network", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (Result.m_nCallback) {
            case CaEngine.SET_RESERVE_INFO: {

                try {
                    JSONObject jo = Result.object;

                    if(jo.getInt("result_code")==0){
                        Toast.makeText(this, "서비스 예약에 실패하였습니다.", Toast.LENGTH_LONG).show();
                    }
                    else{
                        CaApplication.m_Info.nServiceReservation = jo.getInt("service_reservation_id");
                        CaApplication.m_Info.nExpectedFee = jo.getInt("expected_fee");

                        Intent it = new Intent(this, ActivityReserveEnd.class);
                        startActivity(it);
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