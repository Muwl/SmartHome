package com.mu.smarthome.utils;

import com.mu.smarthome.model.DeviceEntity;

/**
 * @author Mu
 * @date 2015-10-22 下午5:17:07
 * @Description
 */
public class ConnectionHelper {

	public static final int SYNCAll = 0; // 同步

	public static final int CANCELCONTROL = 1; // 取消控制

	public static final int READ = 2; // 读取

	public static final int OPEN = 3;// 打开

	public static final int CLOSE = 4;// 关闭

	public static final int GATEWAY = 5;// 获取网关

	public static byte[] getCommand(int type, DeviceEntity device) {
		if (type == SYNCAll) {
			byte[] buff = new byte[4];
			buff[0] = 0x10;
			buff[1] = 0x00;
			buff[2] = 0x00;
			buff[3] = 0x00;
			return buff;
		}

		if (type == GATEWAY) {
			byte[] buff = new byte[4];
			buff[0] = 0x03;
			buff[1] = 0x00;
			buff[2] = 0x00;
			buff[3] = 0x00;
			return buff;
		}

		byte[] buff = new byte[44];
		buff[0] = (byte) 0xD0;
		buff[1] = 0;
		buff[2] = 0x28;
		buff[3] = 0;
		buff[4] = (byte) 0xFE;
		buff[5] = (byte) 0xFE;
		buff[42] = (byte) 0xEE;
		buff[43] = (byte) 0xEE;
		if (type == CANCELCONTROL) {
			buff[8] = (byte) 0xFF;
			buff[9] = (byte) 0xFF;
			buff[22] = (byte) 0x12;
			return buff;
		} else {
			if (Constant.TYPE_GANGEDSWITCH.equals(device.type)) {
				String tail = device.longAddress.substring(device.longAddress
						.length() - 1);
				if (tail.equals("A")) {
					if (type == OPEN) {
						buff[22] = 0x10;
					} else if (type == CLOSE) {
						buff[22] = 0x11;
					} else {
						buff[22] = 0x12;
					}
				} else {
					if (type == OPEN) {
						buff[22] = 0x20;
					} else if (type == CLOSE) {
						buff[22] = 0x21;
					} else {
						buff[22] = 0x12;
					}
				}
			} else {
				if (type == OPEN) {
					buff[22] = 0x10;
				} else if (type == CLOSE) {
					buff[22] = 0x11;
				} else {
					buff[22] = 0x12;
				}
			}

			System.arraycopy(hexStringToByte(device.shortAddress), 0, buff, 8,
					2);
			System.arraycopy(hexStringToByte(device.longAddress), 0, buff, 10,
					8);
			return buff;
		}

	}

	public static byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	private static byte toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

}
