package com.example.smartgreenhouse.ui.reservation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
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

public class LedReservationActivity extends AppCompatActivity {
    Button btnStart, btnEnd, btnReserve;

    CalendarView calendar;
    TimePicker time;
    TextView txtreserve, txtreserve2;
    String date = " ";
    String date2 = " ";
    String txtStart, txtEnd;
    String txtHour, txtMinute, txtMonth, txtdayOfMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led_reservation);

        final String readID = readId();

        //b1 = (Button)findViewById(R.id.button);
        btnStart = (Button) findViewById(R.id.button2);
        btnEnd = (Button) findViewById(R.id.button3);
        btnReserve = (Button) findViewById(R.id.button4);

        time = (TimePicker) findViewById(R.id.timePicker);
        txtreserve = (TextView) findViewById(R.id.textView);
        txtreserve2 = (TextView) findViewById(R.id.textView2);

        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (time.getHour() <10){
                    txtHour = "0"+ time.getHour();
                }else if(time.getHour()>9) {
                    txtHour = Integer.toString(time.getHour());
                }
                if(time.getMinute()<10){
                    txtMinute = "0"+time.getMinute();
                }else if(time.getMinute()>9){
                    txtMinute = Integer.toString(time.getMinute());
                }

                String times = txtHour + ":" + txtMinute;
                txtreserve2.setText(times);


                // txtEnd = txtreserve2.getText().toString();
                // Toast.makeText(getApplicationContext(), txtEnd, Toast.LENGTH_SHORT).show();
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (time.getHour() <10){
                    txtHour = "0"+ time.getHour();
                }else if(time.getHour()>9) {
                    txtHour = Integer.toString(time.getHour());
                }
                if(time.getMinute()<10){
                    txtMinute = "0"+time.getMinute();
                }else if(time.getMinute()>9){
                    txtMinute = Integer.toString(time.getMinute());
                }
                String times = txtHour + ":" + txtMinute ;
                txtreserve.setText(times);

                //txtStart = txtreserve.getText().toString();
                //Toast.makeText(getApplicationContext(), txtStart, Toast.LENGTH_SHORT).show();

            }
        });
        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtStart = txtreserve.getText().toString();
                //Toast.makeText(getApplicationContext(), txtStart, Toast.LENGTH_SHORT).show();
                txtEnd = txtreserve2.getText().toString();
                //Toast.makeText(getApplicationContext(), txtStart +" /// " +txtEnd, Toast.LENGTH_SHORT).show();
                reserveLed(readID, txtStart, txtEnd);
                Toast.makeText(getApplicationContext(), "시간 설정을 완료했습니다.", Toast.LENGTH_SHORT).show();
            }
        });




        //reserveLed(readID, txtStart);
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


    public void reserveLed(final String id, final String start, final String end)
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String Url = getString(R.string.reservedLedUrl); //온습도를 받아오는 url
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
                        Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
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
                params.put("start", start);
                params.put("end", end);
                return params;
            }
        };
        queue.add(stringRequest);
    }

}
