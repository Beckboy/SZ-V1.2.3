package com.junhsue.ksee.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.adapter.HomeSolutionPackageAdapter;
import com.junhsue.ksee.entity.Solution;
import com.junhsue.ksee.view.fancycoverflow.FancyCoverFlow;

import java.util.ArrayList;

/**
 *
 *方案包详情
 * Created by longer on 17/9/29.
 */

public class HomeSolutionPView extends FrameLayout  implements AdapterView.OnItemClickListener,AdapterView.OnItemSelectedListener{


    private Context mContext;
    //方案包
    private FancyCoverFlow mFancyCoverFlow;
    private HomeSolutionPackageAdapter mHomeSolutionPackageAdapter;
    //
    private TextView mTxTitle;
    private TextView mTxtDesc;
    private LinearLayout mLLlContentTag;
    //
    private ArrayList<Solution> mList=new ArrayList<>();

    public HomeSolutionPView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        initializeView();
    }

    public HomeSolutionPView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        initializeView();
    }

    public HomeSolutionPView(Context context) {
        super(context);
        mContext=context;
        initializeView();
    }


    private void  initializeView(){
        LayoutInflater layoutInflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.component_home_solution,this);
        mFancyCoverFlow=(FancyCoverFlow)view.findViewById(R.id.fancyCoverFlow);
        mTxTitle=(TextView)view.findViewById(R.id.txt_solution_title);
        mTxtDesc=(TextView)view.findViewById(R.id.txt_solution_desc);
        mLLlContentTag=(LinearLayout)view.findViewById(R.id.ll_content_tag);
        //
        mFancyCoverFlow.setUnselectedAlpha(0.1f);
        mFancyCoverFlow.setUnselectedSaturation(0.0f);
        mFancyCoverFlow.setUnselectedScale(0.5f);
        mFancyCoverFlow.setSpacing(-20);
        mFancyCoverFlow.setMaxRotation(0);
        mFancyCoverFlow.setScaleDownGravity(0.2f);
        mFancyCoverFlow.setActionDistance(FancyCoverFlow.ACTION_DISTANCE_AUTO);
        //
        mFancyCoverFlow.setOnItemClickListener(this);
        mFancyCoverFlow.setOnItemSelectedListener(this);
        //
        setData("","This is desction",null);
    }




    /**
     * 初始化数据
     */
    public void  setData(String title, String desc, ArrayList<Solution> list){
        if(null==list){
            list=new ArrayList<>();
        }
        mList.clear();
        mList.addAll(list);
        mTxTitle.setText(title);
        mTxtDesc.setText(desc);

/*
        mHomeSolutionPackageAdapter=new HomeSolutionPackageAdapter(mContext,mList);
        mFancyCoverFlow.setAdapter(mHomeSolutionPackageAdapter);
*/

    }



    public void setTitle(String title){
        mTxTitle.setText(title);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

