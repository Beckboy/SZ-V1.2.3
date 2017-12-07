package com.junhsue.ksee.common;

import android.util.Log;

/**
 *   日志工具类
 */
public class Trace {
    private static final String LOG_KEY = "FSee";

    public static final boolean DEBUG_MODE = true;
    public static Trace.CustomLogger customLogger;

    public static void v(String msg) {
        if (DEBUG_MODE)
            Log.v(LOG_KEY, msg);
    }

    public static void d(String msg) {
        if (DEBUG_MODE)
            Log.d(LOG_KEY, msg);
    }

    public static void i(String msg) {
        if (!DEBUG_MODE){
            return;
        }
        if(customLogger != null) {
            customLogger.i(LOG_KEY, msg);
        } else {
            Log.i(LOG_KEY, msg);
        }
    }



    public static void w(String msg) {
        if (!DEBUG_MODE){
            return;
        }
        if(customLogger != null) {
            customLogger.w(LOG_KEY, msg);
        } else {
            Log.w(LOG_KEY, msg);
        }
    }

    public static void w(String msg,Throwable tr) {
        if (!DEBUG_MODE){
            return;
        }
        if(customLogger != null) {
            customLogger.w(LOG_KEY, msg,tr);
        } else {
            Log.w(LOG_KEY, msg,tr);
        }
    }



    public static void e(String msg) {
        if (DEBUG_MODE) {
            if(customLogger != null) {
                customLogger.e(LOG_KEY, msg);
            } else {
                Log.e(LOG_KEY, msg);
            }
        }

    }

    public static void e(String msg,Throwable tr) {
        if (DEBUG_MODE) {
            if(customLogger != null) {
                customLogger.e(LOG_KEY, msg,tr);
            } else {
                Log.e(LOG_KEY, msg,tr);
            }
        }

    }



    public interface CustomLogger {
        void d(String var1, String var2);

        void d(String var1, String var2, Throwable var3);

        void e(String var1, String var2);

        void e(String var1, String var2, Throwable var3);

        void i(String var1, String var2);

        void i(String var1, String var2, Throwable var3);

        void v(String var1, String var2);

        void v(String var1, String var2, Throwable var3);

        void w(String var1, String var2);

        void w(String var1, String var2, Throwable var3);

        void w(String var1, Throwable var2);

        void wtf(String var1, String var2);

        void wtf(String var1, String var2, Throwable var3);

        void wtf(String var1, Throwable var2);
    }
}
