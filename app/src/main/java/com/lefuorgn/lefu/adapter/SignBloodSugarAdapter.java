package com.lefuorgn.lefu.adapter;

import android.bluetooth.BluetoothDevice;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lefuorgn.R;

import java.util.List;

/**
 * 可以连接蓝牙设备对象数据适配器
 */

public class SignBloodSugarAdapter extends BaseQuickAdapter<BluetoothDevice> {

    public SignBloodSugarAdapter(List<BluetoothDevice> data) {
        super(R.layout.item_activity_sign_blood_sugar, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, BluetoothDevice device) {
        holder.setText(R.id.tv_item_activity_sign_blood_sugar, device.getName());
    }
}
