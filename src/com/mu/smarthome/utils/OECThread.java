package com.mu.smarthome.utils;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.Socket;
import java.net.UnknownHostException;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
 
/**
 * 从线程发送消息到UI线程（主线程）
 * org.fneg.ThreadWithLooper.java
 * Create at: 2012-6-4 下午4:58:11
 * @author:feng<br/>
 * Email:fengcunhan@gmail.com
 *
 */
public class OECThread extends Thread {
    private Handler handler;
    private Handler uiHandler;
	String host = "192.168.1.25"; // 要连接的服务端IP地址
	int port = 8000; // 要连接的服务端对应的监听端口
	// 与服务端建立连接
	Socket client;	
	Context ctx;
     
    public OECThread(Handler mHandler){
    	
        this.uiHandler=mHandler;
        //初始化Handler，接收到主线程发送过来的Message就回复一个Message给主线程，消息内容是 一个字符串和当前时间
        handler =new Handler(){        	
            @Override
			public void handleMessage(Message msg) {
				try {
					sendControl(Messages.getAddr(msg.what,ctx));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
             
        };
    }
    
    private Handler recvhandler = new Handler( );

//	private Runnable recvrunnable = new Runnable() {
//		public void run() {
//			try {
//				connectTestBox();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			recvhandler.postDelayed(this, 500);
//			// postDelayed(this,1000)方法安排一个Runnable对象到主线程队列中
//		}
//	};

    
    public void sendControl(String s) throws IOException{
    	String tempstr = s.toString();
    	long len = tempstr.length();    	
    	byte[] dgaddr = tempstr.getBytes("ISO-8859-1");
		byte[] sendbuf = new byte[9];
		sendbuf[0] = (byte) 0xD0;
		sendbuf[1] = 0;
		sendbuf[2] = 5;
		sendbuf[3] = 0;
		sendbuf[4] = (byte) 0xFF;
		System.arraycopy(dgaddr, 0, sendbuf,5, 4);		
		OutputStream writer = client.getOutputStream();
		writer.write(sendbuf, 0, 9);
		writer.flush();
    }
	public void connectTestBox() throws UnknownHostException, IOException, InterruptedException {
		// 建立连接后就可以往服务端写数据了

		
		InputStream reader = client.getInputStream();
		byte chars[] = new byte[64];
		int len = 0;
		StringBuffer sb = new StringBuffer();
		String temp;
		while ((len = reader.read(chars, 0, 48)) != -1) {
			sb.append(new String(chars, 0, len));
			if (chars[0] == 'Z')
				break;
		}
		//发送消息到UI
        Message message=new Message();
        message.what=Messages.MSG_HELLO;
//		StringBuffer sb1 = new StringBuffer();

        message.obj= sb.substring(3,45);
        uiHandler.sendMessage(message);
	}

    
    public Handler getHandler() {
        return handler;
    }
 
    public void setHandler(Handler handler) {
        this.handler = handler;
    }
     
    @Override
    public void run() {
        Looper.prepare();
		try {
			client = new Socket(host, port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

  
        new Thread(){  
            @Override  
            public void run() {  
                // TODO Auto-generated method stub  
                super.run();  
                for(;;){
	                try {
						connectTestBox();
					} catch (Exception e) {
						
						e.printStackTrace();
					}
	                recvhandler.sendEmptyMessage(0);
	                try {
						sleep(500);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	             }
                
            }  
        }.start(); 
        
//	    recvhandler.postDelayed(recvrunnable,1000); // 开始Timer
//	    recvhandler.removeCallbacks(recvrunnable); //停止Timer

        Looper.loop();
    }
     
     
}