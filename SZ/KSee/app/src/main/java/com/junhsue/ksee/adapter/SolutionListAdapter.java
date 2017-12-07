package com.junhsue.ksee.adapter;

import android.content.Context;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.dto.SolutionListDTO;
import com.junhsue.ksee.entity.SolutionList;
import com.junhsue.ksee.entity.SolutionListItem;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 *方案包列表适配器
 * Created by longer on 17/10/12.
 */

public class SolutionListAdapter extends BaseExpandableListAdapter {


    private Context mContext;
    //
    private LayoutInflater mLayoutInflater;
    //
    private ArrayList<SolutionList> solutions=new ArrayList<SolutionList>();
    //
    private ViewHolderChild  mViewHolderChild;
    //
    private ViewHolderParent mViewHolderParent;

    public SolutionListAdapter(Context context){
        mContext=context;
        mLayoutInflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void modifyData(ArrayList<SolutionList> arrayList){
        solutions.addAll(arrayList);
        notifyDataSetChanged();
    }

    public void clearData(){
        solutions.clear();
    }

    public ArrayList<SolutionList> getList(){

        return solutions;
    }

    @Override
    public int getGroupCount() {
        return solutions.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return solutions.get(groupPosition).solutions.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return solutions.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return solutions.get(groupPosition).solutions.get(childPosition);
    }


    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(null==convertView){
            convertView=mLayoutInflater.inflate(R.layout.item_solution_list_group,null);
            mViewHolderParent=new ViewHolderParent();
            mViewHolderParent.mView=convertView.findViewById(R.id.view);
            mViewHolderParent.mTxtTilte=(TextView)convertView.findViewById(R.id.txt_title);
            convertView.setTag(mViewHolderParent);
        }else{
            mViewHolderParent=(ViewHolderParent)convertView.getTag();
        }
        //
        SolutionList solutionList=solutions.get(groupPosition);
        mViewHolderParent.mTxtTilte.setText(solutionList.group_title);
        //
        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(null==convertView){
            convertView=mLayoutInflater.inflate(R.layout.item_solution_list,null);
            mViewHolderChild=new ViewHolderChild();
            mViewHolderChild.mRoundedImageView=(RoundedImageView)convertView.findViewById(R.id.rounded_image_view);
            mViewHolderChild.mTxtTitle=(TextView)convertView.findViewById(R.id.txt_title);
            mViewHolderChild.mTxtReadCount=(TextView)convertView.findViewById(R.id.txt_read_count);
            mViewHolderChild.mTxtTag=(TextView)convertView.findViewById(R.id.txt_tag);
            //
            convertView.setTag(mViewHolderChild);
        }else{
            mViewHolderChild=(ViewHolderChild) convertView.getTag();
        }
        SolutionList solutionList=solutions.get(groupPosition);
        //
        SolutionListItem solutionListItem=solutionList.solutions.get(childPosition);

        mViewHolderChild.mTxtTitle.setText(solutionListItem.title);
        mViewHolderChild.mTxtTag.setText("#"+solutionListItem.tag_name);
        setReadCount(mViewHolderChild.mTxtReadCount,solutionListItem.readcount);
        //
        ImageLoader.getInstance().displayImage(solutionListItem.poster,mViewHolderChild.mRoundedImageView,
                ImageLoaderOptions.option(R.drawable.img_default_course_system));
        //
        return convertView;
    }

    private void setReadCount(TextView tvReadCount, long readCount) {
        if (readCount >= 1000) {
            tvReadCount.setText((readCount / 1000) + "." + (readCount % 1000) / 100 + "k" + "浏览");
        } else {
            tvReadCount.setText(readCount + "浏览");
        }
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }



    private class ViewHolderParent{

        private View mView;
        //
        private TextView mTxtTilte;
    }



    private class  ViewHolderChild {
        //
        private RoundedImageView mRoundedImageView;
        //
        private TextView mTxtTitle;
        //
        private TextView mTxtTag;
        //
        private TextView mTxtReadCount;

    }
}
