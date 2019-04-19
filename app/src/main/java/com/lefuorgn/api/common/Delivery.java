package com.lefuorgn.api.common;

import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.http.request.ApiRequest;


/**
 * 异步结果分发
 */
public class Delivery {

	private static Delivery delivery;

	private Delivery() {
	}
	
	public static Delivery get() {
		if(delivery == null) {
			synchronized (Delivery.class) {
				if(delivery == null) {
					delivery = new Delivery();
				}
			}
		}
		return delivery;
	}

	/**
	 * 请求执行之前的响应分发
	 * @param callback 回调函数
	 */
	public <T> void deliveryOnPreExecute(final RequestCallback<T> callback) {
		if(callback != null) {
			callback.getHandler().post(new Runnable() {
				@Override
				public void run() {
					callback.onPreExecute();
				}
			});
		}
	}
	
	/**
	 * 请求成功之后对结果进行分发
	 * @param result 请求结果实体类
	 * @param callback 回调函数
	 */
	public <T> void deliverySuccessResult(final T result, final RequestCallback<T> callback) {
		if(callback != null) {
			callback.getHandler().post(new Runnable() {
				@Override
				public void run() {
					callback.onSuccess(result);
					callback.onAfterExecute();
				}
			});
		}
	}

	/**
	 * 请求失败之后的响应分发
	 * @param e 请求异常
	 * @param callback 回调函数
	 * @param <T> 泛型
     */
	public <T> void deliveryFailureResult(final ApiHttpException e, final RequestCallback<T> callback) {
		if(callback != null) {
			callback.getHandler().post(new Runnable() {
				@Override
				public void run() {
					callback.onFailure(e);
					callback.onAfterExecute();
				}
			});
		}
	}

	/**
	 * 请求超时之后的响应分发
	 * @param request
     */
	public <T> void deliveryOvertimeResult(final ApiRequest<T> request) {
		if(request.getRequestCallback() != null) {
			request.getRequestCallback().getHandler().post(new Runnable() {
				@Override
				public void run() {
					request.getRequestCallback().onOvertime(request);
				}
			});
		}
	}

	/**
	 * 请求过程中的响应分发
	 * @param totalCount 请求总字节数
	 * @param currCount 当前下载的字节数
	 * @param callback 回调函数
     * @param <T> 泛型
     */
	public <T> void deliveryOnLoading(final long totalCount, final long currCount, final RequestCallback<T> callback) {
		if(callback != null) {
			callback.getHandler().post(new Runnable() {
				@Override
				public void run() {
					callback.onLoading(totalCount, currCount);
				}
			});
		}
	}
}
