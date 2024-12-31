package com.ipoallotmentchecker.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ipoallotmentchecker.dto.LinkinTimeResponseDTO;
import com.ipoallotmentchecker.model.User;
import com.ipoallotmentchecker.repository.IPORepository;
import com.ipoallotmentchecker.util.EmailService;

@Service
public class NotificationService {

	@Autowired
	private EmailService emailService;

	@Autowired
	private IPORepository ipoRepository;

	@Autowired
	private LinkInTimeService linkInTimeService;

	public void notifyUser(String email, LinkinTimeResponseDTO allotmentResponse) {
		String subject = "IPO Allotment Status";
		String body = String.format(
				"Dear %s,\n\nYour allotment status for IPO '%s' is as follows:\n\n"
						+ "Shares Applied: %d\nShares Allotted: %d\nStatus: %s\n\nThank you!",
				allotmentResponse.getName(), "IPO Name", allotmentResponse.getShareApplied(),
				allotmentResponse.getShareAlloted(), allotmentResponse.getStatus());
		emailService.sendEmail(email, subject, body);
	}

	@Scheduled(fixedRate = 3600000) // Executes every 1 hour
	public void notifyAllUsers() {

		List<User> users = ipoRepository.findUsersByIpoName("11799");

		for (User user : users) {
			try {
				// Fetch the allotment status from the service
				Map<String, Object> checkAllotmentStatus = linkInTimeService.checkAllotmentStatus("", user.getId(),
						user.getPan());

				if (checkAllotmentStatus != null) {
					// Extract the user's allotment status from the response map
					LinkinTimeResponseDTO allotmentResponse = (LinkinTimeResponseDTO) checkAllotmentStatus
							.get("userStatus");

					// Notify the user about their allotment status
					if (allotmentResponse != null) {
						notifyUser(user.getEmail(), allotmentResponse);
					}

					// Extract the linked accounts allotment status (list of linked accounts)
					List<LinkinTimeResponseDTO> linkedAccounts = (List<LinkinTimeResponseDTO>) checkAllotmentStatus
							.get("linkedAccounts");

					// Notify each linked account about their allotment status
					if (linkedAccounts != null && !linkedAccounts.isEmpty()) {
						for (LinkinTimeResponseDTO linkedAccount : linkedAccounts) {
//	                        // Assuming linked account has an email field for notification
//	                        // You can retrieve the email from the linked account or assume a default behavior
//	                        String linkedEmail = linkedAccount.getEmail(); // Replace with actual logic if required
							notifyUser(user.getEmail(), linkedAccount);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				// Handle any exceptions that occur during the process
			}
		}
	}

}
