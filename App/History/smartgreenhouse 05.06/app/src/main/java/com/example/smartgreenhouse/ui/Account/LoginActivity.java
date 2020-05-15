package com.example.smartgreenhouse.ui.Account;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartgreenhouse.BackPressCloseHandler;
import com.example.smartgreenhouse.MainActivity;
import com.example.smartgreenhouse.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private BackPressCloseHandler backPressCloseHandler;
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    Button btnJoin, btnFindAccount, btnLogin;
    CheckBox autoLogin;
    EditText userId, userPassword;
    String txtId, txtPassword;
    String ID, PW;
    String Switch = "1";
    TextView loginMessage;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        backPressCloseHandler = new BackPressCloseHandler(this);
        btnJoin =  (Button)findViewById(R.id.btnJoin);
        btnFindAccount = (Button)findViewById(R.id.btnFindAccount);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        autoLogin = (CheckBox)findViewById(R.id.autoLogin);
        userId = (EditText)findViewById(R.id.userId);
        userPassword = (EditText)findViewById(R.id.userPassword);
        setting = getSharedPreferences("setting", MODE_PRIVATE);
        editor = setting.edit();
        loginMessage = (TextView)findViewById(R.id.loginMessage);

        ID = userId.getText().toString();
        PW = userPassword.getText().toString();
        //Toast.makeText(getApplicationContext(),setting.getBoolean("Auto_Login",false),Toast.LENGTH_SHORT).show();
        autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editor.putString("ID", ID);
                    editor.putString("PW", PW);
                    editor.putString("Switch",Switch);
                    editor.putBoolean("Auto_Login",true);
                    editor.apply();
                }else{
                    editor.remove("ID");
                    editor.remove("PW");
                    editor.remove("Switch");
                    editor.remove("Auto_Login");
                    editor.clear();
                    editor.commit();
                }
            }
        });

        if(setting.getBoolean("Auto_Login", false)){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        }

        userId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == true)
                {
                    loginMessage.setText("");
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtId = userId.getText().toString();
                txtPassword = userPassword.getText().toString();
                MemberCheck(txtId, txtPassword);
            }
        });

        btnLogin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN :
                        btnLogin.setBackgroundColor(Color.GRAY);
                        break;
                    case MotionEvent.ACTION_UP :
                        btnLogin.setBackgroundColor(getResources().getColor(R.color.colorLightGreen));
                }
                return false;
            }
        });


        btnJoin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);

            }
        });

        btnFindAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), FindAccountActivity.class);
                startActivity(intent);
            }
        });

    }

    public void saveId(String inputId)  // 아이디를 파일에 저장하는 함수
    {
        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(getFilesDir() + "/" + "id.txt", false));
            bw.write(inputId);
            bw.close();
            Toast.makeText(this,getFilesDir()+"저장완료", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ioe) {
            Toast.makeText(this, "Exception", Toast.LENGTH_SHORT).show();
        }
    }

    public void onStop(){
        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed(){
        backPressCloseHandler.onBackPressed();
    }

    public void MemberCheck(final String id, final String password)
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = getString(R.string.loginUrl);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
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
                        loginMessage.setText("");
                        saveId(id);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("ID",id);
                        intent.putExtra("PW",password);
                        startActivity(intent);
                    }
                    else
                    {
                        loginMessage.setTextColor(Color.RED);
                        loginMessage.setText(R.string.hintLogin);
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
                Toast.makeText(getApplicationContext(), "시스템 오류", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("ID", id);
                params.put("PASSWORD", password);
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
