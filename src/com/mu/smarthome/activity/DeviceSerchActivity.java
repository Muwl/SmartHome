package com.mu.smarthome.activity;

import java.util.Dictionary;
import java.util.List;
import java.util.logging.LoggingMXBean;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mu.smarthome.R;
import com.mu.smarthome.adapter.DeviceSerchAdapter;
import com.mu.smarthome.dialog.SetGateIdDialog;
import com.mu.smarthome.model.TransferEntity;
import com.mu.smarthome.oos.OSSAndroid;
import com.mu.smarthome.utils.LogManager;
import com.mu.smarthome.utils.ShareDataTool;
import com.mu.smarthome.utils.ToastUtils;
import com.mu.smarthome.utils.ToosUtils;

/**
 * @author Mu
 * @date 2015-10-17下午2:45:12
 * @description
 */
public class DeviceSerchActivity extends BaseActivity implements
		OnClickListener {

	private ListView listView;

	private TextView serch;

	private TextView down;

	private TextView save;

	private DeviceSerchAdapter adapter;

	private View pro;

	private OSSAndroid ossAndroid;
	
	private TransferEntity transferEntity;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 108:
				pro.setVisibility(View.VISIBLE);
				ossAndroid.downLoad();
				break;
			case 109:

				break;
			case 1000:
				pro.setVisibility(View.GONE);
				ToastUtils.displayShortToast(DeviceSerchActivity.this, "下载成功！");
				String s = (String) msg.obj;
				Gson gson=new Gson();
				transferEntity=gson.fromJson(s, TransferEntity.class);
				
				LogManager.LogShow("----------------", s, LogManager.ERROR);

				break;
			case 2000:
				pro.setVisibility(View.GONE);
				ToastUtils.displayShortToast(DeviceSerchActivity.this, "保存成功！");
				break;
			case -999:
				pro.setVisibility(View.GONE);
				ToastUtils.displayShortToast(DeviceSerchActivity.this, "下载失败！");
				break;
			case -998:
				pro.setVisibility(View.GONE);
				ToastUtils.displayShortToast(DeviceSerchActivity.this, "保存失败！");
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diviceserch);
		ossAndroid = new OSSAndroid();
		ossAndroid.main(this, handler);
		initView();
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.diviceserch_listview);
		serch = (TextView) findViewById(R.id.diviceserch_serchroom);
		down = (TextView) findViewById(R.id.diviceserch_down);
		save = (TextView) findViewById(R.id.diviceserch_save);
		pro = findViewById(R.id.diviceserch_pro);
		adapter = new DeviceSerchAdapter(this);
		listView.setAdapter(adapter);

		serch.setOnClickListener(this);
		down.setOnClickListener(this);
		save.setOnClickListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(DeviceSerchActivity.this,
						DeviceSetActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.diviceserch_serchroom:

			break;
		case R.id.diviceserch_down:
			if (ToosUtils.isStringEmpty(ShareDataTool
					.getGateId(DeviceSerchActivity.this))) {
				SetGateIdDialog dialog = new SetGateIdDialog(
						DeviceSerchActivity.this, handler, 108);
			} else {
				pro.setVisibility(View.VISIBLE);
				ossAndroid.downLoad();
			}

			break;
		case R.id.diviceserch_save:
			if (ToosUtils.isStringEmpty(ShareDataTool
					.getGateId(DeviceSerchActivity.this))) {
				SetGateIdDialog dialog = new SetGateIdDialog(
						DeviceSerchActivity.this, handler, 109);
			} else {
				// pro.setVisibility(View.VISIBLE);
				// ossAndroid.upload();
			}
			break;

		default:
			break;
		}
	}
}
