package com.example.kuime.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kuime.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ActivityReserveCharger extends AppCompatActivity {

    private ChargerAdapter m_ChargerAdapter;

    SimpleDateFormat myyyyMMddFormat = new SimpleDateFormat("yyyyMMdd");

    Calendar calToday = Calendar.getInstance();
    String m_dtToday = myyyyMMddFormat.format(calToday.getTime());


    private class ChargerViewHolder {
        public TextView tvChargerName;
        public TextView tvChargerUsed;

    }

    private class ChargerAdapter extends BaseAdapter {



        public ChargerAdapter() {
            super();
        }

        @Override
        public int getCount() {

            //return plan.m_alAct.size();
            return 3;
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
            ChargerViewHolder holder;
            if (convertView == null) {
                holder = new ChargerViewHolder();

                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_item_charger, null);

                holder.tvChargerName = convertView.findViewById(R.id.tv_charger_name);
                holder.tvChargerUsed = convertView.findViewById(R.id.tv_charger_name2);

                convertView.setTag(holder);
            }
            else {
                holder = (ChargerViewHolder) convertView.getTag();
            }


            /*
            final CaAct act = plan.m_alAct.get(position);
            holder.m_CheckBox.setText(act.m_strActContent);


            boolean flag = false;

            for(int i=0;i<act.m_alActHistory.size();i++){
                CaActHistory actHistory = act.m_alActHistory.get(i);

                if(m_dtToday.equals(myyyyMMddFormat.format(actHistory.m_dtBegin))){
                    holder.m_CheckBox.setChecked(true);
                    flag = true;
                    break;
                }
            }

            if(flag==false) holder.m_CheckBox.setChecked(false);


            holder.m_CheckBox.setOnClickListener(new CheckBox.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((CheckBox)v).isChecked()) {
                        CaApplication.m_Engine.SetSaveActBegin(act.m_nSeqAct,CaApplication.m_Info.m_nSeqAdmin, m_dtToday, getApplicationContext(),ActivityAlarm.this);
                    } else {
                        // TODO : CheckBox is unchecked.
                    }
                }
            }) ;

            if(holder.m_CheckBox.isChecked()){
                holder.m_CheckBox.setClickable(false);
            }

             */



            return convertView;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_charger);

        ListView listView = findViewById(R.id.lv_charger);

        m_ChargerAdapter= new ChargerAdapter();

        listView.setAdapter(m_ChargerAdapter);

        if (m_ChargerAdapter == null) {
            // pre-condition
            return;
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // adapter.getItem(position)의 return 값은 Object 형
                // 실제 Item의 자료형은 CustomDTO 형이기 때문에
                // 형변환을 시켜야 getResId() 메소드를 호출할 수 있습니다.

                Intent intent = new Intent(ActivityReserveCharger.this, ActivityReserveConnect.class);
                // putExtra(key, value)
                //intent.putExtra("imgRes", imgRes);
                startActivity(intent);
            }
        });
    }
}