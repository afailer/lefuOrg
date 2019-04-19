package com.lefuorgn.api.remote.synch;

import com.lefuorgn.api.ApiOkHttp;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.request.JsonApiRequest;
import com.lefuorgn.api.remote.common.RemoteUtil;
import com.lefuorgn.db.model.basic.BatchEditingTask;
import com.lefuorgn.db.model.basic.Dictionary;
import com.lefuorgn.db.model.basic.NursingItem;
import com.lefuorgn.db.model.basic.Permission;
import com.lefuorgn.interactive.bean.ConfigContent;

import java.util.List;


public class ConfigApi {
	
	/**
	 * 获取体征项和护理项值颜色配置以及要显示的体征项和护理项的条目
	 * @param agencyId 机构ID
	 * @param requestCallback 接口回调
	 */
	public static void getSignAndNursingConfig(long agencyId, RequestCallback<ConfigContent> requestCallback) {
		String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/config/getConfig");
		JsonApiRequest<ConfigContent> request = new JsonApiRequest<ConfigContent>(url, ConfigContent.class);
		request.addParam("agency_id", agencyId);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	
	/**
	 * 获取护理条目
	 * @param agencyId 机构ID
	 * @param requestCallback 接口回调
	 */
	public static void getNursingItem(long agencyId, RequestCallback<List<NursingItem>> requestCallback) {
		String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/nursingItemsCtr/getNursingItemsByAgencyId");
		JsonApiRequest<List<NursingItem>> request = new JsonApiRequest<List<NursingItem>>(url, List.class);
		request.addParam("agency_id", agencyId);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	
	/**
	 * 获取当前用户权限信息
	 * @param agencyId 机构ID
	 * @param requestCallback 接口回调
	 */
	public static void getPermission(long agencyId, RequestCallback<List<Permission>> requestCallback) {
		String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/orgUserCtr/queryPermissionUser");
		JsonApiRequest<List<Permission>> request = new JsonApiRequest<List<Permission>>(url, List.class);
		request.addParam("agency_id", agencyId);
		ApiOkHttp.postAsync(request, requestCallback);
	}
	
	/**
	 * 获取当前字典信息
	 * @param agencyId 机构ID
	 * @param requestCallback 接口回调
	 */
	public static void getDictionary(long agencyId, RequestCallback<List<Dictionary>> requestCallback) {
		String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/dictCtr/queryDictListAll");
		JsonApiRequest<List<Dictionary>> request = new JsonApiRequest<List<Dictionary>>(url, List.class);
		request.addParam("agency_id", agencyId);
		ApiOkHttp.postAsync(request, requestCallback);
	}

    /**
     * 下载体征和护理项配单信息
     * @param agencyId 机构ID
     * @param requestCallback 接口回调
     */
	public static void getBatchEditingTask(long agencyId, RequestCallback<List<BatchEditingTask>> requestCallback) {
        String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/nursingLevelCtr/queryAllLevelItems");
        JsonApiRequest<List<BatchEditingTask>> request = new JsonApiRequest<List<BatchEditingTask>>(url, List.class);
        request.addParam("agency_id", agencyId);
        ApiOkHttp.postAsync(request, requestCallback);
	}

}
