/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright 2014 Manabu Shimobe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.junhsue.ksee.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.utils.StringUtils;

/***
 * 可以扩展收缩的TextView
 * <p/>
 * add by liuwenjing  参照https://github.com/Manabu-GT/ExpandableTextView
 * <p/>
 * style="@style/text_f02_34_708293"设置字体的的颜色和大小
 * etv:collapse_drawable_tip_text="收起" 需要折叠时的文字提示
 * etv:drawable_tip_text_color="@color/c07_f0eff4" 图片旁边的提示文字的字体颜色
 * etv:drawable_tip_text_size="@dimen/dimen_34px" 图片旁边的提示文字的字体大小
 * etv:expand_drawable_tip_text="展开" 需要展开的文字提示
 * etv:expand_drawable_tip_text_spacing="@dimen/dimen_34px" 提示图片和提示文字之间的间距,默认的为20px
 * etv:expand_padding_bottom="@dimen/dimen_34px" 整个UI的上下左右padding,默认的为0px
 * etv:expand_padding_left="@dimen/dimen_34px" 整个UI的上下左右padding,默认的为20px
 * etv:expand_padding_right="@dimen/dimen_34px" 整个UI的上下左右padding,默认的为20px
 * etv:expand_padding_top="@dimen/dimen_34px"  整个UI的上下左右padding,默认的为0px
 * etv:text_drawable_spacing="@dimen/dimen_88px"  提示图片和文字之间的间距,默认的为20px
 */
public class ExpandableTextView extends LinearLayout implements View.OnClickListener {

    private final String TAG = ExpandableTextView.class.getSimpleName();

    /* The default number of lines */
    private final int MAX_COLLAPSED_LINES = 5;
    private final int MAX_EXPANDED_LINES = Integer.MAX_VALUE;
    /* The default animation duration */
    private final int DEFAULT_ANIM_DURATION = 300;
    /* The default alpha value when the animation starts */
    private final float DEFAULT_ANIM_ALPHA_START = 0.7f;

    protected EllipsizeTextView mTextView;
    protected ImageView mButton; // Button to expand/collapse
    protected TextView tvExpandDrawableText;
    protected LinearLayout expandCollapseLayout;

    private boolean mRelayout;
    private boolean mCollapsed = true; // Show short version as default.

    private int mCollapsedHeight;
    private int mTextHeightWithMaxLines;
    private int mMaxCollapsedLines;
    private int mMaxExpandedLines;
    private int mMarginBetweenTxtAndBottom;
    private int orientation;
    private int expandPaddingLeft;
    private int expandPaddingRight;
    private int expandPaddingTop;
    private int expandPaddingBottom;
    private int textDrawableSpacing;
    private int expandDrawableTipTextSpacing;
    private int drawableTipTextSize;
    private int drawableTipTextColor;
    private String expandDrawableTipText;
    private String collapseDrawableTipText;

    private Drawable mExpandDrawable;
    private Drawable mCollapseDrawable;

    private int mAnimationDuration;
    private float mAnimAlphaStart;
    private boolean mAnimating;

    /* Listener for callback */
    private OnExpandStateChangeListener mListener;

    /* For saving collapsed status when used in ListView */
    private SparseBooleanArray mCollapsedStatus;
    private int mPosition;
    private Context context;
    private String originalTextString;


    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ExpandableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, context);
    }

    private void init(AttributeSet attrs, Context context) {
        this.context = context;
        if (attrs == null) {
            return;
        }

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
        if (typedArray == null) {
            return;
        }

        mMaxCollapsedLines = typedArray.getInt(R.styleable.ExpandableTextView_max_collapsed_lines, MAX_COLLAPSED_LINES);
        mMaxExpandedLines = typedArray.getInt(R.styleable.ExpandableTextView_max_expanded_lines, MAX_EXPANDED_LINES);
        mAnimationDuration = typedArray.getInt(R.styleable.ExpandableTextView_anim_duration, DEFAULT_ANIM_DURATION);
        mAnimAlphaStart = typedArray.getFloat(R.styleable.ExpandableTextView_anim_alpha_start, DEFAULT_ANIM_ALPHA_START);
        mExpandDrawable = typedArray.getDrawable(R.styleable.ExpandableTextView_expand_drawable);
        mCollapseDrawable = typedArray.getDrawable(R.styleable.ExpandableTextView_collapse_drawable);

        orientation = typedArray.getInt(R.styleable.ExpandableTextView_orientation, LinearLayout.VERTICAL);
//        int defaultDimen = context.getResources().getDimensionPixelOffset(R.dimen.dimen_20px);
        int defaultDimen = 0;
        expandPaddingLeft = typedArray.getDimensionPixelOffset(R.styleable.ExpandableTextView_expand_padding_left, defaultDimen);
        expandPaddingRight = typedArray.getDimensionPixelOffset(R.styleable.ExpandableTextView_expand_padding_right, defaultDimen);
        expandPaddingTop = typedArray.getDimensionPixelOffset(R.styleable.ExpandableTextView_expand_padding_top, 0);
        expandPaddingBottom = typedArray.getDimensionPixelOffset(R.styleable.ExpandableTextView_expand_padding_bottom, 0);
        textDrawableSpacing = typedArray.getDimensionPixelOffset(R.styleable.ExpandableTextView_text_drawable_spacing, defaultDimen);
        expandDrawableTipTextSpacing = typedArray.getDimensionPixelOffset(R.styleable.ExpandableTextView_expand_drawable_tip_text_spacing, defaultDimen);
        expandDrawableTipText = typedArray.getString(R.styleable.ExpandableTextView_expand_drawable_tip_text);
        collapseDrawableTipText = typedArray.getString(R.styleable.ExpandableTextView_collapse_drawable_tip_text);
        int defaultSize = context.getResources().getDimensionPixelSize(R.dimen.f_24);
        drawableTipTextSize = typedArray.getDimensionPixelSize(R.styleable.ExpandableTextView_drawable_tip_text_size, defaultSize);
        drawableTipTextColor = typedArray.getColor(R.styleable.ExpandableTextView_drawable_tip_text_color, getResources().getColor(R.color.c_black_55626e));

        if (expandDrawableTipText == null) {
            expandDrawableTipText = "";
        }
        if (collapseDrawableTipText == null) {
            collapseDrawableTipText = "";
        }
        if (mExpandDrawable == null) {
            mExpandDrawable = getDrawable(getContext(), R.drawable.icon_post_open);
        }
        if (mCollapseDrawable == null) {
            mCollapseDrawable = getDrawable(getContext(), R.drawable.icon_post_close);
        }

        typedArray.recycle();
        //初始化UI
        initLayout();

        //根据xml文件设置的属性更新UI
        refreshLayoutByStyle(attrs);
        refreshLayoutByAutoAttrs();
    }

    private void initLayout() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.view_exandable_textview, this, true);

        mTextView = (EllipsizeTextView) rootView.findViewById(R.id.tv_expandable_text);
        expandCollapseLayout = (LinearLayout) rootView.findViewById(R.id.ll_vertical_layout);
        mButton = (ImageView) rootView.findViewById(R.id.iv_expand_collapse);
        tvExpandDrawableText = (TextView) rootView.findViewById(R.id.tv_expand_drawable_text);
//        mTextView.setOnClickListener(this);
//        expandCollapseLayout.setOnClickListener(this);

        initHorizontalLayout();
        setPaddingByAttrs();

        //  orientation
        setOrientation(orientation);
        // default visibility is gone
        setVisibility(GONE);
    }

    /***
     * 根据布局文件中设置的Style更新UI
     *
     * @param attrs
     */
    private void refreshLayoutByStyle(AttributeSet attrs) {
        TypedArray typeAttrs = this.context.obtainStyledAttributes(attrs, new int[]{android.R.attr.textSize
                , android.R.attr.textColor});

        int defaultTextSize = context.getResources().getDimensionPixelSize(R.dimen.f_24);
        int defaultColor = context.getResources().getColor(R.color.c_black_55626e);

        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, typeAttrs.getDimensionPixelSize(0, defaultTextSize));
        int length = typeAttrs.length();
        if (length < 2) {
            typeAttrs.recycle();
            return;
        }
        mTextView.setTextColor(typeAttrs.getColor(typeAttrs.length() - 1, defaultColor));
        typeAttrs.recycle();

    }


    /***
     * 根据自定义属性更新UI
     */
    private void refreshLayoutByAutoAttrs() {
        refreshExpandDrawable();
        refreshExpandDrawableText();
        refreshTextView();
        refreshHorizontalLayout();
    }

    /***
     * 初始化水平布局的UI
     */
    private void initHorizontalLayout() {
        if (orientation == VERTICAL) {
            return;
        }
        removeAllViews();
        FrameLayout frameLayout = new FrameLayout(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        frameLayout.setLayoutParams(params);
        frameLayout.addView(mTextView);

        FrameLayout.LayoutParams expandParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        expandParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        expandCollapseLayout.setLayoutParams(expandParams);

        frameLayout.addView(expandCollapseLayout);
        addView(frameLayout);
    }

    /***
     * 设置各个padding
     */
    private void setPaddingByAttrs() {
        this.setPadding(expandPaddingLeft, expandPaddingTop, expandPaddingRight, expandPaddingBottom);

        mButton.setPadding(expandDrawableTipTextSpacing, 0, 0, 0);

        int top = orientation == VERTICAL ? textDrawableSpacing : 0;
        int left = orientation == VERTICAL ? 0 : textDrawableSpacing;
        expandCollapseLayout.setPadding(left, top, 0, 0);

    }

    /***
     * 更新drawable旁边的文字
     */
    private void refreshExpandDrawableText() {
        String text = mCollapsed ? expandDrawableTipText : collapseDrawableTipText;
        if (StringUtils.isBlank(text)) {
            return;
        }

        tvExpandDrawableText.setText(text);
        tvExpandDrawableText.setTextColor(drawableTipTextColor);
        tvExpandDrawableText.setTextSize(TypedValue.COMPLEX_UNIT_PX, drawableTipTextSize);
    }


    /***
     * 更新drawable图片
     */
    private void refreshExpandDrawable() {
        Drawable drawable = mCollapsed ? mExpandDrawable : mCollapseDrawable;
        mButton.setImageDrawable(drawable);
    }

    /***
     * 刷新水平UI ,获取不到宽度
     */
    private void refreshHorizontalLayout() {
        if (orientation == VERTICAL) {
            return;
        }
        int additionalWidth = getDrawableTipTextLength() + mExpandDrawable.getIntrinsicWidth() + expandDrawableTipTextSpacing + textDrawableSpacing;
        mTextView.setAdditionalEllipsizeWidth(additionalWidth);
    }


    private void refreshTextView() {
        if (!mCollapsed) {
            //因为在折叠的时候 textView里面的内容做了截取
            mTextView.setText(originalTextString);
            //因为在折叠的时候已经设置了setMaxLines(mMaxCollapsedLines)，所以在打开的时候，需要重新设置下该maxLine
            mTextView.setMaxLines(mMaxExpandedLines);
            return;
        }
        mTextView.setText(originalTextString);
        mTextView.setMaxLines(mMaxCollapsedLines);

    }


    @Override
    public void onClick(View view) {
        if (expandCollapseLayout.getVisibility() != View.VISIBLE) {
            return;
        }

        mCollapsed = !mCollapsed;
        refreshLayoutByAutoAttrs();

        // 清除动画
        if (true) return;

        if (mCollapsedStatus != null) {
            mCollapsedStatus.put(mPosition, mCollapsed);
        }

        // mark that the animation is in progress
        mAnimating = true;

        Animation animation;
        if (mCollapsed) {
            animation = new ExpandCollapseAnimation(this, getHeight(), mCollapsedHeight);
        } else {
            animation = new ExpandCollapseAnimation(this, getHeight(), getHeight() +
                    mTextHeightWithMaxLines - mTextView.getHeight());
        }

        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                applyAlphaAnimation(mTextView, mAnimAlphaStart);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // clear animation here to avoid repeated applyTransformation() calls
                clearAnimation();
                // clear the animation flag
                mAnimating = false;

                // notify the listener
                if (mListener != null) {
                    mListener.onExpandStateChanged(mTextView, !mCollapsed);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        clearAnimation();
        startAnimation(animation);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // while an animation is in progress, intercept all the touch events to children to
        // prevent extra clicks during the animation
        return mAnimating;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // If no change, measure and return
        if (!mRelayout || getVisibility() == View.GONE) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        mRelayout = false;

        // Setup with optimistic case
        // i.e. Everything fits. No button needed
        //现在增加了文字 mButton.setVisibility(View.GONE);
        expandCollapseLayout.setVisibility(View.GONE);

        // Measure
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // If the text fits in collapsed mode, we are done.
        if (mTextView.getLineCount() <= mMaxCollapsedLines) {
            return;
        }

        // Saves the text height w/ max lines
        mTextHeightWithMaxLines = getRealTextViewHeight(mTextView);

        // Doesn't fit in collapsed mode. Collapse text view as needed. Show
        // button.
        if (mCollapsed) {
            mTextView.setMaxLines(mMaxCollapsedLines);
        }

        //现在增加了文字 mButton.setVisibility(View.VISIBLE);
        /** 隐藏文章过长时展开隐藏按钮 **/
        expandCollapseLayout.setVisibility(View.GONE);

        // Re-measure with new setup
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mCollapsed) {
            // Gets the margin between the TextView's bottom and the ViewGroup's bottom
            mTextView.post(new Runnable() {
                @Override
                public void run() {
                    mMarginBetweenTxtAndBottom = getHeight() - mTextView.getHeight();
                }
            });
            // Saves the collapsed height of this ViewGroup
            mCollapsedHeight = getMeasuredHeight();
        }

    }

    /***
     * 设置展开的监听
     *
     * @param listener
     */
    public void setOnExpandStateChangeListener(@Nullable OnExpandStateChangeListener listener) {
        mListener = listener;
    }

    /***
     * 设置文字
     *
     * @param text
     */
    public void setText(@Nullable CharSequence text) {
        mRelayout = true;
        mTextView.setText(text);
        this.originalTextString = text.toString();
        setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
    }

    public void setText(@Nullable CharSequence text, @NonNull SparseBooleanArray collapsedStatus, int position) {
        mCollapsedStatus = collapsedStatus;
        mPosition = position;
        this.originalTextString = text.toString();
        boolean isCollapsed = collapsedStatus.get(position, true);
        clearAnimation();
        mCollapsed = isCollapsed;
        refreshExpandDrawable();
        setText(text);
        getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        requestLayout();
    }

    @Nullable
    public CharSequence getText() {
        if (mTextView == null) {
            return "";
        }
        return mTextView.getText();
    }


    /***
     * 计算出该扩展或者折叠提示文字的长度(像素)
     */
    private int getDrawableTipTextLength() {
        String text = mCollapsed ? expandDrawableTipText : collapseDrawableTipText;
        if (StringUtils.isBlank(text)) {
            return 0;
        }
        Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(drawableTipTextSize);
        // Measure the width of the text string.
        return (int) mTextPaint.measureText(text);
    }

    private static boolean isPostHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    private static boolean isPostLolipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void applyAlphaAnimation(View view, float alpha) {
        if (isPostHoneycomb()) {
            view.setAlpha(alpha);
        } else {
            AlphaAnimation alphaAnimation = new AlphaAnimation(alpha, alpha);
            // make it instant
            alphaAnimation.setDuration(0);
            alphaAnimation.setFillAfter(true);
            view.startAnimation(alphaAnimation);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Drawable getDrawable(@NonNull Context context, @DrawableRes int resId) {
        Resources resources = context.getResources();
        if (isPostLolipop()) {
            return resources.getDrawable(resId, context.getTheme());
        } else {
            return resources.getDrawable(resId);
        }
    }

    private int getRealTextViewHeight(@NonNull TextView textView) {
        int lines = mMaxExpandedLines != Integer.MAX_VALUE ? mMaxExpandedLines : textView.getLineCount();
        int textHeight = textView.getLayout().getLineTop(lines);
        int padding = textView.getCompoundPaddingTop() + textView.getCompoundPaddingBottom();
        return textHeight + padding;
    }

    class ExpandCollapseAnimation extends Animation {
        private final View mTargetView;
        private final int mStartHeight;
        private final int mEndHeight;

        public ExpandCollapseAnimation(View view, int startHeight, int endHeight) {
            mTargetView = view;
            mStartHeight = startHeight;
            mEndHeight = endHeight;
            setDuration(mAnimationDuration);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final int newHeight = (int) ((mEndHeight - mStartHeight) * interpolatedTime + mStartHeight);
            mTextView.setMaxHeight(newHeight - mMarginBetweenTxtAndBottom);
            if (Float.compare(mAnimAlphaStart, 1.0f) != 0) {
                applyAlphaAnimation(mTextView, mAnimAlphaStart + interpolatedTime * (1.0f - mAnimAlphaStart));
            }
            mTargetView.getLayoutParams().height = newHeight;
            mTargetView.requestLayout();
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

    public interface OnExpandStateChangeListener {
        /**
         * Called when the expand/collapse animation has been finished
         *
         * @param textView   - TextView being expanded/collapsed
         * @param isExpanded - true if the TextView has been expanded
         */
        void onExpandStateChanged(TextView textView, boolean isExpanded);
    }
}