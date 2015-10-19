package com.mu.smarthome.oos;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.view.View;

import com.alibaba.sdk.android.oss.OSSService;
import com.alibaba.sdk.android.oss.OSSServiceProvider;
import com.alibaba.sdk.android.oss.model.AccessControlList;
import com.alibaba.sdk.android.oss.model.AuthenticationType;
import com.alibaba.sdk.android.oss.model.ClientConfiguration;
import com.alibaba.sdk.android.oss.model.TokenGenerator;
import com.alibaba.sdk.android.oss.util.OSSLog;
import com.alibaba.sdk.android.oss.util.OSSToolKit;
import com.mu.smarthome.utils.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * @author Mu
 * @date 2015-10-19 下午3:02:10
 * @Description oos 工具
 */
public class OSSAndroid {

	static String accessKey;
	static String screctKey;
	static String bucketName;

	public static String srcFileDir;
	public static OSSService ossService;

	private Handler handler;
	private Context context;

	public void main(Context context, Handler handler) {

		// 测试代码没有考虑AK/SK的安全性，保存在本地
		try {
			this.context = context;
			this.handler = handler;
			accessKey = context.getPackageManager().getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA).metaData
					.getString("com.alibaba.app.ossak");
			screctKey = context.getPackageManager().getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA).metaData
					.getString("com.alibaba.app.osssk");
			bucketName = context.getPackageManager().getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA).metaData
					.getString("com.alibaba.app.ossbucketname");
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			ToastUtils.displayShortToast(context, "网络连接失败");
		}

		// 在本地文件系统建立两个随机文件，用于后续的上传
		// try {
		// initLocalFile();
		// } catch (Exception ignore) {
		// }

		// 初始化ossService
		initOssService(context);

		// runDemo();
	}

	private void initOssService(Context context) {
		// 开启Log
		OSSLog.enableLog();

		ossService = OSSServiceProvider.getService();

		ossService.setApplicationContext(context);
		ossService.setGlobalDefaultHostId("oss-cn-beijing.aliyuncs.com"); // 设置region
																			// host
																			// 即
																			// endpoint
		ossService.setGlobalDefaultACL(AccessControlList.PRIVATE); // 默认为private
		ossService.setAuthenticationType(AuthenticationType.ORIGIN_AKSK); // 设置加签类型为原始AK/SK加签
		ossService.setGlobalDefaultTokenGenerator(new TokenGenerator() { // 设置全局默认加签器
					@Override
					public String generateToken(String httpMethod, String md5,
							String type, String date, String ossHeaders,
							String resource) {

						String content = httpMethod + "\n" + md5 + "\n" + type
								+ "\n" + date + "\n" + ossHeaders + resource;

						return OSSToolKit.generateToken(accessKey, screctKey,
								content);
					}
				});
		ossService
				.setCustomStandardTimeWithEpochSec(System.currentTimeMillis() / 1000);

		ClientConfiguration conf = new ClientConfiguration();
		conf.setConnectTimeout(30 * 1000); // 设置全局网络连接超时时间，默认30s
		conf.setSocketTimeout(30 * 1000); // 设置全局socket超时时间，默认30s
		conf.setMaxConcurrentTaskNum(5); // 替换设置最大连接数接口，设置全局最大并发任务数，默认为6
		conf.setIsSecurityTunnelRequired(false); // 是否使用https，默认为false
		ossService.setClientConfiguration(conf);
	}

	private void initLocalFile() throws IOException {
		srcFileDir = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/oss_demo_dir/";
		File dir = new File(srcFileDir);
		if (!dir.exists()) {
			dir.mkdir();
		}
		File file1m = new File(srcFileDir + "file1m");
		File file10m = new File(srcFileDir + "file10m");
		writeRandomContentToFile(file1m, 1024 * 1024);
		writeRandomContentToFile(file10m, 1024 * 1024 * 10);
	}

	private void writeRandomContentToFile(File file, long size)
			throws IOException {
		FileOutputStream fo = new FileOutputStream(file);
		byte[] buffer = new byte[4096];
		new Random().nextBytes(buffer);

		for (int i = 0; i < size / 4096; i++) {
			fo.write(buffer);
		}
		fo.close();
	}

	public void downLoad() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				new GetAndUploadData(context, handler).asyncGetData();
				;
			}
		}).start();
	}

	public void upload(final String temp) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				new GetAndUploadData(context, handler).asyncUpload(temp);
				;
			}
		}).start();
	}

	public void runDemo() {

		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		// new ListObjectsInBucketDemo().show();
		// }
		// }).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				new GetAndUploadData(context, handler).show();
				;
			}
		}).start();

		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		// new GetAndUploadFileDemo().show();
		// }
		// }).start();

		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		// new MultipartUploadDemo().show();
		// }
		// }).start();
	}
}
