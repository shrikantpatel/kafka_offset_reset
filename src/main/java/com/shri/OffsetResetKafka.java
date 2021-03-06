package com.shri;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("OffsetResetKafka")
public class OffsetResetKafka {

    public static Logger logger = LoggerFactory.getLogger(OffsetResetKafka.class);

    @Value("${topic}")
    private String topic;

    @Value("${bootstrap.server}")
    private String bootstrap_server;

    @Value("${security.protocol}")
    private String security_protocol;

    @Value("${ssl.truststore.location}")
    private String ssl_truststore_location;

    @Value("${ssl.truststore.password}")
    private String ssl_truststore_password;

    @Value("${ssl.keystore.location}")
    private String ssl_keystore_location;

    @Value("${ssl.keystore.password}")
    private String ssl_keystore_password;

    @Value("${ssl.key.password}")
    private String ssl_key_password;

    @Value("${ssl.enabled.protocols}")
    private String ssl_enabled_protocols;

    @Value("${ssl.truststore.type}")
    private String ssl_truststore_type;

    @Value("${ssl.keystore.type}")
    private String ssl_keystore_type;

    @Value("${consumer.group}")
    private String consumer_group;

    @Value("${partition.number}")
    private int partition_number;

    @Value("${offset.value}")
    private int offset_value;

    public void setOffset() {

        Map<String, Object> consumerProps = new HashMap<String, Object>();
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Boolean.FALSE);
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap_server);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, consumer_group);

        consumerProps.put(ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG, 1000);
        consumerProps.put(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, 1000);
        consumerProps.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 2000);
        consumerProps.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 6000);
        consumerProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 10);

        consumerProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, security_protocol);
        consumerProps.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, ssl_truststore_location);
        consumerProps.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, ssl_truststore_password);
        consumerProps.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, ssl_keystore_location);
        consumerProps.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, ssl_keystore_password);
        consumerProps.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, ssl_key_password);
        consumerProps.put(SslConfigs.SSL_ENABLED_PROTOCOLS_CONFIG, ssl_enabled_protocols);
        consumerProps.put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, ssl_truststore_type);
        consumerProps.put(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, ssl_keystore_type);

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps);

        List<PartitionInfo> partitionInfo = consumer.partitionsFor(topic);
        int partitionCount = partitionInfo.size();
        consumer.subscribe(Arrays.asList(topic));
        ConsumerRecords<String, String> records = consumer.poll(100);

        if (partition_number == -1) {
            for (int i = 0; i < partitionCount; i++) {
                TopicPartition topicPartition = new TopicPartition(topic, i);
                consumer.seek(topicPartition, offset_value);
                consumer.commitSync();
            }
        } else {
            TopicPartition topicPartition = new TopicPartition(topic, partition_number);
            consumer.seek(topicPartition, offset_value);
            consumer.commitSync();
        }

        logger.info("**************The offset change committed, please run the consumer group lap command to check the lag offsets applied **************");

    }

}
