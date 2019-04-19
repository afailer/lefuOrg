package com.lefuorgn.lefu.activity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lefuorgn.R;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.lefu.adapter.SignBloodSugarAdapter;
import com.lefuorgn.lefu.bean.SignInfo;
import com.lefuorgn.util.DividerItemDecoration;
import com.lefuorgn.util.TLog;

import java.util.ArrayList;
import java.util.List;

public class SignBloodSugarActivity extends BaseActivity {

    /**
     * 设备操作指示页面
     */
    private static final int STATE_INITIALIZATION = 1;
    /**
     * 设备搜索失败指示页面
     */
    private static final int STATE_SEARCH_FAILED = 2;
    /**
     * 设备搜索成功
     */
    private static final int STATE_SEARCH_SUCCESS = 3;

    private LinearLayout mBootPage; // 引导页面
    private LinearLayout mDeviceOperationView; // 设备操作引导页
    private LinearLayout mSearchErrorView;  // 设备搜索失败指示页面
    private RecyclerView mSearchSuccessView; // 设备搜索成功显示页面

    // 本设备的蓝牙适配器
    private BluetoothAdapter mBluetoothAdapter;
    // 蓝牙设备对象集合
    private List<BluetoothDevice> mBluetoothDeviceList;
    // 蓝牙设备对象适配器
    private SignBloodSugarAdapter mSignBloodSugarAdapter;
    private BluetoothDevice mSelectDevice;
    private SignInfo mSignInfo; // 当前条目信息

    private BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
                // 蓝牙扫描过程开始
                showLoadingDialog("搜索中...");
                if(mBluetoothDeviceList == null) {
                    // 初始化蓝牙设备对象集合
                    mBluetoothDeviceList = new ArrayList<BluetoothDevice>();
                }else {
                    // 清空蓝牙设备对象集合
                    mBluetoothDeviceList.clear();
                }
            }else if(intent.getAction().equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                // 蓝牙扫描过程结束
                hideLoadingDialog();
                if(mBluetoothDeviceList.size() == 0) {
                    pageSwitching(STATE_SEARCH_FAILED);
                    showToast("没有搜索到任何血糖设备");
                    return;
                }
                pageSwitching(STATE_SEARCH_SUCCESS);
                if(mSignBloodSugarAdapter == null) {
                    mSignBloodSugarAdapter = new SignBloodSugarAdapter(mBluetoothDeviceList);
                    @SuppressLint("InflateParams")
                    View view = getLayoutInflater().inflate(R.layout.item_sign_blood_sugar_footer, null);
                    mSignBloodSugarAdapter.addFooterView(view);
                    // 添加条目点击事件
                    addAdapterListener();
                    mSearchSuccessView.setAdapter(mSignBloodSugarAdapter);
                }else {
                    mSignBloodSugarAdapter.setNewData(mBluetoothDeviceList);
                }
            }else if(intent.getAction().equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                // 远程蓝牙设备状态改变的时候发出这个广播
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getName().equalsIgnoreCase(mSelectDevice.getName())) {
                    switch (device.getBondState()) {
                        case BluetoothDevice.BOND_NONE:
                            TLog.error("配对取消...");
                            break;
                        case BluetoothDevice.BOND_BONDING:
                            TLog.error("正在配对中...");
                            break;
                        case BluetoothDevice.BOND_BONDED:
                            // 绑定成功
                            TLog.error("配对成功...");
                            binderBluetooth(device.getAddress(), device.getName());
                            break;
                    }
                }
            }else if(intent.getAction().equals(BluetoothDevice.ACTION_FOUND)) {
                // 蓝牙扫描时，扫描到任一远程蓝牙设备时，会发送此广播
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                TLog.error("device == " + device.toString());
                TLog.error("name == " + device.getName());
                if(checkDevice(device) && !mBluetoothDeviceList.contains(device)) {
                    mBluetoothDeviceList.add(device);
                }
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign_blood_sugar;
    }

    @Override
    protected void initView() {
        findViewById(R.id.btn_activity_sign_blood_sugar_search).setOnClickListener(this);
        mBootPage = (LinearLayout) findViewById(R.id.ll_activity_sign_blood_sugar_boot_page);
        mDeviceOperationView = (LinearLayout) findViewById(R.id.ll_activity_sign_blood_sugar_device_operation);
        mSearchErrorView = (LinearLayout) findViewById(R.id.ll_activity_sign_blood_sugar_search_error);
        mSearchSuccessView = (RecyclerView) findViewById(R.id.rv_activity_sign_blood_sugar_search_success);
        mSearchSuccessView.setLayoutManager(new LinearLayoutManager(this));
        mSearchSuccessView.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.recycler_view_item_division_color)));
    }

    /**
     * 根据状态显示指定的页面
     * @param state 状态码
     */
    private void pageSwitching(int state) {
        if(state == STATE_INITIALIZATION) {
            // 切换到初始化页面
            mBootPage.setVisibility(View.VISIBLE);
            mSearchSuccessView.setVisibility(View.GONE);
            mDeviceOperationView.setVisibility(View.VISIBLE);
            mSearchErrorView.setVisibility(View.GONE);
        }else if(state == STATE_SEARCH_FAILED) {
            // 设备搜索失败提示页面
            mBootPage.setVisibility(View.VISIBLE);
            mSearchSuccessView.setVisibility(View.GONE);
            mDeviceOperationView.setVisibility(View.GONE);
            mSearchErrorView.setVisibility(View.VISIBLE);
        }else if(state == STATE_SEARCH_SUCCESS) {
            // 设备搜索成功,设备号显示页面
            mBootPage.setVisibility(View.GONE);
            mSearchSuccessView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData() {
        setToolBarTitle("血糖设备采集");
        mSignInfo = (SignInfo) getIntent().getSerializableExtra("SignInfo");
        // 初始化蓝牙模块对象
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null) {
            showToast("该设备不支持蓝牙模块");
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }
        // 注册广播
        registerBluetoothReceiver();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_activity_sign_blood_sugar_search:
                // 设备搜索
                searchDevice();
                break;
        }
    }

    /**
     * 搜索设备
     */
    private void searchDevice() {
        if(mBluetoothAdapter == null) {
            showToast("该设备不支持蓝牙模块");
            return;
        }
        if(!mBluetoothAdapter.isEnabled()) {
            // 打开蓝牙
            boolean enable = mBluetoothAdapter.enable();
            if(!enable) {
                showToast("请先打开蓝牙");
                return;
            }
        }
        // 扫描蓝牙设备
        mBluetoothAdapter.startDiscovery();
    }

    /**
     * 校验当前蓝牙设备
     * @param device 当前待校验的设备
     * @return true: 当前需要的设备; false: 当前不是需要的设备
     */
    private boolean checkDevice(BluetoothDevice device) {
        String name = device.getName();
        if(name == null || !name.startsWith("TC")) {
            return false;
        }
        String address = device.getAddress();
        if(address.contains(":")) {
            address = address.replace(":", "");
        }
        try {
            long result = Long.parseLong(address, 16);
            long max = Long.parseLong("70F1A1AB5C98", 16);
            long min = Long.parseLong("70F1A19C1A58", 16);
            return result <= max && result >= min;
        }catch (Exception e) {
            return false;
        }
    }

    @SuppressLint("NewApi")
    private void addAdapterListener() {
        mSignBloodSugarAdapter.setOnRecyclerViewItemClickListener(
                new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                mSelectDevice = mSignBloodSugarAdapter.getItem(i);
                // 取消搜索
                mBluetoothAdapter.cancelDiscovery();
                if(BluetoothDevice.BOND_NONE == mSelectDevice.getBondState()) {
                    // 未配对, 输入pin值
                    // 尝试配对
                    if(!mSelectDevice.createBond()) {
                        //
                        showToast("配对失败...");
                        return;
                    }
                    mSelectDevice.setPin("9527".getBytes());
                }else {
                    // 已经绑定
                    binderBluetooth(mSelectDevice.getAddress(), mSelectDevice.getName());
                }
            }
        });
    }

    /**
     * 启动窗体页面
     * @param address 蓝牙设备地址
     * @param name 蓝牙设备名称
     */
    private void binderBluetooth(String address, String name) {
        Intent intent = new Intent(this, SignBloodSugarAcquisitionActivity.class);
        intent.putExtra("address", address);
        intent.putExtra("name", name);
        intent.putExtra("SignInfo", mSignInfo);
        startActivity(intent);
    }

    /**
     * 注册广播
     */
    private void registerBluetoothReceiver() {
        IntentFilter filter = new IntentFilter();
        // 蓝牙扫描过程开始
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        // 蓝牙扫描过程结束
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        // 远程蓝牙设备状态改变的时候发出这个广播
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        // 蓝牙扫描时，扫描到任一远程蓝牙设备时，会发送此广播
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mBluetoothReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBluetoothReceiver);
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
