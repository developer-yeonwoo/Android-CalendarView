package com.nonkkor.calendar.calendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.greencar.campuscar.R;
import com.greencar.campuscar.activity.dailyreport.vo.DailyReprotItemVo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CalendarGridAdapter extends ArrayAdapter<Date> {

    private List<DailyReprotItemVo> eventDays;
    private LayoutInflater inflater;
    private Date currentDate;

    public CalendarGridAdapter(Context context, ArrayList<Date> days, List<DailyReprotItemVo> eventDays, Date currentDate) {
        super(context, R.layout.control_calendar_day, days);
        this.eventDays = eventDays;
        inflater = LayoutInflater.from(context);
        this.currentDate = currentDate;

    }

    private DailyReprotItemVo getDayData(int day){
        if (eventDays == null){
            return null;
        }
        for (DailyReprotItemVo mVo : eventDays) {
            if (mVo.writeDay == day){
                return mVo;
            }
        }

        return null;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Date date = getItem(position);
        int day = date.getDate();
        int month = date.getMonth();
        int year = date.getYear();

        // today
        Date today = new Date();

        Calendar mCal = Calendar.getInstance();
        mCal.add(Calendar.DATE, 1);

        if (view == null){
            view = inflater.inflate(R.layout.control_calendar_day, parent, false);

        }


        Calendar todayCal = Calendar.getInstance();
        todayCal.setTime(date);

        // clear styling
        ((TextView)view.findViewById(R.id.calendar_day)).setTypeface(null, Typeface.NORMAL);
        ((TextView)view.findViewById(R.id.calendar_day)).setTextColor(Color.BLACK);


        if (month < currentDate.getMonth() || month > currentDate.getMonth()) {
            view.findViewById(R.id.todayView).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.glassImgView).setVisibility(View.INVISIBLE);

            return view;
        }

        boolean todayInMonth = month == mCal.get(Calendar.MONTH) && day == (mCal.get(Calendar.DAY_OF_MONTH)-1);
        if ( todayInMonth ) {
            view.findViewById(R.id.todayView).setVisibility(View.VISIBLE);

        }else{
            view.findViewById(R.id.todayView).setVisibility(View.INVISIBLE);

        }


        if ( todayCal.get(Calendar.DAY_OF_WEEK) == 1) {         // sunday
            ((TextView)view.findViewById(R.id.calendar_day)).setTextColor(getContext().getResources().getColor(R.color.dayilyReport_sunday));

        }else if(todayCal.get(Calendar.DAY_OF_WEEK) == 7){      // sat
            ((TextView)view.findViewById(R.id.calendar_day)).setTextColor(getContext().getResources().getColor(R.color.dayilyReport_saturday));

        }else{

        }

        DailyReprotItemVo dayData = getDayData(day);
        if (dayData != null){
            ((TextView)view.findViewById(R.id.point)).setText( dayData.pointPay );
            ((TextView)view.findViewById(R.id.point)).setTextColor(getContext().getResources().getColor(R.color.mainTheme));

        }else{
            if(!(year == 117 && month == 3) && !todayInMonth) { //2017년04월
                ((TextView)view.findViewById(R.id.point)).setText( "기간경과" );
                ((TextView)view.findViewById(R.id.point)).setTextColor(Color.RED);
            } else {
                ((TextView)view.findViewById(R.id.point)).setVisibility(View.INVISIBLE);

            }
        }

        if ( month == mCal.get(Calendar.MONTH) && day > (mCal.get(Calendar.DAY_OF_MONTH)-1) ){
            ((TextView)view.findViewById(R.id.point)).setText( "" );

            view.findViewById(R.id.glassImgView).setVisibility(View.INVISIBLE);
        }

        // set text
        ((TextView)view.findViewById(R.id.calendar_day)).setText(String.valueOf(date.getDate()));

        return view;
    }



}
