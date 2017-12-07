package com.junhsue.ksee.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Sasa on 2016/7/28.
 */

public abstract  class MyBaseAdapter<T> extends BaseAdapter {

    protected List<T> mList=new LinkedList<T>();

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void modifyList(List<T> list){
        if(null!=list){
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void cleanList(){
        mList.clear();
    }

    public List<T> getList(){
        return  mList;
    }

    public T getLastListIndex(){
        if(mList.size()==0){
            return null;
        }
        return mList.get(mList.size());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getWrappeView(position,convertView,parent);
    }

    protected  abstract View getWrappeView(int position, View convertView, ViewGroup parent);

}
