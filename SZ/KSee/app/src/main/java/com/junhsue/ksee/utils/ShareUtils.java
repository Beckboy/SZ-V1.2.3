package com.junhsue.ksee.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.hyphenate.util.ImageUtils;
import com.junhsue.ksee.R;
import com.junhsue.ksee.frame.MyApplication;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.umeng.analytics.social.UMPlatformData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * Created by Sugar on 16/11/11.
 */

public class ShareUtils {

    public Context mContext;
    private static ShareUtils shareUtils = null;
    private static final int THUMB_SIZE = 160;

    private ShareUtils(Context context) {
        this.mContext = context;
    }

    public static ShareUtils getInstance(Context context) {
        if (shareUtils == null) {
            shareUtils = new ShareUtils(context);
        }
        return shareUtils;
    }

    /**
     * 微信分享
     */
    public void share(SendToPlatformType platform, String filePath, String title, String description) {

        Bitmap bmp = BitmapFactory.decodeFile(filePath);

        WXMediaMessage msg = new WXMediaMessage();
        msg.title = title;
        msg.description = description;
        //设置缩略图
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = BitmapUtil.bitmap2Bytes(thumbBmp, true);

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();

        req.message = msg;
        req.scene = (platform == SendToPlatformType.TO_FRIEND ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline);
        MyApplication.getIwxapi().sendReq(req);
    }


    /**
     * 微信分享网页
     */
    public void sharePage(SendToPlatformType platform, String weburl, String filePath, String title, String description, UMPlatformData.UMedia meida) {

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = weburl;

        WXMediaMessage msg = new WXMediaMessage(webpage);

        msg.title = title;

        if (description.length() > 400) {//微信的描述字数过多，可能无法分享，需要设置描述字数限制
            description = description.trim().substring(0, 400);
        }
        msg.description = description;

        Bitmap b = BitmapFactory.decodeFile(filePath);
        if (null == b) {
            b = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_launcher);
        }
        Bitmap bmp = BitmapUtil.resizeBitmap(b, 120, 120);
        msg.thumbData = BitmapUtil.bitmap2Bytes(bmp, true);

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = System.currentTimeMillis() + "";
        req.message = msg;
        req.scene = (platform == SendToPlatformType.TO_FRIEND ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline);
        MyApplication.getIwxapi().sendReq(req);

        StatisticsUtil.getInstance(mContext).onSocialEvent(mContext, meida);

    }

    public static final int IMAGE_SIZE = 32768;//微信分享图片大小限制

    /**
     * 分享图片
     *
     * @param platform
     * @param filePath
     */
    public void shareImage(SendToPlatformType platform, String filePath, UMPlatformData.UMedia meida) {
        //Bitmap bmp = BitmapFactory.decodeFile(filePath);
        Bitmap bmp = BitmapUtil.compressImageFromFile(filePath);
        if (null == bmp) {
            return;
        }

        WXImageObject imgObj = new WXImageObject(bmp);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        //设置缩略图
        Bitmap thumbBmp1 = Bitmap.createScaledBitmap(bmp, 120, 480, true);
        msg.thumbData = BitmapUtil.weChatBitmapToByteArray(thumbBmp1, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = msg;
        req.transaction = buildTransaction("img");
        req.scene = (platform == SendToPlatformType.TO_FRIEND ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline);
        MyApplication.getIwxapi().sendReq(req);

        StatisticsUtil.getInstance(mContext).onSocialEvent(mContext, meida);

    }


    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public enum SendToPlatformType {
        TO_FRIEND,
        TO_CIRCLE
    }
}
