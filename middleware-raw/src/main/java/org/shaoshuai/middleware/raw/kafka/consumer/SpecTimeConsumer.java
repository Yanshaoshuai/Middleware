package org.shaoshuai.middleware.raw.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;

/**
 * @Author yan
 * @Date 2026/1/5
 */
public class SpecTimeConsumer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.21:9092,192.168.1.22:9092,192.168.1.23:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "testgroup");
        try (KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties)) {
            kafkaConsumer.subscribe(List.of("testtopic"));
            //等待assignment分配完成
            Set<TopicPartition> assignment = kafkaConsumer.assignment();
            while (assignment.isEmpty()) {
                kafkaConsumer.poll(Duration.ofSeconds(1));
                assignment = kafkaConsumer.assignment();
            }
            //topic-partition - 毫秒时间戳
            HashMap<TopicPartition, Long> topicPartitionTimeHashMap = new HashMap<>();
            for (TopicPartition topicPartition : assignment) {
                //设置每个topic-partition的时间戳为两天前
                topicPartitionTimeHashMap.put(topicPartition, System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000);
            }
            //把 "topic-partition - 毫秒时间戳"
            // 转换成
            // "topic-partition - OffsetAndTimestamp"
            Map<TopicPartition, OffsetAndTimestamp> topicPartitionOffsetAndTimestampMap = kafkaConsumer.offsetsForTimes(topicPartitionTimeHashMap);
            //手动指定每个分区的offset
            for (TopicPartition topicPartition : assignment) {
                OffsetAndTimestamp offsetAndTimestamp = topicPartitionOffsetAndTimestampMap.get(topicPartition);
                kafkaConsumer.seek(topicPartition, offsetAndTimestamp.offset());
            }
            //开始消费
            while (true) {
                ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
                consumerRecords.forEach(System.out::println);
            }
        }

    }
}
