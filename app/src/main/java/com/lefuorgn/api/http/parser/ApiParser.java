package com.lefuorgn.api.http.parser;

import com.lefuorgn.api.ApiOkHttp;
import com.lefuorgn.api.common.Delivery;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.http.exception.ApiHttpOvertimeException;
import com.lefuorgn.api.http.exception.ExceptionStatusCode;
import com.lefuorgn.api.http.request.ApiRequest;
import com.lefuorgn.util.TLog;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;

import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okio.BufferedSource;


/**
 * OkHttp请求响应解析器
 * 同步请求请调用无参构造函数，异步请求请调用有参构造函数
 * 如果要自定义自己的解析器，只需要继承此类，重写方法parser(BufferedSource source)即可
 */
public abstract class ApiParser<T> {
    /**
     * 当前请求的请求监听，异步请求才有用
     */
    protected ApiRequest<T> request;
    /**
     * 返回字符串结果时的内容长度
     */
    long contentLength;
    /**
     * 当次请求的编码名称
     */
    private String charsetName;

    /**
     * 同步请求的解析调用此构造函数
     */
    ApiParser(ApiRequest<T> request) {
        this.request = request;
    }

    /**
     * 解析OkHttp同步请求响应，此请求返回单个实体对象
     *
     * @param response 待解析的请求响应体
     * @return 解析成功返回{@link List}
     * @throws ApiHttpException         请求失败状态、解析失败均抛出异常
     * @throws ApiHttpOvertimeException 请求超时异常
     */
    public final T parserSync(Response response) throws ApiHttpException, ApiHttpOvertimeException {

        if (response.isSuccessful()) {
            try {
                getResponseBodyInfo(response.body());
                return parser(response.body().source());
            } catch (IOException e) {
                throw new ApiHttpException(ExceptionStatusCode.STATUS_PARSER_RESULT_ERROR, e.getMessage());
            }
        } else {
            throw new ApiHttpException(ExceptionStatusCode.STATUS_REQUEST_FAILED, response.message());
        }
    }

    /**
     * 解析OkHttp异步请求响应
     * @param response 待解析的请求响应体
     */
    public final void parserAsync(Response response) {
        if (response.isSuccessful()) {
            try {
                getResponseBodyInfo(response.body());
                T data = parser(response.body().source());
                Delivery.get().deliverySuccessResult(data, request.getRequestCallback());
            } catch (IOException e) {
                // 相应体基本信息解析异常
                Delivery.get().deliveryFailureResult(new ApiHttpException(ExceptionStatusCode.STATUS_PARSER_RESULT_ERROR, e.getMessage()), request.getRequestCallback());
            } catch (ApiHttpException e) {
                // 相应体解析异常
                Delivery.get().deliveryFailureResult(e, request.getRequestCallback());
            } catch (ApiHttpOvertimeException e) {
                // 请求超时异常
                Delivery.get().deliveryOvertimeResult(request);
            }
        } else {
            Delivery.get().deliveryFailureResult(new ApiHttpException(ExceptionStatusCode.STATUS_REQUEST_FAILED, response.message()), request.getRequestCallback());
        }
    }

    /**
     * 获取此次请求的编码方式和内容长度
     *
     * @param body 网络请求相应体
     * @throws IOException IO流操作异常
     */
    private void getResponseBodyInfo(ResponseBody body) throws IOException {
        MediaType contentType = body.contentType();
        Charset charset = contentType != null ? contentType.charset(Charset.forName("UTF-8")) : Charset.forName("UTF-8");
        this.charsetName = charset.name();
        this.contentLength = body.contentLength();
    }

    /**
     * 解析OkHttp请求的结果流，并返回解析得到的实体对象
     *
     * @param source 待解析的响应结果流
     * @return 返回指定的类型bean数据
     * @throws ApiHttpException         解析失败抛出异常
     * @throws ApiHttpOvertimeException 请求超时异常
     */
    public abstract T parser(BufferedSource source) throws ApiHttpException, ApiHttpOvertimeException;

    /**
     * 获取当前请求对象
     *
     * @return 当前请求对象
     */
    public ApiRequest<T> getRequest() {
        return request;
    }

    /**
     * 获取请求返回的结果
     *
     * @param source 待解析的响应结果流
     * @return 返回请求响应字符串
     * @throws ApiHttpException         请求失败状态、解析失败均抛出异常
     * @throws ApiHttpOvertimeException 请求超时异常
     */
    final String getWrapperResult(BufferedSource source) throws ApiHttpException, ApiHttpOvertimeException {
        try {
            if (contentLength > Integer.MAX_VALUE) {
                throw new IOException("Cannot buffer entire body for content length: " + contentLength);
            }
            byte[] bytes;
            try {
                bytes = source.readByteArray();

            } finally {
                Util.closeQuietly(source);
            }
            if (contentLength != -1 && contentLength != bytes.length) {
                throw new IOException("Content-Length and stream length disagree");
            }
            String bodyString = getRealString(bytes);
            return getWrapperResult(bodyString);
        } catch (Exception e) {
            if (e instanceof ApiHttpException) {
                throw (ApiHttpException) e;
            } else if (e instanceof ApiHttpOvertimeException) {
                throw (ApiHttpOvertimeException) e;
            } else {
                throw new ApiHttpException(ExceptionStatusCode.STATUS_UNKNOWN, e.getMessage());
            }
        }
    }

    /**
     * 获取请求返回的结果,即data内的数据内容
     *
     * @param bodyString 需要解析的JSON字符串
     * @return 返回内容JSON字符串
     * @throws ApiHttpException         字符串处理异常
     * @throws ApiHttpOvertimeException 请求超时异常
     */
    private String getWrapperResult(String bodyString) throws ApiHttpException, ApiHttpOvertimeException {
        TLog.log(bodyString);
        try {
            if (!ApiOkHttp.getConfig().isWrapperResult) {
                // 结果没有进行包装过
                return bodyString;
            }
            JSONObject json = new JSONObject(bodyString);
            json = json.getJSONObject("ResponseMsg");
            if (!json.has(ApiOkHttp.getConfig().wrapperJsonResult.code_name)) {
                throw new ApiHttpException(ExceptionStatusCode.STATUS_RESULT_FORMAT_ERROR, "结果没有进行包装，无法以包装结果进行解析");
            }
            int flag = json.getInt(ApiOkHttp.getConfig().wrapperJsonResult.code_name);
            if (flag == ApiOkHttp.getConfig().wrapperJsonResult.code_error_value) {
                // 当前返回了错误状态码
                throw new ApiHttpException(ExceptionStatusCode.STATUS_RESULT_EXIST_ERROR_INFO, json.getString(ApiOkHttp.getConfig().wrapperJsonResult.error_name));
            } else if (flag == ApiOkHttp.getConfig().wrapperJsonResult.code_overtime_value) {
                // 当前用户未登录状态
                throw new ApiHttpOvertimeException(json.getString(ApiOkHttp.getConfig().wrapperJsonResult.error_name));
            } else if (flag == ApiOkHttp.getConfig().wrapperJsonResult.code_parameter_error_value) {
                // 参数错误
                throw new ApiHttpException(ExceptionStatusCode.STATUS_REQUEST_ERROR_PARAMS, json.getString(ApiOkHttp.getConfig().wrapperJsonResult.error_name));
            } else if (flag == ApiOkHttp.getConfig().wrapperJsonResult.code_unauthorized_value) {
                // 未授权
                throw new ApiHttpException(ExceptionStatusCode.STATUS_UNAUTHORIZED_ERROR, "未授权");
            }
            return json.getString(ApiOkHttp.getConfig().wrapperJsonResult.result_name);
        } catch (Exception e) {
            if (e instanceof ApiHttpException) {
                throw (ApiHttpException) e;
            } else if (e instanceof ApiHttpOvertimeException) {
                throw (ApiHttpOvertimeException) e;
            } else {
                throw new ApiHttpException(ExceptionStatusCode.STATUS_UNKNOWN, e.getMessage());
            }
        }
    }

    /**
     * 解析jzip文件
     *
     * @param data 字节数据
     * @return 返回解析jzip后的数据
     */
    private String getRealString(byte[] data) {
        byte[] h = new byte[2];
        h[0] = (data)[0];
        h[1] = (data)[1];
        int head = getShort(h);
        boolean t = head == 0x1f8b;
        InputStream in;
        StringBuilder sb = new StringBuilder();
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            if (t) {
                in = new GZIPInputStream(bis);
            } else {
                in = bis;
            }
            BufferedReader r = new BufferedReader(new InputStreamReader(in), 4096);
            for (String line = r.readLine(); line != null; line = r.readLine()) {
                sb.append(line);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private int getShort(byte[] data) {
        return ((data[0] << 8) | data[1] & 0xFF);
    }
}
