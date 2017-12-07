package com.junhsue.ksee.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.junhsue.ksee.R;
import com.junhsue.ksee.file.FileUtil;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.utils.ScreenWindowUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hunter_J on 2017/9/7.
 */

public class CommonBanner extends FrameLayout {

    private Activity mContext;
    private ViewPager mViewPager;
    private LinearLayout mLinear;

    private List<String> mList = new ArrayList<String>();;

    //Banner圆点的宽高
    private float over_width = 10;
    //Banner圆点的间距
    private float over_dashWidth = 10;
    //Banner圆点的个数
    private int over_size = 0;
    //Banner的点击时间
    private long click_time = 0;

    //ViewPager适配器与监听
    private BannerListener mBannerListener;
    private BannerAdapter mBannerAdapter;
    private OnViewPagerClickListener mOnViewPagerClickListener;

    //ViewPager的高度
    private int viewPager_Height = 400;

    //轮播线程
    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (isStop) break;
                    mContext.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            /**
                             *每搁3秒轮播下一张图*/
                            int position=mViewPager.getCurrentItem()+1;
                            mViewPager.setCurrentItem(position);
                            handler.sendEmptyMessageDelayed(0,3000);
                        }
                    });
                    break;
            }
            return false;
        }
    });

    //圆圈标志位
    private int pointIndex = 0;
    //线程标志
    private boolean isStop = false;
    //轮播最大值
    private int max_value = Integer.MAX_VALUE;

    public CommonBanner(@NonNull Context context) {
        super(context);
        this.mContext = (Activity) context;
        initializeView(context, null);
    }

    public CommonBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = (Activity) context;
        initializeView(context, attrs);
    }

    public CommonBanner(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = (Activity) context;
        initializeView(context, attrs);
    }


    private void initializeView(Context context, AttributeSet attrs) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //
        View view = layoutInflater.inflate(R.layout.component_banner, this);

        over_width = ScreenWindowUtil.getScreenWidth(mContext) * 6 / 375;
        over_dashWidth = over_width;

        initView(view);
        initAction();

        parseStyle(context,attrs);

    }

    /**
     * 设置轮播图片的数据源
     * @param mLists
     */
    public void setData(List<String> mLists, OnViewPagerClickListener onViewPagerClickListener){

        this.mOnViewPagerClickListener = onViewPagerClickListener;
        max_value = mLists.size()*1000;
        this.mList.clear();
        this.mList.addAll(mLists);
        initData();
        this.over_size = mLists.size();
        isSelected(true,mLinear.getChildAt(pointIndex));
        //选取中间值作为起始位置
        //int index = (max_value / 2) - over_size + pointIndex;
        //用来促发监听器
        mViewPager.setCurrentItem(0);

        //开启iin线程，每2秒更新Banner
        if (mList.size() <= 1) return;
        handler.removeMessages(0);
        handler.sendEmptyMessageDelayed(0,3000);
    }


    /**
     * 初始化事件
     */
    private void initAction() {
        mBannerListener = new BannerListener();
        mViewPager.addOnPageChangeListener(mBannerListener);

        mViewPager.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        click_time = System.currentTimeMillis();
                        isStop = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        isStop = false;
                        handler.removeMessages(0);
                        click_time = System.currentTimeMillis() - click_time;
                        handler.sendEmptyMessageDelayed(0,3000);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        isStop = false;
                        handler.removeMessages(0);
                        handler.sendEmptyMessageDelayed(0,3000);
                        break;
                }

                //点击事件
                if (click_time != 0 && click_time < 3000){
                    mOnViewPagerClickListener.onClick(pointIndex);
                }

                return false;
            }
        });


    }

    /**
     * 初始化数据源
     */
    private void initData() {
        View view;
        LinearLayout.LayoutParams params;
        if (over_size == mList.size()) return;
        if (over_size > mList.size()){
            mLinear.removeViews(mList.size(),(over_size-mList.size()));
            return;
        }
        for (int i = over_size; i < mList.size(); i++){
            //设置圆点
            view = new View(mContext);
            params = new LinearLayout.LayoutParams((int) over_width, (int) over_width);
            params.leftMargin = (int) over_dashWidth;
            view.setBackgroundResource(R.drawable.banner_circle);
            view.setLayoutParams(params);
            view.setSelected(false);
            mLinear.addView(view,i);
        }
        if (mList.size() <=1 ){
            mLinear.setVisibility(GONE);
        }
        mBannerAdapter = new BannerAdapter();
        mViewPager.setAdapter(mBannerAdapter);
    }

    /**
     * 设置初始化数据
     * @param context
     * @param attrs
     */
    protected void parseStyle(Context context, AttributeSet attrs) {
        TypedArray tarry = context.obtainStyledAttributes(attrs, R.styleable.home_banner);
        if (tarry.hasValue(R.styleable.home_banner_item_pager_height)){
            viewPager_Height = (int) tarry.getDimension(R.styleable.home_banner_item_pager_height, viewPager_Height);
            //setViewPagerHeight(viewPager_Height);
        }
        tarry.recycle();
    }

    /**
     * 初始化View
     */
    private void initView(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mLinear = (LinearLayout) view.findViewById(R.id.ll_banner);
        setViewPagerHeight();
    }

    /**
     * 设置Banner是否被选中时的大小
     */
    public void isSelected(boolean bool, View view){
        LinearLayout.LayoutParams params;
        if (bool){
            params = new LinearLayout.LayoutParams((int)(over_width*2),(int)over_width);
        }else {
            params = new LinearLayout.LayoutParams((int)over_width,(int)over_width);
        }
        params.leftMargin = (int) over_dashWidth;
        view.setLayoutParams(params);
        view.setSelected(bool);
    }

    /**
     * 取消轮播
     */
    public void onActivityDestroy(){
        isStop = true;
        handler.removeMessages(0);
    }

    /**
     * 启动轮播
     */
    public void onActivityResume(){
        isStop = false;
        handler.removeMessages(0);
        handler.sendEmptyMessageDelayed(0,3000);
    }

    public void setViewPagerHeight(){
        int width = ScreenWindowUtil.getScreenWidth(mContext);
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        params.width=width;
        params.height=width*166/375;
        mViewPager.setLayoutParams(params);
    }

    public interface OnViewPagerClickListener{
        void onClick(int index);
    }

    /**
     * 实现VierPager监听器接口
     */
    class BannerListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageSelected(int position) {
            int newPosition = position % mList.size();
            mLinear.getChildAt(pointIndex).setSelected(false);
            isSelected(false,mLinear.getChildAt(pointIndex));
            mLinear.getChildAt(newPosition).setSelected(true);
            isSelected(true,mLinear.getChildAt(newPosition));
            //更新标志位
            pointIndex = newPosition;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            click_time = 0;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    /**
     * ViewPager的适配器
     */
    class BannerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            //选取超大值，实现无限循环
            return max_value;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object ;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
             int index = position % mList.size();
            String url=mList.get(index);
            ImageView imageview = new ImageView(mContext);
            imageview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
            imageview.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(mContext).load(url).into(imageview);
            container.addView(imageview);
            return imageview;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //container.removeView(container.getChildAt(position%mList.size()));
            container.removeView((View) object);
        }
    }



}
