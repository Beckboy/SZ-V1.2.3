package com.junhsue.ksee.frame;

import android.os.Bundle;

/**
 * defines the setting actions for Activity and FragmentActivity
 * 
 * @author liaoralken
 * @date Nov 24, 2014
 */
public interface IBaseScreen {

	/** 从当前上下文启动新界面 */
	 void launchScreen(Class<?> target, Bundle... bd);

	/** 压入管理堆栈 */
	 void addStack();

	/** 弹出管理堆栈 */
	 void popStack();
}
