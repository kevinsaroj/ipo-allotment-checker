package com.ipoallotmentchecker.dto;

import lombok.Data;

@Data
public class AllotmentResponse {
	private String pan;
	private String dematAccountNumber;
	private String applicationNumber;
	private String name;
	private int shareApplied;
	private int shareAlloted;
	private String status;

	// Getters and setters
}
