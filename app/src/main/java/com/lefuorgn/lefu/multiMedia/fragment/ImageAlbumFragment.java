package com.lefuorgn.lefu.multiMedia.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lefuorgn.R;
import com.lefuorgn.base.BaseFragment;
import com.lefuorgn.lefu.multiMedia.MultiMediaActivity;
import com.lefuorgn.lefu.multiMedia.adapter.ImageAlbumFragmentAdapter;
import com.lefuorgn.lefu.multiMedia.adapter.ImageAlbumPopupWindowAdapter;
import com.lefuorgn.lefu.multiMedia.bean.Image;
import com.lefuorgn.lefu.multiMedia.bean.ImageFolder;
import com.lefuorgn.lefu.multiMedia.util.ImageContentProvider;
import com.lefuorgn.util.DividerGridItemDecoration;
import com.lefuorgn.util.DividerItemDecoration;
import com.lefuorgn.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片选择相册页面
 */

public class ImageAlbumFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    // 图片展示适配器
    private ImageAlbumFragmentAdapter mImageAlbumAdapter;
    // 需要展示的图片的集合
    private List<Image> mSelectImages;
    // 文件展示信息容器
    private RelativeLayout mRelativeLayout;
    // 图片内容提供工具类
    private ImageContentProvider mProvider;
    // 图片文件夹集合
    private List<ImageFolder> mImageLoader;
    // 数据加载任务
    private DataAsyncTask mAsyncTask;
    // 文件夹展示信息
    private TextView mFolderView;
    // 当前文件夹中的个数
    private TextView mImageNumView;
    // 文件夹信息展示
    private PopupWindow mPopupWindow;
    // 当前页面可选择最多的图片
    private int mMaxNum;
    // 已经被选中的图片uri
    private List<String> mSelectInfo;

    private boolean first; // 是否是第一次加载

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_multi_media_image_album;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        // 初始化toolBar
        view.findViewById(R.id.iv_fragment_multi_media_image_album_back).setOnClickListener(this);
        view.findViewById(R.id.tv_fragment_multi_media_image_album_complete).setOnClickListener(this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_fragment_multi_media_image_album);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(Color.TRANSPARENT, 3));
        mRelativeLayout = (RelativeLayout) view.findViewById(R.id.rl_fragment_multi_media_image_album);
        mRelativeLayout.setOnClickListener(this);
        mFolderView = (TextView) view.findViewById(R.id.tv_fragment_multi_media_image_album_folder);
        mImageNumView = (TextView) view.findViewById(R.id.tv_fragment_multi_media_image_album_num);
    }

    @Override
    protected void initData() {
        first = true;
        mSelectImages = new ArrayList<Image>();
        // 获取当前最多选择的图片
        mMaxNum = getArguments().getInt(MultiMediaActivity.INTENT_ARGUMENTS);
        // 获取被选中的图片uri
        mSelectInfo = new ArrayList<String>();
        String data = getArguments().getString(MultiMediaActivity.INTENT_ARGUMENTS_SELECT);
        if(!StringUtils.isEmpty(data)) {
            String[] split = data.split(",");
            for (String s : split) {
                if(!StringUtils.isEmpty(s)) {
                    mSelectInfo.add(s);
                }
            }
        }
        // 统计当前页面可以选择图片的最大个数
        mMaxNum = mMaxNum + mSelectInfo.size();
        // 初始化被选中的容器
        mProvider = ImageContentProvider.getInstance(getContext());
        mAsyncTask = new DataAsyncTask();
        mAsyncTask.execute("所有图片");
    }

    /**
     * 获取文件夹以及文件夹中的内容
     */
    private class DataAsyncTask extends AsyncTask<String, Void, List<Image>> {

        @Override
        protected List<Image> doInBackground(String... params) {
            if(first) {
                // 获取文件夹信息
                mImageLoader = mProvider.getImageFolder();
                first = false;
            }

            List<Image> list;
            if("所有图片".equals(params[0])) {
                list = mProvider.getImageUri();
            }else {
                list = mProvider.getImageUriByFolderName(params[0]);
            }
            mSelectImages.clear();
            for (Image image : list) {
                if(mSelectInfo.size() == 0) {
                    break;
                }
                // 过滤相同的地址
                String url = "";
                for (String s : mSelectInfo) {
                    if(image.getUrl() != null && image.getUrl().equals(s)) {
                        url = s;
                        image.setSelect(true);
                        mSelectImages.add(image);
                        break;
                    }
                }
                if(!StringUtils.isEmpty(url)) {
                    mSelectInfo.remove(url);
                }
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<Image> images) {
            // 设置当前文件夹信息
            mFolderView.setText(mImageLoader.get(0).getAlbumName());
            String num = images.size() - 1 + "张";
            mImageNumView.setText(num);
            if(mImageAlbumAdapter == null) {
                mImageAlbumAdapter = new ImageAlbumFragmentAdapter(images);
                mRecyclerView.setAdapter(mImageAlbumAdapter);
                mImageAlbumAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {
                        if(i == 0) {
                            // 跳转到拍照页面
                            redirectTo();
                            return;
                        }
                        // 条目的点击事件
                        Image img = mImageAlbumAdapter.getItem(i);
                        if(img.isSelect()) {
                            // 当前图片是被选中的状态,要将其置为未选中状态
                            img.setSelect(!img.isSelect());
                            mSelectImages.remove(img);
                        }else {
                            if(mSelectImages.size() >= mMaxNum) {
                                // 当前图片已经选择了9张,就不能再选择了
                                showToast("最多选择" + mMaxNum + "张图片");
                                return;
                            }else {
                                img.setSelect(!img.isSelect());
                                mSelectImages.add(img);
                            }
                        }
                        mImageAlbumAdapter.notifyDataSetChanged();
                    }
                });
            }else {
                mImageAlbumAdapter.setNewData(images);
            }

        }
    }

    /**
     * 跳转到拍照页面
     */
    private void redirectTo() {
        Fragment fragment = new ImageCaptureFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        tx.replace(R.id.fl_activity_multi_media, fragment, "ImageCaptureFragment");
        tx.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_fragment_multi_media_image_album_back:
                // 回退
                getActivity().finish();
                break;
            case R.id.tv_fragment_multi_media_image_album_complete:
                // 完成
                Intent intent = new Intent();
                boolean first = true;
                StringBuilder sb = new StringBuilder();
                for (Image image : mSelectImages) {
                    if(first) {
                        sb.append(image.getUrl());
                        first = false;
                    }else {
                        sb.append(",").append(image.getUrl());
                    }
                }
                intent.putExtra(MultiMediaActivity.RESULT_NAME, sb.toString());
                getActivity().setResult(MultiMediaActivity.RESULT_IMAGE_ALBUM, intent);
                getActivity().finish();
                break;
            case R.id.rl_fragment_multi_media_image_album:
                showPopupWindow();
                break;
        }
    }

    /**
     * 显示PopupWindow
     */
    @SuppressWarnings("deprecation")
    private void showPopupWindow() {
        if(mPopupWindow == null) {
            WindowManager manager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics outMetrics = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(outMetrics);
            // 获取屏幕的宽高
            int width = outMetrics.widthPixels;
            int height = outMetrics.heightPixels * 2 / 3;
            // 创建PopupWindow
            mPopupWindow = new PopupWindow(initPopupWindow(), width, height, true);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setAnimationStyle(R.style.anim_image_folder_style);
            mPopupWindow.showAsDropDown(mRelativeLayout);
        }else {
            mPopupWindow.showAsDropDown(mRelativeLayout);
        }
    }

    /**
     * 初始化分组PopupWindow视图
     */
    private View initPopupWindow() {
        @SuppressLint("InflateParams")
        View view = getActivity().getLayoutInflater().inflate(R.layout.popup_window_image_album, null);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv_popup_window_image_album);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST,
                getResources().getColor(R.color.recycler_view_item_division_color)));
        final ImageAlbumPopupWindowAdapter adapter = new ImageAlbumPopupWindowAdapter(mImageLoader);
        rv.setAdapter(adapter);
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                for (int index = 0; index < adapter.getData().size(); index++) {
                    ImageFolder folder = adapter.getItem(index);
                    folder.setChecked(false);
                }
                ImageFolder folder = adapter.getItem(i);
                folder.setChecked(true);
                adapter.notifyDataSetChanged();
                mPopupWindow.dismiss();
                if (mAsyncTask != null) {
                    mAsyncTask.cancel(true);
                }
                mAsyncTask = new DataAsyncTask();
                mAsyncTask.execute(folder.getAlbumName());
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mAsyncTask != null) {
            mAsyncTask.cancel(true);
            mAsyncTask = null;
        }
    }
}
