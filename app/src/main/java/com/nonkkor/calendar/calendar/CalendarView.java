package com.nonkkor.calendar.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greencar.campuscar.R;
import com.greencar.campuscar.activity.dailyreport.vo.DailyReprotItemVo;
import com.greencar.campuscar.util.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CalendarView extends LinearLayout {
    private static final String TAG = "Calendar View";
    private static final int DAYS_MAX_COUNT = 7*6;

    private static final String DATE_FORMAT = "yyyy년 MM월";

    private String dateFormat;
    private Calendar currentDate = Calendar.getInstance();
    private EventHandler eventHandler = null;

    // internal components
    private LinearLayout header;
    private LinearLayout btnPrev;
    private LinearLayout btnNext;
    private TextView txtDate;
    private GridView grid;

    public CalendarView(Context context)
    {
        super(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
    }

    private void initControl(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.control_calendar, this);

        loadDateFormat(attrs);
        assignUiElements();
        assignClickHandlers();

        updateCalendar();
    }

    private void loadDateFormat(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);

        try {
            dateFormat = ta.getString(R.styleable.CalendarView_dateFormat);
            if (dateFormat == null)
                dateFormat = DATE_FORMAT;
        }
        finally {
            ta.recycle();
        }
    }
    private void assignUiElements() {
        header = (LinearLayout)findViewById(R.id.calendar_header);
        btnPrev = (LinearLayout)findViewById(R.id.calendar_prev_button);
        btnNext = (LinearLayout)findViewById(R.id.calendar_next_button);
        txtDate = (TextView)findViewById(R.id.calendar_date_display);
        grid = (GridView)findViewById(R.id.calendar_grid);
    }

    private void assignClickHandlers() {
//        btnNext.setOnClickListener(new OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                currentDate.add(Calendar.MONTH, 1);
//                updateCalendar();
//            }
//        });
//
//        btnPrev.setOnClickListener(new OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                currentDate.add(Calendar.MONTH, -1);
//                updateCalendar();
//            }
//        });

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (eventHandler == null){
                    return;

                }
                eventHandler.onDayClick((Date)parent.getItemAtPosition(position));
            }
        });

    }

    public void setBtnListener(final OnClickListener prevListner, final OnClickListener nextListener){
        btnNext.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                currentDate.add(Calendar.MONTH, 1);
                updateCalendar();
                nextListener.onClick(v);

            }
        });

        btnPrev.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                currentDate.add(Calendar.MONTH, -1);
                updateCalendar();
                prevListner.onClick(v);

            }
        });

    }

    public void updateBtnVisibility(){
        // 4월 ~ 현재월까지만 보여야함
        final int MIN_DATE = 201704;
        boolean result = Integer.parseInt( TimeUtils.getYYMM(  txtDate.getText().toString() ) ) <= MIN_DATE;

        if ( result ){
            btnPrev.setVisibility(View.INVISIBLE);

        }else{
            btnPrev.setVisibility(View.VISIBLE);

        }


        Calendar mCal = Calendar.getInstance();
        mCal.add(Calendar.DATE, 1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월");
        String mToday = format.format(mCal.getTime());

        if (mToday.equals(txtDate.getText().toString())){
            btnNext.setVisibility(View.INVISIBLE);

        }else{
            btnNext.setVisibility(View.VISIBLE);

        }


    }


    public void updateCalendar() {
        updateCalendar(null);

    }

    public void updateCalendar(List<DailyReprotItemVo> events) {
        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar)currentDate.clone();

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        while (cells.size() < DAYS_MAX_COUNT) {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        grid.setAdapter(new CalendarGridAdapter(getContext(), cells, events, currentDate.getTime()));

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        txtDate.setText(sdf.format(currentDate.getTime()));
        updateBtnVisibility();

    }

    public String getDateTitleText(){
        return txtDate.getText().toString();
    }


    public void setEventHandler(EventHandler eventHandler)
    {
        this.eventHandler = eventHandler;
    }

    public interface EventHandler {
        void onDayClick(Date date);
    }

}
