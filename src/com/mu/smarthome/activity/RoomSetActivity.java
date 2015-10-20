package com.mu.smarthome.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.mu.smarthome.R;
import com.mu.smarthome.adapter.RoomSetAdapter;
import com.mu.smarthome.dialog.SetNameDialog;
import com.mu.smarthome.model.DeviceEntity;
import com.mu.smarthome.model.RoomEntity;
import com.mu.smarthome.utils.ShareDataTool;

/**
 * @author Mu
 * @date 2015-10-17下午2:45:12
 * @description
 */
public class RoomSetActivity extends BaseActivity {

	private ListView listView;

	private TextView add;

	private RoomSetAdapter adapter;

	private List<RoomEntity> entities;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 104:
				int position = (int) msg.arg1;
				String name = (String) msg.obj;
				if (position == -1) {
					entities.add(new RoomEntity(name, String.valueOf((System
							.currentTimeMillis() / 1000))));
				} else {
					entities.get(position).name = name;
				}

				adapter.notifyDataSetChanged();
				ShareDataTool.SaveRooms(RoomSetActivity.this, entities);

				break;

			case 40:
				int position1 = msg.arg1;

				List<DeviceEntity> deviceEntities = ShareDataTool
						.getDevice(RoomSetActivity.this);
				if (deviceEntities == null) {
					deviceEntities = new ArrayList<DeviceEntity>();
				}
				for (int i = 0; i < deviceEntities.size(); i++) {
					if (entities.get(position1).roomId.equals(deviceEntities
							.get(i).roomId)) {
						deviceEntities.get(i).roomId = "";
					}
				}
				entities.remove(position1);
				adapter.notifyDataSetChanged();
				ShareDataTool.SaveDevice(RoomSetActivity.this, deviceEntities);
				ShareDataTool.SaveRooms(RoomSetActivity.this, entities);

				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_roomset);
		entities = ShareDataTool.getRooms(this);
		if (entities == null) {
			entities = new ArrayList<RoomEntity>();
		}

		initView();

	}

	public void refush() {
		entities.clear();
		List<RoomEntity> roomEntities = ShareDataTool.getRooms(this);
		if (roomEntities == null) {
			roomEntities = new ArrayList<RoomEntity>();
		}
		for (int i = 0; i < roomEntities.size(); i++) {
			entities.add(roomEntities.get(i));
		}
		adapter.notifyDataSetChanged();
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.roomset_listview);
		add = (TextView) findViewById(R.id.roomset_addroom);
		adapter = new RoomSetAdapter(this, entities, handler);
		listView.setAdapter(adapter);
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SetNameDialog dialog = new SetNameDialog(RoomSetActivity.this,
						"", handler, -1);
			}
		});
	}
}
