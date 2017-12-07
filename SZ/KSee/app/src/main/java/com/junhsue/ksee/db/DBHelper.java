package com.junhsue.ksee.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.junhsue.ksee.file.FileUtil;

/**
 *
 * 数据库
 * Created by chenlang on 2016/11/28 in Junhsue.
 */

public class DBHelper extends SQLiteOpenHelper {


    private static  final String DATABASE_NAME= FileUtil.ROOTPATH+"/KSee.db";
    private static  final   int DATABASE_VERSION=1;



    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    /**
     *  版本更新*/
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
