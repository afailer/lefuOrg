package com.lefuorgn.api.http.exception;

/**
 * 用户登录超时异常
 */

public class ApiHttpOvertimeException extends Exception {

    public ApiHttpOvertimeException() {
        super();
    }

    public ApiHttpOvertimeException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ApiHttpOvertimeException(String detailMessage) {
        super(detailMessage);
    }

    public ApiHttpOvertimeException(Throwable throwable) {
        super(throwable);
    }


}
