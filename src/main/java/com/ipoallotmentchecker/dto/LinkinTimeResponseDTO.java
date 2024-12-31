package com.ipoallotmentchecker.dto;

import lombok.Data;

@Data
public class LinkinTimeResponseDTO {

	private String name;
	private int shareApplied;
	private int shareAlloted;
	private String status;
}
