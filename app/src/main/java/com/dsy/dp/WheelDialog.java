package com.dsy.dp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dsy.datepicker.WheelView;

import java.util.ArrayList;
import java.util.List;

public class WheelDialog extends Dialog {
    public WheelDialog(@NonNull Context context) {
        super(context, R.style.theme_dialog_date_picker);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_wheel);
        List<String> data=new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add(i+"");
        }
        WheelView wheelView=findViewById(R.id.wheel);
        //设置数据
        wheelView.setData(data);
        //设置选中项
        wheelView.setSelected(4);
        //设置是否循环
        wheelView.setIsLoop(false);
        //选中监听
        wheelView.setOnSelectListener(new WheelView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();
            }
        });

        //设置底部弹出
        Window win = this.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.windowAnimations = R.style.AnimBottom;
        lp.gravity = Gravity.BOTTOM;
        win.setAttributes(lp);
        win.setBackgroundDrawableResource(android.R.color.transparent);
    }
}
