package com.example.smartgreenhouse.ui.graph;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphFragmentTab2 extends Fragment {

    private LineChart lineChart;
    String[] dist;
    Integer[] distance;
    Integer[] weeklyDistance;
    int sum = 0;
    public String readID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_graph_tab2, container, false);
        readID = readId();
        getJsonObject(readID);
        lineChart = layout.findViewById(R.id.chart);

        return layout;
    }

    @Override
    public void onResume(){
        super.onResume();
        getJsonObject(readID);
    }

    public void SetGraph(){
        MyMarkerView marker = new MyMarkerView(getActivity(), R.layout.markerviewtext);

        marker.setChartView(lineChart);
        lineChart.setMarker(marker);


        List<Entry> entries = new ArrayList<>();
        for(int i=0; i<7; i++){
            entries.add(new Entry(i, weeklyDistance[i]/25));
        }


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

    public String readId()
    {
        String str = null;
        try{
            BufferedReader br = new BufferedReader(new FileReader(getContext().getFilesDir() + "/" + "id.txt"));
            str = null;
            str = br.readLine();
            //Toast.makeText(getApplicationContext(), str+"readId 함수 안", Toast.LENGTH_SHORT).show();
        } catch (IOException e){
            e.printStackTrace();
        } catch(Exception e) {
            Toast.makeText(getContext(), "Exception", Toast.LENGTH_SHORT).show();
        }
        return str;
    }

    public void getJsonObject(final String id)
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String Url = getString(R.string.getGraphInformationeUrl); //온습도를 받아오는 url
        //Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                // Response
                try
                {
                    //JSONArray jarray = new JSONObject(response).getJSONArray("List");
                    //JSONObject jObject = jarray.getJSONObject(0);
                    //JSONObject distanceJSON = new JSONObject(response).getJSONObject("distance");
                    //JSONObject routeJSON = new JSONObject(response).getJSONObject("route");
                    //JSONObject resultJSON = new JSONObject(response).getJSONObject("result");

                    JSONObject jsonMain = new JSONObject(response);
                    String result = jsonMain.optString("result");
                    String cds = jsonMain.optString("temp");
                    dist = cds.split(",");
                    distance = new Integer[dist.length];
                    weeklyDistance = new Integer[8];
                   for(int i = 0; i < weeklyDistance.length; i++){
                        weeklyDistance[i] = 0;
                    }
                    if(result.equals("1"))
                    {
                        for(int i=0; i<dist.length;i++){
                            distance[i] = Integer.parseInt(dist[i]);
                            weeklyDistance[i/24]+=distance[i];
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
                //Toast.makeText(getContext(), "시스템 오류", Toast.LENGTH_SHORT).show();
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

}


