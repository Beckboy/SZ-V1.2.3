package com.junhsue.ksee.frame;

import com.junhsue.ksee.BaseActivity;

import java.util.Stack;

public class ScreenManager {
	private static Stack<BaseActivity> activityStack;
	private static ScreenManager instance;

	private ScreenManager() {

	}

	public static ScreenManager getScreenManager() {
		if (instance == null) {
			instance = new ScreenManager();
		}
		return instance;
	}

	// 退出栈顶Activity
	public void popActivity(BaseActivity activity) {
		if (activity != null) {
			activity.finish();
			activityStack.remove(activity);
			activity = null;
		}
	}

	// 获得当前栈顶Activity
	public BaseActivity currentActivity() {
		if (0 < activityStack.size()) {
			BaseActivity activity = (BaseActivity) activityStack.lastElement();
			return activity;
		} else {
			return null;
		}
	}

	// 将当前Activity推入栈中
	public void pushActivity(BaseActivity activity) {
		if (activityStack == null) {
			activityStack = new Stack<BaseActivity>();
		}
		activityStack.add(activity);
	}
	
	// 退出栈中所有的Activity
	public void popAllActivity() {
		while (true) {
			BaseActivity activity = currentActivity();
			if (activity == null) {
				break;
			}
			popActivity(activity);
		}
	}

	// 获取栈中所有的Activity
	public Stack<BaseActivity> getAllActivity() {
		if (activityStack != null){
			return activityStack;
		}
		return null;
	}
}
