package com.ipoallotmentchecker.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserDTO {
	
	private Long id;
	private String email;
	private String name;
	private List<FamilyMemberDTO> familyMembers;

	public UserDTO(Long id, String email, String name, List<FamilyMemberDTO> familyMembers) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.familyMembers = familyMembers;
	}

	// Getters and Setters
}
