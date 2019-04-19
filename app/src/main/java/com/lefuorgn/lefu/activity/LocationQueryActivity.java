package com.lefuorgn.lefu.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.adapter.BNCommonSettingParam;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.navisdk.adapter.BaiduNaviManager.RoutePlanListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.lefu.adapter.DeviceElderlySearchAdapter;
import com.lefuorgn.lefu.bean.DeviceElderly;
import com.lefuorgn.lefu.bean.NewTrajectory;
import com.lefuorgn.lefu.bean.NewTrajectoryObj;
import com.lefuorgn.lefu.bean.NewTrajectoryObjData;
import com.lefuorgn.lefu.bean.NewTrajectoryObjDataPoint;
import com.lefuorgn.lefu.bean.NewTrajectoryObjDataTime;
import com.lefuorgn.util.DividerItemDecoration;
import com.lefuorgn.util.PinyinUtils;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.TLog;
import com.lefuorgn.widget.ClearEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 老人定位查询
 */

public class LocationQueryActivity extends BaseActivity {

    private static final String APP_FOLDER_NAME = "lefuOrg";

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private MyLocationListener mMyLocationListener;
    private BDLocation mBDLocation; // 当前我的位置
    // 是否是第一次定位
    private boolean isFirstLoc = true;

    private ClearEditText mSearchView;
    private View mContentView; // 搜索内容视图

    private TextView mNameView, mPlaceView, mTimeView;
    private ImageView mRefreshBtn; // 刷新按钮
    private ProgressBar mProgressBar;

    private LinearLayout mDetailsContainer, mBtnContainer;

    private List<DeviceElderly> mDeviceElderlyList;
    private DeviceElderly mDeviceElderly; // 当前被选中的拥有设备的老人
    private int mPosition; // 当前被选中的拥有设备的老人(List)位置
    // Marker图标
    private BitmapDescriptor mBitmap;

    private String mSDCardPath;

    /**
     * 搜索内容展示适配器
     */
    private DeviceElderlySearchAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_location_query;
    }

    @Override
    protected void initView() {
        mSearchView = (ClearEditText) findViewById(R.id.cet_activity_location_query);
        mMapView = (MapView) findViewById(R.id.map_activity_location_query);
        mBtnContainer = (LinearLayout) findViewById(R.id.ll_activity_location_query_btn);
        mDetailsContainer = (LinearLayout) findViewById(R.id.ll_activity_location_query_details);
        mNameView = (TextView) findViewById(R.id.tv_activity_location_query_name);
        mTimeView = (TextView) findViewById(R.id.tv_activity_location_query_time);
        mPlaceView = (TextView) findViewById(R.id.tv_activity_location_query_place);
        mRefreshBtn = (ImageView) findViewById(R.id.btn_activity_location_query_refresh);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_activity_location_query);
        mRefreshBtn.setOnClickListener(this);
        findViewById(R.id.btn_activity_location_query_phone).setOnClickListener(this);
        findViewById(R.id.btn_activity_location_query_navigation).setOnClickListener(this);
        findViewById(R.id.btn_activity_location_query_electronic_fence).setOnClickListener(this);
        findViewById(R.id.btn_activity_location_query_trajectory_playback).setOnClickListener(this);
        initContentPopWindow();
        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                if(mContentView.getVisibility() == View.GONE) {
                    mContentView.setVisibility(View.VISIBLE);
                }
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();
        // 设置地图的显示类型
        // 普通类型
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient = new LocationClient(this);
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        initLocation();
    }

    @Override
    protected void initData() {
        setToolBarTitle("定位查询");
        // mark图标
        mBitmap = BitmapDescriptorFactory.fromResource(R.mipmap.location);
        showWaitDialog();
        LefuApi.getElderlyHasDevice(new RequestCallback<List<DeviceElderly>>() {
            @Override
            public void onSuccess(List<DeviceElderly> result) {
                mDeviceElderlyList = result;
                PinyinUtils.convertedToPinyin(mDeviceElderlyList);
                hideWaitDialog();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                mDeviceElderlyList = new ArrayList<DeviceElderly>();
                showToast(e.getMessage());
                hideWaitDialog();
            }
        });
        if(initDirs()) {
            initNavigation();
        }
    }

    /**
     * 刷新页面信息数据展示
     */
    private void refreshPage() {
        if(mDeviceElderly != null) {
            if(mDetailsContainer.getVisibility() == View.GONE) {
                mDetailsContainer.setVisibility(View.VISIBLE);
            }
            if(mBtnContainer.getVisibility() == View.INVISIBLE) {
                mBtnContainer.setVisibility(View.VISIBLE);
            }
            mNameView.setText(mDeviceElderly.getOlder_name());
            if(mDeviceElderly.getTime() == 0) {
                // 无定位信息
                mTimeView.setText("更新于:");
                mPlaceView.setText("无定位信息");
                mBaiduMap.clear();
                if(mBDLocation != null) {
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(14));
                    MapStatusUpdate u = MapStatusUpdateFactory
                            .newLatLng(new LatLng(mBDLocation.getLatitude(), mBDLocation.getLongitude()));
                    // 以动画的形式更新状态
                    mBaiduMap.animateMapStatus(u);
                }
                return;
            }
            mTimeView.setText(String.format("更新于: %s", StringUtils.getFriendlyTime(mDeviceElderly.getTime())));
            mPlaceView.setText(mDeviceElderly.getAddress());
            double longitude;
            double latitude;
            try {
                longitude = Double.parseDouble(mDeviceElderly.getLongitude());
                latitude = Double.parseDouble(mDeviceElderly.getLatitude());
            }catch (Exception e) {
                TLog.error(e.toString());
                return;
            }
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(20));
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(new LatLng(latitude, longitude));
            // 以动画的形式更新状态
            mBaiduMap.animateMapStatus(u);
            addPoint(latitude, longitude);
        }
    }

    /**
     * 添加Marker节点
     * @param latitude 经度
     * @param longitude 纬度
     */
    private void addPoint(double latitude, double longitude) {
        mBaiduMap.clear();
        //定义 Maker 坐标点
        LatLng point = new LatLng(latitude, longitude);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .icon(mBitmap)
                .position(point)
                .zIndex(9);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }

    @Override
    public void onClick(View v) {
        if(mDeviceElderly == null) {
            showToast("请选择老人");
            return;
        }
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_activity_location_query_refresh:
                // 刷新按钮
                refreshData();
                break;
            case R.id.btn_activity_location_query_phone:
                // 拨打电话
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                        + mDeviceElderly.getSim_phone()));
                phoneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(phoneIntent);
                break;
            case R.id.btn_activity_location_query_navigation:
                // 导航
                routePlanToNavigation();
                break;
            case R.id.btn_activity_location_query_electronic_fence:
                // 电子围栏
                intent = new Intent(this, ElectronicFenceActivity.class);
                intent.putExtra("id", mDeviceElderly.getOlder_id());
                intent.putExtra("deviceId", mDeviceElderly.getDevice_id());
                startActivity(intent);
                break;
            case R.id.btn_activity_location_query_trajectory_playback:
                // 轨迹回放
                intent = new Intent(this, TrajectoryPlaybackActivity.class);
                intent.putExtra("id", mDeviceElderly.getOlder_id());
                startActivity(intent);
                break;
        }
    }

    /**
     * 刷新老人当前位置数据
     */
    private void refreshData() {
        mRefreshBtn.setEnabled(false);
        mProgressBar.setVisibility(View.VISIBLE);
        // 发送老人位置刷新请求
        LefuApi.sendLocationUpdateCommand(mDeviceElderly.getImei(), new RequestCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {

                    @Override
                    public void run() {
                        // 3秒后请求最新位置信息
                        getNewLocationInfo();
                    }
                };
                timer.schedule(task, 3000);
            }

            @Override
            public void onFailure(ApiHttpException e) {
                showToast(e.getMessage());
                TLog.error("===============> " + e.toString());
                mRefreshBtn.setEnabled(true);
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 获取最新的位置信息
     */
    private void getNewLocationInfo() {
        LefuApi.getNewLocationInfo(mDeviceElderly.getImei(), new RequestCallback<NewTrajectory>() {
            @Override
            public void onSuccess(NewTrajectory result) {
                mRefreshBtn.setEnabled(true);
                mProgressBar.setVisibility(View.GONE);
                if(result == null) {
                    showToast("位置刷新失败");
                    return;
                }
                NewTrajectoryObj newTrajectoryObj = result.getObj();
                if(newTrajectoryObj == null) {
                    showToast("位置刷新失败");
                    return;
                }
                NewTrajectoryObjData newTrajectoryObjData = newTrajectoryObj.getLocationdata();
                if(newTrajectoryObjData == null) {
                    showToast("位置刷新失败");
                    return;
                }
                NewTrajectoryObjDataPoint newTrajectoryObjDataPoint = newTrajectoryObjData.getPoint();
                NewTrajectoryObjDataTime newTrajectoryObjDataTime = newTrajectoryObjData.getTime_begin();
                if(newTrajectoryObjDataPoint == null || newTrajectoryObjDataTime == null) {
                    showToast("位置刷新失败");
                    return;
                }
                List<String> point = newTrajectoryObjDataPoint.getCoordinates();
                long time = newTrajectoryObjDataTime.get$date();
                if(point == null || point.size() != 2) {
                    showToast("位置刷新失败");
                    return;
                }
                DeviceElderly elderly = mAdapter.getItem(mPosition);
                elderly.setAddress(newTrajectoryObjData.getAddress());
                elderly.setLongitude(point.get(0));
                elderly.setLatitude(point.get(1));
                elderly.setTime(time);
                mDeviceElderly = elderly;
                refreshPage();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                mRefreshBtn.setEnabled(true);
                mProgressBar.setVisibility(View.GONE);
                showToast(e.getMessage());
                TLog.error("===============> " + e.toString());
            }
        });
    }

    /**
     * 初始化百度定位功能,实现定位
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        // 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll"); // 可选，默认gcj02，设置返回的定位结果坐标系
        option.setOpenGps(true); // 可选，默认false,设置是否使用gps
        option.setIgnoreKillProcess(false); // 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListener implements BDLocationListener {

        @SuppressLint("NewApi")
        @Override
        public void onReceiveLocation(BDLocation location) {
            TLog.log("定位功能事件触发...");
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            mBDLocation = location;
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            TLog.log("定位成功后成功后,进行中心位置定位");
            if (isFirstLoc) {
                isFirstLoc = false;
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(14));
                BitmapDescriptor resource = BitmapDescriptorFactory.fromResource(R.mipmap.location_me);
                MyLocationConfiguration configuration = new MyLocationConfiguration(
                        MyLocationConfiguration.LocationMode.FOLLOWING, false, resource);
                mBaiduMap.setMyLocationConfiguration(configuration);
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    /**
     * 通过字段过滤老人列表
     * @param filterStr 指定的字符串
     */
    private void filterData(String filterStr) {
        List<DeviceElderly> filterDateList = new ArrayList<DeviceElderly>();
        filterDateList.clear();
        if(StringUtils.isEmpty(filterStr)) {
            filterDateList.addAll(mDeviceElderlyList);
        }else {
            filterStr = filterStr.toLowerCase();
            for (DeviceElderly o : mDeviceElderlyList) {
                if (o.getCharacters().startsWith(filterStr) ||
                        o.getFullPinYin().startsWith(filterStr) ||
                        o.getInitial().startsWith(filterStr)) {
                    filterDateList.add(o);
                }
            }
        }
        mAdapter.setNewData(filterDateList);
    }

    /**
     * 初始化老人选择PopWindow控件
     */
    private void initContentPopWindow() {
        mContentView = findViewById(R.id.ll_popup_window_activity_location_query);
        findViewById(R.id.v_popup_window_activity_location_query)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContentView.setVisibility(View.GONE);
                    }
                });
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv_popup_window_activity_location_query);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.recycler_view_item_division_color)));
        mAdapter = new DeviceElderlySearchAdapter();
        rv.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                mDeviceElderly = (DeviceElderly) mAdapter.getData().get(i);
                mPosition = i;
                mSearchView.setText(mDeviceElderly.getOlder_name());
                mSearchView.setSelection(mSearchView.getText().length());
                mContentView.setVisibility(View.GONE);
                hideKeyBoard(view);
                refreshPage();
            }
        });
    }

    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if(mSDCardPath == null) {
            return false;
        }
        File file = new File(mSDCardPath, APP_FOLDER_NAME);
        if(!file.exists()) {
            try {
                file.mkdir();
            }catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取SD卡路径
     */
    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    /**
     * 初始化导航信息SatnavActivity
     */
    private void initNavigation() {
        TLog.error("开始初始化导航信息: " + StringUtils.getFormatData(System.currentTimeMillis(), "HH:mm:ss"));
        BaiduNaviManager.getInstance().init(this, mSDCardPath, APP_FOLDER_NAME,
                new BaiduNaviManager.NaviInitListener() {

                    @Override
                    public void onAuthResult(int status, String msg) {
                        if (0 == status) {
                            TLog.log("key校验成功");
                        } else {
                            TLog.log("key校验失败");
                        }

                    }

                    @Override
                    public void initSuccess() {
                        TLog.error("初始化导航信息成功: " + StringUtils.getFormatData(System.currentTimeMillis(), "HH:mm:ss"));
                        // 百度导航引擎初始化成功
                        // BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
                        BNaviSettingManager
                                .setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
                        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
                        // BNaviSettingManager.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
                        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
                        BNaviSettingManager.setIsAutoQuitWhenArrived(true);
                        Bundle bundle = new Bundle();
                        // 必须设置APPID，否则会静音
                        bundle.putString(BNCommonSettingParam.TTS_APP_ID, "8581770");
                        BNaviSettingManager.setNaviSdkParam(bundle);
                    }

                    @Override
                    public void initStart() {
                        TLog.log("百度导航引擎初始化开始");
                    }

                    @Override
                    public void initFailed() {
                        TLog.log("百度导航引擎初始化失败");
                    }
                }, null, null, new BaiduNaviManager.TTSPlayStateListener() {

                    @Override
                    public void playStart() {
                        TLog.log("playStart");
                    }

                    @Override
                    public void playEnd() {
                        TLog.log("playEnd");
                    }
                });

    }

    /**
     * 计算导航路线
     */
    private void routePlanToNavigation() {
        if(mBDLocation == null) {
            showToast("定位信息获取失败");
            return;
        }
        double longitude;
        double latitude;
        try {
            longitude = Double.parseDouble(mDeviceElderly.getLongitude());
            latitude = Double.parseDouble(mDeviceElderly.getLatitude());
        }catch (Exception e) {
            showToast("无老人定位信息");
            TLog.error(e.toString());
            return;
        }
        BNRoutePlanNode sNode = new BNRoutePlanNode(mBDLocation.getLongitude(), mBDLocation.getLatitude(), mBDLocation.getDistrict(), null, CoordinateType.BD09LL);
        BNRoutePlanNode eNode = new BNRoutePlanNode(longitude, latitude, mDeviceElderly.getOlder_name(), null, CoordinateType.BD09LL);
        TLog.log("创建起始点-----------");
        List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
        list.add(sNode);
        list.add(eNode);
        showLoadingDialog("导航开启中...");
        BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(sNode));
    }

    /**
     * 路线设计接口
     */
    public class DemoRoutePlanListener implements RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
	        /**
	         * 设置途径点以及resetEndNode会回调该接口
	         */
            Intent intent = new Intent(LocationQueryActivity.this, NavigationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(NavigationActivity.ROUTE_PLAN_NODE, mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);
            hideLoadingDialog();
        }

        @Override
        public void onRoutePlanFailed() {
            hideLoadingDialog();
            showToast("算路失败");
        }
    }

    /**
     * 隐藏软件盘
     * @param v 视图
     */
    private void hideKeyBoard(View v) {
        // 1.得到InputMethodManager对象
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 2.调用hideSoftInputFromWindow方法隐藏软键盘
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0); // 强制隐藏键盘
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        // 退出时销毁定位
        mLocationClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.unRegisterLocationListener(mMyLocationListener);
        mMapView.onDestroy();
        mMapView = null;
        mBitmap.recycle();
        super.onDestroy();
    }

    @Override
    protected boolean hasToolBar() {
        return true;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }
}
