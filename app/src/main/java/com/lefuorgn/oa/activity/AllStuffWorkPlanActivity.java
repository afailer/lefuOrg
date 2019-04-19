package com.lefuorgn.oa.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.OaApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.interf.OnScrollChangedListenerImp;
import com.lefuorgn.oa.adapter.AllStuffWorkAdapter;
import com.lefuorgn.oa.bean.Department;
import com.lefuorgn.oa.bean.DepartmentList;
import com.lefuorgn.oa.bean.OaUser;
import com.lefuorgn.oa.bean.StaffPlan;
import com.lefuorgn.oa.bean.StaffPlanCollector;
import com.lefuorgn.oa.bean.StuffPlanList;
import com.lefuorgn.util.PinyinComparator;
import com.lefuorgn.util.PinyinUtils;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.TLog;
import com.lefuorgn.widget.CenterTextView;
import com.lefuorgn.widget.MyHScrollView;
import com.lefuorgn.widget.materialCalendarView.CalendarDay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lefuorgn.R.id.all_stuff_work_recycle;

public class AllStuffWorkPlanActivity extends BaseActivity {

    LinearLayout title;
    RecyclerView recyclerView;
    long startTime,endTime;
    AllStuffWorkAdapter adapter=null;
    MyHScrollView titleHorizontal;
    int monthNum;
    Calendar cal=Calendar.getInstance();
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM月");
    TextView chartTime;
    @Override
    protected void initView() {
        super.initView();
        setToolBarTitle("我的排班");
        setMenuImageView(R.mipmap.search_org);
        title= (LinearLayout) findViewById(R.id.month_work_title);
        titleHorizontal= (MyHScrollView) findViewById(R.id.title_horizontal);
        titleHorizontal.AddOnScrollChangedListener(new OnScrollChangedListenerImp(titleHorizontal));
        recyclerView= (RecyclerView) findViewById(all_stuff_work_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        CalendarDay calendarDay=CalendarDay.from(new Date());
        chartTime =(TextView)findViewById(R.id.chart_time);
        monthNum=calendarDay.getMonth();
        findViewById(R.id.chartLastMonthData).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               startTime=startTime-86400000l;
                resetStart_endtime(CalendarDay.from(new Date(startTime)));
                int monthDaysNum = getMonthDaysNum(new Date(endTime));
                adapter = new AllStuffWorkAdapter(getApplicationContext(),null,monthDaysNum,0,0);
                recyclerView.setAdapter(adapter);
                addMonthTitle(new Date(endTime));
               resetData(CalendarDay.from(new Date(endTime)),500);

            }
        });
        findViewById(R.id.chartNextMonthData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    endTime=endTime+86400000l;
                resetStart_endtime(CalendarDay.from(new Date(endTime)));
                int monthDaysNum = getMonthDaysNum(new Date(endTime));
                adapter = new AllStuffWorkAdapter(getApplicationContext(),null,monthDaysNum,0,0);
                recyclerView.setAdapter(adapter);
                addMonthTitle(new Date(endTime));
                resetData(CalendarDay.from(new Date(endTime)),500);
            }
        });
    }
    Map<Integer,String> weekMap=new HashMap<Integer, String>();
    @Override
    protected void initData() {
        super.initData();
        setWeekDay();
        addMonthTitle(new Date());
        resetStart_endtime(CalendarDay.from(new Date()));
        adapter = new AllStuffWorkAdapter(getApplicationContext(),null,getMonthDaysNum(new Date(endTime)),0,0);
        recyclerView.setAdapter(adapter);
        resetData(CalendarDay.from(new Date()),0);

    }
    private void resetData(CalendarDay d,int resultCode){
        chartTime.setText(sdf.format(new Date(endTime)));
        showWaitDialog();
        if(resultCode==500){
            loadData();
        }else{
            loadStuffList();
        }

    }
    private void resetStart_endtime(CalendarDay date){
        cal.set(Calendar.YEAR,date.getYear());
        cal.set(Calendar.MONTH,date.getMonth());
        cal.set(Calendar.DAY_OF_MONTH,1);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        startTime=cal.getTimeInMillis();
        startTime=startTime-1000;
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY,23);
        cal.set(Calendar.MINUTE,59);
        cal.set(Calendar.SECOND,59);
        endTime=cal.getTimeInMillis();
        TLog.error(endTime+"");
    }

    private void loadData(List<DepartmentList> result){
        for(int i=0;i<result.size();i++){
            setOaUsers(result.get(i).getDepts());
        }
        OaApi.getStuffPlanList(startTime, endTime, idList, new RequestCallback<List<StuffPlanList>>() {
            @Override
            public void onSuccess(List<StuffPlanList> result) {
                adapter.setNewData(getStaffPlan(result));
                hideWaitDialog();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                hideWaitDialog();
            }
        });
    }

    private void loadData(){
        OaApi.getStuffPlanList(startTime, endTime, idList, new RequestCallback<List<StuffPlanList>>() {
            @Override
            public void onSuccess(List<StuffPlanList> result) {
                adapter.setNewData(getStaffPlan(result));
                adapter.notifyDataSetChanged();
                hideWaitDialog();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                hideWaitDialog();
            }
        });
    }
    private List<StaffPlanCollector> getStaffPlan(List<StuffPlanList> stuffPlans){
        List<StaffPlanCollector> datas=new ArrayList<StaffPlanCollector>();

        for(int k=0;k<stuffPlans.size();k++) {
            List<StaffPlan> list = new ArrayList<StaffPlan>();
            for (int i = 1; i <= getMonthDaysNum(new Date(endTime)); i++) {
                StaffPlan staffPlan = null;
                if(stuffPlans.get(k).getOaStaffPlans()!=null) {
                    for (int j = 0; j < stuffPlans.get(k).getOaStaffPlans().size(); j++) {
                        StaffPlan staffPlan1 = stuffPlans.get(k).getOaStaffPlans().get(j);
                        if (i == CalendarDay.from(new Date(staffPlan1.getTime())).getDay()) {
                            staffPlan = staffPlan1;
                        }
                    }
                }
                list.add(staffPlan);
            }
            datas.add(new StaffPlanCollector(list,stuffPlans.get(k).getUser_name()));
        }
        // 将名称转换成拼音
        PinyinUtils.convertedToPinyin(datas);
        // 排序
        Collections.sort(datas, new PinyinComparator<StaffPlanCollector>());
        return datas;
    }
    List<Long> idList=new ArrayList<Long>();
    public void setOaUsers(List<Department> departmentList){
        for(Department department:departmentList){
            for(OaUser oaUser:department.getUsers()){
                idList.add(oaUser.getUser_id());
            }
            if(department.getChildDepts()!=null){
                setOaUsers(department.getChildDepts());
            }
        }
    }
    
    private void addMonthTitle(Date d){
        title.removeAllViews();
        CalendarDay date=CalendarDay.from(d);
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.YEAR,date.getYear());
        cal.set(Calendar.MONTH,date.getMonth());
        int num = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for(int i=1;i<=num;i++){
            cal.set(Calendar.DAY_OF_MONTH,i);
            CenterTextView centerText=new CenterTextView(getApplicationContext());
            centerText.setTextColor(Color.parseColor("#666666"));
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(StringUtils.dip2px(getApplicationContext(),70), ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity= Gravity.CENTER;
            centerText.setLayoutParams(params);
            centerText.setGravity(Gravity.CENTER);
            centerText.setTextSize(17);
            centerText.setText(Html.fromHtml("<strong>"+i+"</strong>"+"<Br/>"+"<small>"+weekMap.get(cal.get(Calendar.DAY_OF_WEEK))+"</small>"));
            title.addView(centerText);
        }
    }
    private void loadStuffList(){
        AppContext instance = AppContext.getInstance();
        int type=1;
        String ids=instance.getUser().getAgency_id()+"";
        OaApi.queryStuffList(type, ids, new RequestCallback<List<DepartmentList>>() {
            @Override
            public void onSuccess(List<DepartmentList> result) {
                loadData(result);
            }

            @Override
            public void onFailure(ApiHttpException e) {
                hideWaitDialog();
            }
        });
    }
    private void setWeekDay(){
        weekMap.put(Calendar.MONDAY,"周一");
        weekMap.put(Calendar.TUESDAY,"周二");
        weekMap.put(Calendar.WEDNESDAY,"周三");
        weekMap.put(Calendar.THURSDAY,"周四");
        weekMap.put(Calendar.FRIDAY,"周五");
        weekMap.put(Calendar.SATURDAY,"周六");
        weekMap.put(Calendar.SUNDAY,"周日");
    }
    public int getMonthDaysNum(Date dateTime){
        CalendarDay date=CalendarDay.from(dateTime);
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.YEAR,date.getYear());
        cal.set(Calendar.MONTH,date.getMonth());
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    @Override
    protected void onMenuClick(View v) {
        super.onMenuClick(v);
        Intent intent=new Intent(getApplicationContext(),SelectStuffActivity.class);
        intent.putExtra("isCopy","isCopy");
        intent.putExtra("isOnlyShowMyOrg",true);
        startActivityForResult(intent,200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==200&&resultCode==500){
            String stuff = data.getStringExtra("stuff");
            String[] split = stuff.split(SelectStuffActivity.DIVIDE_STR);
            idList.clear();
            for(int i=0;i<split.length;i++){
                if(!"".equals(split[i])){
                    String[] split1 = split[i].split(",");
                    if(!"".equals(split1[0])){
                        idList.add(Long.parseLong(split1[0]));
                    }
                }
            }
            resetData(CalendarDay.from(new Date(endTime)),500);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_all_work_arrange;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected boolean hasStatusBar() {
        return true;
    }

    @Override
    protected boolean hasToolBar() {
        return true;
    }
}
