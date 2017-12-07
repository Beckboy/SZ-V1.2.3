package com.junhsue.ksee.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.junhsue.ksee.BaseActivity;
import com.junhsue.ksee.R;
import com.junhsue.ksee.adapter.CommonFragmentPageAdapter;
import com.junhsue.ksee.dto.CircleListDTO;
import com.junhsue.ksee.entity.CircleEntity;
import com.junhsue.ksee.net.api.OkHttpCircleImpI;
import com.junhsue.ksee.net.callback.RequestCallback;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;

import java.util.ArrayList;

/**
 * 社区首页
 * Created by longer on 17/10/24.
 */


public class CommunityHomeFragment extends BaseFragment implements View.OnClickListener{


    private MagicIndicator magicIndicator;
    //
    private ArrayList<Fragment> mFagements=new ArrayList<>();

    private ViewPager mViewPager;
    //
    private CommonFragmentPageAdapter  mPageAdapter;
    //
    private Context mContext;
    //当无网络时展示空的视图
    LinearLayout mLLEmptyView;
    //
    LinearLayout mLLEmptyViewContent;
    //
    BaseActivity mActivity;

    public static  CommunityHomeFragment newInstance(){
        CommunityHomeFragment commonFragmentPageAdapter=new CommunityHomeFragment();
        return commonFragmentPageAdapter;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_community_home;
    }


    /**
     * 初始化Fragment
     */
    private  void initFramgnet(){
        if(mFagements.size()>0)
            mFagements.clear();
        mFagements.add(CommunityMyCircleFragment.newInstance());
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=(BaseActivity)activity;
    }

    @Override
    protected void onInitilizeView(View view) {
        mContext=getContext();
        //
        //initFramgnet();
        magicIndicator=(MagicIndicator)view.findViewById(R.id.magic_indicator);
        mViewPager=(ViewPager)view.findViewById(R.id.view_pager);
        //
        mLLEmptyView=(LinearLayout)view.findViewById(R.id.empty_view);
        mLLEmptyViewContent=(LinearLayout)view.findViewById(R.id.content);
        //
        mPageAdapter=new CommonFragmentPageAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.setOffscreenPageLimit(1);
        //
        mLLEmptyViewContent.setOnClickListener(this);

        //initFramgnet();
        getCircleList();
    }


    /**
     *
     */
    private void getCircleList() {
        //父类id标签筛选
        String parentId="0";
        OkHttpCircleImpI.getInstance().getCircleList(parentId,
                new RequestCallback<CircleListDTO>() {

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        magicIndicator.setVisibility(View.GONE);
                        mLLEmptyView.setVisibility(View.VISIBLE);
                        mActivity.dismissLoadingDialog();
                    }

                    @Override
                    public void onSuccess(CircleListDTO response) {
                        mActivity.dismissLoadingDialog();
                        if (null == response || response.list.size() == 0){
                            magicIndicator.setVisibility(View.GONE);
                            mLLEmptyView.setVisibility(View.VISIBLE);
                            return;
                        }
                        mLLEmptyView.setVisibility(View.GONE);
                        magicIndicator.setVisibility(View.VISIBLE);


                        //initFramgnet();
                        mFagements.clear();
                        mPageAdapter.clear();

                        mFagements.add(CommunityMyCircleFragment.newInstance());
                        for(int i=0;i<response.list.size();i++) {
                            CircleEntity circleEntity = response.list.get(i);
                            mFagements.add(CommunityCircleFragment.newInstance(circleEntity.id));
                        }
                        mPageAdapter.modifyList(mFagements);
                        initMagicIndicator(response.list);
                    }
                });

    }

    /**
     * 初始化指示器
     * @param arrayList
     */
    private void initMagicIndicator(final ArrayList<CircleEntity> arrayList) {
        final  ArrayList<CircleEntity> arrayListLocal=arrayList;
        magicIndicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setScrollPivotX(0.8f);

        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            //标题大小等于所有圈子＋我的去找你
            @Override
            public int getCount() {
                return mFagements.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                //

                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                clipPagerTitleView.setTextColor(Color.parseColor("#55626E"));
                clipPagerTitleView.setClipColor(Color.parseColor("#55626E"));
                clipPagerTitleView.setTextSize(getResources().getDimension(R.dimen.f_30));

                //如果下标等于0，则代表的是我的去圈子
                if(index==0){
                    clipPagerTitleView.setText(mContext.getString(R.string.msg_my_circle));

                }else{
                    CircleEntity circleEntity=arrayList.get(index-1);
                    if(null!=circleEntity){
                        clipPagerTitleView.setText(circleEntity.name);

                    }
                }

                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index,false);
                    }
                });
                return clipPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(Color.parseColor("#FE603C"));
                indicator.setLineHeight(8);
                indicator.setXOffset(40);
                return  indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.content:
            mActivity.alertLoadingProgress();
            getCircleList();
            break;
        }
    }
}
