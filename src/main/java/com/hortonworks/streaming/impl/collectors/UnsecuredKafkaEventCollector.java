package com.hortonworks.streaming.impl.collectors;

import com.hortonworks.streaming.impl.domain.AbstractEventCollector;
import com.hortonworks.streaming.impl.messages.DumpStats;
import com.hortonworks.streaming.results.utils.ConfigurationUtil;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class UnsecuredKafkaEventCollector extends AbstractEventCollector {
    private String topicName;
    private String bootstrapServer;
    private Properties props;
    private org.apache.kafka.clients.producer.Producer<String, String> producer;

    public UnsecuredKafkaEventCollector() {
        super();
        try {
            bootstrapServer = ConfigurationUtil.getInstance().getProperty(
                    "kafka.bootstrap.servers");
            topicName = ConfigurationUtil.getInstance().getProperty(
                    "kafka.topicName");
            logger.info("Setting up bootstrap servers " +  bootstrapServer+ " and producing to topic "+topicName);

            props = new Properties();

            props.put("bootstrap.servers", bootstrapServer);


            props.put("key.serializer",
                    "org.apache.kafka.common.serialization.StringSerializer");

            props.put("value.serializer",
                    "org.apache.kafka.common.serialization.StringSerializer");

            producer = new KafkaProducer<String, String>(props);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public UnsecuredKafkaEventCollector(int maxEvents) {
        super(maxEvents);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        logger.debug(message);
        if (message instanceof DumpStats) {
            logger.info("Processed " + numberOfEventsProcessed + " events");
        }
        try {
            producer.send(new ProducerRecord<String, String>(topicName,message.toString()));
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
