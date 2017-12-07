package com.junhsue.ksee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.easeui.utils.DateUtil;
import com.junhsue.ksee.R;
import com.junhsue.ksee.entity.SolutionCouponEntity;
import com.junhsue.ksee.utils.DateUtils;

/**
 * 方案包优惠券适配器
 * Created by longer on 17/9/27.
 */

public class SolutionCouponAdapter<T extends SolutionCouponEntity> extends MyBaseAdapter<SolutionCouponEntity> {


    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ViewHolder mViewHolder;

    public SolutionCouponAdapter(Context context){
        this.mContext=context;
        mLayoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    protected View getWrappeView(int position, View convertView, ViewGroup parent) {
        if(null==convertView){
            convertView=mLayoutInflater.inflate(R.layout.item_solution_coupon,null);
            mViewHolder=new ViewHolder();
            mViewHolder.mImgBg=(ImageView)convertView.findViewById(R.id.img_coupon_bg);
            mViewHolder.mTxtCouponTilte=(TextView)convertView.findViewById(R.id.txt_coupon_title);
            mViewHolder.mTxtCouponDesc=(TextView)convertView.findViewById(R.id.txt_coupon_desc);
            mViewHolder.mTxtCouponStatus=(TextView)convertView.findViewById(R.id.txt_coupon_status);
            mViewHolder.mTxtCouponValidDate=(TextView)convertView.findViewById(R.id.txt_coupon_valid_date);
            convertView.setTag(mViewHolder);
        }else{
            mViewHolder=(ViewHolder)convertView.getTag();
        }

        SolutionCouponEntity solutionCouponEntity=getList().get(position);
        if(null!=solutionCouponEntity){

            mViewHolder.mTxtCouponTilte.setText(solutionCouponEntity.title);
            mViewHolder.mTxtCouponDesc.setText(solutionCouponEntity.description);
            mViewHolder.mTxtCouponValidDate.setText(String.format(mContext.getString(R.string.msg_solution_coupon_valid),
                    DateUtils.timestampToPatternTime(solutionCouponEntity.create_at*1000l,"yyyy-MM-dd")
                    , DateUtils.timestampToPatternTime(solutionCouponEntity.deadline_time*1000,"yyyy-MM-dd")));
            //方案包可用
//            if(solutionCouponEntity.status==2){
//                mViewHolder.mImgBg.setBackgroundResource(R.drawable.bg_solution_convert);
//                mViewHolder.mTxtCouponStatus.setText(mContext.getString(R.string.coupon));
//
//            }else if(solutionCouponEntity.status==1){
//
//            }
            if(solutionCouponEntity.is_overdue){
                mViewHolder.mImgBg.setBackgroundResource(R.drawable.bg_solution_convert_due);
                mViewHolder.mTxtCouponStatus.setText(mContext.getString(R.string.coupon_valid));

            }else{
                mViewHolder.mImgBg.setBackgroundResource(R.drawable.bg_solution_convert);
                mViewHolder.mTxtCouponStatus.setText(mContext.getString(R.string.coupon));

            }

            if(solutionCouponEntity.isSelected){
                mViewHolder.mImgBg.setBackgroundResource(R.drawable.bg_solution_convert_selected);
            }

        }

        return convertView;
    }

    class ViewHolder{

        private ImageView mImgBg;
        //标题
        private TextView mTxtCouponTilte;
        //描述
        private TextView mTxtCouponDesc;
        //有效期
        private TextView mTxtCouponValidDate;
        //有效期状态
        private TextView mTxtCouponStatus;

    }
}
