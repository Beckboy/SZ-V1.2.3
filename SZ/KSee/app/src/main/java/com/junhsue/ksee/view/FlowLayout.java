package com.junhsue.ksee.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hunter_J on 17/3/28.
 */

public class FlowLayout extends ViewGroup {

  //存储当前ViewGroup的所有view，在Activity中直接用addView(View view)添加；
  private List<List<View>> mAllViews = new ArrayList<List<View>>();
  //把每一行数据的高度存储到List
  private List<Integer> mHeightList = new ArrayList<Integer>();

  public FlowLayout(Context context) {
    this(context,null);
  }

  public FlowLayout(Context context, AttributeSet attrs) {
    this(context, attrs,0);
  }

  public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    mAllViews.clear();
    mHeightList.clear();
    int width = getWidth();
    int overWidth = 0; //每一行view所占据的总宽度margin，padding
    int overHeight = 0; //每一个view所占据的总高度
    List<View> lineViews = new ArrayList<View>();
    int viewCount = getChildCount(); //所有View的总数量
    for (int i = 0;i < viewCount ; i++){
      View child = getChildAt(i); //每一个子View
      MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
      int childViewWidth = child.getMeasuredWidth();
      int childViewHeight = child.getMeasuredHeight();
      //当前View超过一行时，换行处理
      if (childViewWidth + overWidth + lp.leftMargin + lp.rightMargin > width - getPaddingLeft() - getPaddingRight()){ //换行判断
        mHeightList.add(overHeight);
        mAllViews.add(lineViews);
        //重置行宽和行高
        overWidth = 0;
        overHeight = childViewHeight + lp.topMargin + lp.bottomMargin;
        //重置我们的View集合
        lineViews = new ArrayList<View>();
      }
      overWidth += childViewWidth + lp.leftMargin + lp.rightMargin;
      overHeight = Math.max(overHeight,childViewHeight + lp.topMargin +lp.bottomMargin);
      lineViews.add(child);
    }

    //处理最后一行
    mHeightList.add(overHeight);
    mAllViews.add(lineViews);
    //设置每一个子View的位置
    int childLeft = getPaddingLeft();
    int childTop = getPaddingTop();
    //当前行数
    int linesNum = mAllViews.size();
    for (int i = 0; i < linesNum; i++){
      //当前行的所有view
      lineViews = mAllViews.get(i);
      overHeight = mHeightList.get(i);
      for (int j = 0;j < lineViews.size();j++){
        View child = lineViews.get(j);
        //判断当前View的状态
        if (child.getVisibility() == View.GONE){
          continue;
        }
        MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        int lc = childLeft + lp.leftMargin;
        int tc = childTop + lp.topMargin;
        int rc = lc + child.getMeasuredWidth();
        int bc = tc + child.getMeasuredHeight();
        child.layout(lc,tc,rc,bc); //为子View进行布局
        childLeft +=child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
      }
      childLeft = getPaddingLeft();
      childTop += overHeight;
    }
  }


  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
    int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
    int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
    int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    //wrap_content模式下的宽度和高度
    int width = 0;
    int height = 0;
    //记录每一行的宽度和高度
    int lineWidth = 0;
    int lineHeight = 0;
    //获取内容的View元素个数
    int cCount = getChildCount();
    for (int i = 0; i < cCount; i++){
      View child = getChildAt(i);
      //测量子View的宽度和高度
      measureChild(child,widthMeasureSpec,heightMeasureSpec);
      //得到LayoutParams
      MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
      //子View占据的宽度
      int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
      //子View占据的高度
      int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
      //换行处理
      if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight()){
        //对比得到的最大宽度
        width = Math.max(width,lineWidth);
        //重置
        lineWidth = childWidth;
        //记录行高
        height += lineHeight;
        lineHeight = childHeight;
      }else {
        lineWidth += childWidth;
        //获取当前行最大的高度
        lineHeight = Math.max(lineHeight,childHeight);
      }
      if (i == cCount - 1){ //如果是最后一个控件
        width = Math.max(lineWidth,width);
        height +=lineHeight;
      }


    }

    setMeasuredDimension(
        modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width + getPaddingLeft() + getPaddingRight(),
        modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height + getPaddingTop() + getPaddingBottom());
  }

  public int getCloseHeight(int lines){
    int hh = 0;
    if (lines > mHeightList.size()){
      lines = mHeightList.size();
    }
    for (int i = 0; i < lines;i++){
       hh += mHeightList.get(i);
    }
    return hh;
  }

  public int getLines(){
      return mAllViews.size();
  }

  @Override
  public LayoutParams generateLayoutParams(AttributeSet attrs) {
    return new MarginLayoutParams(getContext(),attrs);
  }


}
