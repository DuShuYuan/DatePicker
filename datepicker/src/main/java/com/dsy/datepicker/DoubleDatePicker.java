package com.dsy.datepicker;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 选择日期 yyyy-MM-dd yyyy-MM-dd
 */
public class DoubleDatePicker {

    private PickerBuilder picker1, picker2;
    private ResultHandler handler;
    private Context context;
    private Dialog datePickerDialog;
    private TextView tv_cancel, tv_select;
    private int layoutId;
    public DoubleDatePicker(Context context, ResultHandler resultHandler) {
        this.context = context;
        this.handler = resultHandler;
        initDialog();
        initView();
    }

    public DoubleDatePicker(Context context, @LayoutRes int layoutId, ResultHandler resultHandler) {
        this(context,resultHandler);
        this.layoutId=layoutId;
    }

    private void initDialog() {
        if (datePickerDialog == null) {
            datePickerDialog = new Dialog(context, R.style.theme_dialog_date_picker);
            datePickerDialog.setCancelable(true);
            datePickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (layoutId!=0){
                datePickerDialog.setContentView(layoutId);
            }else {
                datePickerDialog.setContentView(R.layout.date_picker_double);
            }
            Window window = datePickerDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(dm);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = dm.widthPixels;
            lp.windowAnimations = R.style.AnimBottom;
            window.setAttributes(lp);
        }
    }

    private void initView() {
        WheelView wheel_year = datePickerDialog.findViewById(R.id.wheel_year);
        WheelView wheel_month = datePickerDialog.findViewById(R.id.wheel_month);
        WheelView wheel_day = datePickerDialog.findViewById(R.id.wheel_day);
        picker1 = new PickerBuilder(wheel_year, wheel_month, wheel_day);

        WheelView wheel_year2 = datePickerDialog.findViewById(R.id.wheel_year2);
        WheelView wheel_month2 = datePickerDialog.findViewById(R.id.wheel_month2);
        WheelView wheel_day2 = datePickerDialog.findViewById(R.id.wheel_day2);
        picker2 = new PickerBuilder(wheel_year2, wheel_month2, wheel_day2);
        tv_cancel = datePickerDialog.findViewById(R.id.tv_cancel);
        tv_select = datePickerDialog.findViewById(R.id.tv_select);

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.dismiss();
            }
        });

        tv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.handle(picker1.getSelectedDate(),picker2.getSelectedDate());
                datePickerDialog.dismiss();
            }
        });
    }

    /**
     * 显示时间
     * @param date1 yyyy-MM-dd
     * @param date2 yyyy-MM-dd
     */
    public void show(String date1, String date2){
        if (picker1.show(date1)&&picker2.show(date2)){
            datePickerDialog.show();
        }
    }

    /**
     * 显示当前时间
     */
    public void show(){
        String date=new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date());
        show(date,date);
    }

    public enum SCROLL_TYPE {
        HOUR(1),
        MINUTE(2);

        public int value;

        SCROLL_TYPE(int value) {
            this.value = value;
        }
    }

    /**
     * 定义结果回调接口
     */
    public interface ResultHandler {
        void handle(String startTime, String endTime);
    }

    private class PickerBuilder {
        private int scrollUnits = SCROLL_TYPE.HOUR.value + SCROLL_TYPE.MINUTE.value;
        private static final int MAX_MINUTE = 59;
        private static final int MAX_HOUR = 23;
        private static final int MIN_MINUTE = 0;
        private static final int MIN_HOUR = 0;
        private static final int MAX_MONTH = 12;
        private boolean canAccess = false;
        private WheelView wheel_year, wheel_month, wheel_day;
        private ArrayList<String> year, month, day;
        private int startYear, startMonth, startDay,  endYear, endMonth, endDay;
        private boolean spanYear, spanMon, spanDay;
        private Calendar selectedCalender, startCalendar, endCalendar;

        public String getSelectedDate(){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            return  sdf.format(selectedCalender.getTime());
        }

        public PickerBuilder(WheelView wheel_year, WheelView wheel_month, WheelView wheel_day) {
            this.wheel_year = wheel_year;
            this.wheel_month = wheel_month;
            this.wheel_day = wheel_day;
            canAccess = true;
            selectedCalender = Calendar.getInstance();
            startCalendar = Calendar.getInstance();
            endCalendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            try {
                startCalendar.setTime(sdf.parse("1900-01-01"));
                endCalendar.setTime(sdf.parse("2100-01-01"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        private void initParameter() {
            startYear = startCalendar.get(Calendar.YEAR);
            startMonth = startCalendar.get(Calendar.MONTH) + 1;
            startDay = startCalendar.get(Calendar.DAY_OF_MONTH);
            endYear = endCalendar.get(Calendar.YEAR);
            endMonth = endCalendar.get(Calendar.MONTH) + 1;
            endDay = endCalendar.get(Calendar.DAY_OF_MONTH);
            spanYear = startYear != endYear;
            spanMon = (!spanYear) && (startMonth != endMonth);
            spanDay = (!spanMon) && (startDay != endDay);
            selectedCalender.setTime(startCalendar.getTime());
        }

        private void initTimer() {
            initArrayList();
            if (spanYear) {
                for (int i = startYear; i <= endYear; i++) {
                    year.add(String.valueOf(i));
                }
                for (int i = startMonth; i <= MAX_MONTH; i++) {
                    month.add(formatTimeUnit(i));
                }
                for (int i = startDay; i <= startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                    day.add(formatTimeUnit(i));
                }

            } else if (spanMon) {
                year.add(String.valueOf(startYear));
                for (int i = startMonth; i <= endMonth; i++) {
                    month.add(formatTimeUnit(i));
                }
                for (int i = startDay; i <= startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                    day.add(formatTimeUnit(i));
                }

            } else if (spanDay) {
                year.add(String.valueOf(startYear));
                month.add(formatTimeUnit(startMonth));
                for (int i = startDay; i <= endDay; i++) {
                    day.add(formatTimeUnit(i));
                }

            }
            loadComponent();
        }

        /**
         * 将“0-9”转换为“00-09”
         */
        private String formatTimeUnit(int unit) {
            return unit < 10 ? "0" + String.valueOf(unit) : String.valueOf(unit);
        }

        private void initArrayList() {
            if (year == null) year = new ArrayList<>();
            if (month == null) month = new ArrayList<>();
            if (day == null) day = new ArrayList<>();
            year.clear();
            month.clear();
            day.clear();
        }

        private void loadComponent() {
            wheel_year.setData(year);
            wheel_month.setData(month);
            wheel_day.setData(day);
            wheel_year.setSelected(0);
            wheel_month.setSelected(0);
            wheel_day.setSelected(0);
            executeScroll();
        }

        private void addListener() {
            wheel_year.setOnSelectListener(new WheelView.onSelectListener() {
                @Override
                public void onSelect(String text) {

                    int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);

                    selectedCalender.set(Calendar.DAY_OF_MONTH, 1);
                    selectedCalender.set(Calendar.YEAR, Integer.parseInt(text));

                    int lastDay = selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH);
                    if (selectedDay > lastDay) {
                        selectedCalender.set(Calendar.DAY_OF_MONTH, lastDay);
                    } else {
                        selectedCalender.set(Calendar.DAY_OF_MONTH, selectedDay);
                    }
                    monthChange();
                }
            });

            wheel_month.setOnSelectListener(new WheelView.onSelectListener() {
                @Override
                public void onSelect(String text) {
                    int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);

                    selectedCalender.set(Calendar.DAY_OF_MONTH, 1);
                    selectedCalender.set(Calendar.MONTH, Integer.parseInt(text) - 1);

                    int lastDay = selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH);
                    if (selectedDay > lastDay) {
                        selectedCalender.set(Calendar.DAY_OF_MONTH, lastDay);
                    } else {
                        selectedCalender.set(Calendar.DAY_OF_MONTH, selectedDay);
                    }
                    dayChange();
                }
            });

            wheel_day.setOnSelectListener(new WheelView.onSelectListener() {
                @Override
                public void onSelect(String text) {
                    selectedCalender.set(Calendar.DAY_OF_MONTH, Integer.parseInt(text));
//                hourChange();
                }
            });

        }

        private void monthChange() {
            month.clear();
            int selectedYear = selectedCalender.get(Calendar.YEAR);
            if (selectedYear == startYear) {
                for (int i = startMonth; i <= MAX_MONTH; i++) {
                    month.add(formatTimeUnit(i));
                }
            } else if (selectedYear == endYear) {
                for (int i = 1; i <= endMonth; i++) {
                    month.add(formatTimeUnit(i));
                }
            } else {
                for (int i = 1; i <= MAX_MONTH; i++) {
                    month.add(formatTimeUnit(i));
                }
            }
//        selectedCalender.set(Calendar.MONTH, Integer.parseInt(month.get(0)) - 1);
            wheel_month.setData(month);
            wheel_month.setSelected(selectedCalender.get(Calendar.MONTH));
            executeAnimator(wheel_month);

            wheel_month.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dayChange();
                }
            }, 100);
        }

        private void dayChange() {
            day.clear();
            int selectedYear = selectedCalender.get(Calendar.YEAR);
            int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
            int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);
            if (selectedYear == startYear && selectedMonth == startMonth) {
                for (int i = startDay; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                    day.add(formatTimeUnit(i));
                }
            } else if (selectedYear == endYear && selectedMonth == endMonth) {
                for (int i = 1; i <= endDay; i++) {
                    day.add(formatTimeUnit(i));
                }
            } else {
                for (int i = 1; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                    day.add(formatTimeUnit(i));
                }
            }
            if (selectedDay > day.size()) {
                selectedDay = day.size();
            }
//        selectedCalender.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day.get(selectedDay-1)));
            wheel_day.setData(day);
            wheel_day.setSelected(selectedDay - 1);
            executeAnimator(wheel_day);

//        wheel_day.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                hourChange();
//            }
//        }, 100);
        }


        private void executeAnimator(View view) {
            PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f, 0f, 1f);
            PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.3f, 1f);
            PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.3f, 1f);
            ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY, pvhZ).setDuration(200).start();
        }

        private void executeScroll() {
            wheel_year.setCanScroll(year.size() > 1);
            wheel_month.setCanScroll(month.size() > 1);
            wheel_day.setCanScroll(day.size() > 1);
        }

        private int disScrollUnit(SCROLL_TYPE... scroll_types) {
            if (scroll_types == null || scroll_types.length == 0) {
                scrollUnits = SCROLL_TYPE.HOUR.value + SCROLL_TYPE.MINUTE.value;
            } else {
                for (SCROLL_TYPE scroll_type : scroll_types) {
                    scrollUnits ^= scroll_type.value;
                }
            }
            return scrollUnits;
        }

        public boolean show(String time) {
            if (canAccess) {
                if (isValidDate(time, "yyyy-MM-dd")) {
                    if (startCalendar.getTime().getTime() < endCalendar.getTime().getTime()) {
                        canAccess = true;
                        initParameter();
                        initTimer();
                        addListener();
                        setSelectedTime(time);
                        return true;
                    }
                } else {
                    canAccess = false;
                }
            }
            return false;
        }


        /**
         * 设置日期控件是否可以循环滚动
         */
        public void setIsLoop(boolean isLoop) {
            if (canAccess) {
                this.wheel_year.setIsLoop(isLoop);
                this.wheel_month.setIsLoop(isLoop);
                this.wheel_day.setIsLoop(isLoop);
            }
        }

        /**
         * 设置日期控件默认选中的时间
         */
        public void setSelectedTime(String time) {
            if (canAccess) {
                String[] str = time.split(" ");
                String[] dateStr = str[0].split("-");

                wheel_year.setSelected(dateStr[0]);
                selectedCalender.set(Calendar.YEAR, Integer.parseInt(dateStr[0]));

                month.clear();
                int selectedYear = selectedCalender.get(Calendar.YEAR);
                if (selectedYear == startYear) {
                    for (int i = startMonth; i <= MAX_MONTH; i++) {
                        month.add(formatTimeUnit(i));
                    }
                } else if (selectedYear == endYear) {
                    for (int i = 1; i <= endMonth; i++) {
                        month.add(formatTimeUnit(i));
                    }
                } else {
                    for (int i = 1; i <= MAX_MONTH; i++) {
                        month.add(formatTimeUnit(i));
                    }
                }
                wheel_month.setData(month);
                wheel_month.setSelected(dateStr[1]);
                selectedCalender.set(Calendar.MONTH, Integer.parseInt(dateStr[1]) - 1);
                executeAnimator(wheel_month);

                day.clear();
                int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
                if (selectedYear == startYear && selectedMonth == startMonth) {
                    for (int i = startDay; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                        day.add(formatTimeUnit(i));
                    }
                } else if (selectedYear == endYear && selectedMonth == endMonth) {
                    for (int i = 1; i <= endDay; i++) {
                        day.add(formatTimeUnit(i));
                    }
                } else {
                    for (int i = 1; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                        day.add(formatTimeUnit(i));
                    }
                }
                wheel_day.setData(day);
                wheel_day.setSelected(dateStr[2]);
                selectedCalender.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateStr[2]));
                executeAnimator(wheel_day);

                executeScroll();
            }
        }

        /**
         * 验证字符串是否是一个合法的日期格式
         */
        private boolean isValidDate(String date, String template) {
            boolean convertSuccess = true;
            // 指定日期格式
            SimpleDateFormat format = new SimpleDateFormat(template, Locale.CHINA);
            try {
                // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2015/02/29会被接受，并转换成2015/03/01
                format.setLenient(false);
                format.parse(date);
            } catch (Exception e) {
                // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
                convertSuccess = false;
            }
            return convertSuccess;
        }
    }


}
