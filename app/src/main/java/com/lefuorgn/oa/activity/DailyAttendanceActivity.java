package com.lefuorgn.oa.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.OaApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.oa.bean.SignOrSingOut;
import com.lefuorgn.oa.dialog.DailyAttendanceDialog;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.TLog;

import java.util.Calendar;

/**
 * 日常考勤主页面
 */

public class DailyAttendanceActivity extends BaseActivity {

    private LinearLayout mLocationInfoBackground; // 当前定位信息背景
    private TextView mLocationInfoView; // 当前定位信息指示
    private TextView mDateView, mTimeView;

    private LocationClient mLocationClient;
    private MyLocationListener mMyLocationListener;

    private boolean isSignOrSignOut; // 是否在指定的范围内
    private boolean sign; // 签到
    private boolean signOut; // 签退

    private BDLocation mBDLocation; // 当前位置信息

    private final Handler mHandler = new Handler();

    private final Runnable mTimeRefresher = new Runnable() {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Calendar calendar = Calendar.getInstance();
                    mTimeView.setText(StringUtils.getFormatData(calendar.getTimeInMillis(), "HH:mm:ss"));
                }
            });
            mHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_daily_attendance;
    }

    @Override
    protected void initView() {
        mLocationInfoBackground = (LinearLayout) findViewById(R.id.ll_activity_daily_attendance);
        mLocationInfoView = (TextView) findViewById(R.id.tv_activity_daily_attendance_location_info);
        findViewById(R.id.btn_activity_daily_attendance).setOnClickListener(this);
        mDateView = (TextView) findViewById(R.id.tv_activity_daily_attendance_date);
        mTimeView = (TextView) findViewById(R.id.tv_activity_daily_attendance_time);
        findViewById(R.id.btn_activity_daily_attendance_sign).setOnClickListener(this);
        findViewById(R.id.btn_activity_daily_attendance_sign_out).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        setToolBarTitle("日常考勤");
        isSignOrSignOut = false;
        mDateView.setText(StringUtils.getFormatData(System.currentTimeMillis(), "当前时间: yyyy年MM月dd日"));
        mLocationInfoView.setText("定位中...");
        mLocationClient = new LocationClient(this);
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        initLocation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_activity_daily_attendance:
                // 定位刷新
                isSignOrSignOut = false;
                sign = false;
                signOut = false;
                refreshLocation();
                break;
            case R.id.btn_activity_daily_attendance_sign:
                // 签到
                isSignOrSignOut = true;
                sign = true;
                signOut = false;
                refreshLocation();
                break;
            case R.id.btn_activity_daily_attendance_sign_out:
                // 签退
                isSignOrSignOut = true;
                sign = false;
                signOut = true;
                refreshLocation();
                break;
        }
    }

    /**
     * 请求重新刷新位置
     */
    private void refreshLocation() {
        int code = mLocationClient.requestLocation();
        if(code == 6) {
            // 两次请求时间太短
            if(!isSignOrSignOut) {
                return;
            }
            if (mBDLocation == null) {
                mBDLocation = new BDLocation();
                mBDLocation.setLongitude(0);
                mBDLocation.setLatitude(0);
            }
            isSpecifiedRange(mBDLocation);
        }else if(code == 1) {
            showToast("定位服务未启动");
        }else {
            showLoadingDialog();
        }
    }

    /**
     * 请求当前位置是否在指定的范围内
     */
    private void isSpecifiedRange(BDLocation l) {
        final String longitude = l.getLongitude() + "";
        final String latitude = l.getLatitude() + "";
        final String mac = getMacAddress();
        OaApi.isSpecifiedRange(longitude, latitude, mac, new RequestCallback<String>() {
            @Override
            public void onSuccess(String result) {
                refreshLocationInfoView(true, longitude, latitude, mac);
            }

            @Override
            public void onFailure(ApiHttpException e) {
                refreshLocationInfoView(false, "", "", "");
            }
        });
    }

    /**
     * 刷新定位显示控件内容
     */
    private void refreshLocationInfoView(boolean success, String longitude, String latitude, String mac) {
        if(success) {
            mLocationInfoView.setText("您已进入考勤区域");
            mLocationInfoBackground.setBackgroundColor(Color.parseColor("#156DC7"));
            if(isSignOrSignOut) {
                // 当前为签退或者签到过程
                isSignOrSignOut = false;
                signOrSignOut(longitude, latitude, mac);
            }else {
                hideLoadingDialog();
            }
        }else {
            mLocationInfoView.setText("您未进入考勤区域");
            mLocationInfoBackground.setBackgroundColor(Color.parseColor("#FF5064"));
            hideLoadingDialog();
            if(isSignOrSignOut) {
                // 当前为签退或者签到过程
                isSignOrSignOut = false;
                new DailyAttendanceDialog().show(getSupportFragmentManager(), "refreshLocation");
            }
        }
    }

    /**
     * 签到或者签退
     */
    private void signOrSignOut(String longitude, String latitude, String mac) {
        final int type;
        if(sign && !signOut) {
            // 签到
            type = 1;
        }else if(!sign && signOut) {
            // 签退
            type = 2;
        }else {
            hideLoadingDialog();
            return;
        }
        // 签到或签退请求
        OaApi.signOrSignOut(type, longitude, latitude, mac, new RequestCallback<SignOrSingOut>() {
            @Override
            public void onSuccess(SignOrSingOut result) {
                if(result == null) {
                    showToast("请求失败");
                    return;
                }
                Intent intent = new Intent(DailyAttendanceActivity.this, SignOrSignOutResultActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("state", result.getStatus());
                intent.putExtra("time", result.getTime());
                intent.putExtra("info", result.getMsg());
                startActivity(intent);
                hideLoadingDialog();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                showToast(e.getMessage());
                hideLoadingDialog();
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
//        int span=1000;
//        option.setScanSpan(span); // 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true); // 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true); // 可选，默认false,设置是否使用gps
        option.setLocationNotify(true); // 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true); // 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true); // 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false); // 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false); // 可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false); // 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        showLoadingDialog();
    }

    /**
     * 定位SDK监听函数
     */
    private class MyLocationListener implements BDLocationListener {

        @SuppressLint("NewApi")
        @Override
        public void onReceiveLocation(BDLocation location) {
            TLog.log("定位功能事件触发...");
            // map view 销毁后不在处理新接收的位置
            if (location == null) {
                location = new BDLocation();
                location.setLongitude(0);
                location.setLatitude(0);
            }
            mBDLocation = location;
            isSpecifiedRange(location);
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            TLog.error(s);
        }

    }

    /**
     * 获取WiFi的MAC地址
     */
    private String getMacAddress() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        int state = wifiManager.getWifiState();
        if(state != WifiManager.WIFI_STATE_ENABLED) {
            return "";
        }
        WifiInfo info = wifiManager.getConnectionInfo();
        if(info != null) {
            return info.getBSSID() != null ? info.getBSSID().toUpperCase().replace(":", "-") : "";
        }
        return "";
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.post(mTimeRefresher);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeCallbacks(mTimeRefresher);
    }

    @Override
    protected void onDestroy() {
        mLocationClient.stop();
        mLocationClient.unRegisterLocationListener(mMyLocationListener);
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
