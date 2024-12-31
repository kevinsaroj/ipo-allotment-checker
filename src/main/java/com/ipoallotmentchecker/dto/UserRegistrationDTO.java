package com.ipoallotmentchecker.dto;

import lombok.Data;

@Data
public class UserRegistrationDTO {
	private String email;

	private String password; // Encrypted

	private String name;

	private String pan;


}
