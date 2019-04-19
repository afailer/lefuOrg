package com.lefuorgn.api.http;

import com.lefuorgn.api.common.Delivery;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.http.exception.ApiHttpOvertimeException;
import com.lefuorgn.api.http.exception.ExceptionStatusCode;
import com.lefuorgn.api.http.parser.ApiParser;
import com.lefuorgn.util.TLog;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 请求执行器，发起OkHttp请求
 */
public class OkHttpExecutor {
	private OkHttpClient mOkHttpClient;

	public OkHttpExecutor(OkHttpClient okHttpClient) {
		super();
		this.mOkHttpClient = okHttpClient;
	}
	
	/**
	 * 异步执行某个OkHttp请求
	 * @param request   请求对象
	 * @param parser	请求结果解析器
	 */
	public <T> void asyncExecute(Request request, final ApiParser<T> parser) {
		TLog.log("异步请求地址：" + request.url().toString());
		if(parser == null) {
			throw new IllegalArgumentException("not set Data Parser");
		}
		Delivery.get().deliveryOnPreExecute(parser.getRequest().getRequestCallback());
		mOkHttpClient.newCall(request).enqueue(new Callback() {
			
			@Override
			public void onFailure(Call arg0, IOException e) {
				Delivery.get().deliveryFailureResult(new ApiHttpException(ExceptionStatusCode.STATUS_REQUEST_IO_ERROR, "网络请求失败"), parser.getRequest().getRequestCallback());
			}

			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				parser.parserAsync(response);
			}
		});
	}
	
	/**
	 * 同步执行某个OkHttp请求，并返回实体对象
	 * @param request				请求对象
	 * @param parser                解析器
	 * @return						请求解析成功返回实体对象
	 * @throws ApiHttpException 	请求失败、解析失败均抛出异常
	 * @throws ApiHttpOvertimeException 请求超时异常
	 */
	public <T> T syncExecute(Request request, ApiParser<T> parser) throws ApiHttpException, ApiHttpOvertimeException {
		TLog.log("同步请求地址：" + request.url().toString());
		if(parser == null) {
			throw new IllegalArgumentException("not set Data Parser");
		}
		Response response;
		try {
			response = mOkHttpClient.newCall(request).execute();
		} catch (IOException e) {
			throw new ApiHttpException(ExceptionStatusCode.STATUS_REQUEST_FAILED, e.getMessage());
		}
		return parser.parserSync(response);
	}
}
