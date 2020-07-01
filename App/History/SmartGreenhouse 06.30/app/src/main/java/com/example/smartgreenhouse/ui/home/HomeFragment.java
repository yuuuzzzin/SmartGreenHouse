package com.example.smartgreenhouse.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartgreenhouse.MainActivity;
import com.example.smartgreenhouse.R;
import com.example.smartgreenhouse.ui.MyPlant.ItemTouchHelperCallback;
import com.example.smartgreenhouse.ui.MyPlant.MyPlant;
import com.example.smartgreenhouse.ui.MyPlant.MyPlantAdapter;
import com.example.smartgreenhouse.ui.plantInfo.PlantInfoItem;
import com.example.smartgreenhouse.ui.plantInfo.PlantSettingActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    String info, url;
    RecyclerView recyclerView;
    MyPlantAdapter adapter;
    ItemTouchHelper helper;
    Button button1;
    ToggleButton toggleButton2;
    ToggleButton toggleButton3;
    TextView txtLed, txtWater1, txtWater2, txtWater3, txtFan;
    Switch aSwitch;
    String plantInfoList;
    public String ledStatus, fanStatus, waterStatus;
    private HomeFragment.OnFragmentInteractionListener mListener;
    public HomeFragment.MyData[] data = null;
    ArrayList<String> listdata = new ArrayList<String>();
    TextView test;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentUser.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }
    private void init(){

        recyclerView = recyclerView.findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        //RecyclerView의 Adapter 세팅
        adapter = new MyPlantAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        //ItemTouchHelper 생성
        helper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter));
        //RecyclerView에 ItemTouchHelper 붙이기
        helper.attachToRecyclerView(recyclerView);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));

        button1 = (Button) view.findViewById(R.id.button1);
        toggleButton2 = (ToggleButton) view.findViewById(R.id.toggleButton2);
        toggleButton3 = (ToggleButton) view.findViewById(R.id.toggleButton3);
        test = (TextView) view.findViewById(R.id.test);
        final String finalID = readId();

        //RecyclerView의 Adapter 세팅
        adapter = new MyPlantAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        //ItemTouchHelper 생성
        helper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter));
        //RecyclerView에 ItemTouchHelper 붙이기
        helper.attachToRecyclerView(recyclerView);
        getPlantInfo(finalID);


        // 이 부분이 스레드 부분인데, 현재 오류가 나서 주석 처리 해놓았습니다.
        /*Thread getThread;

        final Handler mHandler = new Handler();
        getThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(;;) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            getDeviceStatus(finalID);   // 장치 상태 받아오기
                            getPlantInfo(finalID);      // 식물 정보 받아오기
                        }
                    });
                    try
                    {
                        Thread.sleep(2000);
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
        getThread.start();*/



        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setWaterStatus(finalID, "on");
                Toast.makeText(getContext(), "전체 물주기 완료", Toast.LENGTH_SHORT).show();
            }
        });

        toggleButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    setLedStatus(finalID, "on");
                    //Toast.makeText(getContext(), "토글클릭-ON", Toast.LENGTH_SHORT).show();
                }
                else{
                    setLedStatus(finalID, "off");
                    //Toast.makeText(getContext(), "토글클릭-OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });

        toggleButton3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    setFanStatus(finalID, "on");
                    //Toast.makeText(getContext(), "토글클릭-ON", Toast.LENGTH_SHORT).show();
                }
                else{
                    setFanStatus(finalID, "off");
                    //Toast.makeText(getContext(), "토글클릭-OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public String readId()
    {
        String str = null;
        try{
            BufferedReader br = new BufferedReader(new FileReader(getContext().getFilesDir() + "/" + "id.txt"));
            str = null;
            str = br.readLine();
            //Toast.makeText(getApplicationContext(), str+"readId 함수 안", Toast.LENGTH_SHORT).show();
        } catch (IOException e){
            e.printStackTrace();
        } catch(Exception e) {
        }
        return str;
    }

    public void initDeviceStatus(final String id)
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String getDeviceStatusUrl = getString(R.string.getDeviceStatusUrl); //온습도를 받아오는 url
        //Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getDeviceStatusUrl, new Response.Listener<String>()
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
                        String water1 = jObject.optString("water1");
                        String water2 = jObject.optString("water2");
                        String water3 = jObject.optString("water3");
                        String led = jObject.optString("led");
                        String fan = jObject.optString("fan");

                        ledStatus = led;
                        fanStatus = fan;

                        if (ledStatus!=null && ledStatus.equals("on")) {
                            toggleButton2.setChecked(true);
                        } else if (ledStatus!=null && ledStatus.equals("off")) {
                            toggleButton2.setChecked(false);
                        }

                        if (fanStatus!=null && fanStatus.equals("on")) {
                            toggleButton3.setChecked(true);
                        } else if (fanStatus!=null && fanStatus.equals("off")) {
                            toggleButton3.setChecked(false);
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
                Toast.makeText(getContext(), error.getMessage() + "시스템 오류", Toast.LENGTH_SHORT).show();
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

    public void getDeviceStatus(final String id)
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String getDeviceStatusUrl = getString(R.string.getDeviceStatusUrl); //온습도를 받아오는 url
        //Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getDeviceStatusUrl, new Response.Listener<String>()
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
                        String water1 = jObject.optString("water1");
                        String water2 = jObject.optString("water2");
                        String water3 = jObject.optString("water3");
                        String led = jObject.optString("led");
                        String fan = jObject.optString("fan");

                        /*ledStatus = led;
                        fanStatus = fan;

                        if (ledStatus!=null && ledStatus.equals("on")) {
                            toggleButton2.setChecked(true);
                        } else if (ledStatus!=null && ledStatus.equals("off")) {
                            toggleButton2.setChecked(false);
                        }

                        if (fanStatus!=null && fanStatus.equals("on")) {
                            toggleButton3.setChecked(true);
                        } else if (fanStatus!=null && fanStatus.equals("off")) {
                            toggleButton3.setChecked(false);
                        }*/

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
                Toast.makeText(getContext(), error.getMessage() + "시스템 오류", Toast.LENGTH_SHORT).show();
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

    public void setLedStatus(final String id, final String led)
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String setLedStatusUrl = getString(R.string.setLedStatusUrl); //온습도를 받아오는 url
        //Toast.makeText(getContext(), led + "상태", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, setLedStatusUrl, new Response.Listener<String>()
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
                        Toast.makeText(getContext(), led + "led 상태 변경 완료", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getContext(), led + "led 상태 변경 실패", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), error.getMessage() + "시스템 오류", Toast.LENGTH_SHORT).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("ID", id);
                params.put("LED", led);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void setFanStatus(final String id, final String fan)
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String setFanStatusUrl = getString(R.string.setFanStatusUrl); //온습도를 받아오는 url
        //Toast.makeText(getContext(), led + "상태", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, setFanStatusUrl, new Response.Listener<String>()
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
                        Toast.makeText(getContext(), fan + "fan 상태 변경 완료", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getContext(), fan + "fan 상태 변경 실패", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), error.getMessage() + "시스템 오류", Toast.LENGTH_SHORT).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("ID", id);
                params.put("FAN", fan);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void setWaterStatus(final String id, final String water)
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String setWaterStatusUrl = getString(R.string.setWaterStatusUrl); //온습도를 받아오는 url
        //Toast.makeText(getContext(), led + "상태", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, setWaterStatusUrl, new Response.Listener<String>()
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
                        Toast.makeText(getContext(), water + "water 상태 변경 완료", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getContext(), water + "water 상태 변경 실패", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), error.getMessage() + "시스템 오류", Toast.LENGTH_SHORT).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("ID", id);
                params.put("WATER", water);
                //  params.put("WATER2", water);
                //  params.put("WATER3", water);
                return params;
            }
        };
        queue.add(stringRequest);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
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

    public class MyAdapter extends ArrayAdapter<MyData> {
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

    public void getPlantInfo(final String id)
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String getPlantInfoUrl = getString(R.string.getPlantInfoUrl);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, getPlantInfoUrl, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.d("onResponse", response);
                ArrayList<MyPlant> list = new ArrayList<>();
                // Response
                try
                {
                    JSONArray jarray = new JSONObject(response).getJSONArray("List");
                    JSONObject jObject = jarray.getJSONObject(0);
                    String result = jObject.optString("RESULT");
                    if(result.equals("1"))
                    {

                        for (int i=0; i<jarray.length(); i++) {
                            JSONObject jo = jarray.getJSONObject(i);
                            listdata.add(jarray.getString(i));
                            String nickname = jo.getString("nickname");
                            String position = jo.getString("position");
                            info = jo.getString("info");
                        }

                        //txtInfo.setText(info);
                        //jsoninfo = txtInfo.getText().toString();



                       // jsonParsingUrl(info);
                      //  Toast.makeText(getContext(), url , Toast.LENGTH_SHORT).show();




                        //test.setText(listdata.get(0) + listdata.get(1) + listdata.get(2));
                        //test.setText(listdata.toString());
                        String json = "";
                        json = "{" + "\"Plants\":" + listdata.toString() + "}";

                        //Toast.makeText(getContext(), json, Toast.LENGTH_SHORT).show();
                        jsonParsing(json);
                    }
                    else
                    {
                        Toast.makeText(getContext(), "식물 가져오기 실패", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(org.json.JSONException e)
                {
                    //Toast.makeText(getApplicationContext(), "캐치 에러", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                // Error Handling
                Toast.makeText(getContext(), "시스템 오류", Toast.LENGTH_SHORT).show();
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
    private void jsonParsingUrl(String json) {
        StringBuffer sb = new StringBuffer();
        json = "[" + json + "]";

        try {
            JSONArray jarray = new JSONArray(json);
            for (int i = 0; i < jarray.length(); i++) {
                JSONObject jObject = jarray.getJSONObject(i);
                 url = jObject.optString("image");
            }
            //txtInfo.setText(sb.toString());
        } catch (JSONException e) {
            Log.d("캐치리스트출력에러", e.toString());
            e.printStackTrace();
        }
    }
    private void jsonParsing(String json)
    {
        //private Bitmap image = null;
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray plantArray = jsonObject.getJSONArray("Plants");
            //Toast.makeText(getContext(), plantArray.length(), Toast.LENGTH_SHORT).show();
            Log.d("리스트출력에러", String.valueOf(plantArray.length()));
            for(int i=0; i<plantArray.length(); i++)
            {

                JSONObject plantObject = plantArray.getJSONObject(i);

                MyPlant dataItem = new MyPlant();

                dataItem.setPlantNickname(plantObject.getString("nickname"));
                //dataItem.setImage(R.drawable.potplant);
                dataItem.setPlantPosition(plantObject.getString("position"));
                String info2 = plantObject.getString("info");

                jsonParsingUrl(info2);
               // Toast.makeText(getContext(), url , Toast.LENGTH_SHORT).show();
                String Url = url;
                try {
                    InputStream is = (InputStream) new URL(Url).getContent();
                    Bitmap d = BitmapFactory.decodeStream(is);
                    is.close();

                    dataItem.setImage(d);

                } catch (Exception e) {
                    Toast.makeText(getContext(), e.toString() + "이미지 불러오기 실패", Toast.LENGTH_SHORT).show();
                }

                adapter.addItem(dataItem);
            }
        }catch (JSONException e) {
            Log.d("캐치리스트출력에러", e.toString());
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }


    /*public void getPlantInfo(final String id)
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String getPlantInfoUrl = getString(R.string.getPlantInfoUrl);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, getPlantInfoUrl, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.d("onResponse", response);
                ArrayList<MyPlant> list = new ArrayList<>();
                // Response
                try
                {
                    JSONArray jarray = new JSONObject(response).getJSONArray("List");
                    JSONObject jObject = jarray.getJSONObject(0);
                    String result = jObject.optString("RESULT");
                    if(result.equals("1"))
                    {
                        String nickname = jObject.getString("nickname");
                        String position = jObject.getString("position");
                        test.setText(nickname + " / " + position);
                    }
                    else
                    {
                        Toast.makeText(getContext(), "식물 가져오기 실패", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(org.json.JSONException e)
                {
                    //Toast.makeText(getApplicationContext(), "캐치 에러", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                // Error Handling
                Toast.makeText(getContext(), "시스템 오류", Toast.LENGTH_SHORT).show();
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
    }*/
}