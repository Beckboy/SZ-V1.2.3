package com.junhsue.ksee.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class IntentLaunch {

	/**
	 * 简单页面跳转辅助函数
	 * @param a
	 * @param target
	 */
	public static void launch(Activity a, Class<?> target, Bundle... bd){
		Intent intent = new Intent();
		intent.setClass(a, target);
		if(bd != null && bd.length > 0){
			intent.putExtras(bd[0]);
		}
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		a.startActivity(intent);
	}
	
	/**
	 * 简单页面跳转辅助函数
	 * @param a
	 */
	public static void launchSelf(Activity a, Bundle... bd){
		Intent intent = new Intent();
		intent.setClass(a, a.getClass());
		if(bd != null && bd.length > 0){
			intent.putExtras(bd[0]);
		}
		a.startActivity(intent);
	}
}
