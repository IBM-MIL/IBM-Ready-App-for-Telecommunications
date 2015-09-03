package com.ibm.mil.readyapps.telco.models;

public class WifiHotspotProperties {
	private String name;
	private double speed;
	private boolean isVerified;
	private double downloadSpeed;
	private boolean signInRequired;
	private int connections;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public boolean isVerified() {
		return isVerified;
	}
	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}
	public double getDownloadSpeed() {
		return downloadSpeed;
	}
	public void setDownloadSpeed(double downloadSpeed) {
		this.downloadSpeed = downloadSpeed;
	}
	public boolean isSignInRequired() {
		return signInRequired;
	}
	public void setSignInRequired(boolean signInRequired) {
		this.signInRequired = signInRequired;
	}
	public int getConnections() {
		return connections;
	}
	public void setConnections(int connections) {
		this.connections = connections;
	}
	
	
	
}
