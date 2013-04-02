package com.hortonworks.streaming.impl.domain;

import org.apache.log4j.Logger;

import akka.actor.UntypedActor;

public abstract class AbstractEventCollector extends UntypedActor {
	protected int maxNumberOfEvents = -1;
	protected Logger logger = Logger.getLogger(this.getClass());
	protected int numberOfEventsProcessed = 0;

	public AbstractEventCollector() {
	}

	public AbstractEventCollector(int maxNumberOfEvents) {
		this();
		this.maxNumberOfEvents = maxNumberOfEvents;
	}
}
