package com.lefuorgn.api;

import com.lefuorgn.AppContext;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.OKHttpBuilder;
import com.lefuorgn.api.http.OkHttpExecutor;
import com.lefuorgn.api.http.config.HttpConfig;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.http.exception.ApiHttpOvertimeException;
import com.lefuorgn.api.http.exception.ExceptionStatusCode;
import com.lefuorgn.api.http.request.ApiRequest;
import com.lefuorgn.api.remote.common.RemoteUtil;
import com.lefuorgn.util.NetworkUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


/**
 * OKHttp请求管理类，单例模式实现，建议在使用之前在Application中调用{@link #init(HttpConfig)}进行初始化
 */
public final class ApiOkHttp {
	/**
	 * OkHttpManager单例对象，保证OKHttpClient全局唯一实例
	 */
	private static ApiOkHttp mOkHttpManager;
	/**
	 * 
	 */
	private static OkHttpClient mOkHttpClient;
	/**
	 * OkHttp相关配置信息
	 */
	private HttpConfig mConfig;
	/**
	 * OkHttp请求执行器
	 */
	private static OkHttpExecutor mOkHttpExecutor;
	
	private ApiOkHttp(HttpConfig config) {
		mConfig = config;
	}
	
	/**
	 * 获取单例对象，建议在使用之前调用此方法进行初始化
	 * @param config 网络请求配置
	 * @return 单例对象
	 */
	public static ApiOkHttp init(HttpConfig config) {
		if(mOkHttpManager == null) {
			synchronized (ApiOkHttp.class) {
				if(mOkHttpManager == null) {
					mOkHttpManager = new ApiOkHttp(config);
					mOkHttpManager.initOkHttpConfig();
					mOkHttpExecutor = new OkHttpExecutor(mOkHttpClient);
				}
			}
		}
		return mOkHttpManager;
	}
	/**
	 * 初始化网络请求配置
	 */
	private void initOkHttpConfig() {
		mOkHttpClient = new OkHttpClient.Builder()
				.connectTimeout(mConfig.connectTimeOut, TimeUnit.SECONDS)
				.readTimeout(mConfig.readTimeOut, TimeUnit.SECONDS)
				.writeTimeout(mConfig.writeTimeOut, TimeUnit.SECONDS)
				.cookieJar(mConfig.cookieJar)
				.hostnameVerifier(mConfig.hostnameVerifier)
				.build(); // build返回的是新的对象
	}

	public static HttpConfig getConfig() {
		return mOkHttpManager.mConfig;
	}

	/**
	 * 获取网络请求执行器
	 * @return 执行器
     */
	private static OkHttpExecutor getOkHttpExecutor() {
		return mOkHttpExecutor;
	}
	
	/**
	 * 同步Get请求
	 * @param 	request 网络请求对象
	 * @return						请求成功、解析成功后返回实体对象
	 * @throws ApiHttpException 	请求失败，结果解析失败抛出该异常
	 * @throws ApiHttpOvertimeException 	请求超时异常
	 */
	public static <T> T getSync(ApiRequest<T> request) throws ApiHttpException, ApiHttpOvertimeException {
		request.setRequestCallback(null);
		return ApiOkHttp.getOkHttpExecutor().syncExecute(OKHttpBuilder.buildGetRequest(request), request.getDataParser());
	}
	
	/**
	 * 异步Get请求
	 * @param request 网络请求对象
	 * @param requestCallback 请求接口回调函数
	 */
	public static <T> void getAsync(ApiRequest<T> request, RequestCallback<T> requestCallback) {
		if(AppContext.getInstance().getNetState() == NetworkUtils.NETWORK_NONE) {
			ApiHttpException e = new ApiHttpException(
					ExceptionStatusCode.STATUS_NO_NETWORK_CONNECTION, "当前网络不可用, 请检查网络连接");
			requestCallback.onFailure(e);
			return;
		}
		request.setRequestCallback(requestCallback);
		// 当前请求自加1
		request.setFrequency();
		ApiOkHttp.getOkHttpExecutor().asyncExecute(OKHttpBuilder.buildGetRequest(request), request.getDataParser());
	}
	
	/**
	 * 同步Post请求
	 * @param 	request 网络请求对象
	 * @return						请求成功、解析成功后返回实体对象
	 * @throws ApiHttpException 	请求失败，结果解析失败抛出该异常
	 * @throws ApiHttpOvertimeException 	请求超时异常
	 */
	public static <T> T postSync(ApiRequest<T> request) throws ApiHttpException, ApiHttpOvertimeException {
		request.setRequestCallback(null);
		return ApiOkHttp.getOkHttpExecutor().syncExecute(OKHttpBuilder.buildPostRequest(request), request.getDataParser());
	}
	
	/**
	 * 异步Post请求(可以返回单个：T为Class 或 多个：T为List<T>)
	 * @param request 网络请求对象
	 * @param requestCallback 请求接口回调函数
	 */
	public static <T> void postAsync(ApiRequest<T> request, RequestCallback<T> requestCallback) {
		if(AppContext.getInstance().getNetState() == NetworkUtils.NETWORK_NONE) {
			ApiHttpException e = new ApiHttpException(
                    ExceptionStatusCode.STATUS_NO_NETWORK_CONNECTION, "当前网络不可用, 请检查网络连接");
			requestCallback.onFailure(e);
			return;
		}
		setRequestHeader(request);
		request.setRequestCallback(requestCallback);
		postAsync(request);
	}

	/**
	 * 异步Post请求(可以返回单个：T为Class 或 多个：T为List<T>)
	 * @param request 网络请求对象
	 */
	public static <T> void postAsync(ApiRequest<T> request) {
		// 当前请求自加1
		request.setFrequency();
		ApiOkHttp.getOkHttpExecutor().asyncExecute(OKHttpBuilder.buildPostRequest(request), request.getDataParser());
	}

	/**
	 * 请求头固定值的添加
	 * @param request 网络请求对象
	 */
	private static<T> void setRequestHeader(ApiRequest<T> request){
		request.addHeader("User-Agent", RemoteUtil.getUserAgent())
		.addHeader("Accept-Encoding", "gzip, deflate");
	}

}
