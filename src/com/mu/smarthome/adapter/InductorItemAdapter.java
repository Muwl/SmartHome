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
 * @date 2015-10-16下午9:08:57
 * @description 人体感应器之类适配器
 */
public class InductorItemAdapter extends BaseAdapter {

	private Context context;

	public InductorItemAdapter(Context context) {
		super();
		this.context = context;
	}

	@Override
	public int getCount() {
		return 2;
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
		return convertView;
	}

	class ViewHolder {
		public ImageView image;
		public TextView name;
		public TextView location;
		public TextView state;
	}

}
