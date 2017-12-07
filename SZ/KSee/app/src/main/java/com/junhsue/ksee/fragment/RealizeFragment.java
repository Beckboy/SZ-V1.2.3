package com.junhsue.ksee.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.junhsue.ksee.ApplicationEnterActivity;
import com.junhsue.ksee.ArticleDetailActivity;
import com.junhsue.ksee.BaseActivity;
import com.junhsue.ksee.R;
import com.junhsue.ksee.RealizeTagsListActivity;
import com.junhsue.ksee.SearchActivity;
import com.junhsue.ksee.adapter.RealizeArticleAdapter;
import com.junhsue.ksee.common.IBusinessType;
import com.junhsue.ksee.common.IntentLaunch;
import com.junhsue.ksee.dto.RealizeArticleDTO;
import com.junhsue.ksee.dto.RealizeArticleTagsDTO;
import com.junhsue.ksee.entity.RealizeArticleEntity;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.net.api.OkHttpRealizeImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.url.WebViewUrl;

import com.junhsue.ksee.utils.ScreenWindowUtil;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.CommonListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 知
 * Created by longer on 17/3/16 in Junhsue.
 */

public class RealizeFragment extends BaseFragment implements View.OnClickListener {


    //文章列表
    private CommonListView mLVArticle;
    //前三条文章
    private CommonListView mLVArticleHead;
    //头部图片
    private LinearLayout mLinear;
    private RealizeArticleAdapter<RealizeArticleEntity> mArticleAdapter;
    //前三条文章
    private RealizeArticleAdapter<RealizeArticleEntity> mArticleAdapterHead;
    //作者入驻
    private ImageView mImgAuthor;
    //
    private ActionBar mAbar;
    private ScrollView mScrollView;
    private HorizontalScrollView mHorizontalScrollView;
    private PtrClassicFrameLayout mPtrClassicFrameLayout;
    private BaseActivity mContext;

    //文章当前页
    private int mPage=0;
    public static RealizeFragment newInstance() {
        RealizeFragment homeFragment = new RealizeFragment();
        return homeFragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (BaseActivity) activity;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_realize;
    }

    @Override
    protected void onInitilizeView(View view) {
        //view.findViewById(R.id.content_detail).setOnClickListener(this);
        mAbar = (ActionBar) view.findViewById(R.id.ab_realize_search_heard);
        mAbar.setOnClickListener(this);

        mLVArticle = (CommonListView) view.findViewById(R.id.list_view_article);
        mLVArticleHead = (CommonListView) view.findViewById(R.id.list_view_article_head);
        mLinear = (LinearLayout) view.findViewById(R.id.ll_realize_tag);
        mScrollView = (ScrollView) view.findViewById(R.id.scroll_v_realize);
        mHorizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.scroll_h_realize);
        mPtrClassicFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_plassic_frameLayout);

        setImageOrg(view);
        //
        mArticleAdapter = new RealizeArticleAdapter<RealizeArticleEntity>(getActivity());
        //
        mArticleAdapterHead = new RealizeArticleAdapter<RealizeArticleEntity>(getActivity());
        mPtrClassicFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
        mPtrClassicFrameLayout.setPtrHandler(ptrDefaultHandler2);
        mLVArticle.setAdapter(mArticleAdapter);
        mLVArticleHead.setAdapter(mArticleAdapterHead);
        //
        mPage = 0;
        getArticleList();
        getArticleHeadList();
        //
        view.findViewById(R.id.img_realize_author).setOnClickListener(this);
        //
        mLVArticleHead.setOnItemClickListener(articleHeadItemClickListener);
        mLVArticle.setOnItemClickListener(articleItemClickListener);

    }

    /**
     * 设置组织机构入驻
     */
    private void setImageOrg(View view) {
        mImgAuthor = (ImageView) view.findViewById(R.id.img_realize_author);
        mImgAuthor.setOnClickListener(this);

        //设置展示标签的宽、高
        int width = ScreenWindowUtil.getScreenWidth(mContext);
        int height = (int) (width * 180.0/750.0);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);
        mImgAuthor.setLayoutParams(layoutParams);
    }

    /**
     * 设置头部轮播标签
     */
    private void setHeadTags(final RealizeArticleTagsDTO response) {

        mLinear.setVisibility(View.VISIBLE);
        mLinear.removeAllViews();
        for (int i = 0; i < response.result.size(); i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            mLinear.addView(imageView);
            //设置展示标签的宽、高
            int width = ScreenWindowUtil.getScreenWidth(mContext) / 4;
            int height = (int) (width * 100.0 / 180.0);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
            //设置展示标签的右间距
            layoutParams.setMargins(20, 0, 0, 0);
            mLinear.getChildAt(i).setLayoutParams(layoutParams);
            mLinear.getChildAt(i).setId(i);
            if (mLinear.getChildAt(i).getTag() == null || mLinear.getChildAt(i).getTag() != response.result.get(i).poster) {
                ImageLoader.getInstance().displayImage(response.result.get(i).poster, imageView,
                        ImageLoaderOptions.optionRounded(getTabBackground(Integer.parseInt(response.result.get(i).id)), 20));
                mLinear.getChildAt(i).setTag(response.result.get(i).poster);
            } else {
                ImageLoader.getInstance().displayImage("", imageView,
                        ImageLoaderOptions.optionRounded(Integer.parseInt(response.result.get(i).id), 20));
            }

            //标签的点击事件
            final int finalI = i;
            mLinear.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    String sb = "";
                    for (int j = 0; j < response.result.get(finalI).sub.size(); j++) {
                        String id = response.result.get(finalI).sub.get(j).id;
                        if (j != 0) {
                            sb = sb + ",";
                        }
                        sb = sb + id;
                    }

                    StatisticsUtil.getInstance(mContext).onCountActionDot("2.3");

                    bundle.putString(RealizeTagsListActivity.TAG_ID, sb);
                    bundle.putInt(RealizeTagsListActivity.TAG_INDEX, Integer.valueOf(response.result.get(finalI).id + ""));
                    IntentLaunch.launch(mContext, RealizeTagsListActivity.class, bundle);

                }
            });


            mLinear.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_MOVE:
                        case MotionEvent.ACTION_DOWN:
                            mPtrClassicFrameLayout.setMode(PtrFrameLayout.Mode.NONE);
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_OUTSIDE:
                            mPtrClassicFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    SystemClock.sleep(1000);
                                    mPtrClassicFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
                                }
                            }).start();
                            break;
                    }

                    return false;
                }
            });


        }

    }

    PtrDefaultHandler2 ptrDefaultHandler2 = new PtrDefaultHandler2() {

        @Override
        public void onLoadMoreBegin(PtrFrameLayout frame) {
            getArticleList();
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            mPage = 0;
            getArticleHeadList();
            getArticleList();
        }
    };

    private void getArticleHeadList() {
        OkHttpRealizeImpl.newInstance().getArticleHeadList(new RequestCallback<RealizeArticleTagsDTO>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
            }

            @Override
            public void onSuccess(RealizeArticleTagsDTO response) {
                setHeadTags(response);
            }
        });
    }

    private void getArticleList() {
        //
        String pageC = String.valueOf(mPage);
        //
        String pageSize = String.valueOf("10");
        OkHttpRealizeImpl.newInstance().getArticleList(pageC, pageSize, new RequestCallback<RealizeArticleDTO>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                mPtrClassicFrameLayout.refreshComplete();
            }

            @Override
            public void onSuccess(RealizeArticleDTO response) {
                mPtrClassicFrameLayout.refreshComplete();
                //
                //如果当前页为0页
                if (mPage == 0) {
                    fillActicleHead(response.list);
                    fillActicle(response.list);
                } else {
                    mArticleAdapter.modifyList(response.list);
                }
                mPage++;
                if (response.list.size() >= 10) {
                    mPtrClassicFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
                } else {
                    mPtrClassicFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH);

                }
            }
        });
    }


    /**
     * 取文章前三条数据
     *
     * @param list
     */
    private void fillActicleHead(List<RealizeArticleEntity> list) {
        ArrayList<RealizeArticleEntity> artilces = new ArrayList<RealizeArticleEntity>();
        int count = list.size() > 3 ? 3 : list.size();
        for (int i = 0; i < count; i++) {
            RealizeArticleEntity articleEntity = list.get(i);
            artilces.add(articleEntity);
        }
        mArticleAdapterHead.cleanList();
        mArticleAdapterHead.modifyList(artilces);

    }

    /**
     * 当页数==0时,取文章集合>3的数据
     *
     * @param
     */

    private void fillActicle(List<RealizeArticleEntity> list) {
        List<RealizeArticleEntity> artilces = new ArrayList<RealizeArticleEntity>();
        if (list.size() > 3) {
            for (int i = 3; i < list.size(); i++) {
                RealizeArticleEntity articleEntity = list.get(i);
                artilces.add(articleEntity);
            }
            mArticleAdapter.cleanList();
            mArticleAdapter.modifyList(artilces);
        }
    }

    /**
     * 添加前三篇热门文章监听
     */
    AdapterView.OnItemClickListener articleHeadItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            RealizeArticleEntity realizeArticleEntity = mArticleAdapterHead.getList().get(position);
            Bundle bundle = new Bundle();
            bundle.putString(ArticleDetailActivity.PARAMS_ARTICLE_ID, realizeArticleEntity.id);
            mContext.launchScreen(ArticleDetailActivity.class, bundle);

            StatisticsUtil.getInstance(mContext).onCountPage("1.2.3");

        }
    };


    AdapterView.OnItemClickListener articleItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            RealizeArticleEntity realizeArticleEntity = mArticleAdapter.getList().get(position);
            Bundle bundle = new Bundle();
            bundle.putString(ArticleDetailActivity.PARAMS_ARTICLE_ID, realizeArticleEntity.id);
            mContext.launchScreen(ArticleDetailActivity.class, bundle);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_realize_author: // 跳转到作者入驻
                Bundle bundle = new Bundle();
                bundle.putString(ApplicationEnterActivity.TITLE, getResources().getString(R.string.title_author_enter));
                bundle.putString(ApplicationEnterActivity.URL, WebViewUrl.H5_REALIZE_AUTHOR_DETAILS);
                bundle.putString(ApplicationEnterActivity.H5_URL, WebViewUrl.H5_REALIZE_AUTHOR_DETAILS);
                mContext.launchScreen(ApplicationEnterActivity.class, bundle);
                break;
            case R.id.btn_right_one:
                Intent intent = new Intent(mContext, SearchActivity.class);
                Bundle bundle_search = new Bundle();
                bundle_search.putInt(SearchActivity.SEARCH_BUSINESS_TYPE, IBusinessType.REALIZE_ARTICLE);
                intent.putExtras(bundle_search);
                mContext.startActivity(intent);
                break;
        }
    }

    /**
     * 设置Tab图片
     *
     * @param index 标题
     *              展示顺序：校长核心 行政人事 市场招生 教学教务 创业者 政策项目
     */
    public int getTabBackground(int index) {
        int background = R.drawable.shi_calendar_def;
        switch (index) {
            case 24: //校长核心
                //校长核心
                background = R.drawable.zhi_img_principal_small;
                break;
            case 23: //行政人事
                //行政人事
                background = R.drawable.zhi_img_administrative_small;
                break;
            case 25: //市场招生
                //市场招生
                background = R.drawable.zhi_img_market_small;
                break;
            case 26: //教学教务
                //教学教务
                background = R.drawable.zhi_img_teaching_small;
                break;
            case 31: //创业者
                //创业者
                background = R.drawable.zhi_img_entrepren_small;
                break;
            case 28: //项目政策
                //政策项目
                background = R.drawable.zhi_img_policies_small;
                break;
            default:
                background = R.drawable.shi_calendar_def;
                break;
        }
        return background;
    }


}
