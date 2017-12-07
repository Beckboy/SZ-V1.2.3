package com.junhsue.ksee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.entity.QuestionEntity;

/**
 * 社板块的问答首页的精选适配器
 * Created by Sugar on 17/3/20 in Junhsue.
 */
public class SocialCircleGreatestQuestionAdapter<T extends QuestionEntity> extends MyBaseAdapter<T> {
    private Context mContext;
    private LayoutInflater mInflater;

    public SocialCircleGreatestQuestionAdapter(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    protected View getWrappeView(final int position, View convertView, ViewGroup parent) {

        final QuestionEntity entity = (QuestionEntity) mList.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_social_circle_greatest, null);
            viewHolder.vDivider = convertView.findViewById(R.id.v_divider);
            viewHolder.tvGreatestTitle = (TextView) convertView.findViewById(R.id.tv_greatest_title);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (entity != null) {
            viewHolder.tvGreatestTitle.setText(entity.title);
        }

        if (position == 0) {//区别显示分割线
            viewHolder.vDivider.setVisibility(View.GONE);

        } else {
            viewHolder.vDivider.setVisibility(View.VISIBLE);

        }


        return convertView;
    }

    private class ViewHolder {
        public View vDivider;
        public TextView tvGreatestTitle;

    }

}
