package com.example.kuime.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.kuime.CaApplication;
import com.example.kuime.R;

public class ActivityReserveTime extends AppCompatActivity {

    private TextView tvDateStart;
    private TextView tvDateEnd;
    private TextView tvTimeStart;
    private TextView tvTimeEnd;

    private DatePickerDialog.OnDateSetListener callbackMethod;
    private TimePickerDialog.OnTimeSetListener callbackMethod2;
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
                DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, 2021, 6, 26);
                dialog.show();

                callbackMethod = new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        tvDateStart.setText(monthOfYear+1 + "/" + dayOfMonth);
                    }
                };
            }
            break;

            case R.id.tv_date_picker2: {
                DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, 2021, 6, 26);
                dialog.show();

                callbackMethod = new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        tvDateEnd.setText(monthOfYear+1 + "/" + dayOfMonth);
                    }
                };
            }
            break;

            case R.id.tv_time_picker1: {
                TimePickerDialog dialog = new TimePickerDialog(this, callbackMethod2, 8, 10, true);

                dialog.show();
                callbackMethod2 = new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {
                        tvTimeStart.setText(hourOfDay + ":" + minute);
                    }
                };
            }
            break;

            case R.id.tv_time_picker2: {
                TimePickerDialog dialog = new TimePickerDialog(this, callbackMethod2, 8, 10, true);

                dialog.show();
                callbackMethod2 = new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {
                        tvTimeEnd.setText(hourOfDay + ":" + minute);
                    }
                };
            }
            break;

            case R.id.btn_next: {
                if(CaApplication.m_Info.nReserveType == 2){ //방전이라면
                    Intent it = new Intent(this, ActivityReserveMin.class);
                    startActivity(it);
                }
                else{
                    Intent it = new Intent(this, ActivityReserveEnd.class);
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
}