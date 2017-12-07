package com.junhsue.ksee.adapter.viewholder;

import android.view.View;
import android.view.ViewStub;

import com.junhsue.ksee.R;
import com.junhsue.ksee.view.MultiImageView;

/**
 * Created by hunter_J on 2017/11/2.
 */

public class ImageViewHolder extends CircleViewHolder {

    /** 图片 **/
    public MultiImageView multiImageView;

    public ImageViewHolder(View itemView) {
        super(itemView, TYPE_IMAGE);
    }

    @Override
    public void initSubView(int viewType, ViewStub viewStub) {
        if (viewStub == null){
            throw new IllegalArgumentException("viewStub is null...");
        }
        viewStub.setLayoutResource(R.layout.viewstub_imgbody);
        View subView = viewStub.inflate();
        MultiImageView multiImageView = (MultiImageView) subView.findViewById(R.id.multiImagView);
        if (multiImageView != null){
            this.multiImageView = multiImageView;
        }
    }
}
