package com.zxqy.xunilaidian.utils;

import com.orhanobut.logger.Logger;

/**
 * 
 * @function 统一管理log打印类
 * @author LiuGuojia 2015年5月4日
 * @describe
 */
public class MyLog {

	public static boolean isPrint = Constants.isPrint;

	public static void v(String tag, String msg) {
		if (isPrint) {
			Logger.t(tag).v(msg);
		}
	}

	public static void d(String tag, String msg) {
		if (isPrint) {
			Logger.t(tag).d(msg);
		}
	}

	public static void i(String tag, String msg) {
		if (isPrint) {
			Logger.t(tag).i(msg);
		}
	}

	public static void w(String tag, String msg) {
		if (isPrint) {
			Logger.t(tag).w(msg);
		}
	}

	public static void e(String tag, String msg) {
		if (isPrint) {
			Logger.t(tag).e(msg);
		}
	}

	public static void json(String tag, String msg) {
		if (isPrint) {
			Logger.t(tag).json(msg);
		}
	}

	public static void xml(String tag, String msg) {
		if (isPrint) {
			Logger.t(tag).xml(msg);
		}
	}
}
