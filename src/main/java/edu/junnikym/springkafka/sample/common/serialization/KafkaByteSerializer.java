package edu.junnikym.springkafka.sample.common.serialization;

import edu.junnikym.springkafka.sample.dto.SampleMessageDto;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

public class KafkaByteSerializer implements Serializer<SampleMessageDto> {

	@Override
	public void configure (Map configs, boolean isKey) {
		Serializer.super.configure(configs, isKey);
	}

	@Override
	public byte[] serialize (String topic, SampleMessageDto data) {
		try(
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
		) {
			objectStream.writeObject(data);
			objectStream.flush();
			objectStream.close();
			return byteStream.toByteArray();

		} catch (Exception e) {
			throw new SerializationException("Error when serializing to byte[] for kafka message");
		}
	}

	@Override
	public byte[] serialize (String topic, Headers headers, SampleMessageDto data) {
		return Serializer.super.serialize(topic, headers, data);
	}

	@Override
	public void close () {
		Serializer.super.close();
	}

}
