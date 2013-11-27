package com.hortonworks.streaming.impl.collectors;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import com.hortonworks.streaming.impl.domain.AbstractEventCollector;
import com.hortonworks.streaming.impl.messages.DumpStats;
import com.hortonworks.streaming.results.utils.ConfigurationUtil;

public class JmsEventCollector extends AbstractEventCollector {
	private ActiveMQConnectionFactory connectionFactory;
	private String user = ActiveMQConnection.DEFAULT_USER;
	private String password = ActiveMQConnection.DEFAULT_PASSWORD;
	private Connection connection = null;
	private Session session = null;
	private Destination destination = null;
	private MessageProducer producer = null;

	public JmsEventCollector() {
		super();
		try {
			String host = ConfigurationUtil.getInstance().getProperty(
					"jms.host");
			String port = ConfigurationUtil.getInstance().getProperty(
					"jms.port");
			logger.debug("Setting up JMS Event Collector with host: " + host
					+ " and port: " + port);
			connectionFactory = new ActiveMQConnectionFactory(user, password,
					"tcp://" + host + ":" + port);
			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue(ConfigurationUtil.getInstance()
					.getProperty("jms.queue"));
			producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public JmsEventCollector(int maxEvents) {
		super(maxEvents);
	}

	@Override
	public void onReceive(Object message) throws Exception {
		logger.debug(message);
		if (message instanceof DumpStats) {
			logger.info("Processed " + numberOfEventsProcessed + " events");
		}
		try {
			TextMessage textMessage = session.createTextMessage(message
					.toString());
			producer.send(textMessage);
			logger.debug(message.toString());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		numberOfEventsProcessed++;
		if (numberOfEventsProcessed != -1
				&& numberOfEventsProcessed == maxNumberOfEvents) {
			logger.info("Maximum number of messages processed, exiting");
			this.getContext().system().shutdown();
			System.exit(0);
		}
	}
}
