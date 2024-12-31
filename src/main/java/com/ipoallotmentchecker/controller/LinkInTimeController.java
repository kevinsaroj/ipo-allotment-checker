package com.ipoallotmentchecker.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ipoallotmentchecker.config.JwtService;
import com.ipoallotmentchecker.model.User;
import com.ipoallotmentchecker.repository.UserRepository;
import com.ipoallotmentchecker.service.LinkInTimeService;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/api/linkintime")
public class LinkInTimeController {

	@Autowired
	private LinkInTimeService linkInTimeService;

	@Autowired
	private UserRepository repository;

	@Autowired
	private JwtService jwtService;

	// Fetch the ViewState and generate token for LinkInTime
	@PostMapping("/status")
	public ResponseEntity<Map<String, Object>> checkAllotmentStatus(@RequestParam(name = "ipoName") String ipoName,

			@RequestHeader("Authorization") String token) {

		String tokens = token.replace("Bearer ", "").trim();

		Claims claims = jwtService.extractAllClaims(tokens);
		// Retrieve the 'id' claim (which is your userId)
		Long userId = claims.get("userId", Long.class); // This gets the userId, assuming it's stored as Long

		// Fetch the user details and check their allotment status
		User user = repository.findById(userId).get(); // Assuming a method to fetch user details) {

		Map<String, Object> checkAllotmentStatus = linkInTimeService.checkAllotmentStatus(ipoName, userId,
				user.getPan());
		return ResponseEntity.ok(checkAllotmentStatus);
	}
}
