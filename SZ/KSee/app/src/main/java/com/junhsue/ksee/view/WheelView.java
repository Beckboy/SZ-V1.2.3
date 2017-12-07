package com.junhsue.ksee.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.junhsue.ksee.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hunter_J on 17/4/7.
 */

public class WheelView extends ScrollView {

  public static final String TAG = WheelView.class.getSimpleName();

  public static class OnWheelViewListener {
    public void onSelected(int selectedIndex, String item) {
    }
  }

  private Context context;
  private RadioGroup views;

  public WheelView(Context context) {
    super(context);
    init(context);
  }

  public WheelView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public WheelView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context);
  }

  List<String> items;

  private List<String> getItems() {
    return items;
  }

  public void setItems(List<String> list) {
    if (null == items) {
      items = new ArrayList<String>();
    }
    items.clear();
    items.addAll(list);

    // 前面和后面补全
//    for (int i = 0; i < offset; i++) {
//      items.add(0, "");
////      if (i != offset-1) {
//        items.add("");
////      }
//    }

    initData();
  }


  public static final int OFF_SET_DEFAULT = 1;
  int offset = OFF_SET_DEFAULT; // 偏移量（需要在最前面和最后面补全）

  public int getOffset() {
    return offset;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  int displayItemCount; // 每页显示的数量

  int selectedIndex = 1;


  private void init(Context context) {
    this.context = context;
    Log.d(TAG, "parent: " + this.getParent());
    this.setVerticalScrollBarEnabled(false);

    views = new RadioGroup(context);
    views.setOrientation(LinearLayout.VERTICAL);
    this.addView(views);

    scrollerTask = new Runnable() {

      public void run() {

        int newY = getScrollY();
        if (initialY - newY == 0) { // stopped
          final int remainder = initialY % itemHeight;
          final int divided = initialY / itemHeight;

          if (remainder == 0) {
            selectedIndex = divided + offset;
            onSeletedCallBack();
          } else {
            if (remainder > itemHeight / 2) {
              WheelView.this.post(new Runnable() {
                @Override
                public void run() {
                  WheelView.this.smoothScrollTo(0, initialY - remainder + itemHeight);
                  selectedIndex = divided + offset + 1;
                  onSeletedCallBack();
                }
              });
            } else {
              WheelView.this.post(new Runnable() {
                @Override
                public void run() {
                  WheelView.this.smoothScrollTo(0, initialY - remainder);
                  selectedIndex = divided + offset;
                  onSeletedCallBack();
                }
              });
            }
          }
        } else {
          initialY = getScrollY();
          WheelView.this.postDelayed(scrollerTask, newCheck);
        }
      }
    };
  }

  int initialY;
  Runnable scrollerTask;
  int newCheck = 50;

  public void startScrollerTask() {

    initialY = getScrollY();
    this.postDelayed(scrollerTask, newCheck);
  }

  private void initData() {
    displayItemCount = offset * 2 + 1;

    for (int i = 0; i < items.size(); i++) {
      String item = items.get(i);
      views.addView(createView(item,i));
    }

    refreshItemView(0);
  }

  int itemHeight = 0;

  private RadioButton createView(final String item, final int index) {
//    final TextView tv = new TextView(context);
    final RadioButton rb = (RadioButton) LayoutInflater.from(context).inflate(R.layout.item_organization_rb,views,false);
//    if (item.equals("")){
//      Log.i("tag","无");
//      rb.setCompoundDrawables(null,null,null,null);
//      rb.setClickable(false);
//    }
    rb.setSingleLine(true);
    rb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
    rb.setText(item);
    rb.setGravity(Gravity.LEFT);
    int padding = dip2px(15);
    rb.setPadding(padding*2, padding, padding*2, padding);
    if (0 == itemHeight) {
      itemHeight = getViewMeasuredHeight(rb)+3;  //TODO 控件高度加上边界线高
      views.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight * displayItemCount));
      LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.getLayoutParams();
      this.setLayoutParams(new LinearLayout.LayoutParams(lp.width, itemHeight * displayItemCount));
    }
    rb.setTag(index);
    rb.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
//        setSeletion(Integer.parseInt(rb.getTag()+""));
        selectedIndex = Integer.parseInt(rb.getTag()+"");
        onSeletedCallBack();
      }
    });
    rb.setBackgroundColor(Color.parseColor("#ffffff"));
    return rb;
  }

  @Override
  protected void onScrollChanged(int l, int t, int oldl, int oldt) {
    super.onScrollChanged(l, t, oldl, oldt);

    refreshItemView(t);

    if (t > oldt) {
      scrollDirection = SCROLL_DIRECTION_DOWN;
    } else {
      scrollDirection = SCROLL_DIRECTION_UP;
    }
  }

  private void refreshItemView(int y) {
    int position = y / itemHeight + offset;
    int remainder = y % itemHeight;
    int divided = y / itemHeight;

    if (remainder == 0) {
      position = divided + offset;
    } else {
      if (remainder > itemHeight / 2) {
        position = divided + offset + 1;
      }
    }

    int childSize = views.getChildCount();
    for (int i = 0; i < childSize; i++) {
      TextView itemView = (TextView) views.getChildAt(i);
      if (null == itemView) {
        return;
      }
//      if (position == i) {
////        itemView.setTextColor(Color.parseColor("#0288ce"));
//      } else {
        itemView.setTextColor(Color.parseColor("#3C4350"));
//      }
    }
  }

  /**
   * 获取选中区域的边界
   */
  int[] selectedAreaBorder;

  private int[] obtainSelectedAreaBorder() {
    if (null == selectedAreaBorder) {
      selectedAreaBorder = new int[2];
      selectedAreaBorder[0] = itemHeight * offset;
      selectedAreaBorder[1] = itemHeight * (offset + 1);
    }
    return selectedAreaBorder;
  }

  private int scrollDirection = -1;
  private static final int SCROLL_DIRECTION_UP = 0;
  private static final int SCROLL_DIRECTION_DOWN = 1;

  Paint paint;
  int viewWidth;

  @Override
  public void setBackgroundDrawable(Drawable background) {

    if (viewWidth == 0) {
      viewWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
      Log.d(TAG, "viewWidth: " + viewWidth);
    }

    if (null == paint) {
      paint = new Paint();
//      paint.setColor(Color.parseColor("#83cde6"));
      paint.setColor(Color.parseColor("#55000000"));
      paint.setStrokeWidth(dip2px(1f));
    }

    background = new Drawable() {
      @Override
      public void draw(Canvas canvas) {
//        canvas.drawLine(0, obtainSelectedAreaBorder()[0], viewWidth * 6 / 6, obtainSelectedAreaBorder()[0], paint);
//        canvas.drawLine(0, obtainSelectedAreaBorder()[1], viewWidth * 6 / 6, obtainSelectedAreaBorder()[1], paint);
      }

      @Override
      public void setAlpha(int alpha) {
      }

      @Override
      public void setColorFilter(ColorFilter cf) {
      }

      @Override
      public int getOpacity() {
        return 0;
      }
    };

    super.setBackgroundDrawable(background);

  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    Log.d(TAG, "w: " + w + ", h: " + h + ", oldw: " + oldw + ", oldh: " + oldh);
    viewWidth = w;
    setBackgroundDrawable(null);
  }

  /**
   * 选中回调
   */
  private void onSeletedCallBack() {
    if (null != onWheelViewListener) {
      setSeletion(selectedIndex);
      onWheelViewListener.onSelected(selectedIndex, items.get(selectedIndex));
    }

  }

  public void setSeletion(int position) {
    final int p = position;
    selectedIndex = p;
    RadioButton rb = (RadioButton) views.getChildAt(position);
    rb.setChecked(true);
    this.post(new Runnable() {
      @Override
      public void run() {
        WheelView.this.smoothScrollTo(0, (p-getOffset()) * itemHeight);
      }
    });

  }

  public String getSeletedItem() {
    return items.get(selectedIndex);
  }

  public int getSeletedIndex() {
    return selectedIndex - offset;
  }


  @Override
  public void fling(int velocityY) {
    super.fling(velocityY / 3);
  }

  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    if (ev.getAction() == MotionEvent.ACTION_UP) {

      startScrollerTask();
    }
    return super.onTouchEvent(ev);
  }

  private OnWheelViewListener onWheelViewListener;

  public OnWheelViewListener getOnWheelViewListener() {
    return onWheelViewListener;
  }

  public void setOnWheelViewListener(OnWheelViewListener onWheelViewListener) {
    this.onWheelViewListener = onWheelViewListener;
  }

  private int dip2px(float dpValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }

  private int getViewMeasuredHeight(View view) {
    int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
    int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
    view.measure(width, expandSpec);
    return view.getMeasuredHeight();
  }

}
