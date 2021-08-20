package com.example.kuime.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ekn.gruzer.gaugelibrary.contract.ValueFormatter;
import com.example.kuime.CaApplication;
import com.example.kuime.CaEngine;
import com.example.kuime.CaResult;
import com.example.kuime.IaResultHandler;
import com.example.kuime.R;
import com.example.kuime.model.CaUsage;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.kuime.CaApplication.m_Context;

public class ActivityFee extends AppCompatActivity implements IaResultHandler {

    LineChart m_LChart;
    public ArrayList<CaUsage> m_alLineUsage = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee);

        initChart();

        CaApplication.m_Engine.GetFeeInfo(this,this);
    }

    public void initChart()
    {

        m_LChart = findViewById(R.id.chart);

        m_LChart.getDescription().setEnabled(false);
        m_LChart.setMaxVisibleValueCount(60);
        m_LChart.setDrawGridBackground(false);
        m_LChart.animateY(1000);

        m_LChart.setScaleEnabled(true);
        m_LChart.setPinchZoom(true);
        m_LChart.setDoubleTapToZoomEnabled(true);
        m_LChart.setTouchEnabled(false);


        //Typeface tf2 = Typeface.createFromAsset(getAssets(), StringUtil.getString(this, R.string.font_open_sans_regular));

        XAxis xAxis2 = m_LChart.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis2.setTypeface(tf);
        xAxis2.setDrawAxisLine(true);
        xAxis2.setDrawGridLines(false);
        xAxis2.setGranularity(0.3f);
        xAxis2.setLabelCount(5, true);

        YAxis yLeft2 = m_LChart.getAxisLeft();
        //yLeft2.setTypeface(tf);
        yLeft2.setDrawAxisLine(true);
        yLeft2.setDrawGridLines(true);
        xAxis2.setGranularity(0.3f);

        Legend lgd2 = m_LChart.getLegend();
        //lgd2.setTypeface(tf);
        lgd2.setDrawInside(false);
        lgd2.setFormSize(13f);
        lgd2.setXEntrySpace(13f);
        lgd2.setTextSize(15f);
        //lgd2.setXOffset(10f);
        lgd2.setYOffset(10f);
        m_LChart.setExtraOffsets(5f,5f,5f,15f);

    }

    public ArrayList<String> getAreaCount(){
        int nCountUsage=m_alLineUsage.size();
        ArrayList<String> label = new ArrayList<>();
        for (int i = 0; i <nCountUsage; i++) {
            CaUsage Usage=m_alLineUsage.get(i);

            label.add((Usage.m_strHHmm).substring(0,2) +"시");
        };
        return label;
    }
    public void setDataChart() {

        //라인차트 시작

        m_LChart.clear();

        ArrayList<Entry> yValsKwhCurr = new ArrayList<>();



        int nCountUsage=m_alLineUsage.size();
        for (int i=0; i<nCountUsage; i++) {
            CaUsage Usage=m_alLineUsage.get(i);

            if(!Double.isNaN(Usage.m_dUsage)){  //double nan 체크 성공

                yValsKwhCurr.add(new Entry(i, (float)Usage.m_dUsage));
                Log.i("ActivitySiteState" ,"fusage is "+ Usage.m_dUsage);
            }

        }

        ValueFormatter vfKwhWithUnit= new ValueFormatter() {

            @Override
            public String getFormattedValue(double value) {
                if (value==0) return "";
                else return CaApplication.m_Info.m_dfWon.format(value)+" 원";
            }


        };

        ValueFormatter vfKwh=new ValueFormatter() {

            @Override
            public String getFormattedValue(double value) {
                return CaApplication.m_Info.m_dfWon.format(value);
            }

        };

        YAxis yLeft = m_LChart.getAxisLeft();
        yLeft.setValueFormatter((com.github.mikephil.charting.formatter.ValueFormatter) vfKwh);

        YAxis yRight = m_LChart.getAxisRight();
        yRight.setValueFormatter((com.github.mikephil.charting.formatter.ValueFormatter) vfKwh);

        XAxis xAxis = m_LChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getAreaCount()));
        //xAxis.setLabelCount(m_alLineUsage.size());
        xAxis.setLabelCount(7, true); //x축 라벨 갯수 제한


        LineDataSet setKwhCurr=new LineDataSet(yValsKwhCurr, "실시간 요금");
        setKwhCurr.setColor(getResources().getColor(R.color.chart_red));
        setKwhCurr.setValueFormatter((com.github.mikephil.charting.formatter.ValueFormatter) vfKwhWithUnit);
        setKwhCurr.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        setKwhCurr.setLineWidth(3f);
        setKwhCurr.setDrawCircles(false);
        setKwhCurr.setCubicIntensity(0.2f);




        LineData dataKwh = new LineData(setKwhCurr);
        dataKwh.setValueTextSize(10f);
        dataKwh.setHighlightEnabled(false);

        m_LChart.setData(dataKwh);
        m_LChart.getXAxis().setAxisMinimum(0);
        m_LChart.getXAxis().setAxisMaximum(nCountUsage);
        m_LChart.getAxisLeft().setAxisMinimum(0f);
        m_LChart.getAxisRight().setEnabled(false);
        // m_LChart.groupBars(0.0f, fGroupSpace, fBarSpace);

        m_LChart.getLegend().setEnabled(true);

    }



    @Override
    public void onBackPressed() {
            finish();
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
    public void onResult(CaResult Result) {
        if (Result.object == null) {
            Toast.makeText(m_Context, "Check Network", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (Result.m_nCallback) {
            case CaEngine.GET_FEE_INFO: {
                Log.i("ActivitySiteState", "Result of GetSiteUsageFront received...");
                try {
                    JSONObject jo = Result.object;


                    JSONArray ja = jo.getJSONArray("list_fee");
                    m_alLineUsage.clear();
                    for (int i = 0; i < ja.length(); i++) {
                        try {
                            JSONObject joUsage = ja.getJSONObject(i);

                            CaUsage Usage = new CaUsage();

                            Usage.m_strHHmm = joUsage.getString("hhmm");
                            if (joUsage.isNull("fee")) {
                                //Usage.m_dUsage=0.0;
                                Usage.m_dUsage = Double.NaN;
                            } else {
                                Usage.m_dUsage = joUsage.getDouble("fee");

                            }
                            m_alLineUsage.add(Usage);
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }

                    setDataChart();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;

            default: {
                Log.i("UsageYearly", "Unknown type result received : " + Result.m_nCallback);
            }
            break;

        }
    }
}