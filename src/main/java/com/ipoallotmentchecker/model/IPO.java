package com.ipoallotmentchecker.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ipos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IPO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private double price;

	@Column(nullable = false)
	private double gmp; // Gray Market Price

	private LocalDate announcementDate;

	private String registrar;
	
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
}
