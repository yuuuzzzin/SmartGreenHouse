package com.example.smartgreenhouse.ui.MyPlant;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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

public class ModifyMyPlantDialog extends Dialog {
    private OnDialogListener listener;
    private Context context;
    private Button mod_bt;
    private EditText mod_text;
    private String text, position;
    private int image;

    public ModifyMyPlantDialog(Context context, String position, MyPlant myPlant) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.modify_my_plant_dialog);

        text = myPlant.getPlantNickname();
        position = myPlant.getPlantPosition();

        mod_text = findViewById(R.id.mod_text);
        mod_text.setText(String.valueOf(text));

        mod_bt = findViewById(R.id.mod_bt);
        final String finalPosition = position;
        mod_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    //EditText의 수정된 값 가져오기
                    String name = mod_text.getText().toString();
                    //MyPlant myPlant = new MyPlant(name, image, position);
                    //Listener를 통해서 person객체 전달
                    //listener.onFinish(position, myPlant);
                    //다이얼로그 종료
                    modifyUserPlantName(readId(), name, finalPosition);

                    dismiss();
                }
            }
        });
    }

    public void setDialogListener(OnDialogListener listener) {
        this.listener = listener;
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

    public void modifyUserPlantName(final String id, final String nickname, final String position)
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String setPlantInfoUrl = getContext().getString(R.string.modifyUserPlantNameUrl);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, setPlantInfoUrl, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.d("onResponse", response);
                Toast.makeText(getContext(), position, Toast.LENGTH_SHORT).show();
                // Response
                try
                {
                    JSONArray jarray = new JSONObject(response).getJSONArray("List");
                    JSONObject jObject = jarray.getJSONObject(0);
                    String result = jObject.optString("RESULT");
                    if(result.equals("1"))
                    {
                        Toast.makeText(getContext(), "식물 정보 저장 완료", Toast.LENGTH_SHORT).show();
                    }
                    /*if(result.equals("2"))
                    {
                        Toast.makeText(getApplicationContext(), "꽉 참", Toast.LENGTH_SHORT).show();
                    }*/
                    else
                    {
                        Toast.makeText(getContext(), "식물 정보 저장 실패", Toast.LENGTH_SHORT).show();
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
                params.put("nickname", nickname);
                params.put("position", position);
                return params;
            }
        };
        queue.add(stringRequest);
    }

}