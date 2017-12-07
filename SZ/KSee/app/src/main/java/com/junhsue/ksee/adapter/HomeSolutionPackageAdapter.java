package com.junhsue.ksee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.junhsue.ksee.R;
import com.junhsue.ksee.entity.Solution;
import com.junhsue.ksee.utils.ScreenWindowUtil;
import com.junhsue.ksee.view.HomePackageItemView;
import com.junhsue.ksee.view.fancycoverflow.FancyCoverFlow;
import com.junhsue.ksee.view.fancycoverflow.FancyCoverFlowAdapter;

import java.util.ArrayList;

/**
 * 方案包 适配器
 * Created by longer on 17/9/29.
 */

public class HomeSolutionPackageAdapter extends FancyCoverFlowAdapter {

    private   Context mContext;
    private  ArrayList<Solution> mList=new ArrayList<Solution>();
    private LayoutInflater mLayoutInflater;
    private ViewHolder mViewHolder;

    private int mItemHeight;

    private int mItemWidth;
    public HomeSolutionPackageAdapter(Context context){

        mContext=context;
        mLayoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mItemWidth= (int) (ScreenWindowUtil.getScreenWidth(mContext) - mContext.getResources().getDimension(R.dimen.dimen_26px) * 2) / 2;

        mItemHeight = (int) (mItemWidth*114/95 );

    }


    public void modifyData(ArrayList<Solution> arrayList){
        if(null==arrayList || arrayList.size()==0) return;

        mList.clear();
        mList.addAll(arrayList);
        notifyDataSetChanged();
    }


    public ArrayList<Solution> getList(){
        return mList;
    }

    @Override
    public View getCoverFlowItem(int position, View reusableView, ViewGroup parent) {
        if(null==reusableView){
            reusableView=mLayoutInflater.inflate(R.layout.item_solution_package,null);

            reusableView.setLayoutParams(new FancyCoverFlow.LayoutParams(mItemWidth,mItemHeight));

            mViewHolder=new ViewHolder();
            mViewHolder.mHomePackageItemView=(HomePackageItemView) reusableView.findViewById(R.id.homePackageItem);
            reusableView.setTag(mViewHolder);
        }else{
            mViewHolder=(ViewHolder)reusableView.getTag();
        }

        Solution solution=mList.get(position);

        mViewHolder.mHomePackageItemView.setData(solution.title,solution.poster,
                solution.readcount,solution.tag_name);

        return reusableView;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    class ViewHolder{
        private HomePackageItemView mHomePackageItemView;
    }
}
