package com.hortonworks.streaming.impl.collectors;

import com.hortonworks.streaming.impl.domain.AbstractEventCollector;

public class DefaultEventCollector extends AbstractEventCollector {
	public DefaultEventCollector(int maxNumberOfEvents) {
		this.maxNumberOfEvents = maxNumberOfEvents;
	}

	@Override
	public void onReceive(Object message) throws Exception {
		logger.info(message);
		numberOfEventsProcessed++;
		if (numberOfEventsProcessed != -1
				&& numberOfEventsProcessed == maxNumberOfEvents) {
			logger.info("Maximum number of messages processed, exiting");
			this.getContext().system().shutdown();
			System.exit(0);
		}
	}
}
