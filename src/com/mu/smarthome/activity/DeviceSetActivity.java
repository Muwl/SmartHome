package com.mu.smarthome.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mu.smarthome.R;
import com.mu.smarthome.dialog.RoomSetDialog;
import com.mu.smarthome.model.DeviceEntity;
import com.mu.smarthome.model.RoomEntity;
import com.mu.smarthome.utils.ShareDataTool;
import com.mu.smarthome.utils.ToastUtils;
import com.mu.smarthome.utils.ToosUtils;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Mu
 * @date 2015-10-17下午7:12:05
 * @description 设备配置
 */
public class DeviceSetActivity extends BaseActivity implements OnClickListener {

	private TextView title;

	private ImageView back;

	private TextView name;

	private TextView id;

	private TextView type;

	private TextView room;

	private View roomView;

	private EditText address;

	private EditText device;

	private TextView save;

	private TextView cancel;

	private int position;

	private DeviceEntity deviceEntity;

	private List<RoomEntity> roomEntities;

	List<DeviceEntity> entities;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 91:
				RoomEntity roomEntity = (RoomEntity) msg.obj;
				if (roomEntity != null) {
					deviceEntity.roomId = roomEntity.roomId;
					room.setText(roomEntity.name);
				}
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diviceset);
		position = getIntent().getIntExtra("position", 0);
		entities = ShareDataTool.getDevice(this);
		if (entities == null) {
			entities = new ArrayList<DeviceEntity>();
		}
		roomEntities = ShareDataTool.getRooms(this);
		if (roomEntities == null) {
			roomEntities = new ArrayList<RoomEntity>();
		}
		deviceEntity = entities.get(position);
		initView();
	}

	private void initView() {
		title = (TextView) findViewById(R.id.title_title);
		back = (ImageView) findViewById(R.id.title_back);
		name = (TextView) findViewById(R.id.diviceset_name);
		id = (TextView) findViewById(R.id.diviceset_id);
		type = (TextView) findViewById(R.id.diviceset_type);
		room = (TextView) findViewById(R.id.diviceset_room);
		roomView = findViewById(R.id.diviceset_roomview);
		address = (EditText) findViewById(R.id.diviceset_address);
		device = (EditText) findViewById(R.id.diviceset_control);
		save = (TextView) findViewById(R.id.diviceset_save);
		cancel = (TextView) findViewById(R.id.diviceset_cancel);

		title.setText("设备配置");
		back.setOnClickListener(this);
		back.setVisibility(View.VISIBLE);
		roomView.setOnClickListener(this);
		save.setOnClickListener(this);
		cancel.setOnClickListener(this);

		name.setText(deviceEntity.name);
		if (!ToosUtils.isStringEmpty(deviceEntity.longAddress)) {
			id.setText(deviceEntity.longAddress);
		} else {
			id.setText("");
		}

		type.setText(ToosUtils.getType(deviceEntity.type));
		room.setText(ToosUtils.getRoonName(deviceEntity.roomId, roomEntities));
		if (ToosUtils.isStringEmpty(deviceEntity.location)) {
			address.setText("未设置");
		} else {
			address.setText(deviceEntity.location);
		}

		if (ToosUtils.isStringEmpty(deviceEntity.controlLocation)) {
			device.setText("未设置");
		} else {
			device.setText(deviceEntity.controlLocation);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.diviceset_roomview:
			if (roomEntities == null || roomEntities.size() == 0) {
				return;
			}
			RoomSetDialog dialog = new RoomSetDialog(DeviceSetActivity.this,
					handler, deviceEntity.roomId);
			break;
		case R.id.diviceset_save:
			if (ToosUtils.isTextEmpty(name)) {
				ToastUtils.displayShortToast(DeviceSetActivity.this, "请输入设备名称");
				return;
			}

			deviceEntity.name = ToosUtils.getTextContent(name);
			if (!ToosUtils.isTextEmpty(address)
					&& "未设置".equals(ToosUtils.getTextContent(address))) {
				deviceEntity.location = "";
			} else {
				deviceEntity.location = ToosUtils.getTextContent(address);
			}

			if (!ToosUtils.isTextEmpty(device)
					&& "未设置".equals(ToosUtils.getTextContent(device))) {
				deviceEntity.controlLocation = "";
			} else {
				deviceEntity.controlLocation = ToosUtils.getTextContent(device);
			}
			entities.set(position, deviceEntity);
			ShareDataTool.SaveDevice(DeviceSetActivity.this, entities);
			ToastUtils.displayShortToast(DeviceSetActivity.this, "保存成功！");
			finish();
			break;
		case R.id.diviceset_cancel:
			finish();
			break;

		default:
			break;
		}
	}

}
