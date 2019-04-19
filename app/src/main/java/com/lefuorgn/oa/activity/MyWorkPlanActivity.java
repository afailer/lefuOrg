package com.lefuorgn.oa.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.View;
import android.widget.LinearLayout;
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
import com.lefuorgn.oa.bean.StaffPlan;
import com.lefuorgn.oa.bean.StuffPlanList;
import com.lefuorgn.util.TLog;
import com.lefuorgn.widget.CircleImageView;
import com.lefuorgn.widget.materialCalendarView.CalendarDay;
import com.lefuorgn.widget.materialCalendarView.MaterialCalendarView;
import com.lefuorgn.widget.materialCalendarView.OnMonthChangedListener;
import com.lefuorgn.widget.materialCalendarView.decorators.StuffPlanDecorator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyWorkPlanActivity extends BaseActivity implements OnMonthChangedListener {
    MaterialCalendarView materialCalendarView;
    long start_time,end_time;
    User user;
    Calendar cal=Calendar.getInstance();
    Map<Long,Long> map=new HashMap<Long, Long>();
    CircleImageView circleImageView;
    TextView stuffSex,stuffName;
    LinearLayout work_count;
    @Override
    protected void initView() {
        super.initView();
        setToolBarTitle("我的排班");
        planDecoratorMap=new HashMap<String, StuffPlanDecorator>();
        user= AppContext.getInstance().getUser();
        resetStart_endtime(CalendarDay.from(new Date()));
        materialCalendarView= (MaterialCalendarView) findViewById(R.id.calendarView);
        materialCalendarView.setShowOtherDates(MaterialCalendarView.SHOW_OUT_OF_RANGE);
        materialCalendarView.state().edit()
                .setMinimumDate(new Date(1388550537133l))//2014年1月1日
                .commit();
        materialCalendarView.setOnMonthChangedListener(this);
       // materialCalendarView.addDecorator(stuffPlanDecorator);
        //materialCalendarView.setOnDateChangedListener(this);
        materialCalendarView.invalidateDecorators();
        circleImageView= (CircleImageView) findViewById(R.id.stuff_icon);
        stuffSex= (TextView) findViewById(R.id.stuff_sex);
        work_count= (LinearLayout) findViewById(R.id.work_count);
        stuffName= (TextView) findViewById(R.id.stuff_name);
        stuffName.setText(user.getUser_name());
    }

    @Override
    protected void initData() {
        super.initData();
        setMenuTextView("全部");
        loadDatas(start_time,end_time,getIds());
        ImageLoader.loadImgByNormalImg(user.getIcon(),R.mipmap.attendance_approval_select,R.mipmap.attendance_approval_select,circleImageView);
        stuffSex.setText(DictionaryManager.getContent(user.getGender()));
    }
    //SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public void loadDatas(final long start_time, long end_time, List<Long> ids){
        if(map.containsKey(start_time)){//判断当前存储工作数据的map中是否已经保存了这个月的数据
            if(viewsMap.containsKey(start_time)){
                work_count.removeAllViews();
                for(int i=0;i<viewsMap.get(start_time).size();i++){
                    work_count.addView(viewsMap.get(start_time).get(i));
                }
            }else{
                work_count.removeAllViews();
            }
            return;
        }
        OaApi.getStuffPlanList(start_time, end_time,ids,new RequestCallback<List<StuffPlanList>>() {
            @Override
            public void onSuccess(List<StuffPlanList> result) {
                map.put(start_time,start_time);
                handlePlanResult(result.get(0).getOaStaffPlans());//处理请求到的数据 1.
                materialCalendarView.invalidateDecorators();
                work_count.removeAllViews();
                for(String s:workMap.keySet()){
                    String[] split = s.split(",");
                    if(split.length==2&& split[0]!=null && split[1]!=null){
                        putItem(split[1],split[0],workMap.get(s));
                    }
                }
                //将view保存，下次如果是同样的时间戳，直接添加保存的数据
                int childCount = work_count.getChildCount();
                if(childCount!=0) {
                    List<View> list = new ArrayList<View>();
                    for (int i = 0; i < childCount; i++) {
                        list.add(work_count.getChildAt(i));
                    }
                    viewsMap.put(start_time,list);
                }
            }

            @Override
            public void onFailure(ApiHttpException e) {

            }
        });
    }
    private static Drawable generateCircleDrawable(final int color) {
        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(color);
        return drawable;
    }
    public static Map<String,StuffPlanDecorator> planDecoratorMap=new HashMap<String, StuffPlanDecorator>();
    private Map<String,Integer> workMap=new HashMap<String, Integer>();//保存每个月本人的班次统计的颜色，班次类型（早班，晚班）key为颜色和类型，value为次数
    private void handlePlanResult(List<StaffPlan> plans){
        if(plans==null){
            return;
        }
        for(StaffPlan plan:plans){
            if(workMap.containsKey(plan.getOa_scheduling_short_name()+","+plan.getOa_scheduling_color())){//当月已经有了该班次（如已经有了“早班”）
                int times = workMap.get(plan.getOa_scheduling_short_name()+","+plan.getOa_scheduling_color());
                workMap.remove(plan.getOa_scheduling_short_name()+","+plan.getOa_scheduling_color());
                times++;//该班次的次数加一天
                workMap.put(plan.getOa_scheduling_short_name()+","+plan.getOa_scheduling_color(),times);
            }else{//如果第一次碰到该班次，就保存1次
                workMap.put(plan.getOa_scheduling_short_name()+","+plan.getOa_scheduling_color(),1);
            }
            if(planDecoratorMap.containsKey(plan.getOa_scheduling_short_name())){
                planDecoratorMap.get(plan.getOa_scheduling_short_name()).addDataValue(plan);
            }else{
                if(plan.getOa_scheduling_short_name()==null||plan.getOa_scheduling_color()==null||"".equals(plan.getOa_scheduling_short_name())||"".equals(plan.getOa_scheduling_color())){
                    continue;
                }
                planDecoratorMap.put(plan.getOa_scheduling_short_name(),new StuffPlanDecorator(plan.getOa_scheduling_short_name(),plan.getOa_scheduling_color()));
                planDecoratorMap.get(plan.getOa_scheduling_short_name()).addDataValue(plan);
                materialCalendarView.addDecorator(planDecoratorMap.get(plan.getOa_scheduling_short_name()));
            }
        }
    }
    private void putItem(String color,String name,int times){
        TLog.error(name+" "+color+" "+times);
        try {
            View view = View.inflate(getApplicationContext(), R.layout.plan_item, null);
            TextView workPoint = (TextView) view.findViewById(R.id.color_point);
            workPoint.setBackgroundDrawable(generateCircleDrawable(Color.parseColor(color)));
            TextView workContent = (TextView) view.findViewById(R.id.work_content);
            workContent.setText(name + ":" + times + "次");
            work_count.addView(view);
        }catch (Exception e){}
    }
    private List<Long> getIds(){
        List<Long> ids=new ArrayList<Long>();
        ids.add( user.getUser_id());
        return ids;
    }
    @Override
    protected void onMenuClick(View v) {
        super.onMenuClick(v);
        Intent intent=new Intent(getApplicationContext(),AllStuffWorkPlanActivity.class);
        startActivityForResult(intent,200);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_work_arrange;
    }

    @Override
    protected boolean hasStatusBar() {
        return true;
    }

    @Override
    protected boolean hasToolBar() {
        return true;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        resetStart_endtime(date);

        loadDatas(start_time,end_time,getIds());
    }
    Map<Long,List<View>> viewsMap=new HashMap<Long, List<View>>();
    private void resetStart_endtime(CalendarDay date){
        workMap.clear();
        cal.set(Calendar.YEAR,date.getYear());
        cal.set(Calendar.MONTH,date.getMonth());
        cal.set(Calendar.DAY_OF_MONTH,1);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        start_time=cal.getTimeInMillis();
        start_time=start_time-1000;
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY,23);
        cal.set(Calendar.MINUTE,59);
        cal.set(Calendar.SECOND,59);
        end_time=cal.getTimeInMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        planDecoratorMap=null;
    }
}
