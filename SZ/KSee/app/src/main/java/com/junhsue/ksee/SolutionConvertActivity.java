package com.junhsue.ksee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.junhsue.ksee.adapter.SolutionCouponAdapter;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.entity.BaseEntity;
import com.junhsue.ksee.entity.CommonResultEntity;
import com.junhsue.ksee.entity.SolutionCouponDTO;
import com.junhsue.ksee.entity.SolutionCouponEntity;
import com.junhsue.ksee.entity.SolutionDetails;
import com.junhsue.ksee.entity.UserInfo;
import com.junhsue.ksee.fragment.dialog.SolutionSendSuccessDialogFragment;
import com.junhsue.ksee.net.api.OKHttpHomeImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.utils.SharedPreferencesUtils;
import com.junhsue.ksee.utils.StringUtils;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.CommonListView;

import java.util.List;

/**
 *
 * 方案包兑换
 * Created by longer on 17/9/26.
 */

public class SolutionConvertActivity extends BaseActivity  implements View.OnClickListener{



    private EditText mTxtEmail;
    //
    private CommonListView mCommonListView;
    //
    private SolutionCouponAdapter<SolutionCouponEntity> mSolutionCouponAdapter;
    //
    private Context mContext=this;
    //
    private SolutionCouponEntity mSolutionCouponEntity;
    //方案包id
    private String mSolutionIds;
    //方案包链接地址
    private String mSolutionLink;
    //方案包标题
    private String mSolutionTitle;

    @Override
    protected void onReceiveArguments(Bundle bundle) {
        mSolutionIds=bundle.getString(SolutionDetailsActivity.PARMAS_SOLUTION_ID,"");
        mSolutionLink=bundle.getString(SolutionDetailsActivity.PARAMS_SOLUTION_LINK,"");
        mSolutionTitle=bundle.getString(SolutionDetailsActivity.PARAMS_SOLUTION_TITLE,"");
    }


    @Override
    protected int setLayoutId() {
        return R.layout.act_solution_convert;
    }

    @Override
    protected void onInitilizeView() {
        //
        mCommonListView=(CommonListView)findViewById(R.id.list_view_solution);
        mTxtEmail=(EditText)findViewById(R.id.txt_send_email);
        TextView txtEmptyView=(TextView)findViewById(R.id.txt_empty);
        //
        findViewById(R.id.btn_submit).setOnClickListener(this);
        findViewById(R.id.action_bar_solution).setOnClickListener(this);
        //
        mCommonListView.setOnItemClickListener(onItemClickListener);
        //
        mSolutionCouponAdapter=new SolutionCouponAdapter<SolutionCouponEntity>(getApplicationContext());
        mCommonListView.setAdapter(mSolutionCouponAdapter);
        mCommonListView.setEmptyView(txtEmptyView);

        //
        String emailLocal=SharedPreferencesUtils.getInstance(mContext).getString(Constants.SF_KEY_EMAIL,"");
        mTxtEmail.setText(emailLocal);
        //
        getSolutionCoupon();

    }



    AdapterView.OnItemClickListener onItemClickListener=new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            mSolutionCouponEntity=mSolutionCouponAdapter.getList().get(position);
            setCouponStatus(position);
        }
    };



    private void setCouponStatus(int position){

        setCouponStatusDefault();
        setCouponStatusSeletecd(position);
        mSolutionCouponAdapter.notifyDataSetChanged();
    }

    /**
     * 设置抵扣券为默认值
     */
    private void  setCouponStatusDefault() {
        List<SolutionCouponEntity> couponS = mSolutionCouponAdapter.getList();

        for(int i=0;i<couponS.size();i++){
            SolutionCouponEntity solutionCouponEntity=couponS.get(i);
            solutionCouponEntity.isSelected=false;
        }
    }

    /**
     * 优惠券是否有选中
     * @return
     */
    private boolean isCouponSeletecd(){
        List<SolutionCouponEntity> couponS = mSolutionCouponAdapter.getList();
        for(int i=0;i<couponS.size();i++){
            SolutionCouponEntity solutionCouponEntity=couponS.get(i);
            if(solutionCouponEntity.isSelected){
                return true;
            }
        }
        return  false;
    }
    /**
     *
     * @param position
     */
    private void setCouponStatusSeletecd(int position){
        List<SolutionCouponEntity> couponS = mSolutionCouponAdapter.getList();
        //
        for(int i=0;i<couponS.size();i++){
            SolutionCouponEntity solutionCouponEntity=couponS.get(i);
            if(i==position && solutionCouponEntity.status==2 && solutionCouponEntity.is_overdue==false){
                solutionCouponEntity.isSelected=true;
            }
        }
    }

    /**
     * 获取抵扣券列表
     */
    private void getSolutionCoupon(){

        OKHttpHomeImpl.getInstance().getSolutionCouponList(new RequestCallback<SolutionCouponDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(SolutionCouponDTO response) {

                mSolutionCouponAdapter.cleanList();
                mSolutionCouponAdapter.modifyList(response.result);
            }
        });
    }


    private void solutionCouponConvert(){
        if(null==mSolutionCouponEntity) return;

        //优惠券id
        String couponId=mSolutionCouponEntity.id;
        final String emalis=mTxtEmail.getText().toString().trim();

        String titleL=mSolutionTitle;
        UserInfo userInfo= UserProfileService.getInstance(mContext).getCurrentLoginedUser();
        String nickName=userInfo.nickname;
        OKHttpHomeImpl.getInstance().solutionConvert(couponId, mSolutionIds, emalis, mSolutionLink, titleL, nickName,
                new RequestCallback<CommonResultEntity>() {
                    @Override
                    public void onError(int errorCode, String errorMsg) {
                        ToastUtil.showToast(mContext,mContext.getString(R.string.msg_coupon_fail));
                    }

                    @Override
                    public void onSuccess(CommonResultEntity response) {

                        //发送成功保存本地邮箱
                        SharedPreferencesUtils.getInstance(mContext).putString(Constants.SF_KEY_EMAIL,emalis);

                        SolutionSendSuccessDialogFragment  solutionSendSuccessDialogFragment=
                                SolutionSendSuccessDialogFragment.newInstance();
                        solutionSendSuccessDialogFragment.show(getSupportFragmentManager(),"");
                        //通知广播发送
                        Intent intent=new Intent();
                        intent.setAction(SolutionDetailsActivity.BROAD_ACTION_SOLUTION_NOFITY);
                        sendBroadcast(intent);
                        //
                        solutionSendSuccessDialogFragment.setIDialogListener(new SolutionSendSuccessDialogFragment.IDialogListener() {
                            @Override
                            public void onConfirm() {
                                finish();
                                setResult(0X1);
                            }
                        });

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.btn_submit:
                if(mSolutionCouponAdapter.getList().size()==0){
                    ToastUtil.showToast(getApplicationContext(),mContext.getString(R.string.msg_coupon_no_data));
                    return;
                }
                if(isCouponSeletecd()==false){
                    ToastUtil.showToast(getApplicationContext(),mContext.getString(R.string.msg_coupon_no_seleted));
                    return;
                }
                //验证邮箱
                String emalis=mTxtEmail.getText().toString().trim();
                if(StringUtils.isEmail(emalis)==false){
                    ToastUtil.showToast(mContext,mContext.getString(R.string.msg_email_invalid));
                return;
                }

                solutionCouponConvert();

                break;

            case R.id.ll_left_layout:
                finish();
                break;

        }
    }
}
