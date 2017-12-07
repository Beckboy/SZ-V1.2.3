package com.junhsue.ksee.entity;

import java.util.ArrayList;

/**
 * Created by Sugar on 16/10/18.
 */

public class WeChatUserInfo extends BaseEntity {
/*    {
        "openid":"OPENID",
            "nickname":"NICKNAME",
            "sex":1,
            "province":"PROVINCE",
            "city":"CITY",
            "country":"COUNTRY",
            "headimgurl": "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0",
            "privilege":[
        "PRIVILEGE1",
                "PRIVILEGE2"
        ],
        "unionid": " o6_bmasdasdsad6_2sgVt7hMZOPfL"

    }*/

    public String openid;
    public String nickname;
    public String sex;
    public String province;
    public String city;
    public String country;
    public String headimgurl;
    public ArrayList<String> privilege = new ArrayList<>();
    public String unionid;

    public static WeChatUserInfo cloneWeChatUserInfoObject(String nickname, String headimgurl) {
        WeChatUserInfo weChatUserInfo = new WeChatUserInfo();
        weChatUserInfo.nickname = nickname;
        weChatUserInfo.headimgurl = headimgurl;
        return weChatUserInfo;
    }

}
