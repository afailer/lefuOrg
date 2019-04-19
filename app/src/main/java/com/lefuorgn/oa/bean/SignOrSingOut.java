package com.lefuorgn.oa.bean;

/**
 * 签到或者签退信息
 */

public class SignOrSingOut {

    private String msg; // 提示信息
    private int status; // 状态码
    private long time; // 签到或者签退时间

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "SignOrSingOut{" +
                "msg='" + msg + '\'' +
                ", status=" + status +
                ", time=" + time +
                '}';
    }
}
