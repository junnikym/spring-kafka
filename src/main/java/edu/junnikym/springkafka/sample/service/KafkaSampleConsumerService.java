package edu.junnikym.springkafka.sample.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class KafkaSampleConsumerService {

	@KafkaListener(topics = "learning-topic-1", groupId = "learning-kafka-group-1")
	public void consumer(String message) throws IOException {
		System.out.println("[recv] message << " + message);
	}

}
