package com.mu.smarthome.model;

import java.io.Serializable;

/**
 * @author Mu
 * @date 2015-10-19 下午2:54:36
 * @Description 网关配置实体类
 */
public class GatewayEntity implements Serializable {
	public String ipAddress;
	public String identier;
	public String type;
	public GatewayEntity(String ipAddress, String identier, String type) {
		super();
		this.ipAddress = ipAddress;
		this.identier = identier;
		this.type = type;
	}
	
	
	public GatewayEntity() {
		super();
		// TODO Auto-generated constructor stub
	}


	@Override
	public String toString() {
		return "GatewayEntity [ipAddress=" + ipAddress + ", identier="
				+ identier + ", type=" + type + "]";
	}
	
	
	
	
	
	
}
