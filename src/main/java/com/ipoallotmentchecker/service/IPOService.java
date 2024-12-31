package com.ipoallotmentchecker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ipoallotmentchecker.model.IPO;
import com.ipoallotmentchecker.repository.IPORepository;

@Service
public class IPOService {
	@Autowired
	private IPORepository ipoRepository;

	public List<IPO> getAllIPOs() {
		return ipoRepository.findAll();
	}
}
