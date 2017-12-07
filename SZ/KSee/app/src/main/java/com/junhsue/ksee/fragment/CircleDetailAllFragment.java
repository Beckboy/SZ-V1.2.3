package com.junhsue.ksee.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.junhsue.ksee.BaseActivity;
import com.junhsue.ksee.CircleDetailActivity;
import com.junhsue.ksee.PostDetailActivity;
import com.junhsue.ksee.R;
import com.junhsue.ksee.adapter.BaseRecycleViewAdapter;
import com.junhsue.ksee.adapter.CircleDetailAllAdapter;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.PostDetailListDTO;
import com.junhsue.ksee.entity.MsgInfoEntity;
import com.junhsue.ksee.entity.PostDetailEntity;
import com.junhsue.ksee.entity.QuestionEntity;
import com.junhsue.ksee.fragment.dialog.CircleCommonDialog;
import com.junhsue.ksee.mvp.contract.CircleContract;
import com.junhsue.ksee.mvp.presenter.CirclePresenter;
import com.junhsue.ksee.net.api.OKHttpNewSocialCircle;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.error.NetResultCode;
import com.junhsue.ksee.utils.ToastUtil;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.List;

/**
 * 圈子详情 —— 帖子列表(全部)
 * Created by hunter_J on 17/4/18.
 */

public class CircleDetailAllFragment extends BaseFragment implements CircleContract.View {

    //贴子删除 @author longer 2017/11/17
    public static  final String BROAD_ACTION_POSTS_DELETE="com.junhsue.ksee.action_posts_delete";
    private static CircleDetailAllFragment myPostAllFragment;

    private SuperRecyclerView mLv;
    private LinearLayoutManager layoutManager;
    public CircleDetailAllAdapter myPostAllAdapter;
    private CircleDetailActivity mContext;
    private CirclePresenter presenter;

    private final static int TYPE_PULLREFRESH = 1;
    private final static int TYPE_UPLOADREFRESH = 2;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;

    //当前页数0页开始
    private int pageIndex = 0;
    //当前页数的数据条目
    private int pagesize = 20;
    //圈子id
    private static String circle_id;
    //圈子公告
    private static String circle_notice;
    //是否为精华
    private static String top = null;
    //是否退出
    private boolean actIsShow = true;

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.ACTION_REFRESH_POST)) {
                PostDetailEntity entity = (PostDetailEntity)intent.getExtras().getSerializable(Constants.POST_DETAIL_ID);
                List<PostDetailEntity> posterList = myPostAllAdapter.getDatas();
                for (int i = 0; i < posterList.size(); i++) {
                    if (entity.id.equals(posterList.get(i).id)) {
                        posterList.get(i).is_approval = entity.is_approval;
                        posterList.get(i).approvalcount = entity.approvalcount;
                        posterList.get(i).is_favorite = entity.is_favorite;
                        posterList.get(i).commentcount=entity.commentcount;
                    }
                }
                myPostAllAdapter.notifyDataSetChanged();
            }else if (action.equals(Constants.ACTION_ADD_MYPOST)||action.equals(Constants.ACTION_REFRESH_POST_LIST)){
//                PostDetailEntity entity = (PostDetailEntity) intent.getExtras().getSerializable(Constants.POST_DETAIL_ID);
//                List<PostDetailEntity> posterList = myPostAllAdapter.getDatas();
//                if (entity.circle_id.equals(circle_id)) {
//                    posterList.add(0,entity);
//                    myPostAllAdapter.setDatas(posterList);
//                    myPostAllAdapter.notifyDataSetChanged();
//                }
                setDataReset();
            }else if(BROAD_ACTION_POSTS_DELETE.equals(action)&& null!=intent.getExtras()){
                //帖子删除
                String postsId=intent.getExtras().getString(Constants.POST_DETAIL_ID);
                deletePosts(postsId);
                Trace.i("posts delete---");
            }
        }
    };


    /**
     * 贴子删除
     */
    private void deletePosts(String id){
       List<PostDetailEntity> postsList= myPostAllAdapter.getDatas();
        for(int i=0;i<postsList.size();i++){
            PostDetailEntity postDetailEntity=postsList.get(i);
            if(id.equals(postDetailEntity.id)){
                postsList.remove(i);
                break;
            }
        }
        myPostAllAdapter.notifyDataSetChanged();

    }

    public static CircleDetailAllFragment newInstance(String mCircle_id, String mTop, String mNotice) {
        circle_id = mCircle_id;
        circle_notice = mNotice;
        top = mTop;
        if (myPostAllFragment == null) {
            myPostAllFragment = new CircleDetailAllFragment();
        }
        return myPostAllFragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fm_my_post_all;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBroadcast();
    }

    @Override
    protected void onInitilizeView(View view) {

        initView(view);

        //实现自动下拉刷新功能
        mLv.getSwipeToRefresh().post(new Runnable() {
            @Override
            public void run() {
                mLv.setRefreshing(true); //执行下拉刷新的动画
                refreshListener.onRefresh(); //执行数据加载操作
            }
        });
    }

    private void initView(View view) {
        presenter = new CirclePresenter(this);
        mLv = (SuperRecyclerView) view.findViewById(R.id.recycleView);
        layoutManager = new LinearLayoutManager(mContext);
        mLv.setLayoutManager(layoutManager);
//    recyclerView.addItemDecoration(new DivItemDecoration(2, true));
        mLv.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setDataReset();
            }
        };
        mLv.setRefreshListener(refreshListener);

        mLv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy>15 && !mContext.isOnScroll){
                    mContext.isOnScroll = true; //正在滑动
                    Animation anim = AnimationUtils.loadAnimation(mContext,R.anim.photo_dialog_out_anim);
                    mContext.mImgPost.startAnimation(anim);
                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {}

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mContext.mImgPost.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }else if (dy <= 0){
                    mContext.mImgPost.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!actIsShow) return;
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mContext.isOnScroll = false;
                    mContext.mImgPost.setVisibility(View.VISIBLE);
                    //TODO
                    Glide.with(mContext).resumeRequests();
                } else {
                    Glide.with(mContext).pauseRequests();
                }
            }

        });

        myPostAllAdapter = new CircleDetailAllAdapter(mContext);
        myPostAllAdapter.setCirclePresenter(presenter);
        myPostAllAdapter.setNotity(circle_notice);
        mLv.setAdapter(myPostAllAdapter);
        myPostAllAdapter.setItemListener(itemClickListener);

    }

    BaseRecycleViewAdapter.RecyclerViewItemListener itemClickListener = new BaseRecycleViewAdapter.RecyclerViewItemListener() {
        @Override
        public void onItemClick(int position) {
            Intent intent = new Intent(mContext, PostDetailActivity.class);
            Bundle bundle = new Bundle();
            PostDetailEntity postDetailEntity = (PostDetailEntity) myPostAllAdapter.getDatas().get(position);
            bundle.putString(Constants.POST_DETAIL_ID, postDetailEntity.id);
            intent.putExtras(bundle);
            startActivity(intent);
        }

        @Override
        public boolean onItemLongClick(int position) {
            return false;
        }
    };

    /**
     * 数据重新刷新
     */
    public void setDataReset() {
        pageIndex = 0;
//    getData();
        presenter.loadData(TYPE_PULLREFRESH, pageIndex, pagesize, top, circle_id);
    }

    @Override
    public void onDestroy() {
        pageIndex = 0;
        if (presenter != null) {
            presenter.recycle();
        }
        mContext.unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        pageIndex = 0;
        actIsShow = false;
        super.onDestroyView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (CircleDetailActivity) activity;
    }

    /**
     * 显示关注成功弹窗
     */
    private void showDialog(String msg, int drawableId) {
        MsgInfoEntity entity = new MsgInfoEntity();
        entity.drawableId = drawableId;
        entity.msgInfo = msg;
        CircleCommonDialog circleCommonDialog = CircleCommonDialog.newInstance(entity);
        circleCommonDialog.show(getFragmentManager(), "");
    }

    @Override
    public void update2DeleteCircle(String circleId) {
    }

    @Override
    public void update2AddFavorite(int circlePosition) {
        PostDetailEntity postDetailEntity = (PostDetailEntity) myPostAllAdapter.getDatas().get(circlePosition);
        postDetailEntity.is_favorite = true;
        int size = Integer.parseInt(postDetailEntity.favoritecount);
        postDetailEntity.favoritecount = size + 1 + "";
        myPostAllAdapter.notifyDataSetChanged();
//            myPostAllAdapter.notifyItemRemoved(circlePosition);
        showDialog("收藏成功", R.drawable.icon_dialog_success);
    }

    @Override
    public void update2DeleteFavort(int circlePosition) {
        PostDetailEntity postDetailEntity = (PostDetailEntity) myPostAllAdapter.getDatas().get(circlePosition);
        postDetailEntity.is_favorite = false;
        int size = Integer.parseInt(postDetailEntity.favoritecount);
        postDetailEntity.favoritecount = size - 1 + "";
        myPostAllAdapter.notifyDataSetChanged();
//            myPostAllAdapter.notifyItemRemoved(circlePosition);
        showDialog("收藏取消", R.drawable.icon_dialog_success);
    }

    @Override
    public void update2AddApproval(int circlePosition) {
        PostDetailEntity postDetailEntity = (PostDetailEntity) myPostAllAdapter.getDatas().get(circlePosition);
        postDetailEntity.is_approval = true;
        int size = Integer.parseInt(postDetailEntity.approvalcount);
        postDetailEntity.approvalcount = size + 1 + "";
        myPostAllAdapter.notifyDataSetChanged();
//            myPostAllAdapter.notifyItemRemoved(circlePosition);
        showDialog("点赞成功", R.drawable.icon_dialog_success);
    }

    @Override
    public void update2DeleteApproval(int circlePosition) {
        PostDetailEntity postDetailEntity = (PostDetailEntity) myPostAllAdapter.getDatas().get(circlePosition);
        postDetailEntity.is_approval = false;
        int size = Integer.parseInt(postDetailEntity.approvalcount);
        postDetailEntity.approvalcount = size - 1 + "";
        myPostAllAdapter.notifyDataSetChanged();
//            myPostAllAdapter.notifyItemRemoved(circlePosition);
        showDialog("点赞取消", R.drawable.icon_dialog_success);
    }

    @Override
    public void upadte2AddComment(int circlePosition) {

    }

    @Override
    public void update2DeleteComment(int circlePosition) {

    }

    @Override
    public void update2loadData(int loadType, PostDetailListDTO datas) {
        if (loadType == TYPE_PULLREFRESH) {
            mLv.setRefreshing(false);
            myPostAllAdapter.setDatas(datas.list);
        } else if (loadType == TYPE_UPLOADREFRESH) {
            myPostAllAdapter.getDatas().addAll(datas.list);
        }
        myPostAllAdapter.notifyDataSetChanged();

        if (myPostAllAdapter.getDatas().size() > 0 && datas.list.size() % pagesize == 0) {
            mLv.setupMoreListener(new OnMoreListener() {
                @Override
                public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                    pageIndex++;
                    presenter.loadData(TYPE_UPLOADREFRESH, pageIndex, pagesize, top, circle_id);
                }
            }, pagesize);
        } else {
            ToastUtil.getInstance(mContext).setContent(getString(R.string.data_load_completed)).setShow();
            mLv.removeMoreListener();
            mLv.hideMoreProgress();
        }

    }

    @Override
    public void update2loadDataFail() {
        ToastUtil.getInstance(mContext).setContent(getString(R.string.data_load_completed)).setShow();
        mLv.setRefreshing(false);
        mLv.removeMoreListener();
        mLv.hideMoreProgress();
        pageIndex--;
    }

    /**
     * 注册广播
     */
    private void registerBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_REFRESH_POST);
        intentFilter.addAction(Constants.ACTION_ADD_MYPOST);
        intentFilter.addAction(Constants.ACTION_REFRESH_POST_LIST);
        intentFilter.addAction(BROAD_ACTION_POSTS_DELETE);
        mContext.registerReceiver(mBroadcastReceiver, intentFilter);
    }

}
