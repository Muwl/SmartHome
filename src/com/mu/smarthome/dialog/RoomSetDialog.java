package com.mu.smarthome.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.mu.smarthome.R;
import com.mu.smarthome.model.RoomEntity;
import com.mu.smarthome.utils.Constant;
import com.mu.smarthome.utils.ShareDataTool;
import com.mu.smarthome.view.ScrollerNumberPicker;

/**
 * @author Mu
 * @date 2015-8-6 上午9:23:47
 * @Description 行业选择
 */
public class RoomSetDialog implements OnClickListener {
	private Dialog d = null;
	private View view = null;
	private Context context = null;
	private TextView ok;
	private TextView cancel;
	private ScrollerNumberPicker picker;
	int height;
	private Handler handler;
	private String roomId;
	private int index = 0;
	private ArrayList<RoomEntity> entities;

	public RoomSetDialog(Context context, Handler handler, String roomId) {
		super();
		this.context = context;
		this.handler = handler;
		this.roomId = roomId;
		d = new Dialog(context);

		entities = (ArrayList<RoomEntity>) ShareDataTool.getRooms(context);
		if (entities == null) {
			entities = new ArrayList<RoomEntity>();
		}
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i).roomId.equals(roomId)) {
				index = i;
			}
		}

		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		view = View.inflate(context, R.layout.trade_select, null);
		d.setContentView(view);
		ok = (TextView) d.findViewById(R.id.trade_ok);
		cancel = (TextView) d.findViewById(R.id.trade_cancel);
		picker = (ScrollerNumberPicker) d.findViewById(R.id.trade_numberPicker);
		picker.setData(entities);
		picker.setDefault(index);
		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);
		init();
	}

	private void init() {
		Window dialogWindow = d.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		lp.width = LayoutParams.MATCH_PARENT;
		// dialogWindow.requestFeature(Window.FEATURE_NO_TITLE);
		lp.height = LayoutParams.WRAP_CONTENT;
		dialogWindow
				.setBackgroundDrawableResource(R.drawable.background_dialog);
		height = lp.height;
		d.show();
		dialogAnimation(d, view, getWindowHeight(), height, false);
	}

	private int getWindowHeight() {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		return dm.widthPixels;
	}

	private void dialogAnimation(final Dialog d, View v, int from, int to,
			boolean needDismiss) {

		Animation anim = new TranslateAnimation(0, 0, from, to);
		anim.setFillAfter(true);
		anim.setDuration(500);
		if (needDismiss) {
			anim.setAnimationListener(new AnimationListener() {

				public void onAnimationStart(Animation animation) {
				}

				public void onAnimationRepeat(Animation animation) {
				}

				public void onAnimationEnd(Animation animation) {
					d.dismiss();
				}
			});

		}
		v.startAnimation(anim);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.trade_cancel:
			dialogAnimation(d, view, height, getWindowHeight(), true);
			break;
		case R.id.trade_ok:
			dialogAnimation(d, view, height, getWindowHeight(), true);
			Message message = new Message();
			message.what = 91;
			message.obj = entities.get(picker.getSelected());
			handler.sendMessage(message);
			break;

		default:
			break;
		}
	}

}
