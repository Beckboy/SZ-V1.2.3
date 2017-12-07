package com.hyphenate.easeui.widget.chatrow;

import java.io.File;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.model.EaseImageCache;
import com.hyphenate.easeui.ui.EaseShowBigImageActivity;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseImageUtils;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.ImageUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class EaseChatRowImage extends EaseChatRowFile{

    protected ImageView imageView;
    private EMImageMessageBody imgBody;

    public EaseChatRowImage(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ? R.layout.ease_row_received_picture : R.layout.ease_row_sent_picture, this);
    }

    /**
     * 当PopupWindow显示或者消失时改变背景色
     */
    private WindowManager.LayoutParams lp;

    @Override
    protected void onFindViewById() {
        percentageView = (TextView) findViewById(R.id.percentage);
        imageView = (ImageView) findViewById(R.id.image);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBubbleClick();
            }
        });
        imageView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
            final Activity act = (Activity) context;
            lp = act.getWindow().getAttributes();
            //设置contentView
            View contentView = LayoutInflater.from(context).inflate(R.layout.itemlongclick_popwindow,null);
            final PopupWindow mPopWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,true);
            mPopWindow.setContentView(contentView);
            EaseCommonUtils.showPop(act,mPopWindow,v,lp);

            TextView copy = (TextView) contentView.findViewById(R.id.tv_copy);
            View view = contentView.findViewById(R.id.v_copy);
            TextView del = (TextView) contentView.findViewById(R.id.tv_del);
            TextView revoke = (TextView) contentView.findViewById(R.id.tv_revoke);
            View vRevoke = contentView.findViewById(R.id.v_revoke);
            long st = System.currentTimeMillis();
            long mt = message.getMsgTime();
            final long time = st - mt;
            if (message.direct() == EMMessage.Direct.RECEIVE){
                revoke.setVisibility(GONE);
                vRevoke.setVisibility(GONE);
            }
            copy.setVisibility(GONE);
            view.setVisibility(GONE);
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View vv) {
                Log.i("img","message:"+message.getBody().toString()+":"+message.getMsgId());
                delCallback.delMsgid(message.getMsgId());
                mPopWindow.dismiss();
                }
            });
            revoke.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View vv) {
                mPopWindow.dismiss();
                if (time > 1000*60*2){
                    Toast.makeText(context,"超过2分钟不能撤回",Toast.LENGTH_SHORT).show();
                    return;
                }
                delCallback.revokeMsgid(message.getMsgId(),message.getStringAttribute(EaseConstant.MESSAGE_ATTR_USER_NICKNAME,"匿名"));
                }
            });
            return true;
            }
        });

    }

    @Override
    protected void onSetUpView() {
        imgBody = (EMImageMessageBody) message.getBody();
        // received messages
        if (message.direct() == EMMessage.Direct.RECEIVE) {
            if (imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
                imgBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING) {
                imageView.setImageResource(R.drawable.ease_default_image);
                setMessageReceiveCallback();
            } else {
                progressBar.setVisibility(View.GONE);
                percentageView.setVisibility(View.GONE);
                imageView.setImageResource(R.drawable.ease_default_image);
                String thumbPath = imgBody.thumbnailLocalPath();
                if (!new File(thumbPath).exists()) {
                	// to make it compatible with thumbnail received in previous version
                    thumbPath = EaseImageUtils.getThumbnailImagePath(imgBody.getLocalUrl());
                }
                showImageView(thumbPath, imageView, imgBody.getLocalUrl(), message);
            }
            return;
        }
        
        String filePath = imgBody.getLocalUrl();
        String thumbPath = EaseImageUtils.getThumbnailImagePath(imgBody.getLocalUrl());
        showImageView(thumbPath, imageView, filePath, message);
        handleSendMessage();
    }
    
    @Override
    protected void onUpdateView() {
        super.onUpdateView();
    }
    
    @Override
    protected void onBubbleClick() {
        Intent intent = new Intent(context, EaseShowBigImageActivity.class);
        File file = new File(imgBody.getLocalUrl());
        if (file.exists()) {
            Uri uri = Uri.fromFile(file);
            intent.putExtra("uri", uri);
        } else {
            // The local full size pic does not exist yet.
            // ShowBigImage needs to download it from the server
            // first
            String msgId = message.getMsgId();
            intent.putExtra("messageId", msgId);
            intent.putExtra("localUrl", imgBody.getLocalUrl());
        }
        if (message != null && message.direct() == EMMessage.Direct.RECEIVE && !message.isAcked()
                && message.getChatType() == ChatType.Chat) {
            try {
                EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        context.startActivity(intent);
    }
    
    /**
     * load image into image view
     * 
     * @param thumbernailPath
     * @param iv
     * @return the image exists or not
     */
    private boolean showImageView(final String thumbernailPath, final ImageView iv, final String localFullSizePath,final EMMessage message) {
        // first check if the thumbnail image already loaded into cache
        Bitmap bitmap = EaseImageCache.getInstance().get(thumbernailPath);
        Log.i("img","图片加载了。。。"+thumbernailPath);
        if (bitmap != null) {
            Log.i("img","图片加载了__1");
            // thumbnail image is already loaded, reuse the drawable
            if (message.direct() == EMMessage.Direct.RECEIVE) {
                ViewGroup.LayoutParams params = iv.getLayoutParams();
                params.width = bitmap.getWidth()*2;
                params.height = bitmap.getHeight()*2;
                iv.setLayoutParams(params);
                Log.i("img","图片是自己的");
            }
            iv.setImageBitmap(bitmap);
            return true;
        } else {
            Log.i("img","图片加载了__2");
            new AsyncTask<Object, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Object... args) {
                    File file = new File(thumbernailPath);
                    if (file.exists()) {
                        Log.i("img","图片下载了__a");
                        return EaseImageUtils.decodeScaleImage(thumbernailPath, 200, 200);
                    } else if (new File(imgBody.thumbnailLocalPath()).exists()) {
                        Log.i("img","图片下载了__b");
                        return EaseImageUtils.decodeScaleImage(imgBody.thumbnailLocalPath(), 200, 200);
                    }
                    else {
                        if (message.direct() == EMMessage.Direct.SEND) {
                            if (localFullSizePath != null && new File(localFullSizePath).exists()) {
                                Log.i("img","图片下载了__c");
                                return EaseImageUtils.decodeScaleImage(localFullSizePath, 200, 200);
                            } else {
                                return null;
                            }
                        } else {
                            return null;
                        }
                    }
                }

                protected void onPostExecute(Bitmap image) {
                    if (image != null) {
                        iv.setImageBitmap(image);
//                        ViewGroup.LayoutParams params=iv.getLayoutParams();
//                        params.width= iv.getWidth()*2;
//                        params.height = iv.getHeight()*2;
//                        iv.setLayoutParams(params);
                        Log.i("img","图片宽高："+iv.getWidth()+":"+iv.getHeight());
                        EaseImageCache.getInstance().put(thumbernailPath, image);
                    } else {
                        if (message.status() == EMMessage.Status.FAIL) {
                            if (EaseCommonUtils.isNetWorkConnected(activity)) {
                                new Thread(new Runnable() {

                                    @Override
                                    public void run() {
                                        EMClient.getInstance().chatManager().downloadThumbnail(message);
                                    }
                                }).start();
                            }
                        }

                    }
                }
            }.execute();

            return true;
        }
    }

}
