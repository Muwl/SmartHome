package com.mu.smarthome.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.mu.smarthome.R;
import com.mu.smarthome.adapter.ControlAdapter;
import com.mu.smarthome.dialog.DeviceSetDialog;
import com.mu.smarthome.dialog.RoomSetDialog;
import com.mu.smarthome.model.DeviceEntity;
import com.mu.smarthome.model.InductorEntity;
import com.mu.smarthome.model.RoomEntity;
import com.mu.smarthome.utils.Connection;
import com.mu.smarthome.utils.DensityUtil;
import com.mu.smarthome.utils.ShareDataTool;
import com.mu.smarthome.utils.ToastUtils;
import com.mu.smarthome.utils.ToosUtils;

/**
 * @author Mu
 * @date 2015-10-16下午7:27:08
 * @description 主页面
 */
public class MainActivity extends BaseActivity implements OnClickListener {

	private TextView title;

	private ImageView rig;

	private View emptyView;

	private TextView controldivice;

	private RadioGroup bottomGroup;

	private RadioButton control;

	private ImageView switchImageView;

	private RadioButton setting;

	private ImageView mImageView;
	private float mCurrentCheckedRadioLeft;// 当前被选中的RadioButton距离左侧的距离
	// private HorizontalScrollView mHorizontalScrollView;// 上面的水平滚动控件
	private ViewPager mViewPager; // 下方的可横向拖动的控件
	private ArrayList<View> mViews;// 用来存放下方滚动的layout(layout_1,layout_2,layout_3)

	LocalActivityManager manager = null;

	private RadioGroup myRadioGroup;

	private int _id = 1000;

	private LinearLayout layout, titleLayout;

	private TextView textView;

	private List<Map<String, Object>> titleList = new ArrayList<Map<String, Object>>();

	private int evryWidth;

	private View tab1;

	private View tab2;

	private RadioGroup group;

	private List<ImageView> imageViews = new ArrayList<ImageView>();

	private List<RadioButton> buttons = new ArrayList<RadioButton>();

	private View controlView;

	private CheckBox allcheBox;

	private GridView gridView;

	private ControlAdapter adapter;

	private int width;

	private List<InductorEntity> inductorEntities;

	private List<DeviceEntity> gridEntities;

	private Connection connection;

	private View pro;

	private boolean switchFlag = false;// true代表现在处于关闭状态 false 代表处于打开状态

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case DeviceSetDialog.RETURN_OK:
				DeviceEntity entity = (DeviceEntity) msg.obj;
				if (entity.running) {
					pro.setVisibility(View.VISIBLE);
					connection.close(entity, handler);
				} else {
					pro.setVisibility(View.VISIBLE);
					connection.open(entity, handler);
				}
				break;
			case Connection.CLOSE_SUCC:
				pro.setVisibility(View.GONE);
				String longAddress = (String) msg.obj;
				for (int i = 0; i < gridEntities.size(); i++) {
					if (gridEntities.get(i).longAddress.equals(longAddress)) {
						gridEntities.get(i).running = !gridEntities.get(i).running;
					}
				}
				ToastUtils.displayShortToast(MainActivity.this, "操作成功！");
				adapter.notifyDataSetChanged();
				break;
			case Connection.OPEN_SUCC:
				pro.setVisibility(View.GONE);
				String longAddress1 = (String) msg.obj;
				for (int i = 0; i < gridEntities.size(); i++) {
					if (gridEntities.get(i).longAddress.equals(longAddress1)) {
						gridEntities.get(i).running = !gridEntities.get(i).running;
					}
				}
				ToastUtils.displayShortToast(MainActivity.this, "操作成功！");
				adapter.notifyDataSetChanged();
				break;
			case Connection.ERROR_CODE:
				pro.setVisibility(View.GONE);
				ToastUtils.displayShortToast(MainActivity.this, "操作失败！");
				break;

			case Connection.OPENALL_SUCC:
				pro.setVisibility(View.GONE);
				List<DeviceEntity> deviceEntities = (List<DeviceEntity>) msg.obj;
				for (int j = 0; j < gridEntities.size(); j++) {
					for (int j2 = 0; j2 < deviceEntities.size(); j2++) {
						if (gridEntities.get(j).longAddress
								.equals(deviceEntities.get(j2).longAddress)) {
							gridEntities.get(j).running = true;
						}
					}
				}
				ToastUtils.displayShortToast(MainActivity.this, "操作成功！");
				adapter.notifyDataSetChanged();
				break;
			case Connection.CLOSEALL_SUCC:
				pro.setVisibility(View.GONE);
				List<DeviceEntity> deviceEntities1 = (List<DeviceEntity>) msg.obj;
				for (int j = 0; j < gridEntities.size(); j++) {
					for (int j2 = 0; j2 < deviceEntities1.size(); j2++) {
						if (gridEntities.get(j).longAddress
								.equals(deviceEntities1.get(j2).longAddress)) {
							gridEntities.get(j).running = false;
						}
					}
				}
				ToastUtils.displayShortToast(MainActivity.this, "操作成功！");
				adapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		connection = new Connection(this);
		manager = new LocalActivityManager(this, true);
		manager.dispatchCreate(savedInstanceState);
		evryWidth = DensityUtil.getScreenWidth(this) / 3;
		width = DensityUtil.getScreenWidth(this);
		gridEntities = new ArrayList<DeviceEntity>();
		getTitleInfo();
		initView();
		iniListener();
		iniVariable();
		mViewPager.setCurrentItem(0);
	}

	private void getTitleInfo() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", 0);
		map.put("title", "房间设置");
		titleList.add(map);
		map = new HashMap<String, Object>();
		map.put("id", 1);
		map.put("title", "设备搜索");
		titleList.add(map);

		map = new HashMap<String, Object>();
		map.put("id", 2);
		map.put("title", "网关配置");
		titleList.add(map);

	}

	private void initView() {
		title = (TextView) findViewById(R.id.title_title);
		rig = (ImageView) findViewById(R.id.title_rig);
		emptyView = findViewById(R.id.main_tab1_empty);
		controldivice = (TextView) findViewById(R.id.control_add);
		bottomGroup = (RadioGroup) findViewById(R.id.main_rg);
		control = (RadioButton) findViewById(R.id.main_bottom_control);
		switchImageView = (ImageView) findViewById(R.id.main_bottom_switch);
		setting = (RadioButton) findViewById(R.id.main_bottom_setting);
		pro = findViewById(R.id.main_pro);
		tab1 = findViewById(R.id.main_tab1);
		tab2 = findViewById(R.id.main_tab2);
		titleLayout = (LinearLayout) findViewById(R.id.main_tab2_title_lay);
		layout = (LinearLayout) findViewById(R.id.main_tab2_lay);
		mImageView = (ImageView) findViewById(R.id.main_tab2_bomimg);

		mViewPager = (ViewPager) findViewById(R.id.main_tab2_pager);

		group = (RadioGroup) findViewById(R.id.control_group);
		controlView = findViewById(R.id.control_root);
		allcheBox = (CheckBox) findViewById(R.id.control_allcheck);
		gridView = (GridView) findViewById(R.id.control_grid);
		rig.setOnClickListener(this);
		rig.setVisibility(View.VISIBLE);
		controldivice.setOnClickListener(this);
		switchImageView.setOnClickListener(this);

		title.setText("控制");

		bottomGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				if (checkedId == R.id.main_bottom_control) {
					tab1.setVisibility(View.VISIBLE);
					tab2.setVisibility(View.GONE);
					title.setText("控制");
					rig.setVisibility(View.VISIBLE);
					onrefush();
				} else {
					tab1.setVisibility(View.GONE);
					tab2.setVisibility(View.VISIBLE);
					title.setText("配置");
					rig.setVisibility(View.GONE);

				}

			}
		});

		myRadioGroup = new RadioGroup(this);
		myRadioGroup.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		myRadioGroup.setOrientation(LinearLayout.HORIZONTAL);
		layout.addView(myRadioGroup);
		for (int i = 0; i < titleList.size(); i++) {
			Map<String, Object> map = titleList.get(i);
			RadioButton radio = new RadioButton(this);

			radio.setBackgroundResource(android.R.color.transparent);
			radio.setButtonDrawable(android.R.color.transparent);
			LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
					Gravity.CENTER);
			l.weight = 1;
			l.width = evryWidth;
			l.height = DensityUtil.dip2px(this, 45);
			radio.setLayoutParams(l);
			radio.setGravity(Gravity.CENTER);
			// radio.setPadding(20, 15, 20, 15);
			// radio.setPadding(left, top, right, bottom)
			radio.setId(_id + i);
			radio.setTextColor(getResources().getColorStateList(
					R.color.bottom_text_bn));
			radio.setText(map.get("title") + "");
			radio.setTag(map);
			if (i == 0) {
				radio.setChecked(true);
				int itemWidth = (int) radio.getPaint().measureText(
						map.get("title") + "");
				mImageView.setLayoutParams(new LinearLayout.LayoutParams(
						evryWidth, DensityUtil.dip2px(this, 2)));
			}
			myRadioGroup.addView(radio);
		}
		myRadioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// Map<String, Object> map = (Map<String, Object>)
						// group.getChildAt(checkedId).getTag();
						int radioButtonId = group.getCheckedRadioButtonId();
						// 根据ID获取RadioButton的实例
						RadioButton rb = (RadioButton) findViewById(radioButtonId);
						Map<String, Object> selectMap = (Map<String, Object>) rb
								.getTag();

						AnimationSet animationSet = new AnimationSet(true);
						TranslateAnimation translateAnimation;
						translateAnimation = new TranslateAnimation(
								mCurrentCheckedRadioLeft, rb.getLeft(), 0f, 0f);
						animationSet.addAnimation(translateAnimation);
						animationSet.setFillBefore(true);
						animationSet.setFillAfter(true);
						animationSet.setDuration(300);

						mImageView.startAnimation(animationSet);// 开始上面蓝色横条图片的动画切换
						mViewPager.setCurrentItem(radioButtonId - _id);// 让下方ViewPager跟随上面的HorizontalScrollView切换
						mCurrentCheckedRadioLeft = rb.getLeft();// 更新当前蓝色横条距离左边的距离
						// mHorizontalScrollView.smoothScrollTo(
						// (int) mCurrentCheckedRadioLeft - evryWidth, 0);

						mImageView
								.setLayoutParams(new LinearLayout.LayoutParams(
										evryWidth, DensityUtil.dip2px(
												MainActivity.this, 2)));

					}
				});

		//

		inductorEntities = new ArrayList<InductorEntity>();

		adapter = new ControlAdapter(this, gridEntities, width);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				gridEntities.get(position).selected = !gridEntities
						.get(position).selected;
				adapter.notifyDataSetChanged();

				setSwitch();
			}
		});

		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				DeviceSetDialog dialog = new DeviceSetDialog(MainActivity.this,
						handler, gridEntities.get(position));
				return true;
			}
		});

		allcheBox
				.setOnCheckedChangeListener(new android.widget.CheckBox.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean flag) {
						for (int j = 0; j < gridEntities.size(); j++) {
							gridEntities.get(j).selected = flag;
						}
						adapter.notifyDataSetChanged();

						setSwitch();
					}

				});

		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int radioButtonId = group.getCheckedRadioButtonId();
				int s = radioButtonId - 2000;
				if (s < 0) {
					return;
				}
				for (int i = 0; i < buttons.size(); i++) {
					if (i == s) {
						LayoutParams params = (LayoutParams) buttons.get(i)
								.getLayoutParams();
						params.width = DensityUtil
								.dip2px(MainActivity.this, 96);
						params.height = DensityUtil.dip2px(MainActivity.this,
								96);
						buttons.get(i).setLayoutParams(params);

					} else {
						LayoutParams params = (LayoutParams) buttons.get(i)
								.getLayoutParams();
						params.width = DensityUtil
								.dip2px(MainActivity.this, 80);
						params.height = DensityUtil.dip2px(MainActivity.this,
								80);
						buttons.get(i).setLayoutParams(params);
					}
				}

				for (int i = 0; i < imageViews.size(); i++) {
					if (i == s || s + 1 == i) {
						imageViews.get(i).setBackgroundResource(
								R.color.divider_red);
					} else {
						imageViews.get(i)
								.setBackgroundResource(R.color.divider);
					}
				}

				gridEntities.clear();
				for (int j = 0; j < inductorEntities.get(s).deviceEntities
						.size(); j++) {
					inductorEntities.get(s).deviceEntities.get(j).selected = false;
					gridEntities.add(inductorEntities.get(s).deviceEntities
							.get(j));
				}
				allcheBox.setChecked(false);
				adapter.notifyDataSetChanged();

				setSwitch();

			}
		});

		// onrefush();
		bottomGroup.check(R.id.main_bottom_control);

	}

	public void onSerchFush() {
		List<DeviceEntity> list = ShareDataTool.getDevice(this);
		if (list == null) {
			list = new ArrayList<DeviceEntity>();
		}
		for (int i = 0; i < inductorEntities.size(); i++) {
			for (int j = 0; j < inductorEntities.get(i).deviceEntities.size(); j++) {
				for (int j2 = 0; j2 < list.size(); j2++) {
					if (inductorEntities.get(i).deviceEntities.get(j).longAddress
							.equals(list.get(j2).longAddress)) {
						inductorEntities.get(i).deviceEntities.set(j,
								list.get(j2));
					}
				}
			}
		}

		for (int i = 0; i < gridEntities.size(); i++) {
			for (int j = 0; j < list.size(); j++) {
				if (gridEntities.get(i).longAddress
						.equals(list.get(j).longAddress)) {
					gridEntities.set(i, list.get(j));
				}
			}
		}
		adapter.notifyDataSetChanged();

	}

	public void onrefush() {
		inductorEntities.clear();
		List<DeviceEntity> deviceEntities = ShareDataTool.getDevice(this);
		List<RoomEntity> roomEntities = ShareDataTool.getRooms(this);
		if (deviceEntities == null) {
			deviceEntities = new ArrayList<DeviceEntity>();
		}
		if (roomEntities == null) {
			roomEntities = new ArrayList<RoomEntity>();
		}
		for (int i = 0; i < roomEntities.size(); i++) {
			InductorEntity entity = new InductorEntity();
			entity.entity = roomEntities.get(i);
			List<DeviceEntity> list = new ArrayList<DeviceEntity>();
			for (int j = 0; j < deviceEntities.size(); j++) {
				if (roomEntities.get(i).roomId
						.equals(deviceEntities.get(j).roomId)
						&& !"03".equals(deviceEntities.get(j).type)) {
					list.add(deviceEntities.get(j));
				}
			}
			entity.deviceEntities = list;
			inductorEntities.add(entity);
		}
		InductorEntity entity = new InductorEntity();
		entity.entity = new RoomEntity("其他", String.valueOf(System
				.currentTimeMillis() / 1000));
		List<DeviceEntity> list = new ArrayList<DeviceEntity>();
		for (int i = 0; i < deviceEntities.size(); i++) {
			if (ToosUtils.isStringEmpty(deviceEntities.get(i).roomId)) {
				list.add(deviceEntities.get(i));
			}
		}
		entity.deviceEntities = list;
		if (list.size() != 0) {
			inductorEntities.add(entity);
		}

		if (roomEntities.size() == 0 && deviceEntities.size() != 0) {
			inductorEntities.clear();
			InductorEntity entity1 = new InductorEntity();
			entity1.entity = new RoomEntity("全部", String.valueOf(System
					.currentTimeMillis() / 1000));
			entity1.deviceEntities = deviceEntities;
			inductorEntities.add(entity1);
		}
		group.removeAllViews();
		imageViews.clear();
		buttons.clear();

		for (int i = 0; i < inductorEntities.size(); i++) {
			ImageView imageView = new ImageView(this);
			group.addView(imageView);
			android.widget.RadioGroup.LayoutParams params1 = (android.widget.RadioGroup.LayoutParams) imageView
					.getLayoutParams();
			params1.width = DensityUtil.dip2px(this, 52);
			params1.height = DensityUtil.dip2px(this, 1);
			imageView.setLayoutParams(params1);
			imageView.setBackgroundResource(R.color.divider);
			imageViews.add(imageView);

			RadioButton button = new RadioButton(this);
			button.setBackgroundResource(R.drawable.rid_rb);
			button.setButtonDrawable(android.R.color.transparent);
			button.setTextColor(Color.rgb(100, 100, 100));
			button.setGravity(Gravity.CENTER);
			button.setSingleLine(true);
			button.setTextColor(getResources().getColorStateList(
					R.color.bottom_text_bn));
			group.addView(button);
			android.widget.RadioGroup.LayoutParams params2 = (android.widget.RadioGroup.LayoutParams) button
					.getLayoutParams();
			params2.width = DensityUtil.dip2px(this, 80);
			params2.height = DensityUtil.dip2px(this, 80);
			params2.gravity = Gravity.CENTER;
			button.setId(2000 + i);
			button.setLayoutParams(params2);
			button.setText(inductorEntities.get(i).entity.name);
			buttons.add(button);

			if (i == inductorEntities.size() - 1) {
				ImageView imageView2 = new ImageView(this);
				group.addView(imageView2);
				android.widget.RadioGroup.LayoutParams params3 = (android.widget.RadioGroup.LayoutParams) imageView2
						.getLayoutParams();
				params3.width = DensityUtil.dip2px(this, 52);
				params3.height = DensityUtil.dip2px(this, 1);
				imageView2.setLayoutParams(params3);
				imageView2.setBackgroundResource(R.color.divider);
				imageViews.add(imageView2);
			}

		}

		if (buttons.size() != 0) {
			group.clearCheck();
			group.check(buttons.get(0).getId());
			emptyView.setVisibility(View.GONE);
			controlView.setVisibility(View.VISIBLE);
		} else {
			emptyView.setVisibility(View.VISIBLE);
			controlView.setVisibility(View.GONE);
		}

	}

	private void setSwitch() {
		boolean b = false;
		for (int i = 0; i < gridEntities.size(); i++) {
			if (!gridEntities.get(i).selected) {
				continue;
			}
			if (gridEntities.get(i).running) {
				b = false;
				break;

			}
			b = true;
		}
		if (b) {
			switchImageView.setImageResource(R.drawable.tab_switch_on);
			switchFlag = true;
		} else {
			switchImageView.setImageResource(R.drawable.tab_switch_off);
			switchFlag = false;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_rig:
			Intent intent = new Intent(MainActivity.this,
					InductorActivity.class);
			startActivity(intent);
			break;
		case R.id.control_add:
			bottomGroup.check(R.id.main_bottom_setting);
			myRadioGroup.check(_id + 1);

			break;
		case R.id.main_bottom_switch:
			List<DeviceEntity> entities = new ArrayList<DeviceEntity>();
			for (int i = 0; i < gridEntities.size(); i++) {
				if (!gridEntities.get(i).selected) {
					continue;
				}
				if (switchFlag && !gridEntities.get(i).running) {
					entities.add(gridEntities.get(i));
				} else if (!switchFlag && !gridEntities.get(i).running) {
					entities.add(gridEntities.get(i));
				}
			}

			if (entities.size() == 0) {
				ToastUtils.displayShortToast(MainActivity.this, "未选中设备");
				return;
			}

			if (switchFlag) {
				pro.setVisibility(View.VISIBLE);
				connection.openAll(entities, handler);
			} else {
				pro.setVisibility(View.VISIBLE);
				connection.closeAll(entities, handler);
			}

			break;

		default:
			break;
		}
	}

	private View getView(String id, Intent intent) {
		return manager.startActivity(id, intent).getDecorView();
	}

	private void iniVariable() {
		mViews = new ArrayList<View>();
		// for (int i = 0; i < titleList.size(); i++) {
		Intent intent = new Intent(this, RoomSetActivity.class);
		mViews.add(getView("View", intent));
		Intent intent1 = new Intent(this, DeviceSerchActivity.class);
		mViews.add(getView("View1", intent1));
		Intent intent2 = new Intent(this, GatewayActivity.class);
		mViews.add(getView("View2", intent2));
		// }
		mViewPager.setAdapter(new MyPagerAdapter());// 设置ViewPager的适配器
	}

	private void iniListener() {
		mViewPager.setOnPageChangeListener(new MyPagerOnPageChangeListener());
	}

	/**
	 * ViewPager的适配器
	 */
	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public void destroyItem(View v, int position, Object obj) {
			// TODO Auto-generated method stub
			((ViewPager) v).removeView(mViews.get(position));
		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mViews.size();
		}

		@Override
		public Object instantiateItem(View v, int position) {
			((ViewPager) v).addView(mViews.get(position));
			return mViews.get(position);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub
		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub
		}

	}

	/**
	 * ViewPager的PageChangeListener(页面改变的监听器)
	 */
	private class MyPagerOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
		}

		/**
		 * 滑动ViewPager的时候,让上方的HorizontalScrollView自动切换
		 */
		@Override
		public void onPageSelected(int position) {
			RadioButton radioButton = (RadioButton) findViewById(_id + position);
			radioButton.performClick();
			if (position == 0) {
				RoomSetActivity activity = (RoomSetActivity) manager
						.getActivity("View");
				activity.refush();
			} else if (position == 1) {
				DeviceSerchActivity activity = (DeviceSerchActivity) manager
						.getActivity("View1");
				activity.refush();
			} else if (position == 2) {
				GatewayActivity activity = (GatewayActivity) manager
						.getActivity("View2");
				activity.refush();
			}

			Log.e("----------", "ddd" + manager.getActivity("View"));
			// manager.getCurrentActivity()
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (myRadioGroup.getCheckedRadioButtonId() - _id == 0) {
			RoomSetActivity activity = (RoomSetActivity) manager
					.getActivity("View");
			activity.refush();
		} else if (myRadioGroup.getCheckedRadioButtonId() - _id == 1) {
			DeviceSerchActivity activity = (DeviceSerchActivity) manager
					.getActivity("View1");
			activity.refush();
		} else if (myRadioGroup.getCheckedRadioButtonId() - _id == 2) {
			GatewayActivity activity = (GatewayActivity) manager
					.getActivity("View2");
			activity.refush();
		}

		Log.e("----------", "ddd" + manager.getActivity("View"));
	}

}
