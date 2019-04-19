package com.lefuorgn.interactive.module;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.j256.ormlite.stmt.QueryBuilder;
import com.lefuorgn.AppConfig;
import com.lefuorgn.AppContext;
import com.lefuorgn.api.common.MultiMediaUpload;
import com.lefuorgn.api.common.RequestCallback;
import com.lefuorgn.api.http.exception.ApiHttpException;
import com.lefuorgn.api.remote.synch.UploadApi;
import com.lefuorgn.db.base.BaseDao;
import com.lefuorgn.db.model.base.BaseMediaUpload;
import com.lefuorgn.db.util.DaoHelper;
import com.lefuorgn.interactive.bean.UploadMediaTableTree;
import com.lefuorgn.interactive.interf.OnSyncChangeListener;
import com.lefuorgn.util.ImageCompressionUtils;
import com.lefuorgn.util.TLog;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 多媒体文件上传模块
 */

public class FileModule {

    private static FileModule instance;
    private DaoHelper mDaoHelper;
    private Handler mCurrentHandler;
    private long mLimitedNumber;

    private OnSyncChangeListener mListener;

    private FileModule (Looper looper) {
        mDaoHelper = DaoHelper.getInstance();
        mCurrentHandler = new Handler(looper){
            @Override
            public void handleMessage(Message msg) {
                // 切换上传表进行上传
                switchTable(msg);
            }
        };
    }

    /**
     * 获取当前多媒体信息上传模块的实例
     *
     * @param looper 指定线程中的Looper
     * @return 当前类实例化对象
     */
    public static FileModule getInstance(Looper looper) {
        if(instance == null) {
            synchronized (FileModule.class) {
                if(instance == null) {
                    instance = new FileModule(looper);
                }
            }
        }
        return instance;
    }

    /**
     * 开启上传模块,上传服务
     * @param tree 下载表链表
     * @param <T> 泛型
     */
    public <T extends BaseMediaUpload> void start(UploadMediaTableTree<T> tree) {
        if(tree == null) {
            throw new IllegalArgumentException("The tree is null");
        }
        if(mListener != null) {
            mListener.onStart();
            // 开始
            mListener.onStateChange(tree.getClazz(), OnSyncChangeListener.STATE_START);
        }
        uploadDataToServer(1, tree);
    }

    /**
     * 切换表进行下载
     * @param msg 上一个节点的同步信息
     * @param <T> 泛型
     */
    @SuppressWarnings("unchecked")
    private <T extends BaseMediaUpload> void switchTable(Message msg) {

        UploadMediaTableTree<T> tree = (UploadMediaTableTree<T>) msg.obj;
        // 表下载完成,
        clearTable(tree.getClazz());
        // 数据下载完成
        if (mListener != null) {
            mListener.onStateChange(tree.getClazz(), OnSyncChangeListener.STATE_SUCCESS);
        }
        // 是否还存在其他表要下载
        if(tree.getSubClazz() != null) {
            // 新表开始下载
            if (mListener != null) {
                mListener.onStateChange(tree.getClazz(), OnSyncChangeListener.STATE_START);
            }
            uploadDataToServer(1, tree.getSubClazz());
        }else if (mListener != null){
            // 所有表下载完毕, 结束点
            clearFiles();
            ImageCompressionUtils.clear(AppContext.getInstance());
            mListener.onEnd();
        }
    }

    /**
     * 上传指定表中的数据到服务器
     * @param pageNo 当前上传的页面
     * @param tree 当前表节点
     */
    private <T extends BaseMediaUpload> void uploadDataToServer(long pageNo, final UploadMediaTableTree<T> tree) {
        // 从数据库中获取数据
        List<T> files = getDataFromBD(pageNo, tree.getClazz());
        if(files.size() > 0) {
            // 当前存在要提交的额数据
            final long fPageNo = ++pageNo;
            List<MultiMediaUpload> result = filterData(files);
            if(result.size() == 0) {
                uploadDataToServer(fPageNo, tree);
                return;
            }
            UploadApi.uploadMultiMedia(result, new RequestCallback<String>(mCurrentHandler) {

                @Override
                public void onSuccess(String result) {
                    // 继续提交数据
                    uploadDataToServer(fPageNo, tree);
                }

                @Override
                public void onFailure(ApiHttpException e) {
                    // 数据上传失败
                    uploadDataToServer(fPageNo, tree);
                }

            });
        }else {
            // 当前不存在要提交的数据
            if(mListener != null && pageNo == 1) {
                // 空表
                mListener.onStateChange(tree.getClazz(), OnSyncChangeListener.STATE_UPLOAD_NULL);
            }
            // 数据上传完毕
            Message msg = Message.obtain();
            msg.obj = tree;
            mCurrentHandler.sendMessage(msg);
        }
    }

    /**
     * 获取指定表中指定页面的数据
     * @param pageNo 当前页面,从1开始
     * @param clazz 当前表类字节码
     * @return 要上传的数据
     */
    private <T extends BaseMediaUpload> List<T> getDataFromBD(long pageNo, Class<T> clazz) {
        if(pageNo < 1) {
            throw new IllegalArgumentException("The pageNo is less than 1, the current pageNo = " + pageNo);
        }
        BaseDao<T, Long> dao = mDaoHelper.getDao(clazz);
        try {
            QueryBuilder<T, Long> queryBuilder = dao.getDao().queryBuilder();
            queryBuilder.distinct() // 去掉重复的数据
                    .groupBy("path") // 以路径字段分组
                    .offset((pageNo - 1) * mLimitedNumber)
                    .limit(mLimitedNumber);
            return dao.queryList(queryBuilder.prepare());
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<T>();
    }

    /**
     * 过滤不存在的文件
     * @param media 所有的媒体文件
     */
    private <T extends BaseMediaUpload> List<MultiMediaUpload> filterData(List<T> media) {
        List<MultiMediaUpload> result = new ArrayList<MultiMediaUpload>();
        for (T t : media) {
            File file = new File(t.getPath());
            if(file.exists()) {
                if(t.getType() == MultiMediaUpload.MULTI_MEDIA_TYPE_PICTURE) {
                    // 图片
                    file = ImageCompressionUtils.getInstance(AppContext.getInstance()).compress(file);
                }
                MultiMediaUpload upload = new MultiMediaUpload();
                upload.setType(t.getType());
                upload.setFile(file);
                upload.setAlias(t.getAlias());
                result.add(upload);
            }
        }
        return result;
    }

    /**
     * 清除指定表中的数据
     * @param clazz 当前上传表字节码
     * @param <T> 泛型
     */
    private <T extends BaseMediaUpload> void clearTable(Class<T> clazz) {
        mDaoHelper.getDao(clazz).clearTable();
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
            file.delete();
            TLog.error("删除媒体文件 == " + file.getAbsolutePath());
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
     * 设置限制条目数
     */
    public void setLimitedNumber(long limitedNumber) {
        this.mLimitedNumber = limitedNumber;
    }

    /**
     * 设置当前上传操作监听器
     *
     * @param l 同步状态监听接口
     */
    public void setOnSyncChangeListener(OnSyncChangeListener l) {
        if (l == null) {
            throw new IllegalArgumentException(
                    "The OnDownloadStateChangeListener is null");
        }
        mListener = l;
    }
}
