package com.junhsue.ksee.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.junhsue.ksee.CircleDetailActivity;
import com.junhsue.ksee.R;
import com.junhsue.ksee.adapter.CircleAdapter;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.CircleListDTO;
import com.junhsue.ksee.entity.CircleEntity;
import com.junhsue.ksee.entity.CommonResultEntity;
import com.junhsue.ksee.entity.MsgInfoEntity;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.fragment.dialog.CircleCommonDialog;
import com.junhsue.ksee.net.api.OkHttpCircleImpI;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.error.NetResultCode;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.CommonListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 我的圈子
 * Created by longer on 17/10/24.
 */


public class CommunityMyCircleFragment extends BaseFragment implements CircleAdapter.ICircleListener , View.OnClickListener{

    //更新我的圈子
    public static  final String BROAD_ACTION_UPDATE_MY_CIRCLE="com.junhsue.ksee.action_udpate_my_circle";
    //
    public static  final String PARAMS_CIRCLE="params_circle";
    //圈子是否收藏
    public static  final String PARAMS_CIRCLE_IS_FAVOURITE="params_is_favourite";
    //
    private CommonListView mCommonListView;
    //
    private CircleAdapter<CircleEntity> mCircleAdapter;
    //我的圈子列表
    private ArrayList<CircleEntity> mMyCircles = new ArrayList<>();
    //
    private Context mContext;
    //
    private PtrClassicFrameLayout  mPtrClassicFrameLayout;
    //我的圈子
    private LinearLayout mLLMyCircle;
    //我的圈子最小展示个数
    private int MyCircleMinSize = 4;
    //
    private RelativeLayout mRlCircleEmpty;
    //获取
    private int page=0;
    private int pageSize = 5;
    //换一批是否正在加载
    private boolean isLoadBatchChange = false;

    public static CommunityMyCircleFragment newInstance() {
        CommunityMyCircleFragment myCircleFragment = new CommunityMyCircleFragment();
        return myCircleFragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_my_circle;
    }

    @Override
    protected void onInitilizeView(View view) {
        mContext=getContext();
        mCommonListView = (CommonListView) view.findViewById(R.id.lv_my_circle);
        mPtrClassicFrameLayout=(PtrClassicFrameLayout)view.findViewById(R.id.ptr_my_circle);
        mLLMyCircle=(LinearLayout)view.findViewById(R.id.ll_my_circle);
        mRlCircleEmpty=(RelativeLayout)view.findViewById(R.id.rl_circle_empty);
        view.findViewById(R.id.btn_batch_change).setOnClickListener(this);
        //
        mCircleAdapter = new CircleAdapter<>(mContext);
        mCommonListView.setAdapter(mCircleAdapter);
        //
        mPtrClassicFrameLayout.setPtrHandler(ptrDefaultHandler2);
        mPtrClassicFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH);
        //
        mCircleAdapter.setICircleListener(this);
        mCommonListView.setOnItemClickListener(onItemClickListener);
        getMyCircle();
        page = 0;
        getCircleRecommendList();
    }

    public void setReFresh(){
        mPtrClassicFrameLayout.autoRefresh();
    }

    AdapterView.OnItemClickListener onItemClickListener=new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            CircleEntity circleEntity=mCircleAdapter.getList().get(position);
            Intent intent=new Intent(mContext,CircleDetailActivity.class);
            intent.putExtra(CircleDetailActivity.PARAMS_CIRCLE_ID,circleEntity.id);
            intent.putExtra(CircleDetailActivity.PARAMS_CIRLCE_TITLE,circleEntity.name);
            intent.putExtra(CircleDetailActivity.PARAMS_CIRLCE_NOTICE,circleEntity.notice);
            startActivity(intent);
        }
    };



    PtrDefaultHandler2 ptrDefaultHandler2=new PtrDefaultHandler2() {

        @Override
        public void onLoadMoreBegin(PtrFrameLayout frame) {

            mPtrClassicFrameLayout.refreshComplete();
//            getCircleRecommendList();
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            page=0;
            getMyCircle();
            getCircleRecommendList();

        }
    };



    /**
     * 获取推荐圈子
     */
    private void getCircleRecommendList() {

        isLoadBatchChange = true;
        String pageLocal = String.valueOf(page);
        final String pageSizeL = String.valueOf(pageSize);

        OkHttpCircleImpI.getInstance().circleRecommend(pageLocal, pageSizeL,
                new RequestCallback<CircleListDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                switch (errorCode){
                    case NetResultCode.SERVER_NO_DATA:
                        if (page != 0) {
                            page = 0;
                            getCircleRecommendList();
                        }
                        break;
                    default:
                        isLoadBatchChange = false;
                        mPtrClassicFrameLayout.refreshComplete();
                        ToastUtil.getInstance(mContext).setContent("数据加载失败").setShow();
                        break;
                }

            }

            @Override
            public void onSuccess(CircleListDTO response) {
                isLoadBatchChange = false;
                mPtrClassicFrameLayout.refreshComplete();
                if (null == response || response.list.size() == 0){ //无更多推荐重新请求
                    page = 0;
                    getCircleRecommendList();
                    return;
                }else if (response.list.size() < pageSize){
                    page = -1;
                }

                mCircleAdapter.cleanList();
                mCircleAdapter.modifyList(response.list);
                page++;
            }
        });

    }


    /**
     * 根据关注，和取消关注选项，推荐圈子选项重新设置我的圈子
     *
     * @param circleEntity  圈子
     * @param isAdd 是否添加到我的圈子 true  添加,false 取消添加
     */
    private void updateCircle(CircleEntity  circleEntity,boolean isAdd) {

        //if (null == mMyCircles || mMyCircles.size() == 0) return;
        ArrayList<CircleEntity> list=new ArrayList<CircleEntity>();
        list.addAll(mMyCircles);
        if (isAdd) {
            list.add(0,circleEntity);
            mMyCircles.add(0,circleEntity);
        }else{
            for(int i=0;i<list.size();i++) {
                String circleId = list.get(i).id;
                if (circleEntity.id.equals(circleId)
                        && isAdd == false) {
                    list.remove(i);
                    mMyCircles.remove(i);
                }
            }
        }
        modifyMyCircle(list);
        //
        if(isAdd==true){
            removeReCommandCicle(circleEntity.id);
        }
        //modifyMyCircle();
    }


    /**
     * 移除推荐圈子
     * @param circleId 圈子id
     */
    private void removeReCommandCicle(String  circleId){
        List<CircleEntity> listS=mCircleAdapter.getList();
        //
        for(int i=0;i<listS.size();i++){
            CircleEntity circleEntity=listS.get(i);
            if(circleId.equals(circleEntity.id)){
                listS.remove(i);
                break;
            }
        }
        mCircleAdapter.notifyDataSetChanged();
    }

    /**
     * 更新我的圈子列表
     * @param
     */
    private  void modifyMyCircle(ArrayList<CircleEntity> circleEntities){
        if (mLLMyCircle.getChildCount() > 0) {
            mLLMyCircle.removeAllViews();
        }
        if(null==circleEntities || circleEntities.size()==0){
            mRlCircleEmpty.setVisibility(View.VISIBLE);
            mLLMyCircle.setVisibility(View.GONE);
            return;
        }else{
            mRlCircleEmpty.setVisibility(View.GONE);
            mLLMyCircle.setVisibility(View.VISIBLE);
        }
        for (int i = 0;i<circleEntities.size();i++){
            addCircleItem(circleEntities.get(i));
            if (i>(MyCircleMinSize-1)){
                mLLMyCircle.getChildAt(i).setVisibility(View.GONE);
            }
        }
        if (circleEntities.size()>MyCircleMinSize){
            addCircleButtonItem();
        }
    }


    /**
     *
     * 获取关注的圈子列表
     */
    private void getMyCircle(){

        OkHttpCircleImpI.getInstance().myFavouriteCircle(new RequestCallback<CircleListDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                switch (errorCode){
                    case NetResultCode.SERVER_NO_DATA:
                        mRlCircleEmpty.setVisibility(View.VISIBLE);
                        mLLMyCircle.setVisibility(View.GONE);
                    default:
                        break;
                }
            }

            @Override
            public void onSuccess(CircleListDTO response) {
                /** 遍历排除已被下架的关注圈子(返回值为null) **/
                if (response != null && response.list.size()>0){
                    for (int i=response.list.size()-1;i>=0;i--){
                        if (null == response.list.get(i)){
                            response.list.remove(i);
                        }
                    }
                }
                if(null==response || response.list.size()==0) {
                    mRlCircleEmpty.setVisibility(View.VISIBLE);
                    mLLMyCircle.setVisibility(View.GONE);
                    return;
                }else{
                    mRlCircleEmpty.setVisibility(View.GONE);
                    mLLMyCircle.setVisibility(View.VISIBLE);
                }
                mMyCircles=response.list;
                modifyMyCircle(response.list);
            }
        });
    }



    /** add我关注的圈子 **/
    private void addCircleItem(final CircleEntity circleEntity){
        if (circleEntity == null) return;
        final String cirlceId = circleEntity.id;

        final String circleTitle = circleEntity.name;
        View viewCircle=LayoutInflater.from(mContext).inflate(R.layout.item_my_circle,null);
        ImageView imgCicle=(ImageView) viewCircle.findViewById(R.id.img_circle);
        TextView  txtTitle=(TextView)viewCircle.findViewById(R.id.txt_circle_title);
        TextView  txtDesc=(TextView)viewCircle.findViewById(R.id.txt_circle_desc);

        //
        ImageLoader.getInstance().displayImage(circleEntity.poster,imgCicle
                , ImageLoaderOptions.option(R.drawable.img_default_course_system));
        //
        txtTitle.setText(circleEntity.name);
        txtDesc.setText(circleEntity.description);
        mLLMyCircle.addView(viewCircle);

        viewCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,CircleDetailActivity.class);
                intent.putExtra(CircleDetailActivity.PARAMS_CIRCLE_ID,cirlceId);
                intent.putExtra(CircleDetailActivity.PARAMS_CIRLCE_TITLE,circleTitle);
                intent.putExtra(CircleDetailActivity.PARAMS_CIRLCE_NOTICE,circleEntity.notice);
                startActivity(intent);
            }
        });
    }

    /** add我关注的圈子底部收缩按钮 **/
    private void addCircleButtonItem(){
        View viewCircle=LayoutInflater.from(mContext).inflate(R.layout.item_my_circle_buttom,null);
        final CheckBox cbButton=(CheckBox) viewCircle.findViewById(R.id.cb_mycircle_open);
        mLLMyCircle.addView(viewCircle);
        viewCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbButton.setChecked(!cbButton.isChecked());
            }
        });
        cbButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){ //展开
                    for (int i = 0;i<mLLMyCircle.getChildCount()-1;i++){
                        mLLMyCircle.getChildAt(i).setVisibility(View.VISIBLE);
                    }
                }else { //收缩
                    for (int i = MyCircleMinSize;i<mLLMyCircle.getChildCount()-1;i++){
                        mLLMyCircle.getChildAt(i).setVisibility(View.GONE);
                    }
                }
            }
        });
    }


    /**
     * 关注圈子
     * @param circleId 圈子id
     */
    private void favouriteCircle(String circleId) {

        OkHttpCircleImpI.getInstance().circleFavourite(circleId, new RequestCallback<CommonResultEntity>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                Trace.i("circle favourite fail!");
            }

            @Override
            public void onSuccess(CommonResultEntity response) {
                Trace.i("circle favourite successful!");
            }
        });
    }

    private void unFavouriteCircle(String circleId) {

        OkHttpCircleImpI.getInstance().circleUnFavourite(circleId, new RequestCallback<CommonResultEntity>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                Trace.i("circle  unFavourite fail!");
            }

            @Override
            public void onSuccess(CommonResultEntity response) {
                Trace.i("circle  unFavourite successful!");

            }
        });
    }


    /**
     * 更新收藏状态
     *
     * @param postion selected  index for the item adapter
     * @return 返回圈子id
     */
    private String updateFavouriteStatus(int postion) {

        List<CircleEntity> list = mCircleAdapter.getList();

        CircleEntity circleEntity = list.get(postion);
        if (circleEntity.is_concern) {
            circleEntity.is_concern = false;
        } else {
            circleEntity.is_concern = true;
        }
        mCircleAdapter.notifyDataSetChanged();

        return circleEntity.id;
    }


    @Override
    public void addFavourite(int position) {
        String circleId = updateFavouriteStatus(position);
        //
        CircleEntity circleEntity = mCircleAdapter.getList().get(position);
        if (circleEntity.is_concern) {
            favouriteCircle(circleId);
        } else {
            unFavouriteCircle(circleId);
        }
        /** 关注成功的弹窗 **/
        showDialog("关注成功",R.drawable.icon_dialog_success);
        /** 更新推荐圈子 **/
        updateCircle(circleEntity,circleEntity.is_concern);
        /** 更新圈子列表 **/
        Intent intent=new Intent(CommunityCircleFragment.BROAD_ACTION_UPDATE_CIRCLE);
        intent.putExtra(CommunityCircleFragment.PARAMS_CIRCLE,circleEntity);
        intent.putExtra(CommunityCircleFragment.PARAMS_CIRCLE_IS_FAVOURITE,circleEntity.is_concern);
        mContext.sendBroadcast(intent);

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

     BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
         @Override
         public void onReceive(Context context, Intent intent) {
             String action=intent.getAction();
             if(action.equals(BROAD_ACTION_UPDATE_MY_CIRCLE)
                     && null!=intent.getExtras()){
                 CircleEntity circleEntity=(CircleEntity)intent.getExtras().getSerializable(PARAMS_CIRCLE);
                 boolean  isFAVOURITE=intent.getExtras().getBoolean(PARAMS_CIRCLE_IS_FAVOURITE);
                 updateCircle(circleEntity,isFAVOURITE);
             }
         }
     };

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(BROAD_ACTION_UPDATE_MY_CIRCLE);
        mContext.registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(broadcastReceiver);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_batch_change: // 推荐圈子换一批
                if (!isLoadBatchChange) { //是否换一批正在加载
                    getCircleRecommendList();
                }
                break;
        }
    }
}
