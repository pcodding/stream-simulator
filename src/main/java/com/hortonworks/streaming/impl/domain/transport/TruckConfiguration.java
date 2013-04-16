package com.hortonworks.streaming.impl.domain.transport;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class TruckConfiguration {
	private static int lastTruckId = 10;
	private static int nextDriverId = 0;
	private static List<Driver> drivers;
	private static Logger logger = Logger.getLogger(TruckConfiguration.class);

	static {
		drivers = new ArrayList<Driver>();
		Driver riskyDriver = new Driver(1, 30);
		Driver lessRiskyDriver = new Driver(2, 60);
		drivers.add(riskyDriver);
		drivers.add(lessRiskyDriver);
	}

	public synchronized static int getNextTruckId() {
		lastTruckId++;
		return lastTruckId;
	}

	public synchronized static Driver getNextDriver() {
		Driver nextDriver;
		try {
			nextDriver = drivers.get(nextDriverId);
		} catch (IndexOutOfBoundsException e) {
			drivers.add(new Driver(nextDriverId + 1, 100));
		}
		nextDriver = drivers.get(nextDriverId);
		logger.debug("Next Driver: " + nextDriver.toString());
		nextDriverId++;
		return nextDriver;
	}
}
