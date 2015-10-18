package com.mu.smarthome.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author Mu
 * @date 2015-8-5下午9:55:20
 * @description SharePrefence 工具�?
 */
public class ShareDataTool {

	/**
	 * 保存登录信息
	 * 
	 * @param context
	 * @param token
	 * @param imUsername
	 * @param imPassword
	 * @return
	 */
	public static boolean SaveInfo(Context context, String token,
			String userId, String imUsername, String imPassword) {
		SharedPreferences sp = context.getSharedPreferences("sp",
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putString("token", token);
		e.putString("userId", userId);
		e.putString("imUsername", imUsername);
		e.putString("imPassword", imPassword);
		return e.commit();
	}

	/**
	 * 保存昵称 头像 大楼
	 * 
	 * @param context
	 * @return
	 */
	public static boolean SaveInfoDetail(Context context, String nickname,
			String icon, String location) {
		SharedPreferences sp = context.getSharedPreferences("sp",
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putString("nickname", nickname);
		e.putString("icon", icon);
		e.putString("location", location);
		return e.commit();
	}

	/**
	 * 获取userId
	 * 
	 * @param context
	 * @return
	 */
	public static String getUserId(Context context) {

		return context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("userId", "");
	}

	/**
	 * 获取昵称
	 * 
	 * @param context
	 * @return
	 */
	public static String getNickname(Context context) {

		return context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("nickname", "");
	}

	/**
	 * 获取头像
	 * 
	 * @param context
	 * @return
	 */
	public static String getIcon(Context context) {

		return context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("icon", "");
	}

	/**
	 * 获取地址
	 * 
	 * @param context
	 * @return
	 */
	public static String getLocation(Context context) {
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("location", "");
	}

	/**
	 * 获取token
	 * 
	 * @param context
	 * @return
	 */
	public static String getToken(Context context) {

		return context.getSharedPreferences("sp", Context.MODE_PRIVATE)

		.getString("token", "");
	}

	/**
	 * 获取imUsername
	 * 
	 * @param context
	 * @return
	 */
	public static String getImUsername(Context context) {
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("imUsername", "");
	}

	/**
	 * 获取imPassword
	 * 
	 * @param context
	 * @return
	 */
	public static String getImPassword(Context context) {
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("imPassword", "");
	}

	/**
	 * 设置用户是否完善信息
	 * 
	 * @param context
	 * @param flag
	 *            0不完�? 1 完善
	 * @return
	 */
	public static boolean SaveFlag(Context context, int flag) {
		SharedPreferences sp = context.getSharedPreferences("sp",
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putInt("flag", flag);
		return e.commit();
	}

	/**
	 * 获取用户是否完善信息
	 * 
	 * @param context
	 * @return
	 */
	public static int getFlag(Context context) {
		// return 1;
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE).getInt(
				"flag", 0);
	}

	/**
	 * 保存未读数量
	 * 
	 * @param context
	 * @param flag
	 *            0不完�? 1 完善
	 * @return
	 */
	public static boolean saveNum(Context context, int num) {
		SharedPreferences sp = context.getSharedPreferences("sp",
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putInt("noreadnum", num);
		return e.commit();
	}

	/**
	 * 获取未读数量
	 * 
	 * @param context
	 * @return
	 */
	public static int getNum(Context context) {
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE).getInt(
				"noreadnum", 0);
	}

	/**
	 * 保存未读时间
	 * 
	 * @param context
	 * @param flag
	 *            0不完�? 1 完善
	 * @return
	 */
	public static boolean saveNoReadTime(Context context, long million) {
		SharedPreferences sp = context.getSharedPreferences("sp",
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putLong("noreadtime", million);
		return e.commit();
	}

	/**
	 * 获取未读时间
	 * 
	 * @param context
	 * @return
	 */
	public static long getNoReadTime(Context context) {
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getLong("noreadtime", 0);
	}

	/**
	 * 保存未获取数�?
	 * 
	 * @param context
	 * @param flag
	 *            0不完�? 1 完善
	 * @return
	 */
	public static boolean saveGetNum(Context context, int num) {
		SharedPreferences sp = context.getSharedPreferences("sp",
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putInt("noget", num);
		return e.commit();
	}

	/**
	 * 获取未获取数�?
	 * 
	 * @param context
	 * @return
	 */
	public static int getGetNum(Context context) {
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE).getInt(
				"noget", 0);
	}

	/**
	 * 保存未获取时�?
	 * 
	 * @param context
	 * @param flag
	 *            0不完�? 1 完善
	 * @return
	 */
	public static boolean saveGetNumTime(Context context, long million) {
		SharedPreferences sp = context.getSharedPreferences("sp",
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putLong("nogettime", million);
		return e.commit();
	}

	/**
	 * 获取未获取时�?
	 * 
	 * @param context
	 * @return
	 */
	public static long getGetNumTime(Context context) {
		return context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getLong("nogettime", 0);
	}

}
