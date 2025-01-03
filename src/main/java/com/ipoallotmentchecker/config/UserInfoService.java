package com.ipoallotmentchecker.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ipoallotmentchecker.dto.UserRegistrationDTO;
import com.ipoallotmentchecker.model.User;
import com.ipoallotmentchecker.repository.UserRepository;

@Service
public class UserInfoService implements UserDetailsService {

	@Autowired
	private UserRepository repository;

	@Autowired
	private PasswordEncoder encoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userDetail = repository.findByEmail(username); // Assuming 'email' is used as username

		// Converting UserInfo to UserDetails
		return userDetail.map(UserInfoDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
	}

	public String addUser(UserRegistrationDTO userInfo) {
		// Encode password before saving the user
		User user = new User();
		user.setPan(userInfo.getPan());
		user.setName(userInfo.getName());
		user.setEmail(userInfo.getEmail());
		user.setPassword(encoder.encode(userInfo.getPassword()));
		user.setRoles("ROLE_USER");
		repository.save(user);
		return "User Added Successfully";
	}
}