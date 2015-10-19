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
	public DeviceEntity(String longAddress, String shortAddress, String type,
			boolean running, boolean waitting, boolean selected, String name,
			String onIcon, String location, String controlLocation,
			String currentPower, String standbyPower, String roomId) {
		super();
		this.longAddress = longAddress;
		this.shortAddress = shortAddress;
		this.type = type;
		this.running = running;
		this.waitting = waitting;
		this.selected = selected;
		this.name = name;
		this.onIcon = onIcon;
		this.location = location;
		this.controlLocation = controlLocation;
		this.currentPower = currentPower;
		this.standbyPower = standbyPower;
		this.roomId = roomId;
	}
	@Override
	public String toString() {
		return "DeviceEntity [longAddress=" + longAddress + ", shortAddress="
				+ shortAddress + ", type=" + type + ", running=" + running
				+ ", waitting=" + waitting + ", selected=" + selected
				+ ", name=" + name + ", onIcon=" + onIcon + ", location="
				+ location + ", controlLocation=" + controlLocation
				+ ", currentPower=" + currentPower + ", standbyPower="
				+ standbyPower + ", roomId=" + roomId + "]";
	}
	
	
	
	

}
