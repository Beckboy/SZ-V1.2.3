package com.junhsue.ksee.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hunter_J on 2017/11/2.
 */

public abstract class BaseRecycleViewAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected RecyclerViewItemListener itemListener;
    protected List<T> datas = new ArrayList<T>();

    public List<T> getDatas() {
        if (datas== null)
            datas = new ArrayList<T>();
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    public void setItemListener(RecyclerViewItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public interface RecyclerViewItemListener {

        void onItemClick(int position);
        boolean onItemLongClick(int position);

    }

}
