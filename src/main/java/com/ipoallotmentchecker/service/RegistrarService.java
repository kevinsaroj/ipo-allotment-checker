package com.ipoallotmentchecker.service;

import org.springframework.stereotype.Service;

@Service
public class RegistrarService {
	public String fetchKfintechStatus(String pan, String ipoName) {
		// Use RestTemplate to call Kfintech API
		return "X Shares Allotted"; // Mocked response
	}

	public String fetchLinkintimeStatus(String pan, String ipoName) {
		// Use RestTemplate to call Linkintime API
		return "Not Allotted"; // Mocked response
	}
}
