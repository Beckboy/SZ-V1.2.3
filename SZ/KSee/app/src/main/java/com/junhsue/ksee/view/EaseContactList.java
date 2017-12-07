package com.junhsue.ksee.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hyphenate.easeui.R;
import com.junhsue.ksee.adapter.ClassGroupChatContactListAdapter;
import com.hyphenate.util.HanziToPinyin;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.entity.ClassGroupChatDetailsEntity;
import com.junhsue.ksee.entity.ClassGroupChatDetailsListEnitity;

public class EaseContactList extends RelativeLayout {
    protected static final String TAG = EaseContactList.class.getSimpleName();
    
    protected Context context;
    protected ExpandableListView listView;
    protected ClassGroupChatContactListAdapter adapter;
    protected List<ClassGroupChatDetailsEntity> contacts;
    protected List<ClassGroupChatDetailsListEnitity> contactslist = new ArrayList<>();
    protected EaseSidebar sidebar;
    
    protected int primaryColor;
    protected int primarySize;
    protected boolean showSiderBar;
    protected Drawable initialLetterBg;
    
    static final int MSG_UPDATE_LIST = 0;
    
//    Handler handler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//            case MSG_UPDATE_LIST:
////                if(adapter != null){
////                	adapter.clear();
////                	adapter.addAll(new ArrayList<EaseUser>(contactList));
////                	adapter.notifyDataSetChanged();
////                }
//                break;
//            default:
//                break;
//            }
//            super.handleMessage(msg);
//        }
//    };

    protected int initialLetterColor;

    
    public EaseContactList(Context context) {
        super(context);
        init(context, null);
    }

    public EaseContactList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    
    public EaseContactList(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    
    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EaseContactList);
        primaryColor = ta.getColor(R.styleable.EaseContactList_ctsListPrimaryTextColor, 0);
        primarySize = ta.getDimensionPixelSize(R.styleable.EaseContactList_ctsListPrimaryTextSize, 0);
        showSiderBar = ta.getBoolean(R.styleable.EaseContactList_ctsListShowSiderBar, true);
        initialLetterBg = ta.getDrawable(R.styleable.EaseContactList_ctsListInitialLetterBg);
        initialLetterColor = ta.getColor(R.styleable.EaseContactList_ctsListInitialLetterColor, 0);
        ta.recycle();

        LayoutInflater.from(context).inflate(R.layout.ease_widget_contact_list, this);
        listView = (ExpandableListView)findViewById(R.id.expandableb);
        sidebar = (EaseSidebar) findViewById(R.id.sidebar);
        sidebar.setListView(listView,contactslist);

        //TODO setAdapter
        adapter = new ClassGroupChatContactListAdapter(context);
        adapter.setPrimaryColor(primaryColor).setPrimarySize(primarySize).setInitialLetterBg(initialLetterBg)
            .setInitialLetterColor(initialLetterColor);
        listView.setAdapter(adapter);

        listView.setOnGroupClickListener(onGroupClickListener);
        listView.setOnChildClickListener(onChildClickListener);

        if(!showSiderBar)
            sidebar.setVisibility(View.GONE);

    }
    
    /*
     * init view
     */
    public void init(List<ClassGroupChatDetailsEntity> contactList){
    	this.contacts = contactList;
        Trace.i(contactList.toString());

        setData(contactList);
        
        if(showSiderBar){
            sidebar.setListView(listView,contactslist);
        }
    }

    private void setData(List<ClassGroupChatDetailsEntity> contactList) {
        final String DefaultLetter = "#";

        String letter = DefaultLetter;

        final class GetInitialLetter {
            String getLetter(String name) {
                if (TextUtils.isEmpty(name)) {
                    return DefaultLetter;
                }
                char char0 = name.toLowerCase().charAt(0);
                if (Character.isDigit(char0)) {
                    return DefaultLetter;
                }
                ArrayList<HanziToPinyin.Token> l = HanziToPinyin.getInstance().get(name.substring(0, 1));
                if (l != null && l.size() > 0 && l.get(0).target.length() > 0)
                {
                    HanziToPinyin.Token token = l.get(0);
                    String letter = token.target.substring(0, 1).toUpperCase();
                    char c = letter.charAt(0);
                    if (c < 'A' || c > 'Z') {
                        return DefaultLetter;
                    }
                    return letter;
                }
                return DefaultLetter;
            }
        }

        for (int i = 0; i < contactList.size(); i++) {
            if (!TextUtils.isEmpty(contactList.get(i).nickname)) {
                letter = new GetInitialLetter().getLetter(contactList.get(i).nickname);
            }else if (letter.equals(DefaultLetter) && !TextUtils.isEmpty(contactList.get(i).nickname)) {
                letter = new GetInitialLetter().getLetter(contactList.get(i).nickname);
            }
            setMap(letter,contactList.get(i));
        }
        adapter.modifyList(contactslist);
        expandAll();
    }

    private void setMap(String letter, ClassGroupChatDetailsEntity classGroupChatDetailsEntity) {
        Trace.i("setMap:"+letter);
        for (int i = 0; i < contactslist.size();i++){
            if (contactslist.get(i).english.equals(letter)){
                contactslist.get(i).list.add(classGroupChatDetailsEntity);
                return;
            }
        }
        ClassGroupChatDetailsListEnitity groupList = new ClassGroupChatDetailsListEnitity();
        groupList.english = letter;
        groupList.list.add(classGroupChatDetailsEntity);
        for (int i = 0;i < contactslist.size();i++) {
            char a = letter.charAt(0);
            char c = contactslist.get(i).english.charAt(0);
            if (a<=c) {
                contactslist.add(i,groupList);
                Trace.i("add contactslist:"+groupList.english+":"+groupList.list.toString());
                return;
            }
        }
        contactslist.add(groupList);
        Trace.i("add contactslist:"+groupList.english+":"+groupList.list.toString());
    }

    private void expandAll(){
        List<ClassGroupChatDetailsListEnitity> list=adapter.getList();
        for(int i=0;i<list.size();i++){
            listView.expandGroup(i);
        }

    }


    ExpandableListView.OnGroupClickListener onGroupClickListener =new ExpandableListView.OnGroupClickListener() {
        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            return true;
        }
    };

    ExpandableListView.OnChildClickListener onChildClickListener=new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

            // TODO child item ClickListener
            return false;
        }
    };

    public void filter(CharSequence str) {
//        adapter.getFilter().filter(str);
    }

    public ListView getListView(){
        return listView;
    }

    public void setShowSiderBar(boolean showSiderBar){
        if(showSiderBar){
            sidebar.setVisibility(View.VISIBLE);
        }else{
            sidebar.setVisibility(View.GONE);
        }
    }


}
