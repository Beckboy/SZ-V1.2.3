package com.junhsue.ksee.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.hyphenate.easeui.widget.photoview.CircleImageView;
import com.junhsue.ksee.R;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.entity.ClassGroupChatDetailsEntity;
import com.junhsue.ksee.entity.ClassGroupChatDetailsListEnitity;
import com.junhsue.ksee.file.ImageLoaderOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hunter_J on 17/5/9.
 */

public class ClassGroupChatContactListAdapter extends BaseExpandableListAdapter {

  protected List<ClassGroupChatDetailsListEnitity> contactlist = new ArrayList<>();
  private Context mContext;
  private LayoutInflater mInflater;

  private ViewHolder mViewHolder;

  private ViewHolderChild mViewHolderChild;

  public ClassGroupChatContactListAdapter(Context mContext) {
    this.mContext = mContext;
    mInflater = LayoutInflater.from(mContext);
  }

  public void modifyList(List<ClassGroupChatDetailsListEnitity> contactlist) {
    if (null != contactlist) {
      this.contactlist.clear();
      this.contactlist.addAll(contactlist);
    }
    notifyDataSetChanged();
  }

  public List<ClassGroupChatDetailsListEnitity> getList() {
    return contactlist;
  }

  public void cleanList() {
    contactlist.clear();
  }

  /**
   * 获取群总人数
   */
  public int getCourseCount() {
    int count = 0;
    for (int i = 0; i < contactlist.size(); i++) {
      List<ClassGroupChatDetailsEntity> courseDate = contactlist.get(i).list;
      count += courseDate.size();
    }
    return count;
  }

  @Override
  public int getGroupCount() {
    return contactlist != null ? contactlist.size(): 0;
  }

  @Override
  public int getChildrenCount(int groupPosition) {
    return contactlist != null ? contactlist.get(groupPosition).list.size() : 0;
  }

  @Override
  public List<ClassGroupChatDetailsEntity> getGroup(int groupPosition) {
    return contactlist.get(groupPosition).list;
  }

  @Override
  public ClassGroupChatDetailsEntity getChild(int groupPosition, int childPosition) {
    return contactlist.get(groupPosition).list.get(childPosition);
  }

  @Override
  public long getGroupId(int groupPosition) {
    return groupPosition;
  }

  @Override
  public long getChildId(int groupPosition, int childPosition) {
    return childPosition;
  }

  @Override
  public boolean hasStableIds() {
    return false;
  }

  @Override
  public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
    if (null == convertView) {
      convertView = mInflater.inflate(R.layout.item_groupchat_contacts, null);
      mViewHolder = new ViewHolder();
      mViewHolder.mTxt = (TextView) convertView.findViewById(R.id.tv_contacts);
      convertView.setTag(mViewHolder);
    } else {
      mViewHolder = (ViewHolder) convertView.getTag();
    }
    if (null != convertView)
      mViewHolder.mTxt.setText(contactlist.get(groupPosition).english);
    return convertView;
    }

  @Override
  public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
    if (null == convertView) {
      convertView = mInflater.inflate(R.layout.item_groupchat_contact_child, null);
      mViewHolderChild = new ViewHolderChild();
      mViewHolderChild.mImg = (CircleImageView) convertView.findViewById(R.id.img_contact);
      mViewHolderChild.mTxtNickName = (TextView) convertView.findViewById(R.id.tv_contact_nickname);
      mViewHolderChild.mTxtOrg = (TextView) convertView.findViewById(R.id.tv_contact_org);
      convertView.setTag(mViewHolderChild);
    } else {
      mViewHolderChild = (ViewHolderChild) convertView.getTag();
    }
    List<ClassGroupChatDetailsEntity> courseDate = new ArrayList<>();
    courseDate.addAll(contactlist.get(groupPosition).list);
    ClassGroupChatDetailsEntity course = courseDate.get(childPosition);
    if (null != course) {
      if(!course.avatar.equals(mViewHolderChild.mImg.getTag())){
        ImageLoader.getInstance().displayImage(course.avatar, mViewHolderChild.mImg,
            ImageLoaderOptions.option(R.drawable.img_default_course_suject));
        mViewHolderChild.mImg.setTag(course.avatar);
      }

      mViewHolderChild.mTxtNickName.setText(course.nickname);
      mViewHolderChild.mTxtOrg.setText(course.org);
    }
//    if(isLastChild){
//      mViewHolderChild.mImgLine.setVisibility(View.GONE);
//    }else
//      mViewHolderChild.mImgLine.setVisibility(View.VISIBLE);
    return convertView;
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return false;
  }

  class ViewHolder {
    public TextView mTxt;//拼音首字母
  }

  class ViewHolderChild {
    public CircleImageView mImg;//用户头像
    public TextView mTxtNickName;//用户名称
    public TextView mTxtOrg;//用户学校/机构
  }

  protected int primaryColor;
  protected int primarySize;
  protected Drawable initialLetterBg;
  protected int initialLetterColor;

  public ClassGroupChatContactListAdapter setPrimaryColor(int primaryColor) {
    this.primaryColor = primaryColor;
    return this;
  }

  public ClassGroupChatContactListAdapter setPrimarySize(int primarySize) {
    this.primarySize = primarySize;
    return this;
  }

  public ClassGroupChatContactListAdapter setInitialLetterBg(Drawable initialLetterBg) {
    this.initialLetterBg = initialLetterBg;
    return this;
  }

  public ClassGroupChatContactListAdapter setInitialLetterColor(int initialLetterColor) {
    this.initialLetterColor = initialLetterColor;
    return this;
  }


}
