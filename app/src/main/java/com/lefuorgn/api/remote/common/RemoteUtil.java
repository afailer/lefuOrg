package com.lefuorgn.api.remote.common;

import android.os.Build;

import com.lefuorgn.AppContext;

public class RemoteUtil {

	public static final String IMG_URL = "http://www.lefukj.cn";
    private static final String API_URL = "http://www.lefukj.cn/%s";

    /**
	 * 获取绝对路径
	 * @param partUrl 如:lefuyun/userInfoCtr/queryUserInfo
	 * @return 完整的请求路径
	 */
	public static String getAbsoluteApiUrl(String partUrl) {
        return String.format(API_URL, partUrl);
    }

	/**
	 * 获取登录路径
	 * @return 登录路径url
     */
	public static String getLoginUrl() {
		return getAbsoluteApiUrl("lefuyun/userInfoCtr/queryUserInfo");
	}

	public static String getUserAgent() {
		return "lefuAppO (" + Build.MODEL + " ; Android " + Build.VERSION.RELEASE + ")";
	}

    /**
     * 获取当前指定的版本号
     */
	public static String getVersion() {
		return "160427";
	}

	/**
	 * 获取当前设备, 设备号码
     */
	public static String getDeviceIMEI() {
		return AppContext.getInstance().getDeviceIMEI();
	}

}
