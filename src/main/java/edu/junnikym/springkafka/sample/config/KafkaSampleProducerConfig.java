package edu.junnikym.springkafka.sample.config;

import edu.junnikym.springkafka.sample.common.serialization.KafkaByteSerializer;
import edu.junnikym.springkafka.sample.dto.SampleMessageDto;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaSampleProducerConfig {

	@Value("${spring.kafka.producer.bootstrap-servers}")
	private String bootstrapAddress;

	@Bean
	public ProducerFactory<String, SampleMessageDto> producerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(
				ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
				bootstrapAddress);
		configProps.put(
				ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				StringSerializer.class);
		configProps.put(
				ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				"edu.junnikym.springkafka.sample.common.serialization.KafkaByteSerializer");

		return new DefaultKafkaProducerFactory<>(configProps);
	}

	@Bean
	public KafkaTemplate<String, SampleMessageDto> kafkaTemplate(ProducerFactory<String, SampleMessageDto>  producerFactory) {
		return new KafkaTemplate<String, SampleMessageDto> (producerFactory);
	}

}
