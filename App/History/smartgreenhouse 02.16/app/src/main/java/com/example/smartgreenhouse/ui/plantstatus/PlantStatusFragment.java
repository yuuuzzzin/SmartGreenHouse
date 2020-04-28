package com.example.smartgreenhouse.ui.plantstatus;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Handler;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlantStatusFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlantStatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlantStatusFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private FragmentActivity rootActivity;
    private View rootView;



    TextView textTemperature, textHumidity, textSoil1, textSoil2, textSoil3, textWater,textCds,textDate;
    Button btnShowStreaming, btnInstantFeed, btnDeleteAutoFeed, btnSetFeeding;
    Spinner spinner;
    ProgressBar progressTemp, progressHumi, progressFeed;
    private boolean runFlag = false;
    private final String zero = "0 / 100";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Context mContext;

    private Activity activity;

    // Initialise it from onAttach()
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity)
            activity = (Activity) context;
    }

    public PlantStatusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_plant_status, container, false);
        rootActivity = getActivity();

        // Strict Mode 설정
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        textTemperature = rootView.findViewById(R.id.txtTemp);
        textHumidity = rootView.findViewById(R.id.txtHum);
        textDate = rootView.findViewById(R.id.txtDate);
        final String finalID = readId();
        Toast.makeText(mContext, finalID+"함수 밖", Toast.LENGTH_SHORT);

        Thread getThread;
        final Handler mHandler = new Handler();
        getThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(;;) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            getPlantStatus(finalID);
                        }
                    });
                    try
                    {
                        Thread.sleep(2000); //5초마다 온습도 정보 가져오게 하기위해서
                    }
                    catch(Exception e)
                    {
                        initStatus();
                        e.printStackTrace();
                    }
                }
            }
        });

        getThread.start();


        return rootView;
    }

    public void setStatus(String temp, String humi, String soil1, String soil2, String soil3, String cds, String water)
    {
        int Temp = Integer.parseInt(temp);
        int Humi = Integer.parseInt(humi);
        int Soil1 = Integer.parseInt(soil1);
        int Soil2 = Integer.parseInt(soil2);
        int Soil3 = Integer.parseInt(soil3);
        int Cds = Integer.parseInt(cds);
        int Water = Integer.parseInt(water);

        textTemperature.setText(temp + "Cº");
        progressTemp.setProgress(Temp);
        textHumidity.setText(humi + "%");
        progressHumi.setProgress(Humi);
        textSoil1.setText(soil1 + "%");
        textSoil2.setText(soil2 + "%");
        textSoil3.setText(soil3 + "%");
        textCds.setText(cds + "%");
        textWater.setText(water + "%");
    }
    public void getPlantStatus(final String id) //DB에서 온습도 정보 가져오기
    {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        String getPlantStatusUrl = getString(R.string.getPlantStatusUrl); //온습도를 받아오는 url
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
                        String date = jObject.optString("date"); // 1970년으로부터의 시간
                        String temp = jObject.optString("temp");
                        String humi = jObject.optString("humi");
                        String soil1 = jObject.optString("soil1");
                        String soil2 = jObject.optString("soil2");
                        String soil3 =jObject.optString("soil3");
                        String cds = jObject.optString("cds");
                        String water = jObject.optString("water");

                        long time = Long.parseLong(date);
                        long curTime = System.currentTimeMillis() / 1000;
                        long timePeriod = curTime - time;
                        if(timePeriod >= 300) {
                            return;
                        }
                        DateFormat df = new SimpleDateFormat("yyyy/MM/dd a hh:mm:ss");
                        Date dateObj = new Date(time * 1000);
                        String str = df.format(dateObj);
                        textDate.setText(str);
                        setStatus(temp, humi, soil1,soil2,soil3,cds,water);
                    }
                    else
                    {
                        textHumidity.setText("?");

                        textTemperature.setText("?");

                        textDate.setText("");
                    }
                }
                catch(JSONException e)
                {
                    Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                // Error Handling
                Toast.makeText(mContext, "시스템 오류", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("number", id);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlantStatusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlantStatusFragment newInstance(String param1, String param2) {
        PlantStatusFragment fragment = new PlantStatusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    public void initStatus()
    {
        textHumidity.setText(zero);
        progressHumi.setProgress(0);
        textTemperature.setText(zero);
        progressTemp.setProgress(0);
        textDate.setText("");

        progressFeed.setProgress(0);
    }

    public void destroyWebView(WebView mWebView) {
        mWebView.clearHistory();
        mWebView.clearCache(true);
        mWebView.loadUrl("about:blank");
        mWebView.onPause();
        mWebView.removeAllViews();
        mWebView.destroyDrawingCache();
        mWebView.pauseTimers();
        mWebView.destroy();
        mWebView = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public String readId()
    {
        String str = null;
        try{
            BufferedReader br = new BufferedReader(new FileReader(mContext.getFilesDir() + "/" + "id.txt"));
            str = null;
            str = br.readLine();
            //Toast.makeText(getContext(), str+"readId 함수 안", Toast.LENGTH_SHORT).show();
        } catch (IOException e){
            e.printStackTrace();
        } catch(Exception e) {
            Toast.makeText(mContext, "Exception", Toast.LENGTH_SHORT).show();
        }
        return str;
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
}