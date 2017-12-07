package com.junhsue.ksee;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.junhsue.ksee.common.IBusinessType;
import com.junhsue.ksee.dto.TagListDTO;
import com.junhsue.ksee.entity.TagItem;
import com.junhsue.ksee.net.api.OkHttpILoginImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.utils.StringUtils;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.CancelEditText;
import com.junhsue.ksee.view.FlowLayout;

import java.util.ArrayList;


/**
 * 搜索页
 * Created by Sugar on 17/3/21 in Junhsue.
 */

public class SearchActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;
    private ActionBar actionBar;
    private CancelEditText searchView;//搜索框中的编辑框
    private RelativeLayout mRlBlankPage; //占位图

    private ArrayList<TagItem> tagItemData; //标签数据源
    private FlowLayout mFlow; //标签流列表
    private CheckBox cbTag; //标签
    private String selectedTopic = ""; //标签内容
    private int index; //标签下标

    //搜索的业务类型
    private int business_type = 1;
    //搜索常量
    public final static  String SEARCH_BUSINESS_TYPE = "search business type";
    public final static  String SEARCH_CONTENT_TYPE = "search content type";
    public final static String SEARCH_CONTENT = "search content"; //关键词搜索
    public final static int SEARCH_KEY_WORD = 1; //关键词搜索
    public final static int SEARCH_KEY_TAG = 2; //标签搜索
    public final static String SEARCH_KEY_NAME = "tag_name"; //标签名


    @Override
    protected void onReceiveArguments(Bundle bundle) {
        business_type = bundle.getInt(SEARCH_BUSINESS_TYPE, 1);
    }

    @Override
    protected int setLayoutId() {
        mContext = this;
        return R.layout.act_search;
    }

    @Override
    protected void onInitilizeView() {
        // 默认进入界面弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        actionBar = (ActionBar) findViewById(R.id.ab_search_heard);
        searchView = actionBar.getSearchEditText(); //获取搜索框
        mFlow = (FlowLayout) findViewById(R.id.flowlayout_tags);
        mRlBlankPage = (RelativeLayout) findViewById(R.id.rl_search_blank_page);
        if (business_type == IBusinessType.REALIZE_ARTICLE){
            searchView.setEditTextHintContent("搜索你感兴趣的干货");
            mFlow.setVisibility(View.GONE);
        }else {
            //加载标签列表的数据源信息
            loadData();
            mRlBlankPage.setVisibility(View.GONE);
        }

        setListener();

    }


    /**
     * 加载标签列表的数据源信息
     */
    public void loadData() {
        OkHttpILoginImpl.getInstance().registerGetTags(new RequestCallback<TagListDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(TagListDTO response) {

                if (response.result.size() > 0) {
                    initTags(response.result);
                    tagItemData = response.result;
                }
            }
        });
    }


    /**
     * 更新话题列表的数据
     */
    private void initTags(final ArrayList<TagItem> tagList) {
        for (int i = 0; i < tagList.size(); i++) {
            cbTag = (CheckBox) getLayoutInflater().inflate(R.layout.item_topic_tag, mFlow, false);
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
            lp.leftMargin = 0;
            lp.rightMargin = 40;
            lp.topMargin = 40;
            cbTag.setLayoutParams(lp);
            cbTag.setTag(i);
            cbTag.setId(i);
            cbTag.setText(tagList.get(i).name);
            mFlow.addView(cbTag);

            cbTag.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (StringUtils.isNotBlank(selectedTopic)) {
                            Log.i("tag", index + "");
                            CheckBox cb = (CheckBox) mFlow.getChildAt(index);
                            cb.setChecked(false);
                        }
                        index = Integer.parseInt(buttonView.getTag() + "");
                        buttonView.setTextColor(getResources().getColor(R.color.c_flow_item_normal));
                        selectedTopic = tagList.get(Integer.parseInt(buttonView.getTag().toString().trim())).id + "";
                        startSearchResultActivityByTag();
                    } else {
                        buttonView.setTextColor(getResources().getColor(R.color.c_flow_item_selector));
                        selectedTopic = "";
                        index = -1;
                    }
                }
            });
        }
    }


    @Override
    protected void onResume() {
        StatisticsUtil.getInstance(mContext).onCountPage("1.3.1");//自定义页面统计
        super.onResume();
    }

    private void setListener() {
        actionBar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_btn_search_right:
                startSearchResultActivityByContent();
                StatisticsUtil.getInstance(mContext).onCountActionDot("3.1");//自定义埋点
                break;
        }
    }

    /**
     * 关键词搜索
     * 发送到搜索结果页
     */
    private void startSearchResultActivityByContent() {
        if (StringUtils.isBlank(searchView.getEditTextContent())) {
            Toast.makeText(mContext, "还未输入搜索的问题哦!", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent keyIntent =  new Intent(mContext, SearchResultActivity.class);
        if (business_type == IBusinessType.REALIZE_ARTICLE){
            keyIntent = new Intent(mContext, SearchResultRealizeActivity.class);
        }else {
            keyIntent = new Intent(mContext, SearchResultActivity.class);
            keyIntent.putExtra(SEARCH_CONTENT_TYPE, SEARCH_KEY_WORD);
        }
        keyIntent.putExtra(SEARCH_CONTENT, searchView.getEditTextContent());
        mContext.startActivity(keyIntent);
    }

    /**
     * 标签搜索
     * 发送到搜索结果页
     */
    private void startSearchResultActivityByTag() {
        Intent keyIntent =  new Intent(mContext, SearchResultActivity.class);
        if (business_type == IBusinessType.REALIZE_ARTICLE){
            keyIntent = new Intent(mContext, SearchResultRealizeActivity.class);
        }else {
            keyIntent = new Intent(mContext, SearchResultActivity.class);
            keyIntent.putExtra(SEARCH_CONTENT_TYPE, SEARCH_KEY_TAG);
            keyIntent.putExtra(SEARCH_KEY_NAME, tagItemData.get(index).name);
        }
        keyIntent.putExtra(SEARCH_CONTENT, tagItemData.get(index).id+"");
        mContext.startActivity(keyIntent);
    }
}
