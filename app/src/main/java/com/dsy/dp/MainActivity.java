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
                //选中时间
                Toast.makeText(MainActivity.this,time,Toast.LENGTH_SHORT).show();
            }
        });
        datePicker.setIsLoop(false);//是否可以循环滚动
        datePicker.showSpecificTime(false);//是否显示分钟 默认true
        datePicker.show();//显示当前时间
//        datePicker.show("2008-08-08 00:00");//显示定位时间 yyyy-MM-dd HH:mm
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
