package com.ipoallotmentchecker.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FamilyMemberDTO {
 
	private Long id;
    private String name;
    private String panNumber;
 
  

	public FamilyMemberDTO(Long id, String name, String panNumber) {
		super();
		this.id = id;
		this.name = name;
		this.panNumber = panNumber;
	}

 
}
