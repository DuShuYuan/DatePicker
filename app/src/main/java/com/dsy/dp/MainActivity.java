package com.dsy.dp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dsy.datepicker.DatePicker;
import com.dsy.datepicker.DoubleDatePicker;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void dateDialog(View view) {
        DatePicker datePicker = new DatePicker(this, new DatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                Toast.makeText(MainActivity.this,time,Toast.LENGTH_SHORT).show();
            }
        });
        datePicker.setIsLoop(false);
        datePicker.showSpecificTime(false);
        datePicker.show();
    }

    public void doubleDateDialog(View view) {
        DoubleDatePicker datePicker = new DoubleDatePicker(this, new DoubleDatePicker.ResultHandler() {
            @Override
            public void handle(String startTime, String endTime) {
                Toast.makeText(MainActivity.this,startTime+"  "+endTime,Toast.LENGTH_SHORT).show();
            }
        });
        datePicker.show();
    }

    public void genderDialog(View view) {
        new WheelDialog(this).show();
    }
}
