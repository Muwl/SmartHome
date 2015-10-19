package com.mu.smarthome.adapter;

import com.mu.smarthome.R;

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

	public DeviceSerchAdapter(Context context) {
		super();
		this.context = context;
	}

	@Override
	public int getCount() {
		return 5;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
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
		return convertView;
	}

	class ViewHolder {
		public ImageView image;
		public TextView name;
		public TextView locate;
		public TextView address;
	}

}
