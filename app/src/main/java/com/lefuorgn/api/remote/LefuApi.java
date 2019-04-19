package com.lefuorgn.api.remote;

import com.lefuorgn.api.ApiOkHttp;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.request.FileApiRequest;
import com.lefuorgn.api.http.request.JsonApiRequest;
import com.lefuorgn.api.http.request.JsonParameterApiRequest;
import com.lefuorgn.api.remote.common.RemoteUtil;
import com.lefuorgn.bean.User;
import com.lefuorgn.bean.Version;
import com.lefuorgn.db.model.basic.AllocatingTypeTask;
import com.lefuorgn.lefu.bean.AlarmEntry;
import com.lefuorgn.lefu.bean.AlarmEntryDetails;
import com.lefuorgn.lefu.bean.AllocatingTask;
import com.lefuorgn.lefu.bean.AllocatingTaskProgress;
import com.lefuorgn.lefu.bean.Birthday;
import com.lefuorgn.lefu.bean.BirthdayDetails;
import com.lefuorgn.lefu.bean.ContactPublic;
import com.lefuorgn.lefu.bean.Department;
import com.lefuorgn.lefu.bean.DeviceElderly;
import com.lefuorgn.lefu.bean.ElectronicFence;
import com.lefuorgn.lefu.bean.ElectronicFenceItem;
import com.lefuorgn.lefu.bean.Estimate;
import com.lefuorgn.lefu.bean.EstimateItem;
import com.lefuorgn.lefu.bean.Event;
import com.lefuorgn.lefu.bean.GroupOldPeople;
import com.lefuorgn.lefu.bean.Leave;
import com.lefuorgn.lefu.bean.Logbook;
import com.lefuorgn.lefu.bean.LogbookGroup;
import com.lefuorgn.lefu.bean.LogbookType;
import com.lefuorgn.lefu.bean.MedicalHistory;
import com.lefuorgn.lefu.bean.NewTrajectory;
import com.lefuorgn.lefu.bean.SignDataAudit;
import com.lefuorgn.lefu.bean.SignGraphicData;
import com.lefuorgn.lefu.bean.Staff;

import java.io.File;
import java.util.List;

public class LefuApi {

    /**
     * 登录接口端
     */
	public static void login(String userName, String password, RequestCallback<User> requestCallback) {
        JsonApiRequest<User> nRequest = new JsonApiRequest<User>(RemoteUtil.getLoginUrl(), User.class);
        nRequest.addParam("version", RemoteUtil.getVersion())
                .addParam("extInfo", true)
                .addParam("user_name", userName)
                .addParam("password", password)
                .addParam("loginIMEI", RemoteUtil.getDeviceIMEI());
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 判断当前用户是否登录(sessionID是否有效)
     * @param requestCallback 接口回调
     */
    public static void isLogin(RequestCallback<String> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/userInfoCtr/isLogin");
        JsonApiRequest<String> nRequest = new JsonApiRequest<String>(url, String.class);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 判断当前号码是否已经注册
     * @param mobile 需校验的手机号
     * @param requestCallback 接口回调
     */
    public static void isRegister(String mobile, RequestCallback<User> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/userInfoCtr/queryUserInfoByMobile");
        JsonApiRequest<User> request = new JsonApiRequest<User>(url, User.class);
        request.addParam("mobile", mobile);
        ApiOkHttp.postAsync(request, requestCallback);
    }

    /**
     * 获取用户登录或注册验证码
     * @param mobile 用户登录或注册手机号
     * @param requestCallback 接口回调
     */
    public static void getMobileCode(String mobile, RequestCallback<String> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/userInfoCtr/getMobileCode");
        JsonApiRequest<String> request = new JsonApiRequest<String>(url, String.class);
        request.addParam("mobile", mobile);
        ApiOkHttp.postAsync(request, requestCallback);
    }

    /**
     * 重置密码
     * @param mobile 手机号码
     * @param password 新密码
     * @param code 验证码
     * @param requestCallback 接口回调
     */
    public static void resetPassword(String mobile, String password, String code,
                                     RequestCallback<String> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/userInfoCtr/mobileResetPasswordByMobileCode");
        JsonApiRequest<String> request = new JsonApiRequest<String>(url, String.class);
        request.addParam("mobile", mobile)
                .addParam("password", password)
                .addParam("mobileCode", code);
        ApiOkHttp.postAsync(request, requestCallback);

    }

    /**
     * 更新用户信息, 姓名和邮箱
     * @param name 要修改的姓名
     * @param email 要修改的邮箱
     * @param user 用户原有信息
     * @param requestCallback 接口回调
     */
    public static void updateUserInfo(String name, String email, User user, RequestCallback<String> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/orgUserCtr/updateUserAndSocietyPeople");
        JsonApiRequest<String> nRequest = new JsonApiRequest<String>(url, String.class);
        nRequest.addParam("user_name", name)
                .addParam("name", " ")
                .addParam("mobile", user.getMobile())
                .addParam("mailbox", email)
                .addParam("user_id", user.getUser_id())
                .addParam("agency_id", user.getAgency_id())
                .addParam("user_type", 9);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 修改当前用户密码
     * @param oldPsw 原密码
     * @param newPsw 新密码
     * @param requestCallback 接口回调
     */
    public static void updatePassword(String oldPsw, String newPsw, RequestCallback<String> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/orgUserCtr/updatePassword");
        JsonApiRequest<String> nRequest = new JsonApiRequest<String>(url, String.class);
        nRequest.addParam("oldPass", oldPsw)
                .addParam("newPass", newPsw)
                .addParam("version", RemoteUtil.getVersion());
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 获取App最新版本
     * @param requestCallback 接口回调
     */
    public static void checkUpdate(RequestCallback<Version> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/update");
        JsonApiRequest<Version> nRequest = new JsonApiRequest<Version>(url, Version.class);
        nRequest.addParam("token", "1e4501b41a");
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 文件下载
     * @param url 文件下载接口
     * @param filePath 文件保存目录
     * @param requestCallback 接口回调
     */
    public static void downloadApp(String url, String filePath, RequestCallback<File> requestCallback) {
        FileApiRequest request = new FileApiRequest(url, filePath);
        ApiOkHttp.postAsync(request, requestCallback);
    }

    /**
     * 多媒体文件下载
     * @param url 文件下载接口
     * @param filePath 文件保存目录
     * @param requestCallback 接口回调
     */
    public static void downloadMultiMedia(String url, String filePath, RequestCallback<File> requestCallback) {
        FileApiRequest request = new FileApiRequest(url, filePath);
        ApiOkHttp.getAsync(request, requestCallback);
    }

    /**
     * 获取院方活动信息
     * @param agencyId 机构ID
     * @param pageNo 当前页面
     * @param requestCallback 接口回调
     */
    public static void getEvent(long agencyId, int pageNo, RequestCallback<List<Event>> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/agencyActivites/toActivitesListWithArea");
        JsonApiRequest<List<Event>> nRequest = new JsonApiRequest<List<Event>>(url, List.class);
        nRequest.addParam("agencyId", agencyId)
                .addParam("pageNo", pageNo);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 获取联系页面公用联系人信息
     * @param pageNo 当前页面
     * @param requestCallback 接口回调
     */
    public static void getContactPublic(int pageNo, RequestCallback<List<ContactPublic>> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/contactsCtr/queryContactsList");
        JsonApiRequest<List<ContactPublic>> nRequest = new JsonApiRequest<List<ContactPublic>>(url, List.class);
        nRequest.addParam("pageNo", pageNo);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 获取生日提醒详细信息
     * @param pageNo 当前页面
     * @param requestCallback 接口回调
     */
    public static void getBirthday(int pageNo, RequestCallback<List<Birthday>> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/birthdayCtr/queryBirthdayListByAgencyId");
        JsonApiRequest<List<Birthday>> nRequest = new JsonApiRequest<List<Birthday>>(url, List.class);
        nRequest.addParam("pageNo", pageNo);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 获取某月老人生日详细信息
     * @param pageNo 当前页面
     * @param mouth 具体的月份
     * @param requestCallback 接口回调
     */
    public static void getBirthdayDetails(int pageNo, String mouth,
                                          RequestCallback<List<BirthdayDetails>> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/birthdayCtr/queryBirthdayListByMonth");
        JsonApiRequest<List<BirthdayDetails>> nRequest = new JsonApiRequest<List<BirthdayDetails>>(url, List.class);
        nRequest.addParam("pageNo", pageNo)
                .addParam("month", mouth);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 获取当前机构所有员工信息
     * @param agencyId 当前机构ID
     * @param requestCallback 接口回调
     */
    public static void getStaffs(long agencyId, RequestCallback<List<Staff>> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/staffCtr/queryStaffListByAgencyId");
        JsonApiRequest<List<Staff>> nRequest = new JsonApiRequest<List<Staff>>(url, List.class);
        nRequest.addParam("agency_id", agencyId)
                .addParam("pageNo", 0)
                .addParam("staff_name", "");
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 获取当前机构所有部门信息
     * @param requestCallback 接口回调
     */
    public static void getDepartments(RequestCallback<List<Department>> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/departmentCtr/getDeptList");
        JsonApiRequest<List<Department>> nRequest = new JsonApiRequest<List<Department>>(url, List.class);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 获取某一部门下的所有员工
     * @param requestCallback 接口回调
     */
    public static void getStaffOnDepartment(long depId, RequestCallback<List<Staff>> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/staffCtr/queryStaffListByAgencyIdAndDeptId");
        JsonApiRequest<List<Staff>> nRequest = new JsonApiRequest<List<Staff>>(url, List.class);
        nRequest.addParam("pageNo", 0)
                .addParam("dept_id", depId);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 获取老人病史信息
     * @param requestCallback 接口回调
     */
    public static void getMedicalHistoryInfo(long oldPeopleId
            , RequestCallback<List<MedicalHistory>> requestCallback) {
        String url = RemoteUtil
                .getAbsoluteApiUrl("lefuyun/healthProfileCtr/queryHealthProfileListByOldPeopleId");
        JsonApiRequest<List<MedicalHistory>> nRequest = new JsonApiRequest<List<MedicalHistory>>(url, List.class);
        nRequest.addParam("uid", oldPeopleId);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 获取指定老人的交班记录信息
     * @param oldPeopleId 老人ID
     * @param pageNo 当前页面
     * @param requestCallback 接口回调
     */
    public static void getLogbookInfo(long oldPeopleId, int pageNo
            , RequestCallback<List<Logbook>> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/handOverCtr/getHandOverRecordWithPage");
        JsonApiRequest<List<Logbook>> nRequest = new JsonApiRequest<List<Logbook>>(url, List.class);
        nRequest.addParam("old_people_id", oldPeopleId).addParam("version",160427)
                .addParam("pageNo", pageNo);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 获取指定老人的交班记录信息
     * @param groupId 分组ID
     * @param pageNo 当前页面
     * @param type 交班类型
     * @param requestCallback 接口回调
     */
    public static void getLogbookInfo(long groupId, int pageNo, long type
            , RequestCallback<List<Logbook>> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/handOverCtr/getHandOverRecordWithPage");
        JsonApiRequest<List<Logbook>> nRequest = new JsonApiRequest<List<Logbook>>(url, List.class);
        nRequest.addParam("group_id", groupId)
                .addParam("version",160427)
                .addParam("pageNo", pageNo)
                .addParam("care_type", type);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 提交指定老人的交班记录信息
     * @param id 老人ID
     * @param name 老人姓名
     * @param content 备注
     * @param type 类别
     * @param user 当前用户信息
     * @param requestCallback 接口回调
     */
    public static void submitLogbookInfo(long id, String name, String content, long type
            , User user, RequestCallback<String> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/handOverCtr/addHandOver");
        JsonApiRequest<String> nRequest = new JsonApiRequest<String>(url, String.class);
        nRequest.addParam("old_people_id", id)
                .addParam("version",160427)
                .addParam("old_people_name", name)
                .addParam("content", content)
                .addParam("care_type", type)
                .addParam("inspect_time", System.currentTimeMillis())
                .addParam("user_name", user.getUser_name())
                .addParam("staff_id", user.getUser_id());
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 获取交班记录分组信息
     * @param requestCallback 接口回调
     */
    public static void getLogbookGroup(RequestCallback<List<LogbookGroup>> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/olderGroup/getOlderGroupByAgency");
        JsonApiRequest<List<LogbookGroup>> nRequest = new JsonApiRequest<List<LogbookGroup>>(url, List.class);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 获取交班记录分组信息
     * @param requestCallback 接口回调
     */
    public static void getLogbookOldPeople(long groupId
            , RequestCallback<List<GroupOldPeople>> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/olderGroup/getOldersByGroupId");
        JsonApiRequest<List<GroupOldPeople>> nRequest = new JsonApiRequest<List<GroupOldPeople>>(url, List.class);
        nRequest.addParam("group_id", groupId);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 根据请假类型获取请假列表
     * @param leaveState 请假类型; 1: 当前未销假; 2: 当前请假已经完成
     * @param pageNo 当前页面
     * @param name 当前老人姓名
     * @param hasName 是否按指定的老人进行搜索
     * @param requestCallback 接口回调
     */
    public static void getLeave(int leaveState, int pageNo, String name
            , boolean hasName, RequestCallback<List<Leave>> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/leaveOutCtr/queryLeaveOutListByAgencyId");
        JsonApiRequest<List<Leave>> nRequest = new JsonApiRequest<List<Leave>>(url, List.class);
        nRequest.addParam("leave_state", leaveState)
                .addParam("pageNo", pageNo);
        if(hasName) {
            nRequest.addParam("elderly_name", name);
        }
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 销假请求
     * @param id 当前请假条目ID
     * @param time 指定的销假日期
     * @param user 当前操作销假的员工
     * @param requestCallback 接口回调
     */
    public static void spendLeave(long id, long time, User user, RequestCallback<String> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/leaveOutCtr/spendLeaveOutById");
        JsonApiRequest<String> nRequest = new JsonApiRequest<String>(url, String.class);
        nRequest.addParam("id", id)
                .addParam("real_return_dt", time)
                .addParam("return_signature_id", user.getUser_id())
                .addParam("return_attn_signature", user.getUser_name());
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 修改请假内容
     * @param leave 当前请假要修改的内容
     * @param requestCallback 接口回调
     */
    public static void updateLeave(Leave leave, RequestCallback<String> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/leaveOutCtr/updateLeaveOut");
        JsonApiRequest<String> nRequest = new JsonApiRequest<String>(url, String.class);
        nRequest.addParam("id", leave.getId())
                .addParam("old_people_id", leave.getOld_people_id())
                .addParam("leave_hospital_dt", leave.getLeave_hospital_dt())
                .addParam("expected_return_dt", leave.getExpected_return_dt())
                .addParam("party_signature", leave.getParty_signature())
                .addParam("leave_reason", leave.getLeave_reason())
                .addParam("notes_matters", leave.getNotes_matters())
                .addParam("signature_id", leave.getSignature_id())
                .addParam("attn_signature", leave.getAttn_signature());
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 添加一条请假信息
     * @param leave 当前请假条目
     * @param requestCallback 接口回调
     */
    public static void addLeave(Leave leave, RequestCallback<String> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/leaveOutCtr/addLeaveOut");
        JsonApiRequest<String> nRequest = new JsonApiRequest<String>(url, String.class);
        nRequest.addParam("old_people_id", leave.getOld_people_id())
                .addParam("leave_hospital_dt", leave.getLeave_hospital_dt())
                .addParam("expected_return_dt", leave.getExpected_return_dt())
                .addParam("party_signature", leave.getParty_signature())
                .addParam("leave_reason", leave.getLeave_reason())
                .addParam("notes_matters", leave.getNotes_matters())
                .addParam("signature_id", leave.getSignature_id())
                .addParam("attn_signature", leave.getAttn_signature());
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 加载要审核的数据
     * @param pageNo 当前页号
     * @param uri 当前数据的接口
     * @param requestCallback 接口回调
     */
    public static void getDataAudit(int pageNo, String uri, RequestCallback<List<SignDataAudit>> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/singndata/verify/" + uri);
        JsonApiRequest<List<SignDataAudit>> nRequest = new JsonApiRequest<List<SignDataAudit>>(url, List.class);
        nRequest.addParam("pageNo", pageNo);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 提交数据审核
     * @param uri 当前数据接口小尾巴
     * @param jsonId 封装成json的id集合
     * @param status 当前审批状态 1: 通过; 2: 不允许通过
     * @param remark 备注
     * @param requestCallback 接口回调
     */
    public static void submitDataAudit(String uri, String jsonId, int status,
                                       String remark, RequestCallback<String> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/singndata/batchPass" + uri);
        JsonApiRequest<String> nRequest = new JsonApiRequest<String>(url, String.class);
        nRequest.addParam("idStr", jsonId)
                .addParam("status", status)
                .addParam("remark", remark);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 获取评估表条目
     * @param agencyId 机构ID
     * @param requestCallback 接口回调
     */
    public static void getEstimateItem(long agencyId, RequestCallback<List<EstimateItem>> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/examinationPaperCtr/listAll");
        JsonApiRequest<List<EstimateItem>> nRequest = new JsonApiRequest<List<EstimateItem>>(url, List.class);
        nRequest.addParam("agency_id", agencyId)
                .addParam("version", RemoteUtil.getVersion());
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 获取评估表条目
     * @param agencyId 机构ID
     * @param id 评估表ID
     * @param requestCallback 接口回调
     */
    public static void getEstimate(long agencyId, long id, RequestCallback<Estimate> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/examinationPaperCtr/getInfoById");
        JsonApiRequest<Estimate> nRequest = new JsonApiRequest<Estimate>(url, Estimate.class);
        nRequest.addParam("agency_id", agencyId)
                .addParam("id", id);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 提交当前申请表
     * @param estimate 申请基本内容
     * @param json 选项内容
     * @param sum 总分数
     * @param requestCallback 接口回调
     */
    public static void saveEstimate(Estimate estimate, String json, int sum, RequestCallback<String> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/examAnswerCtr/addExamAnswer");
        JsonApiRequest<String> nRequest = new JsonApiRequest<String>(url, String.class);
        nRequest.addParam("examination_paper_id", estimate.getId())
                .addParam("title", estimate.getTitle())
                .addParam("old_people_card_number", estimate.getOld_people_card_number())
                .addParam("old_people_id", estimate.getOld_people_id())
                .addParam("old_people_name", estimate.getOld_people_name())
                .addParam("old_people_card_number", estimate.getOld_people_card_number())
                .addParam("content", json)
                .addParam("sum", sum);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 获取评估表条目
     * @param agencyId 机构ID
     * @param id 评估表ID
     * @param oldPeopleId 老人ID
     * @param requestCallback 接口回调
     */
    public static void getEstimateList(long agencyId, long id, long oldPeopleId, RequestCallback<List<Estimate>> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/examAnswerCtr/queryExamAnswersByExamAnswerBean");
        JsonApiRequest<List<Estimate>> nRequest = new JsonApiRequest<List<Estimate>>(url, List.class);
        nRequest.addParam("agency_id", agencyId)
                .addParam("examination_paper_id", id)
                .addParam("old_people_id", oldPeopleId);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 修改当前申请表
     * @param estimate 申请基本内容
     * @param json 选项内容
     * @param sum 总分数
     * @param requestCallback 接口回调
     */
    public static void updateEstimate(Estimate estimate, String json, int sum, RequestCallback<String> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/examAnswerCtr/updateExamAnswer");
        JsonApiRequest<String> nRequest = new JsonApiRequest<String>(url, String.class);
        nRequest.addParam("examination_paper_id", estimate.getExamination_paper_id())
                .addParam("title", estimate.getTitle())
                .addParam("id", estimate.getId())
                .addParam("old_people_card_number", estimate.getOld_people_card_number())
                .addParam("old_people_id", estimate.getOld_people_id())
                .addParam("old_people_name", estimate.getOld_people_name())
                .addParam("old_people_card_number", estimate.getOld_people_card_number())
                .addParam("desc", estimate.getDesc())
                .addParam("content", json)
                .addParam("sum", sum);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 创建新的配单或者重置配单
     * @param time 配单任务时间
     * @param userId 当前用户ID
     * @param requestCallback 接口回调
     */
    public static void createAllocatingTask(long time, long userId, RequestCallback<String> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/nursingTaskCtr/sendOrders");
        JsonApiRequest<String> nRequest = new JsonApiRequest<String>(url, String.class);
        nRequest.addParam("order_time", time)
                .addParam("head_nurse_id", userId);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 获取配单任务
     * @param pageNo 当前页面
     * @param userId 当前用户ID
     * @param requestCallback 接口回调
     */
    public static void getAllocatingTask(int pageNo, long userId,
                                             RequestCallback<List<AllocatingTask>> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/nursingTaskRecordCtr/queryNursingTaskRecordList");
        JsonApiRequest<List<AllocatingTask>> nRequest = new JsonApiRequest<List<AllocatingTask>>(url, List.class);
        nRequest.addParam("pageNo", pageNo)
                .addParam("task_state", -1)
                .addParam("head_nurse_id", userId);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    public static void getHandOverTypeList(RequestCallback<List<LogbookType>> requestCallback){
        String url=RemoteUtil.getAbsoluteApiUrl("lefuyun/dictCtr/queryDictionariesList");
        JsonApiRequest<List<LogbookType>> mRequest = new JsonApiRequest<List<LogbookType>>(url,List.class);
        mRequest.addParam("dictionaries_code","hand_over");
        ApiOkHttp.postAsync(mRequest,requestCallback);
    }

    /**
     * 获取当天的配单
     * @param time 当前时间
     * @param userId 当前用户ID
     * @param requestCallback 接口回调
     */
    public static void getSameDayAllocatingTask(
            long time, long userId, RequestCallback<AllocatingTask> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/nursingTaskRecordCtr/queryNursingTaskRecord");
        JsonApiRequest<AllocatingTask> nRequest = new JsonApiRequest<AllocatingTask>(url, AllocatingTask.class);
        nRequest.addParam("task_time", time)
                .addParam("head_nurse_id", userId);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 发布任务
     * @param time 任务时间
     * @param userId 当前用户ID
     * @param requestCallback 接口回调
     */
    public static void releaseAllocatingTask(long time, long userId, RequestCallback<String> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/nursingTaskCtr/sendTasks");
        JsonApiRequest<String> nRequest = new JsonApiRequest<String>(url, String.class);
        nRequest.addParam("task_time", time)
                .addParam("head_nurse_id", userId);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 获取当天的配单
     * @param id 配单ID
     * @param requestCallback 接口回调
     */
    public static void getAllocatingTaskProgress(
            long id, RequestCallback<List<AllocatingTaskProgress>> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/nursingTaskCtr/queryNursingItemProgress");
        JsonApiRequest<List<AllocatingTaskProgress>> nRequest
                = new JsonApiRequest<List<AllocatingTaskProgress>>(url, List.class);
        nRequest.addParam("id", id);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 将发布的配单置为未编辑状态
     * @param time 当前时间
     * @param userId 当前用户ID
     * @param requestCallback 接口回调
     */
    public static void editAllocatingTaskProgress(long time, long userId, RequestCallback<String> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/nursingTaskRecordCtr/editNursingTaskRecord");
        JsonApiRequest<String> nRequest = new JsonApiRequest<String>(url, String.class);
        nRequest.addParam("head_nurse_id", userId)
                .addParam("task_time", time);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 获取指定护理项下所有老人的指定时间的配单情况
     * @param pageNo 当前页号
     * @param userId 当前用户ID
     * @param typeId 当前护理项类型ID
     * @param time 当前配单时间
     * @param requestCallback 接口回调
     */
    public static void getAllocatingTypeTask(int pageNo, long userId, long typeId, long time,
                                             RequestCallback<List<AllocatingTypeTask>> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/nursingTaskCtr/queryNursingTaskList");
        JsonApiRequest<List<AllocatingTypeTask>> nRequest =
                new JsonApiRequest<List<AllocatingTypeTask>>(url, List.class);
        nRequest.addParam("pageNo", pageNo)
                .addParam("head_nurse_id", userId)
                .addParam("nursing_item_id", typeId)
                .addParam("task_time", time)
                .addParam("task_state", 0);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 获取指定护理项下所有老人的指定时间的配单情况
     * @param oldPeopleId 老人ID
     * @param time 当前配单时间
     * @param requestCallback 接口回调
     */
    public static void getAllocatingTypeTask(long oldPeopleId, long time,
                                             RequestCallback<List<AllocatingTypeTask>> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/nursingTaskCtr/queryNursingTaskList");
        JsonApiRequest<List<AllocatingTypeTask>> nRequest =
                new JsonApiRequest<List<AllocatingTypeTask>>(url, List.class);
        nRequest.addParam("old_people_id", oldPeopleId)
                .addParam("task_time", time);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 获取指定护理项下所有老人的指定时间的配单情况
     * @param pageNo 当前数据页号
     * @param id 护理项ID
     * @param time 当前配单时间
     * @param type 数据类型
     * @param userId 用户ID
     * @param requestCallback 接口回调
     */
    public static void getAllocatingTypeTask(int pageNo, long id, long time, int type, long userId,
                                             RequestCallback<List<AllocatingTypeTask>> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/nursingTaskCtr/queryNursingTaskList");
        JsonApiRequest<List<AllocatingTypeTask>> nRequest =
                new JsonApiRequest<List<AllocatingTypeTask>>(url, List.class);
        nRequest.addParam("nursing_item_id", id)
                .addParam("task_time", time)
                .addParam("task_state", type)
                .addParam("pageNo", pageNo)
                .addParam("head_nurse_id", userId);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 修改未编辑状态的配单
     * @param json 配单json格式数据
     * @param requestCallback 接口回调
     */
    public static void editAllocatingTypeTask(String json, RequestCallback<String> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/nursingTaskCtr/addOrDelOrUpNursingTask");
        JsonApiRequest<String> nRequest = new JsonApiRequest<String>(url, String.class);
        nRequest.addParam("tbs", json);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 删除某一个配单条目
     * @param id 要删除配单条目的ID
     * @param requestCallback 接口回调
     */
    public static void deleteAllocatingTypeTask(long id, RequestCallback<String> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/nursingTaskCtr/delNursingTask");
        JsonApiRequest<String> nRequest = new JsonApiRequest<String>(url, String.class);
        nRequest.addParam("id", id);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 获取配单数据
     * @param userId 当前用户ID
     * @param time 当前时间
     * @param state 配单任务状态; 1: 未接单; 2: 已接单
     * @param pageNo 当前页面页号
     * @param requestCallback 接口回调
     */
    public static void getOrderTakingNursing(long userId, long time, int state, int pageNo
            , RequestCallback<String> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/nursingTaskCtr/queryCareWorkerTaskList");
        JsonApiRequest<String> nRequest = new JsonApiRequest<String>(url, String.class);
        nRequest.addParam("care_worker", userId)
                .addParam("task_time", time)
                .addParam("task_state", state)
                .addParam("pageNo", pageNo);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 接收指定的配单信息
     * @param userId 当前用户ID
     * @param ids 配单id集合
     * @param requestCallback 接口回调
     */
    public static void receiveAllocatingTypeTask(long userId, String ids
            , RequestCallback<List<AllocatingTypeTask>> requestCallback) {
        long time = System.currentTimeMillis();
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/nursingTaskCtr/receiveOrders");
        JsonApiRequest<List<AllocatingTypeTask>> nRequest =
                new JsonApiRequest<List<AllocatingTypeTask>>(url, List.class);
        nRequest.addParam("ids", ids)
                .addParam("task_time", time)
                .addParam("task_state", 2)
                .addParam("care_worker", userId)
                .addParam("update_time", time);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 提交配单内容
     * @param json 配单json格式数据
     * @param requestCallback 接口回调
     */
    public static void submitAllocatingTypeTask(String json, RequestCallback<String> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/nursingTaskCtr/commitTasks");
        JsonApiRequest<String> nRequest = new JsonApiRequest<String>(url, String.class);
        nRequest.addParam("tbs", json);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     *
     * @param uri 请求路径uri
     * @param id 老人ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param type 1: 按日查询; 2: 按周查询; 3: 按月查询; 4: 按年查询
     * @param order 方向 1: 上一个月 2: 下一个月
     * @param requestCallback 接口回调
     */
    public static void getSignGraphicData(String uri, long id, long startTime, long endTime
            , int type, int order, RequestCallback<List<SignGraphicData>> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl(uri);
        JsonApiRequest<List<SignGraphicData>> nRequest = new JsonApiRequest<List<SignGraphicData>>(url, List.class);
        nRequest.addParam("old_people_id", id)
                .addParam("beginTime", order == 1 ? startTime : endTime)
                .addParam("type", type)
                .addParam("order", order);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 获取拥有设备的老人信息
     * @param requestCallback 接口回调
     */
    public static void getElderlyHasDevice(RequestCallback<List<DeviceElderly>> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/watchOlderPositionCtr/listWithPage");
        JsonParameterApiRequest<List<DeviceElderly>> nRequest = new JsonParameterApiRequest<List<DeviceElderly>>(url, List.class);
        nRequest.addParam("pageNo", 0);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 根据拥有设备的老人ID查找安全区域
     * @param id 老人ID
     * @param requestCallback 接口回调
     */
    public static void getElectronicFence(long id, RequestCallback<ElectronicFence> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/watchOlderPositionCtr/getSafeAreaByOlderId");
        JsonParameterApiRequest<ElectronicFence> nRequest = new JsonParameterApiRequest<ElectronicFence>(url, ElectronicFence.class);
        nRequest.addParam("older_id", id);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 修改设备的安全区域
     *
     * @param deviceId 设备ID
     * @param saveAreaId 区域ID
     */
    public static void updateElectronicFence(long deviceId, long saveAreaId, RequestCallback<String> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/watchDeviceOlderMapCtr/update");
        JsonParameterApiRequest<String> nRequest = new JsonParameterApiRequest<String>(url, String.class);
        nRequest.addParam("device_id", deviceId)
                .addParam("safe_area_id", saveAreaId);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 获取安全区域列表
     * @param pageNo 当前页号
     * @param requestCallback 接口回调
     */
    public static void getElectronicFences(int pageNo, RequestCallback<List<ElectronicFenceItem>> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/watchSafeAreaCtr/listWithPage");
        JsonParameterApiRequest<List<ElectronicFenceItem>> nRequest = new JsonParameterApiRequest<List<ElectronicFenceItem>>(url, List.class);
        nRequest.addParam("pageNo", pageNo);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 根据拥有设备的老人ID查找安全区域
     * @param id 老人ID
     * @param startTime 某天的开始时间
     * @param endTime 某天的结束时间
     * @param requestCallback 接口回调
     */
    public static void getTrajectoryPoints(long id, long startTime, long endTime, RequestCallback<ElectronicFence> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/watchOlderPositionCtr/getListPositionByOlderId");
        JsonParameterApiRequest<ElectronicFence> nRequest = new JsonParameterApiRequest<ElectronicFence>(url, ElectronicFence.class);
        nRequest.addParam("older_id", id)
                .addParam("create_time", startTime)
                .addParam("update_time", endTime);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 发送位置更新命令
     * @param imei 设备编号
     * @param requestCallback 接口回调
     */
    public static void sendLocationUpdateCommand(String imei, RequestCallback<String> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/watchOlderPositionCtr/getrefreshPosition");
        JsonParameterApiRequest<String> nRequest = new JsonParameterApiRequest<String>(url, String.class);
        nRequest.addParam("imei", imei);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 配合{@link #sendLocationUpdateCommand(String, RequestCallback)}使用,
     * 当其申请成功隔3秒以上时间再次调用此接口,获取当前最新的位置信息
     * @param imei 设备编号
     * @param requestCallback 接口回调
     */
    public static void getNewLocationInfo(String imei, RequestCallback<NewTrajectory> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/watchOlderPositionCtr/refreshPosition");
        JsonParameterApiRequest<NewTrajectory> nRequest = new JsonParameterApiRequest<NewTrajectory>(url, NewTrajectory.class);
        nRequest.addParam("imei", imei);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 获取告警信息列表
     * @param pageNo 页号
     * @param types 类型(SOS数据类型, 电子围栏告警信息)
     * @param status 状态(未处理、处理中、已处理)
     * @param requestCallback 接口回调
     */
    public static void getAlarmDetailsInfo(int pageNo, int types, int status, RequestCallback<List<AlarmEntry>> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/sosWarningCtr/listWithPage");
        JsonParameterApiRequest<List<AlarmEntry>> nRequest = new JsonParameterApiRequest<List<AlarmEntry>>(url, List.class);
        nRequest.addParam("pageNo", pageNo)
                .addParam("types", types)
                .addParam("status", status);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 获取告警信息详细内容
     * @param id 告警信息ID
     * @param agencyId 机构ID
     * @param requestCallback 接口回调
     */
    public static void getAlarmEntryDetails(long id, long agencyId, RequestCallback<AlarmEntryDetails> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/sosWarningCtr/getInfo");
        JsonParameterApiRequest<AlarmEntryDetails> nRequest = new JsonParameterApiRequest<AlarmEntryDetails>(url, AlarmEntryDetails.class);
        nRequest.addParam("id", id);
        if(agencyId > 0) {
            nRequest.addParam("agency_id", agencyId);
        }
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 接收告警信息
     * @param id 告警记录id
     * @param agencyId 机构ID
     * @param requestCallback 接口回调
     */
    public static void receiveAlarmDetails(long id, long agencyId, RequestCallback<String> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/sosWarningCtr/getSosMessage");
        JsonParameterApiRequest<String> nRequest = new JsonParameterApiRequest<String>(url, String.class);
        nRequest.addParam("id", id);
        if(agencyId > 0) {
            nRequest.addParam("agency_id", agencyId);
        }
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

    /**
     * 处理告警信息
     * @param id 告警记录id
     * @param requestCallback 接口回调
     */
    public static void processingAlarmDetails(long id, String remark, RequestCallback<String> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/sosWarningCtr/getFinish");
        JsonParameterApiRequest<String> nRequest = new JsonParameterApiRequest<String>(url, String.class);
        nRequest.addParam("id", id)
                .addParam("remark", remark);
        ApiOkHttp.postAsync(nRequest, requestCallback);
    }

}
