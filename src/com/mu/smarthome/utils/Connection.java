package com.mu.smarthome.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.mu.smarthome.model.DeviceEntity;
import com.mu.smarthome.model.GatewayEntity;

/**
 * @author Mu
 * @date 2015-10-22下午8:11:45
 * @description
 */
public class Connection {

	/**
	 * 设置socket并连接
	 * 
	 * @param context
	 * @return
	 */
	public Socket getSocket(Context context) {
		GatewayEntity entity = ShareDataTool.getGateWay(context);
		if (entity == null || ToosUtils.isStringEmpty(entity.ipAddress)) {
			ToastUtils.displayShortToast(context, "请先设置网关");
			return null;
		}
		try {
			Socket client = new Socket();
			InetSocketAddress socketAddress = new InetSocketAddress(
					entity.ipAddress, 8000);
			client.connect(socketAddress, 2000);
			client.setSoTimeout(2000);
			return client;
		} catch (IOException e) {
			e.printStackTrace();
			ToastUtils.displayShortToast(context, "连接失败！");
			return null;
		}

	}

	/**
	 * 发送取消控制
	 * 
	 * @param context
	 */
	public void cancelControl(Context context) {
		Socket client = getSocket(context);
		byte[] bs = ConnectionHelper.getCommand(ConnectionHelper.CANCELCONTROL,
				null);
		try {
			OutputStream writer = client.getOutputStream();
			writer.write(bs, 0, 44);
			writer.flush();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送打开指令
	 * 
	 * @param context
	 */
	public void openDevice(Context context, DeviceEntity deviceEntity) {
		Socket client = getSocket(context);
		byte[] bs = ConnectionHelper.getCommand(ConnectionHelper.OPEN,
				deviceEntity);
		try {
			OutputStream writer = client.getOutputStream();
			writer.write(bs, 0, 44);
			writer.flush();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送关闭指令
	 * 
	 * @param context
	 */
	public void closeDevice(Context context, DeviceEntity deviceEntity) {
		Socket client = getSocket(context);
		byte[] bs = ConnectionHelper.getCommand(ConnectionHelper.CLOSE,
				deviceEntity);
		try {
			OutputStream writer = client.getOutputStream();
			writer.write(bs, 0, 44);
			writer.flush();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查找所有设备并更新
	 * 
	 * @param context
	 */
	public void searchDevices(Context context) {
		Socket client = getSocket(context);
		String retStrArray[] = new String[100];// 最多100个设备

		byte chars[] = new byte[2048];
		int index = 0;
		String tempstr;
		int len = 0;
		try {
			OutputStream writer = client.getOutputStream();
			InputStream reader = client.getInputStream();

			StringBuffer sb = new StringBuffer();

			writer.write(
					ConnectionHelper.getCommand(ConnectionHelper.SYNCAll, null),
					0, 4);
			writer.flush();
			byte tempb[] = new byte[1];
			while ((len = reader.read(tempb, 0, 1)) != -1) {
				if (len == 1) {
					chars[index] = tempb[0];
					index++;
				}
				if (index > 2000)// 最多100个设备
					break;
			}
		} catch (Exception e) {
			int number = (index) / 40;
			for (int l = 0; l < number; l++) {
				byte tempbyte[] = new byte[40];
				System.arraycopy(chars, l * 40, tempbyte, 0, 40);
				tempstr = ZigbeeConn.bytesToHexString(tempbyte).toUpperCase();
				retStrArray[l] = tempstr;
				index++;
			}
			try {
				client.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			syncShortAddr(context, retStrArray);
			return;
		}
		int number = (index) / 40;
		for (int l = 0; l < number; l++) {
			byte tempbyte[] = new byte[40];
			System.arraycopy(chars, l * 40, tempbyte, 0, 40);
			tempstr = ZigbeeConn.bytesToHexString(tempbyte).toUpperCase();
			retStrArray[l] = tempstr;
			index++;
		}
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		syncShortAddr(context, retStrArray);
		return;

	}

	/**
	 * 对获取的数据进行格式话
	 * 
	 * @param context
	 * @param res
	 */

	private void syncShortAddr(Context context, String[] res) {

		if (res == null) {
			return;
		}
		List<DeviceEntity> entities = new ArrayList<DeviceEntity>();
		for (int i = 0; i < res.length; i++) {
			String result = res[i];
			if (((result == null) || result.length() != 80))
				continue;
			String hexDevid = result.substring(12, 28);
			String shrotaddr = result.substring(8, 12);
			String type = result.substring(30, 32);

			DeviceEntity deviceEntity = new DeviceEntity();
			deviceEntity.longAddress = hexDevid;
			deviceEntity.shortAddress = shrotaddr;
			deviceEntity.type = type;
			switch (type) {
			case Constant.TYPE_OUTLET:
				String statue = result.substring(36, 38);
				String engHex = result.substring(38, 44);
				deviceEntity.running = statue.equals("00");
				deviceEntity.currentPower = engHex;
				entities.add(deviceEntity);
				break;
			case Constant.TYPE_INFRARED:
				String statue1 = result.substring(36, 38);
				deviceEntity.running = (statue1.equals("00") || statue1
						.equals("01"));
				deviceEntity.waitting = statue1.equals("01");
				entities.add(deviceEntity);
				break;
			case Constant.TYPE_AIRCONDITIONING:
			case Constant.TYPE_COMBINATIONSWITCH:
			case Constant.TYPE_SINGLESWITCH:
				String statue2 = result.substring(36, 38);
				deviceEntity.running = statue2.equals("00");
				entities.add(deviceEntity);
				break;
			case Constant.TYPE_GANGEDSWITCH:
				String statue3 = result.substring(36, 38);
				String statue4 = result.substring(38, 40);

				DeviceEntity entity = new DeviceEntity();
				entity.longAddress = hexDevid + "A";
				entity.shortAddress = shrotaddr;
				entity.type = type;
				entity.running = statue3.equals("00");
				entities.add(entity);

				DeviceEntity entity1 = new DeviceEntity();
				entity1.longAddress = hexDevid + "B";
				entity1.shortAddress = shrotaddr;
				entity1.type = type;
				entity1.running = statue4.equals("00");
				entities.add(entity1);

				break;

			default:
				break;
			}

		}
		List<DeviceEntity> deviceEntities = ShareDataTool.getDevice(context);
		if (deviceEntities != null) {
			deviceEntities = new ArrayList<DeviceEntity>();
		}

		List<DeviceEntity> addEntities = new ArrayList<DeviceEntity>();
		for (int i = 0; i < entities.size(); i++) {
			boolean flag = false;
			for (int j = 0; j < deviceEntities.size(); j++) {
				if (entities.get(i).longAddress
						.equals(deviceEntities.get(j).longAddress)) {
					flag = true;
					entities.get(i).shortAddress = deviceEntities.get(j).shortAddress;
					entities.get(i).type = deviceEntities.get(j).type;
					entities.get(i).running = deviceEntities.get(j).running;
					entities.get(i).waitting = deviceEntities.get(j).waitting;
					entities.get(i).currentPower = deviceEntities.get(j).currentPower;
				}
			}

			if (!flag) {
				addEntities.add(entities.get(i));
			}
		}
		entities.addAll(addEntities);
		ShareDataTool.SaveDevice(context, entities);

	}

	/**
	 * 查找网关id并保存
	 * 
	 * @param context
	 */
	public void startFindGatewayId(Context context) {
		Socket client = getSocket(context);
		byte chars[] = new byte[2048];
		int index = 0;
		String tempstr;
		int len = 0;
		try {
			OutputStream writer = client.getOutputStream();
			InputStream reader = client.getInputStream();

			StringBuffer sb = new StringBuffer();

			writer.write(
					ConnectionHelper.getCommand(ConnectionHelper.GATEWAY, null),
					0, 4);
			writer.flush();
			byte tempb[] = new byte[1];
			while ((len = reader.read(tempb, 0, 1)) != -1) {
				if (index == 43)// 最多100个设备
				{
					chars[index] = tempb[0];
					break;
				}
				if (len == 1) {
					chars[index] = tempb[0];
					index++;
				}

			}
		} catch (Exception e) {
			return;
		}
		byte tempbyte[] = new byte[43];
		System.arraycopy(chars, 0, tempbyte, 0, 43);
		tempstr = ZigbeeConn.bytesToHexString(tempbyte).toUpperCase();
		String idStr = tempstr.substring(44, 60);
		GatewayEntity entity = ShareDataTool.getGateWay(context);
		if (entity == null) {
			entity = new GatewayEntity();
		}
		entity.identier = idStr;
		ShareDataTool.SaveGateWay(context, entity);
		ShareDataTool.SaveGateId(context, idStr);
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;

	}

}
