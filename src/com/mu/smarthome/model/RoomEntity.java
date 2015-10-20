package com.mu.smarthome.model;

import java.io.Serializable;

import com.mu.smarthome.activity.BaseActivity;

/**
 * @author Mu
 * @date 2015-10-19 下午2:37:23
 * @Description 房间实体类
 */
public class RoomEntity implements Serializable {
	public String name;
	public String roomId;// 0:全部  1:其他
	@Override
	public String toString() {
		return "RoomEntity [name=" + name + ", roomId=" + roomId + "]";
	}
	public RoomEntity(String name, String roomId) {
		super();
		this.name = name;
		this.roomId = roomId;
	}
	public RoomEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	

}
