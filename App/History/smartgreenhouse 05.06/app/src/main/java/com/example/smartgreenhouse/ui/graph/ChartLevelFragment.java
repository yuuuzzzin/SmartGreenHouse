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

public class ChartLevelFragment extends Fragment {
    EditText year, month, day;
    Button button;
    private final String zero = "0 / 100";
    int x;
    float y1, y2, y3, y4, y5, y6, y7, y8, y9, y10, y11, y12;
    String userDate;
    Spinner spinner;
    String period;

    public static ChartLevelFragment newInstance() {
        Bundle args = new Bundle();
        ChartLevelFragment fragment = new ChartLevelFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart_level,container,false);
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
                //button.setText(period);

                if(period.equals("주간")){
                    getWeeklyLevel(readId(), userDate);
                }
                else if(period.equals("월간")){
                    getMonthlyLevel(readId(), userDate);
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

    public void getWeeklyLevel(final String id, final String date)
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String Url = getString(R.string.getWeeklyLevelUrl); // DB에 저장된 센서 값 불러오는 URL
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
                        String level = jObject.optString("level1");
                        String level2 = jObject.optString("level2");
                        String level3 = jObject.optString("level3");
                        String level4 = jObject.optString("level4");
                        String level5 = jObject.optString("level5");
                        String level6 = jObject.optString("level6");
                        String level7 = jObject.optString("level7");

                        ArrayList<String> labels = new ArrayList<String>();
                        for(int i=1; i<8; i++){
                            String date = jObject.optString("date" + i);
                            labels.add(date);
                        }

                        y1 = Float.parseFloat(level);
                        y2 = Float.parseFloat(level2);
                        y3 = Float.parseFloat(level3);
                        y4 = Float.parseFloat(level4);
                        y5 = Float.parseFloat(level5);
                        y6 = Float.parseFloat(level6);
                        y7 = Float.parseFloat(level7);

                        LineChart lineChart = (LineChart)getView().findViewById(R.id.chart);
                        ArrayList<Entry> entries = new ArrayList<Entry>();

                        entries.add(new Entry(y1, 0));
                        entries.add(new Entry(y2, 1));
                        entries.add(new Entry(y3, 2));
                        entries.add(new Entry(y4, 3));
                        entries.add(new Entry(y5, 4));
                        entries.add(new Entry(y6, 5));
                        entries.add(new Entry(y7, 6));

                        LineDataSet dataSet = new LineDataSet(entries, "수위");
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

    public void getMonthlyLevel(final String id, final String date)
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String Url = getString(R.string.getMonthlyLevelUrl); // DB에 저장된 센서 값 불러오는 URL
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
                        String level = jObject.optString("level1");
                        String level2 = jObject.optString("level2");
                        String level3 = jObject.optString("level3");
                        String level4 = jObject.optString("level4");
                        String level5 = jObject.optString("level5");
                        String level6 = jObject.optString("level6");
                        String level7 = jObject.optString("level7");
                        String level8 = jObject.optString("level8");
                        String level9 = jObject.optString("level9");
                        String level10 = jObject.optString("level10");
                        String level11 = jObject.optString("level11");
                        String level12 = jObject.optString("level12");

                        ArrayList<String> labels = new ArrayList<String>();
                        for(int i=1; i<13; i++){
                            String date = jObject.optString("date" + i);
                            labels.add(date);
                        }

                        y1 = Float.parseFloat(level);
                        y2 = Float.parseFloat(level2);
                        y3 = Float.parseFloat(level3);
                        y4 = Float.parseFloat(level4);
                        y5 = Float.parseFloat(level5);
                        y6 = Float.parseFloat(level6);
                        y7 = Float.parseFloat(level7);
                        y8 = Float.parseFloat(level8);
                        y9 = Float.parseFloat(level9);
                        y10 = Float.parseFloat(level10);
                        y11 = Float.parseFloat(level11);
                        y12 = Float.parseFloat(level12);

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

                        LineDataSet dataSet = new LineDataSet(entries, "수위");
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