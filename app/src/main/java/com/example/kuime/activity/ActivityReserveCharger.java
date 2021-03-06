package com.example.kuime.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
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

    TextView tvStation;


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

                holder.tvChargerName = convertView.findViewById(R.id.tv_charge_name);
                holder.tvChargerUsed = convertView.findViewById(R.id.tv_charger_used);

                convertView.setTag(holder);
            }
            else {
                holder = (ChargerViewHolder) convertView.getTag();
            }



            final CaCharger charger= alCharger.get(position);

            Log.i("ReserveCharger", "????????? ????????? ??????" + charger.strChargerName + " ????????? : " + position);
            Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/nanumsquareroundr.ttf");
            holder.tvChargerUsed.setTypeface(tf);
            holder.tvChargerName.setTypeface(tf);

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
        Log.i("ReserveCharger", "????????? ????????? " + CaApplication.m_Info.nFastCharger + " " + CaApplication.m_Info.nV2gCharger +
                " " + CaApplication.m_Info.nSlowCharger);
        int nCount = CaApplication.m_Info.nFastCharger + CaApplication.m_Info.nV2gCharger + CaApplication.m_Info.nSlowCharger;
        Log.i("ReserveCharger", "??? ????????? ????????? " + nCount);

        tvStation =findViewById(R.id.tv_station);
        tvStation.setText(CaApplication.m_Info.strStationName);

        Random random = new Random();


        alCharger.clear();
        for(int i=0; i<CaApplication.m_Info.nV2gCharger; i++){
            CaCharger charger = new CaCharger();
            charger.strChargerName = "V2G ?????????";
            charger.bUsed = false;
            Log.i("ReserveCharger", " ????????? ??????: v2g ????????? bused : " + charger.bUsed);
            alCharger.add(charger);
        }

        for(int i=CaApplication.m_Info.nV2gCharger; i<CaApplication.m_Info.nV2gCharger+CaApplication.m_Info.nFastCharger; i++){
            CaCharger charger = new CaCharger();
            charger.strChargerName = Integer.toString(i+1)+"????????? (??????)";
            charger.bUsed = random.nextBoolean();
            Log.i("ReserveCharger", " ????????? ??????: "+ charger.strChargerName +" bused : " + charger.bUsed);
            alCharger.add(charger);
        }
        for(int i=CaApplication.m_Info.nFastCharger+CaApplication.m_Info.nV2gCharger; i<nCount; i++){
            CaCharger charger = new CaCharger();
            charger.strChargerName = Integer.toString(i+1)+"????????? (??????)";
            charger.bUsed = random.nextBoolean();
            Log.i("ReserveCharger", " ????????? ??????: "+ charger.strChargerName +" bused : " + charger.bUsed);
            alCharger.add(charger);
        }
        Log.i("ReserveCharge", "????????? ?????? " + alCharger.size());

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
                if(!charger.bUsed){
                    Intent intent = new Intent(ActivityReserveCharger.this, ActivityReserveConnect.class);
                    CaApplication.m_Info.strChargerName = charger.strChargerName;
                    startActivity(intent);
                }

            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {
                finish();

            }
            break;

        }
    }
    @Override
    public void onBackPressed() {
        finish();

    }
}