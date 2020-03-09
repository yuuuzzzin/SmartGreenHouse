package com.example.smartgreenhouse.ui.graph;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.smartgreenhouse.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphFragmentTab1 extends Fragment implements View.OnClickListener {

    private LineChart lineChart;
    String[] dist;
    String[] route;
    Integer[] distance;
    Integer[] x;
    Integer[] y;
    Button buttonMon;
    Button buttonTue;
    Button buttonWed;
    Button buttonThu;
    Button buttonFri;
    Button buttonSat;
    Button buttonSun;
    int start=0, last=24;
    int j=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_graph_tab1, container, false);
        getJsonObject("jinhak94");
        buttonMon = layout.findViewById(R.id.monButton);
        buttonTue = layout.findViewById(R.id.tueButton);
        buttonWed = layout.findViewById(R.id.wedButton);
        buttonThu = layout.findViewById(R.id.thuButton);
        buttonFri = layout.findViewById(R.id.friButton);
        buttonSat = layout.findViewById(R.id.satButton);
        buttonSun = layout.findViewById(R.id.sunButton);

        buttonMon.setOnClickListener(this);
        buttonTue.setOnClickListener(this);
        buttonWed.setOnClickListener(this);
        buttonThu.setOnClickListener(this);
        buttonFri.setOnClickListener(this);
        buttonSat.setOnClickListener(this);
        buttonSun.setOnClickListener(this);


        lineChart = layout.findViewById(R.id.chart);
        return layout;
    }



    @Override
    public void onResume(){
        super.onResume();
        getJsonObject("jinhak94");
     }

    public void SetRange(int s, int l){
        start = s;
        last = l;
    }



    public void SetGraph(){
        MyMarkerView marker = new MyMarkerView(getActivity(),R.layout.markerviewtext);
        marker.setChartView(lineChart);
        lineChart.setMarker(marker);


        List<Entry> entries = new ArrayList<>();
        for(int i=start; i<last; i++){
            entries.add(new Entry(j, distance[i]/25));
            j++;
        }
        j = 0;


        LineDataSet lineDataSet = new LineDataSet(entries ,"활동량(cm)");
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setCircleColorHole(Color.BLUE);
        lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(8, 24, 0);

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);

        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText("");

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.animateY(2000, Easing.EasingOption.EaseInCubic);
        lineChart.invalidate();
    }

    public void getJsonObject(final String id)
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = getString(R.string.moveUrl);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                // Response
                try
                {
                    JSONObject jsonMain = new JSONObject(response);
                    String result = jsonMain.optString("result");
                    String dt = jsonMain.optString("distance");
                    String rt = jsonMain.optString("route");
                    dist = dt.split(",");
                    route = rt.split(",");
                    distance = new Integer[dist.length];
                    x = new Integer[(route.length)/2];
                    y = new Integer[(route.length)/2];
                    if(result.equals("1"))
                    {
                        for(int i=0; i<dist.length;i++){
                            distance[i] = Integer.parseInt(dist[i]);
                        }
                        for(int j=0; j<route.length; j++){
                            if((j%2)==0){
                                x[j/2] = Integer.parseInt(route[j]);
                            }else{
                                y[j/2] = Integer.parseInt(route[j]);
                            }
                        }
                        SetGraph();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "아이디/비밀번호가 틀렸습니다", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(org.json.JSONException e)
                {
                }
            }
        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                // Error Handling
                Toast.makeText(getContext(), "시스템 오류", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("ID", id);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void setButtonColor(Button b){
        buttonMon.setBackgroundColor(Color.GRAY);
        buttonTue.setBackgroundColor(Color.GRAY);
        buttonWed.setBackgroundColor(Color.GRAY);
        buttonThu.setBackgroundColor(Color.GRAY);
        buttonFri.setBackgroundColor(Color.GRAY);
        buttonSat.setBackgroundColor(Color.GRAY);
        buttonSun.setBackgroundColor(Color.GRAY);
        b.setBackgroundColor(Color.DKGRAY);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.monButton:
                setButtonColor(buttonMon);
                SetRange(0, 24);
                SetGraph();
                break;
            case R.id.tueButton:
                setButtonColor(buttonTue);
                SetRange(25, 47);
                SetGraph();
                break;
            case R.id.wedButton:
                setButtonColor(buttonWed);
                SetRange(48, 71);
                SetGraph();
                break;
            case R.id.thuButton:
                setButtonColor(buttonThu);
                SetRange(72, 95);
                SetGraph();
                break;
            case R.id.friButton:
                setButtonColor(buttonFri);
                SetRange(96, 119);
                SetGraph();
                break;
            case R.id.satButton:
                setButtonColor(buttonSat);
                SetRange(120, 143);
                SetGraph();
                break;
            case R.id.sunButton:
                setButtonColor(buttonSun);
                SetRange(144, 167);
                SetGraph();
                break;
        }
    }

}


