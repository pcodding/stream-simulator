package com.hortonworks;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;

import com.hortonworks.streaming.impl.collectors.DefaultEventCollector;
import com.hortonworks.streaming.impl.domain.AbstractEventCollector;
import com.hortonworks.streaming.impl.domain.transport.TruckConfiguration;
import com.hortonworks.streaming.impl.messages.StartSimulation;
import com.hortonworks.streaming.impl.messages.StopSimulation;
import com.hortonworks.streaming.listeners.SimulatorListener;
import com.hortonworks.streaming.masters.SimulationMaster;

public class App {
	public static void main(String[] args) {
		if (args != null && args.length == 4) {
			try {
				final int numberOfEventEmitters = Integer.parseInt(args[0]);
				final int numberOfEvents = Integer.parseInt(args[1]);
				final Class eventEmitterClass = Class.forName(args[2]);
				final Class eventCollectorClass = Class.forName(args[3]);
				TruckConfiguration
						.setNumberOfEventEmitters(numberOfEventEmitters);
				ActorSystem system = ActorSystem.create("EventSimulator");
				final ActorRef listener = system.actorOf(new Props(
						SimulatorListener.class), "listener");
				final ActorRef eventCollector = system.actorOf(new Props(
						new UntypedActorFactory() {
							public UntypedActor create() {
								try {
									Constructor cons = eventCollectorClass
											.getConstructor(new Class[] { int.class });
									return (AbstractEventCollector) cons
											.newInstance(numberOfEvents);
								} catch (SecurityException e) {
									System.err
											.println("Could not instantiate event collector: "
													+ e.getMessage());
									e.printStackTrace();
								} catch (NoSuchMethodException e) {
									System.err
											.println("Could not instantiate event collector: "
													+ e.getMessage());
									e.printStackTrace();
								} catch (IllegalArgumentException e) {
									System.err
											.println("Could not instantiate event collector: "
													+ e.getMessage());
									e.printStackTrace();
								} catch (InstantiationException e) {
									System.err
											.println("Could not instantiate event collector: "
													+ e.getMessage());
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									System.err
											.println("Could not instantiate event collector: "
													+ e.getMessage());
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									System.err
											.println("Could not instantiate event collector: "
													+ e.getMessage());
									e.printStackTrace();
								}
								return new DefaultEventCollector(numberOfEvents);
							}
						}), "eventCollector");
				System.out.println(eventCollector.path());
				final ActorRef master = system.actorOf(new Props(
						new UntypedActorFactory() {
							public UntypedActor create() {
								return new SimulationMaster(
										numberOfEventEmitters,
										eventEmitterClass, listener);
							}
						}), "master");
				Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
					public void run() {
						master.tell(new StopSimulation());
					}
				}));
				master.tell(new StartSimulation(), master);
			} catch (NumberFormatException e) {
				System.err.println("Invalid number of emitters: "
						+ e.getMessage());
			} catch (ClassNotFoundException e) {
				System.err.println("Cannot find classname: " + e.getMessage());
			}
		} else {
			System.err
					.println("Please specify the number of event emitters and the class that you would like to use.");
		}
	}
}
