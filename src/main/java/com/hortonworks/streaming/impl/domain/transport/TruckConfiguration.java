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
		Driver riskyDriver = new Driver(1, 30, new TimestampedLocation(new GregorianCalendar(),
				new Location(-93.2828, 44.8833, 150.0f)));
		Driver lessRiskyDriver = new Driver(2, 60);
		drivers.add(riskyDriver);
		drivers.add(lessRiskyDriver);
		startingPoints = new LinkedList<TimestampedLocation>();
		startingPoints.add(new TimestampedLocation(new GregorianCalendar(),
				new Location(-93.266670, 44.983334, 150.0f)));
		startingPoints.add(new TimestampedLocation(new GregorianCalendar(),
				new Location(-82.907123, 40.417287, 220.0f)));
		startingPoints.add(new TimestampedLocation(new GregorianCalendar(),
				new Location(-96.800451, 32.780140, 20.0f)));
		startingPoints.add(new TimestampedLocation(new GregorianCalendar(),
				new Location(-112.074037, 33.448377, 20.0f)));
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
