package com.junhsue.ksee;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.dto.TagListDTO;
import com.junhsue.ksee.entity.QuestionEntity;
import com.junhsue.ksee.entity.TagItem;
import com.junhsue.ksee.net.api.OkHttpILoginImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.utils.InputUtil;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.utils.StringUtils;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.FlowLayout;

import java.util.ArrayList;

/**
 * 提问页面
 * Created by Sugar on 17/4/12 in Junhsue.
 */
public class AskActivity extends BaseActivity implements View.OnClickListener {

    private ActionBar actionBar;
    private TextView tvQuestionRemind;
    private EditText etQuestion;
    //    private TextView tvQuestionFloatRemind;
    private Context mContext;
    private TextView tvDescriptionRemind;
    private EditText etDescription;
    private TextView tvDescriptionFloatRemind;
    private TextView tvQuestionTopic;
    private FlowLayout flQuestionTopic;
    private TextView tvQuestionTopicFloat;
    private CheckBox cbTag;
    private TextView mTvQuestion, mTvDescription, mTvTabs;
    private RelativeLayout mRlQuestion, mRlDescription, mRlTabs;
    private String selectedTopic = "";
    private int index;
    /**
     * 输入法管理器
     */
    private InputMethodManager mInputMethodManager;


    private static final int DURATION_TIME = 500;//动画持续时间

    @Override
    protected void onReceiveArguments(Bundle bundle) {

    }

    @Override
    protected int setLayoutId() {
        mContext = this;
        return R.layout.act_ask;
    }

    @Override
    protected void onInitilizeView() {

        //  初始化输入法管理器
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        actionBar = (ActionBar) findViewById(R.id.ab_ask_title);
        tvQuestionRemind = (TextView) findViewById(R.id.tv_question_remind);
        etQuestion = (EditText) findViewById(R.id.et_question);
//        tvQuestionFloatRemind = (TextView) findViewById(R.id.tv_question_float_remind);
        tvDescriptionRemind = (TextView) findViewById(R.id.tv_description_remind);
        etDescription = (EditText) findViewById(R.id.et_description);
//        tvDescriptionFloatRemind = (TextView) findViewById(R.id.tv_description_float_remind);
        tvQuestionTopic = (TextView) findViewById(R.id.tv_question_topic);
        flQuestionTopic = (FlowLayout) findViewById(R.id.fl_topic);
//        tvQuestionTopicFloat = (TextView) findViewById(R.id.tv_question_float_topic);
//        mTvQuestion = (TextView) findViewById(R.id.tv_question);
//        mTvDescription = (TextView) findViewById(R.id.tv_description);
//        mTvTabs = (TextView) findViewById(R.id.tv_tabs);
        mRlQuestion = (RelativeLayout) findViewById(R.id.rl_question_layout);
        mRlDescription = (RelativeLayout) findViewById(R.id.rl_description_layout);
        mRlTabs = (RelativeLayout) findViewById(R.id.rl_tabs_layout);

        actionBar.setBottomDividerVisible(View.GONE);
        // 初始化编辑框焦点状态
        setEditTextFocusStatus(etQuestion, false);
        setEditTextFocusStatus(etDescription, false);

        //加载话题
        loadTopicTags();

        setListener();

    }

    @Override
    protected void onResume() {

        StatisticsUtil.getInstance(mContext).onCountPage("1.3.4");//自定义页面统计
        super.onResume();
    }

    private void setListener() {
        actionBar.setOnClickListener(this);

        etQuestion.setOnClickListener(this);
        etDescription.setOnClickListener(this);
//        tvQuestionFloatRemind.setOnClickListener(this);
//        tvDescriptionFloatRemind.setOnClickListener(this);
//        tvQuestionTopic.setOnClickListener(this);
//        tvQuestionTopicFloat.setOnClickListener(this);

//        mTvQuestion.setOnClickListener(this);
//        mTvTabs.setOnClickListener(this);
//        mTvDescription.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_left_layout:
                finish();
                break;
            case R.id.tv_btn_right:
                startProfessorActivity();
                StatisticsUtil.getInstance(mContext).onCountActionDot("3.7.1");//自定义埋点统计
                break;
//            case R.id.tv_question:
 /*           case R.id.tv_question_float_remind:
           *//* case R.id.et_question:*//*
                mTvQuestion.setVisibility(View.GONE);
                startAnimation(tvQuestionFloatRemind,
                        tvQuestionRemind.getX(),
                        tvQuestionRemind.getY(),
                        tvQuestionRemind,
                        tvQuestionRemind.getX() - tvQuestionFloatRemind.getX(),
                        tvQuestionRemind.getY() - tvQuestionFloatRemind.getY(),
                        etQuestion
                );*/
//                break;
            case R.id.tv_question_topic:
                break;
            case R.id.tv_description:
  /*          case R.id.tv_description_float_remind:
                mTvDescription.setVisibility(View.GONE);
                startAnimation(tvDescriptionFloatRemind,
                        tvDescriptionRemind.getX(),
                        tvDescriptionRemind.getY(),
                        tvDescriptionRemind,
                        tvDescriptionRemind.getX() - tvDescriptionFloatRemind.getX(),
                        tvDescriptionRemind.getY() - tvDescriptionFloatRemind.getY(),
                        etDescription
                );*/
                break;
   /*         case R.id.tv_tabs:
            case R.id.tv_question_float_topic:
                mTvTabs.setVisibility(View.GONE);
                startAnimation(tvQuestionTopicFloat,
                        tvQuestionTopic.getX(),
                        tvQuestionTopic.getY(),
                        tvQuestionTopic,
                        tvQuestionTopic.getX() - tvQuestionTopicFloat.getX(),
                        tvQuestionTopic.getY() - tvQuestionTopicFloat.getY(),
                        null
                );
                break;*/

            case R.id.et_question:
                setEditTextFocusStatus(etQuestion, true);
                break;
            case R.id.et_description:
                setEditTextFocusStatus(etDescription, true);

                break;
        }

    }


    /**
     * 启动邀请人页面
     */
    public void startProfessorActivity() {
        QuestionEntity question = new QuestionEntity();

        if (StringUtils.isBlank(etQuestion.getText().toString().trim())) {
            Toast.makeText(mContext, "未编辑问题", Toast.LENGTH_SHORT).show();
            return;
        }

        if (etQuestion.getText().toString().trim().length() <= 2) {
            Toast.makeText(mContext, "问题字数至少3个字", Toast.LENGTH_SHORT).show();
            return;
        }
        question.title = etQuestion.getText().toString().trim();

        if (StringUtils.isBlank(etDescription.getText().toString().trim())) {
            Toast.makeText(mContext, "未编辑描述", Toast.LENGTH_SHORT).show();
            return;
        }

        question.content = etDescription.getText().toString().trim();

        if (StringUtils.isBlank(selectedTopic)) {
            Toast.makeText(mContext, "话题未选择哦", Toast.LENGTH_SHORT).show();
            return;
        }
        question.fromtopic = selectedTopic;

        Intent intent = new Intent(mContext, ProfessorActivity.class);
        intent.putExtra("question", question);
        startActivityForResult(intent, Constants.PROFESSOR_RESULT);


    }

    /**
     * @param startView 启动控件
     * @param startX    启动控件的
     * @param startY
     * @param endView   终点控件
     * @param endX
     * @param endY
     */
    private void startAnimation(final TextView startView, float startX, float startY, final TextView endView, float endX, float endY, final EditText editText) {

        // Log.e("Animation:", "====startView:" + "==startX:" + startX + "==startY:" + startY + "==endView:" + "==endX:" + endX + "==endY:" + endY);

        AnimationSet animationSet = new AnimationSet(true);

        TranslateAnimation translateAnimation = new TranslateAnimation(startX, endX, startY, endY);
        translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        translateAnimation.setDuration(DURATION_TIME);

//        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0.4f, 1f, 0.4f, Animation.ABSOLUTE, endX, Animation.ABSOLUTE, endY);
//        scaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
//        scaleAnimation.setDuration(DURATION_TIME);

        //添加动画的顺序不可以改变，否则动画效果也跟着改变（移动-缩放）
        animationSet.addAnimation(translateAnimation);
//        animationSet.addAnimation(scaleAnimation);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startView.setVisibility(View.GONE);
                endView.setVisibility(View.VISIBLE);
                if (editText != null) {
                    setEditTextFocusStatus(editText, true);
                } else {
                    flQuestionTopic.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startView.startAnimation(animationSet);
    }

    /**
     * 加载话题列表
     */
    public void loadTopicTags() {
        OkHttpILoginImpl.getInstance().registerGetTags(new RequestCallback<TagListDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(TagListDTO response) {

                if (response.result.size() > 0) {
                    initTags(response.result);
                }
            }
        });
    }


    /**
     * 更新话题列表的数据
     */
    private void initTags(final ArrayList<TagItem> tagList) {
        for (int i = 0; i < tagList.size(); i++) {
            cbTag = (CheckBox) getLayoutInflater().inflate(R.layout.item_topic_tag, flQuestionTopic, false);
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
            lp.leftMargin = 0;
            lp.rightMargin = 40;
            lp.topMargin = 40;
            cbTag.setLayoutParams(lp);
            cbTag.setTag(i);
            cbTag.setId(i);
            cbTag.setText(tagList.get(i).name);
            flQuestionTopic.addView(cbTag);

            cbTag.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (StringUtils.isNotBlank(selectedTopic)) {
                            Log.i("tag", index + "");
                            CheckBox cb = (CheckBox) flQuestionTopic.getChildAt(index);
                            cb.setChecked(false);
                        }
                        index = Integer.parseInt(buttonView.getTag() + "");
                        buttonView.setTextColor(getResources().getColor(R.color.c_flow_item_normal));
                        selectedTopic = tagList.get(Integer.parseInt(buttonView.getTag().toString().trim())).id + "";
                    } else {
                        buttonView.setTextColor(getResources().getColor(R.color.c_flow_item_selector));
                        selectedTopic = "";
                        index = -1;
                    }

                }
            });
        }
    }


    /**
     * 设置编辑框的焦点状态
     *
     * @param editText 编辑框
     * @param isFocus  是否聚焦
     */
    public void setEditTextFocusStatus(EditText editText, boolean isFocus) {
        if (isFocus) {
            editText.setFocusable(true);//设置输入框可聚集
            editText.setFocusableInTouchMode(true);//设置触摸聚焦
            editText.requestFocus();//请求焦点
            editText.findFocus();
            mInputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);// 显示输入法
        } else {
            editText.setFocusable(false);
            if (mInputMethodManager.isActive()) {
                mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);// 隐藏输入法
            }

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        Bundle bundle = data.getExtras();
        if (bundle == null) {
            return;
        }
        if (requestCode != Constants.PROFESSOR_RESULT) {
            return;
        }
        boolean isFinish = bundle.getBoolean(Constants.IS_FINISH, false);
        if (isFinish) {
            finish();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        InputUtil.hideSoftInputFromWindow(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
