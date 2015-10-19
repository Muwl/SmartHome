package com.mu.smarthome.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.mu.smarthome.R;
import com.mu.smarthome.utils.ShareDataTool;
import com.mu.smarthome.utils.ToastUtils;
import com.mu.smarthome.utils.ToosUtils;

/**
 * @author Mu
 * @date 2015-3-6
 * @description 添加备注对话框
 */
public class SetGateIdDialog extends Dialog implements
		android.view.View.OnClickListener {
	private Context context;
	private Handler handler;
	private TextView ok;
	private TextView cancel;
	private EditText editText;
	private int flag;
	public SetGateIdDialog(Context context, Handler handler,int flag) {
		super(context, R.style.dialog2);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.handler = handler;
		this.context = context;
		this.flag=flag;
		setContentView(R.layout.addmark_dialog);
		getWindow().setBackgroundDrawable(new BitmapDrawable());
		show();
		initView();

	}

	private void initView() {
		ok = (TextView) findViewById(R.id.addmark_dialog_ok);
		cancel = (TextView) findViewById(R.id.addmark_dialog_cancel);
		editText = (EditText) findViewById(R.id.addmark_dialog_name);
		// titleText.setText(title);
		editText.setHint("请输入网关Id");
		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addmark_dialog_ok:
			if (ToosUtils.isTextEmpty(editText)) {
				ToastUtils.displayShortToast(context, "网关Id不能为空！");
				return;
			}
			ShareDataTool.SaveGateId(context,
					ToosUtils.getTextContent(editText));
			Message message = new Message();
			message.what = flag;
			message.obj = ToosUtils.getTextContent(editText);
			handler.sendMessage(message);
			dismiss();
			break;
		case R.id.addmark_dialog_cancel:
//			handler.sendEmptyMessage(108);
			dismiss();
			break;
		default:
			break;
		}

	}

}
