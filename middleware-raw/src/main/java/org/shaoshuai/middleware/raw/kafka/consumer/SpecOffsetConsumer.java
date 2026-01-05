package org.shaoshuai.middleware.raw.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @Author yan
 * @Date 2026/1/5
 */
public class SpecOffsetConsumer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.21:9092,192.168.1.22:9092,192.168.1.23:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "testgroup");
        //只对新的消费者组起作用 若是已经提交过offset的消费者组都是从offset后消费
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, OffsetResetStrategy.EARLIEST.toString());
        try (KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties)) {
            kafkaConsumer.subscribe(List.of("testtopic"));

            Set<TopicPartition> assignment = kafkaConsumer.assignment();
            //等待分区方案已经分配
            while (assignment.isEmpty()) {
                kafkaConsumer.poll(Duration.ofSeconds(1));
                assignment = kafkaConsumer.assignment();
            }

            //获取每个分区的起始offset
            Map<TopicPartition, Long> topicPartitionLongMap = kafkaConsumer.beginningOffsets(assignment);
            for (TopicPartition topicPartition : assignment) {
                //跳到每个分区的起始offset 从起始offset开始消费
                kafkaConsumer.seek(topicPartition, topicPartitionLongMap.get(topicPartition));
            }
            while (true) {
                ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
                consumerRecords.forEach(System.out::println);
            }
        }
    }
}
