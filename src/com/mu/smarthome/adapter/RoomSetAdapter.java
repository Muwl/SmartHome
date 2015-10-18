package com.mu.smarthome.adapter;

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

/**
 * @author Mu
 * @date 2015-10-17下午3:21:56
 * @description 房间设置适配器
 */
public class RoomSetAdapter extends BaseAdapter {

	private Context context;
	private Handler handler;

	public RoomSetAdapter(Context context, Handler handler) {
		super();
		this.context = context;
		this.handler = handler;
	}

	@Override
	public int getCount() {
		return 4;
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
				SetNameDialog dialog = new SetNameDialog(context, "ddd",
						handler);
			}
		});

		holder.del.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				CustomeDialog customeDialog = new CustomeDialog(context,
						handler, "删除当前房间", position, -1);
			}
		});
		return convertView;
	}

	class ViewHolder {
		public TextView name;
		public ImageView edit;
		public ImageView del;
	}

}
