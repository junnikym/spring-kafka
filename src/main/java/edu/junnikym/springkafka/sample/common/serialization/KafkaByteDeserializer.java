package edu.junnikym.springkafka.sample.common.serialization;

import edu.junnikym.springkafka.sample.dto.SampleMessageDto;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Map;

public class KafkaByteDeserializer implements Deserializer<SampleMessageDto> {

	@Override
	public void configure (Map<String, ?> configs, boolean isKey) {
		Deserializer.super.configure(configs, isKey);
	}

	@Override
	public SampleMessageDto deserialize (String topic, byte[] data) {
		try(
			ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
			ObjectInputStream objectStream = new ObjectInputStream(byteStream);
		) {
			final SampleMessageDto deserialized = (SampleMessageDto) objectStream.readObject();
			objectStream.close();
			return deserialized;

		} catch (Exception e) {
			throw new SerializationException("Error when serializing to byte[] for kafka message");
		}
	}

	@Override
	public SampleMessageDto deserialize (String topic, Headers headers, byte[] data) {
		return Deserializer.super.deserialize(topic, headers, data);
	}

	@Override
	public void close () {
		Deserializer.super.close();
	}
}
