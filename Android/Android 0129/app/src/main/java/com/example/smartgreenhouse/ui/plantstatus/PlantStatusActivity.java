package com.example.smartgreenhouse.ui.plantstatus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.example.smartgreenhouse.MainActivity;
import com.example.smartgreenhouse.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PlantStatusActivity extends AppCompatActivity {

    TextView textTemperature, textHumidity, textSoil1, textSoil2, textSoil3, textWater,textCds,textDate;
    Button btnShowStreaming, btnInstantFeed, btnDeleteAutoFeed, btnSetFeeding;
    Spinner spinner;
    ProgressBar progressTemp, progressHumi, progressFeed;
    private boolean runFlag = false;
    private final String zero = "0 / 100";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_status);

        final String finalID = readId();
        //Toast.makeText(getApplicationContext(), finalID+"함수 밖", Toast.LENGTH_SHORT);

        textTemperature = (TextView)findViewById(R.id.txtTemp);
        textHumidity = (TextView)findViewById(R.id.txtHum);
        textDate = (TextView)findViewById(R.id.txtDate);
        textSoil1 = (TextView)findViewById(R.id.txtSoil1);
        textSoil2 = (TextView)findViewById(R.id.txtSoil2);
        textSoil3 = (TextView)findViewById(R.id.txtSoil3);
        textCds = (TextView)findViewById(R.id.txtCds);
        textWater = (TextView)findViewById(R.id.txtWater);

        //Toast.makeText(getApplicationContext(), finalID+"함수 밖", Toast.LENGTH_SHORT);

        Thread getThread;
        final Handler mHandler = new Handler();
        getThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(;;) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            getPlantStatus(finalID);
                            //Toast.makeText(getApplicationContext(), finalID + "아이디 보내기 성공", Toast.LENGTH_SHORT).show();
                        }
                    });
                    try
                    {
                        Thread.sleep(2000); //5초마다 온습도 정보 가져오게 하기위해서
                    }
                    catch(Exception e)
                    {
                        initStatus();
                        e.printStackTrace();
                    }
                }
            }
        });

        getThread.start();
    }

    public void initStatus()
    {
        textHumidity.setText(zero);
        progressHumi.setProgress(0);
        textTemperature.setText(zero);
        progressTemp.setProgress(0);
        textDate.setText("");

        progressFeed.setProgress(0);
    }

    public void setStatus(String temp, String humi, String soil1, String soil2, String soil3, String cds, String water)
    {
        int Temp = Integer.parseInt(temp);
        int Humi = Integer.parseInt(humi);
        int Soil1 = Integer.parseInt(soil1);
        int Soil2 = Integer.parseInt(soil2);
        int Soil3 = Integer.parseInt(soil3);
        int Cds = Integer.parseInt(cds);
        int Water = Integer.parseInt(water);

        textTemperature.setText(temp + "Cº");
        progressTemp.setProgress(Temp);
        textHumidity.setText(humi + "%");
        progressHumi.setProgress(Humi);
        textSoil1.setText(soil1 + "%");
        textSoil2.setText(soil2 + "%");
        textSoil3.setText(soil3 + "%");
        textCds.setText(cds + "%");
        textWater.setText(water + "%");
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
        String getPlantStatusUrl = getString(R.string.getPlantStatusUrl); //온습도를 받아오는 url
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
                        String water = jObject.optString("water");

                        textDate.setText(date);
                        textTemperature.setText(temp + "Cº");
                        textHumidity.setText(humi + "%");
                        textSoil1.setText(soil1 + "%");
                        textSoil2.setText(soil2 + "%");
                        textSoil3.setText(soil3 + "%");
                        textCds.setText(cds + "%");
                        textWater.setText(water + "%");
                    }
                    else
                    {
                        textHumidity.setText("?");

                        textTemperature.setText("?");

                        textDate.setText("");
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