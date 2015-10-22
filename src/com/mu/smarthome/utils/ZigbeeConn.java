package com.mu.smarthome.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Message;
import android.util.Log;

public class ZigbeeConn {
	Map<String,String> shortAddrMap= new HashMap<String,String>();
	

	public static final String TAG = ZigbeeConn.class.getSimpleName();
	Socket client;
	String host = "202.204.1.3"; // 要连接的服务端IP地址
	int port = 8000; // 要连接的服务端对应的监听端口

	public void sendControl(String s) throws IOException {
		String tempstr = s.toString();
		long len = tempstr.length();
		byte[] dgaddr = tempstr.getBytes("ISO-8859-1");
		byte[] sendbuf = new byte[9];
		sendbuf[0] = (byte) 0xD0;
		sendbuf[1] = 0;
		sendbuf[2] = 5;
		sendbuf[3] = 0;
		sendbuf[4] = (byte) 0xFF;
		System.arraycopy(dgaddr, 0, sendbuf, 5, 4);
		OutputStream writer = client.getOutputStream();
		writer.write(sendbuf, 0, 9);
		writer.flush();
		
	}

	public String[] sendCommand(String action, String device) {

		SocketAddress socketAddress = null;
		String retarray[] = new String[1];
		try {
			client = new Socket();
			socketAddress = new InetSocketAddress(host, port);
			client.connect(socketAddress, 2000);
			client.setSoTimeout(2000);
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d(TAG, "连接网关失败");
		}

		try {
			OutputStream writer = client.getOutputStream();
			String s = ZigbeeConn.bytesToHexString(getCommand(action, device));
			
			writer.write(getCommand(action, device), 0, 44);
			writer.flush();
			if (action.equals(ZigbeeConn.action_read)) {
				InputStream reader = client.getInputStream();
				byte chars[] = new byte[30];
				int len = 0;
				StringBuffer sb = new StringBuffer();
				while ((len = reader.read(chars, 0, 30)) != -1) {
					sb.append(new String(chars, 0, len));
					if (chars[0] == 'Z')
						break;
				}
				retarray[0] = sb.toString();

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d(TAG, "通讯");
		}

		try {
			client.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d(TAG, "关闭网关失败");
		}
		return retarray;

	}

	public String[] syncAll() throws IOException, InterruptedException {
//		String hexs = ZigbeeConn.bytesToHexString(ZigbeeConn.ADDR_CZLEFT);
		
		String retStrArray[] = new String[100];//最多100个设备
		SocketAddress socketAddress = null;
		client = new Socket();
		socketAddress = new InetSocketAddress(host, port);
		client.connect(socketAddress, 2000);
		client.setSoTimeout(2000);
		

		byte chars[] = new byte[2048];
		int index=0;
		String tempstr;
		int len = 0;
		try {
			OutputStream writer = client.getOutputStream();
			InputStream reader = client.getInputStream();

			StringBuffer sb = new StringBuffer();
					
			writer.write(getCommand(ZigbeeConn.action_sync_all,
					ZigbeeConn.dev_dgleft), 0, 4);
			writer.flush();
			
			byte tempb[]=new byte[1];
			while((len = reader.read(tempb, 0, 1))!=-1){
				if(len==1){
					chars[index]=tempb[0];
					index++;
				}
				if(index>2000)//最多100个设备
					break;
			}
		} catch (Exception e) {
			int number = (index)/40;
			for (int l =0;l<number;l++){
				byte tempbyte[]=new byte[40];
				System.arraycopy(chars, l*40, tempbyte, 0, 40);
				tempstr = ZigbeeConn.bytesToHexString(tempbyte).toUpperCase();
				retStrArray[l] = tempstr;
				index++;
			}
			client.close();

			syncShortAddr(retStrArray);
			return retStrArray;
		}
		int number = (index)/40;
		for (int l =0;l<number;l++){
			byte tempbyte[]=new byte[40];
			System.arraycopy(chars, l*40, tempbyte, 0, 40);
			tempstr = ZigbeeConn.bytesToHexString(tempbyte).toUpperCase();
			retStrArray[l] = tempstr;
			index++;
		}
		client.close();

		syncShortAddr(retStrArray);
		return retStrArray;

	}
	private void syncShortAddr(String[] res){
		
		if (res == null) {
			return ;
		}
		for (int i = 0; i < res.length; i++) {
			String result = res[i];
			if (((result==null)||result.length() != 80))
				continue;
			String hexDevid = result.substring(12, 28);
			String shrotaddr = result.substring(8,12);
			// 1、主控插座 控制左插座
			if ( hexDevid.equalsIgnoreCase(ZigbeeConn.bytesToHexString(ZigbeeConn.ADDR_CZLEFT))) {
				shortAddrMap.put("ADDR_CZLEFT", shrotaddr);
				
			} else if (hexDevid.equalsIgnoreCase(ZigbeeConn.bytesToHexString(ZigbeeConn.ADDR_CZRIGHT))) {// 2、辅控插座 右插座
				shortAddrMap.put("ADDR_CZRIGHT", shrotaddr);
			} else if (hexDevid.equalsIgnoreCase(ZigbeeConn.bytesToHexString(ZigbeeConn.ADDR_FHKG))) {// 3、复合开关 左上灯
				shortAddrMap.put("ADDR_FHKG", shrotaddr);

			} else if (hexDevid.equalsIgnoreCase(ZigbeeConn.bytesToHexString(ZigbeeConn.ADDR_HW))) {// 4、红外
				shortAddrMap.put("ADDR_HW", shrotaddr);			
			} else if (hexDevid.equalsIgnoreCase(ZigbeeConn.bytesToHexString(ZigbeeConn.ADDR_FS))) {// 5、风机
				shortAddrMap.put("ADDR_FS", shrotaddr);	
			} else if (hexDevid.equalsIgnoreCase(ZigbeeConn.bytesToHexString(ZigbeeConn.ADDR_SKKG))){// 6，双联
				shortAddrMap.put("ADDR_SKKG", shrotaddr);	
			}
		}
	}

	public String closeAll() throws IOException, InterruptedException {

		client = new Socket();
		SocketAddress socketAddress = new InetSocketAddress(host, port);
		client.connect(socketAddress, 3000);
		client.setSoTimeout(5000);
		try {
			OutputStream writer = client.getOutputStream();
			writer.write(getCommand(ZigbeeConn.action_close,
					ZigbeeConn.dev_dgleft), 0, 13);
			writer.flush();

			Thread.sleep(1000);
			writer.write(getCommand(ZigbeeConn.action_close,
					ZigbeeConn.dev_dgrgd), 0, 13);
			writer.flush();

			Thread.sleep(1000);
			writer.write(getCommand(ZigbeeConn.action_close,
					ZigbeeConn.dev_dgright), 0, 13);
			writer.flush();

			Thread.sleep(1000);
			writer.write(getCommand(ZigbeeConn.action_close,
					ZigbeeConn.dev_czleft), 0, 13);
			writer.flush();

			Thread.sleep(1000);
			writer.write(getCommand(ZigbeeConn.action_close,
					ZigbeeConn.dev_czright), 0, 13);
			writer.flush();

			Thread.sleep(1000);

			writer.write(getCommand(ZigbeeConn.action_close,
					ZigbeeConn.dev_fs), 0, 13);
			writer.flush();
		} catch (Exception e) {
			client.close();
			throw new IOException();
		}
		client.close();

		return "";

	}

	public String openAll() throws UnknownHostException, IOException,
			InterruptedException {

		client = new Socket();
		SocketAddress socketAddress = new InetSocketAddress(host, port);
		client.connect(socketAddress, 3000);
		client.setSoTimeout(5000);

		OutputStream writer = client.getOutputStream();
		try {
			writer.write(getCommand(ZigbeeConn.action_open,
					ZigbeeConn.dev_dgleft), 0, 13);
			writer.flush();

			Thread.sleep(1000);
			writer.write(getCommand(ZigbeeConn.action_open,
					ZigbeeConn.dev_dgrgd), 0, 13);
			writer.flush();

			Thread.sleep(1000);
			writer.write(getCommand(ZigbeeConn.action_open,
					ZigbeeConn.dev_dgright), 0, 13);
			writer.flush();

			Thread.sleep(1000);

			writer.write(getCommand(ZigbeeConn.action_open,
					ZigbeeConn.dev_czleft), 0, 13);
			writer.flush();

			Thread.sleep(1000);
			writer.write(getCommand(ZigbeeConn.action_open,
					ZigbeeConn.dev_czright), 0, 13);
			writer.flush();

			Thread.sleep(1000);

			writer.write(getCommand(ZigbeeConn.action_open,
					ZigbeeConn.dev_fs), 0, 13);
			writer.flush();
		} catch (Exception e) {
			client.close();
			throw new IOException();
		}
		client.close();
		return "";
	}

	public void connectTestBox() throws UnknownHostException, IOException,
			InterruptedException {
		// 建立连接后就可以往服务端写数据了

		InputStream reader = client.getInputStream();
		byte chars[] = new byte[64];
		int len = 0;
		StringBuffer sb = new StringBuffer();
		String temp;
		while ((len = reader.read(chars, 0, 30)) != -1) {
			sb.append(new String(chars, 0, len));
			if (chars[0] == 'Z')
				break;
		}
		// 发送消息到UI
		Message message = new Message();
		message.what = Messages.MSG_HELLO;

	}

	public static String toHexString(String s) {
		String str = "";
		byte[] b = new byte[s.length()];
		b=s.getBytes();
		int ii = -43;
		String s5 =Integer.toHexString((ii & 0x000000FF) | 0xFFFFFF00).substring(6);
		char [] cc = s.toCharArray();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			byte bs  = (byte)ch;
			String s4 = Integer.toHexString((bs & 0x000000FF) | 0xFFFFFF00).substring(6);
//			String s4 = Integer.toHexString(ch);
			str = str + s4;
		}
		return str;
	}


	private static byte[] getBytes (char[] chars) {
		   Charset cs = Charset.forName("US-ASCII");
		   CharBuffer cb = CharBuffer.allocate (chars.length);
		   cb.put (chars);
		                 cb.flip ();
		   ByteBuffer bb = cs.encode (cb);
		  
		   return bb.array();
		         }

	static private char[] getChars (byte[] bytes) {
		      Charset cs = Charset.forName ("UTF-8");
		      ByteBuffer bb = ByteBuffer.allocate (bytes.length);
		      bb.put (bytes);
		                 bb.flip ();
		       CharBuffer cb = cs.decode (bb);
		  
		   return cb.array();
		}
	static public JSONArray parseResult(String[] res) throws Exception {
		JSONArray jsonArray = new JSONArray();

		JSONObject retobj = new JSONObject();
		if (res == null) {
			return jsonArray;
		}
		for (int i = 0; i < res.length; i++) {
			retobj = new JSONObject();
			String result = res[i];
			if (((result==null)||result.length() != 80))
				continue;
			String hexDevid = result.substring(12, 28);			
			// 1、主控插座 控制左插座
			if ( hexDevid.equalsIgnoreCase(ZigbeeConn.bytesToHexString(ZigbeeConn.ADDR_CZLEFT))) {
				String status = result.substring(36, 38);
				retobj.put("type", ZigbeeConn.dev_czleft);
				if (status.equals("00")) {
					retobj.put("status", "open");
				} else if (status.equals("FF")) {
					retobj.put("status", "close");
				}
				String engHex = result.substring(38, 44);
				String energy = PowerUtils.getPEString(engHex);
				retobj.put("pe", energy);
				jsonArray.put(retobj);
			} else if (hexDevid.equalsIgnoreCase(ZigbeeConn.bytesToHexString(ZigbeeConn.ADDR_CZRIGHT))) {// 2、辅控插座 右插座
				String status = result.substring(36, 38);
				retobj.put("type", ZigbeeConn.dev_czright);
				if (status.equals("00")) {
					retobj.put("status", "open");
				} else if (status.equals("FF")) {
					retobj.put("status", "close");
				}
				String engHex = result.substring(38, 44);
				String energy = PowerUtils.getPEString(engHex);
				retobj.put("pe", energy);
				jsonArray.put(retobj);
			} else if (hexDevid.equalsIgnoreCase(ZigbeeConn.bytesToHexString(ZigbeeConn.ADDR_FHKG))) {// 3、复合开关 左上灯
				String status = result.substring(36, 38);
				retobj.put("type", ZigbeeConn.dev_dgleft);
				if (status.equals("00")) {
					retobj.put("status", "open");
				} else if (status.equals("FF")) {
					retobj.put("status", "close");
				}
				jsonArray.put(retobj);

			} else if (hexDevid.equalsIgnoreCase(ZigbeeConn.bytesToHexString(ZigbeeConn.ADDR_HW))) {// 4、红外
				String status = result.substring(36, 38);
				retobj.put("type", ZigbeeConn.dev_hw);
				if (status.equals("00")) {
					retobj.put("status", "open");
				} else if (status.equals("FF")) {
					retobj.put("status", "close");
				} else if (status.equals("01")) {
					retobj.put("status", "wait");
				}
				jsonArray.put(retobj);
			} else if (hexDevid.equalsIgnoreCase(ZigbeeConn.bytesToHexString(ZigbeeConn.ADDR_FS))) {// 5、风机
				String status = result.substring(36, 38);
				retobj.put("type", ZigbeeConn.dev_fs);
				if (status.equals("00")) {
					retobj.put("status", "open");
				} else if (status.equals("FF")) {
					retobj.put("status", "close");
				}
				jsonArray.put(retobj);
			} else if (hexDevid.equalsIgnoreCase(ZigbeeConn.bytesToHexString(ZigbeeConn.ADDR_SKKG))){// 6，双联
				String leftstatus = result.substring(36, 38);
				String rightstatus = result.substring(38, 40);
				retobj.put("type", ZigbeeConn.dev_dgright);
				if (leftstatus.equals("00")) {
					retobj.put("status", "open");
				} else if (leftstatus.equals("FF")) {
					retobj.put("status", "close");
				}
				jsonArray.put(retobj);
				JSONObject rgdObj = new JSONObject();
				rgdObj.put("type", ZigbeeConn.dev_dgrgd);
				if (rightstatus.equals("00")) {
					rgdObj.put("status", "open");
				} else if (rightstatus.equals("FF")) {
					rgdObj.put("status", "close");
				}
				jsonArray.put(rgdObj);
			}
		}
		return jsonArray;
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
	 public static String bytesToHexString(byte[] src){
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

	public byte[] getCommand(String action, String device) {
//		byte[] buff = new byte[13];
		byte[] buff = new byte[44];
		buff[0] = (byte) 0xD0;
		buff[1] = 0;
		buff[2] = 0x28;
		buff[3] = 0;
		if(action.equals("sync_all")){
			buff = new byte[4];
//			buff[0] = (byte) 0xD0;
//			buff[1] = 0;
//			buff[2] = 4;
//			buff[3] = 0;
			buff[0] =0x10;
			buff[1] =0x00;
			buff[2] = 0;
			buff[3] = 0;
			return buff;
		}
		buff[4]=(byte)0xFE;
		buff[5]=(byte)0xFE;
		buff[42]=(byte)0xEE;
		buff[43]=(byte)0xEE;
		
		if (device.equals(ZigbeeConn.dev_czleft)) {
			if (action.equals("open")) {
				buff[22] = 0x10;
			} else if (action.equals("close")) {
				buff[22] = 0x11;
			} else {
				buff[22] = 0x12;
			}
			
			System.arraycopy(hexStringToByte(this.shortAddrMap.get("ADDR_CZLEFT")), 0, buff, 8, 2);
			System.arraycopy(ZigbeeConn.ADDR_CZLEFT, 0, buff, 10, 8);
		} else if (device.equals(ZigbeeConn.dev_czright)) {
			if (action.equals("open")) {
				buff[22] = 0x10;
			} else if (action.equals("close")) {
				buff[22] = 0x11;
			} else {
				buff[22] = 0x12;
			}
			System.arraycopy(hexStringToByte(this.shortAddrMap.get("ADDR_CZRIGHT")), 0, buff, 8, 2);
			System.arraycopy(ZigbeeConn.ADDR_CZRIGHT, 0, buff, 10, 8);
		} else if (device.equals(ZigbeeConn.dev_fs)) {
			if (action.equals("open")) {
				buff[22] = 0x10;
			} else if (action.equals("close")) {
				buff[22] = 0x11;
			} else {
				buff[22] = 0x12;
			}
			System.arraycopy(hexStringToByte(this.shortAddrMap.get("ADDR_FS")), 0, buff, 8, 2);
			System.arraycopy(ZigbeeConn.ADDR_FS, 0, buff, 10, 8);
		} else if (device.equals(ZigbeeConn.dev_dgleft)) {
			if (action.equals("open")) {
				buff[22] = 0x10;
			} else if (action.equals("close")) {
				buff[22] = 0x11;
			} else {
				buff[22] = 0x12;
			}

			System.arraycopy(hexStringToByte(this.shortAddrMap.get("ADDR_FHKG")), 0, buff, 8, 2);
			System.arraycopy(ZigbeeConn.ADDR_FHKG, 0, buff, 10, 8);
		} else if (device.equals(ZigbeeConn.dev_dgright)) {
			if (action.equals("open")) {
				buff[22] = 0x10;
			} else if (action.equals("close")) {
				buff[22] = 0x11;
			} else {
				buff[22] = 0x12;
			}
			System.arraycopy(hexStringToByte(this.shortAddrMap.get("ADDR_SKKG")), 0, buff, 8, 2);
			System.arraycopy(ZigbeeConn.ADDR_SKKG, 0, buff, 10, 8);
		} else if (device.equals(ZigbeeConn.dev_dgrgd)) {
			if (action.equals("open")) {
				buff[22] = 0x20;
			} else if (action.equals("close")) {
				buff[22] = 0x21;
			} else {
				buff[22] = 0x12;
			}
			System.arraycopy(hexStringToByte(this.shortAddrMap.get("ADDR_SKKG")), 0, buff, 8, 2);
			System.arraycopy(ZigbeeConn.ADDR_SKKG, 0, buff, 10, 8);
		} else if (device.equals(ZigbeeConn.dev_hw)) {
			buff[22] = 0x12;
			System.arraycopy(hexStringToByte(this.shortAddrMap.get("ADDR_HW")), 0, buff, 8, 2);
			System.arraycopy(ZigbeeConn.ADDR_HW, 0, buff, 10, 8);
		}
		return buff;
	}

	static String action_open = "open";
	static String action_close = "close";
	static String action_read = "read";
	static String action_sync_all = "sync_all";

	static String dev_dgleft = "dgleft";
	static String dev_dgright = "dgright";
	static String dev_dgrgd = "dgrgd";
	static String dev_fs = "fs";
	static String dev_czleft = "czleft";
	static String dev_czright = "czright";
	static String dev_hw = "hw";

	static byte[] ADDR_CZLEFT = { 0x00, 0x12, 0x4B, 0x00, 0x03, (byte) 0xD5,
			0x07, (byte) 0xBC };
	static byte[] ADDR_CZRIGHT = { 0x00, 0x12, 0x4B, 0x00, 0x03, (byte) 0xD5,
			0x09, 0x53 };
	static byte[] ADDR_FS = { 0x00, 0x12, 0x4B, 0x00, 0x03, (byte) 0xD5, 0x09,
			(byte) 0x84 };
	static byte[] ADDR_SKKG = { 0x00, 0x12, 0x4B, 0x00, 0x03, (byte) 0xD5,
			0x09, 0x45 };
	static byte[] ADDR_FHKG = { 0x00, 0x12, 0x4B, 0x00, 0x05, (byte) 0xAB,
			0x18, (byte) 0x9E };
	static byte[] ADDR_HW = { 0x00, 0x12, 0x4B, 0x00, 0x03, (byte) 0xD5, 0x0B,
			 0x2C };


	static byte[] ADDR_CZLEFT_S = {(byte)0xB3,0x08 };
	static byte[] ADDR_CZRIGHT_S = { (byte)0x90,0x11 };
	static byte[] ADDR_FS_S = { (byte)0xAC,0x0F};
	static byte[] ADDR_SKKG_S = {(byte)0xCB,0x07};
	static byte[] ADDR_FHKG_S = {0x72,(byte)0xEC};
	static byte[] ADDR_HW_S = { 0x39, (byte)0x90};
	
	// 复合开关：00 12 4B 00 03 D5 0A 88
	// 红外：00 12 4B 00 03 D5 0B 2C
	// 风机：00 12 4B 00 03 D5 09 84
	// 双控开关：00 12 4B 00 03 D5 09 45
}
