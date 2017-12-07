package com.junhsue.ksee.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.frame.MyApplication;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by Sugar on 17/4/27.
 */

public class CommonUtils {

    private static int appVersionCode = 0;//app当前版本号对应的码
    protected static String uuid;
    private static String appVersionName = "";//app当前版本号
    private static String deviceUUID = ""; // 用静态变量保证该变量保存的时间长点
    private static String deviceInfo = "";//设备信息
    private static String deviceMode = "";//手机型号
    private static String deviceBrand = "";//手机品牌
    private static String deviceSystemVersion = "";//手机系统版本号
    private static String deviceMac = "";
    private static String appPackageName = "";
    private static CommonUtils utils = null;

    private static long lastClickTime;

    private CommonUtils() {

    }

    public static CommonUtils getInstance() {
        if (utils == null) {
            utils = new CommonUtils();
        }
        return utils;
    }

    /**
     * 控件是否可以重复点击
     */
    public static boolean isRepeatClick() {
        if (System.currentTimeMillis() - lastClickTime <= 1000) {
            Trace.e("你点击太快了");
            return false;
        }
        lastClickTime = System.currentTimeMillis();
        return true;
    }


    /**
     * 网络状态的监听
     *
     * @param mContext
     * @return
     */
    public static boolean getIntnetConnect(Context mContext) {
        System.out.println("网络状态发生变化");
        //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {

            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

            //获取ConnectivityManager对象对应的NetworkInfo对象
            //获取WIFI连接的信息
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //获取移动数据连接的信息
            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
                return true;
            } else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                return true;
            } else {
                return false;
            }
            //API大于23时使用下面的方式进行网络监听
        } else {
            System.out.println("API level 大于23");
            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取所有网络连接的信息
            Network[] networks = connMgr.getAllNetworks();
            //用于存放网络连接信息
            StringBuilder sb = new StringBuilder();
            //通过循环将网络信息逐个取出来
            for (int i = 0; i < networks.length; i++) {
                //获取ConnectivityManager对象对应的NetworkInfo对象
                NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
                if (networkInfo.isConnected()) return true;
            }
            return false;
        }
    }


    /**
     * 获取VersionCode
     */
    public int getAppVersionCode() {
        if (appVersionCode == 0) {
            try {
                appVersionCode = MyApplication.getApplication().getPackageManager().getPackageInfo(
                        MyApplication.getApplication().getPackageName(), 0).versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                appVersionCode = 1;
                e.printStackTrace();
            }
        }
        return appVersionCode;
    }

    /**
     * 获取VersionName
     */
    public String getAppVersionName() {
        if (StringUtils.isBlank(appVersionName)) {
            try {
                appVersionName = MyApplication.getApplication().getPackageManager().getPackageInfo(
                        MyApplication.getApplication().getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                appVersionName = "";
                e.printStackTrace();
            }
        }
        if (StringUtils.isBlank(appVersionName)) {
            appVersionName = "unknown version name " + getDeviceInfo();
        }
        return appVersionName;
    }

    /**
     * 先获取手机型号,后获取手机品牌,如果获取不出来则显示unknown
     */
    public String getDeviceInfo() {
        deviceInfo = getDeviceModel();
        if (StringUtils.isBlank(deviceInfo)) {
            deviceInfo = getDeviceBrand();
        }
        if (StringUtils.isBlank(deviceInfo)) {
            deviceInfo = "unknown android phone";
        }
        return deviceInfo;
    }

    /**
     * 获取手机型号
     */
    private String getDeviceModel() {
        if (StringUtils.isBlank(deviceMode)) {
            deviceMode = Build.MODEL; //
        }
        return deviceMode;
    }

    /**
     * 获取手机品牌
     */
    private String getDeviceBrand() {

        if (StringUtils.isBlank(deviceBrand)) {
            deviceBrand = Build.BRAND;
        }
        return deviceBrand;
    }


    /**
     * 获取手机的Android版本号
     */
    public String getDeviceVersion() {
        if (StringUtils.isBlank(deviceSystemVersion)) {
            deviceSystemVersion = Build.VERSION.RELEASE;
        }
        //如果还为空的话,取SDK_INT
        if (StringUtils.isBlank(deviceSystemVersion)) {
            deviceSystemVersion = String.valueOf(Build.VERSION.SDK_INT);
        }
        //如果还为空的话,直接赋值android"
        if (StringUtils.isBlank(deviceSystemVersion)) {
            deviceSystemVersion = "unknown sdk " + getDeviceInfo();
        }
        return "android-" + deviceSystemVersion;
    }


    /**
     * 获取UUID
     */
    public String getDeviceUUID() {
        if (StringUtils.isBlank(deviceUUID)) {
            deviceUUID = getDeviceUuid().toString();
        }
        //如果仍然还是一个空值，赋值随机数
        if (StringUtils.isBlank(deviceUUID)) {
            deviceUUID = null;
        }
        Trace.d("Socket connect deviceUUID = " + deviceUUID);
        return deviceUUID;
    }


    public void setDeviceUUID(String uuid) {
        this.deviceUUID = uuid;
    }

    /**
     * Returns a unique UUID for the current android device. As with all UUIDs,
     * this unique ID is "very highly likely" to be unique across all Android
     * devices. Much more so than ANDROID_ID is.
     * <p/>
     * The UUID is generated by using ANDROID_ID as the base key if appropriate,
     * falling back on TelephonyManager.getDeviceID() if ANDROID_ID is known to
     * be incorrect, and finally falling back on a random UUID that's persisted
     * to SharedPreferences if getDeviceID() does not return a usable value.
     */
    public synchronized String getDeviceUuid() {

        if (uuid == null) {
            //本地保存的UUID
            String id = SharedPreferencesUtils.getInstance(MyApplication.getApplication()
                    , SharedPreferencesUtils.UUID).getString(SharedPreferencesUtils.KEY_UUID, null);
            if (StringUtils.isNotBlank(id)) {
                // Use the ids previously computed and stored in the
                // prefs file
                uuid = UUID.fromString(id).toString();
            } else {
                //获取UUID
                uuid = getUniqueUUID();
                // Write the value out to the prefs file
                SharedPreferencesUtils.getInstance(MyApplication.getApplication()
                        , SharedPreferencesUtils.UUID).putString(SharedPreferencesUtils.KEY_UUID, uuid.toString());
            }
        }

        return uuid;
    }

    /**
     * 如果出现getDeviceUuid出现异常，随机生成一个唯一的标示符,生成规则按照
     * mac地址－>SerialNo－>SubscriberId－>SIM卡的序列号－>手机号－>null
     */
    private String getUniqueUUID() {
        TelephonyManager tm = ((TelephonyManager) MyApplication.getApplication()
                .getSystemService(Context.TELEPHONY_SERVICE));
        String uuidString = null;
        UUID uuid = null;
        //android_id
        uuidString = Settings.Secure.getString(MyApplication.getApplication().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        if (StringUtils.isNotBlank(uuidString) && (!"9774d56d682e549c".equals(uuidString))) {
            uuid = getUUIDFromString(uuidString);
            Trace.d("android = " + uuid);
            if (uuid != null) {
                return uuid.toString();
            }
        } else {
            //deviceId
            if (tm == null) {
                uuidString = getOtherUUID();
                return uuidString;
            }
            uuidString = tm.getDeviceId();
            uuid = getUUIDFromString(uuidString);
            Trace.d("device id = " + uuid);
            if (uuid != null) {
                return uuid.toString();
            } else {
                uuidString = getOtherUUID();
                return uuidString;
            }
        }

        return "";
    }


    private String getOtherUUID() {
        String uuidString = null;
        UUID uuid = null;
        //判断wifi的mac地址
        uuidString = getMacAddress();
        if (StringUtils.isNotBlank(uuidString)) {
            uuid = getUUIDFromString(uuidString);
            Trace.d("wifi = " + uuid);
            if (uuid != null) {
                uuidString = "appw-" + uuid.toString();
                return uuidString;
            }
        }

        //序列号
        uuidString = getSerialNo();
        if (StringUtils.isNotBlank(uuidString)) {
            uuid = getUUIDFromString(uuidString);
            Trace.d("SerialNo = " + uuid);
            if (uuid != null) {
                uuidString = "appser-" + uuid.toString();
                return uuidString;
            }
        }

        TelephonyManager tm = ((TelephonyManager) MyApplication.getApplication()
                .getSystemService(Context.TELEPHONY_SERVICE));
        if (tm == null) {
            return null;
        }

        //获取手机唯一的用户ID 例如：IMSI(国际移动用户识别码) for a GSM phone.
        uuidString = tm.getSubscriberId();
        if (StringUtils.isNotBlank(uuidString)) {
            uuid = getUUIDFromString(uuidString);
            Trace.d("Subscriber = " + uuid);
            if (uuid != null) {
                uuidString = "appsub-" + uuid.toString();
                return uuidString;
            }
        }
        //获取手机的SIM卡的序列号
        uuidString = tm.getSimSerialNumber();
        if (StringUtils.isNotBlank(uuidString)) {
            uuid = getUUIDFromString(uuidString);
            Trace.d("SimSerial   = " + uuid);
            if (uuid != null) {
                uuidString = "appsim-" + uuid.toString();
                return uuidString;
            }
        }
        //手机号：
        uuidString = tm.getLine1Number();
        if (StringUtils.isNotBlank(uuidString)) {
            uuid = getUUIDFromString(uuidString);
            Trace.d("line  = " + uuid);
            if (uuid != null) {
                uuidString = "appline-" + uuid.toString();
                return uuidString;
            }
        }
        return null;
    }


    /**
     * .获取手机MAC地址
     * 只有手机开启wifi才能获取到mac地址
     */
    public String getMacAddress() {
        if (StringUtils.isBlank(deviceMac)) {
            WifiManager wifiManager = (WifiManager) MyApplication.getApplication().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            deviceMac = wifiInfo.getMacAddress();
        }

        return deviceMac;
    }

    //获取手机序列号
    private String getSerialNo() {
        String serialno = Build.SERIAL;
        //防止得出来的值和imei一样
        if (StringUtils.isNotBlank(serialno) && (!"9774d56d682e549c".equals(serialno))) {
            return serialno;
        }
        //若没有build属性,则通过反射去读取字段
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            serialno = (String) (get.invoke(c, "ro.serialno", ""));
        } catch (Exception e) {
            e.printStackTrace();
            serialno = null;
        }
        if ("9774d56d682e549c".equals(serialno)) {
            return null;
        }
        return serialno;
    }


    //根据String来生成UUID
    private UUID getUUIDFromString(String uuid) {
        UUID result;
        //华为手机在屏蔽权限之后，返回的是15个0，要排除这种情况
        if (StringUtils.isBlank(uuid) || uuid.contains("000000000000")) {
            return null;
        }
        try {
            result = UUID.nameUUIDFromBytes(uuid.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            //出现异常则直接赋值随机
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    /**
     * 获取包名
     */
    public String getAppPackageName() {
        if (StringUtils.isBlank(appPackageName)) {
            appPackageName = MyApplication.getApplication().getApplicationContext().getPackageName();
        }
        //如果取不到包名，直接赋值,注意不同的应用，该包名要区别一下
        if (StringUtils.isBlank(appPackageName)) {
            appPackageName = "com.junhsue.ksee";
        }
        return appPackageName;
    }


}
