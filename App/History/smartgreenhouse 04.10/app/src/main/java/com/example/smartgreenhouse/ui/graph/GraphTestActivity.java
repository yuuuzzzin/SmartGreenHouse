package com.example.smartgreenhouse.ui.graph;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartgreenhouse.R;
import android.graphics.Color; import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart; import com.github.mikephil.charting.data.Entry; import com.github.mikephil.charting.data.LineData; import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.ArrayList;
import java.util.Map;

public class GraphTestActivity extends AppCompatActivity {

    String txttemp, txtdate, txthumi, txtsoil1, txtsoil2, txtsoil3, txtcds, txtlevel;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_graph_test);
        LineChart lineChart = (LineChart)findViewById(R.id.chart);
        getPlantStatus(readId());
        List<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(10,12 ));

        Toast.makeText(getApplicationContext(), txtdate, Toast.LENGTH_SHORT).show();

        LineDataSet dataSet = new LineDataSet(entries, "Label");
        dataSet.setColor(Color.red(1));
        dataSet.setValueTextColor(Color.blue(1));
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData); lineChart.invalidate();
    }


    public void getPlantStatus(final String id)
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String getPlantStatusUrl = getString(R.string.getPlantStatusUrl); // DB에 저장된 센서 값 불러오는 URL
        //Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getPlantStatusUrl, new Response.Listener<String>()
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
                        String date = jObject.optString("date");
                        String temp = jObject.optString("temp");
                        String humi = jObject.optString("humi");
                        String soil1 = jObject.optString("soil1");
                        String soil2 = jObject.optString("soil2");
                        String soil3 =jObject.optString("soil3");
                        String cds = jObject.optString("cds");
                        String level = jObject.optString("level");

                        txtdate = date;
                        //textTemperature.setText(temp + "Cº");
                        //textHumidity.setText(humi + "%");
                        //textSoil1.setText(soil1 + "%");
                        //textSoil2.setText(soil2 + "%");
                        //textSoil3.setText(soil3 + "%");
                        //textCds.setText(cds + "%");
                        //textLevel.setText(level + "%");
                    }
                    else
                    {
                        /*
                        textDate.setText("?");
                        textTemperature.setText("?");
                        textHumidity.setText("?");
                        textSoil1.setText("?");
                        textSoil2.setText("?");
                        textSoil3.setText("?");
                        textCds.setText("?");
                        textLevel.setText("?");

                         */
                    }
                }
                catch(JSONException e)
                {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
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
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public String readId()
    {
        String str = null;
        try{
            BufferedReader br = new BufferedReader(new FileReader(getApplicationContext().getFilesDir() + "/" + "id.txt"));
            str = null;
            str = br.readLine();
            //Toast.makeText(getApplicationContext(), str+"readId 함수 안", Toast.LENGTH_SHORT).show();
        } catch (IOException e){
            e.printStackTrace();
        } catch(Exception e) {
            Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_SHORT).show();
        }
        return str;
    }

}