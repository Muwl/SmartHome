package com.mu.smarthome.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author Mu
 * @date 2015-10-19 下午2:57:31
 * @Description 交互实体
 */
public class TransferEntity implements Serializable {
	public List<RoomEntity> rooms;
	public List<DeviceEntity> devices;
	public GatewayEntity gateway;
}
