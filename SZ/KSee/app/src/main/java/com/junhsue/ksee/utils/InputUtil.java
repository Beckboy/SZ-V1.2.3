package com.junhsue.ksee.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

public class InputUtil {



	//显示软键盘
	public static  void softInputFromWiddow(Activity act){
		InputMethodManager imm = (InputMethodManager) act
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	//隐藏软键盘
	public static void hideSoftInputFromWindow(Activity act) {
		if (null == act || null == act.getCurrentFocus()) {
			return;
		}
		InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), 0);
	}



	/**return 138*****000*/
	public static String getSecureMobileText(String s){
		if (s == null || s.length() < 10) {
			return null;
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(s.substring(0, 3));
		stringBuilder.append("****");
		stringBuilder.append(s.substring(s.length() - 4, s.length()));
		return stringBuilder.toString();
	}


	/**
	 * 判断该输入是否含有表情
	 * @param str
	 * @return
     */
	public static boolean noContainsEmoji(String str) {//真为不含有表情
		int len = str.length();
		for (int i = 0; i < len; i++) {
			if (isEmojiCharacter(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * emoij的匹配
	 * @param codePoint
	 * @return
     */
	private static boolean isEmojiCharacter(char codePoint) {
		return !((codePoint == 0x0) ||
				(codePoint == 0x9) ||
				(codePoint == 0xA) ||
				(codePoint == 0xD) ||
				((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
				((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
				((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
	}
}
