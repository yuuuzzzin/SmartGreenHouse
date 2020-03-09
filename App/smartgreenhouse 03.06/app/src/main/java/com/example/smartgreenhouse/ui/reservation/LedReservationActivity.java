package com.example.smartgreenhouse.ui.reservation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.smartgreenhouse.R;
import com.example.smartgreenhouse.ui.MyPlant.SetWaterActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;


import android.os.SystemClock;
import android.provider.Settings;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class LedReservationActivity extends AppCompatActivity {

    Button b2;
    RadioButton btnSetdate, btnSettime;
    CalendarView calendar;
    TimePicker time;
    TextView txtreserve;
    String date = " ";
    String date2 = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led_reservation);

        //b1 = (Button)findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);
        btnSetdate = (RadioButton) findViewById(R.id.radioButton);
        btnSettime = (RadioButton) findViewById(R.id.radioButton2);
        calendar = (CalendarView) findViewById(R.id.calendarView);
        time = (TimePicker) findViewById(R.id.timePicker);
        txtreserve = (TextView) findViewById(R.id.textView);


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String times = Integer.toString(time.getHour()) + "시 " + time.getMinute() + "분 ";

                txtreserve.setText(date + times);
            }
        });
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                date = year + "년 " + (month + 1) + "월 " + dayOfMonth + "일 ";

            }
        });
        btnSetdate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (btnSetdate.isChecked()) {
                    calendar.setVisibility(View.VISIBLE);
                    time.setVisibility(View.INVISIBLE);
                }
            }
        });
        btnSettime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (btnSettime.isChecked()) {
                    calendar.setVisibility(View.INVISIBLE);
                    time.setVisibility(View.VISIBLE);
                }
            }
        });
    }

}