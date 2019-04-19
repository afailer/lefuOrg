package com.lefuorgn.api.remote;

import com.lefuorgn.api.ApiOkHttp;
import com.lefuorgn.api.common.MultiMediaUpload;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.request.JsonApiRequest;
import com.lefuorgn.api.http.request.JsonParameterApiRequest;
import com.lefuorgn.api.http.request.UploadMultiMediaApiRequest;
import com.lefuorgn.api.remote.common.RemoteUtil;
import com.lefuorgn.oa.bean.ApplyDetails;
import com.lefuorgn.oa.bean.ApprovalInformation;
import com.lefuorgn.oa.bean.AttendanceApply;
import com.lefuorgn.oa.bean.AttendanceApplyDetails;
import com.lefuorgn.oa.bean.AttendanceApprovalClock;
import com.lefuorgn.oa.bean.AttendanceApprovalCurrentApprover;
import com.lefuorgn.oa.bean.AttendanceApprovalSubmit;
import com.lefuorgn.oa.bean.AttendanceApprovalType;
import com.lefuorgn.oa.bean.ClockAttendanceApply;
import com.lefuorgn.oa.bean.ClockType;
import com.lefuorgn.oa.bean.DepartmentList;
import com.lefuorgn.oa.bean.ExternalPackage;
import com.lefuorgn.oa.bean.OaAttendanceRecord;
import com.lefuorgn.oa.bean.OaConfig;
import com.lefuorgn.oa.bean.SignOrSingOut;
import com.lefuorgn.oa.bean.StuffPlanList;

import java.util.List;

public class OaApi {
    private static String getUrl(String url){
        return RemoteUtil.getAbsoluteApiUrl(url);
    }

    /**
     * 获取审批内容的类型
     * @param callback 接口回调
     */
    public static void getAttendanceApprovalType(RequestCallback<List<AttendanceApprovalType>> callback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/verifyFormCtr/listWithPage");
        JsonParameterApiRequest<List<AttendanceApprovalType>> mRequest = new JsonParameterApiRequest<List<AttendanceApprovalType>>(url, List.class);
        mRequest.addParam("status", 0);
        mRequest.addParam("pageNo",0);
        ApiOkHttp.postAsync(mRequest, callback);
    }

    /**
     * 获取审批内容详情
     * @param id 审批内容类型ID
     * @param callback 接口回调
     */
    public static void getAttendanceApprovalClock(long id, RequestCallback<AttendanceApprovalClock> callback){
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/verifyFormCtr/queryInfoById");
        JsonParameterApiRequest<AttendanceApprovalClock> mRequest = new JsonParameterApiRequest<AttendanceApprovalClock>(url, AttendanceApprovalClock.class);
        mRequest.addParam("id",id);
        ApiOkHttp.postAsync(mRequest,callback);
    }

    /**
     * 提交用户申请提交
     * @param agencyId 机构ID
     * @param typeId 申请类型ID
     * @param typeName 申请类型名称
     * @param result 申请内容
     * @param list 审批人信息以及抄送人信息
     * @param callback 接口回调
     */
    public static void saveAttendanceApprovalClock(long agencyId, long typeId, String typeName, String result,
                                                   List<AttendanceApprovalSubmit> list, RequestCallback<Boolean> callback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/verifyAskCtr/saveAsk");
        JsonParameterApiRequest<Boolean> mRequest=new JsonParameterApiRequest<Boolean>(url, Boolean.class);
        mRequest.addParam("agency_id", agencyId);
        mRequest.addParam("oa_verify_from_id", typeId);
        mRequest.addParam("oa_verify_from_name", typeName);
        mRequest.addParam("result", result);
        mRequest.addParam("verifyAskLines", list);
        ApiOkHttp.postAsync(mRequest, callback);
    }

    public static void queryStuffList(int type,String ids,RequestCallback<List<DepartmentList>> callback){
        JsonApiRequest<List<DepartmentList>> mRequest=new JsonApiRequest<List<DepartmentList>>(getUrl("lefuyun/staffCtr/queryStaffList"),List.class);
        mRequest.addParam("type",type);
        if(!"".equals(ids)){
            mRequest.addParam("ids",ids);
        }
        ApiOkHttp.postAsync(mRequest,callback);
    }

    /**
     * 审批信息列表
     * @param status 告警记录id
     * @param callback 接口回调
     */
    public static void getMyAttendanceApply(int status, int pageNo, boolean approval, RequestCallback<List<AttendanceApply>> callback) {
        String uri;
        if(approval) {
            // 我审批的
            uri = "lefuyun/verifyAskCtr/queryListAudit";
        }else {
            // 我申请的列表信息
            uri = "lefuyun/verifyAskCtr/queryListAsk";
        }
        String url = RemoteUtil.getAbsoluteApiUrl(uri);
        JsonParameterApiRequest<List<AttendanceApply>> nRequest = new JsonParameterApiRequest<List<AttendanceApply>>(url, List.class);
        nRequest.addParam("status", status)
                .addParam("pageNo", pageNo);
        ApiOkHttp.postAsync(nRequest, callback);
    }

    /**
     * 审批信息列表(考勤信息)
     * @param status 告警记录id
     * @param callback 接口回调
     */
    public static void getClockAttendanceApply(long userId, int status, int pageNo, boolean approval, RequestCallback<List<ClockAttendanceApply>> callback) {
        String uri;
        if(approval) {
            // 我审批的
            uri = "lefuyun/askForLeaveVerifyCtr/listWithPage";
        }else {
            // 我申请的列表信息
            uri = "lefuyun/askForLeaveCtr/listWithPage";
        }
        String url = RemoteUtil.getAbsoluteApiUrl(uri);
        JsonParameterApiRequest<List<ClockAttendanceApply>> nRequest = new JsonParameterApiRequest<List<ClockAttendanceApply>>(url, List.class);
        nRequest.addParam("status", status)
                .addParam("pageNo", pageNo)
                .addParam("user_id", userId);
        ApiOkHttp.postAsync(nRequest, callback);
    }

    /**
     * 获取审批信息详情
     * @param id 详情ID
     * @param callback 接口回调
     */
    public static void getAttendanceApplyDetails(long id, RequestCallback<AttendanceApplyDetails> callback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/verifyAskCtr/queryInfoById");
        JsonParameterApiRequest<AttendanceApplyDetails> nRequest = new JsonParameterApiRequest<AttendanceApplyDetails>(url, AttendanceApplyDetails.class);
        nRequest.addParam("id", id);
        ApiOkHttp.postAsync(nRequest, callback);
    }

    /**
     * 获取审批人和抄送人信息
     * @param id 详情ID
     * @param callback 接口回调
     */
    public static void getAttendanceApprovalApprover(long id, RequestCallback<ExternalPackage> callback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/verifyAskCtr/queryAuditInfoById");
        JsonParameterApiRequest<ExternalPackage> mRequest = new JsonParameterApiRequest<ExternalPackage>(url, ExternalPackage.class);
        mRequest.addParam("id", id);
        ApiOkHttp.postAsync(mRequest, callback);
    }

    /**
     * 上传指定表的信息
     * @param files 多媒体文件集合
     * @param requestCallback 回调接口
     */
    public static void uploadMultiMedia(List<MultiMediaUpload> files, RequestCallback<String> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/dailyNursingRecordCtr/addOrUpdateMedias");
        UploadMultiMediaApiRequest<String> request = new UploadMultiMediaApiRequest<String>(url, files);
        ApiOkHttp.postAsync(request, requestCallback);
    }

    /**
     * 审核申请 通过、驳回、撤销均调用此接口
     * @param approver 当前审核人信息
     * @param submit 下一级审核人信息
     * @param callback 接口回调
     */
    public static void approvalOperation(AttendanceApprovalCurrentApprover approver, AttendanceApprovalSubmit submit, RequestCallback<Boolean> callback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/verifyAskCtr/saveAskAudit");
        JsonParameterApiRequest<Boolean> mRequest = new JsonParameterApiRequest<Boolean>(url, Boolean.class);
        mRequest.addParam("audit", approver);
        if(submit != null) {
            mRequest.addParam("nextVerifyAskLine", submit);
        }
        ApiOkHttp.postAsync(mRequest, callback);
    }

    /**
     * 是否在指定的范围内(用于用户打卡)
     * @param longitude 当前用户经度
     * @param latitude 当前用户纬度
     * @param wifi_mac wifi mac地址
     * @param callback 接口回调
     */
    public static void isSpecifiedRange(String longitude, String latitude, String wifi_mac, RequestCallback<String> callback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/attendanceCtr/getInfoByGpsOrMac");
        JsonParameterApiRequest<String> nRequest = new JsonParameterApiRequest<String>(url, String.class);
        nRequest.addParam("longitude", longitude)
                .addParam("latitude", latitude)
                .addParam("wifi_mac", wifi_mac);
        ApiOkHttp.postAsync(nRequest, callback);
    }

    /**
     * 签到或者签退
     * @param type 1: 签到; 2: 签退
     * @param longitude 当前用户经度
     * @param latitude 当前用户纬度
     * @param wifi_mac wifi mac地址
     * @param callback 接口回调
     */
    public static void signOrSignOut(int type, String longitude, String latitude, String wifi_mac, RequestCallback<SignOrSingOut> callback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/attendanceCtr/save");
        JsonParameterApiRequest<SignOrSingOut> nRequest = new JsonParameterApiRequest<SignOrSingOut>(url, SignOrSingOut.class);
        nRequest.addParam("attendance_type", type)
                .addParam("longitude", longitude)
                .addParam("latitude", latitude)
                .addParam("wifi_mac", wifi_mac)
                .addParam("version", RemoteUtil.getVersion());
        ApiOkHttp.postAsync(nRequest, callback);
    }

    /**
     * 获取考勤记录信息
     */
    public static void getAttendanceRecord(long user_id, long pageNo, long start_time, long end_time, RequestCallback<List<OaAttendanceRecord>> callback){
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/attendanceReportingCtr/listWithPage");
        JsonParameterApiRequest<List<OaAttendanceRecord>> nRequest = new JsonParameterApiRequest<List<OaAttendanceRecord>>(url, List.class);
        nRequest.addParam("user_id", user_id);
        nRequest.addParam("pageNo",pageNo).addParam("start_time",start_time).addParam("end_time",end_time);
        ApiOkHttp.postAsync(nRequest, callback);
    }

    /**
     * 获取请假类型列表
     * @param callback 接口回调
     */
    public static void getClockType(RequestCallback<List<ClockType>> callback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/vacationCtr/listWithPage");
        JsonParameterApiRequest<List<ClockType>> nRequest = new JsonParameterApiRequest<List<ClockType>>(url, List.class);
        nRequest.addParam("pageNo", 0)
                .addParam("status", 1);
        ApiOkHttp.postAsync(nRequest, callback);
    }

    /**
     * 获取当前用户当前假期类型剩余的时间
     * @param id 假期条目ID
     * @param callback 接口回调
     */
    public static void getRemainingTime(long id, RequestCallback<ExternalPackage> callback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/askForLeaveCtr/getFirstLine");
        JsonParameterApiRequest<ExternalPackage> nRequest = new JsonParameterApiRequest<ExternalPackage>(url, ExternalPackage.class);
        nRequest.addParam("vacation_id", id);
        ApiOkHttp.postAsync(nRequest, callback);
    }

    /**
     * 获取OA通用配置文件
     * @param callback 接口回调
     */
    public static void getOaConfig(RequestCallback<OaConfig> callback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/oaConfigCtr/getInfoByAgencyId");
        JsonParameterApiRequest<OaConfig> nRequest = new JsonParameterApiRequest<OaConfig>(url, OaConfig.class);
        ApiOkHttp.postAsync(nRequest, callback);
    }

    /**
     * 获取申请信息详情(申请详情人信息, 审批人信息)
     * @param id 信息条目ID
     * @param callback 接口回调
     */
    public static void getApplyDetails(long id, RequestCallback<ApplyDetails> callback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/askForLeaveCtr/getInfoById");
        JsonParameterApiRequest<ApplyDetails> nRequest = new JsonParameterApiRequest<ApplyDetails>(url, ApplyDetails.class);
        nRequest.addParam("id", id);
        ApiOkHttp.postAsync(nRequest, callback);
    }

    /**
     * 获取申请人信息详情(申请详情人信息)
     * @param id 信息条目ID
     * @param callback 接口回调
     */
    public static void getApplyDetailsApplicant(long id, RequestCallback<ClockType> callback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/vacationCtr/getInfoById");
        JsonParameterApiRequest<ClockType> nRequest = new JsonParameterApiRequest<ClockType>(url, ClockType.class);
        nRequest.addParam("id", id);
        ApiOkHttp.postAsync(nRequest, callback);
    }

    /**
     * 获取申请信息详情(申请详情人信息, 审批人信息)
     * @param id 信息条目ID
     * @param callback 接口回调
     */
    public static void getApplyDetailsApprover(long id, RequestCallback<ApprovalInformation> callback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/askForLeaveVerifyCtr/getInfoById");
        JsonParameterApiRequest<ApprovalInformation> nRequest = new JsonParameterApiRequest<ApprovalInformation>(url, ApprovalInformation.class);
        nRequest.addParam("id", id);
        ApiOkHttp.postAsync(nRequest, callback);
    }

    /**
     * 提交请假申请
     * @param start 开始时间
     * @param end 结束时间
     * @param id 假期类型ID
     * @param name 假期类型名称
     * @param remark 备注
     * @param approverId 下一级审批人ID
     * @param callback 接口回调
     */
    public static void confirmApplyApprover(long start, long end, long id, String name
            , String remark, long approverId, RequestCallback<String> callback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/askForLeaveCtr/save");
        JsonParameterApiRequest<String> nRequest = new JsonParameterApiRequest<String>(url, String.class);
        nRequest.addParam("start_time", start)
                .addParam("end_time", end)
                .addParam("oa_vacation_id", id)
                .addParam("oa_vacation_name", name)
                .addParam("remark", remark)
                .addParam("verify_user_id", approverId);
        ApiOkHttp.postAsync(nRequest, callback);
    }

    /**
     * 撤销请假
     * @param id 请假条目ID
     * @param callback 接口回调
     */
    public static void revokeApplyApprover(long id, RequestCallback<String> callback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/askForLeaveCtr/back");
        JsonParameterApiRequest<String> nRequest = new JsonParameterApiRequest<String>(url, String.class);
        nRequest.addParam("id", id);
        ApiOkHttp.postAsync(nRequest, callback);
    }

    /**
     * 驳回或者同意请假
     * @param id 请假条目ID
     * @param approverId 当前审批人条目信息ID
     * @param time 请假时长
     * @param stop 是否停止
     * @param status 状态 驳回或者同意
     * @param leave 当前审批人审批级别
     * @param remark 当前驳回信息备注
     * @param nextApproverId 下一级审批人ID
     * @param callback 接口回调
     */
    public static void rejectOrAgreeApplyApprover(long id, long approverId, int time, int stop
            , int status, int leave, String remark, long nextApproverId, RequestCallback<String> callback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/askForLeaveVerifyCtr/save");
        JsonParameterApiRequest<String> nRequest = new JsonParameterApiRequest<String>(url, String.class);
        nRequest.addParam("id", approverId)
                .addParam("oa_ask_for_leave_id", id)
                .addParam("duration_hour", time)
                .addParam("stop", stop)
                .addParam("status", status)
                .addParam("level", leave)
                .addParam("remark", remark)
                .addParam("next_verify_user_id", nextApproverId);
        ApiOkHttp.postAsync(nRequest, callback);
    }
    public static void getStuffPlanList(long start_time,long end_ime,List<Long> ids,RequestCallback<List<StuffPlanList>> callback){
        String url=getUrl("lefuyun/oaStaffPlanCtr/listWithPage");
        JsonParameterApiRequest<List<StuffPlanList>> nRequest = new JsonParameterApiRequest<List<StuffPlanList>>(url, List.class);
        nRequest.addParam("start_time",start_time).addParam("end_time",end_ime).addParam("user_ids",ids);
        ApiOkHttp.postAsync(nRequest,callback);
    }
}
