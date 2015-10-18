package com.mu.smarthome.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

	private static final String TAG = MyApplication.class.getName();

	private List<Activity> list = new ArrayList<Activity>();

	private static MyApplication instance;

	public static Context applicationContext;

	public static MyApplication getInstance() {
		return instance;
	}

	public void addActivity(Activity activity) {
		list.add(activity);
	}

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

	}
}
