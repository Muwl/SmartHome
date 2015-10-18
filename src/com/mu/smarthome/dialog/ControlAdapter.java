package com.mu.smarthome.dialog;

import com.mu.smarthome.R;
import com.mu.smarthome.utils.DensityUtil;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * @author Mu
 * @date 2015-10-18下午1:29:49
 * @description 控制适配器
 */
public class ControlAdapter extends BaseAdapter {

	private Context context;
	private int width;

	public ControlAdapter(Context context,int width) {
		super();
		this.context = context;
		this.width=width;
	}

	@Override
	public int getCount() {
		return 10;
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
		ViewHolder holder=null;
		if (convertView==null) {
			convertView=View.inflate(context, R.layout.activity_control_item, null);
			holder=new ViewHolder();
			holder.root=convertView.findViewById(R.id.control_item_root);
			holder.checkBox=(CheckBox) convertView.findViewById(R.id.control_item_check);
			holder.imageView=(ImageView) convertView.findViewById(R.id.control_item_image);
			holder.name=(TextView) convertView.findViewById(R.id.control_item_name);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		LayoutParams params=(LayoutParams) holder.root.getLayoutParams();
		params.width=(width-DensityUtil.dip2px(context, 52))/3;
		params.height=(width-DensityUtil.dip2px(context, 52))/3;
		holder.root.setLayoutParams(params);
		return convertView;
	}

	class ViewHolder {
		public View root;
		public CheckBox checkBox;
		public ImageView imageView;
		public TextView name;
	}

}
