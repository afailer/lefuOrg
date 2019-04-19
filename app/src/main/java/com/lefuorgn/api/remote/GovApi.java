package com.lefuorgn.api.remote;

import com.baidu.mapapi.map.MapStatus;
import com.lefuorgn.api.ApiOkHttp;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.request.JsonApiRequest;
import com.lefuorgn.api.http.request.JsonParameterApiRequest;
import com.lefuorgn.api.remote.common.RemoteUtil;
import com.lefuorgn.gov.bean.GovAgencyMsg;
import com.lefuorgn.gov.bean.GovOrgInfo;
import com.lefuorgn.gov.bean.LeaderNews;
import com.lefuorgn.gov.bean.MedicalMsg;
import com.lefuorgn.gov.bean.MessageInfo;
import com.lefuorgn.gov.bean.NewsType;
import com.lefuorgn.gov.bean.NurseLevel;
import com.lefuorgn.gov.bean.OldMsg;
import com.lefuorgn.gov.bean.OrgActive;
import com.lefuorgn.gov.bean.Organization;
import com.lefuorgn.gov.bean.UseRate;

import java.util.List;

public class GovApi {

    /**
     * 判断当前用户是否拥有跳转到机构版页面的权限
     * @param agencyId 机构ID
     * @param callback 接口回调
     */
    public static void isHasSkipPermission(long agencyId, RequestCallback<String> callback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/staffCtr/changeAgency");
        JsonApiRequest<String> nRequest = new JsonApiRequest<String>(url, String.class);
        nRequest.addParam("agency_id", agencyId);
        ApiOkHttp.postAsync(nRequest, callback);
    }

    /**
     * @param status 阅读状态 1未读，0所有
     * @param pageNo 第几页
     * @param pageSize 列表条数
     * @param callback 已读或未读消息集合
     */
    public static void getNotice(int status,int pageNo,int pageSize,RequestCallback<List<MessageInfo>> callback){
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/leaderNotiUserMapController/listWithPage");
        JsonApiRequest<List<MessageInfo>> nRequest = new JsonApiRequest<List<MessageInfo>>(url, List.class);
        nRequest.addParam("status", status)
                .addParam("pageNo", pageNo).addParam("pageSize",pageSize);
        ApiOkHttp.postAsync(nRequest, callback);
    }

    /**
     * 根据集团账号ID拿到机构活动
     * @param groupId 集团账号ID
     * @param pageNo 当前页面页号
     * @param pageSize 当前页面的最大数量
     * @param callback 接口回调
     */
    public static void getOrgActivities(long groupId, int pageNo,int pageSize,RequestCallback<List<OrgActive>> callback){
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/agencyActivites/queryLeaderAllAgencyActivites");
        JsonApiRequest<List<OrgActive>> mRequest = new JsonApiRequest<List<OrgActive>>(url, List.class);
        mRequest.addParam("organize_id",groupId)
                .addParam("pageNo",pageNo)
                .addParam("pageSize",pageSize);
        ApiOkHttp.postAsync(mRequest,callback);
    }

    /**
     * 请求辖区数据
     */
    public static void getAreaData(String agencys,RequestCallback<GovAgencyMsg> requestCallback){
        String url=RemoteUtil.getAbsoluteApiUrl("lefuyun/governmentleader/getRoverviewWithAgencyId");
        JsonApiRequest<GovAgencyMsg> mRequest = new JsonApiRequest<GovAgencyMsg>(url, List.class);
        mRequest.addParam("creat_time","0").
                addParam("agencys",agencys);
        ApiOkHttp.postAsync(mRequest,requestCallback);
    }

    /**
     * 获取民生政话内容
     * @param type 新闻类型 0: 所有类型
     * @param pageNo 当前页号
     * @param pageSize 每页请求内容条数
     * @param callback 接口回调
     */
    public static void getOrgNews(long orgId, long type, int pageNo, int pageSize, RequestCallback<List<LeaderNews>> callback){
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/leaderNewsCtr/queryLeaderNewsList");
        JsonApiRequest<List<LeaderNews>> mRequest = new JsonApiRequest<List<LeaderNews>>(url, List.class);
        mRequest.addParam("organize_id", orgId)
                .addParam("type",type)
                .addParam("pageSize",pageSize)
                .addParam("pageNo",pageNo);
        ApiOkHttp.postAsync(mRequest,callback);
    }

    /**
     * 获取民生政话的新闻种类
     * @param orgId 政府账号ID
     * @param requestCallback 接口回调
     */
    public static void requestNewsType(long orgId, RequestCallback<List<NewsType>> requestCallback){
        String url=RemoteUtil.getAbsoluteApiUrl("lefuyun/tblNewsCategoryCtr/queryNewsCategoryList");
        JsonParameterApiRequest<List<NewsType>> mRequest = new JsonParameterApiRequest<List<NewsType>>(url, List.class);
        mRequest.addParam("organize_id", orgId);
        ApiOkHttp.postAsync(mRequest, requestCallback);
    }

    /**
     * 加载周边数据
     * @param mapStatus 当前地图属性
     * @param ids 当前机构id集合 格式: "1,12,3"
     * @param callback 接口回调
     */
    public static void loadAroundCities(MapStatus mapStatus, String ids, RequestCallback<List<Organization>> callback){
        String url=RemoteUtil.getAbsoluteApiUrl("lefuyun/agencyInfoCtr/getAgencyListWithCoordinates");
        JsonApiRequest<List<Organization>> mRequest = new JsonApiRequest<List<Organization>>(url, List.class);
        mRequest.addParam("startLongitude", mapStatus.bound.southwest.longitude)
                .addParam("startLatitude", mapStatus.bound.southwest.latitude).
                addParam("endLongitude", mapStatus.bound.northeast.longitude).
                addParam("endLatitude", mapStatus.bound.northeast.latitude).
                addParam("agencys", ids);
        ApiOkHttp.postAsync(mRequest,callback);
    }

    /**
     * 得到性别，男女比例，疾病的图表数据
     * @param time
     * @param agencys 当前领导所拥有的机构ID集合。
     * @param requestCallback
     */
    public static void getSexAgeDieases(long time, String agencys, RequestCallback<OldMsg> requestCallback){
        String url=RemoteUtil.getAbsoluteApiUrl("lefuyun/governmentleader/getOlderWithAgencyId");
        JsonApiRequest<OldMsg> mRequest = new JsonApiRequest<OldMsg>(url, List.class);
        mRequest.addParam("creat_time",time);
        mRequest.addParam("agencys",agencys);
        ApiOkHttp.postAsync(mRequest,requestCallback);
    }

    /**
     * 根据组织ID请求其所管理的地点，机构信息
     * @param id 集团ID或者政府ID
     */
    public static void getOrgInfo(long id, RequestCallback<GovOrgInfo> callback){
        String url=RemoteUtil.getAbsoluteApiUrl("lefuyun/governmentleader/getAreaWithLeader");
        JsonApiRequest<GovOrgInfo> mRequest = new JsonApiRequest<GovOrgInfo>(url, List.class);
        mRequest.addParam("organize_id", id);
        ApiOkHttp.postAsync(mRequest,callback);
    }
    /**
     * 获得床位使用率
     * @param time
     * @param agencys
     * @param requestCallback
     */
    public static void getBedUseRate(long time,String agencys,RequestCallback<UseRate> requestCallback){
        String url=RemoteUtil.getAbsoluteApiUrl("lefuyun/governmentleader/getChickInWithAgencyId");
        JsonApiRequest<UseRate> mRequest=new JsonApiRequest<UseRate>(url,UseRate.class);
        mRequest.addParam("creat_time",time).
                addParam("agencys",agencys);
        ApiOkHttp.postAsync(mRequest,requestCallback);
    }

    /**
     * 获得医保比例
     * @param agencys
     * @param time
     * @param requestCallback
     */
    public static void getInsuranceRate(String agencys,long time,RequestCallback<MedicalMsg> requestCallback){
        String url=RemoteUtil.getAbsoluteApiUrl("lefuyun/governmentleader/getInsuranceWithAgencyId");
        JsonApiRequest<MedicalMsg> mRequest=new JsonApiRequest<MedicalMsg>(url,MedicalMsg.class);
        mRequest.addParam("agencys",agencys).
                addParam("creat_time",time);
        ApiOkHttp.postAsync(mRequest,requestCallback);
    }
    public static void getNurseLevel(String agencys,long time,RequestCallback<NurseLevel> requestCallback){
        String url=RemoteUtil.getAbsoluteApiUrl("lefuyun/governmentleader/getNuringlevelWithAgencyId");
        JsonApiRequest<NurseLevel> mRequest = new JsonApiRequest<NurseLevel>(url,NurseLevel.class);
        mRequest.addParam("agencys",agencys).
                addParam("creat_time",time);
        ApiOkHttp.postAsync(mRequest,requestCallback);
    }

}
