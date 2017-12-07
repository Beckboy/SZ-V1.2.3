package com.junhsue.ksee.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sugar on 16/10/9 in Junhsue.
 */

public class StringUtils {
    /***
     * 是不是空字符串
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        // int strLen;
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        // for (int i = 0; i < strLen; i++) {
        // if ((Character.isWhitespace(str.trim().charAt(i)
        // ) == false)) {
        // return false;
        // }
        // }
        return false;
    }

    public static boolean isNotBlank(String s) {
        return !StringUtils.isBlank(s);
    }

    public static boolean isNullObject(Object obj) {
        return obj == null;
    }

    public static boolean isNotNullObject(Object obj) {
        return !isNullObject(obj);
    }

    /***
     * 是不是手机号码
     * @param mobileNo
     * @return
     */
    public static boolean isPhoneNumber(String mobileNo) {
        String regex = "^[1][0-9]{10}$";
        return Pattern.matches(regex, mobileNo);
    }

    /***
     * 是不是有效的6-20为密码
     *
     * @param pwd
     * @return
     */
    public static boolean isValidPwd(Context context, String pwd) {
        String regex = "^[a-zA-Z0-9`~!@#$%^&*()_]{6,20}$";
        boolean flag = Pattern.matches(regex, pwd);
        if (!flag){
            ToastUtil.getInstance(context).setContent("密码长度为6~20位").setShow();
            return false;
        }
        if (!hasNum(pwd)){
            ToastUtil.getInstance(context).setContent("密码必须包含数字和字母").setShow();
            return false;
        }
        if (!hasEng(pwd)){
            ToastUtil.getInstance(context).setContent("密码必须包含数字和字母").setShow();
            return false;
        }
        return true;
    }

    // 判断一个字符串是否含有数字
    public static boolean hasNum(String content) {
        boolean flag = false;
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(content);
        if (m.matches()) {
            flag = true;
        }
        return flag;
    }

    // 判断一个字符串是否含有英文字母
    public static boolean hasEng(String content) {
        boolean flag = false;
        Pattern p = Pattern.compile("[a-zA-z]");
        Matcher m = p.matcher(content);
        if (m.find()) {
            flag = true;
        }
        return flag;
    }

    /**
     * Check the given strings are not null or emtpy
     */
    public static boolean isEmpty(String... values) {
        for (String v : values) {
            if (TextUtils.isEmpty(v)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 是不是验证码格式
     * @param verifyCode
     * @return
     */
    public static boolean isVerifyCode(String verifyCode) {
        String regex = "[0-9]{6}";
        return Pattern.matches(regex, verifyCode);
    }

    public static void setTextFont(TextView view,String familyName,int typeface ){
        Typeface font = Typeface.create(familyName,typeface);
        view.setTypeface(font);
    }

    /**
     * 字体风格
     * @param view 控件
     * @param fontType 如:黑体,常规
     * @param typeface 如:斜体
     */
    public static void setTextFont(TextView view,Typeface fontType,int typeface){
        Typeface font = Typeface.create(fontType,typeface);
        view.setTypeface(font);
    }

    /**
     * 设置特定子字符串高亮显示
     *
     * @param textView
     * @param orgstr           原字符串
     * @param highLightStrings 更改的字符串和对应颜色
     */
    public static void highlight(TextView textView, String orgstr, HighLightString[] highLightStrings) {

        if (textView == null || StringUtils.isBlank(orgstr) || highLightStrings == null) {
            return;
        }
        SpannableStringBuilder spannable = new SpannableStringBuilder(orgstr);//用于可变字符串
        for (int i = 0; i < highLightStrings.length; i++) {
            int start = orgstr.indexOf(highLightStrings[i].str);
            if (start < 0) {
                continue;
            }
            int end = orgstr.indexOf(highLightStrings[i].str) + highLightStrings[i].str.length();
            ForegroundColorSpan span = new ForegroundColorSpan(highLightStrings[i].color);
            spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setText(spannable);
    }

    public static class HighLightString {
        public HighLightString(String str, int color) {
            this.color = color;
            this.str = str;
        }

        public String str;
        public int color;
    }

  /**
   * 获取性别
   */
    public static String getSexint2String(int index){
        switch (index){
            case 0:
                return "男";
            case 1:
                return "女";
            case 2:
                return "保密";
            default:
                break;
        }
        return null;
    }


    //毫秒转秒
    public static String long2String(long time){

        //毫秒转秒
        int sec = (int) time / 1000 ;
        int min = sec / 60 ;	//分钟
        sec = sec % 60 ;		//秒
        if(min < 10){	//分钟补0
            if(sec < 10){	//秒补0
                return "0"+min+":0"+sec;
            }else{
                return "0"+min+":"+sec;
            }
        }else{
            if(sec < 10){	//秒补0
                return min+":0"+sec;
            }else{
                return min+":"+sec;
            }
        }

    }

  /**
   * 若str中含有s，已s为隔断字符，截取一个a后面的所有内容
   * @param str
   * @param s
   * @return
   */
    public static String StrSplit(String str,String s){
        if (str == null) return str;
        if (s == null) return str;
        if (str.contains(s)){
            String[] ss = str.split(s);
            if (ss.length == 1) return str;
            int index = ss[0].length();
            return str.substring(index+1);
        }
        return str;
    }

    /**
     * 判断邮箱是否合法
     * @param email
     * @return
     */
    public static boolean isEmail(String email){
        if (null==email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }



    public static boolean isNewVersion(String local, String response){
        if (local.equals(response)) return true;

        String[] ver_local = local.split("\\.");
        String[] ver_res = response.split("\\.");
        int len = ver_local.length < ver_res.length ? ver_local.length :ver_res.length;
        for (int i = 0; i < len ; i++){
            int l = Integer.parseInt(ver_local[i]);
            int r = Integer.parseInt(ver_res[i]);
            if (l > r){
                return true;
            }else if (r > l){
                return false;
            }
        }
        if (ver_local.length >= ver_res.length){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 点赞数、评论数
     */
    public static String tranNum(String number){
        DecimalFormat format = new DecimalFormat("0.0");
        long num = Long.parseLong(number);
        if (num < 1000){
            return num+"";
        }else if (num < 1000 * 100){
            num  = num / 100;
            return format.format(num * 0.1) + "k";
        }else {
            num = num / 1000;
            return format.format(num * 0.1) + "w";
        }
    }

}
