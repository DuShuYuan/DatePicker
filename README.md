# DatePicker  [![](https://jitpack.io/v/DuShuYuan/DatePicker.svg)](https://jitpack.io/#DuShuYuan/DatePicker)


DatePicker 日期选择器；DoubleDatePicker 双日期选择；WheelView 滚轮选择器
---
## 截图
![gif](/img/simple.gif)

## 使用
---

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.DuShuYuan:DatePicker:1.0.0'
	}
    


### WheelView
---
```xml

    <com.dsy.datepicker.WheelView
        android:id="@+id/wheel"
        android:layout_width="160dp"
        android:layout_height="160dp" />

```
```
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
```



### DatePicker & DoubleDatePicker
---
```
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
        datePicker.show("2008-08-08 00:00");//显示定位时间 yyyy-MM-dd HH:mm
```



### 自定义日期选择器样式
---

使用如下初始化方法，传入你的布局id
```
        public DatePicker(Context context,@LayoutRes int layoutId, ResultHandler resultHandler)
        public DoubleDatePicker(Context context, @LayoutRes int layoutId, ResultHandler resultHandler)

```
layoutId 为自定义的布局文件id，其中必须包含以下View id：

DatePicker：
```
    tv_cancel;
    tv_select;
    wheel_year;
    wheel_month;
    wheel_day;
    wheel_hour;
    tv_hour;
    wheel_minute;
    tv_minute;
```
DoubleDatePicker：
```
    tv_cancel;
    tv_select;
    wheel_year;
    wheel_month;
    wheel_day;
    wheel_year2;
    wheel_month2;
    wheel_day2;
```
