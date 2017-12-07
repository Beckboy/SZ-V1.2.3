package com.junhsue.ksee.view.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.junhsue.ksee.R;
import com.junhsue.ksee.entity.CircleEntity;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.junhsue.ksee.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 首页菜单改版
 * Created by longer on 17/11/6.
 */

public class HomeTabViewHelper {



    public static  LinearLayout getTagView(Context context, CircleEntity circleEntity){
        LinearLayout ll=new LinearLayout(context);
        ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        return  ll;

    }

    public static View getItemView(Context context, String name, String url) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_home_tab, null);
        TextView txtName = (TextView) view.findViewById(R.id.txt_name);
        //
        txtName.setText(name);
        //
        CircleImageView circleImg = (CircleImageView) view.findViewById(R.id.img);
        ImageLoader.getInstance().displayImage(url,circleImg,
                ImageLoaderOptions.option(R.drawable.img_default_course_suject));

        return view;
    }

}
