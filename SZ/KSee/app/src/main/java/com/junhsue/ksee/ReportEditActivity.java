package com.junhsue.ksee;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.junhsue.ksee.adapter.ReportSelectAdapter;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.ReportSelectDTO;
import com.junhsue.ksee.entity.ReportSelectEntity;
import com.junhsue.ksee.entity.ResultEntity;
import com.junhsue.ksee.net.api.OKHttpNewSocialCircle;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.utils.InputUtil;
import com.junhsue.ksee.utils.StringUtils;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.CommonListView;

import java.util.List;

/**
 * 举报页面
 * Created by Sugar on 17/10/26 in Junhsue.
 */

public class ReportEditActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;
    private CommonListView clvReportList;
    private EditText etReportContent;
    private ActionBar actionBar;
    private RelativeLayout rlReportSend;
    private ReportSelectAdapter reportSelectAdapter;
    public static int CURRENT_CHECK_POSITION = -1;//当前勾选的位置
    private String selectedrePortTypeId = "";//选中选项
    private String contentId = "";// 举报id
    private String businessId = "";//业务id
    private TextView tvTextLimit;


    @Override
    protected void onReceiveArguments(Bundle bundle) {
        contentId = bundle.getString(Constants.REPORT_CONTENT_ID);
        businessId = bundle.getString(Constants.REPORT_BUSINESS_ID);

    }

    @Override
    protected int setLayoutId() {
        mContext = this;
        return R.layout.act_report_edit;
    }

    @Override
    protected void onInitilizeView() {
        actionBar = (ActionBar) findViewById(R.id.ab_report_title);
        clvReportList = (CommonListView) findViewById(R.id.clv_report_select);
        etReportContent = (EditText) findViewById(R.id.et_report_content);
        rlReportSend = (RelativeLayout) findViewById(R.id.rl_report_send);
        tvTextLimit = (TextView) findViewById(R.id.tv_text_limit);
        reportSelectAdapter = new ReportSelectAdapter(mContext);
        clvReportList.setAdapter(reportSelectAdapter);

        actionBar.setBottomDividerVisible(View.GONE);
        // 默认进入界面弹出软键盘为隐藏状态
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setListener();

        loadReportList();
    }

    private void setListener() {
        actionBar.setOnClickListener(this);
        rlReportSend.setOnClickListener(this);
        etReportContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputUtil.softInputFromWiddow((ReportEditActivity) mContext);
                } else {
                    InputUtil.hideSoftInputFromWindow((ReportEditActivity) mContext);
                }
            }
        });

        etReportContent.addTextChangedListener(new TextWatcher() {

            private CharSequence temp;
            private int editStart;
            private int editEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvTextLimit.setText(s.length() + "/100字");
            }

            @Override
            public void afterTextChanged(Editable s) {
                editStart = etReportContent.getSelectionStart();
                editEnd = etReportContent.getSelectionEnd();
                if (temp.length() > 100) {

                    s.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    etReportContent.setText(s);
                    etReportContent.setSelection(tempSelection);
                }
                if (etReportContent.getText().length() >= 100) {
                    ToastUtil.showToast(getApplicationContext(),"你输入的字数已经超过了限制！");
                }
            }
        });

        reportSelectAdapter.setRefreshSelectListener(new ReportSelectAdapter.RefreshSelectListener() {
            @Override
            public void refreshSelected(ReportSelectEntity reportSelectEntity, int position, View v) {

                if (CURRENT_CHECK_POSITION == position) {
                    CURRENT_CHECK_POSITION = -1;
                } else {
                    CURRENT_CHECK_POSITION = position;
                }
                if (CURRENT_CHECK_POSITION >= 0 && CURRENT_CHECK_POSITION < reportSelectAdapter.getList().size()) {
                    //获取优惠券的id
                    ReportSelectEntity entity = (ReportSelectEntity) reportSelectAdapter.getList().get(position);
                    selectedrePortTypeId = entity.id;
                }
                reportSelectAdapter.notifyDataSetChanged();
            }
        });


    }

    /**
     * 加载举报选项
     */
    private void loadReportList() {
        OKHttpNewSocialCircle.getInstance().postReportList(new RequestCallback<ReportSelectDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(ReportSelectDTO response) {
                if (response != null) {
                    reportSelectAdapter.modifyList(response.list);
                }


            }
        });

    }

    /**
     * @param report_type_id
     * @param content_id
     * @param business_id
     * @param content
     */
    private void sendReport(String report_type_id, String content_id, String business_id, String content) {
        OKHttpNewSocialCircle.getInstance().senderReportToServer(report_type_id, content_id, business_id, content, new RequestCallback<ResultEntity>() {


            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(ResultEntity response) {
                Toast.makeText(mContext, "举报发送成功", Toast.LENGTH_LONG).show();
                finish();

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left_layout:
                finish();
                break;
            case R.id.rl_report_send:
                if (StringUtils.isBlank(selectedrePortTypeId) || CURRENT_CHECK_POSITION < 0) {
                    Toast.makeText(mContext, "请选择举报类型", Toast.LENGTH_LONG).show();
                    return;
                }
                if (StringUtils.isBlank(etReportContent.getText().toString().trim())) {
                    Toast.makeText(mContext, "请输入举报内容描述", Toast.LENGTH_LONG).show();
                    return;
                }
                if (CURRENT_CHECK_POSITION >= 0 && CURRENT_CHECK_POSITION < reportSelectAdapter.getList().size()) {
                    sendReport(selectedrePortTypeId, contentId, businessId, etReportContent.getText().toString().trim());
                }
                break;

        }
    }

    @Override
    protected void onDestroy() {
        //重置选项
        CURRENT_CHECK_POSITION = -1;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        //返回前通知上一个页面刷新变动数据
        // 默认进入界面弹出软键盘为隐藏状态
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onBackPressed();
    }

}
