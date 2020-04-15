package com.example.smartgreenhouse.ui.reservation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.smartgreenhouse.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class LedReservationActivity extends AppCompatActivity implements View.OnClickListener {
    Button setStartDate, setStartTime, setFinishDate, setFinishTime, btnSetReservation;
    TextView txtSetReservation;
    int year1, month1, day1, hour1, minute1;
    int year2, month2, day2, hour2, minute2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led_reservation);

        setStartDate = findViewById(R.id.setStartDate);
        setStartTime = findViewById(R.id.setStartTime);
        setFinishDate = findViewById(R.id.setFinishDate);
        setFinishTime = findViewById(R.id.setFinishTime);
        txtSetReservation = findViewById(R.id.txtSetReservation);
        btnSetReservation = findViewById(R.id.btnSetReservation);

        Calendar c1 = new GregorianCalendar();
        year1 = c1.get(Calendar.YEAR);
        month1 = c1.get(Calendar.MONTH);
        day1 = c1.get(Calendar.DAY_OF_MONTH);
        hour1 = c1.get(Calendar.HOUR_OF_DAY);
        minute1 = c1.get(Calendar.MINUTE);

        btnSetReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReservationActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setStartDate:
                new DatePickerDialog(LedReservationActivity.this, DateSetListener1, year1, month1, day1).show();
                break;
            case R.id.setStartTime:
                new TimePickerDialog(LedReservationActivity.this, TimeSetListener1, hour1, minute1, false).show();
                break;
            case R.id.setFinishDate:
                break;
            case R.id.setFinishTime:
                break;
        }
    }

    DatePickerDialog.OnDateSetListener DateSetListener1 =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    year1 = year;
                    month1 = month;
                    day1 = dayOfMonth;
                    UpdateNow();
                }

            };

    TimePickerDialog.OnTimeSetListener TimeSetListener1 =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    hour1 = hourOfDay;
                    minute1 = minute;
                    UpdateNow();
                }

            };

    void UpdateNow() {
        setStartDate.setText(String.format("%d-%d-%d", year1, month1 + 1, day1));
        setStartTime.setText(String.format("%d:%d:%d", hour1, minute1, "00"));
    }
}
