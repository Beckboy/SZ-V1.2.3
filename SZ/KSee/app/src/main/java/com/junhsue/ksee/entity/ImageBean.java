package com.junhsue.ksee.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 单个文件夹下的图片信息
 */

public class ImageBean implements Parcelable {
    private String path;
    private boolean isSelect = false;
    private boolean isLocal = true;
    private boolean in_qiniu = false;
    private String qiniu_path;

    public static final Creator<ImageBean> CREATOR = new Creator<ImageBean>() {
        @Override
        public ImageBean createFromParcel(Parcel in) {
            ImageBean bean = new ImageBean();
            bean.path = in.readString();
            //1: true  0:false
            bean.isSelect = in.readByte() != 0;
            return bean;
        }

        @Override
        public ImageBean[] newArray(int size) {
            return new ImageBean[size];
        }
    };

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean getIsLocal() {
        return isLocal;
    }

    public void setIsLocal(boolean isLocal) {
        this.isLocal = isLocal;
    }
    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public void setIn_qiniu(boolean in_qiniu) {
        this.in_qiniu = in_qiniu;
    }

    public boolean getIn_qiniu() {
        return in_qiniu;
    }

    public void setQiniu_path(String qiniu_path) {
        this.qiniu_path = qiniu_path;
    }

    public String getQiniu_path() {
        return qiniu_path;
    }

    @Override
    public String toString() {
        return "ImageBean{" +
                "path='" + path + '\'' +
                ", isSelect=" + isSelect +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        //1: true  0:false
        dest.writeByte((byte) (isSelect ? 1 : 0));
    }
}
