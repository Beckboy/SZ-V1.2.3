package com.junhsue.ksee.db;

import android.content.Context;


/**
 * 数据库业务层
 * Created by chenlang on 2016/11/29.
 */


public class DBManager {


    private static DBHelper dbHelper;

    private static final String TAG = "db_ksee";


    public static DBManager newInstance(Context context) {
        DBManager dbManager = new DBManager();
        initDatabase(context);
        return dbManager;

    }


    private static void initDatabase(Context context) {
        if (null == dbHelper) {
            synchronized (DBManager.class) {
                dbHelper = new DBHelper(context);
                dbHelper.getWritableDatabase();
            }
        }
    }

    /**
     **/
    public void closeDBHelper() {
        if (null != dbHelper) {
            dbHelper.close();
            dbHelper = null;
        }
    }




}
