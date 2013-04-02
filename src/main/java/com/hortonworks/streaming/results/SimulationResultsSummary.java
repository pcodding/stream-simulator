package com.hortonworks.streaming.results;

public class SimulationResultsSummary {
	private int numberOfMessages = 0;

	public SimulationResultsSummary(int numberOfMessages) {
		this.numberOfMessages = numberOfMessages;
	}

	public String toString() {
		return "System generated " + numberOfMessages + " events";
	}
}
