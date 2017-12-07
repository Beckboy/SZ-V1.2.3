package com.junhsue.ksee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.entity.MyCoinListEntity;
import com.junhsue.ksee.utils.DateUtils;
import com.junhsue.ksee.utils.ScreenWindowUtil;

/**
 * Created by hunter_J on 2017/9/23.
 */

public class MyCoinListAdapter<T extends MyCoinListEntity> extends MyBaseAdapter<MyCoinListEntity>{

    private Context mContext;
    private LayoutInflater mInflater;

    public MyCoinListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    protected View getWrappeView(int position, View convertView, ViewGroup parent) {

        ViewHolder mHolder = null;
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.item_my_coin_list,null);
            mHolder = new ViewHolder(convertView);
            convertView.setTag(mHolder);
        }else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        MyCoinListEntity mycoin = mList.get(position);
        if (mycoin != null){
            if (mycoin.title != null){
                mHolder.mTvCoinName.setText(mycoin.title);
            }
            if (mycoin.description != null){
                mHolder.mTvCoinDes.setText(mycoin.description);
            }
            String create_at = "/", deadline_time = "/";
            if (mycoin.create_at != null ){
                create_at = DateUtils.getFormatData(Long.parseLong(mycoin.create_at)*1000);
            }
            if (mycoin.deadline_time != null){
                deadline_time = DateUtils.getFormatData(Long.parseLong(mycoin.deadline_time)*1000);
            }
            mHolder.mTvCoinTime.setText("有效期："+create_at+" 至 "+deadline_time);
            if (mycoin.is_overdue){
                mHolder.mLlItem.setBackgroundResource(R.drawable.bg_me_solution_convert_due);

                mHolder.mTvCoinTime.setTextColor(mContext.getResources().getColor(R.color.c_gray_95a3b1));
                mHolder.mTvCoinState.setText("已过期");
            }else{
                mHolder.mLlItem.setBackgroundResource(R.drawable.bg_me_solution_convert);

            }
            if (!mycoin.is_overdue && mycoin.status != null){
                int type = Integer.parseInt(mycoin.status);
                switch (type){
                    case 1:
                        mHolder.mLlItem.setBackgroundResource(R.drawable.bg_me_solution_convert_due);
                        mHolder.mTvCoinTime.setTextColor(mContext.getResources().getColor(R.color.c_gray_95a3b1));
                        mHolder.mTvCoinState.setText("已兑换");
                        break;
                    case 2:
                        mHolder.mLlItem.setBackgroundResource(R.drawable.bg_me_solution_convert);
                        mHolder.mTvCoinTime.setTextColor(mContext.getResources().getColor(R.color.c_red_fc613c));
                        mHolder.mTvCoinState.setText("兑换券");
                        break;
                    default:
                        mHolder.mLlItem.setBackgroundResource(R.drawable.bg_me_solution_convert_due);
                        mHolder.mTvCoinTime.setTextColor(mContext.getResources().getColor(R.color.c_gray_95a3b1));
                        mHolder.mTvCoinState.setText("已失效");
                        break;
                }
            }
        }
        return convertView;
    }


    private class ViewHolder{
        public TextView mTvCoinName; //兑换券名称
        public TextView mTvCoinDes; //兑换券描述
        public TextView mTvCoinTime; //兑换券有效期
        public TextView mTvCoinState; //兑换券状态
        public LinearLayout mLlItem; //兑换券列表项

        public ViewHolder(View view) {
            mTvCoinName = (TextView) view.findViewById(R.id.tv_title);
            mTvCoinDes = (TextView) view.findViewById(R.id.tv_description);
            mTvCoinTime = (TextView) view.findViewById(R.id.tv_time);
            mTvCoinState = (TextView) view.findViewById(R.id.tv_coin_name);
            mLlItem = (LinearLayout) view.findViewById(R.id.ll_coin_item);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mLlItem.getLayoutParams();
            params.width = (int) (ScreenWindowUtil.getScreenWidth(mContext) * 0.96);
            params.height = (int) (ScreenWindowUtil.getScreenWidth(mContext) * (220.0 / 720.0));
            params.leftMargin = (int) (ScreenWindowUtil.getScreenWidth(mContext) * 0.02);
            mLlItem.setLayoutParams(params);
        }
    }


}
