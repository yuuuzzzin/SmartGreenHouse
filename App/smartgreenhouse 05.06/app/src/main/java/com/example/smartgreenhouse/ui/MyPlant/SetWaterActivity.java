package com.example.smartgreenhouse.ui.MyPlant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SetWaterActivity extends AppCompatActivity {



    private TextView txtPosition;
    String Position;
    String positionNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_water);

        Intent intent = getIntent();
        txtPosition = findViewById(R.id.txtPosition);
        //selectButton = findViewById(R.id.selectButton);
        txtPosition.setText(intent.getStringExtra("position"));

        Position = txtPosition.getText().toString();

        char last = Position .charAt(Position .length() - 1);
        String id = Position.substring(0, Position.length()-1);
        positionNum = Character.toString(last);

        setWaterStatus(id, "on", positionNum);
        Toast.makeText(getApplicationContext(), "물주기 설정 완료", Toast.LENGTH_SHORT).show();
        finish();

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


    public void setWaterStatus(final String id, final String water, final String position)
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String Url = getString(R.string.setOneWaterUrl); //온습도를 받아오는 url
        //Toast.makeText(getContext(), led + "상태", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>()
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
                        Toast.makeText(getApplicationContext(), water + "water 상태 변경 완료", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), water + "water 상태 변경 실패", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), error.getMessage() + "시스템 오류", Toast.LENGTH_SHORT).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("ID", id);
                params.put("WATER", water);
                params.put("position", position);
                return params;
            }
        };
        queue.add(stringRequest);
    }

}
