package com.junhsue.ksee.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextUtils.TruncateAt;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.junhsue.ksee.R;

/**
 * 当超过最大行数之后添加省略号的TextView，当超过两行的时候还是会显示2行
 * 自定义控件：监听器EllipsizeListener，这个监听器原文作者说的是开始在Ellipsize的时候调用的 大概意思应该就是说在开始省略（也就是开始”...“
 * ）的时候调用
 *
 * @author wenjing.liu
 * @version 2016-05-11
 */
public class EllipsizeTextView extends TextView {

    private final String DEFAULT_ELLIPSIZE_TEXT = "...";
    private boolean isEllipsized = false;
    private int maxLines;
    private boolean isState = false;
    private float lineAdditionalVerPadding;
    private float lineSpacingMultiplier;
    private boolean programmaticChange = false;
    private String fullText;
    private String ellipsizeText;
    private Drawable ellipsizeDrawable;
    private int ellipsizeDrawableLength;
    private boolean isNeedEllipsize = false;
    private int additionalEllipsizeWidth;

    public interface EllipsizeListener {
        void ellipsizeStateChanged(boolean ellipsized);
    }

    private EllipsizeListener ellipsizeListener;

    public EllipsizeTextView(Context context) {
        this(context, null);

    }

    public EllipsizeTextView(Context context, AttributeSet attr) {
        this(context, attr, 0);

    }

    public EllipsizeTextView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        initLayout(context, attr, defStyle);
    }

    /***
     * 初始化UI
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    private void initLayout(Context context, AttributeSet attrs, int defStyle) {
        TypedArray typeAttrs = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.maxLines});
        int maxLine = typeAttrs.getInt(0, Integer.MAX_VALUE);
        setMaxLines(maxLine);
        typeAttrs.recycle();

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.EllipsizeTextView);
        ellipsizeText = array.getString(R.styleable.EllipsizeTextView_ellipsize_text);
        if (ellipsizeText == null) {
            ellipsizeText = DEFAULT_ELLIPSIZE_TEXT;
        }
        ellipsizeDrawable = array.getDrawable(R.styleable.EllipsizeTextView_ellipsize_drawable);
        if (ellipsizeDrawable != null) {
            ellipsizeDrawableLength = ellipsizeDrawable.getIntrinsicWidth();
        }
        array.recycle();
    }

    /***
     * 设置Ellipsize的监听
     *
     * @param listener
     */
    public void setEllipsizeListener(EllipsizeListener listener) {
        this.ellipsizeListener = listener;
    }

    /***
     * 是否是折叠还是展开状态
     *
     * @return
     */
    public boolean isEllipsized() {
        return isEllipsized;
    }

    /***
     * 是否需要进行折叠显示
     *
     * @return
     */
    public boolean isNeedEllipsize() {
        return isNeedEllipsize;
    }

    /***
     * 设置ellipsize图片
     *
     * @param drawable
     */
    public void setEllipsizeDrawable(Drawable drawable) {
        if (drawable == null) {
            return;
        }
        ellipsizeDrawable = drawable;
    }

    /***
     * 设置ellipsize文字
     *
     * @param text
     */
    public void setEllipsizeText(String text) {
        if (text == null) {
            return;
        }
        ellipsizeText = text;
    }

    /***
     * 设置额外需要被占用的宽度
     *
     * @param width 额外被占用的宽度
     */
    public void setAdditionalEllipsizeWidth(int width) {
        additionalEllipsizeWidth = width;
    }


    @Override
    public void setMaxLines(int maxLine) {
        super.setMaxLines(maxLine);
        this.maxLines = maxLine;
        isState = true;
    }

    public int getMaxLines() {
        return maxLines;
    }

    @Override
    public void setLineSpacing(float add, float mult) {
        this.lineAdditionalVerPadding = add;
        this.lineSpacingMultiplier = mult;
        super.setLineSpacing(add, mult);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (!programmaticChange) {
            fullText = text.toString();
            isState = true;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isState) {
            super.setEllipsize(null);
            measureText();
        }
        super.onDraw(canvas);
    }

    /***
     * 根据后缀和设置的行数来设置最后textView中剩余的文字
     */
    private void measureText() {
        int maxLines = getMaxLines();
        String workingText = fullText;
        boolean ellipsized = false;
        if (maxLines < 0) {
            return;
        }
        String noEllipsizeText = fullText; //不含有ellipsizeText的文字
        Layout layout = createWorkingLayout(workingText);
        if (layout.getLineCount() > maxLines) {
            workingText = fullText.substring(0, layout.getLineEnd(maxLines - 1)).trim();
            while (createWorkingLayout(workingText + ellipsizeText).getLineCount() > maxLines) {
                int length = workingText.length();
                if (length == -1) {
                    break;
                }
                workingText = workingText.substring(0, length - 2);
            }
            noEllipsizeText = workingText;
            workingText = workingText + ellipsizeText;
            ellipsized = true;
            isNeedEllipsize = ellipsized;
        }
        //设置TextView的内容
        setTextByEllipsize(workingText, noEllipsizeText);
        //改变状态
        isState = false;
        if (ellipsized == isEllipsized) {
            return;
        }
        isEllipsized = ellipsized;
        if (ellipsizeListener == null) {
            return;
        }
        ellipsizeListener.ellipsizeStateChanged(ellipsized);
    }

    private void setTextByEllipsize(String workingText, String noEllipsizeText) {
        if (!workingText.equals(getText())) {
            programmaticChange = true;
            try {
                //获取累加上ellipsizeDrawable之后的还剩余文字
                String ellipsizeText = getEllipsizeTextWithDrawableAndAdditionalText(noEllipsizeText);
                //获取图片转换成的文字
                SpannableString ellipsizeDrawableText = getEllipsizeDrawableString(ellipsizeText);
                //设置文字
                setText((ellipsizeDrawable != null || additionalEllipsizeWidth > 0) ? ellipsizeText : workingText);
                if (ellipsizeDrawable != null) {
                    append(ellipsizeDrawableText);
                }
            } finally {
                programmaticChange = false;
            }
        }
    }

    /***
     * 获取含有ellipsizeDrawable累加在省略文字之后的图片文字,仅仅是需要累加的图片文字
     *
     * @param text
     * @return
     */
    private SpannableString getEllipsizeDrawableString(String text) {
        if (ellipsizeDrawable == null) {
            return null;
        }
        ellipsizeDrawable.setBounds(0, 0, ellipsizeDrawable.getMinimumWidth(), ellipsizeDrawable.getMinimumHeight());
        ImageSpan imgSpan = new ImageSpan(ellipsizeDrawable);
        SpannableString spanString = new SpannableString(text);
        spanString.setSpan(imgSpan, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    /***
     * 获取加上ellipsizeDrawable图片之后还剩下的文字，该返回的文字中包含ellipsizeText
     *
     * @param workingText 还没有加入ellipsizeText的文字
     * @return
     */
    private String getEllipsizeTextWithDrawableAndAdditionalText(String workingText) {

        if (ellipsizeDrawableLength <= 0 && additionalEllipsizeWidth <= 0) {
            return workingText + ellipsizeText;
        }
        String[] resultText = getResultTextByAdditionalText(workingText);
        if (resultText == null || resultText.length < 2) {
            return workingText + ellipsizeText;
        }
        String result = resultText[1];
        int width = getWidth() - getPaddingLeft() - getPaddingRight() - ellipsizeDrawableLength - additionalEllipsizeWidth;
        //仅仅处理最后一行的数据,将最后一行的数据加上additionalEllipsizeWidth的空间
        while (createWorkingLayout(result + ellipsizeText, width).getLineCount() > 1) {
            int length = result.length();
            if (length == -1) {
                break;
            }
            result = result.substring(0, length - 2);
        }
        return resultText[0] + result + ellipsizeText;
    }

    /***
     * 将最后一行和其他文字分出来
     *
     * @param workingText
     * @return
     */
    private String[] getResultTextByAdditionalText(String workingText) {
        String[] result = new String[2];
        int lineWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        Layout originalLayout = createWorkingLayout(workingText + ellipsizeText, lineWidth);
        int index = originalLayout.getLineStart(originalLayout.getLineCount() - 1);
        try {
            String preLineString = workingText.substring(0, index);
            String lastLineString = workingText.substring(index + 1, workingText.length() - 1);
            result[0] = preLineString;
            result[1] = lastLineString;
        } catch (IndexOutOfBoundsException e) {
            result = null;
        }
        return result;
    }


    private Layout createWorkingLayout(String workingText) {
        return createWorkingLayout(workingText, getWidth() - getPaddingLeft() - getPaddingRight());
    }

    private Layout createWorkingLayout(String workingText, int width) {
        return new StaticLayout(workingText, getPaint(), width,
                Alignment.ALIGN_NORMAL, lineSpacingMultiplier, lineAdditionalVerPadding, false);
    }

    @Override
    public void setEllipsize(TruncateAt where) {
        // Ellipsize settings are not respected
    }

}
