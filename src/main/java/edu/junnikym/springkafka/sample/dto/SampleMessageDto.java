package edu.junnikym.springkafka.sample.dto;

import java.io.Serializable;

public class SampleMessageDto implements Serializable {

	private String message;

	public SampleMessageDto() {}

	public SampleMessageDto(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
