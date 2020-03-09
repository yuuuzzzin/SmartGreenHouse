package com.example.smartgreenhouse.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartgreenhouse.R;
import com.example.smartgreenhouse.ui.MyPlant.ItemTouchHelperCallback;
import com.example.smartgreenhouse.ui.MyPlant.MyPlant;
import com.example.smartgreenhouse.ui.MyPlant.MyPlantAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MyPlantAdapter adapter;
    ItemTouchHelper helper;
    ToggleButton toggleButton1;
    ToggleButton toggleButton2;
    ToggleButton toggleButton3;
    Switch aSwitch;



    TextView textbtn1;//테스트용

    private HomeFragment.OnFragmentInteractionListener mListener;
    public HomeFragment.MyData[] data = null;


    private void init(){

        recyclerView = recyclerView.findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, true));


        //RecyclerView의 Adapter 세팅
        adapter = new MyPlantAdapter(HomeActivity.this);
        recyclerView.setAdapter(adapter);

        //ItemTouchHelper 생성
        helper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter));
        //RecyclerView에 ItemTouchHelper 붙이기
        helper.attachToRecyclerView(recyclerView);

    }



    private void testData() {
        MyPlant myplant1 = new MyPlant("Plant1", R.drawable.potplant, 1);
        MyPlant myplant2 = new MyPlant("Plant2", R.drawable.potplant, 2);

        adapter.addItem(myplant1);
        adapter.addItem(myplant2);
    }

    private void setUpRecyclerView() {
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                helper.onDraw(c, parent, state);
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public static class MyData{
        public MyData( String _title, String content){

            strTitle = _title;
            strContent = content;

        }

        public String strTitle;
        public String strContent;

    }

    public class MyAdapter extends ArrayAdapter<HomeFragment.MyData> {
        Activity _context;
        public MyAdapter( Activity context) {
            super(context, R.layout.info_adapterlayout, data);
            _context = context;
        }

        @Override
        public View getView(int position, View convertView,  ViewGroup parent) {
            LayoutInflater inflater = _context.getLayoutInflater();
            View myView = inflater.inflate(R.layout.info_adapterlayout, null);
            TextView textClass = (TextView)myView.findViewById(R.id.adapterTitle);
            TextView textMajor = (TextView)myView.findViewById(R.id.adapterContent);

            textClass.setText(data[position].strTitle);
            textMajor.setText(data[position].strContent);

            return myView;
            //return super.getView(position, convertView, parent);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        final String finalID = readId();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
        recyclerView = findViewById(R.id.recyclerView);

        toggleButton1 = (ToggleButton) findViewById(R.id.toggleButton1);
        toggleButton2 = (ToggleButton) findViewById(R.id.toggleButton2);
        toggleButton3 = (ToggleButton) findViewById(R.id.toggleButton3);

        textbtn1 = (TextView)findViewById(R.id.textbtn1);
        //aSwitch = (Switch) findViewById(R.id.switch1);

        toggleButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    Toast.makeText(HomeActivity.this, "토글클릭-OFF", Toast.LENGTH_SHORT).show();
                    getDeviceStatus(finalID);
                } else {
                    Toast.makeText(HomeActivity.this, "토글클릭-OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });

        toggleButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    Toast.makeText(HomeActivity.this, "토글클릭-ON", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HomeActivity.this, "토글클릭-OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });

        toggleButton3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    Toast.makeText(HomeActivity.this, "토글클릭-ON", Toast.LENGTH_SHORT).show();
                    toggleButton3.setText("sdkfjw");
                } else {
                    Toast.makeText(HomeActivity.this, "토글클릭-OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, true));


        //RecyclerView의 Adapter 세팅
        adapter = new MyPlantAdapter(HomeActivity.this);
        recyclerView.setAdapter(adapter);

        //ItemTouchHelper 생성
        helper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter));
        //RecyclerView에 ItemTouchHelper 붙이기
        helper.attachToRecyclerView(recyclerView);
        testData();

    }


    public void getDeviceStatus(final String id)
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String getPlantStatusUrl = getString(R.string.getDeviceStatusUrl); //온습도를 받아오는 url
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
                        String water1 = jObject.optString("water1");
                        String water2 = jObject.optString("water2");
                        String water3 = jObject.optString("water3");
                        String led = jObject.optString("led");
                        String fan = jObject.optString("fan");

                        toggleButton1.setText(water1);
                        toggleButton2.setText(led);
                        toggleButton3.setText(fan);

                        textbtn1.setText(led);

                    }
                    else
                    {
                        System.out.println("에러");
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
           // Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_SHORT).show();
        }
        return str;
    }

}
