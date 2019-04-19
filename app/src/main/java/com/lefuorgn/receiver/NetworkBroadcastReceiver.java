package com.lefuorgn.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.lefuorgn.AppContext;
import com.lefuorgn.util.NetworkUtils;
import com.lefuorgn.util.TLog;

/**
 * 自定义检查手机网络状态是否切换的广播接受器
 */

public class NetworkBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 如果相等的话就说明网络状态发生了变化
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int state = NetworkUtils.getNetWorkState(context);
            // 接口回调传过去状态的类型
            AppContext.getInstance().setNetState(state);
            TLog.error("网络状态 == " + state);
        }
    }
}
