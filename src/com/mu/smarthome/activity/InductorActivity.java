package com.mu.smarthome.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mu.smarthome.R;
import com.mu.smarthome.adapter.InductorAdapter;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inductor);
		initView();
	}

	private void initView() {
		back = (ImageView) findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		listView = (ListView) findViewById(R.id.inductor_listview);

		title.setText("人体感应器");
		back.setVisibility(View.VISIBLE);
		adapter = new InductorAdapter(this);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		listView.setAdapter(adapter);
	}

}
