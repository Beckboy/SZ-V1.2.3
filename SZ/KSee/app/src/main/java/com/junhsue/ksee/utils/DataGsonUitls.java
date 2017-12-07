package com.junhsue.ksee.utils;

import com.google.gson.Gson;

/**
 *
 * public data parsing class for the data
 * Created by longer on 17/6/5.
 */

public class DataGsonUitls {


    private static Gson gson = new Gson();

    public static <T> T format(String str, Class<T> clazz) {

        return gson.fromJson(str, clazz);

    }



    public static String  toJson(Object object) {


        return  gson.toJson(object);
    }

}
