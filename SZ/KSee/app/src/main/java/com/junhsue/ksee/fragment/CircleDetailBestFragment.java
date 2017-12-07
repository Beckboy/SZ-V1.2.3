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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.junhsue.ksee.BaseActivity;
import com.junhsue.ksee.CircleDetailActivity;
import com.junhsue.ksee.PostDetailActivity;
import com.junhsue.ksee.R;
import com.junhsue.ksee.adapter.BaseRecycleViewAdapter;
import com.junhsue.ksee.adapter.CircleDetailAllAdapter;
import com.junhsue.ksee.adapter.CircleDetailBestAdapter;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.PostDetailListDTO;
import com.junhsue.ksee.entity.MsgInfoEntity;
import com.junhsue.ksee.entity.PostDetailEntity;
import com.junhsue.ksee.fragment.dialog.CircleCommonDialog;
import com.junhsue.ksee.mvp.contract.CircleContract;
import com.junhsue.ksee.mvp.presenter.CirclePresenter;
import com.junhsue.ksee.net.api.OKHttpNewSocialCircle;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.error.NetResultCode;
import com.junhsue.ksee.utils.CommonUtils;
import com.junhsue.ksee.utils.ToastUtil;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.List;


/**
 * 圈子详情 —— 帖子列表(全部)
 * Created by hunter_J on 17/4/18.
 */

public class CircleDetailBestFragment extends BaseFragment implements CircleContract.View {

    private static CircleDetailBestFragment myPostBestFragment;

    private SuperRecyclerView mLv;
    private LinearLayoutManager layoutManager;
    public CircleDetailBestAdapter myPostBestAdapter;
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
    //是否为精华
    private static String top = null;

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.ACTION_REFRESH_POST)) {

                PostDetailEntity entity = (PostDetailEntity) intent.getExtras().getSerializable(Constants.POST_DETAIL_ID);
                List<PostDetailEntity> posterList = myPostBestAdapter.getDatas();
                for (int i = 0; i < posterList.size(); i++) {

                    if (entity.id.equals(posterList.get(i).id)) {
                        posterList.get(i).is_approval = entity.is_approval;
                        posterList.get(i).approvalcount = entity.approvalcount;
                        posterList.get(i).is_favorite = entity.is_favorite;
                        posterList.get(i).commentcount = entity.commentcount;
                    }
                }
                myPostBestAdapter.notifyDataSetChanged();

            } else if (action.equals(Constants.ACTION_REFRESH_POST_LIST)) {
                setDataReset();
                myPostBestAdapter.notifyDataSetChanged();
            }
        }
    };


    public static CircleDetailBestFragment newInstance(String mCircle_id, String mTop) {
        circle_id = mCircle_id;
        top = mTop;
        if (myPostBestFragment == null) {
            myPostBestFragment = new CircleDetailBestFragment();
        }
        return myPostBestFragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fm_my_post_best;
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
                if (dy > 15 && !mContext.isOnScroll) {
                    mContext.isOnScroll = true; //正在滑动
                    Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.photo_dialog_out_anim);
                    mContext.mImgPost.startAnimation(anim);
                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mContext.mImgPost.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                } else if (dy <= 0) {
                    mContext.mImgPost.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mContext.isOnScroll = false;
                    mContext.mImgPost.setVisibility(View.VISIBLE);
                    Glide.with(mContext).resumeRequests();
                } else {
                    Glide.with(mContext).pauseRequests();
                }
            }

        });

        myPostBestAdapter = new CircleDetailBestAdapter(mContext);
        myPostBestAdapter.setCirclePresenter(presenter);
        mLv.setAdapter(myPostBestAdapter);
        myPostBestAdapter.setItemListener(itemClickListener);

    }


    BaseRecycleViewAdapter.RecyclerViewItemListener itemClickListener = new BaseRecycleViewAdapter.RecyclerViewItemListener() {
        @Override
        public void onItemClick(int position) {
            Intent intent = new Intent(mContext, PostDetailActivity.class);
            Bundle bundle = new Bundle();
            PostDetailEntity postDetailEntity = (PostDetailEntity) myPostBestAdapter.getDatas().get(position);
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
        PostDetailEntity postDetailEntity = (PostDetailEntity) myPostBestAdapter.getDatas().get(circlePosition);
        postDetailEntity.is_favorite = true;
        int size = Integer.parseInt(postDetailEntity.favoritecount);
        postDetailEntity.favoritecount = size + 1 + "";
        myPostBestAdapter.notifyDataSetChanged();
//   myPostAllAdapter.notifyItemRemoved(circlePosition);
        showDialog("收藏成功", R.drawable.icon_dialog_success);
    }

    @Override
    public void update2DeleteFavort(int circlePosition) {
        PostDetailEntity postDetailEntity = (PostDetailEntity) myPostBestAdapter.getDatas().get(circlePosition);
        postDetailEntity.is_favorite = false;
        int size = Integer.parseInt(postDetailEntity.favoritecount);
        postDetailEntity.favoritecount = size - 1 + "";
        myPostBestAdapter.notifyDataSetChanged();
//   myPostAllAdapter.notifyItemRemoved(circlePosition);
        showDialog("收藏取消", R.drawable.icon_dialog_success);
    }

    @Override
    public void update2AddApproval(int circlePosition) {
        PostDetailEntity postDetailEntity = (PostDetailEntity) myPostBestAdapter.getDatas().get(circlePosition);
        postDetailEntity.is_approval = true;
        int size = Integer.parseInt(postDetailEntity.approvalcount);
        postDetailEntity.approvalcount = size + 1 + "";
        myPostBestAdapter.notifyDataSetChanged();
//   myPostAllAdapter.notifyItemRemoved(circlePosition);
        showDialog("点赞成功", R.drawable.icon_dialog_success);
    }

    @Override
    public void update2DeleteApproval(int circlePosition) {
        PostDetailEntity postDetailEntity = (PostDetailEntity) myPostBestAdapter.getDatas().get(circlePosition);
        postDetailEntity.is_approval = false;
        int size = Integer.parseInt(postDetailEntity.approvalcount);
        postDetailEntity.approvalcount = size - 1 + "";
        myPostBestAdapter.notifyDataSetChanged();
//   myPostAllAdapter.notifyItemRemoved(circlePosition);
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
            myPostBestAdapter.setDatas(datas.list);
        } else if (loadType == TYPE_UPLOADREFRESH) {
            myPostBestAdapter.getDatas().addAll(datas.list);
        }
        myPostBestAdapter.notifyDataSetChanged();

        if (myPostBestAdapter.getDatas().size() > 0 && myPostBestAdapter.getDatas().size() % pagesize == 0) {
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
        mLv.removeMoreListener();
        mLv.hideMoreProgress();
        mLv.setRefreshing(false);
        pageIndex--;
    }

    /**
     * 注册广播
     */
    private void registerBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_REFRESH_POST);
        intentFilter.addAction(Constants.ACTION_REFRESH_POST_LIST);
        mContext.registerReceiver(mBroadcastReceiver, intentFilter);
    }
}
