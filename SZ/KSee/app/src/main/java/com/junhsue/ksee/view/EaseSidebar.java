package com.junhsue.ksee.view; /**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.hyphenate.easeui.R;
import com.hyphenate.easeui.model.ClassGroupChatDetailsEntity;
import com.hyphenate.util.DensityUtil;
import com.junhsue.ksee.adapter.ClassGroupChatContactListAdapter;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.entity.ClassGroupChatDetailsListEnitity;

import java.util.ArrayList;
import java.util.List;

public class EaseSidebar extends View{
	private Paint paint;
	private TextView header;
	private float height;
	private ExpandableListView mListView;
	private Context context;

	protected List<ClassGroupChatDetailsListEnitity> contactslist = new ArrayList<>();
	private int index;
	private SectionIndexer sectionIndex = null;
	
	public void setListView(ExpandableListView listView,List<ClassGroupChatDetailsListEnitity> contactslist){
		mListView = listView;
		this.contactslist = contactslist;
	}
	

	public EaseSidebar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	private String[] sections; 

	private void init(){
//	    String st = context.getString(R.string.search_new);
        sections= new String[]{"A","B","C","D","E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z","#"};
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.parseColor("#DCBFA5"));
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(DensityUtil.sp2px(context, 13));
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float center = getWidth() / 2;
		height = getHeight() / sections.length;
		for (int i = sections.length - 1; i > -1; i--) {
			canvas.drawText(sections[i], center, height * (i+1), paint);
		}
	}
	
	private int sectionForPoint(float y) {
		int index = (int) (y / height);
		if(index < 0) {
			index = 0;
		}
		if(index > sections.length - 1){
			index = sections.length - 1;
		}
		return index;
	}
	
	private void setHeaderTextAndscroll(MotionEvent event){
//		 if (mListView) {
//		        //check the mListView to avoid NPE. but the mListView shouldn't be null
//		        //need to check the call stack later
//		        return;
//		    }
		String headerString = sections[sectionForPoint(event.getY())];
		header.setText(headerString);

		index = 0;
		char h = headerString.charAt(0);
		if (headerString == "#"){
			Trace.i("#");
			for (int i = 0; i < contactslist.size();i++){
				if (contactslist.get(i).english == "#"){
					mListView.setSelectedGroup(index);
					return;
				}
				index = index + 1;
			}
			mListView.setSelectedGroup(index-1);
			return;
		}
		if (h < 'A' || h > 'Z') {
			return;
		}
		for (int i = 0 ;i < contactslist.size();i++){
			char c = contactslist.get(i).english.charAt(0);
			if (h == c){
				Trace.i(h+"");
				mListView.setSelectedGroup(index);
				return;
			}
			index = index+1;
		}
		
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:{
			if(header == null){
				header = (TextView) ((View)getParent()).findViewById(R.id.floating_header);
			}
			setHeaderTextAndscroll(event);
			header.setVisibility(View.VISIBLE);

			setBackgroundResource(R.drawable.ease_sidebar_background_pressed);
			return true;
		}
		case MotionEvent.ACTION_MOVE:{
			setHeaderTextAndscroll(event);
			return true;
		}
		case MotionEvent.ACTION_UP:
			header.setVisibility(View.INVISIBLE);
			setBackgroundColor(Color.TRANSPARENT);
			return true;
		case MotionEvent.ACTION_CANCEL:
			header.setVisibility(View.INVISIBLE);
			setBackgroundColor(Color.TRANSPARENT);
			return true;
		}
		return super.onTouchEvent(event);
	}

}
