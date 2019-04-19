package com.lefuorgn.lefu.activity;

import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.lefu.adapter.ElectronicFenceAdapter;
import com.lefuorgn.lefu.bean.ElectronicFence;
import com.lefuorgn.lefu.bean.ElectronicFenceItem;
import com.lefuorgn.util.DividerItemDecoration;
import com.lefuorgn.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 电子围栏页面
 */

public class ElectronicFenceActivity extends BaseActivity {

    private long mDeviceId;

    private BaiduMap mBaiduMap;
    private MapView mMapView;
    private View mShowView; // pop窗体依赖的控件

    private List<ElectronicFenceItem> mElectronicFenceItems;
    private PopupWindow mPopupWindow;
    private View mPopView;
    private int mPopHeight;
    private int mPopWidth;
    private int mStatisticsNum; // 当前网络请求执行完成的条数

    @Override
    protected int getLayoutId() {
        return R.layout.activity_electronic_fence;
    }

    @Override
    protected void initView() {
        mMapView = (MapView) findViewById(R.id.map_activity_electronic_fence);
        mShowView = findViewById(R.id.pop_activity_electronic_fence);
        mBaiduMap = mMapView.getMap();
        // 设置地图的显示类型
        // 普通类型
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
    }

    @Override
    protected void initData() {
        setToolBarTitle("电子围栏");
        setMenuTextView("切换");
        long id = getIntent().getLongExtra("id", 0);
        mDeviceId = getIntent().getLongExtra("deviceId", 0);
        mStatisticsNum = 0;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mPopWidth = metrics.widthPixels;
        mPopHeight = metrics.heightPixels / 2;
        showWaitDialog();
        // 获取当前设备绑定的安全区域
        LefuApi.getElectronicFence(id, new RequestCallback<ElectronicFence>() {
            @Override
            public void onSuccess(ElectronicFence result) {
                statistics();
                if(result == null) {
                    showToast("无电子围栏");
                    return;
                }
                if(StringUtils.isEmpty(result.getCoordinate())) {
                    showToast("无电子围栏");
                    return;
                }
                addOverlay(result.getCoordinate());
            }

            @Override
            public void onFailure(ApiHttpException e) {
                showToast(e.getMessage());
                statistics();
            }
        });
        // 获取当前机构下所有的安全区域
        LefuApi.getElectronicFences(0, new RequestCallback<List<ElectronicFenceItem>>() {
            @Override
            public void onSuccess(List<ElectronicFenceItem> result) {
                mElectronicFenceItems = result;
                ElectronicFenceItem item = new ElectronicFenceItem();
                item.setId(0);
                item.setName("关闭围栏");
                mElectronicFenceItems.add(0, item);
                statistics();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                mElectronicFenceItems = new ArrayList<ElectronicFenceItem>();
                ElectronicFenceItem item = new ElectronicFenceItem();
                item.setId(0);
                item.setName("关闭围栏");
                mElectronicFenceItems.add(item);
                statistics();
            }
        });
    }

    /**
     * 统计当前网络请求的个数
     */
    private synchronized void statistics() {
        mStatisticsNum++;
        if(mStatisticsNum == 2) {
            hideWaitDialog();
            initPopupWindowView();
        }
    }

    @Override
    protected void onMenuClick(View v) {
        showPopupWindow(mPopView, mShowView);
    }

    /**
     * 绘制覆盖物
     * @param coordinateStr 坐标
     */
    private void addOverlay(String coordinateStr) {
        mBaiduMap.clear();
        List<LatLng> pts = StringUtils.getLatLngList(coordinateStr);
        // 移动到安全区域
        if(pts.size() > 0) {
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(13));
            LatLng latLng = StringUtils.getCenterLatLng(pts);
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(new LatLng(latLng.latitude, latLng.longitude));
            // 以动画的形式更新状态
            mBaiduMap.animateMapStatus(u);
            //构建用户绘制多边形的Option对象
            OverlayOptions polygonOption = new PolygonOptions()
                    .points(pts)
                    .stroke(new Stroke(5, 0xAA00FF00))
                    .fillColor(0xAA147BE7);
            mBaiduMap.addOverlay(polygonOption);
        }else {
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(12));
        }
    }

    /**
     * 初始化PopupWindow
     */
    @SuppressLint("InflateParams")
    private void initPopupWindowView() {
        mPopView = getLayoutInflater().inflate(R.layout.popup_window_electronic_fence, null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(mPopWidth, mPopHeight);
        mPopView.setLayoutParams(params);
        RecyclerView rv = (RecyclerView) mPopView.findViewById(R.id.rv_popup_window_electronic);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.recycler_view_item_division_color)));
        final ElectronicFenceAdapter adapter = new ElectronicFenceAdapter(mElectronicFenceItems);
        adapter.setEmptyView(getEmptyView(rv));
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                if(mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
                ElectronicFenceItem item = adapter.getItem(i);
                updateElectronicFence(item.getId(), item.getCoordinate());
            }
        });
        rv.setAdapter(adapter);
    }

    /**
     * 获取空内容指示信息控件
     * @return 控件
     */
    private View getEmptyView(RecyclerView recyclerView) {
        View view = getLayoutInflater().inflate(R.layout.item_recyclerview_empty,
                (ViewGroup) recyclerView.getParent(), false);
        ((TextView) view.findViewById(R.id.item_recycler_view_item)).setText("无电子围栏信息");
        return view;
    }

    /**
     * 修改安全区域
     */
    private void updateElectronicFence(final long areaId, final String coordinateStr) {
        showLoadingDialog();
        LefuApi.updateElectronicFence(mDeviceId, areaId, new RequestCallback<String>() {
            @Override
            public void onSuccess(String result) {
                showToast(areaId == 0 ? "围栏关闭成功" : "修改成功");
                addOverlay(coordinateStr);
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
     * 显示安全区域popup页面
     */
    private void showPopupWindow(View popView, View view) {
        if(mPopupWindow == null) {
            mPopupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setAnimationStyle(R.style.anim_image_folder_style);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.showAsDropDown(view, 0, -mPopHeight);
        }else {
            mPopupWindow.showAsDropDown(view, 0, -mPopHeight);
        }
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
