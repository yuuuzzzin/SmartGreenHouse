package com.example.smartgreenhouse.ui.reservation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.smartgreenhouse.R;
import com.example.smartgreenhouse.ui.plantInfo.PlantSettingActivity;

public class ReservationActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnLedReservation, btnFanReservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        btnLedReservation = findViewById(R.id.btnLedReservation);
        btnFanReservation = findViewById(R.id.btnFanReservation);

        btnLedReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LedReservationActivity.class);
                startActivity(intent);
            }
        });

        btnFanReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FanReservationActivity.class);
                startActivity(intent);
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

    @Override
    public void onClick(View v) {

    }
}
