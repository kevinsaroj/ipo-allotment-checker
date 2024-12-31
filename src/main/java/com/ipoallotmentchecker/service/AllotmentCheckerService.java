package com.ipoallotmentchecker.service;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipoallotmentchecker.dto.AllotmentResponse;

@Service
public class AllotmentCheckerService {

	public AllotmentResponse checkAllotmentMaashitla(String panNumber, String company)
			throws JsonMappingException, JsonProcessingException {
		RestTemplate restTemplate = new RestTemplate();

		// URL for the Maashitla allotment API
		String url = "https://maashitla.com/PublicIssues/Search?company=" + company + "&search=" + panNumber;

		// Make GET request to the URL
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

		// Parse the response body (assuming it's a JSON response)
		String responseBody = response.getBody();

		// Assuming responseBody is a JSON, you need to parse it (using a JSON library
		// like Jackson)
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode rootNode = objectMapper.readTree(responseBody);

		// Extracting values from the JSON response
		String pan = rootNode.path("pan").asText(); // PAN number
		String dematAccountNumber = rootNode.path("demat_Account_Number").asText(); // Demat Account Number
		String applicationNumber = rootNode.path("application_Number").asText(); // Application Number
		String name = rootNode.path("name").asText(); // Name of the applicant
		int shareApplied = rootNode.path("share_Applied").asInt(); // Shares applied
		int shareAllotted = rootNode.path("share_Alloted").asInt(); // Shares allotted

		// Create AllotmentResponse based on the parsed data
		AllotmentResponse allotmentResponse = new AllotmentResponse();
		allotmentResponse.setPan(pan); // Set PAN
		allotmentResponse.setDematAccountNumber(dematAccountNumber); // Set Demat Account Number
		allotmentResponse.setApplicationNumber(applicationNumber); // Set Application Number
		allotmentResponse.setName(name); // Set Name
		allotmentResponse.setShareApplied(shareApplied); // Set Shares Applied
		allotmentResponse.setShareAlloted(shareAllotted); // Set Shares Allotted

		// Set status based on the values
		if (shareAllotted > 0) {
			allotmentResponse.setStatus("Allotted " + shareAllotted + " Shares");
		} else if (shareApplied > 0) {
			allotmentResponse.setStatus("Not Allotted");
		} else {
			allotmentResponse.setStatus("Not Applied");
		}

		return allotmentResponse;
	}

}
