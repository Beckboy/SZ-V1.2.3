/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyphenate.easeui;

public class EaseConstant {
    public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
    public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";
    
    public static final String MESSAGE_ATTR_IS_BIG_EXPRESSION = "em_is_big_expression";
    public static final String MESSAGE_ATTR_EXPRESSION_ID = "em_expression_id";
    
    public static final String MESSAGE_ATTR_AT_MSG = "em_at_list";
    public static final String MESSAGE_ATTR_VALUE_AT_MSG_ALL = "ALL";
    public static final String MESSAGE_ATTR_USER_NICKNAME = "hx_user_name";
    public static final String MESSAGE_ATTR_USER_AVATAR = "hx_user_avatar";
    public static final String MESSAGE_ATTR_USER_MSG = "hx_user_msg";

    public static final int CHATTYPE_SINGLE = 1;
    public static final int CHATTYPE_GROUP = 2;
    public static final int CHATTYPE_CHATROOM = 3;
    
    public static final String EXTRA_CHAT_TYPE = "chatType";
    public static final String EXTRA_GROUPCHAT_ID = "userId";
    public static final String EXTRA_GROUPCHAT_TITLE = "userName";
    public static final String EXTRA_USER_AVATAR = "ownAvatar";
    public static final String EXTRA_USER_NICKNAME = "ownNickName";
    public static final String EXTRA_USER_INTERFACE = "interface";

    public static final String GROUPCHAT_USER_ID = "groupchat_userId";
    public static final String GROUPCHAT_USER_LIST = "groupchat_userList";


    //群消息列表更新广播
    public final static  String BROAD_ACTION_GROUPCHAT_LIST_UPDATE="com.junhuse.ksee.acion_groupchat_list_update";
    //大图界面中图片本地下载的广播
    public final static  String BROAD_ACTION_BIG_IMG_LOADING="com.junhuse.ksee.acion_big_img_loading";
    //消息中长按头像@某的广播
    public final static  String BROAD_ACTION_AVATAR_LONGCLICK="com.junhuse.ksee.acion_avatar_longclick";

    //群消息列表更新广播类型
    public final static  String BROAD_GROUPCHAT_UPDATE_TYPE = "broad_TYPE";
    public final static  int BROAD_GROUPCHAT_UPDATE_TYPE_READ = 0;
    public final static  int BROAD_GROUPCHAT_UPDATE_TYPE_MSG = 1;

    //群消息列表更新广播文本内容
    public final static  String BROAD_GROUPCHAT_UPDATE_HXROOM_ID = "HX_roomid";
    public final static  String BROAD_GROUPCHAT_UPDATE_CONTENT = "broad_content";

    //大图界面下载图片
    public final static  String BROAD_BIG_IMG_LOADING = "broad_big_img_loading";

    //消息中长按头像@某的广播的userid
    public final static  String BROAD_ACTION_AVATAR_LONGCLICK_USERID = "broad_avatar_longclick_userid";


}
