package com.mu.smarthome.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mu.smarthome.activity.DeviceSerchActivity;
import com.mu.smarthome.activity.MainActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;

public class MyApplication extends Application {

	private static final String TAG = MyApplication.class.getName();

	private List<Activity> list = new ArrayList<Activity>();

	private static MyApplication instance;

	public static Context applicationContext;

	private Connection connection;

	private boolean flag = true;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 777:
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i) != null
							&& list.get(i).getClass()
									.equals(MainActivity.class)) {
						((MainActivity) (list.get(i))).onSerchFush();
					}
					if (list.get(i) != null
							&& list.get(i).getClass()
									.equals(DeviceSerchActivity.class)) {
						((DeviceSerchActivity) (list.get(i))).refush();
					}

				}
				break;

			default:
				break;
			}
		};
	};

	public static MyApplication getInstance() {
		return instance;
	}

	public void addActivity(Activity activity) {
		list.add(activity);
	}

	// public boolean isFlag() {
	// return flag;
	// }
	//
	// public void setFlag(boolean flag) {
	// this.flag = flag;
	// }

	public List<Activity> getActivities() {
		return list;
	}

	public void exit() {
		for (Activity activity : list) {
			if (activity != null) {
				activity.finish();
			}
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		applicationContext = this;
		instance = this;
		connection = new Connection(this);
		new Thread(new Runnable() {

			@Override
			public void run() {

				while (true) {
					LogManager.LogShow("-----", "后台运行 ，但是不查询-----",
							LogManager.ERROR);
					if (!isApplicationBroughtToBackground()) {
						flag = true;
						LogManager.LogShow("-----", "全局查询------------",
								LogManager.ERROR);
						try {
							connection.searchDevices();
							handler.sendEmptyMessage(777);
						} catch (IOException e) {
							e.printStackTrace();
						}

					} else {
						if (flag) {
							sendCancel();
						}
					}
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}).start();

	}

	/**
	 * 判断当前应用程序处于前台还是后台
	 */
	public static boolean isApplicationBroughtToBackground() {
		ActivityManager am = (ActivityManager) instance
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(instance.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	private void sendCancel() {
		LogManager.LogShow("-----", "取消控制------------",
				LogManager.ERROR);
		flag = false;
		connection.cancelConl();
	}
}
