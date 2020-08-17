package com.example.smartgreenhouse.ui.plantInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PlantSettingActivity extends AppCompatActivity {

    private TextView plantName, inputDate;
    private EditText inputName;
    private Button btnFinish;
    String Name, Code, Info, Total;
    String txtname, txtpostion;
    String txtid;

    long now = System.currentTimeMillis();
    Date date = new Date(now);    // 현재시간을 date 변수에 저장
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd");  // 시간을 나타냇 포맷을 정함
    String formatDate = sdfNow.format(date);   // nowDate 변수에 값을 저장

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_setting);
        Intent intent = getIntent();
        final String finalID = readId();

        Name = intent.getStringExtra("name");
        Code = intent.getStringExtra("code");
        Info = intent.getStringExtra("total");
        //Info = intent.getStringExtra("total");

       // Toast.makeText(getApplicationContext(), Info, Toast.LENGTH_SHORT).show();

        btnFinish = findViewById(R.id.btnFinish);
        plantName = findViewById(R.id.plantName);
        inputDate = findViewById(R.id.inputDate);
        inputName = (EditText) findViewById(R.id.inputName);
        plantName.setText(Name);

        inputDate.setText(formatDate);

        btnFinish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "클릭", Toast.LENGTH_SHORT).show();
                txtname = inputName.getText().toString();

                setPlantInfo(finalID, Info, txtname);
                Intent intent = new Intent(PlantSettingActivity.this, MainActivity.class);
                startActivity(intent);

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

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void setPlantInfo(final String id, final String info, final String nickname)
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String setPlantInfoUrl = getString(R.string.setPlantInfoUrl);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, setPlantInfoUrl, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.d("onResponse", response);
                // Response
                try
                {
                    JSONArray jarray = new JSONObject(response).getJSONArray("List");
                    JSONObject jObject = jarray.getJSONObject(0);
                    String result = jObject.optString("RESULT");
                    if(result.equals("1"))
                    {
                        Toast.makeText(getApplicationContext(), "식물 정보 저장 완료", Toast.LENGTH_SHORT).show();
                    }
                    /*if(result.equals("2"))
                    {
                        Toast.makeText(getApplicationContext(), "꽉 참", Toast.LENGTH_SHORT).show();
                    }*/
                    else
                    {
                        Toast.makeText(getApplicationContext(), "식물 정보 저장 실패(식물은 최대 3개까지 등록할 수 있습니다.)", Toast.LENGTH_LONG).show();
                    }
                }
                catch(org.json.JSONException e)
                {
                    Toast.makeText(getApplicationContext(), "캐치 에러", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                // Error Handling
                Toast.makeText(getApplicationContext(), "시스템 오류", Toast.LENGTH_SHORT).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                // params.put("position", position);
                params.put("info", info);
                params.put("nickname", nickname);
                return params;
            }
        };
        queue.add(stringRequest);
    }
}