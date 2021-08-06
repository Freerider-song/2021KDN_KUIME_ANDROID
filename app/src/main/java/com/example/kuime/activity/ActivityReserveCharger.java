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

import com.example.kuime.CaApplication;
import com.example.kuime.R;
import com.example.kuime.model.CaCharger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class ActivityReserveCharger extends AppCompatActivity {

    private ChargerAdapter m_ChargerAdapter;

    SimpleDateFormat myyyyMMddFormat = new SimpleDateFormat("yyyyMMdd");

    Calendar calToday = Calendar.getInstance();
    String m_dtToday = myyyyMMddFormat.format(calToday.getTime());

    ArrayList<CaCharger> alCharger = new ArrayList<>();


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

            return alCharger.size();

        }

        @Override
        public Object getItem(int position) {
            //return plan.m_alAct.get(position);
            return alCharger.get(position);

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
                holder.tvChargerUsed = convertView.findViewById(R.id.tv_charge_used);

                convertView.setTag(holder);
            }
            else {
                holder = (ChargerViewHolder) convertView.getTag();
            }



            final CaCharger charger= alCharger.get(position);

            holder.tvChargerName.setText(charger.strChargerName);
            if(charger.bUsed){
                holder.tvChargerUsed.setVisibility(View.VISIBLE);
            }
            else{
                holder.tvChargerUsed.setVisibility(View.INVISIBLE);
            }

            return convertView;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_charger);

        int nCount = CaApplication.m_Info.nFastCharger + CaApplication.m_Info.nSlowCharger;

        Random random = new Random();
        CaCharger charger = new CaCharger();
        for(int i=0; i<CaApplication.m_Info.nV2gCharger; i++){
            charger.strChargerName = Integer.toString(i+1)+" V2G 충전기";
            charger.bUsed = false;
            alCharger.add(charger);
        }

        for(int i=CaApplication.m_Info.nV2gCharger; i<CaApplication.m_Info.nFastCharger; i++){
            charger.strChargerName = Integer.toString(i+1)+"충전기 (급속)";
            charger.bUsed = random.nextBoolean();
            alCharger.add(charger);
        }
        for(int i=CaApplication.m_Info.nFastCharger; i<nCount; i++){

            charger.strChargerName = Integer.toString(i+1)+"충전기 (완속)";
            charger.bUsed = random.nextBoolean();
            alCharger.add(charger);
        }

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
                final CaCharger charger= alCharger.get(position);
                Intent intent = new Intent(ActivityReserveCharger.this, ActivityReserveConnect.class);
                CaApplication.m_Info.strChargerName = charger.strChargerName;
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();

    }
}