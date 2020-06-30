package com.example.smartgreenhouse.ui.graph;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.smartgreenhouse.R;
import com.example.smartgreenhouse.ui.Account.FindAccountActivity;
import com.example.smartgreenhouse.ui.reservation.FanReservationActivity;
import com.example.smartgreenhouse.ui.reservation.LedReservationActivity;

public class ChartMainActivity extends AppCompatActivity {
    Button btnDailyChart, btnWeeklyChart, btnMonthlyChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_main);

        btnDailyChart = findViewById(R.id.btnDailyChart);
        btnWeeklyChart = findViewById(R.id.btnWeeklyChart);
        btnMonthlyChart = findViewById(R.id.btnMonthlyChart);

        btnDailyChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChartTabActivity.class);
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

}
