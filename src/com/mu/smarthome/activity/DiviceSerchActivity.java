package com.mu.smarthome.activity;

import java.util.Dictionary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.mu.smarthome.R;
import com.mu.smarthome.adapter.DiviceSerchAdapter;

/**
 * @author Mu
 * @date 2015-10-17下午2:45:12
 * @description
 */
public class DiviceSerchActivity extends BaseActivity implements
		OnClickListener {

	private ListView listView;

	private TextView serch;

	private TextView down;

	private TextView save;

	private DiviceSerchAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diviceserch);
		initView();
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.diviceserch_listview);
		serch = (TextView) findViewById(R.id.diviceserch_serchroom);
		down = (TextView) findViewById(R.id.diviceserch_down);
		save = (TextView) findViewById(R.id.diviceserch_save);
		adapter = new DiviceSerchAdapter(this);
		listView.setAdapter(adapter);

		serch.setOnClickListener(this);
		down.setOnClickListener(this);
		save.setOnClickListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(DiviceSerchActivity.this,
						DiviceSetActivity.class);
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

			break;
		case R.id.diviceserch_save:

			break;

		default:
			break;
		}
	}
}
