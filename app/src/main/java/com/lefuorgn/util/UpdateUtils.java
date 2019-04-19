package com.lefuorgn.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.lefuorgn.AppConfig;
import com.lefuorgn.AppContext;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.LefuApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.bean.Version;
import com.lefuorgn.dialog.AlertDialog;
import com.lefuorgn.dialog.ProgressDialog;
import com.lefuorgn.dialog.WaitDialog;

import java.io.File;


/**
 * 更新管理类
 * 
 */
public class UpdateUtils {

	private Version mVersion;

	private BaseActivity mActivity;
	// 是否显示等待dialog
	private boolean isShow = false;
    private int rate;
    private int pRate;
	
	private WaitDialog _waitDialog;
	
	public UpdateUtils(BaseActivity activity, boolean isShow) {
		this.mActivity = activity;
		this.isShow = isShow;
	}
	
	private boolean haveNewVersion() {
		if (mVersion == null) {
			return false;
		}
		boolean haveNew = false;
		String curVersionName = AppContext.getInstance().getPackageInfo().versionName;
		if (!curVersionName.equals(mVersion.getVersion())) {
			haveNew = true;
		}
		return haveNew;
	}

	public void checkUpdate() {
        if (isShow) {
            showCheckDialog();
        }
        LefuApi.checkUpdate(new RequestCallback<Version>() {

            @Override
            public void onSuccess(Version result) {
                if(isShow) {
                    hideCheckDialog();
                }
                mVersion = result;
                onFinishCheck();
            }

            @Override
            public void onFailure(ApiHttpException e) {
                hideCheckDialog();
                if (isShow) {
                    showFailDialog();
                }

            }
        });
	}
	
	private void onFinishCheck() {
		if (haveNewVersion()) {
			showUpdateInfo();
		} else {
			if (isShow) {
				showLatestDialog();
			}
		}
	}

	private void showCheckDialog() {
		if (_waitDialog == null) {
			_waitDialog = new WaitDialog();
		}
		_waitDialog.show(mActivity.getSupportFragmentManager(), "");
	}

	private void hideCheckDialog() {
		if (_waitDialog != null) {
			_waitDialog.dismiss();
		}
	}
	
	private void showUpdateInfo() {
		if (mVersion == null) {
			return;
		}
		// 发现新版本
        new AlertDialog()
                .setTitle("发现新版本").setHtmlContent(mVersion.getDesc())
                .setGravity(AlertDialog.LEFT)
                .setClickCallBack(new AlertDialog.ClickCallBack() {
            @Override
            public void cancel() {

            }

            @Override
            public void confirm() {
                // 开启下载
                startDownload();
            }
        }).show(mActivity.getSupportFragmentManager(), "AlertDialog");
	}

    /**
     * 开始下载
     */
    @SuppressLint("WorldReadableFiles")
    private void startDownload() {
        final ProgressDialog pd = new ProgressDialog();
        pd.setTitle("下载更新");
        // 判断是否存在sd卡
        boolean sdExist = android.os.Environment.MEDIA_MOUNTED
                .equals(android.os.Environment.getExternalStorageState());
        // 生成保存APP的路径
        String saveUrl;
        if(sdExist) {
            // SD卡挂载
            TLog.error("SD卡搭载...");
            saveUrl = AppConfig.DEFAULT_SAVE_FILE_PATH;
        }else {
            // SD卡没有挂载
            TLog.error("SD卡未搭载...");
            saveUrl = mActivity.getDir("download", Context.MODE_WORLD_READABLE).getAbsolutePath();
        }
        saveUrl = saveUrl + getSaveFileName(mVersion.getAppUrl());
        TLog.error("下载APP文件的路径 == " + saveUrl);
        pd.show(mActivity.getSupportFragmentManager(), "ProgressDialog");
        pRate = -1;
        LefuApi.downloadApp(mVersion.getAppUrl(), saveUrl, new RequestCallback<File>() {
            @Override
            public void onSuccess(File result) {
                installApk(result);
                pd.dismiss();
            }

            @Override
            public void onFailure(ApiHttpException e) {

            }

            @Override
            public void onLoading(long count, long current) {
                pd.setProgressBar((int) current, (int) count);
                rate = (int) (current * 1.0f / count * 100);
                if(rate % 2 == 0 && rate != pRate) {
                    // 每下载10%的数据进行更新
                    pRate = rate;
                    // 更新进度
                    pd.setNote("正在下载 " + rate + "%");
                }
            }
        });
    }

	/**
	 * 最新版本dialog
	 */
	private void showLatestDialog() {
		ToastUtils.show(mActivity, "已经是最新版本");
	}

	/**
	 * 网络异常dialog
	 */
	private void showFailDialog() {
		ToastUtils.show(mActivity, "网络异常,无法获取新版本信息");
	}

    /**
     * 安装应用
     * @param file 要安装的文件
     */
    private void installApk(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        mActivity.startActivity(intent);
    }

    /**
     * 获取文件名称
     * @param downloadUrl 要下载文件的url
     * @return 文件名称
     */
    private String getSaveFileName(String downloadUrl) {
        if (downloadUrl == null || StringUtils.isEmpty(downloadUrl)) {
            return "";
        }
        return downloadUrl.substring(downloadUrl.lastIndexOf("/"));
    }

}
