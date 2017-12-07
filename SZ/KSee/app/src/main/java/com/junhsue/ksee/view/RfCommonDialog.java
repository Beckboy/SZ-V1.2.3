package com.junhsue.ksee.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.utils.ScreenWindowUtil;
import com.junhsue.ksee.utils.StringUtils;


/**
 * 相对于CommonDialog 的区别就是
 */
public class RfCommonDialog extends Dialog {

    public RfCommonDialog(Context context) {
        super(context);
    }

    public RfCommonDialog(Context context, int theme) {
        super(context, theme);
    }

    protected RfCommonDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private Context mContext;
        private String title;
        private String message;
        private String bottomRemind;
        private int color;
        private int remindColor;
        private int remindBackgroundColor;
        private int closeVisibility = View.GONE;//关闭按钮，默认是gone
        private int messageGravity = Gravity.CENTER_HORIZONTAL;//message内容显示位置，默认水平居中

        private String mPositiveButtonText;//底部按钮的文字显示
        private String mNegativeButtonText;

        private View contentView;
        private RfCommonDialog dialog;

        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;

        private View.OnClickListener onCloseClickListener;

        private LinearLayout topLayout;

        private TextView titleView;

        private LinearLayout messageLayout;

        private TextView messageView;

        private TextView tvBottomRemind;//提示放在内容底部时，可使用该控件


        private ImageView ivClose;

        private Button okBtn;

        //包含取消和确认两个按钮的layout

        private LinearLayout buttonslayout;

        private Button cancelBtn;

        private Button okBtn_right;

        private View dividerView;

        public Builder(Context context) {
            this.mContext = context;
        }


        /**
         * 设置标题
         *
         * @param title 标题
         * @return
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * 设置标题
         *
         * @param title 标题
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) mContext.getText(title);
            setTitle(this.title);
            return this;
        }

        /**
         * 设置副标题
         *
         * @param message 内容
         * @return
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * 设置副标题
         *
         * @param message 内容
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) mContext.getText(message);
            return this;
        }

        /**
         * 设置副标题
         *
         * @param message 消息
         * @param color   颜色 R.color.xx
         * @return
         */
        public Builder setMessage(String message, int color) {
            this.message = message;
            this.color = color;
            return this;
        }

        /**
         * 设置副标题
         *
         * @param message      内容
         * @param color        内容文本颜色
         * @param textLocation 内容文本显示位置
         * @return
         */
        public Builder setMessage(String message, int color, int textLocation) {
            this.message = message;
            this.color = color;
            this.messageGravity = textLocation;
            return this;
        }

        /**
         * 设置副标题
         *
         * @param textLocation 内容文本显示位置
         * @param message      内容
         * @return
         */
        public Builder setMessage(int textLocation, String message) {
            this.message = message;
            this.messageGravity = textLocation;
            return this;
        }

        /**
         * 设置标题
         *
         * @param message 消息
         * @param color   颜色 R.color.xx
         * @return
         */
        public Builder setMessage(int message, int color) {
            this.message = (String) mContext.getText(message);
            this.color = color;
            return this;
        }

        /**
         * 设置底部提示消息，有些对话框的提示不在标题，在内容底部显示
         *
         * @param bottomRemindMessage 内容底部提示信息
         * @return
         */
        public Builder setBottomRemindMessage(int bottomRemindMessage) {
            this.bottomRemind = (String) mContext.getText(bottomRemindMessage);
            return this;
        }

        /**
         * 设置底部提示消息，有些对话框的提示不在标题，在内容底部显示
         *
         * @param bottomRemindMessage 内容底部提示信息
         * @param textColor           提示信息的字体颜色
         * @param backgroundColor     提示信息的背景颜色
         * @return
         */
        public Builder setBottomRemindMessage(String bottomRemindMessage, int textColor, int backgroundColor) {
            this.bottomRemind = bottomRemindMessage;
            this.remindColor = textColor;
            this.remindBackgroundColor = backgroundColor;
            return this;
        }

        /**
         * 设置底部提示消息，有些对话框的提示不在标题，在内容底部显示
         *
         * @param bottomRemindMessage 内容底部提示信息
         * @param textColor           提示信息的字体颜色
         * @param backgroundColor     提示信息的背景颜色
         * @return
         */
        public Builder setBottomRemindMessage(int bottomRemindMessage, int textColor, int backgroundColor) {
            this.bottomRemind = (String) mContext.getText(bottomRemindMessage);
            this.remindColor = textColor;
            this.remindBackgroundColor = backgroundColor;
            return this;
        }

        /**
         * 设置整个背景
         *
         * @param v
         * @return
         */
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setCloseVisibility(int visibility) {
            closeVisibility = visibility;
            return this;
        }

        /**
         * 设置按钮和其点击事件
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                                         OnClickListener listener) {
            this.mPositiveButtonText = (String) mContext
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         OnClickListener listener) {
            this.mPositiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setOnCloseClickListener(View.OnClickListener onCloseClickListener) {
            this.onCloseClickListener = onCloseClickListener;
            return this;
        }


        //设置取消按钮和其事件
        public Builder setNegativeButton(int negativeButtonText,
                                         OnClickListener listener) {
            this.mNegativeButtonText = (String) mContext
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.mNegativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }


        public RfCommonDialog create() {
            dialog = new RfCommonDialog(mContext, R.style.CommonDialog);
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layoutView = inflater.inflate(R.layout.common_dialog_rf, null);
//
            topLayout = (LinearLayout) layoutView.findViewById(R.id.ll_top_layout);
            titleView = (TextView) layoutView.findViewById(R.id.tv_dialog_title);//
            messageLayout = (LinearLayout) layoutView.findViewById(R.id.ll_content_layout);
            messageView = (TextView) layoutView.findViewById(R.id.tv_message);
            tvBottomRemind = (TextView) layoutView.findViewById(R.id.bottom_remind);//提示放在内容底部时，可使用该控件
            ivClose = (ImageView) layoutView.findViewById(R.id.iv_redcommon_dialog_close);
            okBtn = (Button) layoutView.findViewById(R.id.btn_ok);

            //包含取消和确认两个按钮的layout

            buttonslayout = (LinearLayout) layoutView.findViewById(R.id.ll_button_layout);
            cancelBtn = (Button) layoutView.findViewById(R.id.btn_dialog_cancel);//
            okBtn_right = (Button) layoutView.findViewById(R.id.btn_ok_right);
            dividerView = layoutView.findViewById(R.id.vw_divider);

            dialog.addContentView(layoutView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            ViewGroup.LayoutParams params = topLayout.getLayoutParams();
            params.width = (int) (ScreenWindowUtil.getScreenWidth(mContext) * 0.66);
            layoutView.setLayoutParams(params);

            if (!StringUtils.isBlank(this.title)) {
                titleView.setText(this.title);
                titleView.setVisibility(View.VISIBLE);
            } else {
                titleView.setVisibility(View.GONE);
            }
            if (!StringUtils.isBlank(this.message)) {
                messageView.setText(this.message);
                messageLayout.setVisibility(View.VISIBLE);

                LinearLayout.LayoutParams messageLayoutParams = (LinearLayout.LayoutParams) messageLayout.getLayoutParams();
                if (titleView.getVisibility() == View.GONE) {//如果只显示message的情况下，设置message的上边距
                    messageLayoutParams.topMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_40px);
                }
                if (this.messageGravity == Gravity.LEFT) {
                    messageView.setGravity(Gravity.LEFT);
                }

                messageLayoutParams.bottomMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_40px);
                messageLayout.setLayoutParams(messageLayoutParams);

                if (color != 0) {
                    messageView.setTextColor(mContext.getResources().getColor(this.color));
                }
            } else {
                messageLayout.setVisibility(View.GONE);
            }

            if (!StringUtils.isBlank(this.bottomRemind)) {
                tvBottomRemind.setVisibility(View.VISIBLE);
                tvBottomRemind.setText(bottomRemind);
                if (remindColor != 0) {
                    tvBottomRemind.setTextColor(mContext.getResources().getColor(this.remindColor));
                }

                if (remindBackgroundColor != 0) {
                    tvBottomRemind.setBackgroundColor(mContext.getResources().getColor(this.remindBackgroundColor));
                }

            } else {
                tvBottomRemind.setVisibility(View.GONE);
            }


            //如果设置了contentView，而不是设置message
            if (contentView != null) {
                // if no message set add the contentView to the dialog body
                messageLayout.setVisibility(View.VISIBLE);
                messageView.setVisibility(View.GONE);
                topLayout.setBackgroundColor(Color.TRANSPARENT);

                int topPadding = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_20px);
                contentView.setPadding(0, topPadding, 0, 0);
                // messageLayout.removeAllViews();
                messageLayout.addView(contentView,
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            setButtonLayout();
            ivClose.setVisibility(closeVisibility);
            if (onCloseClickListener == null) {
                //设置右上角的关闭按钮点击事件
                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            } else {
                ivClose.setOnClickListener(onCloseClickListener);
            }

            dialog.setContentView(layoutView);

            return dialog;
        }

        /**
         * 根据设置是不是设置Button，来确定是显示几个button
         */
        private void setButtonLayout() {

            //没有任何两个Button
            if (StringUtils.isBlank(mNegativeButtonText) && StringUtils.isBlank(mPositiveButtonText)) {
                buttonslayout.setVisibility(View.GONE);
                okBtn.setVisibility(View.GONE);
                return;
            }

            //取消
            if (StringUtils.isNotBlank(mNegativeButtonText) && StringUtils.isBlank(mPositiveButtonText)) {
                okBtn.setVisibility(View.GONE);
                buttonslayout.setVisibility(View.VISIBLE);

                cancelBtn.setVisibility(View.VISIBLE);
                okBtn_right.setVisibility(View.GONE);
                dividerView.setVisibility(View.GONE);
                buttonslayout.setBackgroundColor(Color.TRANSPARENT);
                cancelBtn.setBackgroundResource(R.drawable.bg_my_dialog_bottom);
                //设置监听事件
                if (StringUtils.isNotBlank(mNegativeButtonText)) {
                    cancelBtn.setText(mNegativeButtonText);
                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (negativeButtonClickListener == null) {
                                dialog.dismiss();
                                return;
                            }
                            negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                    cancelBtn.setVisibility(View.VISIBLE);
                }
                return;
            }
            //Ok
            if (StringUtils.isNotBlank(mPositiveButtonText) && StringUtils.isBlank(mNegativeButtonText)) {

                okBtn.setVisibility(View.VISIBLE);
                buttonslayout.setVisibility(View.GONE);

                if (StringUtils.isNotBlank(mPositiveButtonText)) {
                    okBtn.setText(mPositiveButtonText);
                    okBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (positiveButtonClickListener == null) {
                                dialog.dismiss();
                                return;
                            }
                            positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                    okBtn.setVisibility(View.VISIBLE);
                }
                return;
            }
            //含有两个button
            okBtn.setVisibility(View.GONE);
            buttonslayout.setVisibility(View.VISIBLE);
            buttonslayout.setBackgroundColor(Color.TRANSPARENT);

            //设置监听事件
            if (StringUtils.isNotBlank(mNegativeButtonText)) {
                cancelBtn.setText(mNegativeButtonText);
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (negativeButtonClickListener == null) {
                            dialog.dismiss();
                            return;
                        }
                        negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                    }
                });
                cancelBtn.setVisibility(View.VISIBLE);
            }

            if (StringUtils.isNotBlank(mPositiveButtonText)) {
                okBtn_right.setText(mPositiveButtonText);
                okBtn_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (positiveButtonClickListener == null) {
                            dialog.dismiss();
                            return;
                        }
                        positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                    }
                });
                okBtn_right.setVisibility(View.VISIBLE);
            }
        }
    }

}
