package com.example.smartgreenhouse.ui.community;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartgreenhouse.R;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class CommunityArticleReadActivity extends AppCompatActivity {

    String result;

    TextView textView_hit;
    TextView textView_content;
    TextView textView_time;
    TextView textView_writer_id;
    TextView textView_title;

    String content;
    String title;
    String time;

    String str;
    Integer value;
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_community_article_read);

        textView_hit = findViewById(R.id.hit);
        textView_content = findViewById(R.id.memo);
        textView_time = findViewById(R.id.time);
        textView_writer_id = findViewById(R.id.id);
        textView_title = findViewById(R.id.title);

        //Bundle extras = getIntent().getExtras();
        //if (extras != null) {
          //str   = extras.getLong("input");
        //}
        //if(value==1)
           // Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_LONG).show();
        Intent intent = getIntent();
        str = intent.getStringExtra("position");

        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();

        getContent(str);
    }
    @Override
    public void onResume(){
        super.onResume();
       // getArticle(str);
    }
    public static String getURLDecode(String content){
        try {
            return URLDecoder.decode(content, "utf-8");   // UTF-8
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void getContent(final String idx)
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = getString(R.string.getCommunityReadUrl);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                // Response
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    result = jsonObject.optString("RESULT");
                    if(!(result.equals("1")))
                    {
                        Toast.makeText(getApplicationContext(), "게시글을 불러오지 못했습니다.", Toast.LENGTH_LONG).show();
                    }
                    title = getURLDecode(jsonObject.optString("title"));
                    content = getURLDecode(jsonObject.optString("content"));
                    time = getURLDecode(jsonObject.optString("time"));

                    textView_hit.setText("조회 : " + jsonObject.optString("hit"));
                    textView_content.setText(content);
                    textView_time.setText(time.substring(0,time.length() - 2) + " ");
                    textView_title.setText(title);
                    textView_writer_id.setText(jsonObject.optString("id"));
                }
                catch(org.json.JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                // Error Handling
                Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {

                Map<String, String> params = new HashMap<>();
                params.put("IDX", idx);
                return params;
            }
        };
        queue.add(stringRequest);
    }



}
