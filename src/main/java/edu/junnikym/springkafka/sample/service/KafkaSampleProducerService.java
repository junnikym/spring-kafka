package edu.junnikym.springkafka.sample.service;

import edu.junnikym.springkafka.sample.dto.SampleMessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaSampleProducerService {

//	private KafkaTemplate<String, String> kafkaStringTemplate;

	private KafkaTemplate<String, SampleMessageDto> kafkaDtoTemplate;

	public KafkaSampleProducerService(
//			KafkaTemplate<String, String> kafkaStringTemplate,
			KafkaTemplate<String, SampleMessageDto> kafkaDtoTemplate
	) {
//		this.kafkaStringTemplate = kafkaTemplate;
		this.kafkaDtoTemplate = kafkaDtoTemplate;
	}

//	public void sendMessage(String message) {
//		System.out.println("[send] message >> " + message);
//		kafkaStringTemplate.send("learning-topic-1", message);
//	}

	public void sendMessage(SampleMessageDto message) {
		System.out.println("[send] message as dto >> " + message);
		kafkaDtoTemplate.send("learning-topic-1", message);
	}

}
