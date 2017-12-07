package com.junhsue.ksee;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.junhsue.ksee.adapter.VideoAdapter;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.VideoDTO;
import com.junhsue.ksee.entity.VideoEntity;
import com.junhsue.ksee.net.api.OKHttpCourseImpl;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.utils.StatisticsUtil;
import com.junhsue.ksee.view.ActionBar;
import com.junhsue.ksee.view.CommonListView;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 视频列表
 * Created by Sugar on 17/8/10.
 */

public class VideoActivity extends BaseActivity implements View.OnClickListener {

    private ActionBar actionBar;
    private ImageView ivVideoBanner;
    private CommonListView lvVideo;
    private PtrClassicFrameLayout frameLayout;
    public Context mContext;
    private VideoAdapter videoAdapter;
    private int currentPage;
    private int pageSize = 10;
    private boolean isMaxPage = false;


    @Override
    protected void onReceiveArguments(Bundle bundle) {

    }

    @Override
    protected int setLayoutId() {
        mContext = this;
        return R.layout.act_video;
    }

    @Override
    protected void onResume() {
        StatisticsUtil.getInstance(mContext).onCountPage("1.4.3");
        super.onResume();
    }

    @Override
    protected void onInitilizeView() {

        actionBar = (ActionBar) findViewById(R.id.ab_video_heard);
        frameLayout = (PtrClassicFrameLayout) findViewById(R.id.ptr_pcfl_video_frameLayout);
        ivVideoBanner = (ImageView) findViewById(R.id.iv_video_banner);
        lvVideo = (CommonListView) findViewById(R.id.clv_video_list);

        videoAdapter = new VideoAdapter(mContext);
        lvVideo.setAdapter(videoAdapter);

        setListener();
        alertLoadingProgress(true);
        loadVideoListFromServer();
    }

    private void setListener() {
        actionBar.setOnClickListener(this);
        frameLayout.setPtrHandler(ptrDefaultHandler2);
        lvVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VideoEntity entity = (VideoEntity) parent.getItemAtPosition(position);
                Intent intent = new Intent(mContext, VideoDetailActivity.class);
                intent.putExtra(Constants.VIDEO_ENTITY, entity);
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
                loadVideoListFromServer();
            }
        }

        @Override
        public void onRefreshBegin(PtrFrameLayout frame) {
            currentPage = 0;
            loadVideoListFromServer();
        }
    };


    /**
     * 加载录播视频列表
     */
    public void loadVideoListFromServer() {
        OKHttpCourseImpl.getInstance().getVideoList(currentPage, pageSize, new RequestCallback<VideoDTO>() {

            @Override
            public void onError(int errorCode, String errorMsg) {
                frameLayout.refreshComplete();
                dismissLoadingDialog();
            }

            @Override
            public void onSuccess(VideoDTO response) {

                frameLayout.refreshComplete();
                dismissLoadingDialog();
                if (currentPage == 0) {
                    //清空dapter数据
                    videoAdapter.cleanList();
                }

                VideoDTO videoDto = response;
                //加入到适配器列表

                if (response != null && response.list.size() > 0) {
                    videoAdapter.modifyList(response.list);
                }
                if (videoDto.list.size() < pageSize) {
                    isMaxPage = true;
                } else {
                    isMaxPage = false;
                }


            }
        });
    }


}
