package com.junhsue.ksee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.junhsue.ksee.R;
import com.junhsue.ksee.entity.HomeManagersListEntity;
import com.junhsue.ksee.utils.ScreenWindowUtil;

/**
 * 卡片适配器
 * <p>
 * Created by hunter on 17/9/8
 */

public class HomeManagerTagsAdapter<T extends HomeManagersListEntity> extends MyBaseAdapter<T> {


    private Context mContext;

    private LayoutInflater mLayoutInflater;

    private HomeManagersListEntity.IManagersClickListener mIManagersClickListener;


    public HomeManagerTagsAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemViewType(int position) {
        //
        int pos = (getList().get(position).id);
        return pos;
    }

    @Override
    protected View getWrappeView(int position, View convertView, ViewGroup parent) {

        final HomeManagersListEntity msgEntity = getList().get(position);
        //
        final int type = msgEntity.id;

        switch (type) {

            //初次办学
            case HomeManagersListEntity.IManagerType.NEW_SCHOOL:
            //特级教师
            case HomeManagersListEntity.IManagerType.SUPER_TEACHER:
            //中层管理
            case HomeManagersListEntity.IManagerType.MIDDLE_MANAGER:
            //职业化素养
            case HomeManagersListEntity.IManagerType.PROFESSIONALISM:

                ViewHolderNewSchool viewHolderNewSchool=null;
                final int ty = HomeManagersListEntity.IManagerType.ENGLISH_SCHOOL;
                if(null==convertView || ty == convertView.getId()){
                    convertView=mLayoutInflater.inflate(R.layout.item_manager_new_school,null);
                    viewHolderNewSchool=new ViewHolderNewSchool(convertView);
                    convertView.setTag(viewHolderNewSchool);
                    convertView.setId(type);
                }else{
                    viewHolderNewSchool=(ViewHolderNewSchool) convertView.getTag();
                }
                //标签
                if (null != msgEntity.name) {
                    viewHolderNewSchool.mtvTag.setText(msgEntity.name);
                }
                //左上
                if (null != msgEntity.poster) {
                    setImageView(viewHolderNewSchool.mImgTL, msgEntity.poster);
                }
                if (null != msgEntity.article.get(0) && null != msgEntity.article.get(0).title)
                    viewHolderNewSchool.mTvTLTitle.setText(msgEntity.article.get(0).title);
                //右上
                if (null != msgEntity.tagx.get(0)) {
                    if (null != msgEntity.tagx.get(0).poster)
                        setImageView(viewHolderNewSchool.mImgTR, msgEntity.tagx.get(0).poster);
                    if (null != msgEntity.tagx.get(0).name)
                        viewHolderNewSchool.mTvTRTitle.setText(msgEntity.tagx.get(0).name);
                    if (null != msgEntity.tagx.get(0).description)
                        viewHolderNewSchool.mTvTRDes.setText(msgEntity.tagx.get(0).description);
                }
                //左下
                if (null != msgEntity.tagx.get(1)) {
                    if (null != msgEntity.tagx.get(1).poster)
                        setImageView(viewHolderNewSchool.mImgBL, msgEntity.tagx.get(1).poster);
                    if (null != msgEntity.tagx.get(1).name)
                        viewHolderNewSchool.mTvBLTitle.setText(msgEntity.tagx.get(1).name);
                    if (null != msgEntity.tagx.get(1).description)
                        viewHolderNewSchool.mTvBLDes.setText(msgEntity.tagx.get(1).description);
                }
                //右下
                if (null != msgEntity.tagx.get(3)) {
                    if (null != msgEntity.tagx.get(3).poster)
                        setImageView(viewHolderNewSchool.mImgBR, msgEntity.tagx.get(3).poster);
                    if (null != msgEntity.tagx.get(3).name)
                        viewHolderNewSchool.mTvBRTitle.setText(msgEntity.tagx.get(3).name);
                    if (null != msgEntity.tagx.get(3).description)
                        viewHolderNewSchool.mTvBRDes.setText(msgEntity.tagx.get(3).description);
                }
                //中下
                if (null != msgEntity.tagx.get(2)) {
                    if (null != msgEntity.tagx.get(2).poster)
                        setImageView(viewHolderNewSchool.mImgBM, msgEntity.tagx.get(2).poster);
                    if (null != msgEntity.tagx.get(2).name)
                        viewHolderNewSchool.mTvBMTitle.setText(msgEntity.tagx.get(2).name);
                    if (null != msgEntity.tagx.get(2).description)
                        viewHolderNewSchool.mTvBMDes.setText(msgEntity.tagx.get(2).description);
                }

                //设置图片宽
                setImgWidth(viewHolderNewSchool.mImgTR);
                setImgWidth(viewHolderNewSchool.mImgBR);
                setImgWidth(viewHolderNewSchool.mImgBL);

                //标签的点击事件
                viewHolderNewSchool.mImgTL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null == msgEntity.article.get(0)) return;
                        if (null == msgEntity.article.get(0).id) return;
                        int id = Integer.parseInt(msgEntity.article.get(0).id);
                        mIManagersClickListener.jumpArticlePage(id);
                    }
                });
                viewHolderNewSchool.mImgTR.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null == msgEntity.tagx.get(0)) return;
                        if (null == msgEntity.tagx.get(0).id) return;
                        if (null == msgEntity.tagx.get(0).name){
                            msgEntity.tagx.get(0).name = "";
                        }
                        mIManagersClickListener.jumpTagsListPage(msgEntity.tagx.get(0).id, msgEntity.tagx.get(0).name);
                    }
                });
                viewHolderNewSchool.mImgBL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null == msgEntity.tagx.get(1)) return;
                        if (null == msgEntity.tagx.get(1).id) return;
                        if (null == msgEntity.tagx.get(1).name){
                            msgEntity.tagx.get(1).name = "";
                        }
                        mIManagersClickListener.jumpTagsListPage(msgEntity.tagx.get(1).id, msgEntity.tagx.get(1).name);
                    }
                });
                viewHolderNewSchool.mImgBM.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null == msgEntity.tagx.get(2)) return;
                        if (null == msgEntity.tagx.get(2).id) return;
                        if (null == msgEntity.tagx.get(2).name){
                            msgEntity.tagx.get(2).name = "";
                        }
                        mIManagersClickListener.jumpTagsListPage(msgEntity.tagx.get(2).id, msgEntity.tagx.get(2).name);
                    }
                });
                viewHolderNewSchool.mImgBR.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null == msgEntity.tagx.get(3)) return;
                        if (null == msgEntity.tagx.get(3).id) return;
                        if (null == msgEntity.tagx.get(3).name){
                            msgEntity.tagx.get(3).name = "";
                        }
                        mIManagersClickListener.jumpTagsListPage(msgEntity.tagx.get(3).id, msgEntity.tagx.get(3).name);
                    }
                });

                //设置标签的背景
                switch (type){
                    //初次办学
                    case HomeManagersListEntity.IManagerType.NEW_SCHOOL:
                        viewHolderNewSchool.mtvTag.setBackgroundResource(R.drawable.purple_title);
                        break;
                        //特级教师
                    case HomeManagersListEntity.IManagerType.SUPER_TEACHER:
                        viewHolderNewSchool.mtvTag.setBackgroundResource(R.drawable.origin_title);
                        break;
                        //中层管理
                    case HomeManagersListEntity.IManagerType.MIDDLE_MANAGER:
                        viewHolderNewSchool.mtvTag.setBackgroundResource(R.drawable.green_title);
                        break;
                        //职业化素养
                    case HomeManagersListEntity.IManagerType.PROFESSIONALISM:
                        viewHolderNewSchool.mtvTag.setBackgroundResource(R.drawable.purple_title);
                        break;
                }

                break;

            //英语学校
            case HomeManagersListEntity.IManagerType.ENGLISH_SCHOOL:

                ViewHolderEnglishSchool viewHolderEnglishSchool=null;
                if(null==convertView || type != convertView.getId()){
                    convertView=mLayoutInflater.inflate(R.layout.item_manager_enslish_school,null);
                    viewHolderEnglishSchool=new ViewHolderEnglishSchool(convertView);
                    convertView.setTag(viewHolderEnglishSchool);
                    convertView.setId(type);
                }else{
                    viewHolderEnglishSchool=(ViewHolderEnglishSchool) convertView.getTag();
                }
                //标签
                if (null != msgEntity.name)
                    viewHolderEnglishSchool.mtvTag.setText(msgEntity.name);
                //上
                if (null != msgEntity.article.get(0)) {
                    if (null != msgEntity.article.get(0).poster)
                        setImageView(viewHolderEnglishSchool.mImgTop,msgEntity.article.get(0).poster);
                    if (null != msgEntity.article.get(0).title)
                        viewHolderEnglishSchool.mTvTopTitle.setText(msgEntity.article.get(0).title);
                    if (null != msgEntity.article.get(0).description)
                        viewHolderEnglishSchool.mTvTopDes.setText(msgEntity.article.get(0).description);
                }
                //左下
                if (null != msgEntity.tagx.get(0)) {
                    if (null != msgEntity.tagx.get(0).poster)
                        setImageView(viewHolderEnglishSchool.mImgBL, msgEntity.tagx.get(0).poster);
                    if (null != msgEntity.tagx.get(0).name)
                        viewHolderEnglishSchool.mTvBLTitle.setText(msgEntity.tagx.get(0).name);
                    if (null != msgEntity.tagx.get(0).description)
                        viewHolderEnglishSchool.mTvBLDes.setText(msgEntity.tagx.get(0).description);
                }
                //右下
                if (null != msgEntity.tagx.get(2)) {
                    if (null != msgEntity.tagx.get(2).poster)
                        setImageView(viewHolderEnglishSchool.mImgBR, msgEntity.tagx.get(2).poster);
                    if (null != msgEntity.tagx.get(2).name)
                        viewHolderEnglishSchool.mTvBRTitle.setText(msgEntity.tagx.get(2).name);
                    if (null != msgEntity.tagx.get(2).description)
                        viewHolderEnglishSchool.mTvBRDes.setText(msgEntity.tagx.get(2).description);
                }
                //中下
                if (null != msgEntity.tagx.get(1)) {
                    if (null != msgEntity.tagx.get(1).poster)
                        setImageView(viewHolderEnglishSchool.mImgBM, msgEntity.tagx.get(1).poster);
                    if (null != msgEntity.tagx.get(1).name)
                        viewHolderEnglishSchool.mTvBMTitle.setText(msgEntity.tagx.get(1).name);
                    if (null != msgEntity.tagx.get(1).description)
                        viewHolderEnglishSchool.mTvBMDes.setText(msgEntity.tagx.get(1).description);
                }

                //设置图片宽
                setImgWidth(viewHolderEnglishSchool.mImgBR);
                setImgWidth(viewHolderEnglishSchool.mImgBL);

                //标签的点击事件
                viewHolderEnglishSchool.mRlTop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null == msgEntity.article) return;
                        if (null == msgEntity.article.get(0)) return;
                        if (null == msgEntity.article.get(0).id) return;
                        int id = Integer.parseInt(msgEntity.article.get(0).id);
                        mIManagersClickListener.jumpArticlePage(id);
                    }
                });
                viewHolderEnglishSchool.mImgBL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null == msgEntity.tagx) return;
                        if (null == msgEntity.tagx.get(0)) return;
                        if (null == msgEntity.tagx.get(0).id) return;
                        if (null == msgEntity.tagx.get(0).name){
                            msgEntity.tagx.get(0).name = "";
                        }
                        mIManagersClickListener.jumpTagsListPage(msgEntity.tagx.get(0).id, msgEntity.tagx.get(0).name);
                    }
                });
                viewHolderEnglishSchool.mImgBM.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null == msgEntity.tagx) return;
                        if (null == msgEntity.tagx.get(1)) return;
                        if (null == msgEntity.tagx.get(1).id) return;
                        if (null == msgEntity.tagx.get(1).name){
                            msgEntity.tagx.get(1).name = "";
                        }
                        mIManagersClickListener.jumpTagsListPage(msgEntity.tagx.get(1).id, msgEntity.tagx.get(1).name);
                    }
                });
                viewHolderEnglishSchool.mImgBR.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null == msgEntity.tagx) return;
                        if (null == msgEntity.tagx.get(2)) return;
                        if (null == msgEntity.tagx.get(2).id) return;
                        if (null == msgEntity.tagx.get(2).name){
                            msgEntity.tagx.get(2).name = "";
                        }
                        mIManagersClickListener.jumpTagsListPage(msgEntity.tagx.get(2).id+"", msgEntity.tagx.get(2).name);
                    }
                });

                //设置标签的背景颜色
                viewHolderEnglishSchool.mtvTag.setBackgroundResource(R.drawable.blue_title);
                break;
            default:
                convertView = mLayoutInflater.inflate(R.layout.item_msg_empty, null);
                break;
        }
        return convertView;
    }


    private void setImageView(ImageView view, String url){
        Glide.with(mContext).load(url).error(R.drawable.img_default_course_suject).into(view);
    }

    private void setImgWidth(ImageView view){
        int width = ScreenWindowUtil.getScreenWidth(mContext)/3 - 6;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width,width);
        view.setLayoutParams(params);
    }

    /**
     * 初次办学
     */
    class ViewHolderNewSchool {

        //列表标签
        private TextView mtvTag;

        //左上标签海报
        private ImageView mImgTL;
        //左上标签标题
        private TextView mTvTLTitle;

        //右上标签海报
        private ImageView mImgTR;
        //右上标签标题
        private TextView mTvTRTitle;
        //右上标签描述
        private TextView mTvTRDes;

        //左下标签海报
        private ImageView mImgBL;
        //左下标签标题
        private TextView mTvBLTitle;
        //左下标签描述
        private TextView mTvBLDes;

        //右下标签海报
        private ImageView mImgBR;
        //右下标签标题
        private TextView mTvBRTitle;
        //右下标签描述
        private TextView mTvBRDes;

        //中下标签海报
        private ImageView mImgBM;
        //中下标签标题
        private TextView mTvBMTitle;
        //中下标签描述
        private TextView mTvBMDes;

        public ViewHolderNewSchool(View view){
            //标签
            mtvTag = (TextView) view.findViewById(R.id.tv_tag);
            //左上
            mImgTL = (ImageView) view.findViewById(R.id.img_top_left);
            mTvTLTitle = (TextView) view.findViewById(R.id.tv_top_left_title);
            //右上
            mImgTR = (ImageView) view.findViewById(R.id.img_top_right);
            mTvTRTitle = (TextView) view.findViewById(R.id.tv_top_right_title);
            mTvTRDes = (TextView) view.findViewById(R.id.tv_top_right_des);
            //左下
            mImgBL = (ImageView) view.findViewById(R.id.img_bottom_left);
            mTvBLTitle = (TextView) view.findViewById(R.id.tv_bottom_left_title);
            mTvBLDes = (TextView) view.findViewById(R.id.tv_bottom_left_des);
            //右下
            mImgBR = (ImageView) view.findViewById(R.id.img_bottom_right);
            mTvBRTitle = (TextView) view.findViewById(R.id.tv_bottom_right_title);
            mTvBRDes = (TextView) view.findViewById(R.id.tv_bottom_right_des);
            //中下
            mImgBM = (ImageView) view.findViewById(R.id.img_bottom_middle);
            mTvBMTitle = (TextView) view.findViewById(R.id.tv_bottom_middle_title);
            mTvBMDes = (TextView) view.findViewById(R.id.tv_bottom_middle_des);
        }
    }


    /**
     * 英语学校
     */
    class ViewHolderEnglishSchool {

        //列表标签
        private TextView mtvTag;

        //上标签海报
        private ImageView mImgTop;
        private RelativeLayout mRlTop;
        //上标签标题
        private TextView mTvTopTitle;
        //上标签描述
        private TextView mTvTopDes;

        //左下标签海报
        private ImageView mImgBL;
        //左下标签标题
        private TextView mTvBLTitle;
        //左下标签描述
        private TextView mTvBLDes;

        //右下标签海报
        private ImageView mImgBR;
        //右下标签标题
        private TextView mTvBRTitle;
        //右下标签描述
        private TextView mTvBRDes;

        //中下标签海报
        private ImageView mImgBM;
        //中下标签标题
        private TextView mTvBMTitle;
        //中下标签描述
        private TextView mTvBMDes;

        public ViewHolderEnglishSchool(View view){
            //标签
            mtvTag = (TextView) view.findViewById(R.id.tv_tag);
            //上
            mImgTop = (ImageView) view.findViewById(R.id.img_top);
            mTvTopTitle = (TextView) view.findViewById(R.id.tv_top_title);
            mTvTopDes = (TextView) view.findViewById(R.id.tv_top_des);
            mRlTop = (RelativeLayout) view.findViewById(R.id.rl_top);
            //左下
            mImgBL = (ImageView) view.findViewById(R.id.img_bottom_left);
            mTvBLTitle = (TextView) view.findViewById(R.id.tv_bottom_left_title);
            mTvBLDes = (TextView) view.findViewById(R.id.tv_bottom_left_des);
            //右下
            mImgBR = (ImageView) view.findViewById(R.id.img_bottom_right);
            mTvBRTitle = (TextView) view.findViewById(R.id.tv_bottom_right_title);
            mTvBRDes = (TextView) view.findViewById(R.id.tv_bottom_right_des);
            //中下
            mImgBM = (ImageView) view.findViewById(R.id.img_bottom_middle);
            mTvBMTitle = (TextView) view.findViewById(R.id.tv_bottom_middle_title);
            mTvBMDes = (TextView) view.findViewById(R.id.tv_bottom_middle_des);
        }
    }



    public void setIManagersClickListener(HomeManagersListEntity.IManagersClickListener mIManagersClickListener) {
        this.mIManagersClickListener = mIManagersClickListener;
    }
}
