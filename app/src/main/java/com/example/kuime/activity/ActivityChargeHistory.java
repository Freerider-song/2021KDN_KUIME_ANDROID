package com.example.kuime.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kuime.EgMonthPicker;
import com.example.kuime.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ActivityChargeHistory extends AppCompatActivity {

    private ChargeHistoryAdapter m_ChargeHistoryAdapter;
    public Button btnMonth;
    SimpleDateFormat mFormat = new SimpleDateFormat("MM");

    private EgMonthPicker m_dlgMonthPicker;
    public int Month;

    public void timeSetting() {

        btnMonth = (Button) findViewById(R.id.btn_month_picker);


        Calendar calToday = Calendar.getInstance();
        String m_dtToday = mFormat.format(calToday.getTime());
        btnMonth.setText(m_dtToday+"월");

    }


    private class ChargeHistoryViewHolder {
        public TextView tvChargeDate;
        public TextView tvChargeType;
        public TextView tvEarningFee;
    }

    private class ChargeHistoryAdapter extends BaseAdapter {



        public ChargeHistoryAdapter() {
            super();
        }

        @Override
        public int getCount() {

            //return plan.m_alAct.size();
            return 4;
        }

        @Override
        public Object getItem(int position) {
            //return plan.m_alAct.get(position);
            return position;

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ChargeHistoryViewHolder holder;
            if (convertView == null) {
                holder = new ChargeHistoryViewHolder();

                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item_history, null);

                holder.tvChargeDate = convertView.findViewById(R.id.tv_charge_date);
                holder.tvChargeType = convertView.findViewById(R.id.tv_charge_type);
                holder.tvEarningFee = convertView.findViewById(R.id.tv_earning_fee);

                convertView.setTag(holder);
            }
            else {
                holder = (ChargeHistoryViewHolder) convertView.getTag();
            }

            return convertView;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_history);

        timeSetting();

        ListView listView = findViewById(R.id.lv_charge_history);

        m_ChargeHistoryAdapter= new ChargeHistoryAdapter();

        listView.setAdapter(m_ChargeHistoryAdapter);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {
                finish();
            }
            break;

            case R.id.btn_month_picker: {

                View.OnClickListener LsnConfirmYes = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        m_dlgMonthPicker.dismiss();
                        int nMonth = m_dlgMonthPicker.m_npMonth.getValue();
                        Log.i("MonthPicker", "month=" + nMonth);
                        Month = nMonth;

                        String strMonth = Integer.toString(nMonth);
                        if (nMonth <= 9) strMonth = "0" + strMonth;

                        btnMonth = (Button) findViewById(R.id.btn_month_picker);
                        btnMonth.setText(strMonth+"월");

                    }
                };

                View.OnClickListener LsnConfirmNo = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("ActivityCandidate", "No button clicked...");
                        m_dlgMonthPicker.dismiss();
                    }
                };

                m_dlgMonthPicker = new EgMonthPicker(this, "조회할 날짜를 선택하세요", LsnConfirmYes, LsnConfirmNo);
                m_dlgMonthPicker.show();
            }
            break;

        }
    }
}