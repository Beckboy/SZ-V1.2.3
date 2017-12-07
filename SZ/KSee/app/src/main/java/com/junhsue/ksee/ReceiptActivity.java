package com.junhsue.ksee;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.entity.ReceiptInfoEnity;
import com.junhsue.ksee.utils.InputUtil;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.InvoiceItemView;

/**
 * 发票UI
 * Created by Sugar on 17/7/14.
 */

public class ReceiptActivity extends BaseActivity implements View.OnClickListener{

    private ActionBar mAbar;
    private RelativeLayout mRlType,mRlContent;
    private TextView mTvType,mTvContent,mTvHint;
    private View mVContent,mVOrgaIdentify,mVOrgaAddress,mVOrgaPhone,mVOrgaBank,mVOrgaBankAccount;
    private InvoiceItemView mIIVOrgaName,mIIVOrgaIdentify,mIIVOrgaAddress,mIIVOrgaPhone,mIIVOrgaBank,mIIVOrgaBankAccount,mIIVRecePerson,mIIVRecePhone,mIIVReceAddress;

    private Context mContext;
    private PopupWindow mPopType;
    private int type = 1; //发票的默认类型
    private int fee = 1; //咨询费：1 ； 服务费：2 ;
    private TextView mBtnReceipt,mBtnNormal,mBtnSpecial,mBtnConsult,mBtnServer;
    private View mVReceip,mVNormal,mVConsult;

    //发票信息填写成功结果回传
    public final static int CODE_RESULT_RECEIPT_SUCCES = 0X60002;

    private ReceiptInfoEnity infoEntity;

    @Override
    protected void onReceiveArguments(Bundle bundle) {
        infoEntity = (ReceiptInfoEnity) bundle.getSerializable("receipt");
    }

    @Override
    protected int setLayoutId() {
        return R.layout.act_receipt;
    }

    @Override
    protected void onInitilizeView() {
        
        initView();

        initListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setViewData();
    }

    /**
     * 设置参数
     */
    private void setViewData() {
        if (null == infoEntity) return;
        Trace.i("receiptInfoEnity:\n"+infoEntity.receipt_type+"\n:"+infoEntity.fee_type+"\n:"+infoEntity.organization_name+"\n:"+infoEntity.organization_identify+"\n:"+infoEntity.organization_bank_name+"\n:"+infoEntity.organization_address+"\n:"+infoEntity.organization_phone+"\n:"+infoEntity.organization_bank_account+"\n:"+infoEntity.receiver_person_name+"\n:"+infoEntity.receiver_person_phone+"\n:"+infoEntity.receiver_person_address);

        mIIVOrgaName.setContent(infoEntity.organization_name);
        mIIVOrgaIdentify.setContent(infoEntity.organization_identify);
        mIIVOrgaAddress.setContent(infoEntity.organization_address);
        mIIVOrgaPhone.setContent(infoEntity.organization_phone);
        mIIVOrgaBank.setContent(infoEntity.organization_bank_name);
        mIIVOrgaBankAccount.setContent(infoEntity.organization_bank_account);
        mIIVRecePerson.setContent(infoEntity.receiver_person_name);
        mIIVRecePhone.setContent(infoEntity.receiver_person_phone);
        mIIVReceAddress.setContent(infoEntity.receiver_person_address);

        type = Integer.parseInt(infoEntity.receipt_type);
        setType(type);
        fee = Integer.parseInt(infoEntity.fee_type);
        switch (fee){
            case 1:
                mTvContent.setText("咨询费");
                break;
            case 2:
                mTvContent.setText("服务费");
                break;
            default:
                mTvContent.setText("咨询费");
                break;
        }
    }

    private void initListener() {
        mAbar.setOnClickListener(this);
        mRlType.setOnClickListener(this);
        mRlContent.setOnClickListener(this);
    }

    private void initView() {
        mAbar = (ActionBar) findViewById(R.id.ab_receipt);
        mRlType = (RelativeLayout) findViewById(R.id.rl_receipt_type);
        mRlContent = (RelativeLayout) findViewById(R.id.rl_receipt_content);
        mTvType = (TextView) findViewById(R.id.tv_receipt_type_content);
        mTvContent = (TextView) findViewById(R.id.tv_receipt_content_content);
        mTvHint = (TextView) findViewById(R.id.tv_receipt_hint);
        mVContent = findViewById(R.id.v_receipt_content);
        mVOrgaIdentify = findViewById(R.id.v_receipt_identify);
        mVOrgaAddress = findViewById(R.id.v_receipt_oaddress);
        mVOrgaPhone = findViewById(R.id.v_receipt_otelephonenumber);
        mVOrgaBank = findViewById(R.id.v_receipt_obankname);
        mVOrgaBankAccount = findViewById(R.id.v_receipt_obankaccount);
        mIIVOrgaName = (InvoiceItemView) findViewById(R.id.iiv_receipt_organization);
        mIIVOrgaIdentify = (InvoiceItemView) findViewById(R.id.iiv_receipt_identify);
        mIIVOrgaAddress = (InvoiceItemView) findViewById(R.id.iiv_receipt_oaddress);
        mIIVOrgaPhone = (InvoiceItemView) findViewById(R.id.iiv_receipt_otelephonenumber);
        mIIVOrgaBank = (InvoiceItemView) findViewById(R.id.iiv_receipt_obankname);
        mIIVOrgaBankAccount = (InvoiceItemView) findViewById(R.id.iiv_receipt_obankaccount);
        mIIVRecePerson = (InvoiceItemView) findViewById(R.id.iiv_receipt_receiverperson);
        mIIVRecePhone = (InvoiceItemView) findViewById(R.id.iiv_receipt_receiverperson_telephonenumber);
        mIIVReceAddress = (InvoiceItemView) findViewById(R.id.iiv_receipt_receiverperson_address);
        mIIVOrgaPhone.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        mIIVOrgaBankAccount.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        mIIVRecePhone.setInputType(EditorInfo.TYPE_CLASS_NUMBER);

        mContext = this;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_receipt_type:
                View contentViewType = LayoutInflater.from(this).inflate(R.layout.dialog_receipt_type,null);
                mBtnReceipt = (TextView) contentViewType.findViewById(R.id.btn_receipt_receipt);
                mBtnNormal = (TextView) contentViewType.findViewById(R.id.btn_receipt_normal);
                mBtnSpecial = (TextView) contentViewType.findViewById(R.id.btn_receipt_special);
                mVReceip = contentViewType.findViewById(R.id.v_receipt_receipt);
                mVNormal = contentViewType.findViewById(R.id.v_receipt_normal);
                InputUtil.hideSoftInputFromWindow(this);
                setReceiptTypeListener(v, contentViewType);
                setTypeVisible(type);
                break;
            case R.id.rl_receipt_content:
                View contentViewContent = LayoutInflater.from(this).inflate(R.layout.dialog_receipt_content,null);
                InputUtil.hideSoftInputFromWindow(this);
                mBtnConsult = (TextView) contentViewContent.findViewById(R.id.btn_receipt_consult);
                mBtnServer = (TextView) contentViewContent.findViewById(R.id.btn_receipt_server);
                mVConsult = contentViewContent.findViewById(R.id.v_receipt_consult);
                setReceiptTypeListener(v, contentViewContent);
                setFeeVisible(fee);
                break;
            case R.id.ll_left_layout: //返回
                finish();
                break;
            case R.id.tv_btn_right: //保存
                saveReceiptInfo();
                break;
            case R.id.btn_receipt_receipt:
                setType(1);
                break;
            case R.id.btn_receipt_normal:
                setType(2);
                break;
            case R.id.btn_receipt_special:
                setType(3);
                break;
            case R.id.btn_receipt_consult:
                fee = 1;
                mPopType.dismiss();
                mTvContent.setText("咨询费");
                break;
            case R.id.btn_receipt_server:
                fee = 2;
                mPopType.dismiss();
                mTvContent.setText("服务费");
                break;
            default:
                break;
        }
    }

    /**
     * 点击发票类型
     * @param type
     */
    private void setTypeVisible(int type) {
        switch (type){
            case 1:
                mBtnReceipt.setVisibility(View.GONE);
                mVReceip.setVisibility(View.GONE);
                mBtnNormal.setVisibility(View.VISIBLE);
                mVNormal.setVisibility(View.VISIBLE);
                mBtnSpecial.setVisibility(View.VISIBLE);
                break;
            case 2:
                mBtnReceipt.setVisibility(View.VISIBLE);
                mVReceip.setVisibility(View.VISIBLE);
                mBtnNormal.setVisibility(View.GONE);
                mVNormal.setVisibility(View.GONE);
                mBtnSpecial.setVisibility(View.VISIBLE);
                break;
            case 3:
                mBtnReceipt.setVisibility(View.VISIBLE);
                mVReceip.setVisibility(View.VISIBLE);
                mBtnNormal.setVisibility(View.VISIBLE);
                mVNormal.setVisibility(View.GONE);
                mBtnSpecial.setVisibility(View.GONE);
                break;
            default:
                setTypeVisible(1);
                break;
        }
    }

    /**
     * 点击发票内容
     * @param fee
     */
    private void setFeeVisible(int fee) {
        switch (fee){
            case 1:
                mBtnConsult.setVisibility(View.GONE);
                mVConsult.setVisibility(View.GONE);
                mBtnServer.setVisibility(View.VISIBLE);
                break;
            case 2:
                mBtnConsult.setVisibility(View.VISIBLE);
                mVConsult.setVisibility(View.GONE);
                mBtnServer.setVisibility(View.GONE);
                break;
            default:
                setFeeVisible(1);
                break;
        }
    }

    /**
     * 保存发票信息
     */
    private void saveReceiptInfo() {
        infoEntity = new ReceiptInfoEnity();
        infoEntity.receipt_type = type+"";
        if (null == mIIVOrgaName.getContent() || mIIVOrgaName.getContent().equals("")){
            ToastUtil.getInstance(mContext).setContent("发票抬头不能为空").setShow();
            return;
        }
        //除收据之外的信息
        infoEntity.organization_name = mIIVOrgaName.getContent();
        if (!"1".equals(type+"")){
            infoEntity.fee_type = fee + "";
            if (null == mIIVOrgaIdentify.getContent() || mIIVOrgaIdentify.getContent().equals("")){
                ToastUtil.getInstance(mContext).setContent("纳税人识别号不能为空").setShow();
                return;
            }
            infoEntity.organization_identify = mIIVOrgaIdentify.getContent();
        }else {
            infoEntity.fee_type = fee+"";
            infoEntity.organization_identify = null;
        }
        //增值税的信息
        if ("3".equals(type+"")){
            if (null == mIIVOrgaAddress.getContent() || mIIVOrgaAddress.getContent().equals("")){
                ToastUtil.getInstance(mContext).setContent("地址不能为空").setShow();
                return;
            }
            infoEntity.organization_address = mIIVOrgaAddress.getContent();
            if (null == mIIVOrgaPhone.getContent() || mIIVOrgaPhone.getContent().equals("")){
                ToastUtil.getInstance(mContext).setContent("电话不能为空").setShow();
                return;
            }
            infoEntity.organization_phone = mIIVOrgaPhone.getContent();
            if (null == mIIVOrgaBank.getContent() || mIIVOrgaBank.getContent().equals("")){
                ToastUtil.getInstance(mContext).setContent("开户行不能为空").setShow();
                return;
            }
            infoEntity.organization_bank_name = mIIVOrgaBank.getContent();
            if (null == mIIVOrgaBankAccount.getContent() || mIIVOrgaBankAccount.getContent().equals("")){
                ToastUtil.getInstance(mContext).setContent("账号不能为空").setShow();
                return;
            }
            infoEntity.organization_bank_account = mIIVOrgaBankAccount.getContent();
        }else {
            infoEntity.organization_address = null;
            infoEntity.organization_phone = null;
            infoEntity.organization_bank_name = null;
            infoEntity.organization_bank_account = null;
        }

        if (null == mIIVRecePerson.getContent() || mIIVRecePerson.getContent().equals("")){
            ToastUtil.getInstance(mContext).setContent("寄送人不能为空").setShow();
            return;
        }
        infoEntity.receiver_person_name = mIIVRecePerson.getContent();
        if (null == mIIVRecePhone.getContent() || mIIVRecePhone.getContent().equals("")){
            ToastUtil.getInstance(mContext).setContent("寄送人号码不能为空").setShow();
            return;
        }
        infoEntity.receiver_person_phone = mIIVRecePhone.getContent();
        if (null == mIIVReceAddress.getContent() || mIIVReceAddress.getContent().equals("")){
            ToastUtil.getInstance(mContext).setContent("寄送地址不能为空").setShow();
            return;
        }
        infoEntity.receiver_person_address = mIIVReceAddress.getContent();
        Intent intent = new Intent();
        intent.putExtra("receipt",infoEntity);
        setResult(CODE_RESULT_RECEIPT_SUCCES,intent);
        finish();
    }

    /**
     * 发票种类的点击事件
     * @param v
     */
    private void setReceiptTypeListener(View v, View contentView) {
        mPopType = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        mPopType.setFocusable(true);
        mPopType.setOutsideTouchable(true);
        mPopType.setBackgroundDrawable(new BitmapDrawable());
        mPopType.showAsDropDown(v);

        ImageView img = (ImageView) contentView.findViewById(R.id.img_receipt_diss);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopType.dismiss();
            }
        });
    }

    public void setType(int type) {
        if (null != mPopType) {
            mPopType.dismiss();
        }
        this.type = type;
        switch (type){
            case 1:
                mTvType.setText("收据");
                mTvHint.setText("请详细填写以下信息");
                mRlContent.setVisibility(View.GONE);
                mVContent.setVisibility(View.GONE);
                mIIVOrgaIdentify.setVisibility(View.GONE);
                mVOrgaIdentify.setVisibility(View.GONE);
                mIIVOrgaAddress.setVisibility(View.GONE);
                mVOrgaAddress.setVisibility(View.GONE);
                mIIVOrgaPhone.setVisibility(View.GONE);
                mVOrgaPhone.setVisibility(View.GONE);
                mIIVOrgaBank.setVisibility(View.GONE);
                mVOrgaBank.setVisibility(View.GONE);
                mIIVOrgaBankAccount.setVisibility(View.GONE);
                mVOrgaBankAccount.setVisibility(View.GONE);
                break;
            case 2:
                mTvType.setText("增值税普通发票");
                mTvHint.setText("请按照营业执照内容填写以下信息");
                mRlContent.setVisibility(View.VISIBLE);
                mVContent.setVisibility(View.VISIBLE);
                mIIVOrgaIdentify.setVisibility(View.VISIBLE);
                mVOrgaIdentify.setVisibility(View.VISIBLE);
                mIIVOrgaAddress.setVisibility(View.GONE);
                mVOrgaAddress.setVisibility(View.GONE);
                mIIVOrgaPhone.setVisibility(View.GONE);
                mVOrgaPhone.setVisibility(View.GONE);
                mIIVOrgaBank.setVisibility(View.GONE);
                mVOrgaBank.setVisibility(View.GONE);
                mIIVOrgaBankAccount.setVisibility(View.GONE);
                mVOrgaBankAccount.setVisibility(View.GONE);
                break;
            case 3:
                mTvType.setText("增值税专用发票");
                mTvHint.setText("请按照营业执照内容填写以下信息");
                mRlContent.setVisibility(View.VISIBLE);
                mVContent.setVisibility(View.VISIBLE);
                mIIVOrgaIdentify.setVisibility(View.VISIBLE);
                mVOrgaIdentify.setVisibility(View.VISIBLE);
                mIIVOrgaAddress.setVisibility(View.VISIBLE);
                mVOrgaAddress.setVisibility(View.VISIBLE);
                mIIVOrgaPhone.setVisibility(View.VISIBLE);
                mVOrgaPhone.setVisibility(View.VISIBLE);
                mIIVOrgaBank.setVisibility(View.VISIBLE);
                mVOrgaBank.setVisibility(View.VISIBLE);
                mIIVOrgaBankAccount.setVisibility(View.VISIBLE);
                mVOrgaBankAccount.setVisibility(View.VISIBLE);
                break;
            default:
                setType(1);
                return;
        }
    }

}
