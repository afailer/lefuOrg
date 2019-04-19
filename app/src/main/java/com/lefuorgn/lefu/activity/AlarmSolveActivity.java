package com.lefuorgn.lefu.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.lefu.adapter.AlarmSolverAdapter;
import com.lefuorgn.lefu.bean.AlarmEntryDetails;
import com.lefuorgn.lefu.bean.AlarmEntrySolver;
import com.lefuorgn.lefu.bean.AlarmEntryWarning;
import com.lefuorgn.lefu.fragment.AlarmInformationDetailsFragment;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.TLog;
import com.lefuorgn.widget.MapContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * 告警信息处理中详细内容展示页面
 */

public class AlarmSolveActivity extends BaseActivity {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private TextView mNameView, mDetailsView, mPlaceView;
    private EditText mRemarkView;

    private long mId; // 告警信息ID
    private long mAgencyId; // 机构ID
    private String mAgencyName; // 机构名称
    private int mStatus; // 当年页面告警信息状态

    // Marker图标
    private BitmapDescriptor mBitmap;

    private RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_alarm_solve;
    }

    @Override
    protected void initView() {
        ScrollView scrollView = (ScrollView) findViewById(R.id.sv_activity_alarm_solve);
        MapContainer mapContainer = (MapContainer) findViewById(R.id.mc_activity_alarm_solve);
        mapContainer.setScrollView(scrollView);
        mMapView = (MapView) findViewById(R.id.mv_activity_alarm_solve);
        mBaiduMap = mMapView.getMap();
        // 设置地图的显示类型
        // 普通类型
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mNameView = (TextView) findViewById(R.id.tv_activity_alarm_solve_name);
        mDetailsView = (TextView) findViewById(R.id.tv_activity_alarm_solve_details);
        mPlaceView = (TextView) findViewById(R.id.tv_activity_alarm_solve_place);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_activity_alarm_solve);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 根据状态初始化按钮控件以及处理结果控件
        mStatus = getIntent().getIntExtra("status", 0);
        mRemarkView = (EditText) findViewById(R.id.et_activity_alarm_solve);
        TextView btn = (TextView) findViewById(R.id.btn_activity_alarm_solve_processing);
        if(mStatus == AlarmInformationDetailsFragment.BUNDLE_ALARM_INFO_STATUS_UNRESOLVED) {
            // 未处理
            btn.setText("接收报警");
            btn.setOnClickListener(this);
        }else if(mStatus == AlarmInformationDetailsFragment.BUNDLE_ALARM_INFO_STATUS_SOLVING) {
            // 处理中
            btn.setText("提交");
            btn.setOnClickListener(this);
            mRemarkView.setVisibility(View.VISIBLE);
        }else {
            btn.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {
        mId = getIntent().getLongExtra("id", 0);
        mAgencyId = getIntent().getLongExtra("agencyId", 0);
        mAgencyName = getIntent().getStringExtra("agencyName");
        String title = getIntent().getStringExtra("title");
        setToolBarTitle(title);
        mBitmap = BitmapDescriptorFactory.fromResource(R.mipmap.location);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        ViewGroup.LayoutParams params = mMapView.getLayoutParams();
        params.height = metrics.heightPixels / 2;
        mMapView.setLayoutParams(params);
        showWaitDialog();
        LefuApi.getAlarmEntryDetails(mId, mAgencyId, new RequestCallback<AlarmEntryDetails>() {
            @Override
            public void onSuccess(AlarmEntryDetails result) {
                hideWaitDialog();
                if(result == null) {
                    showToast("警告详情获取失败");
                    return;
                }
                initPageInfo(result);
            }

            @Override
            public void onFailure(ApiHttpException e) {
                showToast(e.getMessage());
                hideWaitDialog();
            }
        });
    }

    /**
     * 设置页面内容详情
     */
    private void initPageInfo(AlarmEntryDetails details) {
        AlarmEntryWarning warning = details.getSosWarningForm();
        if(warning != null) {
            mNameView.setText(warning.getOlder_name());
            String date = StringUtils.getFormatData(warning.getTime(), "yyyy-MM-dd HH:mm");
            String status;
            if(warning.getStatus() == AlarmInformationDetailsFragment.BUNDLE_ALARM_INFO_STATUS_UNRESOLVED) {
                status = "未处理";
            }else if(warning.getStatus() == AlarmInformationDetailsFragment.BUNDLE_ALARM_INFO_STATUS_SOLVING) {
                status = "处理中";
            }else if(warning.getStatus() == AlarmInformationDetailsFragment.BUNDLE_ALARM_INFO_STATUS_SOLVED) {
                status = "已处理";
            }else {
                status = "";
            }
            String sb = "身份证号: " + warning.getDocument_number() + "\n" +
                    "告警时间: " + date + "\n" +
                    "告警内容: " + getType(warning.getTypes()) + "\n" +
                    "处理状态: " + status;
            if(!StringUtils.isEmpty(mAgencyName)) {
                sb = "所在机构: " + mAgencyName + "\n" + sb;
            }
            mDetailsView.setText(sb);
            mPlaceView.setText(warning.getAddress());
            // 添加地图坐标位置
            try {
                double longitude = Double.parseDouble(warning.getLongitude());
                double latitude = Double.parseDouble(warning.getLatitude());
                if(longitude > 0 || latitude > 0) {
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(18));
                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(new LatLng(latitude, longitude));
                    // 以动画的形式更新状态
                    mBaiduMap.animateMapStatus(u);
                    // 显示定位点
                    addPoint(latitude, longitude);
                    // 显示电子围栏区域
                    addOverlay(details.getCoordinate());
                }
            }catch (Exception e) {
                TLog.error(e.toString());
                showToast("老人坐标点获取失败");
            }
        }
        List<AlarmEntrySolver> alarmEntrySolvers = details.getSosWarningUserMaps();
        if(alarmEntrySolvers == null) {
            alarmEntrySolvers = new ArrayList<AlarmEntrySolver>();
        }
        AlarmSolverAdapter adapter = new AlarmSolverAdapter(alarmEntrySolvers);
        mRecyclerView.setAdapter(adapter);

    }

    /**
     * 添加Marker节点
     * @param latitude 经度
     * @param longitude 纬度
     */
    private void addPoint(double latitude, double longitude) {
        //定义Maker坐标点
        LatLng point = new LatLng(latitude, longitude);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .zIndex(9)
                .icon(mBitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }

    /**
     * 绘制覆盖物
     * @param coordinateStr 坐标
     */
    private void addOverlay(String coordinateStr) {
        List<LatLng> pts = StringUtils.getLatLngList(coordinateStr);
        // 移动到安全区域
        if(pts.size() > 0) {
            //构建用户绘制多边形的Option对象
            OverlayOptions polygonOption = new PolygonOptions()
                    .points(pts)
                    .stroke(new Stroke(5, 0xAA00FF00))
                    .fillColor(0xAA147BE7);
            mBaiduMap.addOverlay(polygonOption);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_activity_alarm_solve_processing:
                // 处理告警
                if(mStatus == AlarmInformationDetailsFragment.BUNDLE_ALARM_INFO_STATUS_UNRESOLVED) {
                    // 未处理
                    receiveAlarm(mId);
                }else if(mStatus == AlarmInformationDetailsFragment.BUNDLE_ALARM_INFO_STATUS_SOLVING) {
                    // 处理中
                    String remark = mRemarkView.getText().toString();
                    if(StringUtils.isEmpty(remark)) {
                        showToast("处理结果不能为空");
                        return;
                    }
                    processingAlarm(mId, remark);
                }
                break;
        }
    }

    /**
     * 接收告警
     */
    private void receiveAlarm(long id) {
        showLoadingDialog();
        LefuApi.receiveAlarmDetails(id, mAgencyId, new RequestCallback<String>() {
            @Override
            public void onSuccess(String result) {
                showToast("报警接收成功");
                hideLoadingDialog();
                setResult(200);
                finish();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                showToast(e.getMessage());
                hideLoadingDialog();
            }
        });
    }

    /**
     * 处理告警
     */
    private void processingAlarm(long id, String remark) {
        showLoadingDialog();
        LefuApi.processingAlarmDetails(id, remark, new RequestCallback<String>() {
            @Override
            public void onSuccess(String result) {
                showToast("提交成功");
                hideLoadingDialog();
                setResult(200);
                finish();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                showToast(e.getMessage());
                hideLoadingDialog();
            }
        });
    }

    /**
     * 获取告警信息类型
     */
    private String getType(int type) {
        String result = "";
        if(type == AlarmInformationDetailsActivity.INTENT_ALARM_TYPE_SOS) {
            result = "SOS告警";
        }else if(type == AlarmInformationDetailsActivity.INTENT_ALARM_TYPE_ELECTRONIC_FENCE) {
            result = "电子围栏告警";
        }
        return result;
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
