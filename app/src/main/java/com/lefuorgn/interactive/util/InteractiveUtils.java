package com.lefuorgn.interactive.util;

import com.lefuorgn.AppContext;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.dialog.AlertDialog;
import com.lefuorgn.util.NetworkUtils;
import com.lefuorgn.util.ToastUtils;

/**
 * 同步数据工具类, 用于进行同步规则校验;
 */

public class InteractiveUtils {

    private BaseActivity mActivity;
    private ClickCallBack mClickCallBack;

    public InteractiveUtils(BaseActivity activity) {
        this.mActivity = activity;
    }

    /**
     * 添加同步回调
     * @param clickCallBack 接口
     */
    public void setClickCallBack(ClickCallBack clickCallBack) {
        this.mClickCallBack = clickCallBack;
    }

    public void start() {
        if(AppContext.getInstance().getNetState() == NetworkUtils.NETWORK_WIFI) {
            // WiFi情况下直接同步
            if(mClickCallBack != null) {
                mClickCallBack.sync();
            }
        }else if(AppContext.getInstance().getNetState() == NetworkUtils.NETWORK_MOBILE) {
            // 移动数据
            if(AppContext.isNetFlowAllowed()) {
                // 用户允许使用流量进行同步同步
                if(mClickCallBack != null) {
                    mClickCallBack.sync();
                }
            }else {
                // 提示用户要使用数据流量进行数据同步
                new AlertDialog().setContent("使用运营商网络,是否进行同步")
                        .setCancelBtnText("是")
                        .setConfirmBtnText("否")
                        .setClickCallBack(new AlertDialog.ClickCallBack() {
                            @Override
                            public void cancel() {
                                if(mClickCallBack != null) {
                                    // 用户同意使用流量进行数据同步
                                    AppContext.setNetFlowAllowed(true);
                                    mClickCallBack.sync();
                                }
                            }

                            @Override
                            public void confirm() {
                                // 用户不同意; 不进行任何操作
                            }
                        }).show(mActivity.getSupportFragmentManager(), "AlertDialog");
            }
        }else {
            ToastUtils.show(mActivity, "网络不可用");
        }
    }

    /**
     * 点击事件触发接口回调函数
     */
    public interface ClickCallBack {

        /**
         * 跳转到外部设备采集事件触发
         */
        void sync();

    }

}
