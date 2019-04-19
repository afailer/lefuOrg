package com.lefuorgn.api.http.exception;

public class ApiHttpException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * 异常码
	 */
	private int errorCode = ExceptionStatusCode.STATUS_UNKNOWN;
	
	public ApiHttpException() {
		super();
	}

	public ApiHttpException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ApiHttpException(int errorCode, String detailMessage) {
		super(detailMessage);
		this.errorCode = errorCode;
	}

	public ApiHttpException(int errorCode, Throwable throwable) {
		super(throwable);
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public String toString() {
		String msg = super.toString();
        StringBuilder sb = new StringBuilder();
        sb.append("errorCode:");
		switch(errorCode) {
            case ExceptionStatusCode.STATUS_RESULT_FORMAT_ERROR:
                sb.append("请求结果没有安装指定格式进行包装");
                break;
            case ExceptionStatusCode.STATUS_RESULT_EXIST_ERROR_INFO:
                sb.append("请求结果中存在服务端错误信息");
                break;
            case ExceptionStatusCode.STATUS_REQUEST_FAILED:
                sb.append("请求失败");
                break;
            case ExceptionStatusCode.STATUS_PARSER_RESULT_ERROR:
                sb.append("解析结果失败");
                break;
            case ExceptionStatusCode.STATUS_REQUEST_IO_ERROR:
                sb.append("请求IO异常导致失败");
                break;
            case ExceptionStatusCode.STATUS_REQUEST_ERROR_PARAMS:
                sb.append("参数错误或者请求时错误");
                break;
            case ExceptionStatusCode.STATUS_REQUEST_OVERTIME:
                sb.append("用户未登录");
                break;
            case ExceptionStatusCode.STATUS_UNAUTHORIZED_ERROR:
                sb.append("未授权");
                break;
            case ExceptionStatusCode.STATUS_NO_NETWORK_CONNECTION:
                sb.append("当前网络不可用");
                break;
            default :
                break;
		}
        sb.append("\n");
        sb.append(msg);
		return sb.toString();
	}
	
}
