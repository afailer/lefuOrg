package com.lefuorgn.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.lefuorgn.AppConfig;
import com.lefuorgn.AppContext;
import com.lefuorgn.AppManager;
import com.lefuorgn.AppStart;
import com.lefuorgn.bean.User;
import com.lefuorgn.lefu.activity.AlarmSolveActivity;
import com.lefuorgn.lefu.activity.LowBatterySolveActivity;
import com.lefuorgn.lefu.fragment.AlarmInformationDetailsFragment;
import com.lefuorgn.oa.activity.AttendanceApplyHandleActivity;
import com.lefuorgn.oa.activity.ClockAttendanceApplyHandleActivity;
import com.lefuorgn.oa.fragment.ClockAttendanceApplyFragment;
import com.lefuorgn.oa.fragment.MyAttendanceApplyFragment;
import com.lefuorgn.receiver.config.JPushConfig;
import com.lefuorgn.util.TLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 极光推送接收
 */

public class ReceiveJPushReceiver extends BroadcastReceiver {

    // 推送类型对象的跳转对象配置文件
    private static Map<String, Class> mConfig;
    private Context mContext;

    static{
        mConfig = new HashMap<String, Class>();
        // 告警信息处理推送
        mConfig.put(JPushConfig.PUSH_TYPE_ALARM_SOS, AlarmSolveActivity.class);
        mConfig.put(JPushConfig.PUSH_TYPE_ALARM_FENCE, AlarmSolveActivity.class);
        mConfig.put(JPushConfig.PUSH_TYPE_ALARM_LOW_BATTERY, LowBatterySolveActivity.class);

        mConfig.put(JPushConfig.PUSH_TYPE_OA_VACATION_VERIFY, ClockAttendanceApplyHandleActivity.class);
        mConfig.put(JPushConfig.PUSH_TYPE_OA_VACATION_RESULT, ClockAttendanceApplyHandleActivity.class);

        mConfig.put(JPushConfig.PUSH_TYPE_OA_VERIFY, AttendanceApplyHandleActivity.class);
        mConfig.put(JPushConfig.PUSH_TYPE_OA_VERIFY_COPY, AttendanceApplyHandleActivity.class);
        mConfig.put(JPushConfig.PUSH_TYPE_OA_VERIFY_RESULT, AttendanceApplyHandleActivity.class);
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        Bundle bundle = intent.getExtras();
        TLog.error("onReceive - " + intent.getAction());
        if(JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            // 在这里可以做些统计，或者做些其他工作
            TLog.error("收到了通知");
            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            String content = bundle.getString(JPushInterface.EXTRA_ALERT);//获取通知内容
            //获取推送的附加字段
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            TLog.error("title == " + title);
            TLog.error("content == " + content);
            TLog.error("extras == " + extras);

        }else if(JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            // 在这里可以自己写代码去定义用户点击后的行为
            TLog.error("用户点击打开了通知");
            //获取推送的附加字段
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            // 判断当前应用是否退出
            if(AppManager.getAppManager().getRunningActivitySize() == 0) {
                // 先开启主页面
                Intent sIntent = new Intent(context, AppStart.class);
                sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                bundle.putBoolean("push", true);
                sIntent.putExtras(bundle);
                context.startActivity(sIntent);
                return;
            }
            // 处理返回的数据信息
            handlePushJson(extras);
        }else if(JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
            TLog.error("收到了自定义消息：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        }else if(AppConfig.INTENT_ACTION_NOTICE_MAIN_PAGE_OPEN.equals(intent.getAction())) {
            // 主页面成功打开, 并且包含用户查看通知的事件
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            // 处理返回的数据信息
            handlePushJson(extras);
        }else if(AppConfig.INTENT_ACTION_NOTICE_LOGIN.equals(intent.getAction())) {
            // 用户登录成功,设置别名
            setAliasForUser();
        }else {
            TLog.error("Unhandled intent - " + intent.getAction());
        }
    }

    /**
     * 处理推送信息(去掉外层包裹的内容)
     * @param extras 获取的推送内容
     */
    private void handlePushJson(String extras) {
        try {
            JSONObject jsonObject = new JSONObject(extras);
            // 推送类型
            String type = jsonObject.getString("type");
            // 推送的实际内容
            String data = jsonObject.getString("data");
            // 推送类型要跳转的页面类字节码
            Class clazz = mConfig.get(type);
            if(clazz == null) {
                // 位置推送内容不进行任何处理
                return;
            }
            jumpToTheSpecifiedPage(data, type, clazz);
        }catch (JSONException e) {
            TLog.error(e.toString());
        }
    }

    /**
     * 跳转到指定的页面
     * @param data 推送的实际内容
     * @param type 推送的内容类型
     * @param clazz 要跳转的页面类字节码(根据类型进行判断)
     */
    private void jumpToTheSpecifiedPage(String data, String type, Class clazz) throws JSONException {
        JSONObject dataObject = new JSONObject(data);
        if(JPushConfig.PUSH_TYPE_ALARM_SOS.equals(type)) {
            // 告警信息内容推送 SOS告警
            jumpToAlarmRelevantPage(dataObject, "SOS告警", clazz);
        }else if(JPushConfig.PUSH_TYPE_ALARM_FENCE.equals(type)) {
            // 告警信息内容推送 电子围栏告警
            jumpToAlarmRelevantPage(dataObject, "电子围栏告警", clazz);
        }else if(JPushConfig.PUSH_TYPE_ALARM_LOW_BATTERY.equals(type)) {
            // 告警信息内容推送 低电量告警
            jumpToAlarmRelevantPage(dataObject, "电量告警", clazz);
        }else if(JPushConfig.PUSH_TYPE_OA_VACATION_VERIFY.equals(type)) {
            // 跳转到 "申请" --> "我审批的" 页面, 待审批状态
            jumpToOaApplyPage(dataObject.getLong("oa_ask_for_leave_id"), clazz, true,
                    ClockAttendanceApplyFragment.BUNDLE_ATTENDANCE_STATUS_UNRESOLVED);
        }else if(JPushConfig.PUSH_TYPE_OA_VACATION_RESULT.equals(type)) {
            // 跳转到 "申请" --> "我申请的" 页面, 除待审批状态外的其他状态
            // 跳转状态不为1, 即可......
            jumpToOaApplyPage(dataObject.getLong("oa_ask_for_leave_id"), clazz, false, 2);
        }else if(JPushConfig.PUSH_TYPE_OA_VERIFY.equals(type)) {
            // 跳转到 "审核" --> "我审批的" 页面 待处理状态
            jumpToOaApplyPage(dataObject.getLong("oa_verify_ask_id"), clazz, true,
                    MyAttendanceApplyFragment.BUNDLE_ATTENDANCE_STATUS_UNRESOLVED);
        }else if(JPushConfig.PUSH_TYPE_OA_VERIFY_COPY.equals(type)) {
            // 跳转到 "审核" --> "我审批的" 页面 抄送我的状态
            jumpToOaApplyPage(dataObject.getLong("oa_verify_ask_id"), clazz, true,
                    MyAttendanceApplyFragment.BUNDLE_ATTENDANCE_STATUS_COPY_FOR_ME);
        }else if(JPushConfig.PUSH_TYPE_OA_VERIFY_RESULT.equals(type)) {
            // 跳转到 "审核" --> "我申请的" 页面 审批处理结果
            // 跳转状态不为1, 即可......
            jumpToOaApplyPage(dataObject.getLong("oa_verify_ask_id"), clazz, false, 2);
        }
    }

    /**
     * 跳转到老人手表定位页面
     * @param object 跳转页面相关信息
     * @param title 告警信息类型
     * @param clazz 跳转页面类字节码
     * @throws JSONException
     */
    private void jumpToAlarmRelevantPage(JSONObject object, String title, Class clazz) throws JSONException {
        Intent intent = new Intent(mContext, clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("status", AlarmInformationDetailsFragment.BUNDLE_ALARM_INFO_STATUS_UNRESOLVED);
        intent.putExtra("id", object.getLong("sos_warning_id"));
        intent.putExtra("agencyId", object.getLong("agency_id"));
        intent.putExtra("agencyName", object.getString("agency_name"));
        intent.putExtra("title", title);
        mContext.startActivity(intent);
    }

    /**
     * 跳转到假期请假页面 即 "申请" 内容模块
     * @param id 请假信息ID
     * @param clazz 跳转页面类字节码
     * @param approval 页面状态; true: 我审批的页面; false: 我申请的页面
     * @param status 当前状态
     */
    private void jumpToOaApplyPage(long id, Class clazz, boolean approval, int status) {
        Intent intent = new Intent(mContext, clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("id", id);
        intent.putExtra("approval", approval);
        intent.putExtra("status", status);
        mContext.startActivity(intent);
    }

    /**
     * 为用户设置别名
     */
    private void setAliasForUser() {
        //设置别名，不同的用户有不同的别名,用于推送
        User user = AppContext.getInstance().getUser();
        if(user == null) {
            TLog.error("设置别名失败 user == null");
            return;
        }
        JPushInterface.setAlias(AppContext.getInstance(), user.getUser_id() + "", new TagAliasCallback() {

            @Override
            public void gotResult(int arg0, String arg1, Set<String> arg2) {
                TLog.error(arg0 == 0 ? "设置别名成功" : "设置别名失败");
            }
        });
    }

}
