package com.junhsue.ksee.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.junhsue.ksee.BaseActivity;
import com.junhsue.ksee.ColleageLiveLookActivity;
import com.junhsue.ksee.ConfirmOrderActivity;
import com.junhsue.ksee.R;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.entity.GoodsInfo;
import com.junhsue.ksee.entity.LiveEntity;
import com.junhsue.ksee.file.FileUtil;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.net.api.OKHttpCourseImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.url.WebViewUrl;
import com.junhsue.ksee.utils.DateUtils;
import com.junhsue.ksee.utils.DensityUtil;
import com.junhsue.ksee.utils.NumberFormatUtils;
import com.junhsue.ksee.utils.ShareUtils;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.ActionSheet;
import com.junhsue.ksee.view.WebPageView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.social.UMPlatformData;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 直播详情页
 * Created by longer on 17/4/18.
 */

public class ColleageLiveDetailsFragment extends BaseFragment implements View.OnClickListener {


    //更新直播详情状态
    public final static String BROAD_ACTION_LIVE_DETAILS="com.junhsue.ksee.action.live_details";

    //直播
    public final static String PARAMS_LIVE = "params_live";
    //直播id
    public final static String PARAMS_LIVE_ID="params_live_id";
    //直播大小
    public final static String PARAMS_LIVE_SIZE="params_live_size";
//
//    private RoundedImageView mImgLive;
//    //图片状态标签
//    private RoundedImageView mImgLiveTag;
//    //直播标题
//    private TextView mTxtTitle;
//    //内容
//    private TextView mTxtLiveContent;
//    //主讲人
//    private TextView mTxtLiveUser;
//    //演讲时间
//    private TextView mTxtTime;
//    //价格
//    private TextView mTxtPrice;
//    //进入直播
//    //分享
//    private ImageView mImgShare;
//    //
//    private PtrClassicFrameLayout mPtrClassicFrameLayout;
//    //
//    private ImageView mImg;
//    //

    private WebPageView mWebPageView;
    private Button mBtnLive;

    private LiveEntity mLiveEntity;

    private BaseActivity mBaseActivity;

    private ActionSheet shareActionSheetDialog;
    //
    private String mLiveId="";

//    //直播标签
//    private LinearLayout  mLLLiveTag;
//    //直播预告
//    private LinearLayout mLLLiveNoticeTime;
//    //直播预告的时间
//    private TextView mBtnNoticeTime;

    public static  ColleageLiveDetailsFragment newInstance(String liveId){
        ColleageLiveDetailsFragment colleageLiveDetailsFragment =
                new ColleageLiveDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAMS_LIVE_ID,liveId);
        colleageLiveDetailsFragment.setArguments(bundle);
        return  colleageLiveDetailsFragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mBaseActivity = (BaseActivity) activity;
    }


    @Override
    protected int setLayoutId() {
        return R.layout.act_colleage_live_details;
    }


    @Override
    protected void onInitilizeView(View view) {
//        mImgLive = (RoundedImageView) view.findViewById(R.id.img_live);
//        mImgLiveTag = (RoundedImageView) view.findViewById(R.id.img_live_tag);
//        mPtrClassicFrameLayout=(PtrClassicFrameLayout)view.findViewById(R.id.ptr_plassic_frameLayout);
//
//        mTxtTitle = (TextView) view.findViewById(R.id.txtTitle);
//        mTxtLiveContent = (TextView) view.findViewById(R.id.txt_live_content);
//        mTxtLiveUser = (TextView) view.findViewById(R.id.txt_live_user);
//        mTxtTime = (TextView) view.findViewById(R.id.txt_live_time);
//        mTxtPrice = (TextView) view.findViewById(R.id.txt_live_price);
          mWebPageView=(WebPageView)view.findViewById(R.id.web_page_view);
          mBtnLive = (Button) view.findViewById(R.id.btn_live);
//        mImgShare = (ImageView) view.findViewById(R.id.img_share);
//        mLLLiveTag=(LinearLayout)view.findViewById(R.id.ll_live_tag);
//        mLLLiveNoticeTime=(LinearLayout)view.findViewById(R.id.ll_live_tag_start_time);
//        mBtnNoticeTime=(TextView)view.findViewById(R.id.btn_live_tag_start_time);

          //
//        mPtrClassicFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH);
//        mPtrClassicFrameLayout.setPtrHandler(ptrDefaultHandler);
//        //
          mBtnLive.setOnClickListener(this);
//        mImgShare.setOnClickListener(this);
         //

         //mBaseActivity.alertLoadingProgress();
          getLiveDetails();

//        Bundle bundle = getArguments();
//        mLiveEntity = (LiveEntity) bundle.getSerializable(PARAMS_LIVE);

    }


    private void  initilizeData(){

        if (null != mLiveEntity) {
//            mTxtTitle.setText(mLiveEntity.title);
//            mTxtLiveContent.setText(mLiveEntity.content);
//            mTxtLiveUser.setText(mLiveEntity.speaker);
//            mTxtPrice.setText("¥" + NumberFormatUtils.formatPoint(mLiveEntity.price));
            //formatLiveTime(mLiveEntity);
            //setImgTag(mLiveEntity.living_status);
//            ImageLoader.getInstance().displayImage(mLiveEntity.poster, mImgLive,
//                    ImageLoaderOptions.option(R.drawable.img_default_course_system));
            if(mLiveEntity.living_status==LiveEntity.LiveStatus.END){
                mBtnLive.setText("已结束");
                //mLLLiveNoticeTime.setVisibility(View.INVISIBLE);
            }else if(mLiveEntity.living_status==LiveEntity.LiveStatus.NO_START){
                //mLLLiveNoticeTime.setVisibility(View.VISIBLE);
                //mBtnNoticeTime.setText(DateUtils.timestampToPatternTime(mLiveEntity.start_time*1000l,"MM/dd HH:mm"));

            }else if(mLiveEntity.living_status==LiveEntity.LiveStatus.LIVING){
                //mLLLiveNoticeTime.setVisibility(View.INVISIBLE);

            }
        }
    }


    /**
     * 设置直播当前页标签
     * @param position  当前位置
     *
     * @param size   直播总大小
     */
   /* public void setLiveTag(int position,int size){
        if(null==mLLLiveTag) return;
        mLLLiveTag.removeAllViews();
        //
        for(int i=0;i<size;i++){
            LinearLayout linearLayout=new LinearLayout(getActivity());
            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout.setPadding(10,0,10,0);
            //
            ImageView imageView=new ImageView(getActivity());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(DensityUtil.dip2px(getActivity(),13),
                    DensityUtil.dip2px(getActivity(),3)));
            imageView.setBackgroundResource(R.drawable.bg_live_tag_normal);
            if(position==i)
                imageView.setBackgroundResource(R.drawable.bg_live_tag_selected);
            //
            linearLayout.addView(imageView);
            mLLLiveTag.addView(linearLayout);
        }

    }*/

    PtrDefaultHandler ptrDefaultHandler=new PtrDefaultHandler() {

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {

            mBaseActivity.alertLoadingProgress();
            getLiveDetails();

        }
    };

    /**
     * 直播时间格式化
     */
/*    private void formatLiveTime(LiveEntity liveEntity) {
        //获取当前系统时间,获取秒数
        long startTime = liveEntity.start_time * 1000l;
        long endTime = liveEntity.end_time * 1000l;

        String startTimeStr = DateUtils.timestampToPatternTime(startTime, "yyyy年MM月dd日 HH:mm");
        String endTimeStr = DateUtils.timestampToPatternTime(endTime, "yyyy年MM月dd日 HH:mm");
        if (DateUtils.isSameDay(liveEntity.start_time, liveEntity.end_time)) {
            mTxtTime.setText(startTimeStr + " - " + DateUtils.timestampToPatternTime(endTime, "HH:mm"));
        } else {
            mTxtTime.setText(startTimeStr + " — " + endTimeStr);
        }
    }*/


    /**
     * 设置直播状态
     * <p>
     * 1.当前系统的时间 小于直播开始时间,说明直播未开始
     * <p>
     * 2.当前时间大于直播结束时间,直播已结束
     * <p>
     * 3.当前时间大于等于直播开始时间&&小于等于结束时间,直播进行中
     * <p>
     * <p>
     * note : 单位为秒
     */
//    private void setImgTag(int status) {
//        long currentTime = System.currentTimeMillis() / 1000;
//        if (status == LiveEntity.LiveStatus.NO_START) {
//            //直播未开始
//            ImageLoader.getInstance().displayImage("", mImgLiveTag,
//                    ImageLoaderOptions.option(R.drawable.icon_live_tag_no_start));
//
//
//        } else if (status == LiveEntity.LiveStatus.END) {
//            //直播已结束
//            ImageLoader.getInstance().displayImage("", mImgLiveTag,
//                    ImageLoaderOptions.option(R.drawable.icon_live_tag_end));
//        } else if (status == LiveEntity.LiveStatus.LIVING) {
//            //正在直播
//            ImageLoader.getInstance().displayImage("", mImgLiveTag,
//                    ImageLoaderOptions.option(R.drawable.icon_live_tag_ing));
//
//        }
//    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_live:
                handleBtn();
                break;
//            case R.id.img_share:
//                showShareActionSheetDailog();
//                break;
        }
    }


    private void getLiveDetails(){

        mLiveId=getArguments().getString(PARAMS_LIVE_ID, "");
        OKHttpCourseImpl.getInstance().getLiveDetails(mLiveId,
                new RequestCallback<LiveEntity>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
//                mBaseActivity.dismissLoadingDialog();
//                mPtrClassicFrameLayout.refreshComplete();
            }

            @Override
            public void onSuccess(LiveEntity response) {
//                mPtrClassicFrameLayout.refreshComplete();
//                mBaseActivity.dismissLoadingDialog();
                mLiveEntity=response;
                initilizeData();
               // setBtnStatus();
            }
        });
    }
    /**
     * 直播底部按钮分为不同的状态
     * <p>
     * <可以直接进入直播观看页>
     * <p>
     * 1.正在直播&直播免费
     * <p>
     * 2.正在直播&用户已购买
     * <p>
     * <p>
     * #按钮设置成"进入直播"
     * <p>
     * <正在直播>
     * <p>
     * 1.用户未购买& 非免费直播,按钮设置成"购买直播",点击跳转到购买页
     * <p>
     * <直播未开始>
     * <p>
     * 1.用户未购买,非免费直播&可以直接进入购买详情,按钮设置成 "购买直播",按钮可以点击
     * <p>
     * 2.用户已购买 或者 免费和收费直播,按钮设置成 "进入直播",点击按钮提示"直播还未开始"
     * <p>
     * <p>
     * <直播已结束>
     * <p>
     * 1.不能购买,按钮设置成"已结束",按钮不能点击
     */

    private void setBtnStatus() {
        if(null==mLiveEntity) return;
        if(mLiveEntity.shelf_status==2){
            mBtnLive.setBackgroundColor(mBaseActivity.getResources().getColor(R.color.c_gray_9da1a7));
            mBtnLive.setText("已下架");
            return;
        }
        if (mLiveEntity.living_status == LiveEntity.LiveStatus.NO_START &&
                mLiveEntity.status == LiveEntity.LivePayStatus.PAY_NO &&
                mLiveEntity.is_free == 0) {
            mBtnLive.setBackgroundColor(mBaseActivity.getResources().getColor(R.color.c_red_B33030));
            mBtnLive.setText("购买直播");
        } else if (mLiveEntity.living_status == LiveEntity.LiveStatus.LIVING &&
                mLiveEntity.status == LiveEntity.LivePayStatus.PAY_NO &&
                mLiveEntity.is_free == 0) {
            mBtnLive.setBackgroundColor(mBaseActivity.getResources().getColor(R.color.c_red_B33030));
            mBtnLive.setText("购买直播");
        } else if (mLiveEntity.living_status == LiveEntity.LiveStatus.END) {
            mBtnLive.setBackgroundColor(mBaseActivity.getResources().getColor(R.color.c_gray_9da1a7));
            mBtnLive.setText("已结束");
        } else if((mLiveEntity.living_status==LiveEntity.LiveStatus.LIVING &&
                mLiveEntity.is_free == 1) || (mLiveEntity.living_status==LiveEntity.LiveStatus.LIVING &&
                mLiveEntity.status==LiveEntity.LivePayStatus.PAY_OK)){
            mBtnLive.setText("进入直播");
            mBtnLive.setBackgroundColor(mBaseActivity.getResources().getColor(R.color.c_green_59b197));
        }else if((mLiveEntity.living_status==LiveEntity.LiveStatus.NO_START &&
                mLiveEntity.status==LiveEntity.LivePayStatus.PAY_OK) ||
                mLiveEntity.living_status==LiveEntity.LiveStatus.NO_START &&
                        mLiveEntity.is_free == 1 ){
            mBtnLive.setText("进入直播");
            mBtnLive.setBackgroundColor(mBaseActivity.getResources().getColor(R.color.c_green_59b197));
        }

    }




    /**
     * 跳转到直播观看页
     */
    private void enterLiveLookPage() {
        if (null == mLiveEntity) return;

        Bundle bundle = new Bundle();
        bundle.putSerializable(ColleageLiveLookActivity.PARAMS_LIVE, mLiveEntity);
        mBaseActivity.launchScreen(ColleageLiveLookActivity.class, bundle);

        StatisticsUtil.getInstance(mBaseActivity).onCountActionDot("5.1.2");
    }


    /**
     * 跳转直播购买页
     */
    private void jumpLivePay() {
        GoodsInfo goodsInfo = GoodsInfo.cloneOject(mLiveEntity.poster, mLiveEntity.live_id,
                mLiveEntity.title, mLiveEntity.price,
                GoodsInfo.GoodsType.getType(mLiveEntity.business_id));
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConfirmOrderActivity.PARAMS_GOODS_INFO, goodsInfo);
        mBaseActivity.launchScreen(ConfirmOrderActivity.class, bundle);

        StatisticsUtil.getInstance(mBaseActivity).onCountActionDot("5.1.1");

    }

    /**
     * 按钮处理
     */
    private void handleBtn() {
        if(null==mLiveEntity) return;

        //已下架
        if(mLiveEntity.shelf_status==2){
            return;
        }
        if (mLiveEntity.living_status == LiveEntity.LiveStatus.LIVING && mLiveEntity.is_free == 1) {
            enterLiveLookPage();

        } else if (mLiveEntity.living_status == LiveEntity.LiveStatus.LIVING &&
                mLiveEntity.status == LiveEntity.LivePayStatus.PAY_OK) {
            enterLiveLookPage();

        } else if (mLiveEntity.living_status == LiveEntity.LiveStatus.LIVING &&
                mLiveEntity.status == LiveEntity.LivePayStatus.PAY_NO &&
                mLiveEntity.is_free == 0) {

            jumpLivePay();
        } else if (mLiveEntity.living_status == LiveEntity.LiveStatus.NO_START &&
                mLiveEntity.status == LiveEntity.LivePayStatus.PAY_NO &&
                mLiveEntity.is_free == 0) {
            // 直播未开始 & 未支付 &非免费
            jumpLivePay();
        } else if (mLiveEntity.living_status == LiveEntity.LiveStatus.NO_START &&
                mLiveEntity.status == LiveEntity.LivePayStatus.PAY_NO &&
                mLiveEntity.is_free == 1) {
            ToastUtil.getInstance(mBaseActivity.getApplicationContext()).setContent("直播还未开始").setShow();
        } else if (mLiveEntity.living_status == LiveEntity.LiveStatus.NO_START &&
                mLiveEntity.status == LiveEntity.LivePayStatus.PAY_OK &&
                mLiveEntity.is_free == 0) {
            ToastUtil.getInstance(mBaseActivity.getApplicationContext()).setContent("直播还未开始").setShow();
        } else if (mLiveEntity.living_status == LiveEntity.LiveStatus.END) {
            return;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    /**
     * 分享弹出框
     */
    private void showShareActionSheetDailog() {
        final String path = FileUtil.getImageFolder() + "/" + mLiveEntity.poster.hashCode();//如果分享图片获取该图片的本地存储地址
        final String webPage = String.format(WebViewUrl.H5_SHARE_LIVE, mLiveEntity.live_id);
        final String title = mLiveEntity.title;
        final String desc = mLiveEntity.description;

        shareActionSheetDialog = new ActionSheet(getActivity());
        LayoutInflater inflater = (LayoutInflater) mBaseActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.component_share_dailog, null);
        shareActionSheetDialog.setContentView(view);
        shareActionSheetDialog.show();


        LinearLayout llShareFriend = (LinearLayout) view.findViewById(R.id.ll_share_friend);
        LinearLayout llShareCircle = (LinearLayout) view.findViewById(R.id.ll_share_circle);
        TextView cancelButton = (TextView) view.findViewById(R.id.tv_cancel);

        llShareFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShareUtils.getInstance(mBaseActivity).sharePage(ShareUtils.SendToPlatformType.TO_FRIEND, webPage, path,
                        title, desc, UMPlatformData.UMedia.WEIXIN_FRIENDS);

                if (shareActionSheetDialog != null) {
                    shareActionSheetDialog.dismiss();
                }
                StatisticsUtil.getInstance(mBaseActivity).onCountActionDot("5.1.4");

            }
        });

        llShareCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.getInstance(mBaseActivity).sharePage(ShareUtils.SendToPlatformType.TO_CIRCLE, webPage, path,
                        title, desc, UMPlatformData.UMedia.WEIXIN_CIRCLE);
                if (shareActionSheetDialog != null) {
                    shareActionSheetDialog.dismiss();
                }
                StatisticsUtil.getInstance(mBaseActivity).onCountActionDot("5.1.3");
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shareActionSheetDialog.isShowing()) {
                    shareActionSheetDialog.dismiss();
                }
            }
        });


    }


    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action=intent.getAction();
            //更新直播状态
            if(BROAD_ACTION_LIVE_DETAILS.equals(action)){
                mBaseActivity.alertLoadingProgress();
                Trace.i("udapte live details!");
                getLiveDetails();
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        if (shareActionSheetDialog != null) {
            shareActionSheetDialog.dismiss();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(BROAD_ACTION_LIVE_DETAILS);
        mBaseActivity.registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBaseActivity.unregisterReceiver(broadcastReceiver);
    }
}
