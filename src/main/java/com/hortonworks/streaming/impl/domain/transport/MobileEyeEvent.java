package com.hortonworks.streaming.impl.domain.transport;

import com.hortonworks.streaming.impl.domain.Event;

public class MobileEyeEvent extends Event {	
	private MobileEyeEventTypeEnum eventType;
	private Truck truck;

	public MobileEyeEvent() {
	}

	public MobileEyeEvent(MobileEyeEventTypeEnum eventType, Truck truck) {
		this.eventType = eventType;
		this.truck = truck;
	}

	public MobileEyeEventTypeEnum getEventType() {
		return eventType;
	}

	public void setEventType(MobileEyeEventTypeEnum eventType) {
		this.eventType = eventType;
	}
	
	@Override
	public String toString() {
		return truck.toString() + eventType.toString();
	}
}
