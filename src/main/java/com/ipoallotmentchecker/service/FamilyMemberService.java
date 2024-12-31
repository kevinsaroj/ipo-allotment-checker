package com.ipoallotmentchecker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import com.ipoallotmentchecker.dto.FamilyMemberDTO;
import com.ipoallotmentchecker.dto.StatusMessage;
import com.ipoallotmentchecker.exception.UserNotFoundException;
import com.ipoallotmentchecker.model.FamilyMember;
import com.ipoallotmentchecker.model.User;
import com.ipoallotmentchecker.repository.FamilyMemberRepository;
import com.ipoallotmentchecker.repository.UserRepository;

@Service
public class FamilyMemberService {

	@Autowired
	private FamilyMemberRepository familyMemberRepository;

	@Autowired
	private UserRepository userRepository;

	public StatusMessage addFamilyMember(FamilyMemberDTO familyMember, Long userId) {
		FamilyMember member = new FamilyMember();
		StatusMessage message = new StatusMessage();
		// Fetch the user based on the user ID in the family member object
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

		// Set the user to the family member
		member.setName(familyMember.getName());
		member.setPanNumber(familyMember.getPanNumber());

		member.setUser(user);

		// Save the family member to the database
		this.familyMemberRepository.save(member);

		message.setMessage("Family Details Added For this User Id " + userId);
		message.setCode(HttpStatusCode.valueOf(200));

		return message;

	}

	public List<FamilyMemberDTO> getFamilyMembersByUserId(Long userId) {
		// Fetch the user by ID, throw an exception if not found
		User foundUser = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

		// Map the list of FamilyMember entities to FamilyMemberDTOs
		return foundUser.getFamilyMembers().stream().map(familyMember -> new FamilyMemberDTO(familyMember.getId(),
				familyMember.getName(), familyMember.getPanNumber())).toList();
	}

}
