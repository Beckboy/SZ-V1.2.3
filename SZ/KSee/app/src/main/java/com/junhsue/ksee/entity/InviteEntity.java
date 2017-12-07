package com.junhsue.ksee.entity;

import android.util.Log;

/**
 * Created by Sugar on 17/9/22.
 */

public class InviteEntity extends BaseEntity {

    public String id = "";
    public String source_user_id = "";
    public String dest_user_id = "";
    public int status;
    public String unionid = "";
    public String nickname = "";
    public String avatar = "";

    /**1
     * 业务id
     */
    public int business_id;

    public int content_id;


    public void msgString() {
        Log.e("==", "==id:" + id + "==source_user_id:" + source_user_id + "==dest_user_id:" + dest_user_id + "==status:" + status + "==unionid:" + unionid + "==nickname:" + nickname + "==avatar:" + avatar);
    }

}
