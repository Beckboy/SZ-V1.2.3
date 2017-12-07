package com.junhsue.ksee.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.junhsue.ksee.R;
import com.junhsue.ksee.utils.StringUtils;


/**
 * 自定义带是删除功能的EditText，仿IOS效果，失去焦点时，删除icon消失
 */
public class CancelEditText extends LinearLayout {

    private Context mContext;
    private boolean aBoolean;
    private OnCancelFocusChangeListener listener;
    private View editTextview;
    private RelativeLayout rlLayout;
    private ImageView ivLeftIcon;
    private EditText etContent;
    private ImageView ivCancelIcon;
    private int rlLayoutErrBg = -1;//边框模式,默认是单色背景,另外是双向颜色模式
    private int rlLayoutNormalBg = -1;

    public static final int CommonDialogStyleNone = 0;//无输入框
    public static final int CommonDialogStylePlainTextInput = 1;//普通输入框
    public static final int CommonDialogStyleSecureTextInput = 2;//密码输入框

    public CancelEditText(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public CancelEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public CancelEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    public boolean isaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;

        setaBoolean(true);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        editTextview = inflater.inflate(R.layout.cancel_editview, this, true);
        setOrientation(VERTICAL);//设置父布局的方向
        //
        rlLayout = (RelativeLayout) editTextview.findViewById(R.id.rl_layout);
        ivLeftIcon = (ImageView) editTextview.findViewById(R.id.iv_left_icon);
        etContent = (EditText) editTextview.findViewById(R.id.et_cancel_content);
        ivCancelIcon = (ImageView) editTextview.findViewById(R.id.iv_cancel_icon);

        getAttrsFromXml(attrs);
        etContent.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                setaBoolean(hasFocus);
                if (listener != null) {
                    listener.OnCancelFocusChangeListener(hasFocus);
                }
                if (hasFocus && StringUtils.isNotBlank(etContent.getText().toString().trim())) {
                    ivCancelIcon.setVisibility(View.VISIBLE);
                } else {
                    ivCancelIcon.setVisibility(View.GONE);
                }
            }
        });
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (StringUtils.isNotBlank(etContent.getText().toString().trim())) {
                    if (isaBoolean()) {
//                        etContent.setSelection(etContent.getText().length());
//                        if (StringUtils.isEmojiCharacter(etContent.getText().toString().trim())) {
//                            Toast.makeText(mContext, mContext.getResources().getString(R.string.nonsupport_emoji), Toast.LENGTH_SHORT).show();
//                        }
                        ivCancelIcon.setVisibility(View.VISIBLE);
                    } else {
                        ivCancelIcon.setVisibility(View.GONE);
                    }
                } else {
                    ivCancelIcon.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ivCancelIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                etContent.setText("");
                setRlLayoutBackground(rlLayoutNormalBg);
            }
        });
    }

    private void getAttrsFromXml(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CancelEditText);
        if (typedArray == null) {
            return;
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_left_icon)) {
            setLeftIcon(typedArray.getResourceId(R.styleable.CancelEditText_left_icon, -1));
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_left_icon_visibility)) {
            setLeftIconVisibility(typedArray.getInt(R.styleable.CancelEditText_left_icon_visibility, View.GONE));
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_content)) {
            setEditTextContent(typedArray.getResourceId(R.styleable.CancelEditText_content, -1));
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_content_style)) {
            setEditTextContentStyle(typedArray.getResourceId(R.styleable.CancelEditText_content_style, -1));
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_content_hint)) {
            setEditTextHintContent(typedArray.getString(R.styleable.CancelEditText_content_hint));
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_input_type)) {
            setEditTextInputType(typedArray.getResourceId(R.styleable.CancelEditText_input_type, -1));
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_max_length)) {
            setEditTextMaxLength(typedArray.getInt(R.styleable.CancelEditText_max_length, -1));
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_cancel_icon)) {
            setCancelIcon(typedArray.getResourceId(R.styleable.CancelEditText_cancel_icon, R.drawable.selector_phone_clean));
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_bg_drawable)) {
            setRlLayoutBackground(typedArray.getResourceId(R.styleable.CancelEditText_bg_drawable, -1));
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_max_lines)) {
            setEditTextMaxLines(typedArray.getInt(R.styleable.CancelEditText_max_lines, -1));
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_single_line)) {
            setEditTextSingleLine(typedArray.getBoolean(R.styleable.CancelEditText_single_line, false));
        }
        if (typedArray.hasValue(R.styleable.CancelEditText_common_dialog_style)) {
            setEditTextInputType(typedArray.getInt(R.styleable.CancelEditText_common_dialog_style, 0));
        }

        typedArray.recycle();
    }

    /**
     * 设置布局的背景图片
     *
     * @param drawable
     */
    public void setRlLayoutBackground(int drawable) {
        if (drawable < 0) {
            return;
        }
        rlLayoutNormalBg = drawable;
        rlLayout.setBackgroundResource(drawable);
    }

    /**
     * 文本框的左边的图片
     *
     * @param resId 图片资源Id
     */
    public void setLeftIcon(int resId) {
        if (resId < 0) {
            return;
        }
        ivLeftIcon.setVisibility(View.VISIBLE);
        ivLeftIcon.setImageResource(resId);
    }

    /**
     * 文本框的左边的图片是否可见
     *
     * @param visibility View.GONE,View.VISIBLE ...
     */
    public void setLeftIconVisibility(int visibility) {
        ivLeftIcon.setVisibility(visibility);
    }

    /**
     * 文本框的文字
     *
     * @param content 文字
     */
    public void setEditTextContent(String content) {
        etContent.setText(content);
    }

    /**
     * 文本框的文字样式
     *
     * @param style 文字样式Id
     */
    public void setEditTextContentStyle(int style) {
        if (style <= 0) {
            return;
        }
        etContent.setTextAppearance(getContext(), style);
    }

    /**
     * 文本框的hint
     *
     * @param content 文字
     */
    public void setEditTextHintContent(String content) {
        etContent.setHint(content);
    }

    /**
     * 文本框的hint
     *
     * @param content 文字资源Id
     */
    public void setEditTextHintContent(int content) {
        if (content <= 0) {
            return;
        }
        setEditTextHintContent(getResources().getString(content));
        // etContent.setHint(content);
    }

    /**
     * 文本框的inputType
     *
     * @param inputType
     */
    public void setEditTextInputType(int inputType) {
        if (inputType <= 0) {
            return;
        }

        if (inputType == CancelEditText.CommonDialogStyleNone) {
            etContent.setInputType(inputType);
        } else if (inputType == CancelEditText.CommonDialogStylePlainTextInput) {
            etContent.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        } else if (inputType == CancelEditText.CommonDialogStyleSecureTextInput) {
            etContent.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            etContent.setInputType(inputType);
        }
    }

    /**
     * 文本框的SingleLine
     *
     * @param singleLine
     */
    public void setEditTextSingleLine(boolean singleLine) {
        etContent.setSingleLine(singleLine);
    }

    /**
     * 设置文本框最大字符数
     *
     * @param maxLength 最大字符数
     */
    public void setEditTextMaxLength(int maxLength) {
        if (maxLength <= 0) {
            return;
        }
        etContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }

    /**
     * 设置文本框最大行数
     *
     * @param maxLines 最大行数
     */
    public void setEditTextMaxLines(int maxLines) {
        if (maxLines <= 0) {
            return;
        }
        etContent.setMaxLines(maxLines);
    }

    /**
     * 获取文本框的文字
     *
     * @return 文字内容
     */
    public String getEditTextContent() {
        return etContent.getText().toString().trim();
    }

    /**
     * 文本框的文字
     *
     * @param content 文字资源Id
     */
    public void setEditTextContent(int content) {
        if (content <= 0) {
            return;
        }
        etContent.setText(content);
    }

    /**
     * 文本框的最右边的图片
     *
     * @param resId 图片资源Id
     */
    public void setCancelIcon(int resId) {
        ivCancelIcon.setImageResource(resId);
    }

    /**
     * 获取焦点事件的接口
     *
     * @param listener
     */
    public void setOnCancelFocusChangeListener(OnCancelFocusChangeListener listener) {
        this.listener = listener;
    }

    /**
     * 设置错误颜色
     *
     * @param errBgId
     */
    public void setRlLayoutErrorBg(int errBgId) {
        if (errBgId < 0) {
            return;
        }
        rlLayoutErrBg = errBgId;
        rlLayout.setBackgroundResource(errBgId);
    }




    public interface OnCancelFocusChangeListener {
        void OnCancelFocusChangeListener(boolean hasFocus);
    }

    /**
     * 监听输入事件
     *
     * @param textWatcher
     */
    public void addTextChangedListener(TextWatcher textWatcher) {
        etContent.addTextChangedListener(textWatcher);
    }

    /**
     * 设置是否可以编辑
     *
     * @param clickAble
     */
    public void setClickAble(boolean clickAble) {
        etContent.setClickable(clickAble);
        etContent.setEnabled(false);
    }

    /**
     * 获取编辑框
     *
     * @return etContent CancelEditText 中的编辑框
     */
    public EditText getCancelContentEditText() {
        return etContent;
    }


    public void setContentHintTextColor(int color) {

        if (etContent == null) {
            return;
        }
        etContent.setHintTextColor(color);
    }
}
