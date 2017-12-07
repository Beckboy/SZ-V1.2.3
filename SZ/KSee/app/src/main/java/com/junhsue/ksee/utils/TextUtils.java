package com.junhsue.ksee.utils;

import com.junhsue.ksee.frame.MyApplication;

/**
 * Created by longer on 17/3/20.
 */

public class TextUtils {


    public static  String  trans(int resoureId){

        return MyApplication.getApplication().getString(resoureId);
    }
}
