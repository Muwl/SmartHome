package com.mu.smarthome.activity;

import java.util.ArrayList;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mu.smarthome.R;
import com.mu.smarthome.adapter.DeviceSerchAdapter;
import com.mu.smarthome.dialog.CustomeDialog;
import com.mu.smarthome.dialog.SetGateIdDialog;
import com.mu.smarthome.model.DeviceEntity;
import com.mu.smarthome.model.RoomEntity;
import com.mu.smarthome.model.TransferEntity;
import com.mu.smarthome.oos.OSSAndroid;
import com.mu.smarthome.utils.Connection;
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

	private List<RoomEntity> roomEntities;

	private List<DeviceEntity> deviceEntities;

	private Connection connection;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 108:
				pro.setVisibility(View.VISIBLE);
				ossAndroid.downLoad();
				break;
			case 109:
				pro.setVisibility(View.VISIBLE);
				TransferEntity entity = new TransferEntity();
				entity.devices = ShareDataTool
						.getDevice(DeviceSerchActivity.this);
				entity.rooms = ShareDataTool.getRooms(DeviceSerchActivity.this);
				entity.gateway = ShareDataTool
						.getGateWay(DeviceSerchActivity.this);
				Gson gson2 = new Gson();
				ossAndroid.upload(gson2.toJson(entity));
				break;
			case 1000:
				pro.setVisibility(View.GONE);
				ToastUtils.displayShortToast(DeviceSerchActivity.this, "下载成功！");
				String s = (String) msg.obj;
				Gson gson = new Gson();
				transferEntity = gson.fromJson(s, TransferEntity.class);
				List<DeviceEntity> dentities = transferEntity.devices;
				List<RoomEntity> rentities = transferEntity.rooms;
				ShareDataTool.SaveRooms(DeviceSerchActivity.this,
						transferEntity.rooms);
				ShareDataTool.SaveGateWay(DeviceSerchActivity.this,
						transferEntity.gateway);
				ShareDataTool.SaveDevice(DeviceSerchActivity.this,
						transferEntity.devices);
				if (dentities == null) {
					dentities = new ArrayList<DeviceEntity>();
				}
				if (rentities == null) {
					rentities = new ArrayList<RoomEntity>();
				}
				deviceEntities.clear();
				roomEntities.clear();
				for (int i = 0; i < dentities.size(); i++) {
					deviceEntities.add(dentities.get(i));
				}
				for (int j = 0; j < rentities.size(); j++) {
					roomEntities.add(rentities.get(j));
				}
				adapter.notifyDataSetChanged();

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

			case Connection.SERCH_SUCC:
				pro.setVisibility(View.GONE);
				refush();
				ToastUtils.displayShortToast(DeviceSerchActivity.this, "操作成功！");
				break;
			case Connection.ERROR_CODE:
				pro.setVisibility(View.GONE);
				ToastUtils.displayShortToast(DeviceSerchActivity.this, "操作失败！");
				break;
			case 40:
				int position = msg.arg1;
				deviceEntities.remove(position);
				ShareDataTool.SaveDevice(DeviceSerchActivity.this,
						deviceEntities);
				adapter.notifyDataSetChanged();
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
		connection = new Connection(this);

		initView();

		ossAndroid = new OSSAndroid();
		ossAndroid.main(this, handler);
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.diviceserch_listview);
		serch = (TextView) findViewById(R.id.diviceserch_serchroom);
		down = (TextView) findViewById(R.id.diviceserch_down);
		save = (TextView) findViewById(R.id.diviceserch_save);
		pro = findViewById(R.id.diviceserch_pro);

		serch.setOnClickListener(this);
		down.setOnClickListener(this);
		save.setOnClickListener(this);

		deviceEntities = ShareDataTool.getDevice(this);
		roomEntities = ShareDataTool.getRooms(this);
		if (deviceEntities == null) {
			deviceEntities = new ArrayList<DeviceEntity>();
		}
		if (roomEntities == null) {
			roomEntities = new ArrayList<RoomEntity>();
		}
		adapter = new DeviceSerchAdapter(this, deviceEntities, roomEntities);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(DeviceSerchActivity.this,
						DeviceSetActivity.class);
				intent.putExtra("position", position);
				startActivity(intent);
			}
		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				CustomeDialog customeDialog = new CustomeDialog(
						DeviceSerchActivity.this, handler, "确定删除此设备？",
						position, -888);

				return true;
			}

		});
	}

	public void refush() {
		deviceEntities.clear();
		roomEntities.clear();
		List<RoomEntity> roomEntities1 = ShareDataTool.getRooms(this);
		if (roomEntities1 == null) {
			roomEntities1 = new ArrayList<RoomEntity>();
		}
		for (int i = 0; i < roomEntities1.size(); i++) {
			roomEntities.add(roomEntities1.get(i));
		}
		List<DeviceEntity> deviceEntities1 = ShareDataTool.getDevice(this);
		if (deviceEntities1 == null) {
			deviceEntities1 = new ArrayList<DeviceEntity>();
		}
		for (int i = 0; i < deviceEntities1.size(); i++) {
			deviceEntities.add(deviceEntities1.get(i));
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.diviceserch_serchroom:
			pro.setVisibility(View.VISIBLE);
			connection.search(handler);

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
				pro.setVisibility(View.VISIBLE);
				TransferEntity entity = new TransferEntity();
				entity.devices = ShareDataTool
						.getDevice(DeviceSerchActivity.this);
				entity.rooms = ShareDataTool.getRooms(DeviceSerchActivity.this);
				entity.gateway = ShareDataTool
						.getGateWay(DeviceSerchActivity.this);
				Gson gson = new Gson();
				ossAndroid.upload(gson.toJson(entity));
			}
			break;

		default:
			break;
		}
	}
}
