package com.junhsue.ksee.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.junhsue.ksee.R;


/**
 * 发票填写item
 * Created by longer on 17/6/1.
 */

public class InvoiceItemView extends FrameLayout {


    private Context mContext;

    //标题
    private TextView mTxtTitle;
    //内容
    private EditText mTxtContent;
    private TextView mTxtRightContent;
    public static final int NORMAL_MODEL = 0;
    public static final int EDIT_MODEL = 1;


    public InvoiceItemView(Context context) {
        super(context);
        this.mContext = context;
        initializeView(context, null);
    }

    public InvoiceItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initializeView(context, attrs);
    }

    public InvoiceItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initializeView(context, attrs);
    }


    private void initializeView(Context context, AttributeSet attrs) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //
        View view = layoutInflater.inflate(R.layout.component_invoice_normal, this);

        mTxtTitle = (TextView) view.findViewById(R.id.txt_title);
        mTxtContent = (EditText) view.findViewById(R.id.txt_content);
        mTxtRightContent = (TextView) view.findViewById(R.id.txt_right_content);
        //
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.invoice_attrs);
        //

        if (typedArray.hasValue(R.styleable.invoice_attrs_item_title)) {
            setTitle(typedArray.getString(R.styleable.invoice_attrs_item_title));
        }

        if (typedArray.hasValue(R.styleable.invoice_attrs_item_content)) {
            setContentHint(typedArray.getString(R.styleable.invoice_attrs_item_content));
        }

        if (typedArray.hasValue(R.styleable.invoice_attrs_item_right_content)) {
            setRightContentText(typedArray.getString(R.styleable.invoice_attrs_item_right_content));
        }

        if (typedArray.hasValue(R.styleable.invoice_attrs_item_title_text_style)) {
            setLeftTitleStyle(typedArray.getResourceId(R.styleable.invoice_attrs_item_title_text_style, R.style.text_f_28_c9da1a7));
        }
        if (typedArray.hasValue(R.styleable.invoice_attrs_item_content_text_color)) {
            setContentTextColor(typedArray.getColor(R.styleable.invoice_attrs_item_content_text_color, getResources().getColor(R.color.c_black_3c4350)));
        }
        if (typedArray.hasValue(R.styleable.invoice_attrs_item_title_text_size)) {
            setLeftTitleTextSize(typedArray.getDimensionPixelOffset(R.styleable.invoice_attrs_item_title_text_size, mContext.getResources().getDimensionPixelOffset(R.dimen.f_28)));
        }

        if (typedArray.hasValue(R.styleable.invoice_attrs_item_content_text_size)) {
            setContentTextSize(typedArray.getDimensionPixelOffset(R.styleable.invoice_attrs_item_content_text_size, mContext.getResources().getDimensionPixelOffset(R.dimen.f_28)));
        }

        if (typedArray.hasValue(R.styleable.invoice_attrs_invoice_model)) {
            setShowModel(typedArray.getInt(R.styleable.invoice_attrs_invoice_model, -1));
        }

        typedArray.recycle();
    }


    /**
     * 设置标题
     *
     * @param title 标题
     */
    private void setTitle(String title) {
        mTxtTitle.setText(title);
    }


    /**
     * 设置内容
     *
     * @param content
     */
    private void setContentHint(String content) {
        mTxtContent.setHint(content);
    }


    public void setContent(String content) {
        mTxtContent.setText(content);
    }

    public String getContent() {
        return mTxtContent.getText().toString().trim();
    }


    public void setInputType(int type) {
        mTxtContent.setInputType(type);
    }

    /**
     * 设置左边文本样式
     *
     * @param leftTitleStyle
     */
    public void setLeftTitleStyle(int leftTitleStyle) {
        if (leftTitleStyle <= 0) {
            return;
        }
        mTxtTitle.setTextAppearance(getContext(), leftTitleStyle);
    }

    /**
     * 设置右边编辑框的
     *
     * @param contentTextColor
     */
    public void setContentTextColor(int contentTextColor) {
        mTxtContent.setTextColor(contentTextColor);
    }

    /**
     * 设置标题字体大小
     *
     * @param leftTitleTextSize
     */
    public void setLeftTitleTextSize(int leftTitleTextSize) {
        mTxtTitle.setTextSize(leftTitleTextSize);
    }

    /**
     * 设置内容字体大小
     *
     * @param contentTextSize
     */
    public void setContentTextSize(int contentTextSize) {
        mTxtContent.setTextSize(contentTextSize);
    }

    /**
     * 设置文本内容
     *
     * @param rightContentText
     */
    public void setRightContentText(String rightContentText) {
        mTxtRightContent.setText(rightContentText);
    }

    public void setShowModel(int model) {
        switch (model) {
            case NORMAL_MODEL:
                mTxtContent.setVisibility(GONE);
                mTxtRightContent.setVisibility(VISIBLE);
                break;
            case EDIT_MODEL:
                mTxtContent.setVisibility(VISIBLE);
                mTxtRightContent.setVisibility(GONE);
                break;
            default:
                mTxtContent.setVisibility(VISIBLE);
                mTxtRightContent.setVisibility(GONE);
                break;
        }
    }

}
