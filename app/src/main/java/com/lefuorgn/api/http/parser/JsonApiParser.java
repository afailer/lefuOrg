package com.lefuorgn.api.http.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.lefuorgn.api.common.Json;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.http.exception.ApiHttpOvertimeException;
import com.lefuorgn.api.http.exception.ExceptionStatusCode;
import com.lefuorgn.api.http.request.ApiRequest;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.TLog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okio.BufferedSource;

/**
 * JSON解析器，将服务返回的JSON字符串解析成JavaBean对象或JavaBean集合
 * 此解析器使用最多
 */
public class JsonApiParser<T> extends ApiParser<T> {
	/**
	 * 结果返回的每条数据类型(同步请求必须设置)
	 */
    private Type resultType;
	
	public JsonApiParser(ApiRequest<T> request) {
		super(request);
	}

	/**
	 * 同步请求最好直接调用此构造函数，异步请求调用此构造函数设置的返回类型不会起作用
	 * @param request		请求对象
	 * @param resultType	结果返回的每条数据类型(同步请求必须设置)，如果是List的话，此resultType为每一项的数据的类型
	 */
	public JsonApiParser(ApiRequest<T> request, Type resultType) {
		super(request);
		this.resultType = resultType;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public T parser(BufferedSource source) throws ApiHttpException, ApiHttpOvertimeException {
        // 获取数据体内容
        String stringResult = getWrapperResult(source);
        if (request.getRequestCallback() != null) { // 异步请求
            resultType = request.getRequestCallback().getResultType();
            // 校验内容请求体
            if ("null".equals(stringResult) || StringUtils.isEmpty(stringResult)) {
                // 校验内容为空
                if(String.class == resultType) {
                    return (T) "";
                }else if(resultType.toString().contains("List")) {
                    return (T) new ArrayList();
                }
                return null;
            }
            try {
                return Json.getGson().fromJson(stringResult, resultType);
            } catch (JsonSyntaxException e) {
                TLog.error(e.toString());
                if(String.class == resultType) {
                    return (T) stringResult;
                }else if(resultType.toString().contains("List")) {
                    return (T) new ArrayList();
                }
                return null;
            }
        } else { // 同步请求结果类型处理
            if (resultType == null) {
                throw new ApiHttpException(ExceptionStatusCode.STATUS_REQUEST_ERROR_PARAMS
                        , "synchronous request must be set resultType");
            }
            try {
                JsonParser parser = new JsonParser();
                JsonElement jsonElement = parser.parse(stringResult);
                if (jsonElement.isJsonArray()) {//结果是JSON数组
                    List list = new ArrayList();
                    JsonArray array = jsonElement.getAsJsonArray();
                    for (JsonElement json : array) {
                        list.add(Json.getGson().fromJson(json, resultType));
                    }
                    return (T) list;
                } else {//不是JSON数组
                    return Json.getGson().fromJson(jsonElement, resultType);
                }
            } catch (Exception e) {
                TLog.error("json数据处理===>>> " + e.toString());
                throw new ApiHttpException(ExceptionStatusCode.STATUS_UNKNOWN, e.getMessage());
            }
        }
    }
}
