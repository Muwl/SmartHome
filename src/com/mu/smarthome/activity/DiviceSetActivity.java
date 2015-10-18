package com.mu.smarthome.activity;

import java.io.Serializable;

import com.mu.smarthome.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Mu
 * @date 2015-10-17下午7:12:05
 * @description 设备配置
 */
public class DiviceSetActivity extends BaseActivity implements OnClickListener {

	private TextView title;

	private ImageView back;

	private TextView name;

	private TextView id;

	private TextView type;

	private TextView room;

	private View roomView;

	private TextView address;

	private TextView device;

	private TextView save;

	private TextView cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diviceset);
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
		address = (TextView) findViewById(R.id.diviceset_address);
		device = (TextView) findViewById(R.id.diviceset_control);
		save = (TextView) findViewById(R.id.diviceset_save);
		cancel = (TextView) findViewById(R.id.diviceset_cancel);

		title.setText("设备配置");
		back.setOnClickListener(this);
		back.setVisibility(View.VISIBLE);
		roomView.setOnClickListener(this);
		save.setOnClickListener(this);
		cancel.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.diviceset_roomview:
			break;
		case R.id.diviceset_save:
			break;
		case R.id.diviceset_cancel:
			break;

		default:
			break;
		}
	}

}
