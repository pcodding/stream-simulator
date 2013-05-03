package com.hortonworks.streaming.impl.domain.transport;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.hortonworks.streaming.impl.domain.gps.Location;
import com.hortonworks.streaming.impl.domain.gps.TimestampedLocation;

public class TruckConfiguration {
	private static int lastTruckId = 10;
	private static int nextDriverId = 0;
	private static List<Driver> drivers;
	private static Logger logger = Logger.getLogger(TruckConfiguration.class);
	private static List<TimestampedLocation> startingPoints = null;
	private static Iterator<TimestampedLocation> startingPointIterator = null;

	static {
		drivers = new ArrayList<Driver>();
		Driver riskyDriver = new Driver(1, 30);
		Driver lessRiskyDriver = new Driver(2, 60);
		drivers.add(riskyDriver);
		drivers.add(lessRiskyDriver);
		startingPoints = new LinkedList<TimestampedLocation>();
		startingPoints.add(new TimestampedLocation(new GregorianCalendar(),
				new Location(4.87, 44.93, 150.0f)));
		startingPoints.add(new TimestampedLocation(new GregorianCalendar(),
				new Location(5.73, 45.18, 220.0f)));
		startingPointIterator = startingPoints.iterator();
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

	public synchronized static TimestampedLocation getNextStartingPoint() {
		if (startingPointIterator.hasNext())
			return startingPointIterator.next();
		else
			startingPointIterator = startingPoints.iterator();
		return getNextStartingPoint();
	}
}
