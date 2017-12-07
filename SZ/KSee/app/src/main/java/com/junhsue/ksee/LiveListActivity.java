package com.junhsue.ksee;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.junhsue.ksee.adapter.LiveAdapter;
import com.junhsue.ksee.dto.LiveDTO;
import com.junhsue.ksee.dto.VideoDTO;
import com.junhsue.ksee.entity.LiveEntity;
import com.junhsue.ksee.net.api.OKHttpCourseImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.CommonListView;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 直播列表页
 * Created by Sugar on 17/8/11.
 */

public class LiveListActivity extends BaseActivity implements View.OnClickListener {

    private ActionBar actionBar;
    private PtrClassicFrameLayout frameLayout;
    private ImageView ivliveListBanner;
    private CommonListView clvLiveList;
    private LiveAdapter liveAdapter;
    private Context mContext;
    private int currentPage;
    private boolean isMaxPage;
    private int pageSize = 10;

    @Override
    protected void onReceiveArguments(Bundle bundle) {
    }

    @Override
    protected int setLayoutId() {
        mContext = this;
        return R.layout.act_live_list;
    }

    @Override
    protected void onResume() {
        StatisticsUtil.getInstance(mContext).onCountPage("1.4.1");
        super.onResume();
    }

    @Override
    protected void onInitilizeView() {
        actionBar = (ActionBar) findViewById(R.id.ab_live_list);
        frameLayout = (PtrClassicFrameLayout) findViewById(R.id.ptr_pcfl_live_frameLayout);
        ivliveListBanner = (ImageView) findViewById(R.id.iv_live_list_banner);
        clvLiveList = (CommonListView) findViewById(R.id.clv_live_list);

        liveAdapter = new LiveAdapter(mContext);
        clvLiveList.setAdapter(liveAdapter);

        setListener();
        alertLoadingProgress(true);
        loadLiveListFromServer(currentPage,pageSize);

    }

    private void setListener() {
        actionBar.setOnClickListener(this);
        frameLayout.setPtrHandler(ptrDefaultHandler2);
        clvLiveList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LiveEntity entity = (LiveEntity) parent.getItemAtPosition(position);

                Intent intent = new Intent(mContext, LiveDetailsActivity.class);
                intent.putExtra(LiveDetailsActivity.PARAMS_LIVE_ID, entity.live_id);
                intent.putExtra(LiveDetailsActivity.PARAMS_LIVE_TITLE, entity.title);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left_layout:
                finish();
                break;
        }
    }


    PtrDefaultHandler2 ptrDefaultHandler2 = new PtrDefaultHandler2() {
        @Override
        public void onLoadMoreBegin(PtrFrameLayout frame) {
            if (isMaxPage) {
                Toast.makeText(mContext, getString(R.string.data_load_completed), Toast.LENGTH_SHORT).show();
                frameLayout.refreshComplete();//加载完毕
            } else {
                currentPage++;
                loadLiveListFromServer(currentPage, pageSize);
            }
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            currentPage = 0;
            loadLiveListFromServer(currentPage, pageSize);
        }
    };


    /**
     * 分页加载直播列表数据
     *
     * @param currentPage
     * @param pageSize
     */
    public void loadLiveListFromServer(final int currentPage, final int pageSize) {

        OKHttpCourseImpl.getInstance().getLiveList(currentPage, pageSize, new RequestCallback<LiveDTO>() {
            @Override
            public void onError(int errorCode, String errorMsg) {
                frameLayout.refreshComplete();
                dismissLoadingDialog();
            }

            @Override
            public void onSuccess(LiveDTO response) {
                frameLayout.refreshComplete();
                dismissLoadingDialog();
                if (currentPage == 0) {
                    //清空dapter数据
                    liveAdapter.cleanList();
                }

                LiveDTO liveDto = response;
                //加入到适配器列表

                if (response != null && response.list.size() > 0) {
                    liveAdapter.modifyList(response.list);
                }
                if (response.list.size() < pageSize) {
                    isMaxPage = true;
                } else {
                    isMaxPage = false;
                }
            }
        });

    }


}
