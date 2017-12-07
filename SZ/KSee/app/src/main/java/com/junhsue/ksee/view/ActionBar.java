package com.junhsue.ksee.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.utils.ScreenWindowUtil;
import com.junhsue.ksee.utils.StringUtils;


/**
 * 标题栏，可以通过ActionBar_title_style属性来设置为是通常模式还是带搜索框
 */
public class ActionBar extends LinearLayout {


    /**
     * 同ActionBar_title_style属性
     * 用来设置为是通常模式还是带搜索框
     */
    public static final int NORMAL_RIGHT_DOUBLE_IMG = 0;
    public static final int SEARCH = 1;
    public static final int SELF_MIDDLE_LAYOUT = 2;
    public static final int NORMAL_RIGHT_TXT = 3;

    /**
     * 同ActionBar_search_left_style属性
     * 用来设置为是搜索模式下左边显示的是第一级还是第二级目录；
     */
    public static final int SEARCH_LEFT_FIRST = 0;
    public static final int SEARCH_LEFT_SECOND = 1;
    private int currentStyle = NORMAL_RIGHT_TXT;
    private int currentSearchLeftStyle = SEARCH_LEFT_FIRST;

    private RelativeLayout normalLayout;
    //标题,普通模式下的中间的文字标题
    private TextView tvTitle;
    //普通模式下的最左边按钮布局
    private LinearLayout btnNormalLeftLayout;
    //普通模式下的最左边图片按钮
    private ImageView ivNFistLeft;
    //普通模式下的最左边的文字按钮
    private TextView tvNSecondLeft;
    //普通模式下的,最右边是两个图片按钮的布局
    private LinearLayout llDoubleImgRightLayout;
    //普通模式下的,最右边第一个图片按钮的布局
    private RelativeLayout btnRightOneLayout;
    //普通模式下的,最右边第一个图片按钮
    private ImageView ivRightBtnOne;
    //普通模式下的,最右边第二个图片按钮的布局
    private RelativeLayout btnRightTwoLayout;
    //普通模式下的,最右边第二个图片按钮
    private ImageView ivRightBtnTwo;
    //普通模式下的,最右边带文字的按钮布局
    private LinearLayout llTxtRightLayout;
    //普通模式下的,最右边带文字的按钮
    private TextView tvBtnRight;
    //普通模式下最右边的图片按钮布局
    private RelativeLayout btnNormalRightImg;

    private RelativeLayout searchLayout;

    private CancelEditText etSearch;

    //搜索模式下的最左边图片按钮
    private ImageView imgbtnSearchLeft;

    //搜索模式下的最右边的文字按钮
    private TextView tvbtnSearchRight;//右边按钮

    private ImageView ivRight; // 添加图片

    private View dividerView;

    private View barView;
    private Context mContext;
    private LayoutInflater inflater;
    private String titleInfo = "";
    private String secondLeftInfo;
    private String rightInfo;
    private String leftInfo;
    private String searchHint;
    private String searchRightText;
    private OnClickListener listener;
    private int sysDefHeight;//系统默认的标题栏高度
    private int searchHeight; //设置搜索框的高度
    private boolean searchRightImageAdjuest = false;

    public ActionBar(Context context) {
        super(context);
        initLayout(context);
    }

    public ActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context, attrs);
    }

    public ActionBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout(context, attrs, defStyle);
    }

    private void initLayout(Context context) {
        this.initLayout(context, null);
    }

    private void initLayout(Context context, AttributeSet attrs) {
        initLayout(context, attrs, 0);
    }

    private void initLayout(Context context, AttributeSet attrs, int defStyle) {
        this.mContext = context;

        sysDefHeight = ScreenWindowUtil.getActionBarHeight(mContext);
        searchHeight = 8 * sysDefHeight / 11;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //设置父布局的LinearLayout方向
        setOrientation(VERTICAL);

        barView = inflater.inflate(R.layout.actionbar, this, true);

        normalLayout = (RelativeLayout) barView.findViewById(R.id.rl_actionbar);
        btnNormalLeftLayout = (LinearLayout) barView.findViewById(R.id.ll_left_layout);
        tvTitle = (TextView) barView.findViewById(R.id.tv_title);
        ivNFistLeft = (ImageView) barView.findViewById(R.id.iv_first_btn_left);
        tvNSecondLeft = (TextView) barView.findViewById(R.id.tv_second_btn_left);
        searchLayout = (RelativeLayout) barView.findViewById(R.id.rl_search_layout);

        etSearch = (CancelEditText) barView.findViewById(R.id.et_search);

        imgbtnSearchLeft = (ImageView) barView.findViewById(R.id.iv_btn_search_left);
        tvbtnSearchRight = (TextView) barView.findViewById(R.id.tv_btn_search_right);

        llDoubleImgRightLayout = (LinearLayout) barView.findViewById(R.id.double_iv_btn_layout);
        btnRightOneLayout = (RelativeLayout) barView.findViewById(R.id.btn_right_one);
        ivRightBtnOne = (ImageView) barView.findViewById(R.id.iv_btn_one);
        btnRightTwoLayout = (RelativeLayout) barView.findViewById(R.id.btn_right_two);
        ivRightBtnTwo = (ImageView) barView.findViewById(R.id.iv_btn_two);

        llTxtRightLayout = (LinearLayout) barView.findViewById(R.id.ll_right_text_layout);
        tvBtnRight = (TextView) barView.findViewById(R.id.tv_btn_right);
        btnNormalRightImg = (RelativeLayout) barView.findViewById(R.id.btn_right_normal_img);
        ivRight = (ImageView) barView.findViewById(R.id.iv_right); // 添加图片
        dividerView = (View) barView.findViewById(R.id.vw_divider);


        RelativeLayout.LayoutParams editTextParams = (RelativeLayout.LayoutParams) etSearch.getLayoutParams();
        editTextParams.height = searchHeight;
        etSearch.setLayoutParams(editTextParams);

        /*RelativeLayout.LayoutParams btnRightParams = (RelativeLayout.LayoutParams) btnSearchRight.getLayoutParams();
        btnRightParams.width = searchHeight;
        btnRightParams.height = searchHeight;
        btnSearchRight.setLayoutParams(btnRightParams);*/

        // 根据xml配置的属性设置UI
        initAttributeFromXml(attrs);
        updateSearchLayoutParam();
    }

    private void updateSearchLayoutParam() {
        if (imgbtnSearchLeft == null) {
            return;
        }
        RelativeLayout.LayoutParams editTextParams = (RelativeLayout.LayoutParams) etSearch.getLayoutParams();
        if (imgbtnSearchLeft.getVisibility() == View.GONE) {
            editTextParams.leftMargin = getResources().getDimensionPixelOffset(R.dimen.dimen_20px);
        } else {
            editTextParams.leftMargin = 0;
        }
    }

    private void initAttributeFromXml(AttributeSet attrs) {

        if (attrs == null) {
            return;
        }
        TypedArray typed = this.mContext.obtainStyledAttributes(attrs, R.styleable.ActionBar);
        if (typed == null) {
            return;
        }
        if (typed.hasValue(R.styleable.ActionBar_background)) {
            int abBackGround = typed.getResourceId(R.styleable.ActionBar_background, -1);
            setBackgroundResource(abBackGround);
        }
        if (typed.hasValue(R.styleable.ActionBar_title_style)) {
            setStyle(typed.getInt(R.styleable.ActionBar_title_style, 0));
        }

        if (typed.hasValue(R.styleable.ActionBar_center_title_text_style)) {
            setCenterTextStyle(typed.getResourceId(R.styleable.ActionBar_center_title_text_style, R.style.text_f_36_white));
        }

        //设置不同分辨率下的带搜索框样式

        if (typed.hasValue(R.styleable.ActionBar_search_right_image_adjust)) {
            searchRightImageAdjuest = typed.getBoolean(R.styleable.ActionBar_search_right_image_adjust, false);
        }
        if (typed.hasValue(R.styleable.ActionBar_search_left_style)) {
            setSearchLeftLayout(typed.getInt(R.styleable.ActionBar_search_left_style, 0));
        }
        if (typed.hasValue(R.styleable.ActionBar_search_left_image)) {
            setSearchLeftImage(typed.getResourceId(R.styleable.ActionBar_search_left_image, -1));
        }
        if (typed.hasValue(R.styleable.ActionBar_search_right_image)) {
            setSearchRightImage(typed.getResourceId(R.styleable.ActionBar_search_right_image, -1));
        }
        if (typed.hasValue(R.styleable.ActionBar_search_right_text)) {
            setSearchRightText(typed.getString(R.styleable.ActionBar_search_right_text));
        }

        if (typed.hasValue(R.styleable.ActionBar_search_hint)) {
            setSearchEditHint(typed.getString(R.styleable.ActionBar_search_hint));
        }
        if (typed.hasValue(R.styleable.ActionBar_search_left_image_visibility)) {
            setSearchLeftImageVisibility(typed.getInt(R.styleable.ActionBar_search_left_image_visibility, View.GONE));
        }
        if (typed.hasValue(R.styleable.ActionBar_search_edittext_background)) {
            setSearchEditTextBackground(typed.getResourceId(R.styleable.ActionBar_search_edittext_background, -1));
        }
        if (typed.hasValue(R.styleable.ActionBar_search_hint_color)) {
            setSearchEditHintColor(typed.getColor(R.styleable.ActionBar_search_hint_color, getResources().getColor(R.color.c_gray_999)));
        }
/*        if (typed.hasValue(R.styleable.ActionBar_search_edittext_drawable_left)) {
            setSearchEditTextDrawable(typed.getResourceId(R.styleable.ActionBar_search_edittext_drawable_left, R.drawable.home_search_gray), 0, 0, 0);
        }
        if (typed.hasValue(R.styleable.ActionBar_search_edittext_drawable_top)) {
            setSearchEditTextDrawable(0, typed.getResourceId(R.styleable.ActionBar_search_edittext_drawable_top, R.drawable.home_search_gray), 0, 0);
        }
        if (typed.hasValue(R.styleable.ActionBar_search_edittext_drawable_right)) {
            setSearchEditTextDrawable(0, 0, typed.getResourceId(R.styleable.ActionBar_search_edittext_drawable_right, R.drawable.home_search_gray), 0);
        }
        if (typed.hasValue(R.styleable.ActionBar_search_edittext_drawable_bottom)) {
            setSearchEditTextDrawable(0, 0, 0, typed.getResourceId(R.styleable.ActionBar_search_edittext_drawable_bottom, R.drawable.home_search_gray));
        }*/


        if (typed.hasValue(R.styleable.ActionBar_search_edittext_style)) {
            setSearchEditTextStyle(typed.getResourceId(R.styleable.ActionBar_search_edittext_style, R.style.text_f_30_c_9da1a7));
        }

        //设置不同分辨率下的带标题样式
        if (typed.hasValue(R.styleable.ActionBar_title)) {
            setTitle(typed.getString(R.styleable.ActionBar_title));
        }

        if (typed.hasValue(R.styleable.ActionBar_left_first_image)) {
            setNormalLeftFirstImage(typed.getResourceId(R.styleable.ActionBar_left_first_image, -1));
        }

        if (typed.hasValue(R.styleable.ActionBar_left_first_visibility)) {
            setNormalLeftFirstVisible(typed.getInt(R.styleable.ActionBar_left_first_visibility, View.VISIBLE));
        }

//        setNormalLeftFirstImage(R.drawable.icon_btn_head_back);
        if (typed.hasValue(R.styleable.ActionBar_left_second_visibility)) {
            setNormalLeftSecondVisible(typed.getInt(R.styleable.ActionBar_left_second_visibility, View.GONE));
        }
        if (typed.hasValue(R.styleable.ActionBar_left_second_text)) {
            setNormalLeftSecondText(typed.getString(R.styleable.ActionBar_left_second_text));
        }

        if (typed.hasValue(R.styleable.ActionBar_right_text)) {
            setRightText(typed.getString(R.styleable.ActionBar_right_text));
        }
        if (typed.hasValue(R.styleable.ActionBar_right_image)) {
            setRightImage(typed.getResourceId(R.styleable.ActionBar_right_image, -1));
        }
        if (typed.hasValue(R.styleable.ActionBar_right_img_one)) {
            setRightImgeOne(typed.getResourceId(R.styleable.ActionBar_right_img_one, -1));
        }

        if (typed.hasValue(R.styleable.ActionBar_right_img_two)) {
            setRightImgeTwo(typed.getResourceId(R.styleable.ActionBar_right_img_two, -1));
        }

        if (typed.hasValue(R.styleable.ActionBar_right_visibility)) {
            setRightVisible(typed.getInt(R.styleable.ActionBar_right_visibility, View.GONE));
        }


        if (typed.hasValue(R.styleable.ActionBar_bottom_divider_visibility)) {
            setBottomDividerVisible(typed.getInt(R.styleable.ActionBar_bottom_divider_visibility, View.GONE));
        }


        if (typed.hasValue(R.styleable.ActionBar_right_text_style)) {
            setRightTextStyle(typed.getResourceId(R.styleable.ActionBar_right_text_style, R.style.text_f_30_c_9da1a7));
        }
        if (typed.hasValue(R.styleable.ActionBar_right_text_color)) {
            setRightTextColor(typed.getResourceId(R.styleable.ActionBar_right_text_color, R.color.c_gray_9da1a7));
        }

        if (typed.hasValue(R.styleable.ActionBar_left_second_text_color)) {
            tvNSecondLeft.setTextColor(typed.getColor(R.styleable.ActionBar_search_hint_color, 0));

        }

        typed.recycle();
    }


    /**
     * 设置是通常模式还是搜索框模式
     *
     * @param style NORMAL、SEARCH
     */
    public void setStyle(int style) {
        this.currentStyle = style;
        switch (currentStyle) {
            case NORMAL_RIGHT_DOUBLE_IMG:
            case NORMAL_RIGHT_TXT: {
                normalLayout.setVisibility(View.VISIBLE);
                searchLayout.setVisibility(View.GONE);
                break;
            }
            case SEARCH: {
                normalLayout.setVisibility(View.GONE);
                searchLayout.setVisibility(View.VISIBLE);
                break;
            }
            case SELF_MIDDLE_LAYOUT:
                normalLayout.setVisibility(View.VISIBLE);
                searchLayout.setVisibility(View.GONE);
                tvTitle.setVisibility(GONE);
                break;
        }
    }

    /***
     * 在自定义标题栏中间添加UI
     *
     * @param view 自定义的view,不要直接添加TextView
     */
    public void addSelfMiddleLayoutView(View view) {
        if (view instanceof TextView) {
            throw new IllegalArgumentException("if you only add TextView, you can call method setTitle");
        }
        int count = normalLayout.getChildCount();
        View firstChildView = null;
        //没有add view之前第1个view是TextView
        if (count >= 1) {
            firstChildView = normalLayout.getChildAt(1);
        }
        if (firstChildView != null && !(firstChildView instanceof TextView)) {
            //说明已经添加了一个view
            normalLayout.removeViewAt(1);
        }
        //在第一个布局后面添加控件
        normalLayout.addView(view, 1);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        tvTitle.setVisibility(GONE);
    }

    /**
     * 设置为是搜索模式下左边显示的是第一级还是第二级目录
     *
     * @param style SEARCH_LEFT_FIRST、SEARCH_LEFT_SECOND
     */
    public void setSearchLeftLayout(int style) {
        this.currentSearchLeftStyle = style;
        // setSearchLayout();
    }

    /**
     * 设置搜索模式下左边的图片是否可见
     *
     * @param visibility View.GONE,VISIBLE...
     */

    public void setSearchLeftImageVisibility(int visibility) {

        imgbtnSearchLeft.setVisibility(visibility);
        if (visibility == View.GONE) {
            updateSearchLayoutParam();
        }

    }

    /**
     * 设置搜索框的hint
     *
     * @param hint 字符串
     */
    public void setSearchEditHint(String hint) {
        this.searchHint = hint;
        if (etSearch == null || searchHint == null) {
            return;
        }

        etSearch.setEditTextHintContent(searchHint);
    }

    /**
     * 设置搜索框的hint颜色
     *
     * @param color 颜色id
     */
    public void setSearchEditHintColor(int color) {
        if (etSearch == null) {
            return;
        }

        etSearch.setContentHintTextColor(color);
    }

    /**
     * 设置搜索模式下搜索框的左边图片
     *
     * @param drawableIdLeft   左边图片Id
     * @param drawableIdTop    上边图片Id
     * @param drawableIdRight  右边图片Id
     * @param drawableIdBottom 下边图片Id
     */

//    public void setSearchEditTextDrawable(int drawableIdLeft, int drawableIdTop, int drawableIdRight, int drawableIdBottom) {
//        if (drawableIdLeft > 0) {
//            Drawable drawable = mContext.getResources().getDrawable(drawableIdLeft);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            etSearch.setCompoundDrawables(drawable, null, null, null);
//        } else if (drawableIdTop > 0) {
//            Drawable drawable = mContext.getResources().getDrawable(drawableIdTop);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            etSearch.setCompoundDrawables(null, drawable, null, null);
//        } else if (drawableIdRight > 0) {
//            Drawable drawable = mContext.getResources().getDrawable(drawableIdRight);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            etSearch.setCompoundDrawables(null, null, drawable, null);
//        } else if (drawableIdBottom > 0) {
//            Drawable drawable = mContext.getResources().getDrawable(drawableIdBottom);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            etSearch.setCompoundDrawables(null, null, null, drawable);
//        }
//
//    }

    /**
     * 设置搜索模式下搜索框的字体样式
     *
     * @param style 图片Id
     */

    public void setSearchEditTextStyle(int style) {
        if (style <= 0) {
            return;
        }
        etSearch.setEditTextContentStyle(style);
    }

    /**
     * 设置搜索模式下搜索框的背景
     *
     * @param drawableId 图片Id
     */

    public void setSearchEditTextBackground(int drawableId) {
        if (drawableId <= 0) {
            return;
        }
        etSearch.setBackgroundResource(drawableId);
    }

    /**
     * 得到搜索框
     */
    public CancelEditText getSearchEditText() {
        return etSearch;
    }

    /**
     * 得到搜索框右边的button
     */
    public TextView getBtnSearchRight() {
        return tvbtnSearchRight;
    }

    /**
     * 得到右边button
     *
     * @return
     */
    public TextView getBtnRight() {
        return tvBtnRight;
    }

    /**
     * 设置搜索模式下的右边的显示文字
     *
     * @param text 字符串
     */

    public void setSearchRightText(String text) {
        if (StringUtils.isBlank(text) && tvbtnSearchRight != null) {
            tvbtnSearchRight.setVisibility(View.GONE);
            return;
        }
        tvbtnSearchRight.setVisibility(View.VISIBLE);
        this.searchRightText = text;
        tvbtnSearchRight.setText(searchRightText);
    }

    /**
     * 设置搜索模式下的右边的显示图片
     *
     * @param drawableId 图片Id
     */

    public void setSearchRightImage(int drawableId) {
        if (drawableId <= 0) {
            return;
        }
        tvbtnSearchRight.setVisibility(View.VISIBLE);
        Drawable drawable = mContext.getResources().getDrawable(drawableId);


        if (searchRightImageAdjuest) {
            drawable.setBounds(0, 0, searchHeight, searchHeight);
        } else {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        }


    }

    /**
     * 设置搜索模式下的左边的显示图片
     *
     * @param drawableId 图片Id
     */

    public void setSearchLeftImage(int drawableId) {
        if (drawableId <= 0) {
            return;
        }
        imgbtnSearchLeft.setVisibility(View.VISIBLE);
        imgbtnSearchLeft.setImageResource(drawableId);
    }

    /**
     * 设置搜索模式下的右边的显示文字
     *
     * @param textId 字符串Id
     */

    public void setSearchRightText(int textId) {
        this.searchRightText = mContext.getString(textId);
        setSearchRightText(searchRightText);
    }


    /**
     * 设置最右边的文字
     *
     * @param confirm
     */
    public void setRightText(String confirm) {
        if (StringUtils.isBlank(confirm)) {
            return;
        }
        llTxtRightLayout.setVisibility(VISIBLE);
        tvBtnRight.setVisibility(View.VISIBLE);
        this.rightInfo = confirm;
        tvBtnRight.setText(rightInfo);
        if (ivRight.getVisibility() == View.GONE) {//右边没图时候,设置右边文字的右padding
            tvBtnRight.setPadding(0, 0, mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_30px), 0);
        }
    }

    /**
     * 获取最右边的文字
     *
     * @return 返回最右边的文字
     */
    public String getRightText() {
        return rightInfo;
    }

    /**
     * 设置最右边的文字
     *
     * @param confirmId 字符串的ID
     */
    public void setRightText(int confirmId) {
        this.rightInfo = mContext.getString(confirmId);
        setRightText(rightInfo);
    }

    /**
     * 设置最右边的控件是否可见
     *
     * @param visible View.GONE,VISIBLE...
     */
    public void setRightVisible(int visible) {
        switch (currentStyle) {
            case NORMAL_RIGHT_DOUBLE_IMG:
                llDoubleImgRightLayout.setVisibility(visible);
                llTxtRightLayout.setVisibility(GONE);
                break;
            case NORMAL_RIGHT_TXT:
                llDoubleImgRightLayout.setVisibility(GONE);
                llTxtRightLayout.setVisibility(visible);
                break;
        }
    }

    /**
     * 设置最右边的图片
     *
     * @param drawableId 图片资源ID
     */

    public void setRightImage(int drawableId) {
        if (drawableId <= 0) {
            return;
        }
        llTxtRightLayout.setVisibility(VISIBLE);
        ivRight.setVisibility(VISIBLE);
        ivRight.setImageResource(drawableId);
    }

    /**
     * 设置最右边的图片，just for H5添加图片
     *
     * @param url 图片资源url
     */
    public void setRightImage(String url) {
        tvBtnRight.setVisibility(View.GONE);
        ivRight.setVisibility(View.VISIBLE);
//        BitmapHelper.getSingleton().getBitmapUtils().display(ivRight, url);
    }

    /**
     * 设置最右边的控件imageview是否可见 just for h5
     *
     * @param visible View.GONE,VISIBLE...
     */
    public void setRightH5ImageVisible(int visible) {
        ivRight.setVisibility(visible);
        tvBtnRight.setVisibility(View.GONE);
    }

    /**
     * 设置标题
     *
     * @param titleId 字符串对应的Id
     */
    public void setTitle(int titleId) {
        if (titleId <= 0) {
            return;
        }
        this.titleInfo = mContext.getString(titleId);
        setTitle(titleInfo);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        if (StringUtils.isBlank(title)) {
            return;

        }
        this.titleInfo = title;
        tvTitle.setVisibility(VISIBLE);
        tvTitle.setText(title);
    }

    /**
     * 设置标题颜色
     *
     * @param color
     */
    public void setTitleColor(int color) {
        tvTitle.setTextColor(color);
    }

    public String getTitle() {
        return titleInfo;
    }


    /**
     * 设置最左边的两个控件是否可见
     *
     * @param visible View.GONE,VISIBLE...
     */
    public void setLeftVisible(int visible) {
        //leftAllLayout.setVisibility(visible);
        ivNFistLeft.setVisibility(visible);
    }


    /**
     * 设置最左边的第一个控件是否可见
     *
     * @param visible View.GONE,VISIBLE...
     */
    public void setNormalLeftFirstVisible(int visible) {
        //leftAllLayout.setVisibility(visible);
        ivNFistLeft.setVisibility(visible);
    }


    /**
     * 设置左边的是否可见
     *
     * @param drawableId 图片的资源Id
     */
    public void setNormalLeftFirstImage(int drawableId) {
        if (drawableId <= 0) {
            return;
        }
        ivNFistLeft.setImageResource(drawableId);
    }

    /**
     * 设置最左边的第二个控件的文字
     *
     * @param s
     * @param page_index
     * @param leftTextId 字符串
     */
    public void setNormalLeftSecondText(String s, int page_index, int leftTextId) {
        this.secondLeftInfo = mContext.getString(leftTextId);
        setNormalLeftSecondText(secondLeftInfo);
    }

    /**
     * 设置最左边的第二个控件的文字
     *
     * @param leftText 字符串
     */
    public void setNormalLeftSecondText(String leftText) {


        if (StringUtils.isBlank(leftText)) {
            return;
        }
        this.leftInfo = leftText;
        tvNSecondLeft.setText(leftText);
        setNormalLeftSecondVisible(View.VISIBLE);

        if (ivNFistLeft.getVisibility() == View.GONE) {

            tvNSecondLeft.setPadding(mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_10px), 0, 0, 0);

        }

    }

    /**
     * 设置最左边的第二个控件是否可见
     *
     * @param visible View.GONE,VISIBLE...
     */
    public void setNormalLeftSecondVisible(int visible) {
        //leftAllLayout.setVisibility(visible);
        tvNSecondLeft.setVisibility(visible);
        if (tvNSecondLeft.getVisibility() == View.VISIBLE) {
            tvTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(14)});
        } else {
            tvTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        }
    }

    /**
     * 设置底部分割线的两个控件是否可见
     *
     * @param visible View.GONE,VISIBLE...
     */
    public void setBottomDividerVisible(int visible) {
        dividerView.setVisibility(visible);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
        btnNormalLeftLayout.setOnClickListener(listener);
//        ivNFistLeft.setOnClickListener(listener);//正常模式下的最左边的图片按钮
//        tvNSecondLeft.setOnClickListener(listener);//正常模式下的最左边的文字按钮
        btnRightOneLayout.setOnClickListener(listener);
        btnRightTwoLayout.setOnClickListener(listener);
//        ivRightBtnOne.setOnClickListener(listener);
//        ivRightBtnTwo.setOnClickListener(listener);
        tvbtnSearchRight.setOnClickListener(listener);//搜索右边文字按钮
        tvBtnRight.setOnClickListener(listener);
        btnNormalRightImg.setOnClickListener(listener);
        ivRight.setOnClickListener(listener);
    }

    /**
     * 设置右边文字的样式
     *
     * @param styleId
     */
    public void setRightTextStyle(int styleId) {
        if (styleId <= 0) {
            return;
        }
        tvBtnRight.setTextAppearance(getContext(), styleId);
    }

    /**
     * 设置右边文字的样式
     * @param styleId
     */
    public void setRightTextColor(int styleId) {
        if (styleId <= 0) {
            return;
        }
        tvBtnRight.setTextColor(styleId);
    }


    /**
     * z针对首页上面action
     *
     * @param position
     */
    public void setSearchPosition(int position) {
        RelativeLayout.LayoutParams editTextParams = (RelativeLayout.LayoutParams) etSearch.getLayoutParams();
        editTextParams.addRule(position);
        editTextParams.bottomMargin = sysDefHeight / 11 * 3 / 2 / 2;

        RelativeLayout.LayoutParams leftimageParams = (RelativeLayout.LayoutParams) imgbtnSearchLeft.getLayoutParams();
        leftimageParams.addRule(RelativeLayout.ALIGN_BOTTOM, etSearch.getId());
        imgbtnSearchLeft.setPadding(imgbtnSearchLeft.getPaddingLeft(), imgbtnSearchLeft.getPaddingTop(),
                imgbtnSearchLeft.getPaddingRight(), sysDefHeight / 11 * 3 / 2);

        RelativeLayout.LayoutParams btnRightLayoutParams = (RelativeLayout.LayoutParams) tvbtnSearchRight.getLayoutParams();
        btnRightLayoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, etSearch.getId());
        tvbtnSearchRight.setPadding(tvbtnSearchRight.getPaddingLeft(), tvbtnSearchRight.getPaddingTop(),
                tvbtnSearchRight.getPaddingRight(), sysDefHeight / 11 * 3 / 2);

        //imgSearchLeft.setY(etSearch.getY() + sysDefHeight / 2 + imgSearchLeft.getHeight()/2) ;
    }

    public void setRightImgeTwo(int rightImgeTwo) {
        if (rightImgeTwo <= 0) {
            return;
        }
        ivRightBtnTwo.setImageResource(rightImgeTwo);
    }

    public void setRightImgeOne(int rightImgeOne) {
        if (rightImgeOne <= 0) {
            return;
        }
        ivRightBtnOne.setImageResource(rightImgeOne);
    }

    public void setCenterTextStyle(int styleId) {
        if (styleId <= 0) {
            return;
        }
        tvTitle.setTextAppearance(getContext(), styleId);
    }
}
