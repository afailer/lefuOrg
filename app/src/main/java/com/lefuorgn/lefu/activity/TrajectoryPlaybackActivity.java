package com.lefuorgn.lefu.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.lefu.adapter.TrajectoryPlaybackAdapter;
import com.lefuorgn.lefu.bean.ElectronicFence;
import com.lefuorgn.lefu.bean.Trajectory;
import com.lefuorgn.util.DividerItemDecoration;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.TLog;

import java.util.ArrayList;
import java.util.List;

/**
 * 轨迹回放页面
 */

public class TrajectoryPlaybackActivity extends BaseActivity {

    private TextView mCurrentTimeView;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private RecyclerView mRecyclerView;
    private Trajectory mTrajectory; // 当前显示的轨迹点
    private long mCurrentTime; // 当前时间
    private TrajectoryPlaybackAdapter mAdapter;
    private long mElderlyId;

    // Marker图标
    private BitmapDescriptor mBitmap;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_trajectory_playback;
    }

    @Override
    protected void initView() {
        mCurrentTimeView = (TextView) findViewById(R.id.tv_activity_trajectory_playback_date);
        findViewById(R.id.btn_activity_trajectory_playback_date_pre).setOnClickListener(this);
        findViewById(R.id.btn_activity_trajectory_playback_date_back).setOnClickListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_activity_trajectory_playback);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.recycler_view_item_division_color)));
        mMapView = (MapView) findViewById(R.id.map_activity_trajectory_playback);
        mBaiduMap = mMapView.getMap();
        // 设置地图的显示类型
        // 普通类型
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
    }

    @Override
    protected void initData() {
        setToolBarTitle("轨迹回放");
        mElderlyId = getIntent().getLongExtra("id", 0);
        mCurrentTime = System.currentTimeMillis();

        // 初始化marker图标
        mBitmap = BitmapDescriptorFactory.fromResource(R.mipmap.location);
        loadTrajectoryPoints();
    }

    /**
     * 加载某一天轨迹点
     */
    private void loadTrajectoryPoints() {
        mCurrentTimeView.setText(StringUtils.getFormatData(mCurrentTime, "yyyy.MM.dd"));
        long[] time = StringUtils.getSectionTime(mCurrentTime);
        showWaitDialog();
        LefuApi.getTrajectoryPoints(mElderlyId, time[0], time[1], new RequestCallback<ElectronicFence>() {
            @Override
            public void onSuccess(ElectronicFence result) {
                hideWaitDialog();
                if(result != null && result.getList() != null) {
                    if(result.getList().size() > 0) {
                        // 第一条数据默认被选中
                        Trajectory trajectory = result.getList().get(0);
                        trajectory.setSelect(true);
                        mTrajectory = trajectory;
                        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(20));
                        refreshMap(mTrajectory);
                    }
                    setData(result.getList());
                }else {
                    setData(new ArrayList<Trajectory>());
                }
            }

            @Override
            public void onFailure(ApiHttpException e) {
                hideWaitDialog();
                setData(new ArrayList<Trajectory>());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_activity_trajectory_playback_date_pre:
                // 前一天
                mCurrentTime = StringUtils.getTimeBeforeTheCurrentDate(mCurrentTime);
                loadTrajectoryPoints();
                break;
            case R.id.btn_activity_trajectory_playback_date_back:
                // 后一天
                if(StringUtils.isToday(mCurrentTime)) {
                    showToast("已经是最近一天了");
                    return;
                }
                mCurrentTime = StringUtils.getTimeAfterTheCurrentDate(mCurrentTime);
                loadTrajectoryPoints();
                break;
        }
    }

    /**
     * 刷新地图信息展示
     * @param trajectory 轨迹点
     */
    private void refreshMap(Trajectory trajectory) {
        double longitude;
        double latitude;
        try {
            longitude = Double.parseDouble(trajectory.getLongitude());
            latitude = Double.parseDouble(trajectory.getLatitude());
        }catch (Exception e) {
            TLog.error(e.toString());
            return;
        }
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(new LatLng(latitude, longitude));
        // 以动画的形式更新状态
        mBaiduMap.animateMapStatus(u);
        addPoint(latitude, longitude);
    }

    /**
     * 添加Marker节点
     * @param latitude 经度
     * @param longitude 纬度
     */
    private void addPoint(double latitude, double longitude) {
        mBaiduMap.clear();
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
     * 显示数据
     */
    private void setData(List<Trajectory> trajectories) {
        if(mAdapter == null) {
            mAdapter = new TrajectoryPlaybackAdapter(trajectories);
            mAdapter.setEmptyView(getEmptyView());
            mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int i) {
                    List<Trajectory> list = mAdapter.getData();
                    for (int i1 = 0; i1 < list.size(); i1++) {
                        list.get(i1).setSelect(i == i1);
                    }
                    mTrajectory = mAdapter.getItem(i);
                    refreshMap(mTrajectory);
                    mAdapter.notifyDataSetChanged();
                }
            });
            mRecyclerView.setAdapter(mAdapter);
        }else {
            mAdapter.setNewData(trajectories);
        }
    }

    /**
     * 获取空内容指示信息控件
     * @return 控件
     */
    private View getEmptyView() {
        View view = getLayoutInflater().inflate(R.layout.item_recyclerview_empty,
                (ViewGroup) mRecyclerView.getParent(), false);
        ((TextView) view.findViewById(R.id.item_recycler_view_item)).setText("当日无轨迹记录");
        return view;
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
