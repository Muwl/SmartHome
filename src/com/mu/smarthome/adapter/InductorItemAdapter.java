package com.mu.smarthome.adapter;

import java.util.List;

import com.alibaba.sdk.android.dpa.util.ToolKit;
import com.mu.smarthome.R;
import com.mu.smarthome.model.DeviceEntity;
import com.mu.smarthome.utils.ToosUtils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Mu
 * @date 2015-10-16下午9:08:57
 * @description 人体感应器之类适配器
 */
public class InductorItemAdapter extends BaseAdapter {

	private Context context;
	private List<DeviceEntity> entities;

	public InductorItemAdapter(Context context, List<DeviceEntity> entities) {
		super();
		this.context = context;
		this.entities = entities;
	}

	@Override
	public int getCount() {
		return entities.size();
	}

	@Override
	public Object getItem(int position) {
		return entities.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context,
					R.layout.activity_inductor_item_item, null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView
					.findViewById(R.id.inductor_item_image);
			holder.name = (TextView) convertView
					.findViewById(R.id.inductor_item_name);
			holder.location = (TextView) convertView
					.findViewById(R.id.inductor_item_locate);
			holder.state = (TextView) convertView
					.findViewById(R.id.inductor_item_state);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (entities.get(position).running) {
			holder.image.setImageResource(R.drawable.sensor_on);
			holder.state.setText("有人");
		} else {
			holder.image.setImageResource(R.drawable.sensor_off);
			holder.state.setText("无人");
		}
		holder.name.setText(entities.get(position).name);
		if (ToosUtils.isStringEmpty(entities.get(position).location)) {
			holder.location.setText("未设置");
		} else {
			holder.location.setText(entities.get(position).location);
		}

		return convertView;
	}

	class ViewHolder {
		public ImageView image;
		public TextView name;
		public TextView location;
		public TextView state;
	}

}
