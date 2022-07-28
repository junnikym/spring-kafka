package edu.junnikym.springkafka.sample.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class kafkaSampleConfig {

	@Bean
	public NewTopic sampleTopic () {
		return TopicBuilder
				.name("learning-kafka-group-on-spring")
				.partitions(10)
				.replicas(1)
				.build();
	}

}
