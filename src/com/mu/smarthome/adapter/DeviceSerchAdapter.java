package com.mu.smarthome.adapter;

import java.util.List;

import com.alibaba.sdk.android.dpa.util.ToolKit;
import com.mu.smarthome.R;
import com.mu.smarthome.model.DeviceEntity;
import com.mu.smarthome.model.RoomEntity;
import com.mu.smarthome.utils.ToosUtils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Mu
 * @date 2015-10-17下午4:29:26
 * @description 设备搜索适配器
 */
public class DeviceSerchAdapter extends BaseAdapter {

	private Context context;
	private List<DeviceEntity> entities;
	private List<RoomEntity> roomEntities;

	public DeviceSerchAdapter(Context context, List<DeviceEntity> entities,
			List<RoomEntity> roomEntities) {
		super();
		this.context = context;
		this.entities = entities;
		this.roomEntities = roomEntities;

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
			holder = new ViewHolder();
			convertView = View.inflate(context,
					R.layout.activity_diviceserch_item, null);
			holder.image = (ImageView) convertView
					.findViewById(R.id.diviceserch_item_image);
			holder.name = (TextView) convertView
					.findViewById(R.id.diciceserch_item_name);
			holder.locate = (TextView) convertView
					.findViewById(R.id.diciceserch_item_locate);
			holder.address = (TextView) convertView
					.findViewById(R.id.diciceserch_item_address);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.name.setText(entities.get(position).name);
		if (ToosUtils.isStringEmpty(entities.get(position).location)) {
			holder.address.setText("");
		} else {
			holder.address.setText(entities.get(position).location);
		}
		if (ToosUtils.isStringEmpty(entities.get(position).roomId)
				|| roomEntities == null) {
			holder.locate.setText(entities.get(position).longAddress
					.substring(8) + "");
		} else {
			for (int i = 0; i < roomEntities.size(); i++) {
				if (entities.get(position).roomId
						.equals(roomEntities.get(i).roomId)) {
					holder.locate
							.setText(entities.get(position).longAddress
									.substring(8)
									+ "\u3000"
									+ roomEntities.get(i).name);
				}
			}
		}

		holder.image.setImageResource(ToosUtils.getDrawable(
				entities.get(position).type, entities.get(position).running));
		return convertView;
	}

	class ViewHolder {
		public ImageView image;
		public TextView name;
		public TextView locate;
		public TextView address;
	}

}
