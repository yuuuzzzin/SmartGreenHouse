package com.example.smartgreenhouse.ui.MyPlant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
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
import com.example.smartgreenhouse.ui.plantInfo.PlantSettingActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class UserPlantInfoActivity extends AppCompatActivity {


    private TextView txtPosition, txtNickname;
    TextView txtInfo;
    String Position, info, jsoninfo;
    String positionNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_plant_info);
        Intent intent = getIntent();
        txtPosition = findViewById(R.id.txtPosition);
        txtNickname = findViewById(R.id.txtNickname);

        txtInfo = findViewById(R.id.txtInfo);
        //selectButton = findViewById(R.id.selectButton);
        txtPosition.setText(intent.getStringExtra("position"));
        txtNickname.setText(intent.getStringExtra("nickname"));
        Position = txtPosition.getText().toString();

        //char last = Position .charAt(Position .length() - 1);
        String id = Position.substring(0, Position.length()-1);
       // positionNum = Character.toString(last);

        getUserPlantInfo(id, Position);
       // Toast.makeText(getApplicationContext(), "정보 가져오기 완료", Toast.LENGTH_SHORT).show();

    }

    private void jsonParsing(String json)
    {
        StringBuffer sb = new StringBuffer();
        json = "["+json+"]";

        try{
            JSONArray jarray = new JSONArray(json);
            for(int i=0; i<jarray.length(); i++)
            {
                JSONObject jObject = jarray.getJSONObject(i);
                String num = jObject.optString("cntntsNo");
                String name = jObject.optString("plntbneNm");
                String adviseinfo = jObject.optString("adviseInfo");
                sb.append(
                        "컨텐츠번호 : " +num+ "\n"+
                                "식물명:" +name+ "\n"+
                                "정보:" +adviseinfo+ "\n"
                );
            }
            txtInfo.setText(sb.toString());
        }catch (JSONException e) {
            Log.d("캐치리스트출력에러", e.toString());
            e.printStackTrace();
        }
    }

    public void getUserPlantInfo(final String id, final String position)
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String Url = getString(R.string.getUserPlantInfoUrl); //온습도를 받아오는 url
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
                        info = jObject.optString("info");
                        txtInfo.setText(info);
                        jsoninfo = txtInfo.getText().toString();
                        jsonParsing(jsoninfo);
                        //txtInfo.setText(jsonParsing(info));
                    }
                    else
                    {
                        txtInfo.setText("?");
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
                params.put("position", position);
                return params;
            }
        };
        queue.add(stringRequest);
    }

}