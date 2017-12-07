package com.junhsue.ksee.utils;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.util.Log;

import com.junhsue.ksee.R;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.jeesoft.widget.pickerview.CharacterPickerView;
import cn.jeesoft.widget.pickerview.CharacterPickerWindow;
import cn.jeesoft.widget.pickerview.OnOptionChangedListener;

/**
 * 地址选择器
 *
 * @version 0.1 king 2015-10
 */
public class OptionsWindowHelperProvince {

    private static List<String> options1Items = null;
    private static List<List<String>> options2Items = null;
    private static List<List<List<String>>> options3Items = null;

    public interface OnOptionsSelectListener {
        void onOptionsSelect(String province, String city, String area);
    }

    private OptionsWindowHelperProvince() {
    }

    public static CharacterPickerWindow builder(Activity activity, final OnOptionsSelectListener listener) {
        //选项选择器
        CharacterPickerWindow mOptions = new CharacterPickerWindow(activity);
        //初始化选项数据
        setPickerData(activity,mOptions.getPickerView());

        //设置默认选中的三级项目
        mOptions.setSelectOptions(0, 0, 0);
        //监听确定选择按钮
        mOptions.setOnoptionsSelectListener(new OnOptionChangedListener() {
            @Override
            public void onOptionChanged(int option1, int option2, int option3) {
                if (listener != null) {
                    String province = options1Items.get(option1);
                    option2 = option2 < options2Items.get(option1).size() ? option2 : (options2Items.get(option1).size()-1);
                    String city = options2Items.get(option1).get(option2);
                    option3 = option3 < options3Items.get(option1).get(option2).size() ? option3 : (options3Items.get(option1).get(option2).size()-1);
                    String area = options3Items.get(option1).get(option2).get(option3);
                    listener.onOptionsSelect(province, city, area);
                }
            }
        });
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

            final HashMap<String, HashMap<String, List<String>>> allCitys = parseXml(activity);

            Iterator<Map.Entry<String, HashMap<String, List<String>>>> iter = allCitys.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, HashMap<String, List<String>>> entry = iter.next();
                options1Items.add(entry.getKey());
            }


            for (Map.Entry<String, HashMap<String, List<String>>> entry1 : allCitys.entrySet()) {

                Map<String, List<String>> value1 = entry1.getValue();

                List<String> options2Items01 = new ArrayList<>();

                List<List<String>> options3Items01 = new ArrayList<>();

                for (Map.Entry<String, List<String>> entry2 : value1.entrySet()) {
                    String key2 = entry2.getKey();
                    List<String> value2 = entry2.getValue();
                    options2Items01.add(key2);
                    options3Items01.add(new ArrayList<>(value2));
                }
                options2Items.add(options2Items01);
                options3Items.add(options3Items01);
            }
        }


        //三级联动效果
        view.setPicker(options1Items, options2Items, options3Items);

    }

    private static HashMap<String, HashMap<String, List<String>>> parseXml(Activity activity) {

        HashMap<String, HashMap<String, List<String>>> province = new HashMap<>();


        XmlResourceParser xml = activity.getResources().getXml(R.xml.province_data);

        try {
            int eventType = xml.getEventType();

            String preProvince = null;
            String preCity = null;
            String preDistrict = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                Log.e("TAG", "进入循环了");
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String tag = xml.getName();
                        if ("province".equals(tag)) {
                            preProvince = xml.getAttributeValue(null, "name");
                            province.put(preProvince, new HashMap<String, List<String>>());
                        } else if ("city".equals(tag)) {
                            preCity = xml.getAttributeValue(null, "name");
                            province.get(preProvince).put(preCity, new ArrayList<String>());
                        } else if ("district".equals(tag)) {
                            preDistrict = xml.getAttributeValue(null, "name");
                            province.get(preProvince).get(preCity).add(preDistrict);
                        }
                        break;

                }
                //如果xml没有结束，则导航到下一个river节点
                eventType = xml.next();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return province;

    }


}
