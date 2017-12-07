package com.junhsue.ksee;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.net.api.OKHttpDownloadFileImpl;
import com.junhsue.ksee.net.callback.FileRequestCallBack;
import com.junhsue.ksee.utils.MD5Utils;
import com.junhsue.ksee.utils.ToastUtil;
import com.junhsue.ksee.view.ActionSheet;
import com.junhsue.ksee.view.HackyViewPager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Sugar on 17/4/3 in Junhsue.
 */

public class BigPictureActivity extends BaseActivity {

    private Activity mContext;
    private ArrayList<String> sudokuPictureEntityList;
    private int pageLocation;//页面滑动停留的当前位置
    private int currentIndex;//初始化页面的当前索引
    public static final String CURRENT_ITEM = "current_item";
    public static final String PICTURE_LIST = "picture_list";
    private HackyViewPager hvpPager;
    private ImageView imgSave;
    private TextView tvDownloadProgress;
    private LinearLayout llDotLayout;
    private SamplePagerAdapter pagerAdapter;

    @Override
    protected void onReceiveArguments(Bundle extras) {
        initBundle(extras);
    }

    @Override
    protected int setLayoutId() {
        mContext = this;
        return R.layout.activity_big_picture;
    }

    @Override
    protected void onInitilizeView() {
        initLayout();

    }

    private void initBundle(Bundle extras) {

        sudokuPictureEntityList = extras.getStringArrayList(PICTURE_LIST);
        currentIndex = extras.getInt(CURRENT_ITEM, 0);

    }

    /**
     * 初始化UI
     */
    private void initLayout() {
        this.setFinishOnTouchOutside(true);
        hvpPager = (HackyViewPager) findViewById(R.id.hvp_pager);
        llDotLayout = (LinearLayout) findViewById(R.id.ll_dot_layout);
        imgSave = (ImageView) findViewById(R.id.img_save);
        tvDownloadProgress = (TextView) findViewById(R.id.tv_download_progress);

        initPagerLayout();
        initDotLayout(sudokuPictureEntityList.size(), currentIndex);
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showPostReportDialog();
                if (0 <= pageLocation && pageLocation < sudokuPictureEntityList.size()) {
                    String picUrl = sudokuPictureEntityList.get(pageLocation);
                    downloadPicture(picUrl);
                }


            }
        });

    }


    private void downloadPicture(String picUrl) {

        String fileDir = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.BIG_IMAGE_FILE;

        File localFile = new File(fileDir);
        if (!localFile.exists()) {
            localFile.mkdir();
        }

        String pictureName = MD5Utils.getSmall32MD5Str(picUrl);
        final String fileName = pictureName + ".png";

        File tempFile = new File(fileDir, fileName);
        if (tempFile.exists()) {
            ToastUtil.getInstance(mContext).showToast(mContext, "图片已保存到" + Constants.BIG_IMAGE_FILE + "文件夹");
            return;
        }

        OKHttpDownloadFileImpl.getInstance().downloadFile(picUrl, fileDir, fileName, new FileRequestCallBack() {
            @Override
            public void onError(int errorCode, String errorMsg) {

            }

            @Override
            public void onSuccess(File file) {

                //保存图片后发送广播通知更新手机系统图库数据库
                Uri uri = Uri.fromFile(file);
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                tvDownloadProgress.setText("完成");
                tvDownloadProgress.setVisibility(View.GONE);
                ToastUtil.getInstance(mContext).showToast(mContext, "图片已保存到" + Constants.BIG_IMAGE_FILE + "文件夹");

            }

            @Override
            public void inProgress(float progress) {
                tvDownloadProgress.setVisibility(View.VISIBLE);
                int duration = (int) (progress * 100);
                tvDownloadProgress.setText(duration + "%");

            }
        });
    }

    /***
     * 初始化pagerUi
     */
    private void initPagerLayout() {
        pagerAdapter = new SamplePagerAdapter(mContext, sudokuPictureEntityList);
        hvpPager.setAdapter(pagerAdapter);
        hvpPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                refreshDotLayout(position);
                pageLocation = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        hvpPager.setCurrentItem(currentIndex);
    }


    /***
     * 初始化dot的UI
     *
     * @param totalCount
     * @param currentIndex
     */
    private void initDotLayout(int totalCount, int currentIndex) {
        if (totalCount <= 1) {
            llDotLayout.removeAllViews();
            llDotLayout.setVisibility(View.GONE);
            return;
        }
        llDotLayout.setVisibility(View.VISIBLE);
        llDotLayout.removeAllViews();
        for (int i = 0; i < totalCount; i++) {
            boolean isLast = i == totalCount - 1 && i > 1;
            View dotView = (i == currentIndex) ? getDotView(R.drawable.dot_solid_c01_e33c4c_size_16px, isLast)
                    : getDotView(R.drawable.dot_solid_cccccc_size_16px, isLast);
            llDotLayout.addView(dotView);
        }
    }

    /***
     * 刷新dot的UI
     *
     * @param currentIndex
     */
    private void refreshDotLayout(int currentIndex) {
        int dotViewCount = llDotLayout.getChildCount();
        for (int i = 0; i < dotViewCount; i++) {
            int drawableId = (i == currentIndex) ? R.drawable.dot_solid_c01_e33c4c_size_16px
                    : R.drawable.dot_solid_cccccc_size_16px;
            llDotLayout.getChildAt(i).setBackgroundResource(drawableId);
        }
    }


    /***
     * 获取dot的view
     *
     * @param drawableId
     * @param isLastDot
     * @return
     */
    private View getDotView(int drawableId, boolean isLastDot) {
        View dotView = new View(mContext);
        dotView.setBackgroundResource(drawableId);
        int dimen16px = getResources().getDimensionPixelOffset(R.dimen.dimen_16px);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dimen16px, dimen16px);
        if (!isLastDot)
            params.rightMargin = getResources().getDimensionPixelOffset(R.dimen.dimen_20px);
        dotView.setLayoutParams(params);
        return dotView;
    }


    private ActionSheet postReportDialog;

    /**
     * 图片保存弹出窗
     */
    private void showPostReportDialog() {

        postReportDialog = new ActionSheet(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.component_post_report, null);
        postReportDialog.setContentView(view);
        postReportDialog.show();

        LinearLayout llPostBottomShareLayout = (LinearLayout) view.findViewById(R.id.ll_post_bottom_share_layout);//分享
        LinearLayout llPostBottomReportLayout = (LinearLayout) view.findViewById(R.id.ll_post_bottom_report_layout);//举报
        TextView tvPostOrDelete = (TextView) view.findViewById(R.id.tv_post_report_or_delete);
        TextView tvPostBottomCancel = (TextView) view.findViewById(R.id.tv_post_bottom_cancel);//取消
        llPostBottomShareLayout.setVisibility(View.GONE);
        tvPostOrDelete.setText("保存本地");

        llPostBottomReportLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 图片本地保存
                if (postReportDialog != null) {
                    postReportDialog.dismiss();
                }
                ToastUtil.getInstance(mContext).setContent("已保存到本地").setShow();
            }
        });

        tvPostBottomCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postReportDialog.isShowing()) {
                    postReportDialog.dismiss();
                }
            }
        });

    }

    private static class SamplePagerAdapter extends PagerAdapter {
        private List<String> imagePaths;
        private Activity currentActivity;
        private int mChildCount = 0;

        public SamplePagerAdapter(Activity mContext, List<String> length) {
            this.imagePaths = length;
            this.currentActivity = mContext;
        }

        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return imagePaths.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, final int position) {
  /*          MyPhotoView photoView = new MyPhotoView(container.getContext());
            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            //load imageurl
            ImageLoader.getInstance().displayImage(imagePaths.get(position), photoView, ImageLoaderOptions.option(R.drawable.img_default_article));

            photoView.setOnTouchOutsideOfPhoneViewListener(new MyPhotoView.OnTouchOutsideOfPhoneView() {
                @Override
                public void onTouchOutside(MyPhotoView view, boolean isOut) {
                    if (!isOut) {
                        return;
                    }
                    finishActivity();
                }
            });*/
            final PhotoView photoView = new PhotoView(container.getContext());
            photoView.enable();
            photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finishActivity();
                }
            });
            photoView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ToastUtil.getInstance(currentActivity).setContent("长按").setShow();
//                    showPostReportDialog();
                    return true;
                }
            });
            container.addView(photoView);
            Glide.with(currentActivity).load(imagePaths.get(position))
                    .placeholder(currentActivity.getResources().getDrawable(R.drawable.img_loading_default))
                    .diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.2f).into(photoView);

            return photoView;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {
            //当调用notifyDataSetChanged时，让getItemPosition方法人为的返回POSITION_NONE，从而达到强迫viewpager重绘所有item的目的。
            if (mChildCount > 0) {
                mChildCount--;
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

        private void finishActivity() {
            currentActivity.finish();
        }
    }


}
