package com.junhsue.ksee.profile;

import android.content.Context;

import com.junhsue.ksee.entity.UserInfo;


/** 2015-10-20
 * clobal user information
 */
public class UserProfileService {

	private static UserProfileService instance;
	private static final String PREF_CURRENT_LOGGED_USER = "current_logged_user";
	private Context context;
	private PrefAccessor mPrefAccessor;
	
	private UserProfileService(Context context) {
		mPrefAccessor=new PrefAccessor(context);
	}

	public static UserProfileService getInstance(Context context) {
		if (null == instance) {
			synchronized (UserProfileService.class) {
				instance = new UserProfileService(context);
			}
		}
		return instance;
	}

	public void updateCurrentLoginUser(UserInfo userInfo) {
		mPrefAccessor.saveObject(PREF_CURRENT_LOGGED_USER, userInfo);
	}

	public UserInfo getCurrentLoginedUser() {
		return (UserInfo)mPrefAccessor.getObject(
				PREF_CURRENT_LOGGED_USER);
	}

}
