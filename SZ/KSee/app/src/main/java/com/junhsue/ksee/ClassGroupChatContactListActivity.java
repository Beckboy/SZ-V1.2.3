package com.junhsue.ksee;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

import com.hyphenate.easeui.EaseConstant;
import com.junhsue.ksee.view.EaseContactList;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.dto.ClassGroupChatDetailsDTO;

public class ClassGroupChatContactListActivity extends BaseActivity {

  private EaseTitleBar titlebar;
  private EaseContactList contactlist;

  private ClassGroupChatDetailsDTO persons = new ClassGroupChatDetailsDTO();
  private String groupChatId;

  @Override
  protected void onReceiveArguments(Bundle bundle) {
    groupChatId = bundle.getString(EaseConstant.GROUPCHAT_USER_ID);
    persons.memberlist.clear();
    persons = (ClassGroupChatDetailsDTO) bundle.getSerializable(EaseConstant.GROUPCHAT_USER_LIST);
  }

  @Override
  protected int setLayoutId() {
    return R.layout.act_class_group_chat_contact_list;
  }

  @Override
  protected void onInitilizeView() {

    initView();

    setUpView();

  }

  private void setUpView() {
    titlebar.setLeftLayoutClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }

  private void initView() {
    titlebar = (EaseTitleBar) findViewById(R.id.title_bar);
    titlebar.setTitle("所有人","("+persons.memberlist.size()+")");
    contactlist = (EaseContactList) findViewById(R.id.contact_list);
    contactlist.init(persons.memberlist);
  }

}
