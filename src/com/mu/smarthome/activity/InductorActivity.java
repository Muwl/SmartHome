package com.mu.smarthome.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mu.smarthome.R;
import com.mu.smarthome.adapter.InductorAdapter;
import com.mu.smarthome.model.DeviceEntity;
import com.mu.smarthome.model.InductorEntity;
import com.mu.smarthome.model.RoomEntity;
import com.mu.smarthome.utils.ShareDataTool;

/**
 * @author Mu
 * @date 2015-10-16下午9:19:21
 * @description 体感应器页面
 */
public class InductorActivity extends BaseActivity {

	private ImageView back;

	private TextView title;

	private ListView listView;

	private InductorAdapter adapter;

	private List<InductorEntity> inductorEntities;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inductor);
		initDate();
		initView();
	}

	private void initDate() {
		List<RoomEntity> roomEntities = ShareDataTool.getRooms(this);
		List<DeviceEntity> deviceEntities = ShareDataTool.getDevice(this);
		if (roomEntities == null) {
			roomEntities = new ArrayList<RoomEntity>();
		}
		if (deviceEntities == null) {
			deviceEntities = new ArrayList<DeviceEntity>();
		}
		inductorEntities = new ArrayList<InductorEntity>();

		for (int i = 0; i < roomEntities.size(); i++) {
			List<DeviceEntity> entities = new ArrayList<DeviceEntity>();
			InductorEntity inductorEntity = new InductorEntity();
			inductorEntity.entity = roomEntities.get(i);
			for (int j = 0; j < deviceEntities.size(); j++) {
				if ("03".equals(deviceEntities.get(j).type)
						&& roomEntities.get(i).roomId.equals(deviceEntities
								.get(j).roomId)) {
					entities.add(deviceEntities.get(j));
				}
			}

			if (entities.size() != 0) {
				inductorEntity.deviceEntities = entities;
				inductorEntities.add(inductorEntity);
			}
		}

	}

	private void initView() {
		back = (ImageView) findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		listView = (ListView) findViewById(R.id.inductor_listview);

		title.setText("人体感应器");
		back.setVisibility(View.VISIBLE);
		adapter = new InductorAdapter(this, inductorEntities);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		listView.setAdapter(adapter);
	}

}
