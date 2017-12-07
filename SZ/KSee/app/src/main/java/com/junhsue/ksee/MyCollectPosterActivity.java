package com.junhsue.ksee;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.junhsue.ksee.adapter.MyCollectPosterAdapter;
import com.junhsue.ksee.adapter.MyPosterListAdapter;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.IBusinessType;
import com.junhsue.ksee.dto.PostDetailListDTO;
import com.junhsue.ksee.dto.SendPostResultDTO;
import com.junhsue.ksee.entity.PostDetailEntity;
import com.junhsue.ksee.net.api.OKHttpNewSocialCircle;
import com.junhsue.ksee.net.callback.DeleteItemCallback;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.error.NetResultCode;
import com.junhsue.ksee.utils.CommonUtils;
import com.junhsue.ksee.utils.ScreenWindowUtil;
import com.junhsue.ksee.utils.StartActivityUtils;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.ActionSheet;
import com.junhsue.ksee.view.CircleImageView;
import com.junhsue.ksee.view.CommonListView;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class MyCollectPosterActivity extends BaseActivity implements
        View.OnClickListener,DeleteItemCallback{

    private ActionBar mAbar;
    private PtrClassicFrameLayout mPtrFram;
    private CommonListView mLv;
    private CircleImageView mCircleNoData;
    private LinearLayout mLlNodata;
    private TextView mTvNoData;
    public Button btn_reloading;
    private View vHead;

    private MyCollectPosterAdapter<PostDetailEntity> myPosterListAdapter;
    private MyCollectPosterActivity mContext;

    //确认删除弹出框
    private ActionSheet deleteActionSheetDialog;
    private DeleteItemCallback deleteItemCallback;

    //当前页数0页开始
    private int pageIndex = 0;
    //当前页数的数据条目
    private int pagesize = 10;
    //数据总数
    private int totlaPage;
    //是否正在预加载的flag；
    private boolean isRefresh = false;
    //是否数据全部加载完毕
    private boolean isFinish = false;
    //删除按钮的展示与隐藏
    private boolean isDelShowManager = false;

    @Override
    protected void onReceiveArguments(Bundle bundle) {
    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_my_collect_poster_list;
    }

    @Override
    protected void onInitilizeView() {
        mContext = this;
        deleteItemCallback = this;
        initView();
    }

    private void initView() {
        mAbar = (ActionBar) findViewById(R.id.ab_my_collect_post);
        mAbar.setOnClickListener(mContext);
        mPtrFram = (PtrClassicFrameLayout) findViewById(R.id.ptrClassicFrameLayout_my_collect_post);
        mLv = (CommonListView) findViewById(R.id.lv_my_collect_post);
        mLlNodata = (LinearLayout) findViewById(R.id.ll_img_bitmap);
        vHead = View.inflate(mContext,R.layout.item_myanswer_head,null);
        vHead.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
        mLv.addHeaderView(vHead);
        mCircleNoData = (CircleImageView) mLv.findViewById(R.id.img_answer_nodata);
        mTvNoData = (TextView) mLv.findViewById(R.id.tv_answer_nodata);
        btn_reloading = (Button) mLv.findViewById(R.id.btn_answer_reloading);
        btn_reloading.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setDataReset();
            }
        });

        mPtrFram.setPtrHandler(mPtrDefaultHandler2);
        myPosterListAdapter = new MyCollectPosterAdapter<>(mContext,deleteItemCallback);

        if (!CommonUtils.getIntnetConnect(mContext)){
            setNoNet();
        }else {
            btn_reloading.setVisibility(View.GONE);
            mContext.alertLoadingProgress();
            getData();
        }

        mLv.setAdapter(myPosterListAdapter);

        mLv.setOnItemClickListener(itemClickListener);
        mLv.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + pagesize/2 + visibleItemCount >= totalItemCount){
                    if (isRefresh) return;
                    if (isFinish) return;
                    if (!CommonUtils.getIntnetConnect(mContext)){
                        setNoNet();
                        return;
                    }
                    btn_reloading.setVisibility(View.GONE);
                    if (pageIndex > totlaPage){
                        isFinish = true;
                        mPtrFram.refreshComplete();
                    }else {
                        getData();
                    }
                }
            }
        });
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position>=0){
                position -= 1;
            }else {
                return;
            }
            Intent intent = new Intent(mContext,PostDetailActivity.class);
            PostDetailEntity postDetailEntity = myPosterListAdapter.getList().get(position);
            Bundle bundle = new Bundle();
            bundle.putString(Constants.POST_DETAIL_ID, postDetailEntity.id);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    PtrDefaultHandler2 mPtrDefaultHandler2 = new PtrDefaultHandler2() {
        @Override
        public void onLoadMoreBegin(PtrFrameLayout frame) {
            if (!CommonUtils.getIntnetConnect(mContext)){
                setNoNet();
                return;
            }
            btn_reloading.setVisibility(View.GONE);
            if (pageIndex > totlaPage){
                ToastUtil.getInstance(mContext).setContent(getString(R.string.data_load_completed)).setShow();
                mPtrFram.refreshComplete();
                isFinish = true;
            }else {
                getData();
            }
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            setDataReset();
        }
    };


    /**
     * 获取我收藏的帖子数据
     * bisness_id :默认14为收藏帖子的业务id
     */
    public void getData() {
        isRefresh = true;
        final String mPageIndex = String.valueOf(pageIndex);
        String mPagesize = String.valueOf(pagesize);
        OKHttpNewSocialCircle.getInstance().getPostCollect("14",mPageIndex, mPagesize, new RequestCallback<PostDetailListDTO>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                isRefresh = false;
                mPtrFram.refreshComplete();
                mContext.dismissLoadingDialog();
                switch (errorCode){
                    case NetResultCode.SERVER_NO_DATA:
                        if (pageIndex == 0) {
                            setNoData(View.VISIBLE);
                            myPosterListAdapter.cleanList();
                            myPosterListAdapter.modifyList(null);
                        }else {
                            isFinish = true;
                            ToastUtil.getInstance(mContext).setContent(getString(R.string.data_load_completed)).setShow();
                        }
                        break;
                }
            }

            @Override
            public void onSuccess(PostDetailListDTO response) {
                isRefresh = false;
                mPtrFram.refreshComplete();
                mContext.dismissLoadingDialog();
                if (null == response || response.list.size() == 0){
                    if (pageIndex == 0){
                        setNoData(View.VISIBLE);
                    }
                    return;
                }
                setNoData(View.GONE);
                if (pageIndex == 0){
                    myPosterListAdapter.cleanList();
                }
                pageIndex++;
                myPosterListAdapter.modifyList(response.list);
                totlaPage = response.list.size()/pagesize;
                if (response.list.size() >= pagesize){
                    mPtrFram.setMode(PtrFrameLayout.Mode.BOTH);
                }
            }
        });
    }


    private void setNoData(int visibility) {
//        if (visibility == View.VISIBLE){
//            mLv.addHeaderView(vHead);
//        }else {
//            mLv.removeHeaderView(vHead);
//        }
//        mCircleNoData.setImageResource(R.drawable.wu_def_collection);
//        mTvNoData.setText("暂无新消息");
//        mCircleNoData.setVisibility(visibility);
//        mTvNoData.setVisibility(visibility);
        if (visibility == View.VISIBLE){
            mLlNodata.setVisibility(visibility);
            mLv.setVisibility(View.GONE);
        }else {
            mLlNodata.setVisibility(View.GONE);
            mLv.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 无网络的状态
     */
    private void setNoNet() {
        mCircleNoData.setImageResource(R.drawable.common_def_nonet);
        mTvNoData.setText("网络加载出状况了");
        mCircleNoData.setVisibility(View.VISIBLE);
        mTvNoData.setVisibility(View.VISIBLE);
        ToastUtil.getInstance(mContext).setContent(Constants.INTNET_NOT_CONNCATION).setDuration(Toast.LENGTH_SHORT).setShow();

        btn_reloading.setVisibility(View.VISIBLE);
        pageIndex = 0;
        myPosterListAdapter.cleanList();
        myPosterListAdapter.modifyList(null);
        mPtrFram.refreshComplete();
    }


    /**
     * 数据重新刷新
     */
    public void setDataReset(){
        if (!CommonUtils.getIntnetConnect(mContext)){
            setNoNet();
            return;
        }
        btn_reloading.setVisibility(View.GONE);
        pageIndex = 0;
        getData();
        isFinish = false;
    }

    @Override
    public void onDestroy() {
        pageIndex = 0;
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left_layout:
                finish();
                break;
            case R.id.tv_btn_right:
                isDelShowManager = !isDelShowManager;
                mAbar.setRightText("            "+(isDelShowManager ? "完成" : "编辑"));
                mAbar.setRightTextStyle(isDelShowManager ? R.style.text_f_30_c_red_fc613c : R.style.text_f_30_c_black_55626e);
                myPosterListAdapter.setDelBtnVisibility(isDelShowManager);
                break;
        }
    }

    /**
     * 删除帖子弹出框
     */
    private void deletePoster(final int position) {
        deleteActionSheetDialog = new ActionSheet(this);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.component_delete_dailog, null);
        deleteActionSheetDialog.setContentView(view);
        deleteActionSheetDialog.setGrivate(Gravity.CENTER);
        deleteActionSheetDialog.show();


        TextView tvTitle = (TextView) view.findViewById(R.id.tv_delete_title);
        TextView trueButton = (TextView) view.findViewById(R.id.tv_true);
        TextView cancleButton = (TextView) view.findViewById(R.id.tv_cancel);
        tvTitle.setText("确认取消收藏？");
        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unfavoritePoster(position);
                deleteActionSheetDialog.dismiss();
            }
        });
        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteActionSheetDialog.dismiss();
            }
        });

    }

    /**
     * 取消收藏帖子
     * @param position
     */
    private void unfavoritePoster(final int position) {
        String id = myPosterListAdapter.getList().get(position).id;
        OKHttpNewSocialCircle.getInstance().unfavoritePostCollect("14", id, new RequestCallback<SendPostResultDTO>() {
            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(SendPostResultDTO response) {
                ToastUtil.getInstance(mContext).setContent("帖子取消收藏成功").setShow();
                if (myPosterListAdapter.getList() != null && myPosterListAdapter.getList().size() > position) {
                    myPosterListAdapter.getList().remove(position);
                    myPosterListAdapter.notifyDataSetChanged();
                    if (myPosterListAdapter.getList().size() == 0){
                        setNoData(View.VISIBLE);
                    }
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (deleteActionSheetDialog != null) {
            deleteActionSheetDialog.dismiss();
        }
    }

    /**
     * 点击删除item
     * @param position
     */
    @Override
    public void deleteItem(int position) {
        deletePoster(position);
    }
}
