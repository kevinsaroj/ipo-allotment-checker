package com.ipoallotmentchecker.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ipoallotmentchecker.config.JwtService;
import com.ipoallotmentchecker.dto.AllotmentResponse;
import com.ipoallotmentchecker.dto.FamilyMemberDTO;
import com.ipoallotmentchecker.model.User;
import com.ipoallotmentchecker.repository.UserRepository;
import com.ipoallotmentchecker.service.AllotmentCheckerService;
import com.ipoallotmentchecker.service.FamilyMemberService;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/api/allotment")
public class MaaShitlaController {
	@Autowired
	private AllotmentCheckerService allotmentCheckerService;

	@Autowired
	private FamilyMemberService familyMemberService;

	@Autowired
	private UserRepository repository;

	@Autowired
	private JwtService jwtService;

	@PostMapping("/status")
	public ResponseEntity<Map<String, Object>> checkAllotmentStatus(@RequestParam(name = "ipoName") String ipoName,
			@RequestHeader("Authorization") String token) throws JsonMappingException, JsonProcessingException {

		String tokens = token.replace("Bearer ", "").trim();

		Claims claims = jwtService.extractAllClaims(tokens);
		
		Long userId = claims.get("userId", Long.class); // This gets the userId, assuming it's stored as Long

		User user = repository.findById(userId).get(); // Assuming a method to fetch user details
		AllotmentResponse userStatus = allotmentCheckerService.checkAllotmentMaashitla(user.getPan(), ipoName);

		List<FamilyMemberDTO> familyMembers = familyMemberService.getFamilyMembersByUserId(userId);

		List<AllotmentResponse> linkedAccounts = new ArrayList<>();
		for (FamilyMemberDTO member : familyMembers) {
			try {
				AllotmentResponse familyStatus = allotmentCheckerService.checkAllotmentMaashitla(member.getPanNumber(),
						ipoName);
				linkedAccounts.add(familyStatus);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Wrap the response in a map
		Map<String, Object> response = new HashMap<>();

		// Add userStatus and linkedAccounts to the response map
		response.put("userStatus", userStatus);
		response.put("linkedAccounts", linkedAccounts);

		return ResponseEntity.ok(response);
	}

}
