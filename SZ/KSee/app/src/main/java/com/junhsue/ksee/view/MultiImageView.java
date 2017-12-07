package com.junhsue.ksee.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.Util;
import com.junhsue.ksee.R;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.entity.PhotoInfo;
import com.junhsue.ksee.utils.DensityUtil;
import com.junhsue.ksee.utils.GlideCircleTransformUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by hunter_J on 2017/11/2.
 */

public class MultiImageView extends LinearLayout {

    public static int MAX_WIDTH = 0;

    //照片的Url列表
    private List<PhotoInfo> imageList;

    /** 长度 单位为Pixel **/
    private int pxOneMaxWandH;  //单张图最大允许宽高
    private int pxMoreMaxWandH; //多张图的宽高
    private int pxImagePadding = DensityUtil.dip2px(getContext(), 7); //图片间的间距

    private int MAX_PER_ROW_COUNT = 3; //每行显示最大数

    private LayoutParams onePicPara;
    private LayoutParams morePara, moreParaColumnFirst;
    private LayoutParams rowPara;

    private OnItemClickListener mOnItemClickListener;
    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public MultiImageView(Context context) {
        super(context);
    }

    public MultiImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setList(List<PhotoInfo> lists) throws IllegalArgumentException {
        if(lists==null){
            throw new IllegalArgumentException("imageList is null...");
        }
        imageList = lists;
        if (MAX_WIDTH > 0){
            pxMoreMaxWandH = (MAX_WIDTH - pxImagePadding*2)/3; //解决右侧图片与内容对不齐的问题
            pxOneMaxWandH = MAX_WIDTH * 2 /3 ;
            initImageLayoutParams();
        }
        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MAX_WIDTH == 0){
            int width = measurewWidth(widthMeasureSpec);
            if (width>0){
                MAX_WIDTH = width;
                if (imageList != null && imageList.size() > 0){
                    setList(imageList);
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initImageLayoutParams() {
        int wrap = LayoutParams.WRAP_CONTENT;
        int match = LayoutParams.MATCH_PARENT;

        onePicPara = new LayoutParams(wrap,wrap);

        moreParaColumnFirst = new LayoutParams(pxMoreMaxWandH,pxMoreMaxWandH);
        morePara = new LayoutParams(pxMoreMaxWandH, pxMoreMaxWandH);
        morePara.setMargins(pxImagePadding,0,0,0);

        rowPara = new LayoutParams(match,wrap);
    }


    // 根据imageView的数量初始化不同的View布局,还要为每一个View作点击效果
    private void initView() {
        this.setOrientation(VERTICAL);
        this.removeAllViews();
        if (MAX_WIDTH == 0){
            //为了触发onMeasure()来测量MultiImageView的最大宽度，MultiImageView的宽设置为match_parent
            addView(new View(getContext()));
            return;
        }

        if (imageList == null || imageList.size() == 0){
            return;
        }

        if (imageList.size() == 1){
            addView(createImageView(0, true));
        }else {
            int allCount = imageList.size();
            if (allCount == 4){
                MAX_PER_ROW_COUNT = 2;
            }else {
                MAX_PER_ROW_COUNT = 3;
            }
            int rowCount = allCount / MAX_PER_ROW_COUNT + (allCount % MAX_PER_ROW_COUNT > 0 ? 1 : 0); //行数
            for (int rowCursor = 0; rowCursor < rowCount ; rowCursor++){
                LinearLayout rowLayout = new LinearLayout(getContext());
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);

                rowLayout.setLayoutParams(rowPara);
                if (rowCursor != 0){
                    rowLayout.setPadding(0,pxImagePadding,0,0);
                }

                int columnCount = allCount % MAX_PER_ROW_COUNT == 0 ?
                        MAX_PER_ROW_COUNT : allCount % MAX_PER_ROW_COUNT; //每行的列数
                if (rowCursor != rowCount -1){
                    columnCount = MAX_PER_ROW_COUNT;
                }
                addView(rowLayout);

                int rowOffset = rowCursor * MAX_PER_ROW_COUNT; //行偏
                for (int columnCursor = 0; columnCursor < columnCount; columnCursor++){
                    int position = columnCursor + rowOffset;
                    rowLayout.addView(createImageView(position, true));
                }
            }
        }
    }

    private View createImageView(int position, final boolean isMultiImage) {
        final PhotoInfo photoInfo = imageList.get(position);
        //创建圆角图
        final RoundedImageView imageView = new RoundedImageView(getContext());
        imageView.setCornerRadius(getContext().getResources().getDimension(R.dimen.dimen_10px));
        if (isMultiImage){
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(position % MAX_PER_ROW_COUNT == 0 ? moreParaColumnFirst : morePara);
        }else {
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //imageView.setMaxHeight(pxOneMaxWandH);

            int expectW = photoInfo.w;
            int expectH = photoInfo.h;

            if (expectW == 0 || expectH == 0){
                imageView.setLayoutParams(onePicPara);
            }else {
                int actualW = 0;
                int actualH = 0;
                float scale = ((float) expectH) / ((float) expectW);
                if (expectW > pxOneMaxWandH){
                    actualW = pxMoreMaxWandH;
                    actualH = (int) (actualW * scale);
                }else {
                    actualW = expectW;
                    actualH = expectH;
                }
                imageView.setLayoutParams(new LayoutParams(actualW,actualH));
            }
        }

        imageView.setOnClickListener(new ImageOnClickListener(position));
        imageView.setBackgroundResource(R.drawable.img_loading_default) ;
        imageView.setCornerRadius(DensityUtil.dip2px(getContext(),10));

        if (photoInfo.localPic != 0){
            Glide.with(getContext()).load(photoInfo.localPic).placeholder(R.drawable.img_loading_default).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.2f).into(imageView);
        }else {
            imageView.setId(photoInfo.url.hashCode());
            Glide.with(getContext()).load(photoInfo.url).placeholder(R.drawable.img_loading_default).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.2f).transform(new GlideCircleTransformUtil(getContext(),5))
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                            imageView.setBackgroundResource(0);
                            return false;
                        }
                    }).into(imageView);
        }
        return imageView;
    }


    private int measurewWidth(int widthMeasureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY){
            result = specSize;
        } else {
            if (specMode == MeasureSpec.AT_MOST){
                result = Math.min(result, specSize);
            }
        }
        return result;
    }


    private class ImageOnClickListener implements OnClickListener{

        private int position;

        public ImageOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null){
                mOnItemClickListener.onItemClick(view, position);
            }
        }
    }

    public interface OnItemClickListener{
        public void onItemClick(View view, int position);
    }


}
