package com.ipoallotmentchecker.dto;

import lombok.Data;

@Data
public class AllotmentStatusDTO {
    private String name;
    private String panNumber;
    private String status; // Allotted, Not Allotted, Not Applied
    private String ipoName;
    private int sharesAllotted; // 0 if not allotted or not applied
	
    
    public AllotmentStatusDTO(String name, String panNumber, String status, String ipoName, int sharesAllotted) {
		this.name = name;
		this.panNumber = panNumber;
		this.status = status;
		this.ipoName = ipoName;
		this.sharesAllotted = sharesAllotted;
	}
    
    
    
}
