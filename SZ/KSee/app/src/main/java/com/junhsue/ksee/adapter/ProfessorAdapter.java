package com.junhsue.ksee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.entity.ProfessorEntity;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 邀请人员回答适配器
 * Created by Sugar on 17/4/11.
 */

public class ProfessorAdapter<T extends ProfessorEntity> extends MyBaseAdapter<ProfessorEntity> {

    private Context mContext;
    private LayoutInflater mInflater;

    public ProfessorAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    protected View getWrappeView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_profressor, null);
            viewHolder.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            viewHolder.tvNickname = (TextView) convertView.findViewById(R.id.tv_nickname);
            viewHolder.tvOrganization = (TextView) convertView.findViewById(R.id.tv_organization);
            viewHolder.ivSelect = (ImageView) convertView.findViewById(R.id.iv_select);
            viewHolder.rlProfessContentLayout = (RelativeLayout) convertView.findViewById(R.id.rl_profess_content_layout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ProfessorEntity professor = mList.get(position);

        if (null == viewHolder.ivAvatar.getTag() || !viewHolder.ivAvatar.getTag().toString().equals(professor.avatar)) {
            ImageLoader.getInstance().displayImage(professor.avatar, viewHolder.ivAvatar,
                    ImageLoaderOptions.option(R.drawable.img_default_course_suject));
            viewHolder.ivAvatar.setTag(professor.avatar);
        }
        viewHolder.tvNickname.setText(professor.nickname);
        viewHolder.tvOrganization.setText(professor.organization);
        if (professor.isSelected) {
            viewHolder.ivSelect.setVisibility(View.VISIBLE);
            viewHolder.rlProfessContentLayout.setBackgroundResource(R.drawable.bg_ask_professor_select);
        } else {
            viewHolder.ivSelect.setVisibility(View.GONE);
            viewHolder.rlProfessContentLayout.setBackgroundResource(R.drawable.shape_solid_white_corner_14px);
        }
        return convertView;
    }


    private class ViewHolder {

        public ImageView ivAvatar;
        public TextView tvNickname;
        public TextView tvOrganization;
        public ImageView ivSelect;
        public RelativeLayout rlProfessContentLayout;
    }
}
