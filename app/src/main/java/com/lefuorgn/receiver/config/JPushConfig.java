package com.lefuorgn.receiver.config;

/**
 * 推送码配置信息
 */

public class JPushConfig {

    /**
     * 推送告警类型(SOS告警信息)
     */
    public static final String PUSH_TYPE_ALARM_SOS = "00901";
    /**
     * 推送告警类型(围栏告警信息)
     */
    public static final String PUSH_TYPE_ALARM_FENCE = "00902";
    /**
     * 推送告警类型(低电量告警信息)
     */
    public static final String PUSH_TYPE_ALARM_LOW_BATTERY = "00903";
    /**
     * OA 请假推送审批人审批
     */
    public static final String PUSH_TYPE_OA_VACATION_VERIFY = "00801";
    /**
     * OA 请假审批完成通知申请人
     */
    public static final String PUSH_TYPE_OA_VACATION_RESULT = "00803";
    /**
     * OA 审批 推送审批人审批
     */
    public static final String PUSH_TYPE_OA_VERIFY = "00804";
    /**
     * OA 审批 推送给抄送人
     */
    public static final String PUSH_TYPE_OA_VERIFY_COPY = "00805";
    /**
     * OA 审批 审批完成通知申请人
     */
    public static final String PUSH_TYPE_OA_VERIFY_RESULT = "00806";

}
