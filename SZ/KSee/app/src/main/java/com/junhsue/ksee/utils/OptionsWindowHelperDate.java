package com.junhsue.ksee.utils;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.junhsue.ksee.common.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.jeesoft.widget.pickerview.CharacterPickerView;
import cn.jeesoft.widget.pickerview.CharacterPickerWindow;
import cn.jeesoft.widget.pickerview.OnOptionChangedListener;

/**
 * 地址选择器
 *
 * @version 0.1 king 2015-10
 */
public class OptionsWindowHelperDate {

    private static List<String> options1Items = null;
    private static List<List<String>> options2Items = null;
    private static List<List<List<String>>> options3Items = null;

    public interface OnOptionsSelectListener {
        void onOptionsSelect(String province, String city, String area);
    }

    private OptionsWindowHelperDate() {
    }

    public static CharacterPickerWindow builder(final Activity activity, final OnOptionsSelectListener listener) {
        //选项选择器
        CharacterPickerWindow mOptions = new CharacterPickerWindow(activity);
        //初始化选项数据
        setPickerData(activity, mOptions.getPickerView());

        SimpleDateFormat formatterY = new SimpleDateFormat("yyyy");
        SimpleDateFormat formatterM = new SimpleDateFormat("MM");
        SimpleDateFormat formatterD = new SimpleDateFormat("dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String strY = formatterY.format(curDate);
        String strM = formatterM.format(curDate);
        String strD = formatterD.format(curDate);
        int endYear = Integer.parseInt(strY);
        int endMouth = Integer.parseInt(strM);
        int endDay = Integer.parseInt(strD);

        Log.i("date",endYear+":"+endMouth+":"+endDay);

        //监听确定选择按钮
        mOptions.setOnoptionsSelectListener(new OnOptionChangedListener() {
            @Override
            public void onOptionChanged(int option1, int option2, int option3) {
                if (!CommonUtils.getIntnetConnect(activity)){
                    ToastUtil.getInstance(activity).setContent(Constants.INTNET_NOT_CONNCATION).setDuration(Toast.LENGTH_SHORT).setShow();
                    return;
                }
                if (listener != null) {
                    String province = options1Items.get(option1).substring(0,options1Items.get(option1).length()-1);
                    String city = options2Items.get(option1).get(option2).substring(0,options2Items.get(option1).get(option2).length()-1);
                    String area = options3Items.get(option1).get(option2).get(option3).substring(0,options3Items.get(option1).get(option2).get(option3).length()-1);
                    listener.onOptionsSelect(province, city, area);
                }
            }
        });
        //设置默认选中的三级项目
        mOptions.setSelectOptions(120,endMouth-1, endDay-1);
        return mOptions;
    }

    /**
     * 初始化选项数据
     */
    public static void setPickerData(Activity activity, CharacterPickerView view) {

        if (options1Items == null) {
            options1Items = new ArrayList<>();
            options2Items = new ArrayList<>();
            options3Items = new ArrayList<>();

            final TreeMap<String, TreeMap<String, List<String>>> allCitys = parseXml(activity);

            Iterator<Map.Entry<String, TreeMap<String, List<String>>>> iter = allCitys.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, TreeMap<String, List<String>>> entry = iter.next();
                options1Items.add(entry.getKey());
                Log.i("options1Items",entry.getKey());
            }


            for (Map.Entry<String, TreeMap<String, List<String>>> entry1 : allCitys.entrySet()) {

                Map<String, List<String>> value1 = entry1.getValue();

                List<String> options2Items01 = new ArrayList<>();

                List<List<String>> options3Items01 = new ArrayList<>();

                SimpleDateFormat formatterY = new SimpleDateFormat("yyyy年");
                SimpleDateFormat formatterM = new SimpleDateFormat("MM月");
                SimpleDateFormat formatterD = new SimpleDateFormat("dd日");
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                String strY = formatterY.format(curDate);
                String strM = formatterM.format(curDate);
                String strD = formatterD.format(curDate);

                for (Map.Entry<String, List<String>> entry2 : value1.entrySet()) {
                    String key2 = entry2.getKey();
                    List<String> value2 = entry2.getValue();

                    options2Items01.add(key2);
                    Log.i("options2Items01_key2",entry2.getKey());

                    ArrayList<String> list = new ArrayList(value2);
                    if (key2.equals(strM)){
                        for (int i = list.size()-1; i >= 0;i--){
                            if (!list.get(i).equals(strD)){
                                list.remove(i);
                            }else {
                                break;
                            }
                        }
                    }

                    options3Items01.add(list);
                    Log.i("options3Items01_value2",new ArrayList<>(value2).toString());

                    if ( entry1.getKey().equals(strY) && key2.equals(strM)) break;
                }
                options2Items.add(options2Items01);
                options3Items.add(options3Items01);
            }
        }


        //三级联动效果
        view.setPicker(options1Items, options2Items, options3Items);

    }

    private static TreeMap<String, TreeMap<String, List<String>>> parseXml(Activity activity) {

        TreeMap<String, TreeMap<String, List<String>>> years = new TreeMap<>();  //年

        SimpleDateFormat formatter    =   new SimpleDateFormat("yyyy");
        Date curDate    =   new Date(System.currentTimeMillis());//获取当前时间
        String str    =    formatter.format(curDate);
        int endYear = Integer.parseInt(str);
        Log.i("date",str);

        for (int y = endYear - 120; y <= endYear;y++) {

            TreeMap<String,List<String>> months = new TreeMap<>();  //月

            for (int m = 1; m < 13; m++) {

                ArrayList<String> days = new ArrayList<String>();  //日

                if (m == 1 || m == 3 || m == 5 || m == 7 || m == 8 || m == 10 || m == 12) {
                    for (int d = 1; d < 32; d++) {
                        formatDay(days,d);
                    }
                } else if (m == 4 || m == 6 || m == 9 || m == 11) {
                    for (int d = 1; d < 31; d++) {
                        formatDay(days,d);
                    }
                } else if (m == 2) {
                    if ((y % 4 == 0 && y % 100 != 0) || y % 400 == 0) {
                        for (int d = 1; d < 30; d++) {
                            formatDay(days,d);
                        }
                    } else {
                        for (int d = 1; d < 29; d++) {
                            formatDay(days,d);
                        }
                    }
                }
                formatMouth(months, m, days);
            }
            formatYear(years,y,months);
        }
        return years;

    }

    private static void formatYear(TreeMap<String, TreeMap<String, List<String>>> years, int y, TreeMap<String, List<String>> months) {
        String strY = String.valueOf(y)+"年";
        years.put(strY,months);
    }


    private static void formatMouth(TreeMap<String, List<String>> months, int m, ArrayList<String> days) {
        String strM;
        if(m<=9){
            strM = "0"+m+"月";
        }else {
            strM = String.valueOf(m)+"月";
        }
        months.put(strM,days);
    }

    /**
   * 获取日的数据源
   * @param days
   * @param d
   */
    private static void formatDay(List<String> days, int d) {
        String strD;
        if(d<=9){
            strD = "0"+d+"日";
        }else {
            strD = String.valueOf(d)+"日";
        }
        days.add(strD);
    }

}
