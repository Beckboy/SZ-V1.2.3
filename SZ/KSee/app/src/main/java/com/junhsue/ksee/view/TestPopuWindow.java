package com.junhsue.ksee.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.junhsue.ksee.R;

/**
 * Created by Sugar on 17/5/27.
 */

public class TestPopuWindow extends PopupWindow implements View.OnClickListener {

    private Activity mContext;
    private int mWidth;
    private int mHeight;
    private int statusBarHeight;
    private Bitmap mBitmap = null;
    private Bitmap overlay = null;

    private TestPopuWindow.ShareCallBack callBack;

    private Handler mHandler = new Handler();

    public TestPopuWindow(Activity context) {
        mContext = context;
        init();
    }

    public void init() {
        Rect frame = new Rect();
        mContext.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        statusBarHeight = frame.top;
        DisplayMetrics metrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        mWidth = metrics.widthPixels;
        mHeight = metrics.heightPixels;

        setWidth(mWidth);
        setHeight(mHeight);
    }

    private Animation showAnimation1(final View view, int fromY, int toY) {
        AnimationSet set = new AnimationSet(true);
        TranslateAnimation go = new TranslateAnimation(0, 0, fromY, toY);
        go.setDuration(300);
        TranslateAnimation go1 = new TranslateAnimation(0, 0, -10, 2);
        go1.setDuration(100);
        go1.setStartOffset(250);
        set.addAnimation(go1);
        set.addAnimation(go);

        set.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationStart(Animation animation) {

            }

        });
        return set;
    }


    public void showMoreWindow(View anchor) {
        final RelativeLayout layout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.component_share_dailog, null);
        setContentView(layout);
        layout.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isShowing()) {
                    closeAnimation(layout);
                }
            }

        });

        showAnimation(layout);
//		setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), blur()));
        setOutsideTouchable(true);
        setFocusable(true);
        showAtLocation(anchor, Gravity.BOTTOM, 0, statusBarHeight);
    }

    private void showAnimation(final ViewGroup layout) {

        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {

                ValueAnimator fadeAnim = ObjectAnimator.ofFloat(layout, "translationY", 600, 0);
//                    TranslateAnimation go = new TranslateAnimation(0, 0, fromY, toY);
//                    go.setDuration(300);
                fadeAnim.setDuration(400);
                fadeAnim.start();

            }
        }, 100);

    }

    private void closeAnimation(final ViewGroup layout) {

        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                ValueAnimator fadeAnim = ObjectAnimator.ofFloat(layout, "translationY", 0, 600);
                fadeAnim.setDuration(400);
                fadeAnim.start();
                fadeAnim.addListener(new Animator.AnimatorListener() {

                    @Override
                    public void onAnimationStart(Animator animation) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dismiss();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        // TODO Auto-generated method stub

                    }
                });

            }
        }, 100);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_test://朋友
                if (callBack != null) {
                    Log.e("===", "====朋友");
                    callBack.shareFriend();
                }
                dismiss();
                break;
            case R.id.ll_share_circle://朋友圈
                if (callBack != null) {
                    callBack.shareCircle();
                }
                dismiss();
                break;
//            case R.id.tv_cancel:
//                dismiss();
//                break;
        }
    }


    public void destroy() {
        if (null != overlay) {
            overlay.recycle();
            overlay = null;
            System.gc();
        }
        if (null != mBitmap) {
            mBitmap.recycle();
            mBitmap = null;
            System.gc();
        }
    }


    public void setCallBack(ShareCallBack shareCallBack) {
        this.callBack = shareCallBack;
    }

    public interface ShareCallBack {

        void shareFriend();

        void shareCircle();

    }

}
