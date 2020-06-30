package com.example.smartgreenhouse.ui.graph;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartgreenhouse.R;
import com.example.smartgreenhouse.ui.Account.Tab_findID;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChartTempFragment extends Fragment {
    EditText year, month, day;
    Button button;
    private final String zero = "0 / 100";
    int x;
    float y1, y2, y3, y4, y5, y6, y7, y8, y9, y10, y11, y12;
    String userDate;
    LineChart lineChart;
    Spinner spinner;
    String period;

    public static ChartTempFragment newInstance() {
        Bundle args = new Bundle();
        ChartTempFragment fragment = new ChartTempFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart_temp,container,false);

        lineChart = view.findViewById(R.id.chart);
        year = (EditText)view.findViewById(R.id.year);
        month = (EditText)view.findViewById(R.id.month);
        day = (EditText)view.findViewById(R.id.day);
        button = (Button)view.findViewById(R.id.button);
        spinner = (Spinner)view.findViewById((R.id.spinner));

        final String readID = readId();

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                userDate = (year.getText().toString() + "-" + month.getText().toString() + "-" + day.getText().toString());
                period = spinner.getSelectedItem().toString();
                //Toast.makeText(getContext(), period, Toast.LENGTH_SHORT).show();
                if(period.equals("주간")){
                    getWeeklyTemp(readId(), userDate);
                }
                else if(period.equals("월간")){
                    getMonthlyTemp(readId(), userDate);
                }
            }
        });
        return view;
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

    public void getWeeklyTemp(final String id, final String date)
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String Url = getString(R.string.getWeeklyTempUrl); // DB에 저장된 센서 값 불러오는 URL
        //Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                // Response
                try
                {
                    JSONArray jarray = new JSONObject(response).getJSONArray("List"); // 대괄호 구별
                    JSONObject jObject = jarray.getJSONObject(0); // 중괄호 구별
                    String result = jObject.optString("RESULT"); // 아이디가 중복되었을 시에 1을 리턴
                    if(result.equals("1"))
                    {
                        String temp = jObject.optString("temp1");
                        String temp2 = jObject.optString("temp2");
                        String temp3 = jObject.optString("temp3");
                        String temp4 = jObject.optString("temp4");
                        String temp5 = jObject.optString("temp5");
                        String temp6 = jObject.optString("temp6");
                        String temp7 = jObject.optString("temp7");

                        ArrayList<String> labels = new ArrayList<String>();
                        for(int i=1; i<8; i++){
                            String date = jObject.optString("date" + i);
                            labels.add(date);
                        }

                        y1 = Float.parseFloat(temp);
                        y2 = Float.parseFloat(temp2);
                        y3 = Float.parseFloat(temp3);
                        y4 = Float.parseFloat(temp4);
                        y5 = Float.parseFloat(temp5);
                        y6 = Float.parseFloat(temp6);
                        y7 = Float.parseFloat(temp7);

                        ArrayList<Entry> entries = new ArrayList<Entry>();

                        entries.add(new Entry(y1, 0));
                        entries.add(new Entry(y2, 1));
                        entries.add(new Entry(y3, 2));
                        entries.add(new Entry(y4, 3));
                        entries.add(new Entry(y5, 4));
                        entries.add(new Entry(y6, 5));
                        entries.add(new Entry(y7, 6));

                        LineDataSet dataSet = new LineDataSet(entries, "온도");
                        dataSet.setColor(Color.red(1));
                        dataSet.setValueTextColor(Color.blue(1));
                        dataSet.setColor(Color.BLUE);

                        LineData lineData = new LineData(labels, dataSet);
                        Toast.makeText(getContext(), labels.get(6), Toast.LENGTH_SHORT).show();
                        lineChart.setData(lineData);
                        lineChart.animateY(5000);
                    }
                    else
                    {
                    }
                }
                catch(JSONException e)
                {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                // Error Handling
                Toast.makeText(getContext(), "해당 날짜에 저장된 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), error.getMessage() + "시스템 오류", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("date", date);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void getMonthlyTemp(final String id, final String date)
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String Url = getString(R.string.getMonthlyTempUrl); // DB에 저장된 센서 값 불러오는 URL
        //Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                // Response
                try
                {
                    JSONArray jarray = new JSONObject(response).getJSONArray("List"); // 대괄호 구별
                    JSONObject jObject = jarray.getJSONObject(0); // 중괄호 구별
                    String result = jObject.optString("RESULT"); // 아이디가 중복되었을 시에 1을 리턴
                    if(result.equals("1"))
                    {
                        String temp = jObject.optString("temp1");
                        String temp2 = jObject.optString("temp2");
                        String temp3 = jObject.optString("temp3");
                        String temp4 = jObject.optString("temp4");
                        String temp5 = jObject.optString("temp5");
                        String temp6 = jObject.optString("temp6");
                        String temp7 = jObject.optString("temp7");
                        String temp8 = jObject.optString("temp8");
                        String temp9 = jObject.optString("temp9");
                        String temp10 = jObject.optString("temp10");
                        String temp11 = jObject.optString("temp11");
                        String temp12 = jObject.optString("temp12");

                        ArrayList<String> labels = new ArrayList<String>();
                        for(int i=1; i<13; i++){
                            String date = jObject.optString("date" + i);
                            labels.add(date);
                        }

                        y1 = Float.parseFloat(temp);
                        y2 = Float.parseFloat(temp2);
                        y3 = Float.parseFloat(temp3);
                        y4 = Float.parseFloat(temp4);
                        y5 = Float.parseFloat(temp5);
                        y6 = Float.parseFloat(temp6);
                        y7 = Float.parseFloat(temp7);
                        y8 = Float.parseFloat(temp8);
                        y9 = Float.parseFloat(temp9);
                        y10 = Float.parseFloat(temp10);
                        y11 = Float.parseFloat(temp11);
                        y12 = Float.parseFloat(temp12);

                        LineChart lineChart = (LineChart)getView().findViewById(R.id.chart);
                        ArrayList<Entry> entries = new ArrayList<Entry>();

                        entries.add(new Entry(y1, 0));
                        entries.add(new Entry(y2, 1));
                        entries.add(new Entry(y3, 2));
                        entries.add(new Entry(y4, 3));
                        entries.add(new Entry(y5, 4));
                        entries.add(new Entry(y6, 5));
                        entries.add(new Entry(y7, 6));
                        entries.add(new Entry(y8, 7));
                        entries.add(new Entry(y9, 8));
                        entries.add(new Entry(y10, 9));
                        entries.add(new Entry(y11, 10));
                        entries.add(new Entry(y12, 11));

                        LineDataSet dataSet = new LineDataSet(entries, "온도");
                        dataSet.setColor(Color.red(1));
                        dataSet.setValueTextColor(Color.blue(1));
                        dataSet.setColor(Color.BLUE);

                        LineData lineData = new LineData(labels, dataSet);
                        lineChart.setData(lineData);
                        lineChart.animateY(5000);
                    }
                    else
                    {
                    }
                }
                catch(JSONException e)
                {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                // Error Handling
                Toast.makeText(getContext(), "해당 날짜에 저장된 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), error.getMessage() + "시스템 오류", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("date", date);
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
