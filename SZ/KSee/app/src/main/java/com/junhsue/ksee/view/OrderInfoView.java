package com.junhsue.ksee.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.junhsue.ksee.R;
import com.junhsue.ksee.entity.GoodsInfo;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.utils.NumberFormatUtils;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 商品信息View
 * Created by longer on 17/4/6.
 */


public class OrderInfoView extends FrameLayout  implements View.OnClickListener{


    private Context mContext;
    private ImageView mImgGoods;       //商品图片
    private TextView mTxtGoodsName;    //商品名称
    private TextView mTxtGoodsType;    //商品类型
    private TextView mTxtUnitPrice;    //商品价格
    private TextView mTxtGoodsNumberSmall;    //商品数量
    private LinearLayout mLLNumber0peration;  //数量操作
    private LinearLayout mLLNumberAdd;  //数量增加
    private TextView mTxtGoodsNumber;//商品数量
    private LinearLayout mLLGoodsSubtract;//商品数量减少
    private View mViewLine;
    private View mViewEmpty;
    private LinearLayout mLLPriceTotal; //
    private TextView mTxtPriceTotal; //总金额
    private TextView mTxtNumberUnit;//数量单位

    private ImageView mImgGoodsNumberSubtract;//
    //商品数量是否可以操作
    private boolean mIsOperation;

    //商品数量
    private int mGoodsNumber=1;

    //设置商品单价
    private double mGoodsUnitPrice;


    private IOrderChangeClickListener mIOrderChangeClickListener;

    public OrderInfoView(Context context) {
        super(context);
        this.mContext = context;
        initilizeView(context, null);
    }

    public OrderInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initilizeView(context, attrs);

    }

    public OrderInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initilizeView(context, attrs);
    }


    /**
     * initilize view for the layout
     */
    private void initilizeView(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.component_order_goods,this);
        //
        mImgGoods=(ImageView)view.findViewById(R.id.img_goods);
        mTxtGoodsName=(TextView)view.findViewById(R.id.title);
        mTxtGoodsType=(TextView)view.findViewById(R.id.txt_goods_type);
        mTxtUnitPrice=(TextView)view.findViewById(R.id.txt_unit_price);
        mTxtGoodsNumberSmall=(TextView)view.findViewById(R.id.txt_goods_number_small);
        mTxtGoodsNumber=(TextView)view.findViewById(R.id.txt_goods_number);
        mViewEmpty=view.findViewById(R.id.view_empty);
        mViewLine=view.findViewById(R.id.view_line);
        mLLNumber0peration=(LinearLayout)view.findViewById(R.id.ll_goods_number_operation);
        mLLNumberAdd=(LinearLayout)view.findViewById(R.id.ll_goods_number_add);
        mLLGoodsSubtract=(LinearLayout)view.findViewById(R.id.ll_goods_number_subtract);
        mLLPriceTotal=(LinearLayout)view.findViewById(R.id.ll_price_total);
        mTxtPriceTotal=(TextView)view.findViewById(R.id.txt_prict_quantity);
        mImgGoodsNumberSubtract=(ImageView)view.findViewById(R.id.img_goods_subtract);
        mTxtNumberUnit=(TextView)view.findViewById(R.id.txtNumberUnit);
        //
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.order_goods_style);

        if(typedArray.hasValue(R.styleable.order_goods_style_goods_number_operation_visible)){
            Boolean isOperation=typedArray.getBoolean(R.styleable.order_goods_style_goods_number_operation_visible,true);
            setGoodsOperationVisibility(isOperation);
        }else{
            setGoodsOperationVisibility(true);
        }

        if(typedArray.hasValue(R.styleable.order_goods_style_goods_number_unit_visible)){
            Boolean isShowUnit=typedArray.getBoolean(R.styleable.order_goods_style_goods_number_unit_visible,true);
            showNumberUnit(isShowUnit);
        }else{
            showNumberUnit(false);
        }

        if(typedArray.hasValue(R.styleable.order_goods_style_goods_price_visible)){
            Boolean isShowPrice=typedArray.getBoolean(R.styleable.order_goods_style_goods_price_visible,true);
            setGoodsPriceVisibility(isShowPrice);
        }else{
            setGoodsPriceVisibility(true);
        }

        if(typedArray.hasValue(R.styleable.order_goods_style_goods_number_small_visible)){
            Boolean isShowPriceSmall=typedArray.getBoolean(R.styleable.order_goods_style_goods_number_small_visible,false);
            setGoodsNumberSmallVisibility(isShowPriceSmall);
        }else{
            setGoodsNumberSmallVisibility(false);
        }

        if(typedArray.hasValue(R.styleable.order_goods_style_total_price_visible)){
            Boolean isShowTotalPrice=typedArray.getBoolean(R.styleable.order_goods_style_total_price_visible,true);
            setPriceTotalVisibility(isShowTotalPrice);
        }else{
            setPriceTotalVisibility(true);
        }
        if(typedArray.hasValue(R.styleable.order_goods_style_number_is_operation)){
            Boolean isOperation=typedArray.getBoolean(R.styleable.order_goods_style_number_is_operation,true);
            setOperationStatus(isOperation);
        }else{
            setOperationStatus(true);
        }
        typedArray.recycle();

        //
        mLLNumberAdd.setOnClickListener(this);
        mLLGoodsSubtract.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.ll_goods_number_add:
                StatisticsUtil.getInstance(mContext).onCountActionDot("4.1.2");
                if(mIsOperation){
                    mGoodsNumber++;
                    setGoodsNumber(mGoodsNumber);
                }else{
                    Toast.makeText(mContext,mContext.getString(R.string.msg_goods_number_limit),Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.ll_goods_number_subtract:
                StatisticsUtil.getInstance(mContext).onCountActionDot("4.1.3");
                if(mGoodsNumber==1){
                    return;
                }
                if(mGoodsNumber>1){
                    mGoodsNumber--;
                    setGoodsNumber(mGoodsNumber);
                }
                break;
        }
        if(null!=mIOrderChangeClickListener){
            mIOrderChangeClickListener.onChange(mGoodsNumber);
        }
        updateGoodsTotal(mGoodsNumber,mGoodsUnitPrice);
        setOpetaionImgStatus();
    }


    public void setOrderData(String mImgUrl, String goodsName,
                             double price, GoodsInfo.GoodsType goodsType){
        ImageLoader.getInstance().displayImage(mImgUrl, mImgGoods,
                ImageLoaderOptions.option(R.drawable.img_default_course_suject));

        mGoodsUnitPrice=price;
        mTxtGoodsName.setText(goodsName);
        mTxtUnitPrice.setText("¥"+NumberFormatUtils.formatPointTwo(price));

        if(null==goodsType)return;

         mTxtGoodsType.setText("#"+goodsType.getTypeName());

         if(goodsType==GoodsInfo.GoodsType.LIVE){
             mIsOperation=false;
         }else{
             mIsOperation=true;
         }

    }

    /**
     *
     * 显示单位
     */
    private void showNumberUnit(boolean isShow){
        if(isShow){
            mTxtNumberUnit.setVisibility(View.VISIBLE);
        }else{
            mTxtNumberUnit.setVisibility(View.GONE);
        }
    }

    /**
     * 设置商品价格是否可见
     * @param visible
     */
    private void setGoodsPriceVisibility(boolean visible){
        if(visible)
            mTxtUnitPrice.setVisibility(View.VISIBLE);
        else
            mTxtUnitPrice.setVisibility(View.GONE);
    }


    /**
     * 设置商品数量是否可见
     * @param visible
     */
    private void setGoodsNumberSmallVisibility(boolean visible){
        if(visible)
            mTxtGoodsNumberSmall.setVisibility(View.VISIBLE);
        else
            mTxtGoodsNumberSmall.setVisibility(View.GONE);
    }


//    public void  setGoodsNumber(int number){
//        mTxtGoodsNumberSmall.setText("x"+number);
//    }
    /**
     * 设置商品数量操作区是否可见
     * @param visible
     */
    private void setGoodsOperationVisibility(boolean visible){
        if(visible){
            mViewEmpty.setVisibility(View.VISIBLE);
            mLLNumber0peration.setVisibility(View.VISIBLE);
        }else{
            mViewEmpty.setVisibility(View.GONE);
            mLLNumber0peration.setVisibility(View.GONE);
        }
    }


    /**
     *设置商品总额是否可见
     * @param visible
     */
    private void setPriceTotalVisibility(boolean visible) {
        if (visible) {
            mViewLine.setVisibility(View.VISIBLE);
            mLLPriceTotal.setVisibility(View.VISIBLE);
        } else {
            mViewLine.setVisibility(View.GONE);
            mLLPriceTotal.setVisibility(View.GONE);
        }
    }


    /**
     * 商品数量是否可以操作
     * @param isOperation
     *
     * 如果不可能操作，商品数量默认为1，不做加减
     */
    private void setOperationStatus(boolean isOperation){
        if(isOperation){
            mImgGoodsNumberSubtract.setBackgroundResource(R.drawable.icon_goods_subtract_focusable);
            mIsOperation=true;
        }else{
            mIsOperation=false;
        }
    }


    /**
     * 设置商品数量
     * @param
     */
    private void setGoodsNumber(int number){
        mTxtGoodsNumber.setText(String.valueOf(number));

    }


    /**
     *设置已购买的商品数量
     */
    public  void setGoodsNumberSmall(int number){
        mTxtGoodsNumberSmall.setText(String.valueOf("x"+number));
    }
    /**
     * 修改商品总价格
     * @param number  数量
     *
     * @param  unitPrice  商品单价
     */
    public void updateGoodsTotal(int number,double unitPrice){
        mTxtPriceTotal.setText(NumberFormatUtils.formatPointTwo(number*unitPrice));
    }



    private void setOpetaionImgStatus(){
        if(mGoodsNumber>1){
            mImgGoodsNumberSubtract.setBackgroundResource(R.drawable.icon_goods_subtract_normal);
        }else if(mGoodsNumber==1){
            mImgGoodsNumberSubtract.setBackgroundResource(R.drawable.icon_goods_subtract_focusable);
        }
    }


    /**
     * 获取商品数量*/
    public int getGoodsNumber(){

        return mGoodsNumber;
    }


    public void setIOrderChangeClickListener(OrderInfoView.IOrderChangeClickListener iOrderChangeClickListener) {
        this.mIOrderChangeClickListener = iOrderChangeClickListener;
    }

    /**
     * 商品数量改变接口
     */
    public interface  IOrderChangeClickListener{

        /**
         * @param number  数量
         * */
        void onChange(int number);
    }

}
