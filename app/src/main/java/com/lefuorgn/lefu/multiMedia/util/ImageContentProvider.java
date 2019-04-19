package com.lefuorgn.lefu.multiMedia.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Images.Media;

import com.lefuorgn.lefu.multiMedia.bean.Image;
import com.lefuorgn.lefu.multiMedia.bean.ImageFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 本地图片获取工具类, 获取的图片都来自SD卡上;
 * 如果图片数量很大的时候需要在子线程中执行; 不然会奔溃
 */
public class ImageContentProvider {

    private static ImageContentProvider instance;

    /**
     * 本地存储文件夹中过滤图片不得小于10K
     */
    private static final int IMAGE_MIN_SIZE = 10240;
    /**
     * 记录当前SD卡是否挂载
     */
    private boolean sdExist;

    private ContentResolver mContentResolver;

    private ImageContentProvider(Context context) {
        mContentResolver = context.getContentResolver();
        // 判断是否存在sd卡
        sdExist = android.os.Environment.MEDIA_MOUNTED
                .equals(android.os.Environment.getExternalStorageState());
    }

    public static ImageContentProvider getInstance(Context context) {
        if(instance == null) {
            synchronized (ImageContentProvider.class) {
                if(instance == null) {
                    instance = new ImageContentProvider(context);
                }
            }
        }
        return instance;
    }

    /**
     * 获取所有图片文件夹信息
     * @return 图片文件夹信息集合
     */
    public List<ImageFolder> getImageFolder() {
        List<ImageFolder> folders = new ArrayList<ImageFolder>();
        ImageFolder folder = new ImageFolder();
        folder.setAlbumName("所有图片");
        folder.setCount(0);
        folder.setChecked(true);
        folders.add(folder);
        if(!sdExist) {
            // SD卡没有挂载
            return folders;
        }
        Cursor cursor = mContentResolver.query(Media.EXTERNAL_CONTENT_URI
                , new String[] {ImageColumns.DATA, ImageColumns.BUCKET_DISPLAY_NAME, ImageColumns.SIZE}
                , null, null, null);
        if(cursor == null) {
           // cursor为null
            return folders;
        }
        Map<String, ImageFolder> map = new HashMap<String, ImageFolder>();
        boolean first = true;
        // 获取图片数据
        while (cursor.moveToNext()) {
            // 文件大小
            int size = cursor.getInt(cursor.getColumnIndex(ImageColumns.SIZE));
            String uri = cursor.getString(cursor.getColumnIndex(ImageColumns.DATA));
            if(size < IMAGE_MIN_SIZE || !new File(uri).exists()) {
                // 小于10K的文件不考虑
                continue;
            }
            // 所有图片文件夹个数自加1
            folder.setCount(folder.getCount() + 1);
            String name = cursor.getString(cursor.getColumnIndex(ImageColumns.BUCKET_DISPLAY_NAME));
            if(map.containsKey(name)) {
                // 当前文件夹已经统计过了
                ImageFolder f = map.get(name);
                f.setCount(f.getCount() + 1);
            }else {
                if(first) {
                    folder.setAlbumPath(uri);
                    first = false;
                }
                ImageFolder f = new ImageFolder();
                f.setAlbumName(name);
                f.setCount(1);
                f.setAlbumPath(uri);
                map.put(name, f);
                folders.add(f);
            }
        }
        cursor.close();
        return folders;
    }

    /**
     * 获取所有的图片地址信息
     * @return 图片地址集合
     */
    public List<Image> getImageUri() {
        return getImages(null, null);
    }

    /**
     * 通过文件夹名称获取
     * @param name 文件夹名称
     * @return 图片地址集合
     */
    public List<Image> getImageUriByFolderName(String name) {
        return getImages(ImageColumns.BUCKET_DISPLAY_NAME + " = ?", new String[] { name });
    }

    /**
     * 根据条件获取SD中的图片
     * @return 图片信息集合
     */
    private List<Image> getImages(String selection, String[] selectionArgs) {
        List<Image> uriList = new ArrayList<Image>();
        Image image = new Image();
        uriList.add(image);
        if(!sdExist) {
            // SD卡没有挂载
            return uriList;
        }
        Cursor cursor = mContentResolver.query(Media.EXTERNAL_CONTENT_URI
                , new String[] {ImageColumns.DATA, ImageColumns.SIZE }
                , selection, selectionArgs, ImageColumns.DATE_ADDED);
        if (cursor == null) {
            return uriList;
        }
        // 移动到最后一张图片
        if(cursor.moveToLast()) {
            // 存在并插入当前图片
            Image img;
            String uri;
            if(cursor.getLong(cursor.getColumnIndex(ImageColumns.SIZE)) >= IMAGE_MIN_SIZE) {
                uri = cursor.getString(cursor.getColumnIndex(ImageColumns.DATA));
                if(new File(uri).exists()) {
                    img = new Image();
                    img.setUrl(uri);
                    uriList.add(img);
                }
            }

            // 继续向前遍历图片
            while (cursor.moveToPrevious()) {
                if(cursor.getLong(cursor.getColumnIndex(ImageColumns.SIZE)) >= IMAGE_MIN_SIZE) {
                    uri = cursor.getString(cursor.getColumnIndex(ImageColumns.DATA));
                    if(new File(uri).exists()) {
                        img = new Image();
                        img.setUrl(uri);
                        uriList.add(img);
                    }
                }
            }
        }
        cursor.close();
        return uriList;
    }

}
