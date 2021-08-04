package com.example.kuime.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.kuime.CaApplication;
import com.example.kuime.R;

import java.text.SimpleDateFormat;

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
                        if(monthOfYear>8){
                            tvDateStart.setText(monthOfYear+1 + "/" + dayOfMonth);
                            //commit test
                        }
                        else{
                            tvDateStart.setText("0"+monthOfYear+1 + "/" + dayOfMonth);
                        }

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
                        if(monthOfYear>8){
                            tvDateEnd.setText(monthOfYear+1 + "/" + dayOfMonth);
                        }
                        else{
                            tvDateEnd.setText("0"+monthOfYear+1 + "/" + dayOfMonth);
                        }
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

                String from = "2021-"+tvDateStart.getText().toString().substring(0,2)+"-"
                        +tvDateStart.getText().toString().substring(3,5) + " "+ tvTimeStart.getText().toString().substring(0,2)+
                        ":" + tvTimeStart.getText().toString().substring(3,5)+":00";
                Log.d("ReserveTime", "from is: " +from);
                SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                CaApplication.m_Info.dtStart = transFormat.parse(from);

                String to = "2021-"+tvDateEnd.getText().toString().substring(0,2)+"-"
                        +tvDateEnd.getText().toString().substring(3,5) + " "+ tvTimeEnd.getText().toString().substring(0,2)+
                        ":" + tvTimeEnd.getText().toString().substring(3,5)+":00";
                Log.d("ReserveTime", "to is: " +to);
                CaApplication.m_Info.dtEnd = transFormat.parse(to);

                Intent it = new Intent(this, ActivityReserveEnd.class);
                startActivity(it);

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
}