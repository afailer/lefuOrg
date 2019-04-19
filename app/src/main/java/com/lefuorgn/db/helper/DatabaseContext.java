package com.lefuorgn.db.helper;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import com.lefuorgn.util.TLog;

import java.io.File;
import java.io.IOException;

/**
 * 数据库的创建在指定的路径中
 */

public class DatabaseContext extends ContextWrapper {

    private String mDBDir;
    private Context mBase;


    public DatabaseContext(Context base, String path) {
        super(base);
        this.mBase = base;
        this.mDBDir = path;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
    }

    /**
     * android 4.0会调用此方法获取数据库
     * @param name 数据库名称
     * @param mode 创建模式
     * @param factory  Cursor工厂
     * @param errorHandler  DatabaseErrorHandler
     * @return SQLiteDatabase 数据库实例对象
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
    }

    @Override
    public File getDatabasePath(String name) {
        // 判断是否存在sd卡
        boolean sdExist = android.os.Environment.MEDIA_MOUNTED
                .equals(android.os.Environment.getExternalStorageState());
        // 标记数据库文件是否创建成功
        boolean isFileCreateSuccess = false;
        File dbFile;
        if (sdExist) {
            TLog.log("SD卡存在");
            // 判断目录是否存在，不存在则创建该目录
            File dirFile = new File(mDBDir);
            if (!dirFile.exists())
                dirFile.mkdirs();


            String dbPath = mDBDir + File.separator + name;// 数据库路径
            dbFile = new File(dbPath);
        }else { // 如果不存在,在本地内存创建数据库
            TLog.log("SD卡不存在");
            dbFile = mBase.getDatabasePath(name);
            File dirFile = new File(dbFile.getParent());
            if (!dirFile.exists())
                dirFile.mkdirs();
        }

        // 如果数据库文件不存在则创建该文件
        if (!dbFile.exists()) {
            try {
                isFileCreateSuccess = dbFile.createNewFile();// 创建文件
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            isFileCreateSuccess = true;
        }
        // 返回数据库文件对象
        if (isFileCreateSuccess)
            return dbFile;
        else {
            // 数据库创建失败
            return null;
        }
    }
}
