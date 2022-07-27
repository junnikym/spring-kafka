package edu.junnikym.springkafka.sample.controller;

import edu.junnikym.springkafka.sample.service.KafkaSampleProducerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/sample")
public class KafkaSampleProducerController {

	private final KafkaSampleProducerService kafkaSampleProducerService;

	public KafkaSampleProducerController(KafkaSampleProducerService kafkaSampleProducerService) {
		this.kafkaSampleProducerService = kafkaSampleProducerService;
	}

	@PostMapping
	public void sendMessage(String message) {
		kafkaSampleProducerService.sendMessage(message);
	}

}