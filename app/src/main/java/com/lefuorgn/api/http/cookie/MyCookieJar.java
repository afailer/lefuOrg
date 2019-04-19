package com.lefuorgn.api.http.cookie;

import com.lefuorgn.api.remote.common.RemoteUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Cookie缓存策略类
 */
public class MyCookieJar implements CookieJar {

	/**
	 * 缓存cookie
	 */
	private final HashMap<String, List<Cookie>> allCookies = new HashMap<String, List<Cookie>>();
	/**
	 * 登录HttpUrl
	 */
	private final HttpUrl mHttpUrl = HttpUrl.parse(RemoteUtil.getLoginUrl());

	@Override
	public List<Cookie> loadForRequest(HttpUrl httpUrl) {
		List<Cookie> cookies = allCookies.get(httpUrl.host());
		if(mHttpUrl.uri().toString().equals(httpUrl.uri().toString())) {
			cookies = null;
			allCookies.clear();
		}
		if (cookies == null) {
			cookies = new ArrayList<Cookie>();
			// 防止首次登陆,保存时候遍历的值为null
			allCookies.put(httpUrl.host(), cookies);
		}
		return cookies;
	}

	@Override
	public void saveFromResponse(HttpUrl httpUrl, List<Cookie> cookies) {
		List<Cookie> oldCookies = allCookies.get(httpUrl.host());
		List<Cookie> needRemove = new ArrayList<Cookie>();

		for (Cookie newCookie : cookies) {
			for (Cookie oldCookie : oldCookies) {
				if (newCookie.name().equals(oldCookie.name())) {
					needRemove.add(oldCookie);
				}
			}
		}
		oldCookies.removeAll(needRemove);
		oldCookies.addAll(cookies);
	}

	/**
	 * 获取当前请求的sessionId
	 * @return sessionId
     */
    public String getCookie() {
        List<Cookie> cookies = allCookies.get(mHttpUrl.host());
        if(cookies != null && cookies.size() > 0) {
			Cookie cookie = cookies.get(0);
			return cookie != null ? cookie.toString() : "";
        }
        return "";
    }

}
