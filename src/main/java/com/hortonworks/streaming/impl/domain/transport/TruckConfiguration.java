package com.hortonworks.streaming.impl.domain.transport;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class TruckConfiguration {
	private static int lastTruckId = 10;
	private static int nextDriverId = 0;
	private static List<Driver> drivers;
	private static int numberOfEventEmitters = 0;
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
		if (drivers.size() < numberOfEventEmitters) {
			for (int i = drivers.size() + 1; i <= numberOfEventEmitters; i++) {
				drivers.add(new Driver(i, 100));
			}
		}
		Driver nextDriver = drivers.get(nextDriverId);
		logger.debug("Next Driver: " + nextDriver.toString());
		nextDriverId++;
		return nextDriver;
	}

	public static void setNumberOfEventEmitters(int numberOfEmitters) {
		numberOfEventEmitters = numberOfEmitters;
	}
}
