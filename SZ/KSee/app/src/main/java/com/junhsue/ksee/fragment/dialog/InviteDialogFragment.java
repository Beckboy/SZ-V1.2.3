package com.junhsue.ksee.fragment.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.junhsue.ksee.R;

/**
 * Created by Sugar on 17/10/11.
 */

public class InviteDialogFragment extends BaseDialogFragment {

    private InvitationToShareListener invitationToShareListener;

    public static InviteDialogFragment newInstance() {
        InviteDialogFragment inviteDialogFragment = new InviteDialogFragment();
        return inviteDialogFragment;
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
        View view = inflater.inflate(R.layout.component_invite_dialog, null);
        getDialog().setCanceledOnTouchOutside(true);
        initView(view);
        return view;
    }

    private void initView(View view) {
        Button btnNo = (Button) view.findViewById(R.id.btn_no);
        Button btnYes = (Button) view.findViewById(R.id.btn_yes);

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (invitationToShareListener != null) {
                    invitationToShareListener.toShare();
                }
            }
        });
    }


    public interface InvitationToShareListener {
        void toShare();
    }

    public void setInvitationToShareListener(InvitationToShareListener listener) {
        this.invitationToShareListener = listener;
    }

}
