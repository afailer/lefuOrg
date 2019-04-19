package com.lefuorgn.api.common;

import android.os.Handler;
import android.os.Looper;

import com.lefuorgn.AppContext;
import com.lefuorgn.api.ApiOkHttp;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.http.exception.ExceptionStatusCode;
import com.lefuorgn.api.http.request.ApiRequest;
import com.lefuorgn.api.http.request.JsonApiRequest;
import com.lefuorgn.api.remote.common.RemoteUtil;
import com.lefuorgn.bean.User;
import com.lefuorgn.util.TLog;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/**
 * 请求过程中的监听
 */
public abstract class RequestCallback<T> {
	/**
	 * 请求返回类型
	 */
	private Type clazz;
    private Handler mHandler;
	
	public RequestCallback()  {
		this(new Handler(Looper.getMainLooper()));
    }

	public RequestCallback(Handler handler) {
        this.mHandler = handler;
        this.clazz = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
	
	/**
	 * 请求执行前监听响应,UI线程(可以设置请求进度条)
	 */
	void onPreExecute() {
		
	}
	
	/**
	 * 请求执行结束之后的响应监听，UI线程(可以取消请求进度条，onSuccess或onFailure结束之后都会执行此方法)
	 */
	void onAfterExecute() {
		
	}
	
	/**
	 * 请求成功监听响应,UI线程
	 * @param result 请求成功的实体
	 */
	public abstract void onSuccess(T result);
	
	/**
	 * 请求失败、结果解析失败监听响应,UI线程
	 * @param e 请求异常类
	 */
	public abstract void onFailure(ApiHttpException e);

    /**
     * 用户超时
     * @param request 请求超时的请求接口
     */
	void onOvertime(final ApiRequest<T> request) {

        TLog.log("当前请求超时, 超时次数:" + request.getFrequency());

        if(request.getFrequency() >= 3) {
            request.getRequestCallback().onFailure(new ApiHttpException(ExceptionStatusCode.STATUS_REQUEST_OVERTIME, "用户超时, 登录失败"));
            return;
        }

        JsonApiRequest<User> nRequest = new JsonApiRequest<User>(RemoteUtil.getLoginUrl(), User.class);
		User user = AppContext.getInstance().getUser();
        nRequest.addParam("version", RemoteUtil.getVersion())
                .addParam("extInfo", true)
                .addParam("user_name", user.getMobile())
                .addParam("password", user.getPassword())
                .addParam("loginIMEI", RemoteUtil.getDeviceIMEI());
        ApiOkHttp.postAsync(nRequest, new RequestCallback<User>() {
            @Override
            public void onSuccess(User result) {
                TLog.log("登录成功...");
                // 登录成功
                ApiOkHttp.postAsync(request);
            }

            @Override
            public void onFailure(ApiHttpException e) {
				if(e.getErrorCode() == ExceptionStatusCode.STATUS_RESULT_EXIST_ERROR_INFO) {
					// 用户名或密码错误
                    AppContext.getInstance().skipLoginActivity();
				}else if(e.getErrorCode() == ExceptionStatusCode.STATUS_NO_NETWORK_CONNECTION) {
                    request.getRequestCallback().onFailure(new ApiHttpException(ExceptionStatusCode.STATUS_REQUEST_OVERTIME, "当前网络不可用"));
                }else {
                    request.getRequestCallback().onFailure(new ApiHttpException(ExceptionStatusCode.STATUS_NO_NETWORK_CONNECTION, "用户超时, 登录失败"));
                }
            }
        });
    }
	
	/**
	 * 执行过程中监听响应，仅用于文件上传、下载过程中的进度条加载显示
	 * @param count 当前下载总字节数
	 * @param current 当前已经下载的字节数
	 */
	public void onLoading(long count, long current) {
		
	}

	/**
	 * 获取请求的返回类型
	 * @return 请求数据实体类类型
	 */
	public Type getResultType() {
		return clazz;
	}

    /**
     * 获取当前Handler
     * @return Handler
     */
    public Handler getHandler() {
        return mHandler;
    }
}
