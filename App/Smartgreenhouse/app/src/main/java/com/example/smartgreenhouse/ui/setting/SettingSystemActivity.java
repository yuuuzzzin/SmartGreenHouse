package com.example.smartgreenhouse.ui.setting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartgreenhouse.R;

import java.util.Objects;

public class SettingSystemActivity extends AppCompatActivity {

    Switch swAutoLogin;
    String id;
    String pw;
    String Switch = "1";
    TextView tv;
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    @SuppressLint("CommitPrefEdits")
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingsystem);
        Intent intent = getIntent();
        id = intent.getStringExtra("ID");
        pw = intent.getStringExtra("PW");
        setting = getSharedPreferences("setting",0);
        editor = setting.edit();

        swAutoLogin = (Switch)findViewById(R.id.swAutoLogin); // 자동로그인
        if(Objects.equals(setting.getString("Switch", ""), "1")){
            swAutoLogin.setChecked(true);
        }

        swAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    editor.putString("ID", id);
                    editor.putString("PW", pw);
                    editor.putString("Switch", Switch);
                    editor.putBoolean("Auto_Login_enabled",true);
                    editor.apply();
                } else {
                    // The toggle is disabled
                    editor.remove("ID");
                    editor.remove("PW");
                    editor.remove("Switch");
                    editor.remove("Auto_Login_enabled");
                    editor.clear();
                    editor.commit();
                }
            }
        });

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

}