package com.example.smartgreenhouse.ui.setting;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartgreenhouse.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettingDeviceActvity extends AppCompatActivity {

    ToggleButton btAuto;
    public String autoStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_device);
        btAuto = (ToggleButton) findViewById(R.id.btAuto);
        getAutoStatus(readId());

        btAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    setAutoStatus(readId(), "on");
                    //Toast.makeText(getContext(), "토글클릭-ON", Toast.LENGTH_SHORT).show();
                }
                else{
                    setAutoStatus(readId(), "off");
                    //Toast.makeText(getContext(), "토글클릭-OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        }
        return str;
    }

    public void getAutoStatus(final String id)
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String getAutoStatusUrl = getString(R.string.getAutoStatusUrl); //온습도를 받아오는 url
        //Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getAutoStatusUrl, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                // Response
                //Toast.makeText(getApplicationContext(), "1success", Toast.LENGTH_SHORT).show();
                try
                {
                    JSONArray jarray = new JSONObject(response).getJSONArray("List"); // 대괄호 구별
                    JSONObject jObject = jarray.getJSONObject(0); // 중괄호 구별
                    String result = jObject.optString("RESULT"); // 아이디가 중복되었을 시에 1을 리턴
                    if(result.equals("1"))
                    {
                        //Toast.makeText(getApplicationContext(), "1success", Toast.LENGTH_SHORT).show();
                        String auto = jObject.optString("auto");

                        autoStatus = auto;
                        if (autoStatus!=null && autoStatus.equals("on")) {
                            btAuto.setChecked(true);
                        } else if (autoStatus!=null && autoStatus.equals("off")) {
                            btAuto.setChecked(false);
                        }

                    }
                    else
                    {
                    }
                }
                catch(JSONException e)
                {

                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                // Error Handling
                Toast.makeText(getApplicationContext(), error.getMessage() + "시스템 오류", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("ID", id);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void setAutoStatus(final String id, final String state)
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String setAutoStatusUrl = getString(R.string.setAutoStatusUrl);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, setAutoStatusUrl, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                // Response
                try
                {
                    JSONArray jarray = new JSONObject(response).getJSONArray("List");
                    JSONObject jObject = jarray.getJSONObject(0);
                    String result = jObject.optString("RESULT");
                    if(result.equals("1"))
                    {
                        Toast.makeText(getApplicationContext(), state + "상태 변경 완료", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), state + "상태 변경 실패", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(JSONException e)
                {
                    Log.e("Alerttt","JSON error",e);
                    //Toast.makeText(getApplicationContext(), state + "캐치에러", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                // Error Handling
                Toast.makeText(getApplicationContext(), error.getMessage() + "시스템 오류", Toast.LENGTH_SHORT).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("ID", id);
                params.put("STATE", state);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
