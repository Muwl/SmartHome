package com.mu.smarthome.model;

import java.io.Serializable;

/**
 * @author Mu
 * @date 2015-10-19 下午2:47:27
 * @Description 设备实体类
 */
public class DeviceEntity implements Serializable {
	public String longAddress;
	public String shortAddress;
	public String type;
	public boolean running;
	public boolean waitting;
	public boolean selected;
	public String name;
	public String onIcon;
	public String location;
	public String controlLocation;
	public String currentPower;
	public String standbyPower;
	public String roomId;

}
