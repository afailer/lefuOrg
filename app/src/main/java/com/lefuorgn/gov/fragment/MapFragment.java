package com.lefuorgn.gov.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.lefuorgn.R;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.GovApi;
import com.lefuorgn.api.remote.ImageLoader;
import com.lefuorgn.api.remote.common.RemoteUtil;
import com.lefuorgn.base.BaseFragment;
import com.lefuorgn.dialog.WaitDialog;
import com.lefuorgn.gov.GovMainActivity;
import com.lefuorgn.gov.bean.Organization;
import com.lefuorgn.lefu.MainActivity;
import com.lefuorgn.util.TLog;

import java.util.ArrayList;
import java.util.List;

/**
 * 政府页面地图页
 */

public class MapFragment extends BaseFragment{

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private MyLocationListener mMyLocationListener;
    // 是否是第一次定位
    private boolean isFirstLoc = true;
    // 周边机构集合
    private List<Organization> mOrganization;
    private String mAgencyIds;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_map;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mMapView = (MapView) view.findViewById(R.id.map_around_citys);
        mBaiduMap = mMapView.getMap();
        // 设置地图的显示类型
        // 普通类型
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);

        mLocationClient = new LocationClient(getActivity());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        initLocation();
    }

    @Override
    protected void initData() {
        // 存放机构集合
        mAgencyIds = ((GovMainActivity) getActivity()).getAgencyIds();
        initListener();
    }

    /**
     * 初始化监听方法
     */
    private void initListener() {
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng latLng = marker.getPosition();
                for (final Organization o : mOrganization) {
                    if(o.getLatitude() == latLng.latitude && o.getLongitude() == latLng.longitude) {
                        View rootView = View.inflate(getActivity(), R.layout.fragment_map_info_window, null);
                        ImageView img = (ImageView) rootView.findViewById(R.id.img_info_window_map_fragment);
                        ImageLoader.loadImg(RemoteUtil.getAbsoluteApiUrl(o.getExterior_pic()),img);
                        TextView name = (TextView) rootView.findViewById(R.id.name_info_window_map_fragment);
                        TextView address = (TextView) rootView.findViewById(R.id.address_info_window_map_fragment);
                        name.setText(o.getAgency_name());
                        address.setText(o.getAddress());
                        rootView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                skipMainActivity(o.getAgency_id(), o.getAgency_name());
                                mBaiduMap.hideInfoWindow();
                            }
                        });
                        InfoWindow infoWindow = new InfoWindow(rootView, latLng, -70);
                        mBaiduMap.showInfoWindow(infoWindow);
                        break;
                    }
                }
                return false;
            }
        });

        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }
            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                loadAroundCities(mapStatus);
            }
        });
        mBaiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                mBaiduMap.hideInfoWindow();
            }
        });
    }


    private void skipMainActivity(final long id, final String name) {
        final WaitDialog waitDialog = new WaitDialog();
        waitDialog.show(getFragmentManager(), "");
        GovApi.isHasSkipPermission(id, new RequestCallback<String>() {
            @Override
            public void onSuccess(String result) {
                waitDialog.dismiss();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra(MainActivity.FIRST_LEVEL_PAGE, false);
                intent.putExtra(MainActivity.INTENT_AGENCY_ID, id);
                intent.putExtra(MainActivity.INTENT_AGENCY_NAME, name);
                startActivity(intent);
            }

            @Override
            public void onFailure(ApiHttpException e) {
                showToast(e.getMessage());
                waitDialog.dismiss();
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
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            TLog.log("定位成功后成功后,进行中心位置定位");
            if (isFirstLoc) {
                isFirstLoc = false;
                BitmapDescriptor resource = BitmapDescriptorFactory.fromResource(R.mipmap.location_me);
                MyLocationConfiguration configuration = new MyLocationConfiguration(
                        MyLocationConfiguration.LocationMode.NORMAL, false, resource);
                mBaiduMap.setMyLocationConfigeration(configuration);
                MapStatus status=mBaiduMap.getMapStatus();
                loadAroundCities(status);
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    /**
     * 加载周边的城市
     * @param status 当前地图的状态
     */
    private void loadAroundCities(MapStatus status){
        TLog.error(mAgencyIds);
        GovApi.loadAroundCities(status, mAgencyIds, new RequestCallback<List<Organization>>() {
            @Override
            public void onSuccess(List<Organization> result) {
                mOrganization = result;
                if(mOrganization == null) {
                    mOrganization = new ArrayList<Organization>();
                }
                markerPoint();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                mOrganization = new ArrayList<Organization>();
            }
        });
    }

    private void markerPoint() {
        mBaiduMap.clear();
        List<OverlayOptions> options = new ArrayList<OverlayOptions>();
        for (Organization org : mOrganization) {
            // 定义Maker坐标点
            LatLng point = new LatLng(org.getLatitude(), org.getLongitude());
            // 构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.mipmap.icon_marka);
            // 构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .zIndex(9)
                    .icon(bitmap);
            options.add(option);
        }
        // 在地图上添加Marker，并显示
        mBaiduMap.addOverlays(options);
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
        mLocationClient.stop();
        mLocationClient.unRegisterLocationListener(mMyLocationListener);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();

    }

}
