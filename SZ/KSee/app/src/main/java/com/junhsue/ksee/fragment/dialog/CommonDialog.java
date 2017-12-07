package com.junhsue.ksee.fragment.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.junhsue.ksee.R;
import com.junhsue.ksee.utils.GlideCircleTransformUtil;

/**
 * Created by hunter_J on 2017/6/8.
 */

public class CommonDialog extends ProgressDialog {

    private ImageView img;
    private Context mContext;

    public CommonDialog(Context context) {
        super(context, R.style.common_dialog_style);
        mContext = context;
        this.setCanceledOnTouchOutside(false);
    }

    public CommonDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        this.setCanceledOnTouchOutside(false);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setIndeterminate(true);
        setCancelable(true);
        setContentView(R.layout.common_dialog_loading);
        img = (ImageView) findViewById(R.id.img_common_loading);

        Glide.with(mContext).load(R.drawable.loadingimage).transform(new GlideCircleTransformUtil(mContext,4)).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(img);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }


}
