package com.junhsue.ksee.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.junhsue.ksee.R;
import com.junhsue.ksee.net.api.SlideDateTimeListener;
import com.junhsue.ksee.view.CustomViewPager;
import com.junhsue.ksee.view.SlideDateTimePicker;

import java.util.Calendar;
import java.util.Date;

public class SlideDateTimeDialogFragment extends DialogFragment implements DateFragment.DateChangedListener {
    public static final String TAG_SLIDE_DATE_TIME_DIALOG_FRAGMENT = "tagSlideDateTimeDialogFragment";

    private static SlideDateTimeListener mListener;

    private Context mContext;
    private CustomViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private View mButtonVerticalDivider;
    private Button mOkButton;
    private Button mCancelButton;
    private Date mInitialDate;
    private int mTheme;
    private int mIndicatorColor;
    private Date mMinDate;
    private Date mMaxDate;
    private Calendar mCalendar;
    private int mDateFlags =
        DateUtils.FORMAT_SHOW_WEEKDAY |
        DateUtils.FORMAT_SHOW_DATE |
        DateUtils.FORMAT_ABBREV_ALL;

    public static SlideDateTimeDialogFragment newInstance(SlideDateTimeListener listener,
                                                          Date initialDate, Date minDate, Date maxDate, int theme, int indicatorColor) {
        mListener = listener;

        // Create a new instance of SlideDateTimeDialogFragment
        SlideDateTimeDialogFragment dialogFragment = new SlideDateTimeDialogFragment();

        // Store the arguments and attach the bundle to the fragment
        Bundle bundle = new Bundle();
        bundle.putSerializable("initialDate", initialDate);
        bundle.putSerializable("minDate", minDate);
        bundle.putSerializable("maxDate", maxDate);
        bundle.putInt("theme", theme);
        bundle.putInt("indicatorColor", indicatorColor);
        dialogFragment.setArguments(bundle);

        // Return the fragment with its bundle
        return dialogFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mContext = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        unpackBundle();

        mCalendar = Calendar.getInstance();
        mCalendar.setTime(mInitialDate);

        switch (mTheme){
            case SlideDateTimePicker.HOLO_DARK:
                setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog_NoActionBar);
                break;
            case SlideDateTimePicker.HOLO_LIGHT:
                setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
                break;
            default:  // if no theme was specified, default to holo light
                setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_slide_date_time_picker, container);

        setupViews(view);
        customizeViews();
        initViewPager();
        initButtons();

        return view;
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }

    private void unpackBundle() {
        Bundle args = getArguments();

        mInitialDate = (Date) args.getSerializable("initialDate");
        mMinDate = (Date) args.getSerializable("minDate");
        mMaxDate = (Date) args.getSerializable("maxDate");
        mTheme = args.getInt("theme");
        mIndicatorColor = args.getInt("indicatorColor");
    }

    private void setupViews(View v) {
        mViewPager = (CustomViewPager) v.findViewById(R.id.viewPager);
        mButtonVerticalDivider = v.findViewById(R.id.buttonVerticalDivider);
        mOkButton = (Button) v.findViewById(R.id.okButton);
        mCancelButton = (Button) v.findViewById(R.id.cancelButton);
    }

    private void customizeViews() {
        int lineColor = mTheme == SlideDateTimePicker.HOLO_DARK ?
                getResources().getColor(R.color.gray_holo_dark) :
                getResources().getColor(R.color.gray_holo_light);

        switch (mTheme) {
            case SlideDateTimePicker.HOLO_LIGHT:
            case SlideDateTimePicker.HOLO_DARK:
                mButtonVerticalDivider.setBackgroundColor(lineColor);
                break;
            default:  // if no theme was specified, default to holo light
                mButtonVerticalDivider.setBackgroundColor(Color.parseColor("#00000000"));
        }

    }

    private void initViewPager() {
        mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);
    }

    private void initButtons() {
        mOkButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v)
            {
            if (mListener == null) {
                throw new NullPointerException(
                        "Listener no longer exists for mOkButton");
            }
            mListener.onDateTimeSet(new Date(mCalendar.getTimeInMillis()));
            dismiss();
            }
        });

        mCancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener == null) {
                    throw new NullPointerException(
                            "Listener no longer exists for mCancelButton");
                }
                mListener.onDateTimeCancel();
                dismiss();
            }
        });
    }

    @Override
    public void onDateChanged(int year, int month, int day) {
        mCalendar.set(year, month, day);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (mListener == null) {
            throw new NullPointerException(
                    "Listener no longer exists in onCancel()");
        }
        mListener.onDateTimeCancel();
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
            case 0:
                DateFragment dateFragment = DateFragment.newInstance(
                    mTheme,
                    mCalendar.get(Calendar.YEAR),
                    mCalendar.get(Calendar.MONTH),
                    mCalendar.get(Calendar.DAY_OF_MONTH),
                    mMinDate,
                    mMaxDate);
                dateFragment.setTargetFragment(SlideDateTimeDialogFragment.this, 100);
                return dateFragment;
            default:
                return null;
            }
        }

        @Override
        public int getCount()
        {
            return 1;
        }
    }
}
