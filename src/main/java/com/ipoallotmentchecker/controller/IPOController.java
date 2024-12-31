package com.ipoallotmentchecker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipoallotmentchecker.model.IPO;
import com.ipoallotmentchecker.service.IPOService;

@RestController
@RequestMapping("/api/ipos")
public class IPOController {
	@Autowired
	private IPOService ipoService;

	@GetMapping
	public ResponseEntity<List<IPO>> getAllIPOs() {
		return ResponseEntity.ok(ipoService.getAllIPOs());
	}
}
