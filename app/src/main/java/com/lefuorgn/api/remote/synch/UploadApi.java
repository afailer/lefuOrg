package com.lefuorgn.api.remote.synch;

import com.lefuorgn.api.ApiOkHttp;
import com.lefuorgn.api.common.Json;
import com.lefuorgn.api.common.MultiMediaUpload;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.request.JsonApiRequest;
import com.lefuorgn.api.http.request.UploadMultiMediaApiRequest;
import com.lefuorgn.api.remote.common.RemoteUtil;
import com.lefuorgn.db.model.upload.BreathingUpload;
import com.lefuorgn.db.model.upload.DailyLifeUpload;
import com.lefuorgn.db.model.upload.DailyNursingUpload;
import com.lefuorgn.db.model.upload.DefecationUpload;
import com.lefuorgn.db.model.upload.DrinkingUpload;
import com.lefuorgn.db.model.upload.EatUpload;
import com.lefuorgn.db.model.upload.PressureUpload;
import com.lefuorgn.db.model.upload.PulseUpload;
import com.lefuorgn.db.model.upload.SleepingUpload;
import com.lefuorgn.db.model.upload.SugarUpload;
import com.lefuorgn.db.model.upload.TemperatureUpload;

import java.util.List;

/**
 * 上传表API
 */
public class UploadApi {

	/**
	 * 上传当前用户所关注的老人
	 * @param ids 老人ID字符串, 如: "1,2,3"
	 * @param requestCallback 接口回调函数
     */
	public static void uploadOldPeopleAttention(String ids, RequestCallback<String> requestCallback) {
		// 接口保持在线登录
		String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/userFollowOlderCtr/bulkAdd");
		JsonApiRequest<String> request = new JsonApiRequest<String>(url, String.class);
		request.addParam("older_ids", ids);
		ApiOkHttp.postAsync(request, requestCallback);
	}

	/**
	 * 上传指定表的信息
	 * @param clazz 当前表字节码
	 * @param list 当前表信息结合
	 * @param requestCallback 回调接口
	 */
	public static <T> void uploadData(Class<T> clazz, List<T> list, RequestCallback<String> requestCallback) {
		String url = getCurrentUrl(clazz);
		JsonApiRequest<String> request = new JsonApiRequest<String>(url, String.class);
		request.addParam(getParam(clazz), getJsonFromList(list));
		ApiOkHttp.postAsync(request, requestCallback);
	}
	
	/**
	 * 将结合list转换成JSON字符串
	 * @param list 当前信息集合
	 * @return json字符串
	 */
	private static <T> String getJsonFromList(List<T> list) {
		return Json.getGson().toJson(list);
	}

	/**
	 * 获取当前请求接口地址
	 * @param clazz 当前表字节码
	 * @return 当前接口地址
	 */
	private static <T> String getCurrentUrl(Class<T> clazz) {
		String uri = null;
		if(clazz == PulseUpload.class) {
			// 心率接口地址
			uri = "lefuyun/singndata/pulse/addOrUpdatePulses";
		}else if(clazz == PressureUpload.class) {
			// 血压接口地址
			uri = "lefuyun/singndata/bloodPressure/addOrUpdateBloodPressures";
		}else if(clazz == SugarUpload.class) {
			// 血糖接口地址
			uri = "lefuyun/singndata/bloodSugar/addOrUpdateBloodSugars";
		}else if(clazz == TemperatureUpload.class) {
			// 体温接口地址
			uri = "lefuyun/singndata/temperature/addOrUpdateTemperatures";
		}else if(clazz == DrinkingUpload.class) {
			// 饮水接口地址
			uri = "lefuyun/singndata/drinkwater/addOrUpdateDrinkWaterRecord";
		}else if(clazz == SleepingUpload.class) {
			// 睡眠接口地址
			uri = "lefuyun/singndata/sleep/addOrUpdateSleepRecord";
		}else if(clazz == EatUpload.class) {
			// 饮食接口地址
			uri = "lefuyun/singndata/meal/addOrUpdateMealRecord";
		}else if(clazz == DefecationUpload.class) {
			// 排便接口地址
			uri = "lefuyun/singndata/defecation/addOrUpdateDefecationRecord";
		}else if(clazz == BreathingUpload.class) {
			// 呼吸接口地址
			uri = "lefuyun/singndata/breathing/addOrUpdateBreathingRecord";
		}else if(clazz == DailyLifeUpload.class) {
			// 随手拍接口地址
			uri = "lefuyun/dailyLifeRecordCtr/addOrUpdateDailyLifeRecords";
		}else if(clazz == DailyNursingUpload.class) {
			// 照护记录接口地址
			uri = "lefuyun/dailyNursingRecordCtr/addOrUpdateDailyNursingRecords";
		}
		return RemoteUtil.getAbsoluteApiUrl(uri);
	}
	
	private static <T> String getParam(Class<T> clazz) {
		String param = "";
		if(clazz == PulseUpload.class) {
			// 心率字段
			param = "prs";
		}else if(clazz == PressureUpload.class) {
			// 血压字段
			param = "bprs";
		}else if(clazz == SugarUpload.class) {
			// 血糖字段
			param = "bsrs";
		}else if(clazz == TemperatureUpload.class) {
			// 体温字段
			param = "trs";
		}else if(clazz == DrinkingUpload.class) {
			// 饮水字段
			param = "drs";
		}else if(clazz == SleepingUpload.class) {
			// 睡眠字段
			param = "drs";
		}else if(clazz == EatUpload.class) {
			// 饮食字段
			param = "drs";
		}else if(clazz == DefecationUpload.class) {
			// 排便字段
			param = "drs";
		}else if(clazz == BreathingUpload.class) {
			// 呼吸字段
			param = "brs";
		}else if(clazz == DailyLifeUpload.class) {
			// 随手拍字段
			param = "dnrs";
		}else if(clazz == DailyNursingUpload.class) {
			// 照护记录字段
			param = "dnrs";
		}
		return param;
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

}
