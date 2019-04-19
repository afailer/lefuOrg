package com.lefuorgn.oa.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.ImageLoader;
import com.lefuorgn.api.remote.OaApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.bean.User;
import com.lefuorgn.db.util.DictionaryManager;
import com.lefuorgn.oa.bean.OaAttendanceRecord;
import com.lefuorgn.util.TLog;
import com.lefuorgn.widget.CircleImageView;
import com.lefuorgn.widget.materialCalendarView.CalendarDay;
import com.lefuorgn.widget.materialCalendarView.MaterialCalendarView;
import com.lefuorgn.widget.materialCalendarView.OnDateSelectedListener;
import com.lefuorgn.widget.materialCalendarView.OnMonthChangedListener;
import com.lefuorgn.widget.materialCalendarView.decorators.EventDecorator;
import com.lefuorgn.widget.materialCalendarView.decorators.MySelectorDecorator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AttendanceRecordActivity extends BaseActivity implements OnDateSelectedListener, OnMonthChangedListener {
    MaterialCalendarView widget;
    Calendar cal=Calendar.getInstance();
    long startTime=0;
    long endTime=0;
    EventDecorator eventDecorator=new EventDecorator(null);
    User user;
    SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
    public static MySelectorDecorator mySelectorDecorator;
    Map<Long,String> map=new HashMap<Long, String>();
    TextView signInTime,signOutTime;
    CircleImageView stuffIcon;
    TextView stuffName,stuffSex;
    long currentUserId;
    @Override
    protected void initView() {
        super.initView();
        widget= (MaterialCalendarView) findViewById(R.id.calendarView);
        signInTime= (TextView) findViewById(R.id.sign_in_time);
        signOutTime= (TextView) findViewById(R.id.sign_out_time);
        stuffIcon= (CircleImageView) findViewById(R.id.stuff_icon);
        stuffName= (TextView) findViewById(R.id.stuff_name);
        stuffSex= (TextView) findViewById(R.id.stuff_sex);
    }

    @Override
    protected void initData() {
        super.initData();
        setToolBarTitle("考勤记录");
        setMenuImageView(R.mipmap.search_org);
        user=AppContext.getInstance().getUser();
        currentUserId=user.getUser_id();
        mySelectorDecorator=new MySelectorDecorator(AttendanceRecordActivity.this);
        setStuffNameAndIcon(user.getIcon(),user.getUser_name(),user.getGender());
        widget.setOnDateChangedListener(this);
        widget.setShowOtherDates(MaterialCalendarView.SHOW_OUT_OF_RANGE);
        widget.state().edit()
                .setMinimumDate(new Date(1388550537133l))//2014年1月1日
                .setMaximumDate(new Date())
                .commit();
        widget.setOnMonthChangedListener(this);
        widget.addDecorators(eventDecorator);
        widget.addDecorator(mySelectorDecorator);
        loadData(user.getUser_id(),1,0,new Date().getTime());
    }
    private void loadData(final long user_id, long pageNo, final long start_time, final long end_time){
        TLog.error(user_id+""+start_time+end_time);
        if((user_id+""+start_time+end_time).equals(map.get(user_id+start_time+end_time))){
            return;
        }
        OaApi.getAttendanceRecord(user_id, pageNo, start_time, end_time, new RequestCallback<List<OaAttendanceRecord>>() {
            @Override
            public void onSuccess(List<OaAttendanceRecord> result) {
                map.put(user_id+start_time+end_time,user_id+""+start_time+end_time);
                addData(result);
            }
            @Override
            public void onFailure(ApiHttpException e) {

            }
        });
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_attendance_record;
    }
    public void addData(List<OaAttendanceRecord> result){
        new ApiSimulator().execute(result);
    }
    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        TLog.error(date.getDate().getTime()+" "+CalendarDay.from(new Date()).getDate().getTime());
        eventDecorator.setDate(date.getDate());
        if(mySelectorDecorator.shouldDecorate(date)) {
            OaAttendanceRecord oaAttendanceRecord = mySelectorDecorator.getOaAttendanceRecord(date);
            if(oaAttendanceRecord.getOa_attendance_arrived_time()!=0){
                signInTime.setText(sdf.format(new Date(oaAttendanceRecord.getDate_time()+oaAttendanceRecord.getOa_attendance_arrived_time())));
            }else{
                signInTime.setText("");
            }
            if(oaAttendanceRecord.getOa_attendance_leave_time()!=0){
                signOutTime.setText(sdf.format(new Date(oaAttendanceRecord.getDate_time()+oaAttendanceRecord.getOa_attendance_leave_time())));
            }else{
                signOutTime.setText("");
            }

        }else{
            signInTime.setText("");
            signOutTime.setText("");
        }
        widget.invalidateDecorators();
    }
    boolean addToday=true;
    private class ApiSimulator extends AsyncTask<List<OaAttendanceRecord>, Void, List<CalendarDay>> {


        @Override
        protected List<CalendarDay> doInBackground(List<OaAttendanceRecord>... params) {
            List<CalendarDay> list=new ArrayList<CalendarDay>();
            for(OaAttendanceRecord record:params[0]){
                CalendarDay day = CalendarDay.from(new Date(record.getDate_time()));
                day.setExtraData(record);
                list.add(day);
            }
            TLog.error("--------------------"+list.size());
            return list;
         }

        @Override
        protected void onPostExecute(List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);
            mySelectorDecorator.addCalendarDays(calendarDays);
            if(addToday){
                onDateSelected(widget,CalendarDay.from(new Date()),true);
                addToday=false;
            }
            widget.invalidateDecorators();
        }
    }
    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        clearEventDecorator();
        cal.set(Calendar.YEAR,date.getYear());
        cal.set(Calendar.MONTH,date.getMonth());
        cal.set(Calendar.DAY_OF_MONTH,1);
        startTime=cal.getTimeInMillis();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        endTime=cal.getTimeInMillis();
        loadData(currentUserId,1,startTime,endTime);
    }


    @Override
    protected void onMenuClick(View v) {
        Intent intent=new Intent(getApplicationContext(),SelectStuffActivity.class);
        intent.putExtra("attendance","attendance");
        intent.putExtra("isOnlyShowMyOrg",true);
        startActivityForResult(intent,600);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==800){
            addToday=true;
            String user_name = data.getStringExtra("user_name");
            String user_icon=data.getStringExtra("user_icon");
            long user_id=data.getLongExtra("user_id",0);
            setStuffNameAndIcon(user_icon,user_name,data.getIntExtra("gender",0));
            currentUserId=user_id;
            loadData(user_id,1,startTime,endTime);
        }
    }

    @Override
    protected boolean hasBackButton() {
        return true;

    }

    @Override
    protected boolean hasToolBar() {
        return true;
    }
    private void setStuffNameAndIcon(String url,String userName,int sex){
        ImageLoader.loadImgForUserDefinedView(url, stuffIcon);
        stuffName.setText(userName);
        stuffSex.setText(DictionaryManager.getContent(sex));
        map.clear();
        mySelectorDecorator.clearData();
        clearEventDecorator();
    }
    private void clearEventDecorator(){
        eventDecorator.clearData();
        signInTime.setText("");
        signOutTime.setText("");
        widget.invalidateDecorators();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mySelectorDecorator=null;
    }
}
