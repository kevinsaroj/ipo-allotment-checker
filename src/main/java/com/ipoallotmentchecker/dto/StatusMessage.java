package com.ipoallotmentchecker.dto;

import org.springframework.http.HttpStatusCode;

import lombok.Data;

@Data
public class StatusMessage {

	
	private String message;
	private HttpStatusCode code;
	
}
