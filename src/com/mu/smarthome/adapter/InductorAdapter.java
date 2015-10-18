package com.mu.smarthome.adapter;

import com.mu.smarthome.R;
import com.mu.smarthome.adapter.InductorItemAdapter.ViewHolder;
import com.mu.smarthome.view.MyListView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Mu
 * @date 2015-10-16下午9:16:05
 * @description 体感应器之类适配器
 */
public class InductorAdapter extends BaseAdapter {

	private Context context;

	public InductorAdapter(Context context) {
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
					R.layout.activity_inductor_item, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView
					.findViewById(R.id.inductor_item_name);
			holder.listView = (MyListView) convertView
					.findViewById(R.id.inductor_item_listview);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		InductorItemAdapter adapter = new InductorItemAdapter(context);
		holder.listView.setAdapter(adapter);
		return convertView;
	}

	class ViewHolder {
		public TextView name;
		public MyListView listView;
	}

}
