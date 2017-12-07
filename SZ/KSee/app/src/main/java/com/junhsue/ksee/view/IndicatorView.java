package com.junhsue.ksee.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.junhsue.ksee.R;
import com.junhsue.ksee.adapter.IndicatorViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <p/>
 * 带有指示的view
 * 使用说明：
 * 1.如果直接是静态显示则直接调用setPagerViewList方法即可。
 * 2.如果是需要自动切换的，需要在布局文件中增加设置该can_play属性，
 * 然后在onResume方法中startAutoPlay()，在onPause方法中stopAutoPlay()
 * 3.
 */
public class IndicatorView extends FrameLayout {
    private final int RUNNING = 1;
    private final int STOP = 2;
    private final int DEFAULT_DOTS_GRAVITY = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
    private Handler autoHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RUNNING: {
                    break;
                }
                case STOP: {
                    stopAutoPlay();
                    break;
                }
            }
            refreshAllLayout();
        }
    };
    private View indicatorView;
    private ViewPager viewPager;
    private LinearLayout dotLayout;
    private List<View> pagerViewList;
    private List<ImageView> dotViewList;
    private IndicatorViewPagerAdapter pagerAdapter;
    private Context context;
    private int id;
    private int normalDotRes = R.drawable.icon_home_ask_dot_normal;
    private int selectedDotRes = R.drawable.icon_home_ask_dot_selected;
    private ScheduledExecutorService autoService;
    private int currentAutoIndex = -1;
    private int preState = 0;
    private boolean canAutoPlay = false;
    private int dotLayoutBottomMargin;
    private int dotLayoutTopMargin;
    private int dotLayoutMarginLeft;
    private int dotLayoutMarginRight;
    private int indicatorWidth;
    private int indicatorHeight;
    private int dotsGravity = DEFAULT_DOTS_GRAVITY;
    private int dotsHorizontalSpace;
    private IndicatorOnPageChangeListener indicatorPageChangeListener;

    public IndicatorView(Context context) {
        super(context);
        initLayout(context, null);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context, attrs);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout(context, attrs);
    }

    private void initLayout(Context context, AttributeSet attrs) {
        this.context = context;
        pagerViewList = new ArrayList<>();
        dotViewList = new ArrayList<>();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        indicatorView = inflater.inflate(R.layout.view_indicator, this, true);
        viewPager = (ViewPager) indicatorView.findViewById(R.id.vp_indicator_image);
        dotLayout = (LinearLayout) indicatorView.findViewById(R.id.ll_dot_layout);
        initLayout();
        //从xml文件中读取设置的dotlayout的属性
        getAttrsFromXml(attrs);
        refreshDotLayoutParamsByAttrs();
    }

    /***
     * 根据设置的属性进行更新UI
     */
    private void initLayout() {

        //加载ViewPager
        pagerAdapter = new IndicatorViewPagerAdapter(pagerViewList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOverScrollMode(ViewPager.OVER_SCROLL_NEVER);

        //设置ViewPager的Adapter
        viewPager.setOnPageChangeListener(new ViewPagerChangerListener());


    }

    public void setIndicatorPageChangeListener(IndicatorOnPageChangeListener listener) {
        this.indicatorPageChangeListener = listener;
    }

    /**
     * 获取当前view的id
     */
    public int getViewPagerId() {
        return id;
    }

    /**
     * 设置当前view的id
     */
    public void setViewPagerId(int id) {
        this.id = id;
    }

    /**
     * 设置viewpager的view集合
     */
    public void setPagerViewList(List<View> list) {
        resetIndicatorView();
        pagerViewList.addAll(list);
        pagerAdapter.notifyDataSetChanged();
        //增加小红点
        addImageIndicator();
        refreshAllLayout();
    }

    /**
     * 设置小圆点的背景图片
     */
    public void setDotDrawableRes(int normal, int selected) {
        this.normalDotRes = normal;
        this.selectedDotRes = selected;
    }

    public void setCanAutoPlay(boolean isCan) {
        this.canAutoPlay = isCan;
    }


    /**
     * 从xml文件中读取设置的dotLayout的属性
     */
    private void getAttrsFromXml(AttributeSet attrs) {
        dotsHorizontalSpace = getResources().getDimensionPixelOffset(R.dimen.dimen_10px);
        if (attrs == null) {
            return;
        }
        TypedArray typed = this.context.obtainStyledAttributes(attrs, R.styleable.IndicatorView);
        if (typed == null) {
            return;
        }
        if (typed.hasValue(R.styleable.IndicatorView_dotmargin_bottom)) {
            dotLayoutBottomMargin = typed.getDimensionPixelSize(R.styleable.IndicatorView_dotmargin_bottom, context.getResources().getDimensionPixelSize(R.dimen.dimen_12px));
        }
        if (typed.hasValue(R.styleable.IndicatorView_dotmargin_top)) {
            dotLayoutTopMargin = typed.getDimensionPixelSize(R.styleable.IndicatorView_dotmargin_top, context.getResources().getDimensionPixelSize(R.dimen.dimen_12px));
        }
        if (typed.hasValue(R.styleable.IndicatorView_can_play)) {
            canAutoPlay = typed.getBoolean(R.styleable.IndicatorView_can_play, false);
        }

        if (typed.hasValue(R.styleable.IndicatorView_dots_gravity)) {
            dotsGravity = typed.getInt(R.styleable.IndicatorView_dots_gravity, DEFAULT_DOTS_GRAVITY);
        }

        if (typed.hasValue(R.styleable.IndicatorView_dotmargin_left)) {
            dotLayoutMarginLeft = typed.getDimensionPixelSize(R.styleable.IndicatorView_dotmargin_left, 0);
        }
        if (typed.hasValue(R.styleable.IndicatorView_dotmargin_right)) {
            dotLayoutMarginRight = typed.getDimensionPixelSize(R.styleable.IndicatorView_dotmargin_right, 0);
        }

        if (typed.hasValue(R.styleable.IndicatorView_dots_horizontal_space)) {
            dotsHorizontalSpace = typed.getDimensionPixelOffset(R.styleable.IndicatorView_dots_horizontal_space, context.getResources().getDimensionPixelOffset(R.dimen.dimen_10px));
        }

        TypedArray typeAttrs = this.context.obtainStyledAttributes(attrs, new int[]{android.R.attr.layout_height
                , android.R.attr.layout_width});
        indicatorHeight = typeAttrs.getLayoutDimension(0, LayoutParams.WRAP_CONTENT);
        indicatorWidth = typeAttrs.getLayoutDimension(1, LayoutParams.MATCH_PARENT);
        typeAttrs.recycle();
        typed.recycle();

    }

    /**
     * 获取小圆点dotLayout布局的布局参数
     */
    private void refreshDotLayoutParamsByAttrs() {
        //设置根布局的参数
        LayoutParams rootParams = new LayoutParams(indicatorWidth, indicatorHeight);
        this.setLayoutParams(rootParams);

        LayoutParams dotLayoutParams = (LayoutParams) dotLayout.getLayoutParams();
        dotLayoutParams.gravity = dotsGravity;

        dotLayoutParams.leftMargin = dotLayoutMarginLeft;
        dotLayoutParams.rightMargin = dotLayoutMarginRight;
        dotLayoutParams.topMargin = dotLayoutTopMargin;
        dotLayoutParams.bottomMargin = dotLayoutBottomMargin;

        setCanAutoPlay(canAutoPlay);
    }

    /**
     * 将view进行还原为初始状态
     */
    private void resetIndicatorView() {
        //将所有的小红点移除
        dotLayout.removeAllViews();
        dotViewList.clear();
        //将view的list清空
        pagerViewList.clear();
        currentAutoIndex = -1;//自动播放的position为初始值
    }

    /**
     * 设置view为第几页
     */
    public void setCurrentItem(int item) {
        viewPager.setCurrentItem(item, true);
    }

    /**
     * 更新选中的小红点
     */
    private void refreshDotIndicatorByCurrentPosition(int position) {
        if (position < 0) {
            position = 0;
        }
        if (dotViewList == null || dotViewList.size() < 2) {
            dotLayout.setVisibility(View.GONE);
            return;
        }
        dotLayout.setVisibility(View.VISIBLE);

        for (int i = 0; i < dotViewList.size(); i++) {
            //不是当前选中的pager，其小圆点设置为未选中的状态
            if (position == i) {
                dotViewList.get(i).setImageResource(selectedDotRes);
            } else {
                dotViewList.get(i).setImageResource(normalDotRes);
            }
        }
    }

    /**
     * 设置单个小圆点
     */
    private void addImageIndicator() {
        if (pagerViewList == null || pagerViewList.size() == 0) {
            return;
        }
        for (int i = 0; i < pagerViewList.size(); i++) {
            ImageView dotView = new ImageView(context);
            dotLayout.addView(dotView); //将小圆点加到dotView布局当中
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) dotView.getLayoutParams();
      /*      params.width = (int) context.getResources().getDimension(R.dimen.dimen_12px);
            params.height = (int) context.getResources().getDimension(R.dimen.dimen_12px);*/
            params.rightMargin = dotsHorizontalSpace;
            dotViewList.add(dotView); //将小圆点加到小圆点的Imageview的list中，在进行切换的时候进行设置背景色
        }
    }

    public void startAutoPlay() {
        if (!canAutoPlay) {
            return;
        }
        stopAutoPlay();
        if (autoService == null) {
            autoService = Executors.newSingleThreadScheduledExecutor();
        }
        autoService.scheduleAtFixedRate(new AutoTask(), 3, 3, TimeUnit.SECONDS);
    }

    public void stopAutoPlay() {
        if (autoService != null) {
            autoService.shutdown();
            autoService = null;
        }
    }

    private void refreshAllLayout() {
        if (pagerAdapter == null) {
            return;
        }
        pagerAdapter.notifyDataSetChanged();
        setCurrentItem(currentAutoIndex);
        refreshDotIndicatorByCurrentPosition(currentAutoIndex);
    }

    private class ViewPagerChangerListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            currentAutoIndex = position;  //若是手动滑动的时候也要改变自动播放的index
            refreshDotIndicatorByCurrentPosition(position);
        }

        /**
         * @param （0，1，2）.arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做。
         *                     int SCROLL_STATE_IDLE = 0; SCROLL_STATE_DRAGGING = 1;int SCROLL_STATE_SETTLING = 2;
         */
        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case ViewPager.SCROLL_STATE_DRAGGING: {  // 正在滑动
                    if (canAutoPlay) {
                        stopAutoPlay();
                    }
                    break;
                }
                case ViewPager.SCROLL_STATE_IDLE: //默示什么都没做
                case ViewPager.SCROLL_STATE_SETTLING: { //默示滑动完毕
                    if (autoService == null && canAutoPlay) {
                        startAutoPlay();
                    }
                    break;
                }
            }
            if (preState == ViewPager.SCROLL_STATE_DRAGGING && state == ViewPager.SCROLL_STATE_IDLE
                    && (currentAutoIndex == 0 || currentAutoIndex == -1)) {
                //滑动到最后一个的时候的监听事件
                if (indicatorPageChangeListener != null) {
                    indicatorPageChangeListener.onPageScrollBegin();
                    preState = state;
                    return;
                }
                viewPager.setCurrentItem(pagerAdapter.getCount() - 1, true);
            } else if (preState == ViewPager.SCROLL_STATE_DRAGGING && state == ViewPager.SCROLL_STATE_IDLE
                    && currentAutoIndex == (pagerAdapter.getCount() - 1)) {
                //滑动到最后一个的时候的监听事件
                if (indicatorPageChangeListener != null) {
                    indicatorPageChangeListener.onPageScrollEnd();
                    preState = state;
                    return;
                }
                viewPager.setCurrentItem(0, true);
            }
            preState = state;
        }
    }

    public interface IndicatorOnPageChangeListener {
        //滑动到最后一个的时候的监听事件
        void onPageScrollEnd();

        //滑动到最后一个的时候的监听事件
        void onPageScrollBegin();
    }

    private class AutoTask implements Runnable {
        public void run() {
            if (pagerViewList == null || pagerViewList.size() == 0) {
                return;
            }
            currentAutoIndex = (currentAutoIndex + 1) % pagerViewList.size();
            if (pagerViewList.size() == 1) {
                Message msg = autoHandler.obtainMessage();
                msg.what = STOP;
                autoHandler.sendMessage(msg);
                return;
            }
            Message msg = autoHandler.obtainMessage();
            msg.what = RUNNING;
            autoHandler.sendMessage(msg);
        }
    }
}
