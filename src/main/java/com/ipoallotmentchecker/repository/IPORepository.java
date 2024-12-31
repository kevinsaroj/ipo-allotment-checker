package com.ipoallotmentchecker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ipoallotmentchecker.model.IPO;
import com.ipoallotmentchecker.model.User;

@Repository
public interface IPORepository extends JpaRepository<IPO, Long> {

	 @Query("SELECT u FROM User u JOIN u.ipos i WHERE i.name = :ipoName")
	    List<User> findUsersByIpoName(@Param("ipoName") String ipoName);

}
