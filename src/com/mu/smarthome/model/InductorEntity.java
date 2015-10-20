package com.mu.smarthome.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author Mu
 * @date 2015-10-20 上午9:50:11
 * @Description 人体感应实体类
 */
public class InductorEntity implements Serializable {

	public RoomEntity entity;

	public List<DeviceEntity> deviceEntities;
}
