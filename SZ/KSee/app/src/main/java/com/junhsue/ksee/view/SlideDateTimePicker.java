package com.junhsue.ksee.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.junhsue.ksee.fragment.SlideDateTimeDialogFragment;
import com.junhsue.ksee.net.api.SlideDateTimeListener;

import java.util.Date;

public class SlideDateTimePicker {
    public static final int HOLO_DARK = 1;
    public static final int HOLO_LIGHT = 2;

    private FragmentManager mFragmentManager;
    private SlideDateTimeListener mListener;
    private Date mInitialDate;
    private Date mMinDate;
    private Date mMaxDate;
    private int mTheme;
    private int mIndicatorColor;


    public SlideDateTimePicker(FragmentManager fm) {
        // See if there are any DialogFragments from the FragmentManager
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(SlideDateTimeDialogFragment.TAG_SLIDE_DATE_TIME_DIALOG_FRAGMENT);

        // Remove if found
        if (prev != null) {
            ft.remove(prev);
            ft.commit();
        }

        mFragmentManager = fm;
    }

    public void setListener(SlideDateTimeListener listener)
    {
        mListener = listener;
    }

    public void setInitialDate(Date initialDate)
    {
        mInitialDate = initialDate;
    }

    public void setMinDate(Date minDate)
    {
        mMinDate = minDate;
    }

    public void setMaxDate(Date maxDate)
    {
        mMaxDate = maxDate;
    }

    public void setTheme(int theme)
    {
        mTheme = theme;
    }

    public void setIndicatorColor(int indicatorColor)
    {
        mIndicatorColor = indicatorColor;
    }

    public void show() {
        if (mListener == null) {
            throw new NullPointerException(
                    "Attempting to bind null listener to SlideDateTimePicker");
        }

        if (mInitialDate == null) {
            setInitialDate(new Date());
        }

        SlideDateTimeDialogFragment dialogFragment =
                SlideDateTimeDialogFragment.newInstance(
                        mListener,
                        mInitialDate,
                        mMinDate,
                        mMaxDate,
                        mTheme,
                        mIndicatorColor);

        dialogFragment.show(mFragmentManager,
                SlideDateTimeDialogFragment.TAG_SLIDE_DATE_TIME_DIALOG_FRAGMENT);
    }

    public static class Builder {
        // Required
        private FragmentManager fm;
        private SlideDateTimeListener listener;

        // Optional
        private Date initialDate;
        private Date minDate;
        private Date maxDate;
        private int theme;
        private int indicatorColor;

        public Builder(FragmentManager fm)
        {
            this.fm = fm;
        }

        public Builder setListener(SlideDateTimeListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder setInitialDate(Date initialDate) {
            this.initialDate = initialDate;
            return this;
        }

        public Builder setMinDate(Date minDate) {
            this.minDate = minDate;
            return this;
        }

        public Builder setMaxDate(Date maxDate) {
            this.maxDate = maxDate;
            return this;
        }

        public Builder setTheme(int theme) {
            this.theme = theme;
            return this;
        }

        public Builder setIndicatorColor(int indicatorColor) {
            this.indicatorColor = indicatorColor;
            return this;
        }

        public SlideDateTimePicker build() {
            SlideDateTimePicker picker = new SlideDateTimePicker(fm);
            picker.setListener(listener);
            picker.setInitialDate(initialDate);
            picker.setMinDate(minDate);
            picker.setMaxDate(maxDate);
            picker.setTheme(theme);
            picker.setIndicatorColor(indicatorColor);
            return picker;
        }
    }
}
