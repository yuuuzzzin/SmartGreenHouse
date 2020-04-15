package com.example.smartgreenhouse.ui.graph;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.List;
import java.util.Map;

public class ChartActivity extends AppCompatActivity {

    TextView textTemperature, textHumidity, textSoil1, textSoil2, textSoil3, textLevel,textCds,textDate;
    private final String zero = "0 / 100";

    String txttemp, txtdate, txthumi, txtsoil1, txtsoil2, txtsoil3, txtcds, txtlevel;
    int x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        textTemperature = (TextView) findViewById(R.id.txtTemperature);

        final String readID = readId();
        //Toast.makeText(getApplicationContext(), readID+"함수 밖", Toast.LENGTH_SHORT);
/*
        textTemperature = (TextView)findViewById(R.id.txtTemp);
        textHumidity = (TextView)findViewById(R.id.txtHum);
        textDate = (TextView)findViewById(R.id.txtDate);
        textSoil1 = (TextView)findViewById(R.id.txtSoil1);
        textSoil2 = (TextView)findViewById(R.id.txtSoil2);
        textSoil3 = (TextView)findViewById(R.id.txtSoil3);
        textCds = (TextView)findViewById(R.id.txtCds);
        textLevel = (TextView)findViewById(R.id.txtLevel);
*/
        getPlantStatus(readID);

       // int intx = Integer.parseInt(txttemp);


    }

    public void initStatus()
    {
        textHumidity.setText(zero);
        textTemperature.setText(zero);
        textDate.setText("");
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

                       // txtdate = date;
                       // txttemp = temp;

                        //Toast.makeText(getApplicationContext(), txtdate, Toast.LENGTH_SHORT).show();
                        textTemperature.setText(temp);
                        txttemp = textTemperature.getText().toString();
                        txthumi = humi;

                        x = Integer.parseInt(txttemp);
                        int x2 = Integer.parseInt(humi);
                        //textHumidity.setText(humi);
                        //textSoil1.setText(soil1 + "%");
                        //textSoil2.setText(soil2 + "%");
                        //textSoil3.setText(soil3 + "%");
                        //textCds.setText(cds + "%");
                        //textLevel.setText(level + "%");

                        LineChart lineChart = (LineChart)findViewById(R.id.chart);
                        List<Entry> entries = new ArrayList<Entry>();
                       // entries.add(new Entry(x,12 ));
                        entries.add(new Entry(x2,13 ));

                        Toast.makeText(getApplicationContext(), txttemp, Toast.LENGTH_SHORT).show();

                        LineDataSet dataSet = new LineDataSet(entries, "Label");
                        dataSet.setColor(Color.red(1));
                        dataSet.setValueTextColor(Color.blue(1));
                        LineData lineData = new LineData(dataSet);
                        lineChart.setData(lineData); lineChart.invalidate();
                    }
                    else
                    {
                        textDate.setText("?");
                        textTemperature.setText("?");
                        textHumidity.setText("?");
                        textSoil1.setText("?");
                        textSoil2.setText("?");
                        textSoil3.setText("?");
                        textCds.setText("?");
                        textLevel.setText("?");
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
}
