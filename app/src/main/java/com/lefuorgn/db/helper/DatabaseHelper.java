package com.lefuorgn.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.lefuorgn.db.model.basic.AllocatingTypeTask;
import com.lefuorgn.db.model.basic.BatchEditingTask;
import com.lefuorgn.db.model.basic.Bed;
import com.lefuorgn.db.model.basic.Dictionary;
import com.lefuorgn.db.model.basic.DisplaySignOrNursingItem;
import com.lefuorgn.db.model.basic.NursingItem;
import com.lefuorgn.db.model.basic.OldPeople;
import com.lefuorgn.db.model.basic.OldPeopleFamily;
import com.lefuorgn.db.model.basic.Permission;
import com.lefuorgn.db.model.basic.SignAndNursingTask;
import com.lefuorgn.db.model.basic.SignConfig;
import com.lefuorgn.db.model.basic.SignIntervalColor;
import com.lefuorgn.db.model.basic.SignIntervalPointColor;
import com.lefuorgn.db.model.basic.StaffCache;
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
import com.lefuorgn.db.model.upload.BreathingUpload;
import com.lefuorgn.db.model.upload.DailyLifeMediaUpload;
import com.lefuorgn.db.model.upload.DailyLifeUpload;
import com.lefuorgn.db.model.upload.DailyNursingMediaUpload;
import com.lefuorgn.db.model.upload.DailyNursingUpload;
import com.lefuorgn.db.model.upload.DefecationUpload;
import com.lefuorgn.db.model.upload.DrinkingUpload;
import com.lefuorgn.db.model.upload.EatUpload;
import com.lefuorgn.db.model.upload.PressureUpload;
import com.lefuorgn.db.model.upload.PulseUpload;
import com.lefuorgn.db.model.upload.SleepingUpload;
import com.lefuorgn.db.model.upload.SugarUpload;
import com.lefuorgn.db.model.upload.TemperatureUpload;
import com.lefuorgn.util.TLog;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	
	/**
	 * 默认数据库名称
	 */
	private static final String DB_NAME_DEFAULT = "lefuOrg.db";

	private static final String ROOT_SDCARD = Environment
			.getExternalStorageDirectory().getAbsolutePath();

	/**
	 * 当前数据库版本号
	 */
	private static final int DB_VERSION = 12;
	/**
	 * 当前数据库名称,默认数据库名称
	 */
	private static String sDatabaseName = DB_NAME_DEFAULT;
	
	private static DatabaseHelper instance;

	private DatabaseHelper(Context context, String databaseName) {
		super(new DatabaseContext(context, ROOT_SDCARD + "/lefuOrg/databases"), databaseName, null, DB_VERSION);
	}

	/**
	 * 单例获取该Helper
	 * @param context 环境上下文
	 */
	public static DatabaseHelper getInstance(Context context) {
		if (instance == null) {
			synchronized (DatabaseHelper.class) {
				if (instance == null) {
					// 创建数据库操作对象或者进行切库
					context = context.getApplicationContext();
					instance = new DatabaseHelper(context, sDatabaseName);
				}
			}
		}
		return instance;
	}

    /**
     * 重置数据库; 数据库不存在则创建, 存在如果数据库名称不同则重新创建
     * @param context 环境上下文
     * @param databaseName 数据库名称
     */
    public static void reset(Context context, String databaseName) {
        if(instance == null) {
            // 数据库操作对象不存在
            TLog.log("数据库操作对象不存在");
            sDatabaseName = databaseName;
            getInstance(context);
        }else if (!sDatabaseName.equals(databaseName)) {
            // 数据库对象存在, 且数据库发生变化
            TLog.log("数据库对象存在, 且数据库发生变化");
            sDatabaseName = databaseName;
            instance = null;
            getInstance(context);
        }else {
            TLog.log("数据库对象存在, 且数据库没有发生变化");
        }
    }

    /**
     * 将数据库名称置为默认的情况
     */
    public static void restDatabaseName() {
        if(instance != null) {
            instance = null;
        }
        sDatabaseName = DB_NAME_DEFAULT;
    }
	
	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
		TLog.log("创建数据库表");
		try {
			TableUtils.createTable(connectionSource, PulseDownload.class);
			TableUtils.createTable(connectionSource, PressureDownload.class);
			TableUtils.createTable(connectionSource, SugarDownload.class);
			TableUtils.createTable(connectionSource, TemperatureDownload.class);
			TableUtils.createTable(connectionSource, DrinkingDownload.class);
			TableUtils.createTable(connectionSource, SleepingDownload.class);
			TableUtils.createTable(connectionSource, EatDownload.class);
			TableUtils.createTable(connectionSource, DefecationDownload.class);
			TableUtils.createTable(connectionSource, BreathingDownload.class);
			TableUtils.createTable(connectionSource, DailyLifeDownload.class);
			TableUtils.createTable(connectionSource, DailyNursingDownload.class);

			TableUtils.createTable(connectionSource, SignConfig.class);
			TableUtils.createTable(connectionSource, SignIntervalColor.class);
			TableUtils.createTable(connectionSource, SignIntervalPointColor.class);
			TableUtils.createTable(connectionSource, DisplaySignOrNursingItem.class);
			TableUtils.createTable(connectionSource, NursingItem.class);
			TableUtils.createTable(connectionSource, Permission.class);
			TableUtils.createTable(connectionSource, Dictionary.class);

			TableUtils.createTable(connectionSource, Bed.class);
			TableUtils.createTable(connectionSource, OldPeople.class);
			TableUtils.createTable(connectionSource, OldPeopleFamily.class);

			TableUtils.createTable(connectionSource, PulseUpload.class);
			TableUtils.createTable(connectionSource, PressureUpload.class);
			TableUtils.createTable(connectionSource, SugarUpload.class);
			TableUtils.createTable(connectionSource, TemperatureUpload.class);
			TableUtils.createTable(connectionSource, DrinkingUpload.class);
			TableUtils.createTable(connectionSource, SleepingUpload.class);
			TableUtils.createTable(connectionSource, EatUpload.class);
			TableUtils.createTable(connectionSource, DefecationUpload.class);
			TableUtils.createTable(connectionSource, BreathingUpload.class);
			TableUtils.createTable(connectionSource, DailyLifeUpload.class);
			TableUtils.createTable(connectionSource, DailyNursingUpload.class);

			TableUtils.createTable(connectionSource, AllocatingTypeTask.class);
			TableUtils.createTable(connectionSource, DailyLifeMediaUpload.class);
			TableUtils.createTable(connectionSource, DailyNursingMediaUpload.class);

			TableUtils.createTable(connectionSource, BatchEditingTask.class);
			TableUtils.createTable(connectionSource, SignAndNursingTask.class);
			TableUtils.createTable(connectionSource, StaffCache.class);
		} catch (SQLException e) {
			TLog.log("创建出错啦" + e.toString());
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database,
			ConnectionSource connectionSource, int oldVersion, int newVersion) {
		TLog.log("更新数据库表");
		switch (oldVersion) {
			case 1:
                TLog.error("===========1===========");
			case 2:
                TLog.error("===========2===========");
			case 3:
                TLog.error("===========3===========");
			case 4:
                TLog.error("===========4===========");
			case 5:
                TLog.error("===========5===========");
			case 6:
                TLog.error("===========6===========");
			case 7:
                TLog.error("===========7===========");
			case 8:
                TLog.error("===========8===========");
			case 9:
                dropTable(connectionSource);
                onCreate(database, connectionSource);
			case 10:
                createTable(connectionSource, StaffCache.class);
            case 11:
                updateTable(DailyLifeMediaUpload.class, "ALTER TABLE `DailyLifeMediaUpload` ADD COLUMN alias TEXT;");
                updateTable(DailyNursingMediaUpload.class, "ALTER TABLE `DailyNursingMediaUpload` ADD COLUMN alias TEXT;");

		}
	}

    /**
     * 创建表
     */
	private <T> void createTable(ConnectionSource connectionSource, Class<T> dataClass) {
        try {
            TableUtils.createTable(connectionSource, dataClass);
        } catch (SQLException e) {
            TLog.error(e.toString());
        }
    }

    /**
     * 指定表添加指定的字段
     */
    private <T> void updateTable(Class<T> clazz, String sql) {
        try {
            getDao(clazz).executeRaw(sql);
        } catch (SQLException e) {
            TLog.error(e.toString());
        }
    }

	/**
	 * 删除所有表
	 */
	private void dropTable(ConnectionSource connectionSource) {
		try {
			TableUtils.dropTable(connectionSource, PulseDownload.class, true);
			TableUtils.dropTable(connectionSource, PressureDownload.class, true);
			TableUtils.dropTable(connectionSource, SugarDownload.class, true);
			TableUtils.dropTable(connectionSource, TemperatureDownload.class, true);
			TableUtils.dropTable(connectionSource, DrinkingDownload.class, true);
			TableUtils.dropTable(connectionSource, SleepingDownload.class, true);
			TableUtils.dropTable(connectionSource, EatDownload.class, true);
			TableUtils.dropTable(connectionSource, DefecationDownload.class, true);
			TableUtils.dropTable(connectionSource, BreathingDownload.class, true);
			TableUtils.dropTable(connectionSource, DailyLifeDownload.class, true);
			TableUtils.dropTable(connectionSource, DailyNursingDownload.class, true);

			TableUtils.dropTable(connectionSource, SignConfig.class, true);
			TableUtils.dropTable(connectionSource, SignIntervalColor.class, true);
			TableUtils.dropTable(connectionSource, SignIntervalPointColor.class, true);
			TableUtils.dropTable(connectionSource, DisplaySignOrNursingItem.class, true);
			TableUtils.dropTable(connectionSource, NursingItem.class, true);
			TableUtils.dropTable(connectionSource, Permission.class, true);
			TableUtils.dropTable(connectionSource, Dictionary.class, true);

			TableUtils.dropTable(connectionSource, Bed.class, true);
			TableUtils.dropTable(connectionSource, OldPeople.class, true);
			TableUtils.dropTable(connectionSource, OldPeopleFamily.class, true);

			TableUtils.dropTable(connectionSource, PulseUpload.class, true);
			TableUtils.dropTable(connectionSource, PressureUpload.class, true);
			TableUtils.dropTable(connectionSource, SugarUpload.class, true);
			TableUtils.dropTable(connectionSource, TemperatureUpload.class, true);
			TableUtils.dropTable(connectionSource, DrinkingUpload.class, true);
			TableUtils.dropTable(connectionSource, SleepingUpload.class, true);
			TableUtils.dropTable(connectionSource, EatUpload.class, true);
			TableUtils.dropTable(connectionSource, DefecationUpload.class, true);
			TableUtils.dropTable(connectionSource, BreathingUpload.class, true);
			TableUtils.dropTable(connectionSource, DailyLifeUpload.class, true);
			TableUtils.dropTable(connectionSource, DailyNursingUpload.class, true);

			TableUtils.dropTable(connectionSource, AllocatingTypeTask.class, true);
			TableUtils.dropTable(connectionSource, DailyLifeMediaUpload.class, true);
			TableUtils.dropTable(connectionSource, DailyNursingMediaUpload.class, true);

			TableUtils.dropTable(connectionSource, BatchEditingTask.class, true);
			TableUtils.dropTable(connectionSource, SignAndNursingTask.class, true);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

}
