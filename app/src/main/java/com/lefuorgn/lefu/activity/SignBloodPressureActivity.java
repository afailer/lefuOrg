package com.lefuorgn.lefu.activity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.lefubp.api.TKBloodPressure;
import com.lefubp.bean.BloodPressureBean;
import com.lefubp.bean.BloodPressureReceiveBean;
import com.lefubp.bean.MeasureFailReceiveBean;
import com.lefubp.bean.MeasurePressureReceiveBean;
import com.lefubp.bean.PowerOffReceiveBean;
import com.lefubp.bean.PowerResponseBean;
import com.lefubp.bean.ResultObj;
import com.lefubp.utils.ResponseMsg;
import com.lefubp.utils.ResponseUtil;
import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.bean.User;
import com.lefuorgn.db.util.OldPeopleManager;
import com.lefuorgn.dialog.AlertDialog;
import com.lefuorgn.lefu.bean.SignInfo;

public class SignBloodPressureActivity extends BaseActivity {

    // 蓝牙适配器
    private BluetoothAdapter mBluetoothAdapter;
    // 设备连接工作类
    private TKBloodPressure mTKBloodPressure;
    // 测量结果的显示控件: 舒张压显示控件、收缩压显示控件、心率显示控件、设备电量显示控件
    private TextView mDiastolicView, mSystolicView, mHeartRateView, mBatteryView;
    // 数据保存按钮
    private TextView mSaveBtn;
    // 链接设备按钮和开始测量按钮
    private TextView mConnectingBtn, mStartBtn;
    private boolean linkState; // 当前蓝牙与外部设备的链接状态

    private boolean showMsgDialog; // 是否显示showMsgDialog
    private AlertDialog mAlertDialog;

    private User mUser; // 当前用户信息
    private long mAgencyId; // 当前机构ID
    private SignInfo mSignInfo; // 当前条目信息
    private boolean saveState; // 记录当前操作是否有添加
    private SaveTask mSaveTask;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign_blood_pressure;
    }

    @Override
    protected void initView() {
        mDiastolicView = (TextView) findViewById(R.id.tv_activity_sign_blood_pressure_diastolic);
        mSystolicView = (TextView) findViewById(R.id.tv_activity_sign_blood_pressure_systolic);
        mHeartRateView = (TextView) findViewById(R.id.tv_activity_sign_blood_pressure_heart_rate);
        mBatteryView = (TextView) findViewById(R.id.tv_activity_sign_blood_pressure_battery);
        mConnectingBtn = (TextView) findViewById(R.id.btn_activity_sign_blood_pressure_connecting_device);
        mConnectingBtn.setOnClickListener(this);
        mStartBtn = (TextView) findViewById(R.id.btn_activity_sign_blood_pressure_begin);
        mStartBtn.setOnClickListener(this);
        mSaveBtn = (TextView) findViewById(R.id.btn_activity_sign_blood_pressure_save);
        mSaveBtn.setOnClickListener(this);
        findViewById(R.id.btn_activity_sign_blood_pressure_cancel).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        setToolBarTitle("血压设备采集");
        mUser = AppContext.getInstance().getUser();
        mAgencyId = AppContext.getInstance().getAgencyId();
        mSignInfo = (SignInfo) getIntent().getSerializableExtra("SignInfo");
        // 获取蓝牙适配器
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            showToast("该设备不支持蓝牙模块");
            return;
        }
        try {
            // 获取外部设备操作类对象
            mTKBloodPressure = TKBloodPressure.getInstance(getApplicationContext(), "1.0");
            // 添加handler对外部设备进行监听
            ResponseUtil.setHandler(mResultHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mTKBloodPressure.isEnabled().getCode() != ResponseMsg.BLUETOOTH_OPEN_CODE) {
            // 只返回俩个状态码(103: 蓝牙开启; -103: 蓝牙未开启)
            int code = mTKBloodPressure.enabled().getCode();
            // (101: 蓝牙开启成功; -101: 蓝牙未开启失败)
            if(code == ResponseMsg.OPEN_SUCCESS_CODE) {
                showToast(ResponseMsg.OPEN_SUCCESS_MSG);
            }else {
                showToast("请先打开蓝牙");
            }
        }
        // 判断是否已经和设备配连接
        int code = mTKBloodPressure.isConnnect().getCode();
        // (108: 蓝牙已配对; 105: 蓝牙未配对)
        if(code == ResponseMsg.BOND_PAIRED_CODE) {
            // 当前设备与蓝牙链接
            setBtnState(true);
        }else {
            // 当前设备与蓝牙未链接
            setBtnState(false);
        }
    }

    /**
     * 处理连接蓝牙设备的handler
     */
    @SuppressLint("HandlerLeak")
    private Handler mResultHandler = new Handler() {
        @SuppressLint("SetTextI18n")
        public void handleMessage(android.os.Message msg) {
            if((msg.obj) instanceof PowerOffReceiveBean){
                // 设备关闭通知
                PowerOffReceiveBean powerOffReceiveBean = (PowerOffReceiveBean) msg.obj;
                if(powerOffReceiveBean.getPowerState() == 1){
                    setBtnState(false);
                }
                return;
            }
            if ((msg.obj) instanceof ResultObj) {
                // 当前设备的状态
                // 状态码
                int code = ((ResultObj) msg.obj).getCode();
                if (code == ResponseMsg.CONNECT_SUCCESS_CODE) {
                    // 设备连接成功
                    setBtnState(true);
                }else if(code == ResponseMsg.BLUETOOTH_DISCONNECT_CODE) {
                    // 蓝牙断开
                    setBtnState(false);
                    showMsgDialog("设备未配对,请重新扫描");
                }else if (code == ResponseMsg.CANCEL_CONNECT_SUCCESS_CODE) {
                    // 取消设备连接成功
                    setBtnState(false);
                }else if (code == ResponseMsg.CANCEL_PAIR_CODE) {
                    // 手动取消配对或当获取不到设备name时
                    setBtnState(false);
                    showMsgDialog("请检查设备是否开启且重新扫描设备");
                }else if(code == ResponseMsg.CONNECT_ERROR_CODE){
                    // 设备连接失败（没开机或其他）
                    setBtnState(false);
                    showMsgDialog("请检查设备是否开启");
                }else if(code == ResponseMsg.BLUETOOTH_OFF_CODE){
                    // 蓝牙关闭（本地蓝牙关闭）
                    showToast("蓝牙关闭");
                }else if(code == ResponseMsg.BLUETOOTH_OPEN_CODE){
                    // 本地蓝牙开启成功
                    setBtnState(false);
                }
                return;
            }
            if ((msg.obj) instanceof PowerResponseBean) {
                // 如果为电量
                PowerResponseBean power = (PowerResponseBean) msg.obj;
                mBatteryView.setText(power.getPercent() + "%");
                return;
            }
            // 测量中的时候
            if ((msg.obj) instanceof MeasurePressureReceiveBean) {
                // 汞柱的值
//                MeasurePressureReceiveBean measurePressureReceiveBean = (MeasurePressureReceiveBean) msg.obj;
                return;
            }
            if ((msg.obj) instanceof BloodPressureReceiveBean) {
                // 得到的包含血压与心率的实体类
                BloodPressureReceiveBean receiveBean = (BloodPressureReceiveBean) msg.obj;
                BloodPressureBean bloodPressure = receiveBean.getBloodPressureBeans().get(0);
                if(bloodPressure != null) {
                    mDiastolicView.setText(bloodPressure.getDiastolic()+ "");
                    mSystolicView.setText(bloodPressure.getSystolic()+ "");
                    mHeartRateView.setText(bloodPressure.getPulseRate()+ "");
                    mSaveBtn.setBackgroundColor(getResources().getColor(R.color.pinkred));
                }else {
                    showToast("测量失败");
                }
                return;
            }
            if ((msg.obj) instanceof MeasureFailReceiveBean) {
                // 如果没有戴在胳膊上测量时
                MeasureFailReceiveBean failReceiveBean = (MeasureFailReceiveBean) msg.obj;
                if (failReceiveBean.getFailNumber() == 255) {
                    // 255代表手动测试失败，0-254为测量计划中的第几次失败
                    showMsgDialog("请把设备戴在胳膊上测量");
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_activity_sign_blood_pressure_connecting_device:
                // 外部设备的链接和断开
                connectingDevice();
                break;
            case R.id.btn_activity_sign_blood_pressure_begin:
                // 开始测量
                startMeasurement();
                break;
            case R.id.btn_activity_sign_blood_pressure_save:
               // 保存
                  saveData();
                break;
            case R.id.btn_activity_sign_blood_pressure_cancel:
                // 取消
                finish();
                break;
        }
    }

    /**
     * 链接的断开的链接
     */
    private void connectingDevice() {
        if (mBluetoothAdapter == null) {
            showToast("该设备不支持蓝牙模块");
            return;
        }
        if (mTKBloodPressure.isEnabled().getCode() != ResponseMsg.BLUETOOTH_OPEN_CODE) {
            // 只返回俩个状态码(103: 蓝牙开启; -103: 蓝牙未开启)
            int code = mTKBloodPressure.enabled().getCode();
            // (101: 蓝牙开启成功; -101: 蓝牙未开启失败)
            if(code == ResponseMsg.OPEN_SUCCESS_CODE) {
                showToast(ResponseMsg.OPEN_SUCCESS_MSG);
            }else {
                showToast("请先打开蓝牙");
                return;
            }
        }
        if (linkState) {
            // 如果已经连接了, 则断开链接
            ResultObj resultObj = mTKBloodPressure.cancelConnect();
            // (202: 取消设备连接成功; -202: 取消设备连接失败)
            if (resultObj.getCode() == ResponseMsg.CANCEL_CONNECT_SUCCESS_CODE) {
                // 取消连接成功
                setBtnState(false);
            }
            showToast(resultObj.getMsg());
        } else {
            // 未链接则跳转到二维码扫描界面
            Intent intent = new Intent();
            intent.setClass(this, MipcaActivityCapture.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent, 100);
        }
    }

    /**
     * 开始测量
     */
    private void startMeasurement() {
        clearMeasureData();
        if (linkState) {
            // 开始测量
            ResultObj result_start = mTKBloodPressure.manualMeasure();
            // (302: Socket通信未创建; 304: Socket通信已断开; 303: Socket通信失败; 305: 发送成功)
            if (result_start.getCode() != ResponseMsg.SEND_SUCCESS_CODE) {
                // 开始失败
                showMsgDialog("测量失败,检查设备蓝牙是否开启");
            }
        } else {// 如果没连接
            showMsgDialog("设备未连接,请先扫描连接");
        }
    }

    /**
     * 保存血压和心率
     */
    private void saveData() {
        if(!isSave()) {
            // 不能保存
            showToast("请进行数据的录入");
            return;
        }
        // 获取舒张压(低血压)
        String diastolic = mDiastolicView.getText().toString();
        // 获取收缩压(高血压)
        String systolic = mSystolicView.getText().toString();
        // 获取心率
        String heartRate = mHeartRateView.getText().toString();
        mSaveTask = (SaveTask) new SaveTask().execute(diastolic, systolic, heartRate);
    }

    /**
     * 本地获取数据请求任务类
     */
    private class SaveTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            showLoadingDialog();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            // 获取舒张压(低血压)
            String diastolic = params[0];
            // 获取收缩压(高血压)
            String systolic = params[1];
            // 获取心率
            String heartRate = params[2];
            return OldPeopleManager.saveSignItemInfoByDevice(mUser, mAgencyId, mSignInfo.getOldPeopleId()
                    , mSignInfo.getOldPeopleName(), systolic + "/" + diastolic, heartRate);
        }

        @Override
        protected void onPostExecute(Boolean data) {
            if(data) {
                showToast("保存成功");
            }else {
                showToast("保存失败");
            }
            saveState = true;
            clearMeasureData();
            hideLoadingDialog();
        }
    }

    /**
     * 判断当前是否可以进行内容提交
     */
    public boolean isSave() {
        String diastolic = mDiastolicView.getText().toString();
        String systolic = mSystolicView.getText().toString();
        String heartRate = mHeartRateView.getText().toString();
        return !("".equals(diastolic) || "".equals(systolic) || "".equals(heartRate));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            // 获得二维码扫描结果
            String result = bundle.getString("result");
            ResultObj connect = mTKBloodPressure.connect(result);
            // (301: 蓝牙MAC地址无效; 306: 调用成功)
            if (connect.getCode() == ResponseMsg.EXECUTE_SUCCESS_CODE) {
                // 链接成功
                setBtnState(true);
            }
            showToast(connect.getMsg());
        }
    }

    /**
     * 清空测量数据
     */
    private void clearMeasureData() {
        mDiastolicView.setText("");
        mSystolicView.setText("");
        mHeartRateView.setText("");
        mSaveBtn.setBackgroundColor(getResources().getColor(R.color.gray));
    }

    /**
     * 设置链接设备和开始测量按钮的状态
     * @param link 当前蓝牙与外部设备的链接状态
     */
    private void setBtnState(boolean link) {
        linkState = link;
        if(link) {
            mConnectingBtn.setText("断开连接");
            mStartBtn.setBackgroundColor(getResources().getColor(R.color.pinkred));
        }else {
            mConnectingBtn.setText("连接设备");
            mStartBtn.setBackgroundColor(getResources().getColor(R.color.gray));
        }
    }

    /**
     * 显示提示内容
     * @param message 要提示的内容
     */
    private void showMsgDialog(String message){
        if(!showMsgDialog) {
            return;
        }
        if(mAlertDialog == null) {
            mAlertDialog = new AlertDialog();
            mAlertDialog.isShowCancelBtn(false);
        }
        mAlertDialog.setContent(message);
        mAlertDialog.show(getSupportFragmentManager(), "AlertDialog");
    }

    @Override
    protected void onResume() {
        super.onResume();
        showMsgDialog = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        showMsgDialog = false;

    }

    @Override
    public void finish() {
        if(saveState) {
            setResult(200);
        }
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSaveTask != null) {
            mSaveTask.cancel(true);
            mSaveTask = null;
        }
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected boolean hasToolBar() {
        return true;
    }

}