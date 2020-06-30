package com.example.smartgreenhouse.ui.MyPlant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import java.io.InputStream;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class UserPlantInfoActivity extends AppCompatActivity {

    private TextView txtPosition, txtNickname;
    TextView txtInfo, path;
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
        path = findViewById(R.id.tv);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
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
                String height = jObject.optString("growthHgInfo");
                String width = jObject.optString("growthAraInfo");
                String level = jObject.optString("managelevelCodeNm");
                String temp = jObject.optString("grwhTpCodeNm");
                String soil = jObject.optString("soilInfo");
                String ferti = jObject.optString("frtlzrInfo");
                String spring = jObject.optString("watercycleSprngCodeNm");
                String summer = jObject.optString("watercycleSummerCodeNm");
                String autumn = jObject.optString("watercycleAutumnCodeNm");
                String winter = jObject.optString("watercycleWinterCodeNm");
                String url = jObject.optString("image");



                sb.append(
                        "컨텐츠번호 : " +num+ "\n"+
                                "식물명:" +name+ "\n"+
                                "정보 :" +adviseinfo+ "\n" +
                                "관리 수준 :" +level+ "\n" +
                                "성장 높이:" +height + "\n" +
                                "성장 넓이:" +width+ "\n" +
                                "생육 온도 :"+temp +"\n"+
                               // "봄 물주기 :"+spring+"\n"+
                                "여름 물주기 :"+summer+"\n"+
                              //  "가을 물주기 :"+autumn+"\n"+
                                "겨울 물주기 :"+winter+"\n"+
                                "토양 정보 :"+soil+"\n"+
                                "비료 정보 :"+ferti+"\n"

                );


                    path.setText(url);
                    final String stringpath = path.getText().toString();

                    try {

                        final ImageView iv = (ImageView) findViewById(R.id.imageurl);
                        URL url2 = new URL(stringpath);
                        InputStream is = url2.openStream();
                        final Bitmap bm = BitmapFactory.decodeStream(is);

                        iv.setImageBitmap(bm); //비트맵 객체로 보여주기
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "이미지불러오기 실패", Toast.LENGTH_SHORT).show();
                    }

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