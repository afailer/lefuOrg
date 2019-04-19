package com.lefuorgn.lefu.util;

import com.lefuorgn.R;
import com.lefuorgn.db.util.PermissionManager;
import com.lefuorgn.lefu.activity.AlarmInformationActivity;
import com.lefuorgn.lefu.activity.BirthdayReminderActivity;
import com.lefuorgn.lefu.activity.ContactActivity;
import com.lefuorgn.lefu.activity.CordonInfoActivity;
import com.lefuorgn.lefu.activity.DataAuditActivity;
import com.lefuorgn.lefu.activity.ElderlyInfoActivity;
import com.lefuorgn.lefu.activity.EstimateActivity;
import com.lefuorgn.lefu.activity.FamilyInfoActivity;
import com.lefuorgn.lefu.activity.HospitalizationInfoActivity;
import com.lefuorgn.lefu.activity.LeaveActivity;
import com.lefuorgn.lefu.activity.LocationQueryActivity;
import com.lefuorgn.lefu.activity.LogbookActivity;
import com.lefuorgn.lefu.activity.LogbookInfoActivity;
import com.lefuorgn.lefu.activity.MedicalHistoryInfoActivity;
import com.lefuorgn.lefu.activity.NursingInfoActivity;
import com.lefuorgn.lefu.activity.NursingInfoBatchEditingActivity;
import com.lefuorgn.lefu.activity.PersonalDataActivity;
import com.lefuorgn.lefu.activity.ReadilyShootActivity;
import com.lefuorgn.lefu.activity.SignInfoActivity;
import com.lefuorgn.lefu.activity.TodayWorkActivity;
import com.lefuorgn.lefu.bean.GridLayoutItem;
import com.lefuorgn.oa.activity.AttendanceApplyActivity;
import com.lefuorgn.oa.activity.AttendanceApprovalActivity;
import com.lefuorgn.oa.activity.AttendanceRecordActivity;
import com.lefuorgn.oa.activity.DailyAttendanceActivity;
import com.lefuorgn.oa.activity.MyWorkPlanActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 网格布局条目信息类集合
 */

public class GridLayoutItemUtils {

    /**
     * 获取数据管理信息条目
     * @return 条目集合
     */
    public static List<GridLayoutItem> getDataManagementItem() {
        List<GridLayoutItem> result = new ArrayList<GridLayoutItem>();
        // 显示权限
        String display = PermissionManager.P_V;
        result.add(new GridLayoutItem("个人数据", PersonalDataActivity.class,
                R.drawable.home_personal_data_selector,
                PermissionManager.hasPermission(PermissionManager.SINGN_DATA_ENTRY + display), false));
        result.add(new GridLayoutItem("体征录入", SignInfoActivity.class,
                R.drawable.home_sign_info_selector,
                PermissionManager.hasSignPermission(), false));
        result.add(new GridLayoutItem("护理录入", NursingInfoActivity.class,
                R.drawable.home_nursing_info_selector,
                PermissionManager.hasPermission(PermissionManager.DAILY + display), false));
        result.add(new GridLayoutItem("批量录入", NursingInfoBatchEditingActivity.class,
                R.drawable.home_batch_editing_selector,
                PermissionManager.hasPermission(PermissionManager.DAILY + display), false));
        result.add(new GridLayoutItem("随手拍", ReadilyShootActivity.class,
                R.drawable.home_readily_shoot_selector,
                PermissionManager.hasPermission(PermissionManager.LIFE + display), false));
        result.add(new GridLayoutItem("护理月报", null,
                R.drawable.home_nursing_report_selector,
                PermissionManager.hasPermission(PermissionManager.DAILY + display), true));
        result.add(new GridLayoutItem("交班记录", LogbookActivity.class,
                R.drawable.home_logbook_selector,
                PermissionManager.hasPermission(PermissionManager.LOGBOOK + display), true));
        result.add(new GridLayoutItem("老人外出", LeaveActivity.class,
                R.drawable.home_leave_selector,
                PermissionManager.hasPermission(PermissionManager.LEAVE_OUT + display), true));
        result.add(new GridLayoutItem("生日提醒", BirthdayReminderActivity.class,
                R.drawable.home_birthday_reminder_selector,
                PermissionManager.hasPermission(PermissionManager.BIRTHDAY + display), true));
        result.add(new GridLayoutItem("数据审核", DataAuditActivity.class,
                R.drawable.home_data_audit_selector,
                PermissionManager.hasPermission(PermissionManager.VERIFY + display), true));
        result.add(new GridLayoutItem("今日工作", TodayWorkActivity.class,
                R.drawable.home_today_work_selector,
                PermissionManager.hasPermission(PermissionManager.NURSING_TASK + display), true));
        result.add(new GridLayoutItem("定位查询", LocationQueryActivity.class,
                R.drawable.home_location_query_selector, true, true));
        result.add(new GridLayoutItem("告警信息", AlarmInformationActivity.class,
                R.drawable.home_alarm_information_selector, true, true));
        return result;
    }

    /**
     * 获取数据管理信息条目
     * @return 条目集合
     */
    public static List<GridLayoutItem> getMobileOfficeItem() {
        // 显示权限
        String display = PermissionManager.P_V;
        List<GridLayoutItem> result = new ArrayList<GridLayoutItem>();
        result.add(new GridLayoutItem("日常考勤", DailyAttendanceActivity.class,
                R.drawable.home_daily_attendance_selector, true, true));
        result.add(new GridLayoutItem("考勤记录", AttendanceRecordActivity.class,
                R.drawable.home_attendance_record_selector,
                PermissionManager.hasPermission(PermissionManager.OA_ATTENDANCEREPORTING + display), true, "敬请期待"));
        result.add(new GridLayoutItem("我的排班", MyWorkPlanActivity.class,
                R.drawable.home_my_schedule_selector,
                PermissionManager.hasPermission(PermissionManager.OA_STAFFPLAN + display), true, "敬请期待"));
        result.add(new GridLayoutItem("申请", AttendanceApplyActivity.class,
                R.drawable.home_attendance_apply_selector,
                PermissionManager.hasPermission(PermissionManager.OA_ASKFORLEAVE + display), true));
        result.add(new GridLayoutItem("审批", AttendanceApprovalActivity.class,
                R.drawable.home_attendance_approval_selector,
                PermissionManager.hasPermission(PermissionManager.OA_VERIFYASK + display), true, "敬请期待"));
        result.add(new GridLayoutItem("联系方式", ContactActivity.class,
                R.drawable.home_contact_selector, true, true));
        return result;
    }

    /**
     * 布局条目信息初始化
     */
    public static List<GridLayoutItem> getOldPeopleInfoItem() {
        List<GridLayoutItem> result = new ArrayList<GridLayoutItem>();
        // 显示权限
        String display = PermissionManager.P_V;
        boolean oPermission = PermissionManager.hasPermission(PermissionManager.OLDPEOPLE + display);
        result.add(new GridLayoutItem("基本信息", ElderlyInfoActivity.class,
                R.drawable.person_elderly_info_selector, oPermission, false));
        result.add(new GridLayoutItem("家属信息", FamilyInfoActivity.class,
                R.drawable.person_family_info_selector,
                PermissionManager.hasPermission(PermissionManager.FAMILY + display), false));
        result.add(new GridLayoutItem("住院信息", HospitalizationInfoActivity.class,
                R.drawable.person_hospitalization_info_selector, oPermission, false));
        result.add(new GridLayoutItem("评估", EstimateActivity.class,
                R.drawable.person_estimate_selector,
                PermissionManager.hasPermission(PermissionManager.EXAM_ANSWER + display), true));
        boolean health = PermissionManager.hasPermission(PermissionManager.HEALTH + display);
        result.add(new GridLayoutItem("体征数据警戒值", CordonInfoActivity.class,
                R.drawable.person_sign_data_cordon_selector, health, false));
        result.add(new GridLayoutItem("护理项目", null,
                R.drawable.person_nursing_item_selector, true, false, "敬请期待"));
        result.add(new GridLayoutItem("病史", MedicalHistoryInfoActivity.class,
                R.drawable.person_medical_history_selector, health, true));
        result.add(new GridLayoutItem("交班记录", LogbookInfoActivity.class,
                R.drawable.person_logbook_details_selector,
                PermissionManager.hasPermission(PermissionManager.LOGBOOK + display), true));
        return result;
    }

}
