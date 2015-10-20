package com.mu.smarthome.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mu.smarthome.R;
import com.mu.smarthome.dialog.CustomeDialog;
import com.mu.smarthome.dialog.SetNameDialog;
import com.mu.smarthome.model.RoomEntity;

/**
 * @author Mu
 * @date 2015-10-17下午3:21:56
 * @description 房间设置适配器
 */
public class RoomSetAdapter extends BaseAdapter {

	private Context context;
	private List<RoomEntity> entities;
	private Handler handler;

	public RoomSetAdapter(Context context, List<RoomEntity> entities,
			Handler handler) {
		super();
		this.context = context;
		this.handler = handler;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.activity_roomset_item,
					null);
			holder.name = (TextView) convertView
					.findViewById(R.id.roomset_item_name);
			holder.edit = (ImageView) convertView
					.findViewById(R.id.roomset_item_edit);
			holder.del = (ImageView) convertView
					.findViewById(R.id.roomset_item_del);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SetNameDialog dialog = new SetNameDialog(context, entities
						.get(position).name, handler, position);
			}
		});

		holder.del.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				CustomeDialog customeDialog = new CustomeDialog(context,
						handler, "删除当前房间", position, -1);
			}
		});

		holder.name.setText(entities.get(position).name);
		return convertView;
	}

	class ViewHolder {
		public TextView name;
		public ImageView edit;
		public ImageView del;
	}

}
