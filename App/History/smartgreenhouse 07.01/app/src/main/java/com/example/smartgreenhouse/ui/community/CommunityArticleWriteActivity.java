package com.example.smartgreenhouse.ui.community;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

// JSON 생성까지 했고 https://maenan.tistory.com/6에서 서버 전송하는 부분 구현 필요
// JSON 형식은
// {"title":"제목","content":"내용","id":"아이디","password":"패스워드","time":"작성시간"}

public class CommunityArticleWriteActivity extends AppCompatActivity {

    private EditText title;
    private EditText content;
    private Button savebtn;
    private Button backbtn;
    SharedPreferences setting;

    String time;
    private Timer mTimer;
    String str_title;
    String str_content;
    String tm;

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

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_article_write);
        final String readID = readId();

        //Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        savebtn = findViewById(R.id.save_btn);
        backbtn = findViewById(R.id.back_btn);

        MainTimerTask timerTask = new MainTimerTask();
        mTimer = new Timer();
        mTimer.schedule(timerTask, 500, 1000);

        final JSONObject jsonObject = new JSONObject();

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(content.getText().toString().length()!=0 &&
                        title.getText().toString().length()!=0) {
                    str_title = title.getText().toString();
                    str_content = content.getText().toString();
                    String subTime1 = time.substring(0,10);
                    String subTime2 = time.substring(11,19);
                    tm = subTime1 + " " + subTime2;
                    //Toast.makeText(getApplicationContext(), "클릭", Toast.LENGTH_SHORT).show();
                    String utf_title = "", utf_content = "", utf_id = "", utf_time = "";
                    try {
                        utf_title = URLEncoder.encode(str_title, "UTF-8");
                        utf_content = URLEncoder.encode(str_content, "UTF-8");
                        utf_id = URLEncoder.encode(readID, "UTF-8");
                        utf_time = URLEncoder.encode(time, "UTF-8");
                        Toast.makeText(getApplicationContext(), "클릭", Toast.LENGTH_SHORT).show();
                    } catch(Exception e) {

                    }

                   // Toast.makeText(getApplicationContext(), str_title, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(), str_content, Toast.LENGTH_SHORT).show();
                   // Toast.makeText(getApplicationContext(), readID, Toast.LENGTH_SHORT).show();
                   // Toast.makeText(getApplicationContext(), time, Toast.LENGTH_SHORT).show();

                    putServer(str_title, str_content, readID, time);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"제목과 내용을 모두 입력해주세요!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private Handler mHandler = new Handler();

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {

            Date rightNow = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            time = formatter.format(rightNow);
        }
    };

    class MainTimerTask extends TimerTask {
        public void run() {
            mHandler.post(mUpdateTimeTask);
        }
    }
    @Override
    protected void onDestroy() {
        mTimer.cancel();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mTimer.cancel();
        super.onPause();
    }
    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onResume() {
        MainTimerTask timerTask = new MainTimerTask();
        mTimer.schedule(timerTask, 500, 3000);
        super.onResume();
    }

    public void putServer(final String getTitle, final String getContent, final String getId, final String getTime)
    {
        RequestQueue postRequestQueue = Volley.newRequestQueue(this);
        String url = getString(R.string.writeUrl);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    String result = jsonObject.optString("RESULT");
                    if(result.equals("1"))
                    {
                        Toast.makeText(getApplicationContext(), "hello.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "not hello", Toast.LENGTH_SHORT).show();

                    }
                }
                catch(org.json.JSONException e)
                {
                    e.printStackTrace();
                }
                finish();
            }
        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                // Error Handling
                Toast.makeText(getApplicationContext(), "여기"+error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            //2019-06-21%21:23:35
            @Override
            protected Map<String,String> getParams() throws AuthFailureError
            {
                //Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_SHORT).show();
                Map<String, String> params = new HashMap<>();
                params.put("TITLE", getTitle);
                params.put("CONTENT", getContent);
                params.put("ID", getId);
                params.put("TIME", getTime);
                return params;
            }
        };
        postRequestQueue.add(stringRequest);
    }

}