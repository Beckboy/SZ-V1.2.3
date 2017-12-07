package com.junhsue.ksee.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.utils.CommonUtils;
import com.junhsue.ksee.utils.ScreenWindowUtil;
import com.junhsue.ksee.utils.SharedPreferencesUtils;
import com.junhsue.ksee.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;


/**
 *
 *
 * 悬浮窗基类
 */
public abstract class BaseSuspendView {

    // 定义浮动窗口布局
    private View mFloatLayout;
    private WindowManager.LayoutParams wmParams;
    // 创建浮动窗口设置布局参数的对象
    private WindowManager mWindowManager;
    private ImageView mFloatView;
    private float startRawX;
    private float startRawY;
    private float moveRawX;
    private float moveRawY;
    private float endRawX;
    private float endRawY;
    private int viewWidth;
    private int viewHeight;
    private int screenHeight;
    private boolean isAdd; //判断是否添加view
    private boolean isShow; // 判断是否显示view
    private boolean isCanMove; // 判断是否可以移动

    private Context mContext;

    /**
     * 是否已经显示在窗口中
     */
    public boolean isShow() {
        if (isCreatedSuspendView() && mFloatLayout != null && mFloatView != null && isShow) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param isShow true 显示， false，不显示
     */
    public void setShow(boolean isShow) {
        this.isShow = isShow;
    }

    /**
     * 是否可以移动
     *
     * @return
     */
    public boolean isCanMove() {
        return isCanMove;
    }

    /**
     * @param isCanMove
     */
    public void setIsCanMove(boolean isCanMove) {
        this.isCanMove = isCanMove;
    }


    /**
     * 判断悬浮窗是否存在
     *
     * @return true 是  false  否
     */
    public boolean isCreatedSuspendView() {
        if (wmParams != null && mWindowManager != null && isAdd) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 创建悬浮窗
     *
     * @param activity
     */
    public void createSuspendView(Activity activity) {
        mContext = activity;
        if (isCreatedSuspendView()) {
            return;
        }
        View susView = suspendView();
        int[] position = suspendPosition();
        //设置悬浮框参数
        setSuspendWindowParam(activity, susView, position);
        //设置悬浮框的touch事件
        setSuspendOnTouchListener();
    }

    private void setSuspendWindowParam(Activity activity, View susView, int... position) {

        wmParams = new WindowManager.LayoutParams();
        // 获取WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        // 设置window type
        wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        // 设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        // 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 设置x、y初始值,如果之前没有操作则以屏幕左上角为原点
        screenHeight = ScreenWindowUtil.getScreenHeight(activity);
        //可根据参数来确定悬浮框的位置
        if (position == null || position.length < 2) {
            wmParams.x = SharedPreferencesUtils.getInstance().getInt(Constants.KEY_SUSPEND_PARAMETER_X, ScreenWindowUtil.getScreenWidth(activity));
            wmParams.y = SharedPreferencesUtils.getInstance().getInt(Constants.KEY_SUSPEND_PARAMETER_Y, screenHeight*3/4);
        } else {
            wmParams.x = position[0];
            wmParams.y = position[1];
        }

        // 设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.token = activity.getWindow().getDecorView().getWindowToken();
        // 获取浮动窗口视图所在布局，如果没有传入view，则适用默认的UI效果
        if (susView == null) {
            mFloatLayout = View.inflate(activity, R.layout.float_layout, null);
            mFloatView = (ImageView) mFloatLayout.findViewById(R.id.float_id);
        } else {
            mFloatLayout = susView;
        }
        isAdd = true;
    }

    private void setSuspendOnTouchListener() {

        final int screenWith = ScreenWindowUtil.getScreenWidth(mContext);

        // 浮动窗口按钮
        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        // 设置监听浮动窗口的触摸移动
        mFloatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtils.isRepeatClick()) {
                    suspendViewOnClick();
                }
            }
        });

        mFloatLayout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isCanMove()) {
                    return false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startRawX = event.getRawX();
                        startRawY = event.getRawY();
                        viewWidth = mFloatView.getMeasuredWidth();
                        viewHeight = mFloatView.getMeasuredHeight();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                        moveRawX = event.getRawX();
                        moveRawY = event.getRawY();
                        // 判断是点击还是滑动
                        if (Math.abs(moveRawX - startRawX) > 5 || Math.abs(moveRawY - startRawY) > 5) {
                            wmParams.x = (int) moveRawX - viewWidth / 2;
                            // 25为状态栏的高度
                            wmParams.y = (int) moveRawY - viewHeight / 2 - ScreenWindowUtil.getStatusHeight(mContext);
                            // 刷新
                            updateViewLayout();
                        }
                        break;
                    case MotionEvent.ACTION_UP:

                        endRawX = event.getRawX();
                        endRawY = event.getRawY();
                        // 点击事件
                        if (Math.abs(endRawX - startRawX) < 5 && Math.abs(endRawY - startRawY) < 5) {
                            if (CommonUtils.isRepeatClick()) {
                                suspendViewOnClick();
                            }
                        } else {
                            if (endRawX > screenWith / 2) {
                                wmParams.x = screenWith;
                            } else {
                                wmParams.x = 0;
                            }
                            updateViewLayout();
                        }
                        SharedPreferencesUtils.getInstance().putInt(Constants.KEY_SUSPEND_PARAMETER_X, wmParams.x);
                        SharedPreferencesUtils.getInstance().putInt(Constants.KEY_SUSPEND_PARAMETER_Y, wmParams.y);
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 设置布局文件
     *
     * @param activity
     * @param inflate  布局UI
     */
    public void unpdateView(Activity activity, View inflate) {
        if (inflate == null) {
            return;
        }
        mFloatLayout = inflate;
    }

    /**
     * 设置图片
     *
     * @param resId
     */
    public void setImageView(int resId) {
        if (mFloatLayout == null && resId < 0) {
            return;
        }
        mFloatView.setImageResource(resId);
    }

    /**
     * 设置图片
     *
     * @param url
     */
    public void setImageView(String url) {
        if (mFloatLayout == null && StringUtils.isBlank(url)) {
            return;
        }
        ImageLoader.getInstance().displayImage(url, mFloatView);
    }

    /**
     * 显示悬浮窗
     */
    public void showSuspendView() {
        if (isShow()) {
            return;
        }
        isShow = true;
        // 添加mFloatLayout
        try {
            if (mFloatLayout.getParent() != null)
                return;
            mWindowManager.addView(mFloatLayout, wmParams);
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

    }

    /**
     * 关闭悬浮窗
     */
    public void dismissSuspendView() {
        if (!isShow() || mWindowManager == null || mFloatLayout == null) {
            return;
        }
        try {
            mWindowManager.removeViewImmediate(mFloatLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }
        isShow = false;
    }

    /**
     * 悬浮窗点击事件
     */
    public abstract void suspendViewOnClick();

    /**
     * 设置悬浮窗的view，可以返回为null，当为null的时候，默认为imageView
     */

    public abstract View suspendView();

    /**
     * 悬浮窗的位置，不设置默认为底部 x,y位置 0元素为x,1元素为y
     */

    public abstract int[] suspendPosition();

    /**
     * 刷新UI
     */
    private void updateViewLayout() {
        // TODO 调用此方法更新UI有时会报View not attached to window manager错误，暂不知道怎么解决
        if (mWindowManager == null || mFloatLayout == null) {
            return;
        }
        try {
            mWindowManager.updateViewLayout(mFloatLayout, wmParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
