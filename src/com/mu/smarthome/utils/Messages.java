 package com.mu.smarthome.utils;

import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.content.SharedPreferences;

public class Messages {
	   public static final int MSG_HELLO=0X0;
	   public static final int DGTOP=0X1;
	   public static final int KT=0X2;
	   public static final int DGBOTTOM=0X3;
	   public static final int KGTOP=0X4;
	   public static final int KGBOTTOM=0X5;
	   
	   
	public static String[] getAddrStr(Context ctx) {
		String [] retStr = new String[5];
	    SharedPreferences sp = ctx.getSharedPreferences("SP", android.content.Context.MODE_PRIVATE );
		retStr[0] = sp.getString("lefttop","");
		retStr[1] = sp.getString("leftmiddle","");
		retStr[2] = sp.getString("leftbottom","");
		retStr[3] = sp.getString("righttop","");
		retStr[4] = sp.getString("rightbottom","");
		return retStr;
	}

	public static String getAddr(int msgType, Context ctx) {
		String strarray[] = Messages.getAddrStr(ctx);
		String retstr;
		if (msgType == Messages.DGTOP) {
			retstr = strarray[0];
		} else if (msgType == Messages.KT) {
			retstr = strarray[1];
		} else if (msgType == Messages.DGBOTTOM) {
			retstr = strarray[2];
		} else if (msgType == Messages.KGTOP) {
			retstr = strarray[3];
		} else {
			retstr = strarray[4];
		}
		return retstr;
	}
}