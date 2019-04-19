package com.lefuorgn.interactive.module;

import android.os.Handler;
import android.os.Looper;

import com.lefuorgn.AppContext;
import com.lefuorgn.api.common.Json;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.synch.ConfigApi;
import com.lefuorgn.db.base.BaseDao;
import com.lefuorgn.db.model.basic.BatchEditingTask;
import com.lefuorgn.db.model.basic.Dictionary;
import com.lefuorgn.db.model.basic.DisplaySignOrNursingItem;
import com.lefuorgn.db.model.basic.NursingItem;
import com.lefuorgn.db.model.basic.Permission;
import com.lefuorgn.db.model.basic.SignAndNursingTask;
import com.lefuorgn.db.model.basic.SignConfig;
import com.lefuorgn.db.model.basic.SignIntervalColor;
import com.lefuorgn.db.model.basic.SignIntervalPointColor;
import com.lefuorgn.db.util.DaoHelper;
import com.lefuorgn.db.util.SignConfigManager;
import com.lefuorgn.interactive.bean.ConfigContent;
import com.lefuorgn.interactive.bean.SignAndNursingConfig;
import com.lefuorgn.interactive.interf.OnSyncChangeListener;
import com.lefuorgn.util.TLog;

import java.util.List;

/**
 * 配置请求模块
 */
public class ConfigModule {

	private static final String[] sNames = {"体温", "血压", "血糖", "心率", "排便", "呼吸", "饮水", "睡眠", "进食"};
	/**
	 * 当前请求配置的个数
	 */
	public static final int CONFIG_NUM = 5;

	private static ConfigModule instance;
	private OnSyncChangeListener mListener;
	
	private DaoHelper mDaoHelper;
	
	private BaseDao<SignConfig, Long> mSignConfigDao;
	private BaseDao<SignIntervalColor, Long> mSignIntervalColorDao;
	private BaseDao<SignIntervalPointColor, Long> mSignIntervalPointColorDao;
	private BaseDao<DisplaySignOrNursingItem, Long> mDisplaySignOrNursingItemDao;
	private BaseDao<NursingItem, Long> mNursingItemDao;
	private BaseDao<Permission, Long> mPermissionDao;
	private BaseDao<Dictionary, Long> mDictionaryDao;
	private BaseDao<BatchEditingTask, Long> mBatchEditingTaskDao;
	private BaseDao<SignAndNursingTask, Long> mSignAndNursingTaskDao;

	private Handler mCurrentHandler;
	private long mAgencyId;
	// 网络请求数
	private int nNetworkRequestCount;

	private ConfigModule(Looper looper) {
		mDaoHelper = DaoHelper.getInstance();
		mCurrentHandler = new Handler(looper);

		mSignConfigDao = mDaoHelper.getDao(SignConfig.class);
		mSignIntervalColorDao = mDaoHelper.getDao(SignIntervalColor.class);
		mSignIntervalPointColorDao = mDaoHelper.getDao(SignIntervalPointColor.class);
		mDisplaySignOrNursingItemDao = mDaoHelper.getDao(DisplaySignOrNursingItem.class);
		mNursingItemDao = mDaoHelper.getDao(NursingItem.class);
		mPermissionDao = mDaoHelper.getDao(Permission.class);
		mDictionaryDao = mDaoHelper.getDao(Dictionary.class);
		mBatchEditingTaskDao = mDaoHelper.getDao(BatchEditingTask.class);
        mSignAndNursingTaskDao = mDaoHelper.getDao(SignAndNursingTask.class);
	}
	
	public static ConfigModule getInstance(Looper looper) {
		if(instance == null) {
			synchronized (ConfigModule.class) {
				if(instance == null) {
					instance = new ConfigModule(looper);
				}
			}
		}
		return instance;
	}

    /**
     * 开启下载模块
     */
	public void start() {
		if(mListener != null) {
			mListener.onStart();
		}
		nNetworkRequestCount = 0;

		getNursingItem();
		getDictionary();
		getPermission();
		getSignAndNursingConfig();
		getBatchEditingTask();

	}

	/**
	 * 获取护理项条目配置
	 */
	private void getNursingItem() {
		if(mListener != null) {
			mListener.onStateChange(NursingItem.class, OnSyncChangeListener.STATE_START);
		}
		ConfigApi.getNursingItem(mAgencyId, new RequestCallback<List<NursingItem>>(mCurrentHandler) {

			@Override
			public void onSuccess(List<NursingItem> result) {
				TLog.log("result == " + result.toString());
				// 先清空表, 再保存
				mNursingItemDao.clearTable();
				mNursingItemDao.insertList(result);
				if(mListener != null) {
					mListener.onStateChange(NursingItem.class, OnSyncChangeListener.STATE_SUCCESS);
				}
                TLog.error("NursingItem获取成功");
				statisticNetworkRequest();
			}

			@Override
			public void onFailure(ApiHttpException e) {
				if(mListener != null) {
					mListener.onStateChange(NursingItem.class, OnSyncChangeListener.STATE_ERROR);
				}
                TLog.error("NursingItem获取失败" + e.toString());
				statisticNetworkRequest();
			}
		});
	}

	/**
	 * 获取字典配置
	 */
	private void getDictionary() {
		if(mListener != null) {
			mListener.onStateChange(Dictionary.class, OnSyncChangeListener.STATE_START);
		}
		ConfigApi.getDictionary(mAgencyId, new RequestCallback<List<Dictionary>>(mCurrentHandler) {

			@Override
			public void onSuccess(List<Dictionary> result) {
                // 清空字典
                mDictionaryDao.clearTable();
                // 保存新的数据
				mDictionaryDao.insertList(result);
				if(mListener != null) {
					mListener.onStateChange(Dictionary.class, OnSyncChangeListener.STATE_SUCCESS);
				}
                TLog.error("Dictionary获取成功");
				statisticNetworkRequest();
			}

			@Override
			public void onFailure(ApiHttpException e) {
				if(mListener != null) {
					mListener.onStateChange(Dictionary.class, OnSyncChangeListener.STATE_ERROR);
				}
                TLog.error("Dictionary获取失败" + e.toString());
				statisticNetworkRequest();
			}
		});
	}

	/**
	 * 获取权限配置
	 */
	private void getPermission() {
		if(mListener != null) {
			mListener.onStateChange(Permission.class, OnSyncChangeListener.STATE_START);
		}
		ConfigApi.getPermission(mAgencyId, new RequestCallback<List<Permission>>(mCurrentHandler) {

			@Override
			public void onSuccess(List<Permission> result) {
                // 清空权限表
                mPermissionDao.clearTable();
                // 插入权限内容
				mPermissionDao.insertList(result);
				if(mListener != null) {
					mListener.onStateChange(Permission.class, OnSyncChangeListener.STATE_SUCCESS);
				}
                TLog.error("Permission获取成功");
				statisticNetworkRequest();
			}

			@Override
			public void onFailure(ApiHttpException e) {
				if(mListener != null) {
					mListener.onStateChange(Permission.class, OnSyncChangeListener.STATE_ERROR);
				}
                TLog.error("Permission获取失败" + e.toString());
				statisticNetworkRequest();
			}
		});
	}

	private void getSignAndNursingConfig() {
		if(mListener != null) {
			mListener.onStateChange(SignConfig.class, OnSyncChangeListener.STATE_START);
		}
		ConfigApi.getSignAndNursingConfig(mAgencyId, new RequestCallback<ConfigContent>(mCurrentHandler) {

			@Override
			public void onSuccess(ConfigContent result) {
				TLog.log("Sign == " + result.getContent());
                // 清空表
                mSignConfigDao.clearTable();
                mSignIntervalColorDao.clearTable();
                mSignIntervalPointColorDao.clearTable();
                mDisplaySignOrNursingItemDao.clearTable();
                // 保存内容
				SignAndNursingConfig json = Json.getGson().fromJson(result.getContent(), SignAndNursingConfig.class);
                AppContext.saveApprovalStatus(json.getApprovalStatus());
				saveSignConfigToDB(json.getBloodPressure(), SignConfigManager.SIGN_BLOOD_PRESSURE);
				saveSignConfigToDB(json.getBloodSugar(), SignConfigManager.SIGN_BLOOD_SUGAR);
				saveSignConfigToDB(json.getBreathing(), SignConfigManager.SIGN_BREATHING);
				saveSignConfigToDB(json.getDefecation(), SignConfigManager.SIGN_DEFECATION);
				saveSignConfigToDB(json.getDrinkWater(), SignConfigManager.SIGN_DRINK);
				saveSignConfigToDB(json.getPulse(), SignConfigManager.SIGN_PULSE);
				saveSignConfigToDB(json.getTemperature(), SignConfigManager.SIGN_TEMPERATURE);

				for (DisplaySignOrNursingItem item: json.getDailyNursing()) {
					item.setSign(false);
					mDisplaySignOrNursingItemDao.insert(item);
				}
				for (DisplaySignOrNursingItem item: json.getSignsData()) {
					item.setSign(true);
					int type = (int) item.getType();
					if(type >= 1 && type <= 9) {
						item.setTitle(sNames[type - 1]);
					}
					mDisplaySignOrNursingItemDao.insert(item);
				}
				if(mListener != null) {
					mListener.onStateChange(SignConfig.class, OnSyncChangeListener.STATE_SUCCESS);
				}
                TLog.error("ConfigContent获取成功");
				statisticNetworkRequest();
			}

			@Override
			public void onFailure(ApiHttpException e) {
				if(mListener != null) {
					mListener.onStateChange(SignConfig.class, OnSyncChangeListener.STATE_ERROR);
				}
                TLog.error("ConfigContent获取失败" + e.toString());
				statisticNetworkRequest();
			}
		});
	}

	/**
	 * 将当前配置保存到数据库中
	 * @param config 当前条目配置信息
	 * @param type 当前条目的标记值
     */
	private void saveSignConfigToDB(SignConfig config, int type) {
		// 设置当前config的标记值
		config.setType(type);
		mSignConfigDao.insert(config);

		for (SignIntervalColor color : config.getColor()) {
			// 保存区间颜色值
			color.setSignConfig(config);
			mSignIntervalColorDao.insert(color);
		}

		for (SignIntervalPointColor color : config.getLine()) {
			// 保存端点颜色值
			color.setSignConfig(config);
			mSignIntervalPointColorDao.insert(color);
		}
	}

	/**
	 * 获取实时数据信息
	 */
	private void getBatchEditingTask() {
		if(mListener != null) {
			mListener.onStateChange(BatchEditingTask.class, OnSyncChangeListener.STATE_START);
		}
		ConfigApi.getBatchEditingTask(mAgencyId, new RequestCallback<List<BatchEditingTask>>() {
			@Override
			public void onSuccess(List<BatchEditingTask> result) {
                // 清空表
                mBatchEditingTaskDao.clearTable();
                mSignAndNursingTaskDao.clearTable();
                for (BatchEditingTask task : result) {
                    mBatchEditingTaskDao.insert(task);
                    for (SignAndNursingTask nursingTask : task.getNursingItmes()) {
                        nursingTask.setBatchEditingTask(task);
                        mSignAndNursingTaskDao.insert(nursingTask);
                    }
                    for (SignAndNursingTask signTask : task.getSignItems()) {
                        signTask.setBatchEditingTask(task);
                        mSignAndNursingTaskDao.insert(signTask);
                    }
                }
                TLog.error("BatchEditingTask获取成功");
                statisticNetworkRequest();
                if(mListener != null) {
                    mListener.onStateChange(BatchEditingTask.class, OnSyncChangeListener.STATE_SUCCESS);
                }
            }

			@Override
			public void onFailure(ApiHttpException e) {
                TLog.error("BatchEditingTask获取失败" + e.toString());
                statisticNetworkRequest();
                if(mListener != null) {
                    mListener.onStateChange(BatchEditingTask.class, OnSyncChangeListener.STATE_ERROR);
                }
			}
		});

	}

	/**
	 * 统计当前完成的网络请求个数
	 */
	private synchronized void statisticNetworkRequest() {
		nNetworkRequestCount++;
		TLog.error("被调用了" + nNetworkRequestCount + "次========================");
		if(nNetworkRequestCount == CONFIG_NUM && mListener != null) {
			// 网络请求完成
			mListener.onEnd();
		}
	}
	
	/**
	 * 关闭当前模块
	 */
	public void close() {
		mDaoHelper = null;
		mCurrentHandler = null;
		instance = null;
	}

	/**
	 * 设置机构ID
	 */
	public void setAgencyId(long agencyId) {
		this.mAgencyId = agencyId;
	}

	/**
	 * 设置当前上传操作监听器
	 *
	 * @param l 同步状态监听接口
	 */
	public void setOnSyncChangeListener(OnSyncChangeListener l) {
		if (l == null) {
			throw new IllegalArgumentException("The OnDownloadStateChangeListener is null");
		}
		mListener = l;
	}
	
	
}
