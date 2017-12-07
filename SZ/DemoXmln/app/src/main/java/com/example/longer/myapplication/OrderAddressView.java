package com.example.longer.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 地址信息自定View
 * Created by longer on 17/4/6.
 */

public class OrderAddressView extends FrameLayout {


    private Context mContenxt;

    //用户名
    private TextView mTxtUserName;
    //联系方式
    private TextView mTxtPhone;
    //机构
    private TextView mTxtUnit;
    //
    private LinearLayout mLLHint;
    //箭头
    private ImageView mImgArrow;


    public OrderAddressView(Context context) {
        super(context);
        mContenxt = context;
        initializeView(context, null);

    }

    public OrderAddressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContenxt = context;
        initializeView(context, attrs);
    }


    public OrderAddressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContenxt = context;
        initializeView(context, attrs);
    }



    /**
     * initialize view for the layout
     */
    private void initializeView(Context context, AttributeSet attrs) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.component_order_user_info, this);

        mTxtUserName = (TextView) view.findViewById(R.id.txt_user_name);
        mTxtPhone = (TextView) view.findViewById(R.id.txt_phone);
        mTxtUnit = (TextView) view.findViewById(R.id.txt_unit);
        mLLHint = (LinearLayout) view.findViewById(R.id.ll_hint);
        mImgArrow = (ImageView) view.findViewById(R.id.imgArrow);
        //
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.order_user_info_attrs);

        //是否显示提示信息
        if (typedArray.hasValue(R.styleable.order_user_info_attrs_item_click)) {
            Boolean hint_visilbe = typedArray.getBoolean(R.styleable.order_user_info_attrs_hint_visible, false);
            setShowHint(hint_visilbe);
        }
        // item是否能点击
        if (typedArray.hasValue(R.styleable.order_user_info_attrs_hint_visible)) {
            Boolean itemIsClick = typedArray.getBoolean(R.styleable.order_user_info_attrs_item_click, true);
            setArrowVisible(itemIsClick);
        }

        typedArray.recycle();

        //初始化地址信息
        setAddressInfo(null, null, null);
    }


    /**
     * 设置地址信息
     *
     * @param userName 用户昵称
     * @param phone    手机号
     * @param unit     单位
     */
    public void setAddressInfo(String userName, String phone, String unit) {
        if (TextUtils.isEmpty(userName)) {
            mTxtUserName.setText(String.format("报名人: %s", "无"));
        } else {
            mTxtUserName.setText(String.format("报名人: %s", userName));
        }
        if (TextUtils.isEmpty(phone)) {
            mTxtPhone.setText(String.format("联系方式: %s", "无"));
        } else {
            mTxtPhone.setText(String.format("联系方式: %s", phone));
        }
        if (TextUtils.isEmpty(unit)) {
            mTxtUnit.setText(String.format("机构/学校: %s", "无"));
        } else {
            mTxtUnit.setText(String.format("机构/学校: %s", unit));
        }
    }

    /**
     * 设置完善信息提示
     *
     * @param isShow
     */
    public void setShowHint(boolean isShow) {
        if (isShow)
            mLLHint.setVisibility(View.VISIBLE);
        else
            mLLHint.setVisibility(View.GONE);
    }


    /**
     * 设置箭头是否显示，如果item能点击箭头显示，否则隐藏
     */
    private void setArrowVisible(boolean isShowArrow) {
        if (isShowArrow)
            mImgArrow.setVisibility(View.VISIBLE);
        else
            mImgArrow.setVisibility(View.GONE);
    }


}
