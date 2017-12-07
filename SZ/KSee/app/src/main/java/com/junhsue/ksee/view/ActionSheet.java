package com.junhsue.ksee.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.junhsue.ksee.R;


/**
 * <p/>
 * 只要将弹出框的View设置成width = MATCH_PARENT height ＝ WRAP_CONTENT即可，
 * 若要是弹出框的View有高度，则在传入UI之前就将高度设置好即可。
 */
public class ActionSheet extends Dialog {
    private Context context;
    private boolean mCloseOnTouchOutside = true;
    private boolean mCancelable = true;
    private int mGravity = Gravity.BOTTOM;

    public ActionSheet(Context context) {
        super(context);
        this.context = context;
        initWindowLayoutParam();
    }

    public ActionSheet(Context context, int theme) {
        super(context, theme);
        this.context = context;
        initWindowLayoutParam();
    }



    private void initWindowLayoutParam() {
        Window window = this.getWindow();
        // 设置显示动画,根据展示位置设置动画样式
        window.setWindowAnimations(R.style.ActionSheetStyle);
        //设置没有标题
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams windowParam = getWindow().getAttributes();
        windowParam.gravity = mGravity;
        // 以下这两句是为了保证按钮可以水平满屏,必须放在show后面显示
        windowParam.width = ViewGroup.LayoutParams.MATCH_PARENT;
        windowParam.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
        onWindowAttributesChanged(windowParam);
    }

    public void setGrivate(int grivate) {
        mGravity = grivate;
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
        this.mCloseOnTouchOutside = cancel;
    }

    @Override
    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
        this.mCancelable = flag;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Animation createTranslationInAnimation() {
        int type = TranslateAnimation.RELATIVE_TO_SELF;
        TranslateAnimation tranAnimation = new TranslateAnimation(type, 0, type, 0, type, 1, type, 0);
        tranAnimation.setDuration(500);
        tranAnimation.setFillAfter(true);
        return tranAnimation;
    }


    private Animation createTranslationOutAnimation() {
        int type = TranslateAnimation.RELATIVE_TO_SELF;
        TranslateAnimation tranAnimation = new TranslateAnimation(type, 0, type, 0, type, 0, type, 1);
        tranAnimation.setDuration(500);
        tranAnimation.setFillAfter(true);
        return tranAnimation;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mCancelable && mCloseOnTouchOutside && isOutOfBounds(event)) {
            this.dismiss();
            return false;// 向上传递
        }
        return false;// 向上传递
    }


    /**
     * 判断是不是menu之外的点击区域
     *
     * @param event 点击的event事件
     */
    private boolean isOutOfBounds(MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
        final View decorView = getWindow().getDecorView();
        return (x < -slop) || (y < -slop)
                || (x > (decorView.getWidth() + slop))
                || (y > (decorView.getHeight() + slop));
    }

}
