package com.mu.smarthome.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @author Mu
 * @date 2014-11-7
 * @description Toast工具�??
 */
public class ToastUtils {
	// 短Toast
	public static void displayShortToast(Context context, String temp) {
		if (context == null) {
			return;
		}
		Toast.makeText(context, temp, Toast.LENGTH_SHORT).show();
	}

	// 长Toast
	public static void displaLongToast(Context context, String temp) {
		if (context == null) {
			return;
		}
		Toast.makeText(context, temp, Toast.LENGTH_LONG).show();
	}

	// 自定义时间Toast
	public static void displaDefinedToast(Context context, String temp, int m) {
		if (context == null) {
			return;
		}
		Toast.makeText(context, temp, m).show();
	}

	// 网络连接错误Toast
	public static void displayFailureToast(Context context) {
		if (context == null) {
			return;
		}
		Toast.makeText(context, "网络连接失败，请�?查你的网络设�?", Toast.LENGTH_LONG).show();
	}

	// 网络请求失败Toast
	public static void displaySendFailureToast(Context context) {
		if (context == null) {
			return;
		}
		Toast.makeText(context, "网络请求失败，请稍后再试", Toast.LENGTH_LONG).show();
	}

}
