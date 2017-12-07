package com.junhsue.ksee.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;


/**
 * 自定义没有滚动的ListView
 */

public class NoScrollListView extends ListView {

    private Context context;
    public static final int MEASURE = 0x00000000;
    public static final int MAX = 0x00000001;
    private int currentHeight = MAX;

    public NoScrollListView(Context context) {
        super(context);
        initAttributeFromXml(context, null);
    }

    public NoScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributeFromXml(context, attrs);
    }

    public NoScrollListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttributeFromXml(context, attrs);
    }

    private void initAttributeFromXml(Context context, AttributeSet attrs) {
        this.context = context;
        if (attrs == null) {
            return;
        }
//        TypedArray typed = this.context.obtainStyledAttributes(attrs, R.styleable.NoScrollListView);
//        if (typed == null) {
//            return;
//        }
//        if (typed.hasValue(R.styleable.NoScrollListView_height_style)) {
//            this.currentHeight = typed.getInt(R.styleable.NoScrollListView_height_style, MAX);
//        }
 //       typed.recycle();
    }

    /**
     * 设置不滚动
     */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);

        //TODO暂时没有想到怎么解决健康去哪儿地方采用MAx为啥多一块UI
        switch (currentHeight) {
            case MAX: {
                expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);

                break;
            }
            case MEASURE: {
                ViewGroup.LayoutParams params = this.getLayoutParams();
                params.height = getListViewHeightBasedOnChildren(this);
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                //LogUtils.d("params.height = " + params.height);
                expandSpec = heightMeasureSpec;
                //expandSpec = MeasureSpec.makeMeasureSpec(getListViewHeightBasedOnChildren(this), MeasureSpec.AT_MOST);
                this.setLayoutParams(params);
                break;
            }
        }

        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    private int getListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return 0;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        return totalHeight;
    }
}