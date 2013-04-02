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

public class JmsEventCollector extends AbstractEventCollector {
	private ActiveMQConnectionFactory connectionFactory;
	private String user = ActiveMQConnection.DEFAULT_USER;
	private String password = ActiveMQConnection.DEFAULT_PASSWORD;
	private Connection connection = null;
	private Session session = null;
	private Destination destination = null;
	private MessageProducer producer = null;

	public JmsEventCollector() {
	}

	public JmsEventCollector(int maxEvents) {
		super(maxEvents);
		logger.debug("Setting up JMS Event Collector");
		try {
			connectionFactory = new ActiveMQConnectionFactory(user, password,
					"tcp://sandbox:61616");
			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue("sensor_data");
			producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void onReceive(Object message) throws Exception {
		logger.info(message);
		try {
			TextMessage textMessage = session.createTextMessage(message
					.toString());
			producer.send(textMessage);
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
