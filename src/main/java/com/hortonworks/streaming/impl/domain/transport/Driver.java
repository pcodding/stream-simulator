package com.hortonworks.streaming.impl.domain.transport;

import com.hortonworks.streaming.interfaces.DomainObject;

public class Driver implements DomainObject {
	private static final long serialVersionUID = 6113264533619087412L;
	private int driverId;
	private int riskFactor;

	public Driver() {
	}

	public Driver(int driverId, int riskFactor) {
		this.driverId = driverId;
		this.riskFactor = riskFactor;
	}

	public int getDriverId() {
		return driverId;
	}

	public void setDriverId(int driverId) {
		this.driverId = driverId;
	}

	public int getRiskFactor() {
		return riskFactor;
	}

	public void setRiskFactor(int riskFactor) {
		this.riskFactor = riskFactor;
	}

	@Override
	public String toString() {
		return this.driverId + "|" + this.riskFactor;
	}
}
