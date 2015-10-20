package com.mu.smarthome.utils;

import java.util.HashMap;

import android.content.Context;

public class PowerUtils {
	public static double getPEDouble(String pHexString) {
		byte[] b = hexStringToBytes(pHexString);
		long i = ((b[0] << 16) & 0x00FF0000) + ((b[1] << 8) & 0x0000FF00)
				+ ((b[2]) & 0x000000FF);
		// double fff = (Math.pow(2,-16)+
		// Math.pow(2,-18)+Math.pow(2,-22)+Math.pow(2,-23));
		double E = 0;
		if ((i & 0x00800000) != 0) {
			E = 0;
		} else {
			for (int bi = 22; bi >= 0; bi--) {
				long ll = 1 << bi;
				long k = i & ll;

				if (k != 0) {
					E = E + Math.pow(2, (0 - (23 - bi)));
				}
			}
		}
		double P = (E * 250 * 10) / 0.36;
		return P;
	}

	public static String getPEString(String pHexString) {
		double P = PowerUtils.getPEDouble(pHexString);
		return String.format("%.2f", P);
	}

	public static double getVDouble(String pHexString) {
		byte[] b = hexStringToBytes(pHexString);
		long i = ((b[0] << 16) & 0x00FF0000) + ((b[1] << 8) & 0x0000FF00)
				+ ((b[2]) & 0x000000FF);
		// double fff = (Math.pow(2,-16)+
		// Math.pow(2,-18)+Math.pow(2,-22)+Math.pow(2,-23));
		double E = 0;
		for (int bi = 23; bi >= 0; bi--) {
			long ll = 1 << bi;
			long k = i & ll;

			if (k != 0) {
				E = E + Math.pow(2, (0 - (24 - bi)));
			}
		}

		double P = (E * 250) / 0.6;
		return P;
	}

	public static double getADouble(String pHexString) {
		byte[] b = hexStringToBytes(pHexString);
		long i = ((b[0] << 16) & 0x00FF0000) + ((b[1] << 8) & 0x0000FF00)
				+ ((b[2]) & 0x000000FF);
		// double fff = (Math.pow(2,-16)+
		// Math.pow(2,-18)+Math.pow(2,-22)+Math.pow(2,-23));
		double E = 0;
		for (int bi = 23; bi >= 0; bi--) {
			long ll = 1 << bi;
			long k = i & ll;

			if (k != 0) {
				E = E + Math.pow(2, (0 - (24 - bi)));
			}
		}

		double P = (E * 10) / 0.6;
		return P;
	}

	public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));

        }
        return d;
    }

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

//	public static String getResultStr(String strbuf,Context ctx) {
//		String retstr = PowerUtils.getShowStr(PowerUtils.parseResult(strbuf,ctx));
//		return retstr;
//	}

//	public static HashMap<String, String> parseResult(String strbuf,Context ctx) {
//		HashMap<String, String> retMap = new HashMap();
//		if (strbuf.length() != 42)
//			return null;
//		String type = strbuf.substring(0, 2);
//		String status = strbuf.substring(2, 4);
//		if (status.equals("00")) {
//			retMap.put("status", "off");
//		} else {
//			retMap.put("status", "on");
//		}
//		// 协调器开关（右上）
//		if (type.equals("01")) {
//			String engHex = strbuf.substring(12, 18);
//			String energy = PowerUtils.getPEString(engHex);
//			retMap.put("pe", energy);
//			retMap.put("type", "kgtop");
//		} else if (type.equals("02")) {
//			// 协调器开关（右上）
//			String engHex = strbuf.substring(18, 24);
//			String energy = PowerUtils.getPEString(engHex);
//			retMap.put("pe", energy);
//			retMap.put("type", "kgbottom");
//		} else if (type.equals("03")) {
//			String sAddr = strbuf.substring(12, 16);
//			if (sAddr.equals(Messages.getAddr(Messages.DGBOTTOM, ctx))) {
//				retMap.put("type", "dgbottom");
//			} else {
//				retMap.put("type", "dgtop");
//			}
//			retMap.put("pe", "");
//		} else if (type.equals("05")) {
//			retMap.put("type", "kt");
//			retMap.put("pe", "");
//		} else {
//			retMap.put("type", "hw");
//			retMap.put("pe", "");
//		}
//		return retMap;
//	}

	public static String getShowStr(HashMap<String, String> map) {
		StringBuffer sb = new StringBuffer();
		if (map == null)
			return sb.toString();
		sb.append("--");

		if (map.get("type").equals("dgtop")) {
			sb.append("左上灯光");
		} else if (map.get("type").equals("dgbottom")) {

			sb.append("左下灯光");
		} else if (map.get("type").equals("kt")) {
			sb.append("风机");
		} else if (map.get("type").equals("kgtop")) {
			sb.append("右上开关");
		} else if (map.get("type").equals("kgbottom")) {
			sb.append("右下开关");
		} else {
			sb.append("红外");
		}

		if (map.get("status").equals("on")) {
			sb.append("状态为-开启");
		} else {
			sb.append("状态为-关闭");
		}

		if (map.get("type").equals("kgtop")) {
			sb.append("，电功率为");
			sb.append(map.get("pe"));
			sb.append("w");
		} else if (map.get("type").equals("kgbottom")) {
			sb.append("，电功率为");
			sb.append(map.get("pe"));
			sb.append("w");
		}
		sb.append("\n");
		// sb.append(b)
		return sb.toString();
	}
}
