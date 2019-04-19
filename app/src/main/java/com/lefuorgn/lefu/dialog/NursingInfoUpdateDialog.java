package com.lefuorgn.lefu.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lefuorgn.R;
import com.lefuorgn.lefu.adapter.NursingMultiMediaAdapter;
import com.lefuorgn.lefu.bean.MultiMedia;
import com.lefuorgn.lefu.multiMedia.MultiMediaActivity;
import com.lefuorgn.lefu.multiMedia.PreviewMultiMediaActivity;
import com.lefuorgn.lefu.multiMedia.PreviewPictureActivity;
import com.lefuorgn.util.DividerGridItemDecoration;
import com.lefuorgn.util.StringUtils;
import com.lefuorgn.util.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 护理信息信息基本数据修改框
 */

public class NursingInfoUpdateDialog extends DialogFragment implements View.OnClickListener{

    private EditText mEditView;

    // 按钮点击回调事件
    private ClickCallBack mClickCallBack;
    // dialog标题
    private String mTitle;
    private String mRemarks = "";

    private NursingMultiMediaAdapter mMultiMediaAdapter;

    private List<MultiMedia> mMultiMedias = new ArrayList<MultiMedia>();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if(event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    // 销毁当前创建的多媒体信息
                    destroyMultiMedia();
                    dialog.dismiss();
                    return true;
                }
                return false;
            }

        });
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_nursing_update_info, null);
        ((TextView) view.findViewById(R.id.tv_dialog_nursing_update_info_title)).setText(mTitle);
        mEditView = (EditText) view.findViewById(R.id.iv_dialog_nursing_update_info_remark);
        mEditView.setText(mRemarks);
        mEditView.setSelection(mEditView.length());
        view.findViewById(R.id.iv_dialog_nursing_update_info_close).setOnClickListener(this);
        view.findViewById(R.id.tv_dialog_nursing_update_info_pic).setOnClickListener(this);
        view.findViewById(R.id.tv_dialog_nursing_update_info_video).setOnClickListener(this);
        view.findViewById(R.id.tv_dialog_nursing_update_info_audio).setOnClickListener(this);
        view.findViewById(R.id.btn_dialog_nursing_update_info_input).setOnClickListener(this);
        // 多媒体信息展示控件
        RecyclerView mRView = (RecyclerView) view.findViewById(R.id.rv_dialog_nursing_update_info_multi_media);
        mRView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRView.addItemDecoration(new DividerGridItemDecoration(Color.WHITE, 3));
        mMultiMediaAdapter = new NursingMultiMediaAdapter(mMultiMedias);
        mRView.setAdapter(mMultiMediaAdapter);
        initAdapterListener();
        Dialog dialog = builder.create();
        initDialogWindow(view, dialog);
        return dialog;
    }

    /**
     * 初始化Dialog界面信息
     * @param view 当前界面显示的View
     * @param dialog 当前Dialog
     */
    private void initDialogWindow(View view, Dialog dialog) {
        // 自动弹出软键盘
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);

        // 获取窗体的参数配置对象
        // 获取手机页面的宽度
        WindowManager wManager = getActivity().getWindowManager();
        Display display = wManager.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        int width = (int) (outMetrics.widthPixels * 0.94);
        // 获取dialog的窗体对象
        Window window = dialog.getWindow();
        if(window != null) {
            // 取消dialog在软键盘之上的设置
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            // 获取窗体的参数配置对象
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            int height = layoutParams.height;
            window.setLayout(width, height);
        }
    }

    /**
     * 为Adapter添加事件
     */
    private void initAdapterListener() {
        // 条目点击事件
        mMultiMediaAdapter.setOnRecyclerViewItemClickListener(
                new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                // 多媒体信息预览
                MultiMedia multiMedia = mMultiMediaAdapter.getItem(i);
                previewMultiMedia(multiMedia);
            }
        });
        // 条目上删除按钮点击事件
        mMultiMediaAdapter.setOnRecyclerViewItemChildClickListener(
                new BaseQuickAdapter.OnRecyclerViewItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                // 移除当前条目
                MultiMedia multiMedia = mMultiMediaAdapter.getItem(i);
                if(multiMedia.getSourceType() != MultiMedia.SOURCE_FROM_LOCAL) {
                    // 资源来源于当前应用创建
                    File file = new File(multiMedia.getUri());
                    if(file.exists()) {
                        // 当前文件存在
                        file.delete();
                    }
                }
                mMultiMediaAdapter.getData().remove(i);
                mMultiMediaAdapter.notifyDataSetChanged();
            }
        });
   }

    /**
     * 预览多媒体文件
     */
    private void previewMultiMedia(MultiMedia multiMedia) {
        int type = multiMedia.getType();
        String path = multiMedia.getUri();
        Intent intent;
        if(type == MultiMedia.MULTI_MEDIA_TYPE_VIDEO) {
            // 播放视频
            intent = new Intent(getActivity(), PreviewMultiMediaActivity.class);
            intent.putExtra(PreviewMultiMediaActivity.MULTI_MEDIA_URI, path);
            startActivity(intent);
        }else if(type == MultiMedia.MULTI_MEDIA_TYPE_PICTURE) {
            // 预览图片
            intent = new Intent(getActivity(), PreviewPictureActivity.class);
            intent.putExtra(PreviewPictureActivity.INTENT_PICTURE_URI, path);
            startActivity(intent);
        }else if(type == MultiMedia.MULTI_MEDIA_TYPE_AUDIO) {
            // 预览音频
            Uri uri = Uri.fromFile(new File(path));
            intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setDataAndType(uri, "audio/mp3");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_dialog_nursing_update_info_close:
                // 关闭同步选择框, 并销毁当前创建的多媒体信息
                destroyMultiMedia();
                dismiss();
                break;
            case R.id.tv_dialog_nursing_update_info_pic:
                if(mMultiMedias.size() >= 9) {
                    ToastUtils.show(getContext(), "最多添加9项");
                    return;
                }
                // 获取图片
                intent = new Intent(getActivity(), MultiMediaActivity.class);
                intent.putExtra(MultiMediaActivity.MULTIMEDIA_TYPE, MultiMediaActivity.ACTION_IMAGE_ALBUM);
                intent.putExtra(MultiMediaActivity.INTENT_ARGUMENTS, 9 - mMultiMedias.size());
                // 传递当前选择的图片
                StringBuilder sBuilder = new StringBuilder();
                boolean aFirst = true;
                for (MultiMedia multiMedia : mMultiMedias) {
                    if(multiMedia.getSourceType() == MultiMedia.SOURCE_FROM_LOCAL) {
                        if(aFirst) {
                            sBuilder.append(multiMedia.getUri());
                            aFirst = false;
                        }else {
                            sBuilder.append(",").append(multiMedia.getUri());
                        }
                    }
                }
                intent.putExtra(MultiMediaActivity.INTENT_ARGUMENTS_SELECT, sBuilder.toString());
                startActivityForResult(intent, 100);
                break;
            case R.id.tv_dialog_nursing_update_info_video:
                if(mMultiMedias.size() >= 9) {
                    ToastUtils.show(getContext(), "最多添加9项");
                    return;
                }
                // 获取视频
                intent = new Intent(getActivity(), MultiMediaActivity.class);
                intent.putExtra(MultiMediaActivity.MULTIMEDIA_TYPE, MultiMediaActivity.ACTION_VIDEO_CAPTURE);
                startActivityForResult(intent, 100);
                break;
            case R.id.tv_dialog_nursing_update_info_audio:
                if(mMultiMedias.size() >= 9) {
                    ToastUtils.show(getContext(), "最多添加9项");
                    return;
                }
                // 获取音频
                intent = new Intent(getActivity(), MultiMediaActivity.class);
                intent.putExtra(MultiMediaActivity.MULTIMEDIA_TYPE, MultiMediaActivity.ACTION_AUDIO);
                startActivityForResult(intent, 100);
                break;
            case R.id.btn_dialog_nursing_update_info_input:
                // 提交数据
                submitData();
                break;
        }
    }

    /**
     * 提交数据
     */
    private void submitData() {
        String remarks = mEditView.getText().toString();
        if(mClickCallBack != null) {
            mClickCallBack.saveClick(remarks, mMultiMedias);
        }
        dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == MultiMediaActivity.RESULT_IMAGE_CAPTURE && data != null) {
            // 从相机中获取的照片, 数量为1
            MultiMedia multiMedia = new MultiMedia();
            multiMedia.setSourceType(MultiMedia.SOURCE_FROM_CAMERA);
            multiMedia.setType(MultiMedia.MULTI_MEDIA_TYPE_PICTURE);
            multiMedia.setUri(data.getStringExtra(MultiMediaActivity.RESULT_NAME));
            mMultiMedias.add(multiMedia);
            mMultiMediaAdapter.notifyDataSetChanged();
        }else if(resultCode == MultiMediaActivity.RESULT_IMAGE_ALBUM && data != null) {
            // 从相册中选中的照片,数量为1~9张
            String images = data.getStringExtra(MultiMediaActivity.RESULT_NAME);
            String[] split = images.split(",");
            // 获取返回的结果
            List<String> uri = new ArrayList<String>();
            for (String s : split) {
                if(!StringUtils.isEmpty(s)) {
                    uri.add(s);
                }
            }
            // 存放当前页面要被移除的图片
            List<MultiMedia> nonExistent = new ArrayList<MultiMedia>();
            // 过滤选择的
            for (MultiMedia multiMedia : mMultiMedias) {
                if(multiMedia.getSourceType() == MultiMedia.SOURCE_FROM_LOCAL
                        && multiMedia.getType() == MultiMedia.MULTI_MEDIA_TYPE_PICTURE) {
                    // 本地图片
                    String url = "";
                    for (String s : uri) {
                        if(multiMedia.getUri().equals(s)) {
                            url = s;
                            break;
                        }
                    }
                    if(!StringUtils.isEmpty(url)) {
                        // 当前已经存在
                        uri.remove(url);
                    }else {
                        // 当前页面要移除的
                        nonExistent.add(multiMedia);
                    }
                }
            }
            // 移除被放弃的
            mMultiMedias.removeAll(nonExistent);
            // 添加新增的
            for (String s : uri) {
                MultiMedia multiMedia = new MultiMedia();
                multiMedia.setSourceType(MultiMedia.SOURCE_FROM_LOCAL);
                multiMedia.setType(MultiMedia.MULTI_MEDIA_TYPE_PICTURE);
                multiMedia.setUri(s);
                mMultiMedias.add(multiMedia);
            }
            mMultiMediaAdapter.notifyDataSetChanged();
        }else if(resultCode == MultiMediaActivity.RESULT_VIDEO_CAPTURE && data != null) {
            // 返回视频录取地址
            String url = data.getStringExtra(MultiMediaActivity.RESULT_NAME);
            MultiMedia multiMedia = new MultiMedia();
            multiMedia.setSourceType(MultiMedia.SOURCE_FROM_CAMERA);
            multiMedia.setType(MultiMedia.MULTI_MEDIA_TYPE_VIDEO);
            multiMedia.setUri(url);
            mMultiMedias.add(multiMedia);
            mMultiMediaAdapter.notifyDataSetChanged();
        }else if(resultCode == MultiMediaActivity.RESULT_AUDIO && data != null) {
            // 返回视频录取地址
            String url = data.getStringExtra(MultiMediaActivity.RESULT_NAME);
            MultiMedia multiMedia = new MultiMedia();
            multiMedia.setSourceType(MultiMedia.SOURCE_FROM_CAMERA);
            multiMedia.setType(MultiMedia.MULTI_MEDIA_TYPE_AUDIO);
            multiMedia.setUri(url);
            mMultiMedias.add(multiMedia);
            mMultiMediaAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 销毁当前应用创建的多媒体信息
     */
    private void destroyMultiMedia() {
        if(mMultiMedias == null) {
            // 当前没有多媒体信息
            return;
        }
        for (MultiMedia multiMedia : mMultiMedias) {
            if(multiMedia.getSourceType() == MultiMedia.SOURCE_FROM_CAMERA) {
                // 删除当前资源
                File file = new File(multiMedia.getUri());
                if(file.exists()) {
                    // 当前文件存在
                    file.delete();
                }
            }
        }
    }

    /**
     * 设置dialog的标题和内容
     * @param title 条目名称
     * @return 当前对象
     */
    public NursingInfoUpdateDialog setTitle(String title) {
        mTitle = title;
        return this;
    }

    /**
     * 初始化备注信息
     * @param remarks 备注信息
     * @return 当前对象
     */
    public NursingInfoUpdateDialog setRemarks(String remarks) {
        this.mRemarks = remarks;
        return this;
    }

    /**
     * 初始化媒体信息
     * @param multiMedias 已选的多媒体信息
     */
    public NursingInfoUpdateDialog setMultiMedias(List<MultiMedia> multiMedias) {
        this.mMultiMedias = multiMedias;
        return this;
    }

    /**
     * 设置按钮点击回调接口
     * @param callBack 回调接口函数对象
     * @return 当前对象
     */
    public NursingInfoUpdateDialog setClickCallBack(ClickCallBack callBack) {
        mClickCallBack = callBack;
        return this;
    }

    /**
     * 点击事件触发接口回调函数
     */
    public interface ClickCallBack {

        /**
         * 保存数据事件, 包括选择的多媒体信息和老人信息
         * @param remarks 备注
         * @param multiMedia 多媒体信息
         */
        void saveClick(String remarks, List<MultiMedia> multiMedia);

    }

}
