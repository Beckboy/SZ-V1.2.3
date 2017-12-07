package com.junhsue.ksee;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.junhsue.ksee.dto.ImageDTO;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.view.ActionBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class PicPagesActivity extends BaseActivity implements View.OnClickListener{

    public final static String PIC_DATA = "pic_datas";
    public final static String PIC_INDEX = "pic_index";
    public final static String PIC_FROM = "pic_from";
    public final static String PIC_LOCAL = "pic_from_sendPost";
    public final static String PIC_POST = "pic_from_showPost";
    private ArrayList<ImageDTO> picDatas = new ArrayList<>();

    private ActionBar mAb;
    private ViewPager mVp;

    private Context mContext;
    private int page_index = 0;
    private String page_from = PIC_LOCAL;

    //ViewPager适配器与监听
    private PicPageListener mPicPageListener;
    private PicPageAdapter mPicPageAdapter;

    @Override
    protected void onReceiveArguments(Bundle bundle) {
        picDatas = (ArrayList<ImageDTO>) bundle.getSerializable(PIC_DATA);
        page_index = bundle.getInt(PIC_INDEX,0) -1;
        page_from = bundle.getString(PIC_FROM, PIC_LOCAL);
    }

    @Override
    protected int setLayoutId() {
        mContext = this;
        return R.layout.act_pic_pages;
    }

    @Override
    protected void onInitilizeView() {

        initView();

    }

    private void initView() {

        mAb = (ActionBar) findViewById(R.id.ab_pic);
        mVp = (ViewPager) findViewById(R.id.vp_pic);

        mAb.setNormalLeftSecondText(String.format("      %s / %s",page_index+1, picDatas.size()));
        if (page_from.equals(PIC_LOCAL)){
            mAb.setRightVisible(View.VISIBLE);
        }else {
            mAb.setRightVisible(View.INVISIBLE);
        }

        mPicPageAdapter = new PicPageAdapter(picDatas);
        mVp.setAdapter(mPicPageAdapter);
        mVp.setCurrentItem(page_index);

        mPicPageListener = new PicPageListener();
        mAb.setOnClickListener(this);
        mVp.addOnPageChangeListener(mPicPageListener);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left_layout:
                finish();
                break;
            case R.id.btn_right_one:
                if (picDatas.size() == 0) return;
                //通知发帖界面删除图片
                sendDeletePicBroadcast(page_index);
                //删除ViewPager中的图片
                picDatas.remove(page_index);
                mAb.setNormalLeftSecondText(String.format("      %s / %s" ,page_index+1 ,picDatas.size()));
                if (picDatas.size() != 0) {
                    mPicPageAdapter.notifyDataSetChanged();
                } else {
                    finish();
                }
                break;
        }
    }

    /**
     * 通知发帖界面删除图片
     * @param index 图片下标
     */
    private void sendDeletePicBroadcast(int index) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt(PIC_INDEX, index);
        intent.putExtras(bundle);
        intent.setAction(SendPostActivity.BROAD_ACTION_DELETE_PIC);
        sendBroadcast(intent);
    }

    /**
     * 实现VierPager监听器接口
     */
    class PicPageListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageSelected(int position) {
            page_index = position;
            mAb.setNormalLeftSecondText(String.format("      %s / %s",position+1,picDatas.size()));
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    /**
     * ViewPager的适配器
     */
    class PicPageAdapter extends PagerAdapter {

        private ArrayList<ImageDTO> picData = new ArrayList<>();

        public PicPageAdapter(ArrayList<ImageDTO> picDatas) {
            this.picData = picDatas;
        }

        @Override
        public int getCount() {
            //选取超大值，实现无限循环
            return picData.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object ;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageview = new ImageView(mContext);
            imageview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT));
            Glide.with(mContext).load(picData.get(position).getPath()).into(imageview);
            container.addView(imageview);
            imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAb.getVisibility() == View.VISIBLE){
                        mAb.setVisibility(View.GONE);
                    }else {
                        mAb.setVisibility(View.VISIBLE);
                    }
                }
            });
            return imageview;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //container.removeView(container.getChildAt(position%mList.size()));
            container.removeView((View) object);
        }
    }


}
