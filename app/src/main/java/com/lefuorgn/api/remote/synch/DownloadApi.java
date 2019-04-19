package com.lefuorgn.api.remote.synch;

import com.lefuorgn.api.ApiOkHttp;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.http.request.JsonApiRequest;
import com.lefuorgn.api.remote.common.RemoteUtil;
import com.lefuorgn.db.model.basic.Bed;
import com.lefuorgn.db.model.basic.OldPeople;
import com.lefuorgn.db.model.basic.OldPeopleFamily;
import com.lefuorgn.db.model.download.BreathingDownload;
import com.lefuorgn.db.model.download.DailyLifeDownload;
import com.lefuorgn.db.model.download.DailyNursingDownload;
import com.lefuorgn.db.model.download.DefecationDownload;
import com.lefuorgn.db.model.download.DrinkingDownload;
import com.lefuorgn.db.model.download.EatDownload;
import com.lefuorgn.db.model.download.PressureDownload;
import com.lefuorgn.db.model.download.PulseDownload;
import com.lefuorgn.db.model.download.SleepingDownload;
import com.lefuorgn.db.model.download.SugarDownload;
import com.lefuorgn.db.model.download.TemperatureDownload;
import com.lefuorgn.db.model.interf.IDownloadable;
import com.lefuorgn.interactive.bean.CareOldPeople;

import java.util.List;

/**
 * 下载表请求API
 */
public class DownloadApi {

	/**
	 * 下载关注老人的列表
	 * @param requestCallback 回调函数
     */
	public static void downloadOldPeopleAttention(RequestCallback<List<CareOldPeople>> requestCallback) {
		// 接口保持在线登录
		String url = RemoteUtil.getAbsoluteApiUrl("lefuyun/userFollowOlderCtr/getListByUser");
		JsonApiRequest<List<CareOldPeople>> request = new JsonApiRequest<List<CareOldPeople>>(url, List.class);
		ApiOkHttp.postAsync(request, requestCallback);
	}

	/**
	 * 下载指定表的信息
	 * @param clazz 当前下载表的类字节码
	 * @param lastTime 当前表最近一条数据的时间
	 * @param agencyId 当前机构ID
	 * @param requestCallback 回调函数
	 */
	@SuppressWarnings("rawtypes")
	public static <T extends IDownloadable> void downloadData(Class<T> clazz, long lastTime, long agencyId, final RequestCallback<List> requestCallback) {
		String url = getCurrentUrl(clazz);
		if(clazz == PulseDownload.class) {
			// 心率
			JsonApiRequest<List<PulseDownload>> request = new JsonApiRequest<List<PulseDownload>>(url, List.class);
			addParam(request, lastTime, agencyId);
			ApiOkHttp.postAsync(request, new RequestCallback<List<PulseDownload>>(requestCallback.getHandler()) {

				@Override
				public void onSuccess(List<PulseDownload> result) {
					requestCallback.onSuccess(result);
					
				}

				@Override
				public void onFailure(ApiHttpException e) {
					requestCallback.onFailure(e);
					
				}

			});
		}else if(clazz == PressureDownload.class) {
			// 血压
			JsonApiRequest<List<PressureDownload>> request = new JsonApiRequest<List<PressureDownload>>(url, List.class);
			addParam(request, lastTime, agencyId);
			ApiOkHttp.postAsync(request, new RequestCallback<List<PressureDownload>>(requestCallback.getHandler()) {

				@Override
				public void onSuccess(List<PressureDownload> result) {
					requestCallback.onSuccess(result);
					
				}

				@Override
				public void onFailure(ApiHttpException e) {
					requestCallback.onFailure(e);
					
				}

			});
		}else if(clazz == SugarDownload.class) {
			// 血糖
			JsonApiRequest<List<SugarDownload>> request = new JsonApiRequest<List<SugarDownload>>(url, List.class);
			addParam(request, lastTime, agencyId);
			ApiOkHttp.postAsync(request, new RequestCallback<List<SugarDownload>>(requestCallback.getHandler()) {

				@Override
				public void onSuccess(List<SugarDownload> result) {
					requestCallback.onSuccess(result);
					
				}

				@Override
				public void onFailure(ApiHttpException e) {
					requestCallback.onFailure(e);
					
				}

			});
		}else if(clazz == TemperatureDownload.class) {
			// 体温
			JsonApiRequest<List<TemperatureDownload>> request = new JsonApiRequest<List<TemperatureDownload>>(url, List.class);
			addParam(request, lastTime, agencyId);
			ApiOkHttp.postAsync(request, new RequestCallback<List<TemperatureDownload>>(requestCallback.getHandler()) {

				@Override
				public void onSuccess(List<TemperatureDownload> result) {
					requestCallback.onSuccess(result);
					
				}

				@Override
				public void onFailure(ApiHttpException e) {
					requestCallback.onFailure(e);
					
				}

			});
		}else if(clazz == DrinkingDownload.class) {
			// 饮水
			JsonApiRequest<List<DrinkingDownload>> request = new JsonApiRequest<List<DrinkingDownload>>(url, List.class);
			addParam(request, lastTime, agencyId);
			ApiOkHttp.postAsync(request, new RequestCallback<List<DrinkingDownload>>(requestCallback.getHandler()) {

				@Override
				public void onSuccess(List<DrinkingDownload> result) {
					requestCallback.onSuccess(result);
					
				}

				@Override
				public void onFailure(ApiHttpException e) {
					requestCallback.onFailure(e);
					
				}

			});
		}else if(clazz == SleepingDownload.class) {
			// 睡眠
			JsonApiRequest<List<SleepingDownload>> request = new JsonApiRequest<List<SleepingDownload>>(url, List.class);
			addParam(request, lastTime, agencyId);
			ApiOkHttp.postAsync(request, new RequestCallback<List<SleepingDownload>>(requestCallback.getHandler()) {

				@Override
				public void onSuccess(List<SleepingDownload> result) {
					requestCallback.onSuccess(result);
					
				}

				@Override
				public void onFailure(ApiHttpException e) {
					requestCallback.onFailure(e);
					
				}

			});
		}else if(clazz == EatDownload.class) {
			// 饮食
			JsonApiRequest<List<EatDownload>> request = new JsonApiRequest<List<EatDownload>>(url, List.class);
			addParam(request, lastTime, agencyId);
			ApiOkHttp.postAsync(request, new RequestCallback<List<EatDownload>>(requestCallback.getHandler()) {

				@Override
				public void onSuccess(List<EatDownload> result) {
					requestCallback.onSuccess(result);
					
				}

				@Override
				public void onFailure(ApiHttpException e) {
					requestCallback.onFailure(e);
					
				}

			});
		}else if(clazz == DefecationDownload.class) {
			// 排便接口
			JsonApiRequest<List<DefecationDownload>> request = new JsonApiRequest<List<DefecationDownload>>(url, List.class);
			addParam(request, lastTime, agencyId);
			ApiOkHttp.postAsync(request, new RequestCallback<List<DefecationDownload>>(requestCallback.getHandler()) {

				@Override
				public void onSuccess(List<DefecationDownload> result) {
					requestCallback.onSuccess(result);
					
				}

				@Override
				public void onFailure(ApiHttpException e) {
					requestCallback.onFailure(e);
					
				}

			});
		}else if(clazz == BreathingDownload.class) {
			// 呼吸接口
			JsonApiRequest<List<BreathingDownload>> request = new JsonApiRequest<List<BreathingDownload>>(url, List.class);
			addParam(request, lastTime, agencyId);
			ApiOkHttp.postAsync(request, new RequestCallback<List<BreathingDownload>>(requestCallback.getHandler()) {

				@Override
				public void onSuccess(List<BreathingDownload> result) {
					requestCallback.onSuccess(result);
					
				}

				@Override
				public void onFailure(ApiHttpException e) {
					requestCallback.onFailure(e);
					
				}

			});
		}else if(clazz == DailyLifeDownload.class) {
			// 日常生活接口
			JsonApiRequest<List<DailyLifeDownload>> request = new JsonApiRequest<List<DailyLifeDownload>>(url, List.class);
			addParam(request, lastTime, agencyId);
			ApiOkHttp.postAsync(request, new RequestCallback<List<DailyLifeDownload>>(requestCallback.getHandler()) {

				@Override
				public void onSuccess(List<DailyLifeDownload> result) {
					requestCallback.onSuccess(result);
					
				}

				@Override
				public void onFailure(ApiHttpException e) {
					requestCallback.onFailure(e);
					
				}

			});
		}else if(clazz == DailyNursingDownload.class) {
			// 照护记录接口
			JsonApiRequest<List<DailyNursingDownload>> request = new JsonApiRequest<List<DailyNursingDownload>>(url, List.class);
			addParam(request, lastTime, agencyId);
			ApiOkHttp.postAsync(request, new RequestCallback<List<DailyNursingDownload>>(requestCallback.getHandler()) {

				@Override
				public void onSuccess(List<DailyNursingDownload> result) {
					requestCallback.onSuccess(result);
					
				}

				@Override
				public void onFailure(ApiHttpException e) {
					requestCallback.onFailure(e);
					
				}

			});
		}else if(clazz == OldPeople.class) {
			// 老人接口
			JsonApiRequest<List<OldPeople>> request = new JsonApiRequest<List<OldPeople>>(url, List.class);
			addParam(request, lastTime, agencyId);
			ApiOkHttp.postAsync(request, new RequestCallback<List<OldPeople>>(requestCallback.getHandler()) {

				@Override
				public void onSuccess(List<OldPeople> result) {
					requestCallback.onSuccess(result);

				}

				@Override
				public void onFailure(ApiHttpException e) {
					requestCallback.onFailure(e);

				}

			});
		}else if(clazz == Bed.class) {
			// 床位接口
			JsonApiRequest<List<Bed>> request = new JsonApiRequest<List<Bed>>(url, List.class);
			addParam(request, lastTime, agencyId);
			ApiOkHttp.postAsync(request, new RequestCallback<List<Bed>>(requestCallback.getHandler()) {

				@Override
				public void onSuccess(List<Bed> result) {
					requestCallback.onSuccess(result);

				}

				@Override
				public void onFailure(ApiHttpException e) {
					requestCallback.onFailure(e);

				}

			});
		}else if(clazz == OldPeopleFamily.class) {
			// 老人家属接口
			JsonApiRequest<List<OldPeopleFamily>> request = new JsonApiRequest<List<OldPeopleFamily>>(url, List.class);
			addParam(request, lastTime, agencyId);
			ApiOkHttp.postAsync(request, new RequestCallback<List<OldPeopleFamily>>(requestCallback.getHandler()) {

				@Override
				public void onSuccess(List<OldPeopleFamily> result) {
					requestCallback.onSuccess(result);

				}

				@Override
				public void onFailure(ApiHttpException e) {
					requestCallback.onFailure(e);

				}

			});
		}
	}
	
	private static <T> void addParam(JsonApiRequest<List<T>> request, long lastTime,  long agencyId) {
		request.addParam("agencyId", agencyId)
		.addParam("checkTime", lastTime);
	}
	
	/**
	 * 获取当前请求接口地址
	 * @param clazz 当前表字节码
	 * @return 当前下载表字节码关联的接口地址
	 */
	private static <T> String getCurrentUrl(Class<T> clazz) {
		String uri = null;
		if(clazz == PulseDownload.class) {
			// 心率接口地址
			uri = "lefuyun/singndata/pulse/orgSycnPulses";
		}else if(clazz == PressureDownload.class) {
			// 血压接口地址
			uri = "lefuyun/singndata/bloodPressure/orgSyncBloodPressure";
		}else if(clazz == SugarDownload.class) {
			// 血糖接口地址
			uri = "lefuyun/singndata/bloodSugar/orgSyncBloodSugars";
		}else if(clazz == TemperatureDownload.class) {
			// 体温接口地址
			uri = "lefuyun/singndata/temperature/orgSycnTemperatures";
		}else if(clazz == DrinkingDownload.class) {
			// 饮水接口地址
			uri = "lefuyun/singndata/drinkwater/orgSyncDrinkWater";
		}else if(clazz == SleepingDownload.class) {
			// 睡眠接口地址
			uri = "lefuyun/singndata/sleep/orgSyncSleep";
		}else if(clazz == EatDownload.class) {
			// 饮食接口地址
			uri = "lefuyun/singndata/meal/orgSyncMeal";
		}else if(clazz == DefecationDownload.class) {
			// 排便接口
			uri = "lefuyun/singndata/defecation/orgSyncDefecation";
		}else if(clazz == BreathingDownload.class) {
			// 呼吸接口
			uri = "lefuyun/singndata/breathing/orgSyncbreathing";
		}else if(clazz == DailyLifeDownload.class) {
			// 日常生活接口
			uri = "lefuyun/dailyLifeRecordCtr/orgSyncDailyLifeRecords";
		}else if(clazz == DailyNursingDownload.class) {
			// 照护记录接口
			uri = "lefuyun/dailyNursingRecordCtr/orgSyncDailyNursingRecords";
		}else if(clazz == OldPeople.class) {
			// 老人接口
			uri = "lefuyun/oldPeopleCtr/orgSyncOldPeoples";
		}else if(clazz == Bed.class) {
			// 床位接口
			uri = "lefuyun/bedInfoCtr/orgSyncBedInfos";
		}else if(clazz == OldPeopleFamily.class) {
			// 老人家属接口
			uri = "lefuyun/oldPeopleFamilyCtr/syncOldPeopleFamily";
		}
		return RemoteUtil.getAbsoluteApiUrl(uri);
	}

}
