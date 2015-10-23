package com.mu.smarthome.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
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
import com.mu.smarthome.model.DeviceEntity;
import com.mu.smarthome.model.RoomEntity;
import com.mu.smarthome.utils.Constant;
import com.mu.smarthome.utils.PowerUtils;
import com.mu.smarthome.utils.ShareDataTool;
import com.mu.smarthome.utils.ToosUtils;
import com.mu.smarthome.view.ScrollerNumberPicker;

/**
 * @author Mu
 * @date 2015-10-20下午7:41:58
 * @description
 */
public class DeviceSetDialog implements OnClickListener {
	private Dialog d = null;
	private View view = null;
	private Context context = null;
	private TextView cancel;
	int height;
	private TextView devswitch;
	private TextView locate;
	private TextView declocate;
	private TextView curpower;
	private TextView standpower;
	private Handler handler;
	private DeviceEntity entity;

	public static final int RETURN_OK = 144;

	public DeviceSetDialog(Context context, Handler handler, DeviceEntity entity) {
		super();
		this.context = context;
		this.handler = handler;
		this.entity = entity;
		d = new Dialog(context);

		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		view = View.inflate(context, R.layout.dialog_deviceset, null);
		d.setContentView(view);
		devswitch = (TextView) d.findViewById(R.id.deviceset_switch);
		locate = (TextView) d.findViewById(R.id.deviceset_locate);
		declocate = (TextView) d.findViewById(R.id.deviceset_devicelocate);
		curpower = (TextView) d.findViewById(R.id.deviceset_curpower);
		standpower = (TextView) d.findViewById(R.id.deviceset_standpower);
		cancel = (TextView) d.findViewById(R.id.deviceset_cancel);

		if (entity.running) {
			devswitch.setText("打开");
			devswitch.setTextColor(Color.rgb(0, 189, 255));
		} else {
			devswitch.setText("关闭");
			devswitch.setTextColor(Color.rgb(255, 71, 71));
		}
		if (ToosUtils.isStringEmpty(entity.location)) {
			locate.setText("");
		} else {
			locate.setText(entity.location);
		}
		if (ToosUtils.isStringEmpty(entity.controlLocation)) {
			declocate.setText("");
		} else {
			declocate.setText(entity.controlLocation);
		}
		if (ToosUtils.isStringEmpty(entity.currentPower)) {
			curpower.setText("");
		} else {
			curpower.setText(PowerUtils.getPEString(entity.currentPower) + "W");
		}
		// if (ToosUtils.isStringEmpty(entity.standbyPower)) {
		// standpower.setText("");
		// } else {
		standpower.setText("0W");
		// }
		devswitch.setOnClickListener(this);
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
		case R.id.deviceset_cancel:
			dialogAnimation(d, view, height, getWindowHeight(), true);
			break;
		case R.id.deviceset_switch:
			Message message = new Message();
			message.what = RETURN_OK;
			message.obj = entity;
			handler.sendMessage(message);
			dialogAnimation(d, view, height, getWindowHeight(), true);
			break;

		default:
			break;
		}
	}

}
