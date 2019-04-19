package com.lefuorgn.oa.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.lefuorgn.AppConfig;
import com.lefuorgn.AppContext;
import com.lefuorgn.R;
import com.lefuorgn.api.common.MultiMediaUpload;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.OaApi;
import com.lefuorgn.base.BaseActivity;
import com.lefuorgn.db.model.basic.StaffCache;
import com.lefuorgn.db.util.StaffCacheManager;
import com.lefuorgn.lefu.multiMedia.MultiMediaActivity;
import com.lefuorgn.oa.bean.AttendanceApprovalApprover;
import com.lefuorgn.oa.bean.AttendanceApprovalCarbonCopy;
import com.lefuorgn.oa.bean.AttendanceApprovalClock;
import com.lefuorgn.oa.bean.AttendanceApprovalExternal;
import com.lefuorgn.oa.bean.AttendanceApprovalSubmit;
import com.lefuorgn.oa.bean.AttendanceApprovalSubmitCopy;
import com.lefuorgn.util.ImageCompressionUtils;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.TLog;
import com.lefuorgn.viewloader.ViewLoader;
import com.lefuorgn.viewloader.base.DynamicViewBuilder;
import com.lefuorgn.viewloader.builder.DynamicCheckBoxBuilder;
import com.lefuorgn.viewloader.builder.DynamicRadioBuilder;
import com.lefuorgn.viewloader.impl.InfoEntryPageBuilder;
import com.lefuorgn.viewloader.impl.PersonEntryPageBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * 审批内容申请页面
 */

public class ClockAttendanceApprovalActivity extends BaseActivity {

    private LinearLayout mParent;

    private InfoEntryPageBuilder mInfoEntryPageBuilder;
    private DynamicRadioBuilder mDynamicRadioBuilder; // 审批人控件
    private DynamicCheckBoxBuilder mDynamicCheckBoxBuilder; // 抄送人控件
    private boolean checkBoxDelete; // 抄送人当前状态是否是删除状态
    private boolean radioDelete; // 审批人当前状态是否是删除状态

    private StaffCacheManager mStaffCacheManager;

    private AttendanceApprovalClock mApprovalClock;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_clock_attendance_approval;
    }

    @Override
    protected void initView() {
        mParent = (LinearLayout) findViewById(R.id.ll_activity_clock_attendance_approval);
        findViewById(R.id.btn_activity_clock_attendance_approval_cancel).setOnClickListener(this);
        findViewById(R.id.btn_activity_clock_attendance_approval_submit).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        long id = getIntent().getLongExtra("id", 0);
        String name = getIntent().getStringExtra("name");
        setToolBarTitle(name);
        mStaffCacheManager = StaffCacheManager.getInstance();
        showWaitDialog();
        OaApi.getAttendanceApprovalClock(id, new RequestCallback<AttendanceApprovalClock>() {
            @Override
            public void onSuccess(AttendanceApprovalClock result) {
                hideWaitDialog();
                if(result == null) {
                    return;
                }
                mApprovalClock = result;
                loadView(result.getContent(), result.getVerify());
            }

            @Override
            public void onFailure(ApiHttpException e) {
                hideWaitDialog();
                showToast(e.getMessage());
            }
        });
    }

    /**
     * 加载控件
     * @param content 控件内容json
     */
    private void loadView(String content, AttendanceApprovalExternal external) {
        if(StringUtils.isEmpty(content)) {
            return;
        }
        int type = 0; // 1: 自由审批; 2: 固定审批
        AttendanceApprovalApprover approver = null; // 审批人
        if(external != null) {
            List<AttendanceApprovalApprover> approvers = external.getVerifyLines();
            if(approvers != null && approvers.size() > 0) {
                approver = approvers.get(0);
                type = approver.getType();
            }
        }
        ViewLoader loader = new ViewLoader.Builder().content(content)
                .context(this).copy(true).dynamic(type != 2).build();
        mInfoEntryPageBuilder = new InfoEntryPageBuilder();
        loader.build(mInfoEntryPageBuilder);
        ScrollView scrollView = (ScrollView) mInfoEntryPageBuilder.getPageView();
        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.height = 0;
        params.weight = 1;
        scrollView.setLayoutParams(params);
        scrollView.setVerticalScrollBarEnabled(false);
        mParent.addView(scrollView, 0);
        mInfoEntryPageBuilder.setOnJumpPageListener(new InfoEntryPageBuilder.OnJumpPageListener() {
            @Override
            public void jumpPage(int requestCode, int maxNum) {
                Intent intent = new Intent(ClockAttendanceApprovalActivity.this, MultiMediaActivity.class);
                intent.putExtra(MultiMediaActivity.MULTIMEDIA_TYPE, MultiMediaActivity.ACTION_IMAGE_ALBUM);
                intent.putExtra(MultiMediaActivity.INTENT_ARGUMENTS, maxNum);
                startActivityForResult(intent, requestCode);
            }
        });
        // 初始化审批人信息
        PersonEntryPageBuilder personPageBuilder = new PersonEntryPageBuilder();
        loader.build(personPageBuilder);
        mInfoEntryPageBuilder.getParentView().addView(personPageBuilder.getPageView());
        mDynamicRadioBuilder = personPageBuilder.getDynamicRadioBuilder();
        mDynamicRadioBuilder.showStopView(false);
        if(type == 2) {
            // 固定审批
            mDynamicRadioBuilder.setTextView(approver.getVerify_user_name(), approver.getVerify_user_id());
        }else {
            mDynamicRadioBuilder.setOnButtonClickListener(new DynamicViewBuilder.OnButtonClickListener() {
                @Override
                public void onUpClick(View v) {
                    Intent intent = new Intent(ClockAttendanceApprovalActivity.this, SelectStuffActivity.class);
                    intent.putExtra("isCopy", "notCopy");
                    startActivityForResult(intent, 600);
                    radioDelete = false;
                    mDynamicRadioBuilder.switchView(false);
                }

                @Override
                public void onDownClick(View v) {
                    radioDelete = !radioDelete;
                    mDynamicRadioBuilder.switchView(radioDelete);
                }

                @Override
                public void onViewClick(long tag) {
                    mStaffCacheManager.removeStaffCache(tag, false);
                }
            });
            initDynamicRadioBuilder(-1);
        }
        // 初始化抄送人信息
        mDynamicCheckBoxBuilder = personPageBuilder.getDynamicCheckBoxBuilder();
        if(approver != null) {
            // 初始化固定人
            List<AttendanceApprovalCarbonCopy> copies = approver.getVerifyLineCopys();
            if(copies != null) {
                for (AttendanceApprovalCarbonCopy carbonCopy : copies) {
                    mDynamicCheckBoxBuilder.addTextView(carbonCopy.getCopy_user_name(), carbonCopy.getCopy_user_id());
                }
            }
        }
        // 添加历史记录
        initDynamicCheckBoxBuilder(null);
        mDynamicCheckBoxBuilder.setOnButtonClickListener(new DynamicViewBuilder.OnButtonClickListener() {
            @Override
            public void onUpClick(View v) {
                Intent intent = new Intent(ClockAttendanceApprovalActivity.this, SelectStuffActivity.class);
                intent.putExtra("isCopy", "isCopy");
                startActivityForResult(intent, 600);
                checkBoxDelete = false;
                mDynamicCheckBoxBuilder.switchView(false);
            }

            @Override
            public void onDownClick(View v) {
                checkBoxDelete = !checkBoxDelete;
                mDynamicCheckBoxBuilder.switchView(checkBoxDelete);
            }

            @Override
            public void onViewClick(long tag) {
                mStaffCacheManager.removeStaffCache(tag, true);
            }
        });

    }

    /**
     * 显示审批人控件
     */
    private void initDynamicRadioBuilder(long id) {
        List<StaffCache> staffFalseCaches = mStaffCacheManager.getStaffCache(false);
        int index = 10000;
        mDynamicRadioBuilder.removeAllViews();
        for (StaffCache cache : staffFalseCaches) {
            mDynamicRadioBuilder.addRadioButton(++index, cache.getName(),
                    cache.getId(), cache.getId() == id);
        }
    }

    /**
     * 显示抄送人控件
     */
    private void initDynamicCheckBoxBuilder(List<Long> ids) {
        List<StaffCache> staffTrueCaches = mStaffCacheManager.getStaffCache(true);
        int index = 100000;
        mDynamicCheckBoxBuilder.removeAllCheckBox();
        for (StaffCache cache : staffTrueCaches) {
            mDynamicCheckBoxBuilder.addCheckBox(++index, cache.getName(),
                    cache.getId(), ids != null && ids.contains(cache.getId()));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TLog.error(resultCode + "");
        if(resultCode == 800 && data != null) {
            // 添加审批员工
            String stuff = data.getStringExtra("stuff");
            String[] splits = stuff.substring(0, stuff.length() - SelectStuffActivity.DIVIDE_STR.length()).split(",");
            long id = -1;
            if(splits.length == 2) {
                id = StringUtils.toLong(splits[0], 0);
                if(id == 0) {
                    return;
                }
                StaffCache cache = new StaffCache();
                cache.setId(id);
                cache.setName(splits[1]);
                mStaffCacheManager.addStaffCache(cache, false);
            }
            initDynamicRadioBuilder(id);
        }else if(resultCode == 500 && data != null) {
            // 添加抄送人员
            String stuffStr = data.getStringExtra("stuff");
            if(StringUtils.isEmpty(stuffStr)) {
                return;
            }
            String[] stuffs = stuffStr
                    .substring(0, stuffStr.length() - SelectStuffActivity.DIVIDE_STR.length())
                    .split(SelectStuffActivity.DIVIDE_STR);
            List<Long> ids = new ArrayList<Long>();
            for (String s : stuffs) {
                String[] stuff = s.split(",");
                if(stuff.length == 2) {
                    long id;
                    id = StringUtils.toLong(stuff[0], 0);
                    if(id == 0) {
                        return;
                    }
                    ids.add(id);
                    StaffCache cache = new StaffCache();
                    cache.setId(id);
                    cache.setName(stuff[1]);
                    mStaffCacheManager.addStaffCache(cache, true);
                }
            }
            initDynamicCheckBoxBuilder(ids);
        }else if(resultCode == MultiMediaActivity.RESULT_IMAGE_ALBUM) {
            // 从相册中获取照片
            mInfoEntryPageBuilder.onActivityResult(requestCode, resultCode, data);
        }else if(resultCode == MultiMediaActivity.RESULT_IMAGE_CAPTURE) {
            // 拍照获取照片
            mInfoEntryPageBuilder.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_activity_clock_attendance_approval_cancel:
                // 取消
                finish();
                break;
            case R.id.btn_activity_clock_attendance_approval_submit:
                // 提交
                if(mApprovalClock == null) {
                    showToast("数据加载错误, 请关闭页面重新打开");
                    return;
                }
                if(!mInfoEntryPageBuilder.isComplete()) {
                    return;
                }
                long submitId = mDynamicRadioBuilder.getValue();
                if(submitId == 0) {
                    showToast("请选择审批人");
                    return;
                }
                final List<AttendanceApprovalSubmit> submitList = new ArrayList<AttendanceApprovalSubmit>();
                AttendanceApprovalSubmit submit = new AttendanceApprovalSubmit();
                submit.setVerify_user_id(submitId);
                submit.setVerifyAskCopys(new ArrayList<AttendanceApprovalSubmitCopy>());
                if(mDynamicCheckBoxBuilder != null) {
                    List<Long> ids = mDynamicCheckBoxBuilder.getValue();
                    for (Long id : ids) {
                        AttendanceApprovalSubmitCopy copy = new AttendanceApprovalSubmitCopy();
                        copy.setCopy_user_id(id);
                        submit.getVerifyAskCopys().add(copy);
                    }
                }
                submitList.add(submit);
                TLog.error(mInfoEntryPageBuilder.getResult().toString());
                TLog.error(submitList.toString());

                Map<String, String> pictureMap = mInfoEntryPageBuilder.getPictureResult();
                Map<String, String> result = mInfoEntryPageBuilder.getResult();

                if(pictureMap.size() > 0) {
                    // 图片控件上传
                    // 开启初始化数据请求任务
                    showLoadingDialog("图片上传中");
                    //noinspection unchecked
                    new UploadPictureTask().execute(submitList);
                }else {
                    showLoadingDialog();
                    saveAttendanceApprovalClock(result, submitList);
                }
                break;
        }
    }

    /**
     * 本地获取数据请求任务类
     */
    private class UploadPictureTask extends AsyncTask<List<AttendanceApprovalSubmit>, Void, List<MultiMediaUpload>> {

        private List<AttendanceApprovalSubmit> submitList;
        private Map<String, String> result;

        @Override
        protected List<MultiMediaUpload> doInBackground(List<AttendanceApprovalSubmit>... params) {
            submitList = params[0];
            Map<String, String> pictureMap = mInfoEntryPageBuilder.getPictureResult();
            result = mInfoEntryPageBuilder.getResult();
            // 图片控件上传
            List<MultiMediaUpload> files = new ArrayList<MultiMediaUpload>();
            Set<String> keySet = pictureMap.keySet();
            for (String s : keySet) {
                String value = pictureMap.get(s);
                if(StringUtils.isEmpty(value)) {
                    continue;
                }
                StringBuilder valueResult = new StringBuilder();
                String[] uris = value.split(",");
                for (String uri : uris) {
                    if(uri.startsWith("/lefuyun")) {
                        valueResult.append(uri).append(",");
                    }else {
                        String name = getAlias(uri);
                        if(StringUtils.isEmpty(name)) {
                            continue;
                        }
                        File file = new File(uri);
                        if(file.exists()) {
                            valueResult.append("/lefuyun/resource/upload/").append(name).append(",");
                            file = ImageCompressionUtils.getInstance(AppContext.getInstance()).compress(file);
                            MultiMediaUpload upload = new MultiMediaUpload();
                            upload.setType(MultiMediaUpload.MULTI_MEDIA_TYPE_PICTURE);
                            upload.setFile(file);
                            upload.setAlias(name);
                            files.add(upload);
                        }
                    }
                }
                result.put(s, valueResult.toString());
            }
            TLog.error(result.toString());
            return files;
        }

        @Override
        protected void onPostExecute(List<MultiMediaUpload> files) {
            if(files == null || files.size() == 0) {
                saveAttendanceApprovalClock(result, submitList);
                return;
            }
            OaApi.uploadMultiMedia(files, new RequestCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    saveAttendanceApprovalClock(result, submitList);
                    clearFiles();
                }

                @Override
                public void onFailure(ApiHttpException e) {
                    showToast(e.getMessage());
                    hideLoadingDialog();
                    clearFiles();
                }
            });
        }
    }

    /**
     * 清空所有媒体信息文件
     */
    private void clearFiles() {
        // 判断是否存在sd卡
        boolean sdExist = android.os.Environment.MEDIA_MOUNTED
                .equals(android.os.Environment.getExternalStorageState());
        if(sdExist) {
            // SD卡挂载
            File imgFile = new File(AppConfig.DEFAULT_SAVE_IMAGE_PATH);
            deleteFile(imgFile);
            File videoFile = new File(AppConfig.DEFAULT_SAVE_VIDEO_PATH);
            deleteFile(videoFile);
            File audioFile = new File(AppConfig.DEFAULT_SAVE_AUDIO_PATH);
            deleteFile(audioFile);
        }else {
            AppContext context = AppContext.getInstance();
            // SD卡没有挂载
            String videoPath = context.getDir("lefu_video", Context.MODE_PRIVATE).getAbsolutePath();
            deleteFile(new File(videoPath));
            String audioPath = context.getDir("lefu_audio", Context.MODE_PRIVATE).getAbsolutePath();
            deleteFile(new File(audioPath));
            String imgPath = context.getDir("lefu_img", Context.MODE_PRIVATE).getAbsolutePath();
            deleteFile(new File(imgPath));
        }

    }

    /**
     * 删除文件夹下的所有文件(包括目录)
     * @param file 要删除的文件File对象
     */
    private void deleteFile(File file) {
        if(file.isDirectory()){
            // 表示该文件是文件夹
            String[] files = file.list();
            for(String childFilePath : files){
                File childFile = new File(file.getAbsolutePath() + File.separator + childFilePath);
                deleteFile(childFile);
            }
        }else{
            // 是文件
            //noinspection ResultOfMethodCallIgnored
            file.delete();
            TLog.error("删除媒体文件 == " + file.getAbsolutePath());
        }
    }

    /**
     * 获取图片的别名
     */
    private String getAlias(String uri) {
        if(StringUtils.isEmpty(uri)) {
            return "";
        }
        int index = uri.lastIndexOf(".");
        if(index == -1) {
            return "";
        }
        return UUID.randomUUID().toString() + uri.substring(index, uri.length());
    }

    private void saveAttendanceApprovalClock(Map<String, String> result, List<AttendanceApprovalSubmit> submitList) {
        OaApi.saveAttendanceApprovalClock(mApprovalClock.getAgency_id(),
                mApprovalClock.getId(), mApprovalClock.getName(),
                new Gson().toJson(result), submitList, new RequestCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {
                        showToast("提交成功");
                        hideLoadingDialog();
                        setResult(200);
                        finish();
                    }

                    @Override
                    public void onFailure(ApiHttpException e) {
                        showToast(e.getMessage());
                        hideLoadingDialog();
                    }
                });
    }

    @Override
    protected boolean hasToolBar() {
        return true;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }
}
