package com.mu.smarthome.adapter;

import java.util.List;

import com.mu.smarthome.R;
import com.mu.smarthome.adapter.InductorItemAdapter.ViewHolder;
import com.mu.smarthome.model.InductorEntity;
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
	private List<InductorEntity> entities;

	public InductorAdapter(Context context, List<InductorEntity> entities) {
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

		holder.name.setText(entities.get(position).entity.name);
		InductorItemAdapter adapter = new InductorItemAdapter(context,
				entities.get(position).deviceEntities);
		holder.listView.setAdapter(adapter);
		return convertView;
	}

	class ViewHolder {
		public TextView name;
		public MyListView listView;
	}

}
