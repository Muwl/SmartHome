package com.mu.smarthome.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mu.smarthome.R;
import com.mu.smarthome.model.DeviceEntity;
import com.mu.smarthome.model.GatewayEntity;
import com.mu.smarthome.model.RoomEntity;
import com.mu.smarthome.utils.LogManager;
import com.mu.smarthome.utils.ShareDataTool;
import com.mu.smarthome.utils.ToastUtils;
import com.mu.smarthome.utils.ToosUtils;

/**
 * @author Mu
 * @date 2015-10-17下午2:45:12
 * @description
 */
public class GatewayActivity extends BaseActivity implements OnClickListener {

	private EditText ip;

	private TextView id;

	private TextView type;

	private TextView getId;

	private TextView save;

	private TextView cancel;

	private GatewayEntity entity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gateway);
		initView();

	}

	private void initView() {
		ip = (EditText) findViewById(R.id.gateway_ip);
		id = (TextView) findViewById(R.id.gateway_id);
		type = (TextView) findViewById(R.id.gateway_type);
		getId = (TextView) findViewById(R.id.gateway_getid);
		save = (TextView) findViewById(R.id.gateway_save);
		cancel = (TextView) findViewById(R.id.gateway_cancel);

		getId.setOnClickListener(this);
		save.setOnClickListener(this);
		cancel.setOnClickListener(this);
		entity = ShareDataTool.getGateWay(this);
		if (entity == null) {
			entity = new GatewayEntity("", "", "");
		}
		ip.setText(entity.ipAddress);
		id.setText(entity.identier);
		type.setText("HYV1.1");
		com.mu.smarthome.utils.LogManager.LogShow("-----", "00000000000000",
				LogManager.ERROR);
	}

	public void refush() {
		entity = ShareDataTool.getGateWay(this);
		if (entity == null) {
			entity = new GatewayEntity("", "", "");
		}
		ip.setText(entity.ipAddress);
		id.setText(entity.identier);
		type.setText("HYV1.1");
	}

	@Override
	protected void onResume() {
		super.onResume();
		com.mu.smarthome.utils.LogManager.LogShow("-----", "00000000000000ddd",
				LogManager.ERROR);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.gateway_getid:

			break;
		case R.id.gateway_save:
			if (ToosUtils.isTextEmpty(ip)) {
				ToastUtils.displayShortToast(GatewayActivity.this, "网关IP不能为空");
				return;
			}
			entity.ipAddress = ToosUtils.getTextContent(ip);
			ShareDataTool.SaveGateWay(GatewayActivity.this, entity);
			ToastUtils.displayShortToast(GatewayActivity.this, "保存成功！");
			break;
		case R.id.gateway_cancel:

			break;

		default:
			break;
		}
	}
}
