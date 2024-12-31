package com.ipoallotmentchecker.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipoallotmentchecker.dto.AllotmentResponse;
import com.ipoallotmentchecker.dto.FamilyMemberDTO;
import com.ipoallotmentchecker.dto.LinkinTimeResponseDTO;

@Service
public class LinkInTimeService {

	private static final String BASE_URL = "https://linkintime.co.in/initial_offer/IPO.aspx";
	private final RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	private FamilyMemberService familyMemberService;

	// Step 1: Fetch the __VIEWSTATE and __VIEWSTATEGENERATOR values
	public Map<String, String> fetchViewState() {
		try {
			// Fetch the page that contains the __VIEWSTATE and __VIEWSTATEGENERATOR values
			ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL, String.class);

			// Parse the HTML to extract __VIEWSTATE and __VIEWSTATEGENERATOR
			Document doc = Jsoup.parse(response.getBody());
			String viewState = doc.select("input[id=__VIEWSTATE]").val();
			String viewStateGenerator = doc.select("input[id=__VIEWSTATEGENERATOR]").val();

			// Return the extracted data as a Map
			Map<String, String> stateMap = new HashMap<>();
			stateMap.put("__VIEWSTATE", viewState);
			stateMap.put("__VIEWSTATEGENERATOR", viewStateGenerator);

			return stateMap;
		} catch (Exception e) {
			throw new RuntimeException("Failed to fetch VIEWSTATE: " + e.getMessage(), e);
		}
	}

	public String generateToken2() {
		try {
			// Set headers for the request
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON); // Required content type
			headers.set("X-Requested-With", "XMLHttpRequest"); // Add if necessary

			// Empty payload since none is required
			HttpEntity<String> requestEntity = new HttpEntity<>("", headers);

			// Log the request for debugging
			System.out.println("Sending request to generate token...");

			// Send the POST request
			ResponseEntity<String> response = restTemplate.postForEntity(
					"https://linkintime.co.in/initial_offer/IPO.aspx/generateToken", requestEntity, String.class);

			// Log the response body
			System.out.println("Response Body: " + response.getBody());

			// Parse the JSON response to extract the token
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(response.getBody());
			String token = rootNode.path("d").asText(); // Extract the value of "d"

			System.out.println("Generated Token: " + token);
			return token; // Return the extracted token
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to generate token: " + e.getMessage(), e);
		}
	}


//	public ResponseEntity<ResponseEntity<String>> checkAllotmentStatus(String ipoName,Long UserId, String pan) {
//        try {
//            // Step 1: Generate the token
//            String generatedToken = generateToken2(); // Method to fetch token
//
//            // Step 2: Prepare headers for SearchOnPan API
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.set("X-Requested-With", "XMLHttpRequest");
//
//            // Step 3: Create the payload
//            Map<String, String> payload = new HashMap<>();
//            payload.put("clientid", "11799");
//            payload.put("PAN", pan);
//
//            payload.put("IFSC", "");
//            payload.put("CHKVAL", "1");
//            payload.put("token", generatedToken);
//
//            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(payload, headers);
//
//            // Step 4: Call the SearchOnPan API
//            String url = "https://linkintime.co.in/initial_offer/IPO.aspx/SearchOnPan";
//            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
//
//            // Step 5: Parse the response
//            String responseBody = response.getBody();
//            if (responseBody != null && !responseBody.isEmpty()) {
//                // Decode the escaped XML string from the response
//                String escapedXml = new JSONObject(responseBody).getString("d");
//
//                // Decode the escaped XML (i.e., convert Unicode escape sequences to their actual characters)
//                String decodedXml = decodeUnicode(escapedXml);
//
//                // Parse the decoded XML using JSoup
//                Document xmlDocument = Jsoup.parse(decodedXml, "", org.jsoup.parser.Parser.xmlParser());
//
//                // Extract specific fields from the XML
//                Element nameElement = xmlDocument.select("NAME1").first();
//                Element companyNameElement = xmlDocument.select("companyname").first();
//                Element allotmentElement = xmlDocument.select("ALLOT").first();
//                Element sharesElement = xmlDocument.select("SHARES").first();
//
//                String name = nameElement != null ? nameElement.text() : "";
//                String companyName = companyNameElement != null ? companyNameElement.text() : "";
//                String allotment = allotmentElement != null ? allotmentElement.text() : "";
//                String sharesApplied = sharesElement != null ? sharesElement.text() : "";
//
//                // Create AllotmentResponse based on the parsed data
//                AllotmentResponse allotmentResponse = new AllotmentResponse();
////                allotmentResponse.setPan(token);  // Set PAN
//                allotmentResponse.setDematAccountNumber("");  // Set Demat Account Number (Adjust as needed)
//                allotmentResponse.setApplicationNumber("");  // Set Application Number (Adjust as needed)
//                allotmentResponse.setName(name);  // Set Name
//                allotmentResponse.setShareApplied(Integer.parseInt(sharesApplied));  // Set Shares Applied
//                allotmentResponse.setShareAlloted(Integer.parseInt(allotment));  // Set Shares Allotted
//
//                // Set status based on the values
//                setStatus(allotmentResponse, allotment, sharesApplied);
//                
//                
//                    checkfamilyStatus(UserId);
//
//
//                // Return the response in ResponseEntity
//                return ResponseEntity.ok(ResponseEntity.ok("Success"));
//
//            } else {
//                throw new RuntimeException("Empty or null response received from SearchOnPan API");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("Failed to search on PAN: " + e.getMessage(), e);
//        }
//    }



	
	public Map<String, Object> checkAllotmentStatus(String ipoName, Long userId, String pan) {
	    try {
	        // Step 1: Generate the token
	        String generatedToken = generateToken2(); // Method to fetch token

	        // Step 2: Prepare headers for SearchOnPan API
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.set("X-Requested-With", "XMLHttpRequest");

	        // Step 3: Create the payload
	        Map<String, String> payload = new HashMap<>();
	        payload.put("clientid", "11799");
	        payload.put("PAN", pan);
	        payload.put("IFSC", "");
	        payload.put("CHKVAL", "1");
	        payload.put("token", generatedToken);

	        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(payload, headers);

	        // Step 4: Call the SearchOnPan API
	        String url = "https://linkintime.co.in/initial_offer/IPO.aspx/SearchOnPan";
	        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

	        // Step 5: Parse the response
	        String responseBody = response.getBody();
	        if (responseBody != null && !responseBody.isEmpty()) {
	            // Decode the escaped XML string from the response
	            String escapedXml = new JSONObject(responseBody).getString("d");

	            // Decode the escaped XML (i.e., convert Unicode escape sequences to their actual characters)
	            String decodedXml = decodeUnicode(escapedXml);

	            // Parse the decoded XML using JSoup
	            Document xmlDocument = Jsoup.parse(decodedXml, "", org.jsoup.parser.Parser.xmlParser());

	            // Extract specific fields from the XML
	            Element nameElement = xmlDocument.select("NAME1").first();
	            Element companyNameElement = xmlDocument.select("companyname").first();
	            Element allotmentElement = xmlDocument.select("ALLOT").first();
	            Element sharesElement = xmlDocument.select("SHARES").first();

	            String name = nameElement != null ? nameElement.text() : "";
	            String companyName = companyNameElement != null ? companyNameElement.text() : "";
	            String allotment = allotmentElement != null ? allotmentElement.text() : "";
	            String sharesApplied = sharesElement != null ? sharesElement.text() : "";

	            // Create AllotmentResponse based on the parsed data for the user
	            LinkinTimeResponseDTO allotmentResponse = new LinkinTimeResponseDTO();
	            allotmentResponse.setName(name);  // Set Name
	            allotmentResponse.setShareApplied(Integer.parseInt(sharesApplied));  // Set Shares Applied
	            allotmentResponse.setShareAlloted(Integer.parseInt(allotment));  // Set Shares Allotted

	            // Set status based on the values for the user
	            setStatus(allotmentResponse, allotment, sharesApplied);

	            // Step 6: Check family members' allotment status
	            List<FamilyMemberDTO> familyMembers = familyMemberService.getFamilyMembersByUserId(userId);
	            List<LinkinTimeResponseDTO> linkedAccounts = new ArrayList<>();
	            for (FamilyMemberDTO member : familyMembers) {
	                try {
	                    // Prepare the payload for family member
	                    payload.put("PAN", member.getPanNumber());  // Set the family member's PAN
	                    requestEntity = new HttpEntity<>(payload, headers);

	                    // Call the SearchOnPan API for family member
	                    response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
	                    responseBody = response.getBody();

	                    // Process the response for family member's allotment status
	                    if (responseBody != null && !responseBody.isEmpty()) {
	                        escapedXml = new JSONObject(responseBody).getString("d");
	                        decodedXml = decodeUnicode(escapedXml);
	                        xmlDocument = Jsoup.parse(decodedXml, "", org.jsoup.parser.Parser.xmlParser());

	                        allotmentElement = xmlDocument.select("ALLOT").first();
	                        sharesElement = xmlDocument.select("SHARES").first();
	                        allotment = allotmentElement != null ? allotmentElement.text() : "";
	                        sharesApplied = sharesElement != null ? sharesElement.text() : "";

	                        LinkinTimeResponseDTO familyStatus = new LinkinTimeResponseDTO();
	                        familyStatus.setName(member.getName());  // Set Family Member Name
	                        if (!sharesApplied.isEmpty()) {
	                            familyStatus.setShareApplied(Integer.parseInt(sharesApplied));  // Set Shares Applied
	                        }

	                        if (!allotment.isEmpty()) {
	                            familyStatus.setShareAlloted(Integer.parseInt(allotment));  // Set Shares Allotted
	                        }
	                        // Set status based on the values for family member
	                        setStatus(familyStatus, allotment, sharesApplied);

	                        linkedAccounts.add(familyStatus);  // Add to linked accounts list
	                    }
	                } catch (Exception e) {
	                    e.printStackTrace();
	                    // You can choose to handle errors for individual family members here
	                }
	            }

	            // Create the final response containing both user and family allotment statuses
	            Map<String, Object> responseMap = new HashMap<>();
	            responseMap.put("userStatus", allotmentResponse);
	            responseMap.put("linkedAccounts", linkedAccounts);

	            return responseMap;

	        } else {
	            throw new RuntimeException("Empty or null response received from SearchOnPan API");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Failed to search on PAN: " + e.getMessage(), e);
	    }
	}

	// Method to set status for the allotment (for both user and family members)
	private void setStatus(LinkinTimeResponseDTO allotmentResponse, String allotment, String sharesApplied) {
	    try {
	        int allottedShares = allotment != null && !allotment.isEmpty() ? Integer.parseInt(allotment) : 0;
	        int appliedShares = sharesApplied != null && !sharesApplied.isEmpty() ? Integer.parseInt(sharesApplied) : 0;

	        if (allottedShares > 0) {
	            allotmentResponse.setStatus("Allotted " + allottedShares + " Shares");
	        } else if (appliedShares > 0) {
	            allotmentResponse.setStatus("Not Allotted");
	        } else {
	            allotmentResponse.setStatus("Not Applied");
	        }
	    } catch (NumberFormatException e) {
	        allotmentResponse.setStatus("Invalid Data");
	        e.printStackTrace(); // Log the exception for debugging
	    }
	}



	private String decodeUnicode(String escapedXml) throws UnsupportedEncodingException {
		// Decode the escaped XML string
		return URLDecoder.decode(escapedXml, "UTF-8");
	}
	
}
