package com.lefuorgn.lefu.activity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.j256.ormlite.dao.ForeignCollection;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.db.model.basic.SignConfig;
import com.lefuorgn.db.model.basic.SignIntervalPointColor;
import com.lefuorgn.db.util.SignConfigManager;
import com.lefuorgn.lefu.bean.SignGraphicData;
import com.lefuorgn.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 体征信息图形化数据显示页面
 */

public class SignGraphicDataActivity extends BaseActivity {

    private boolean saveState; // 当前信息是否加载了保存的状态
    private int mSaveType = 1; // 保存上一次被选中的日期类型
    private TextView mDayBtn, mWeekBtn, mMonthBtn, mYearBtn;
    private TextView mDateStrView;
    private TextView mTypeView;
    private LineChart mLineChart;
    // 前一页和后一页数据加载点击按钮
    private ImageView mLeftBtn, mRightBtn;

    private long oneDayTime = 86400000; // 一天的时间值
    // 左右按钮是否能被触发
    private boolean left, right;
    private long mOldPeopleId; // 老人ID
    private String mTitle; // 页面体征名称
    private long mStartTime; // 开始时间
    private long mEndTime; // 结束时间
    private int mType = -1; // 当前被选中的日期类型
    private int mOrder; // 加载前一页数据还是后一页数据
    private boolean first; // 是否是首次加载数据
    private boolean second; // 是否是第二次加载数据

    private Bundle bundle; // 用于记录加载当前数据的状态

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign_graphic_data;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            saveState = true;
            left = savedInstanceState.getBoolean("left");
            right = savedInstanceState.getBoolean("right");
            mStartTime = savedInstanceState.getLong("mStartTime");
            mEndTime = savedInstanceState.getLong("mEndTime");
            mSaveType = savedInstanceState.getInt("mType");
            mOrder = savedInstanceState.getInt("mOrder");
            first = savedInstanceState.getBoolean("first");
            second = savedInstanceState.getBoolean("second");
        }else {
            saveState = false;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // 保存当前数据类型的状态
        outState.putBoolean("left", bundle.getBoolean("left"));
        outState.putBoolean("right", bundle.getBoolean("right"));
        outState.putLong("mStartTime", bundle.getLong("mStartTime"));
        outState.putLong("mEndTime", bundle.getLong("mEndTime"));
        outState.putInt("mType", bundle.getInt("mType"));
        outState.putInt("mOrder", bundle.getInt("mOrder"));
        outState.putBoolean("first", bundle.getBoolean("first"));
        outState.putBoolean("second", bundle.getBoolean("second"));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void initView() {
        mLineChart = (LineChart) findViewById(R.id.lc_activity_sign_graphic_data);
        mDayBtn = (TextView) findViewById(R.id.tv_activity_sign_graphic_data_day);
        mDayBtn.setOnClickListener(this);
        mWeekBtn = (TextView) findViewById(R.id.tv_activity_sign_graphic_data_week);
        mWeekBtn.setOnClickListener(this);
        mMonthBtn = (TextView) findViewById(R.id.tv_activity_sign_graphic_data_month);
        mMonthBtn.setOnClickListener(this);
        mYearBtn = (TextView) findViewById(R.id.tv_activity_sign_graphic_data_year);
        mYearBtn.setOnClickListener(this);
        mTypeView = (TextView) findViewById(R.id.tv_activity_sign_graphic_data_type);
        mLeftBtn = (ImageView) findViewById(R.id.btn_activity_sign_graphic_data_left);
        mRightBtn = (ImageView) findViewById(R.id.btn_activity_sign_graphic_data_right);
        mLeftBtn.setOnClickListener(this);
        mRightBtn.setOnClickListener(this);
        mDateStrView = (TextView) findViewById(R.id.tv_activity_sign_graphic_data_date);
        findViewById(R.id.btn_activity_sign_graphic_data_turn_screen).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        bundle = new Bundle();
        mTitle = getIntent().getStringExtra("name");
        mOldPeopleId = getIntent().getLongExtra("id", 0);
        setToolBarTitle(mTitle);
        initType(mTitle);
        // 初始化图表信息
        initLineChart();
        // 默认是日, 并初始化数据
        if(saveState) {
            mLeftBtn.setSelected(!left);
            mRightBtn.setSelected(!right);
            // 存在的是上次的记录状态
            setDateView(mSaveType);
        }else {
            initConfig();
            setDateView(1);
        }
    }

    private void initType(String mTitle) {
        mTypeView.setText(mTitle);
        if("血糖".equals(mTitle)) {
            mTypeView.setCompoundDrawablesWithIntrinsicBounds(
                    R.mipmap.sign_graphic_data_blood_sugar, 0, 0, 0);
        }else if("体温".equals(mTitle)) {
            mTypeView.setCompoundDrawablesWithIntrinsicBounds(
                    R.mipmap.sign_graphic_data_temperature, 0, 0, 0);
        }else if("血压".equals(mTitle)) {
            mTypeView.setCompoundDrawablesWithIntrinsicBounds(
                    R.mipmap.sign_graphic_data_blood_pressure, 0, 0, 0);
        }else if("心率".equals(mTitle)) {
            mTypeView.setCompoundDrawablesWithIntrinsicBounds(
                    R.mipmap.sign_graphic_data_heart_rate, 0, 0, 0);
        }
    }

    /**
     * 初始化图表信息
     */
    private void initLineChart() {
        // 获取配置文件
        SignConfig config = SignConfigManager.getSignConfig(mTitle);
        // 初始化图表控件
        mLineChart.setTouchEnabled(true);
        mLineChart.setNoDataTextDescription("暂无数据");
        mLineChart.setDragEnabled(true);
        mLineChart.setDescription(mTitle);
        mLineChart.animateXY(600, 600);
        mLineChart.setDrawGridBackground(true);
        // 设置X轴配置
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        // 设置Y轴右侧配置
        YAxis rightAxis = mLineChart.getAxisRight();
        rightAxis.setLabelCount(1,true);
        rightAxis.setGridColor(Color.TRANSPARENT);
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawGridLines(false);
        // 设置Y轴左侧配置
        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.setLabelCount(4,true);
        leftAxis.setAxisMaxValue((float) config.getyMax());
        leftAxis.setAxisMinValue((float) config.getyMin());
        leftAxis.setStartAtZero(false);
        Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        ForeignCollection<SignIntervalPointColor> pointColors = config.getfLine();
        // 设置颜色区段分割线
        if(pointColors == null) {
            return;
        }
        for (SignIntervalPointColor color : pointColors) {
            LimitLine limitLine = new LimitLine((float) color.getValue(), color.getValue() + "");
            limitLine.setLineColor(Color.parseColor(color.getColor()));
            limitLine.enableDashedLine(8f, 12f, 0);
            limitLine.setLineWidth(0.6f);
            limitLine.setLabelPosition(LimitLabelPosition.RIGHT_BOTTOM);
            limitLine.setTextSize(6f);
            limitLine.setTypeface(tf);
            limitLine.setTextColor(Color.parseColor(color.getColor()));
            leftAxis.addLimitLine(limitLine);
        }
    }

    /**
     * 加载数据
     */
    private void loadSignData() {
        // 记录当前的状态
        bundle.putBoolean("left", left);
        bundle.putBoolean("right", right);
        bundle.putLong("mStartTime", mStartTime);
        bundle.putLong("mEndTime", mEndTime);
        bundle.putInt("mType", mType);
        bundle.putInt("mOrder", mOrder);
        bundle.putBoolean("first", first);
        bundle.putBoolean("second", second);
        // 显示等待对话框
        showLoadingDialog();
        LefuApi.getSignGraphicData(getUri(), mOldPeopleId, mStartTime, mEndTime
                , mType, mOrder, new RequestCallback<List<SignGraphicData>>() {
            @Override
            public void onSuccess(List<SignGraphicData> result) {
                hideLoadingDialog();
                if(first) {
                    // 首次加载数据
                    first = false;
                    second = true;
                    if(result.size() == 0) {
                        mLeftBtn.setSelected(true);
                        left = false;
                    }
                    setLineChartData(result);
                }else if(second) {
                    // 二次加载数据
                    second = false;
                    if(result.size() == 0) {
                        mLeftBtn.setSelected(true);
                        left = false;
                        showToast("已无更早数据");
                    }else {
                        mRightBtn.setSelected(false);
                        right = true;
                        setLineChartData(result);
                    }
                }else {
                    if(result.size() == 0) {
                        // 无数据
                        if(mOrder == 1) {
                            left = false;
                            mLeftBtn.setSelected(true);
                            showToast("已无更早数据");
                        }else {
                            right = false;
                            mRightBtn.setSelected(true);
                            showToast("已无最新数据");
                        }
                    }else {
                        if(mOrder == 1 && mRightBtn.isSelected()) {
                            // 将右按钮重新置为可操作状态
                            mRightBtn.setSelected(false);
                            right = true;
                        }else if(mOrder == 2 && mLeftBtn.isSelected()) {
                            // 将左按钮重新置为可操作状态
                            mLeftBtn.setSelected(false);
                            left = true;
                        }
                        setLineChartData(result);
                    }
                }
            }

            @Override
            public void onFailure(ApiHttpException e) {
                hideLoadingDialog();
                showToast(e.getMessage());
            }
        });
    }

    /**
     * 展示图形化数据(血压特殊, 俩个值)
     * @param data 要展示的数据
     */
    private void setLineChartData(List<SignGraphicData> data) {
        if(!mLineChart.isEmpty()) {
            mLineChart.clearValues();
        }
        // 存放X轴所要存放的数据
        ArrayList<String> XValue = new ArrayList<String>();
        // 存放Y轴实际所需要的数据
        ArrayList<LineDataSet> YValue = new ArrayList<LineDataSet>();
        // 存放Y轴的数据, 血压存放收缩压
        ArrayList<Entry> entry1 = new ArrayList<Entry>();
        // 血压存放舒张压
        ArrayList<Entry> entry2 = new ArrayList<Entry>();
        // 遍历数据
        for (int i = 0; i < data.size(); i++) {
            SignGraphicData graphicData = data.get(i);
            XValue.add(StringUtils.getFormatData(graphicData.getInspect_dt(), "MM-dd"));
            entry1.add(new Entry(StringUtils.toFloat(graphicData.getVal1(), 0), i));
            if("血压".equals(mTitle)) {
                entry2.add(new Entry(StringUtils.toFloat(graphicData.getVal2(), 0), i));
            }
            // 刷新下一次数据请求的开始时间和结束时间
            if(i == 0) {
                mStartTime = graphicData.getInspect_dt() - oneDayTime;
            }
            if(i == data.size() - 1) {
                mEndTime = graphicData.getInspect_dt() + oneDayTime;
            }
        }
        // TODO 这里添加日期显示方式
        mDateStrView.setText(StringUtils.getFormatData(mStartTime + oneDayTime, "yyyy年MM月dd日"));
        // 获取主题色颜色
        int color = getResources().getColor(R.color.colorPrimary);
        // Y轴数据打包
        LineDataSet lineDataSet;
        if("血压".equals(mTitle)) {
            lineDataSet = new LineDataSet(entry1, "收缩压");
            lineDataSet.setColor(Color.parseColor("#FF343F"));
            lineDataSet.setFillColor(Color.parseColor("#FF343F"));
            lineDataSet.setHighLightColor(Color.parseColor("#FF343F"));
            lineDataSet.setCircleColor(Color.parseColor("#FF343F"));
        }else {
            lineDataSet = new LineDataSet(entry1, mTitle);
            lineDataSet.setColor(color);
            lineDataSet.setFillColor(color);
            lineDataSet.setHighLightColor(color);
        }
        lineDataSet.setLineWidth(2.5f);
        lineDataSet.setCircleSize(2.6f);
        lineDataSet.setDrawValues(false);
        lineDataSet.enableDashedLine(0f, 0f, 0f);
        YValue.add(lineDataSet);
        // Y轴数据,血压舒张压
        if("血压".equals(mTitle)) {
            LineDataSet d2 = new LineDataSet(entry2, "舒张压");
            d2.setLineWidth(2.5f);
            d2.setCircleSize(2.6f);
            d2.setHighLightColor(color);
            d2.setColor(color);
            d2.setCircleColor(color);
            d2.setDrawValues(false);
            d2.enableDashedLine(0f, 0f, 0f);
            YValue.add(d2);
        }
        mLineChart.setData(new LineData(XValue, YValue));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_activity_sign_graphic_data_day:
                // 日
                initConfig(); // 重置配置
                setDateView(1);
                break;
            case R.id.tv_activity_sign_graphic_data_week:
                // 周
                initConfig(); // 重置配置
                setDateView(2);
                break;
            case R.id.tv_activity_sign_graphic_data_month:
                // 月
                initConfig(); // 重置配置
                setDateView(3);
                break;
            case R.id.tv_activity_sign_graphic_data_year:
                // 年
                initConfig(); // 重置配置
                setDateView(4);
                break;
            case R.id.btn_activity_sign_graphic_data_left:
                // 左边点击按钮
                if(!left) {
                    // 不可操作
                    return;
                }
                setLeftOrRightBtn(true);
                break;
            case R.id.btn_activity_sign_graphic_data_right:
                // 右边点击按钮
                if(!right) {
                    // 不可操作
                    return;
                }
                setLeftOrRightBtn(false);
                break;
            case R.id.btn_activity_sign_graphic_data_turn_screen:
                // 旋转屏幕
                if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                break;
        }
    }

    /**
     * 前一或者后一数据按钮触发事件
     * @param left true: 前一页数据; false: 后一页数据
     */
    private void setLeftOrRightBtn(boolean left) {
        if(left) {
            // 前一页
            mOrder = 1;
        }else {
            // 后一页
            mOrder = 2;
        }
        // 加载数据
        loadSignData();
    }

    /**
     * 重置当前一些配置信息
     */
    private void initConfig() {
        // 记录当前时间
        long time = System.currentTimeMillis();
        mStartTime = time;
        mEndTime = time;
        // 设置左右加载按钮
        left = true;
        right = false;
        mLeftBtn.setSelected(false);
        mRightBtn.setSelected(true);
        mOrder = 1;
        // 数据首次加载
        first = true;
    }

    /**
     * 根据类型设置日期类型
     * @param type 日期类型: 日、周、月、年
     */
    private void setDateView(int type) {
        if(mType == type) {
            return;
        }
        // 切换数据展示时间段
        mType = type;
        if(type == 1) {
            // 日
            mDayBtn.setSelected(false);
            mWeekBtn.setSelected(true);
            mMonthBtn.setSelected(true);
            mYearBtn.setSelected(true);
        }else if(type == 2) {
            // 周
            mDayBtn.setSelected(true);
            mWeekBtn.setSelected(false);
            mMonthBtn.setSelected(true);
            mYearBtn.setSelected(true);
        }else if(type == 3) {
            // 月
            mDayBtn.setSelected(true);
            mWeekBtn.setSelected(true);
            mMonthBtn.setSelected(false);
            mYearBtn.setSelected(true);
        }else if(type == 4) {
            // 年
            mDayBtn.setSelected(true);
            mWeekBtn.setSelected(true);
            mMonthBtn.setSelected(true);
            mYearBtn.setSelected(false);
        }
        // 加载数据
        loadSignData();
    }

    /**
     * 根据当前页面类型获取uri
     * @return 接口地址
     */
    private String getUri() {
        if("血糖".equals(mTitle)) {
            return "lefuyun/singndata/bloodPressure/getBsrCharts";
        }else if("体温".equals(mTitle)) {
            return "lefuyun/singndata/bloodPressure/getTrCharts";
        }else if("血压".equals(mTitle)) {
            return "lefuyun/singndata/bloodPressure/getBprCharts";
        }else if("心率".equals(mTitle)) {
            return "lefuyun/singndata/bloodPressure/getPrCharts";
        }
        return"";
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
    protected boolean isOrientation() {
        return true;
    }
}
