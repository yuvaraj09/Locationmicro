package com.Location.model;
	
public class Location {

	private String userName;
	private Long userId;
	private String ipAddress;
	private String address;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public Location(String userName, Long userId, String ipAddress, String address) {
		super();
		this.userName = userName;
		this.userId = userId;
		this.ipAddress = ipAddress;
		this.address = address;
	}
	public Location() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "User [userName=" + userName + ", userId=" + userId + ", ipAddress=" + ipAddress + ", address=" + address
				+ "]";
	}
	


}
