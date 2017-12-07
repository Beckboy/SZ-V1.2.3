package com.junhsue.ksee.fragment.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.junhsue.ksee.R;
import com.junhsue.ksee.common.Constants;
import com.junhsue.ksee.fragment.BaseFragment;
import com.junhsue.ksee.utils.SharedPreferencesUtils;

/**
 * 抵扣券再次发送有效
 * Created by longer on 17/10/5.
 */

public class SolutionSendDialogFragment extends BaseDialogFragment {


    private Button mImgBtnSumbit;

    private Context mContext=getActivity();

    private SolutionSendSuccessDialogFragment.IDialogListener mIDialogListener;


    public static SolutionSendDialogFragment newInstance(){
        SolutionSendDialogFragment solutionSendDialogFragment=new SolutionSendDialogFragment();
        return solutionSendDialogFragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.common_dialog_style);
    }



    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setGravity(Gravity.CENTER);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_solution_send, null);

        initilizeView(view);
        return view;
    }


    private void initilizeView(View view){
        EditText editText=(EditText)view.findViewById(R.id.txt_send_email);
        String emailLocal= SharedPreferencesUtils.getInstance(getActivity()).getString(Constants.SF_KEY_EMAIL,"");
        editText.setText(emailLocal);
        mImgBtnSumbit=(Button)view.findViewById(R.id.imgBtn_submit);
        mImgBtnSumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
                if(null!=mIDialogListener){
                    mIDialogListener.onConfirm();
                }
            }
        });
    }

    public void setIDialogListener(SolutionSendSuccessDialogFragment.IDialogListener mIDialogListener) {
        this.mIDialogListener = mIDialogListener;
    }
}
