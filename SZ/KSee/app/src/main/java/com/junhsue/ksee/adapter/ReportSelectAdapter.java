package com.junhsue.ksee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.ReportEditActivity;
import com.junhsue.ksee.entity.ReportSelectEntity;

import static com.junhsue.ksee.ReportEditActivity.CURRENT_CHECK_POSITION;

/**
 * 帖子适配器
 * Created by Sugar on 17/10/26.
 */

public class ReportSelectAdapter<T extends ReportSelectEntity> extends MyBaseAdapter<ReportSelectEntity> {
    private Context mContext;
    private LayoutInflater mInflater;
    private RefreshSelectListener refreshSelectListener;

    public ReportSelectAdapter(Context context) {
        this.mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    protected View getWrappeView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_report_select, null);
            viewHolder.tvReportSelectTxt = (TextView) convertView.findViewById(R.id.tv_report_select_txt);
            viewHolder.llReportSelect = (LinearLayout) convertView.findViewById(R.id.ll_report_select);
            viewHolder.ivReportSelect = (ImageView) convertView.findViewById(R.id.iv_report_select);
            viewHolder.bottomDivider = convertView.findViewById(R.id.v_report_bottom_divider);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final ReportSelectEntity reportSelectEntity = mList.get(position);

        viewHolder.llReportSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (refreshSelectListener != null) {
                    refreshSelectListener.refreshSelected(reportSelectEntity, position, v);
                }

            }
        });
        viewHolder.tvReportSelectTxt.setText(reportSelectEntity.name);


        if (ReportEditActivity.CURRENT_CHECK_POSITION >= 0) {
            if (ReportEditActivity.CURRENT_CHECK_POSITION == position) {
                viewHolder.ivReportSelect.setImageResource(R.drawable.icon_anonymous_press);
            } else {
                viewHolder.ivReportSelect.setImageResource(R.drawable.icon_anonymous_normal);
            }
        } else {
            viewHolder.ivReportSelect.setImageResource(R.drawable.icon_anonymous_normal);
        }


        if (position == 0) {
            viewHolder.bottomDivider.setVisibility(View.GONE);
        } else {
            viewHolder.bottomDivider.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    private class ViewHolder {
        TextView tvReportSelectTxt;
        LinearLayout llReportSelect;
        ImageView ivReportSelect;
        View bottomDivider;
    }

    public void setRefreshSelectListener(RefreshSelectListener listener) {
        this.refreshSelectListener = listener;
    }

    public interface RefreshSelectListener {

        void refreshSelected(ReportSelectEntity reportSelectEntity, int position, View v);
    }

}
