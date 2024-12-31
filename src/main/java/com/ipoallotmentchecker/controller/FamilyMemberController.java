package com.ipoallotmentchecker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipoallotmentchecker.config.JwtService;
import com.ipoallotmentchecker.dto.FamilyMemberDTO;
import com.ipoallotmentchecker.dto.StatusMessage;
import com.ipoallotmentchecker.model.FamilyMember;
import com.ipoallotmentchecker.model.User;
import com.ipoallotmentchecker.repository.UserRepository;
import com.ipoallotmentchecker.service.FamilyMemberService;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/api/family")
public class FamilyMemberController {

	@Autowired
	private FamilyMemberService familyMemberService;
	
	@Autowired
	private JwtService jwtService;
	
	

	@PostMapping("/add")
	public ResponseEntity<StatusMessage> addFamilyMember(@RequestBody FamilyMemberDTO familyMember,
			@RequestHeader("Authorization") String token) {
		
		String tokens = token.replace("Bearer ", "").trim();

		Claims claims = jwtService.extractAllClaims(tokens);
		
		Long userId = claims.get("userId", Long.class); // This gets the userId, assuming it's stored as Long

		
		StatusMessage status = familyMemberService.addFamilyMember(familyMember,userId);
		return ResponseEntity.ok(status);
	}

	@GetMapping
	public ResponseEntity<List<FamilyMemberDTO>> getFamilyMembers(@RequestHeader("Authorization") String token) {
		
		String tokens = token.replace("Bearer ", "").trim();

		Claims claims = jwtService.extractAllClaims(tokens);
		
		Long userId = claims.get("userId", Long.class); // This gets the userId, assuming it's stored as Long

		
		
	    List<FamilyMemberDTO> members = familyMemberService.getFamilyMembersByUserId(userId);
	    return ResponseEntity.ok(members);
	}

}
