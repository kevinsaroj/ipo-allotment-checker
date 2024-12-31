package com.ipoallotmentchecker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipoallotmentchecker.service.NotificationService;

@RestController
@RequestMapping("/api/jobs")
public class JobSchedulerController {

	@Autowired
	private NotificationService notificationService;

	@PostMapping("/sync")
	public void testSync() {

		notificationService.notifyAllUsers();

	}

}
