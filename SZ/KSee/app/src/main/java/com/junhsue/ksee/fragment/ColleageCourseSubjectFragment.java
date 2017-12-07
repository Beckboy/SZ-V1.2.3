package com.junhsue.ksee.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.junhsue.ksee.ApplicationEnterActivity;
import com.junhsue.ksee.BaseActivity;
import com.junhsue.ksee.CourseDetailsActivity;
import com.junhsue.ksee.CourseSystemDetailsActivity;
import com.junhsue.ksee.LiveDetailsActivity;
import com.junhsue.ksee.LiveListActivity;
import com.junhsue.ksee.R;
import com.junhsue.ksee.VideoActivity;
import com.junhsue.ksee.VideoDetailActivity;
import com.junhsue.ksee.adapter.CourseSubjectAdapter;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.IntentLaunch;
import com.junhsue.ksee.dto.CourseDTO;
import com.junhsue.ksee.dto.CourseSystemDTO;
import com.junhsue.ksee.dto.LiveDTO;
import com.junhsue.ksee.dto.VideoDTO;
import com.junhsue.ksee.entity.Course;
import com.junhsue.ksee.entity.CourseSystem;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.net.api.ICourse;
import com.junhsue.ksee.net.api.OKHttpCourseImpl;
import com.junhsue.ksee.net.callback.BroadIntnetConnectListener;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.url.WebViewUrl;
import com.junhsue.ksee.utils.CommonUtils;
import com.junhsue.ksee.utils.ScreenWindowUtil;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.CircleImageView;
import com.junhsue.ksee.view.CommonListView;
import com.junhsue.ksee.view.LivePosterView;
import com.junhsue.ksee.view.VedioPosterView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 主题课
 * Created by longer on 17/3/24.
 */

public class ColleageCourseSubjectFragment extends BaseFragment implements BroadIntnetConnectListener.InternetChanged, View.OnClickListener{

    public final static  String PARAMS_COURSE="params_course";

    private PtrClassicFrameLayout mPtrFrame;
    private View vHead_Data,vHead_Error;

    //系统课的4个列表
    private ImageView mImgOne,mImgTwo,mImgThree,mImgFour, mImgOrgApplication;
    private List<ImageView> courses = new ArrayList<ImageView>();

    //主题课列表
    private TextView mTvActivityTitle;
    private CommonListView mCourseListView;
    private CircleImageView mCircleNoData;
    private TextView mTvNoData;
    public Button btn_reloading;
    private CourseSubjectAdapter<Course> mCourseSubjectApdater;

    //直播
    private RelativeLayout mRlLiveMore,mRlVedioMore;
    private LinearLayout mLiveData, mLlVedioData;
    private View mVLive,mVVideo,mVOrz;


    //当前页数0页开始
    private int mPageIndex = 0;

    //页数的数据大小
    private String mLimit = "10";

    private BaseActivity mBaseActivity;

    public  static  ColleageCourseSubjectFragment  newInstance(){
        ColleageCourseSubjectFragment colleageCourseSubjectFragmen=new ColleageCourseSubjectFragment();
        return  colleageCourseSubjectFragmen;
    }


    @Override
    public void onAttach(Activity activity) {
        mBaseActivity=(BaseActivity)activity;
        super.onAttach(activity);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_colleage_course_subject;
    }

    @Override
    protected void onInitilizeView(View view) {

        mPtrFrame=(PtrClassicFrameLayout)view.findViewById(R.id.ptrClassicFrameLayout);
        mCourseListView=(CommonListView)view.findViewById(R.id.expandableb_List_View);

        FrameLayout frameLayout = new FrameLayout(getActivity());
        vHead_Data = View.inflate(mBaseActivity,R.layout.head_colleage_course,null);
        vHead_Data.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
        vHead_Error = View.inflate(mBaseActivity,R.layout.item_myanswer_head,null);
        vHead_Error.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        vHead_Error.setVisibility(View.GONE);
        frameLayout.addView(vHead_Error,new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
        mTvActivityTitle = (TextView) view.findViewById(R.id.tv_colleage_activity);
        mCourseListView.addHeaderView(frameLayout);
        mCircleNoData = (CircleImageView) mCourseListView.findViewById(R.id.img_answer_nodata);
        mTvNoData = (TextView) mCourseListView.findViewById(R.id.tv_answer_nodata);
        btn_reloading = (Button) mCourseListView.findViewById(R.id.btn_answer_reloading);
        btn_reloading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataReset();
            }
        });

        setImageSystem(view);
        setImageOrg(view);

        mRlLiveMore = (RelativeLayout) view.findViewById(R.id.rl_colleage_live_more);
        mRlVedioMore = (RelativeLayout) view.findViewById(R.id.rl_colleage_video_more);
        mLiveData = (LinearLayout) view.findViewById(R.id.ll_colleage_live);
        mLlVedioData = (LinearLayout) view.findViewById(R.id.ll_colleage_video);
        mVLive = view.findViewById(R.id.v_live_bottom);
        mVVideo = view.findViewById(R.id.v_video_bottom);
        mVOrz = view.findViewById(R.id.v_orz_bottom);

        mPtrFrame.setPtrHandler(ptrDefaultHandler2);
        mPtrFrame.setMode(PtrFrameLayout.Mode.REFRESH);
        //
        mCourseSubjectApdater=new CourseSubjectAdapter(getActivity());
        mCourseListView.setAdapter(mCourseSubjectApdater);
        //
        mCourseListView.setOnItemClickListener(courseItemClickListener);

        if (!CommonUtils.getIntnetConnect(mBaseActivity)){
            setNoNet();
        }
        mPageIndex=0;
        getData();

        if (null != mBaseActivity.con){
            mBaseActivity.con.setInternetChanged(this);
        }

        mRlLiveMore.setOnClickListener(this);
        mRlVedioMore.setOnClickListener(this);


    }

    /**
     * 设置组织机构入驻
     */
    private void setImageOrg(View view) {
        mImgOrgApplication = (ImageView) view.findViewById(R.id.img_colleage_origanization);
        mImgOrgApplication.setOnClickListener(this);

        //设置展示标签的宽、高
        int width = ScreenWindowUtil.getScreenWidth(mBaseActivity);
        int height = (int) (width * 180.0/750.0);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);
        mImgOrgApplication.setLayoutParams(layoutParams);
    }

    /**
     * 设置系统课的图片
     */
    private void setImageSystem(View view) {
        mImgOne = (ImageView) view.findViewById(R.id.img_course_one);
        mImgTwo = (ImageView) view.findViewById(R.id.img_course_two);
        mImgThree = (ImageView) view.findViewById(R.id.img_course_three);
        mImgFour = (ImageView) view.findViewById(R.id.img_course_four);
        List<ImageView> imgs = new ArrayList<ImageView>();
        imgs.add(mImgOne);
        imgs.add(mImgTwo);
        imgs.add(mImgThree);
        imgs.add(mImgFour);
    }


    /**
     * 获取私塾界面所有列表的数据请求
     */
    private void getData(){
        getCourse(); //主题课
//        getCourseSystem(); //系统课
        getVedio(); //录播
        getLive(); //直播
    }

    /**
     * 无网络的状态
     */
    private void setNoNet() {
        if (vHead_Data.getVisibility() == View.VISIBLE) {
            vHead_Data.setVisibility(View.GONE);
            vHead_Error.setVisibility(View.VISIBLE);
        }
        mCircleNoData.setImageResource(R.drawable.common_def_nonet);
        mTvNoData.setText("网络加载出状况了");
        mCircleNoData.setVisibility(View.VISIBLE);
        mTvNoData.setVisibility(View.VISIBLE);
        ToastUtil.getInstance(mBaseActivity).setContent(Constants.INTNET_NOT_CONNCATION).setDuration(Toast.LENGTH_SHORT).setShow();

        btn_reloading.setVisibility(View.VISIBLE);
        mPageIndex=0;
        mCourseSubjectApdater.cleanList();
        mCourseSubjectApdater.modifyList(null);
        mPtrFrame.refreshComplete();
    }

    /**
     * 数据重新刷新
     */
    public void setDataReset(){
        if (!CommonUtils.getIntnetConnect(mBaseActivity)){
            setNoNet();
            return;
        }
        if (vHead_Data.getVisibility() == View.GONE) {
            vHead_Data.setVisibility(View.VISIBLE);
            vHead_Error.setVisibility(View.GONE);
        }
        mPageIndex=0;
        getData();
    }

    /**
     *
     * 获取直播*/
    private void getLive(){
        OKHttpCourseImpl.getInstance().getLiveList(0 ,2 , new RequestCallback<LiveDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                mRlLiveMore.setVisibility(View.GONE);
                mLiveData.setVisibility(View.GONE);
                mVLive.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(LiveDTO response) {
                if (null == response.list){
                    mRlLiveMore.setVisibility(View.GONE);
                    mLiveData.setVisibility(View.GONE);
                    mVLive.setVisibility(View.GONE);
                    return;
                }
                mRlLiveMore.setVisibility(View.VISIBLE);
                mLiveData.setVisibility(View.VISIBLE);
                mVLive.setVisibility(View.VISIBLE);
                setLiveList(response);
            }
        });
    }

    /**
     *
     * 获取录播*/
    private void getVedio(){
        OKHttpCourseImpl.getInstance().getVideoList(0 ,6 , new RequestCallback<VideoDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                mRlVedioMore.setVisibility(View.GONE);
                mLlVedioData.setVisibility(View.GONE);
                mVVideo.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(VideoDTO response) {
                if (null == response.list){
                    mRlVedioMore.setVisibility(View.GONE);
                    mLlVedioData.setVisibility(View.GONE);
                    mVVideo.setVisibility(View.GONE);
                    return;
                }
                mRlVedioMore.setVisibility(View.VISIBLE);
                mLlVedioData.setVisibility(View.VISIBLE);
                mVVideo.setVisibility(View.VISIBLE);
                setVideoList(response);
            }
        });
    }


    /**
     * 设置独家直播数据源(2个)
     *
     */
    private void setLiveList(final LiveDTO response){

        mLiveData.setVisibility(View.VISIBLE);
        mLiveData.removeAllViews();
        for (int i = 0; i < response.list.size() ; i++){

            LivePosterView livePosterView = new LivePosterView(mBaseActivity);

            mLiveData.addView(livePosterView);
            //设置直播标题
            if (response.list.get(i).title != null) {
                livePosterView.setTitle(response.list.get(i).title);
            }
            //设置直播海报背景
            if (mLiveData.getChildAt(i) == null || mLiveData.getChildAt(i).getTag() == null || mLiveData.getChildAt(i).getTag() != response.list.get(i).poster) {
                livePosterView.setPosterBackGround(response.list.get(i).poster);
                mLiveData.getChildAt(i).setTag(response.list.get(i).poster);
            }else {
                livePosterView.setPosterBackGround("");
            }
            //设置直播态icon
            livePosterView.setLive_Living(response.list.get(i).living_status);

            if (i != response.list.size()-1) {
                //设置展示标签的宽、高
                int width = ScreenWindowUtil.getScreenWidth(mBaseActivity);
                int height = width * 9/16;
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);
                //设置展示标签的右间距
                layoutParams.setMargins(0,0,0,6);
                livePosterView.setLayoutParams(layoutParams);
            }
            mLiveData.getChildAt(i).setId(i);

            //标签的点击事件
            final int finalI = i;
            mLiveData.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle=new Bundle();
                    bundle.putString(LiveDetailsActivity.PARAMS_LIVE_ID,response.list.get(finalI).live_id);
                    bundle.putString(LiveDetailsActivity.PARAMS_LIVE_TITLE,response.list.get(finalI).title);
                    mBaseActivity.launchScreen(LiveDetailsActivity.class,bundle);
                }
            });
        }
    }


    /**
     * 设置精选视频数据源(6个)
     *
     */
    private void setVideoList(final VideoDTO response){

        mLlVedioData.setVisibility(View.VISIBLE);
        mLlVedioData.removeAllViews();
        for (int i = 0; i < response.list.size() ; i++){

            VedioPosterView vedioPosterView = new VedioPosterView(mBaseActivity);

            mLlVedioData.addView(vedioPosterView);

            if (response.list.get(i).title != null) {
                vedioPosterView.setTitleText(response.list.get(i).title);
            }

            if (mLlVedioData.getChildAt(i) == null || mLlVedioData.getChildAt(i).getTag() == null || mLlVedioData.getChildAt(i).getTag() != response.list.get(i).poster) {
                vedioPosterView.setPosterBackGround(response.list.get(i).poster);
                mLlVedioData.getChildAt(i).setTag(response.list.get(i).poster);
            }else {
                vedioPosterView.setPosterBackGround("");
            }

            //设置展示标签的宽、高
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mLlVedioData.getChildAt(i).getLayoutParams();
            //设置展示标签的右间距
            layoutParams.setMargins(20,0,0,0);
            mLlVedioData.getChildAt(i).setLayoutParams(layoutParams);
            mLlVedioData.getChildAt(i).setId(i);

            //标签的点击事件
            final int finalI = i;
            mLlVedioData.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle=new Bundle();
                    bundle.putSerializable(Constants.VIDEO_ENTITY,response.list.get(finalI));
                    IntentLaunch.launch(mBaseActivity, VideoDetailActivity.class,bundle);
                }
            });
        }

    }


    /**
     * 获取系统课*/
    private void getCourseSystem(){
        String business_id=String.valueOf(ICourse.ICourseType.COURSE_TYPE_SYSTEM);
        OKHttpCourseImpl.getInstance().getCourseSystem(business_id,String.valueOf(mLimit), "0",
                new RequestCallback<CourseSystemDTO>() {

                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        mPtrFrame.refreshComplete();
                    }

                    @Override
                    public void onSuccess(final CourseSystemDTO response) {
                        mPtrFrame.refreshComplete();
                        if(null==response || response.list.size()==0) return;
                        courses.clear();
                        courses.add(mImgOne);
                        courses.add(mImgTwo);
                        courses.add(mImgThree);
                        courses.add(mImgFour);
                        for (int i = 0; i < response.list.size() ; i++){
                            if (courses.get(i).getTag() == null || courses.get(i).getTag() != response.list.get(i).poster) {
                                ImageLoader.getInstance().displayImage(response.list.get(i).poster, courses.get(i),
                                        ImageLoaderOptions.optionRounded(R.drawable.img_default_course_suject,20));
                                courses.get(i).setTag(response.list.get(i).poster);
                            }else {
                                ImageLoader.getInstance().displayImage("", courses.get(i),
                                        ImageLoaderOptions.optionRounded(R.drawable.img_default_course_suject,20));
                            }
                            //设置展示标签的宽、高
                            int width = (int) (courses.get(i).getWidth());
                            int height = (int) (width * 160.0/332.7);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);
                            courses.get(i).setLayoutParams(layoutParams);

                            final int finalI = i;
                            courses.get(i).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CourseSystem courseSystem=response.list.get(finalI);
                                    Bundle bundle=new Bundle();
                                    bundle.putString(CourseSystemDetailsActivity.PARAMS_SYSTEM_COURSE_ID,courseSystem.id);
                                    bundle.putInt(CourseSystemDetailsActivity.PARAMS_SYSTEM_BUSINESS_ID,courseSystem.business_id);
                                    bundle.putString(CourseSystemDetailsActivity.PARAMS_SYSTEM_TITLE,courseSystem.title);
                                    mBaseActivity.launchScreen(CourseSystemDetailsActivity.class,bundle);
                                }
                            });
                        }
                    }
                });
    }


    PtrDefaultHandler2 ptrDefaultHandler2 = new PtrDefaultHandler2() {

        @Override
        public void onLoadMoreBegin(PtrFrameLayout frame) {
            if (!CommonUtils.getIntnetConnect(mBaseActivity)){
                setNoNet();
                return;
            }
            setDataReset();
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            if (!CommonUtils.getIntnetConnect(mBaseActivity)){
                setNoNet();
                return;
            }
            setDataReset();
        }

    };

    /**
     *
     * 获取课程*/
    private void getCourse(){

        String business_id=String.valueOf(ICourse.ICourseType.COURSE_TYPE_SUJECT);
        String pageIndex=String.valueOf(mPageIndex);
        OKHttpCourseImpl.getInstance().getCourseList(business_id, mLimit, pageIndex, new RequestCallback<CourseDTO>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                mPtrFrame.refreshComplete();
            }

            @Override
            public void onSuccess(CourseDTO response) {

                mPtrFrame.refreshComplete();
                if(null==response || response.list.size()==0){
                    mTvActivityTitle.setVisibility(View.GONE);
                    return;
                }else {
                    mTvActivityTitle.setVisibility(View.VISIBLE);
                }
                if(mPageIndex==0) mCourseSubjectApdater.cleanList();
                mPageIndex++;
                mCourseSubjectApdater.modifyList(response.list);
                if (response.list.size()>=10) {
                    if (mPtrFrame.getMode() != PtrFrameLayout.Mode.BOTH) {
                        mPtrFrame.setMode(PtrFrameLayout.Mode.BOTH);
                    }
                }
            }
        });
    }

    /**
     * 线下活动 列表点击事件
     */
    AdapterView.OnItemClickListener courseItemClickListener=new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Course course=mCourseSubjectApdater.getList().get(position-1);

            Bundle bundle=new Bundle();
            bundle.putString(CourseDetailsActivity.PARAMS_COURSE_ID,course.id);
            bundle.putInt(CourseDetailsActivity.PARAMS_BUSINESS_ID,course.business_id);
            bundle.putString(CourseDetailsActivity.PARAMS_COURSE_TITLE,course.title);
            mBaseActivity.launchScreen(CourseDetailsActivity.class,bundle);

        }
    };


    /**
     * 网络状态发送改变的监听
     * @param netConnection
     */
    @Override
    public void onNetChange(boolean netConnection) {
        if (netConnection){
            if (null == mCourseSubjectApdater.getList()|| mCourseSubjectApdater.getList().size() == 0) {
                setDataReset();
            }
        }
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_colleage_live_more:
                Bundle bundle1=new Bundle();
                mBaseActivity.launchScreen(LiveListActivity.class,bundle1);
                break;
            case R.id.rl_colleage_video_more:
                Bundle bundle2=new Bundle();
                mBaseActivity.launchScreen(VideoActivity.class,bundle2);
                break;
            case R.id.img_colleage_origanization:
                Bundle bundle = new Bundle();
                bundle.putString(ApplicationEnterActivity.TITLE, getResources().getString(R.string.title_orz_enter));
                bundle.putString(ApplicationEnterActivity.URL, WebViewUrl.H5_REALIZE_ORIGAN_DETAILS);
                bundle.putString(ApplicationEnterActivity.H5_URL, WebViewUrl.H5_SHARE_ORZ_DETAILS);
                mBaseActivity.launchScreen(ApplicationEnterActivity.class, bundle);
                break;
        }
    }


}
