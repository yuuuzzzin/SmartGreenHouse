package com.example.smartgreenhouse;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartgreenhouse.ui.plantstatus.PlantStatusFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.view.View;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String waterLevel;
    int levelData = 0;
    private AppBarConfiguration mAppBarConfiguration;
    TextView tv;
    // BackgroundService
    private Intent mBackgroundServiceIntent;
    private MyService mBackgroundService;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView)findViewById(R.id.tv);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_camera, R.id.nav_share, R.id.nav_email, R.id.nav_graph,  R.id.nav_plantinfo,  R.id.nav_setting, R.id.nav_set, R.id.nav_reservation )
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //인텐트 객체 생성
        Intent intent = new Intent(this, MyService.class);
        //서비스 시작
        startService(intent);

        mBackgroundService = new MyService(getApplicationContext());
        mBackgroundServiceIntent = new Intent(getApplicationContext(), mBackgroundService.getClass());
        // 서비스가 실행 중인지 확인
        if (!BootReceiver.isServiceRunning(this, mBackgroundService.getClass())) {
            // 서비스가 실행하고 있지 않는 경우 서비스 실행
            startService(mBackgroundServiceIntent);
        }


        //getLevelStatus(readId());

        /*Thread getThread;
        final Handler mHandler = new Handler();
        getThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(;;) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            getLevelStatus(readId());
                            //Toast.makeText(getApplicationContext(), waterLevel, Toast.LENGTH_SHORT).show();
                        }
                    });
                    try
                    {
                        Thread.sleep(2000); //5초마다 온습도 정보 가져오게 하기위해서
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
        getThread.start();

        //Toast.makeText(getApplicationContext(), tv.getText().toString(), Toast.LENGTH_SHORT).show();
        //levelData = Integer.parseInt(tv.getText().toString());
        // 물탱크 물 잔량에 따른 알림
        if(levelData<10)
            createNotification();
        else
            removeNotification();*/
    }

    public void end(View v){

        //인텐트 객체 생성

        Intent intent = new Intent(this, MyService.class);

        //서비스 종료시키기

        stopService(intent);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /*private void createNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

        builder.setSmallIcon(R.mipmap.greenhouse);
        builder.setContentTitle("물탱크 물 부족 경고 !");
        builder.setContentText("물탱크에 물이 부족합니다. 채워주세요. 현재 잔량 : " + waterLevel);
        builder.setColor(Color.GREEN);
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setLights(Color.RED, 1, 0);
        // 사용자가 탭을 클릭하면 자동 제거
        builder.setAutoCancel(true);

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        // id값은
        // 정의해야하는 각 알림의 고유한 int값
        notificationManager.notify(1, builder.build());
    }

    private void removeNotification() {
        // Notification 제거
        NotificationManagerCompat.from(this).cancel(1);
    }*/

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

    public void getLevelStatus(final String id)
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
                        String level = jObject.optString("level");
                        tv.setText(level);
                        //Toast.makeText(getApplicationContext(), tv.getText().toString(), Toast.LENGTH_SHORT).show();
                        waterLevel = tv.getText().toString();
                        //Toast.makeText(getApplicationContext(), waterLevel, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
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


