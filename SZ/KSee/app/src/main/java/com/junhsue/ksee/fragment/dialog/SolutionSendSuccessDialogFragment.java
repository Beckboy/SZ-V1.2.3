package com.junhsue.ksee.fragment.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.junhsue.ksee.R;

/**
 * 方案包兑换成功页
 * Created by longer on 17/9/27.
 */

public class SolutionSendSuccessDialogFragment extends BaseDialogFragment {

    private ImageButton mImgBtnSumbit;


    private IDialogListener mIDialogListener;
    public interface  IDialogListener{

        void onConfirm();
    }

    public static SolutionSendSuccessDialogFragment newInstance(){
        SolutionSendSuccessDialogFragment solutionSendSuccessDialogFragment=new SolutionSendSuccessDialogFragment();
        return solutionSendSuccessDialogFragment;
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
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_live_apply_success, null);

        initilizeView(view);
        return view;
    }


    private void initilizeView(View view){
        mImgBtnSumbit=(ImageButton)view.findViewById(R.id.imgBtn_submit);
        mImgBtnSumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
                if(null==mIDialogListener){
                    mIDialogListener.onConfirm();
                }
            }
        });
    }

    public void setIDialogListener(IDialogListener mIDialogListener) {
        this.mIDialogListener = mIDialogListener;
    }
}
