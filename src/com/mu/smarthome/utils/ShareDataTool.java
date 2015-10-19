package com.mu.smarthome.utils;

import java.lang.reflect.Type;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mu.smarthome.model.DeviceEntity;
import com.mu.smarthome.model.GatewayEntity;
import com.mu.smarthome.model.RoomEntity;

/**
 * @author Mu
 * @date 2015-8-5下午9:55:20
 * @description SharePrefence 工具�?
 */
public class ShareDataTool {

	/**
	 * 保存网关Id
	 * 
	 * @param context
	 * @return
	 */
	public static boolean SaveGateId(Context context, String gateId) {
		SharedPreferences sp = context.getSharedPreferences("sp",
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putString("gateId", gateId);
		return e.commit();
	}

	/**
	 * 获取网关Id
	 * 
	 * @param context
	 * @return
	 */
	public static String getGateId(Context context) {

		return context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("gateId", "");
	}

	/**
	 * 保存房间列表
	 * 
	 * @param context
	 * @return
	 */
	public static boolean SaveRooms(Context context, List<RoomEntity> entities) {
		SharedPreferences sp = context.getSharedPreferences("sp",
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		Gson gson = new Gson();
		e.putString("rooms", gson.toJson(entities));
		return e.commit();
	}

	/**
	 * 获取房间列表
	 * 
	 * @param context
	 * @return
	 */
	public static List<RoomEntity> getRooms(Context context) {
		String s = context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("rooms", "");
		Gson gson = new Gson();
		Type mySuperClass = new TypeToken<List<RoomEntity>>() {
		}.getType();
		List<RoomEntity> entities = gson.fromJson(s, mySuperClass);
		return entities;
	}

	/**
	 * 保存设备信息
	 * 
	 * @param context
	 * @return
	 */
	public static boolean SaveDevice(Context context,
			List<DeviceEntity> entities) {
		SharedPreferences sp = context.getSharedPreferences("sp",
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		Gson gson = new Gson();
		e.putString("devices", gson.toJson(entities));
		return e.commit();
	}

	/**
	 * 获取设备信息
	 * 
	 * @param context
	 * @return
	 */
	public static List<DeviceEntity> getDevice(Context context) {
		String s = context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("devices", "");
		Gson gson = new Gson();
		Type mySuperClass = new TypeToken<List<DeviceEntity>>() {
		}.getType();
		List<DeviceEntity> entities = gson.fromJson(s, mySuperClass);
		return entities;
	}

	/**
	 * 保存网关配置
	 * 
	 * @param context
	 * @return
	 */
	public static boolean SaveGateWay(Context context, GatewayEntity entity) {
		SharedPreferences sp = context.getSharedPreferences("sp",
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		Gson gson = new Gson();
		e.putString("gateway", gson.toJson(entity));
		return e.commit();
	}

	/**
	 * 获取网关配置
	 * 
	 * @param context
	 * @return
	 */
	public static GatewayEntity getGateWay(Context context) {
		String s = context.getSharedPreferences("sp", Context.MODE_PRIVATE)
				.getString("gateway", "");
		Gson gson = new Gson();
		GatewayEntity entity = gson.fromJson(s, GatewayEntity.class);
		return entity;
	}
}
