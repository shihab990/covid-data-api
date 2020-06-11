package com.shihab.coviddata.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shihab.coviddata.entities.CovidData;

public interface CovidDataRepository extends JpaRepository<CovidData, Long> {

	//search by state
	@Query("SELECT c from CovidData c WHERE c.state = :state")
	List<CovidData> searchByState(@Param("state") String state);
	
	//Search by state, startDate and endDate OR Search by startDate and endDate
	@Query("SELECT c from CovidData c WHERE (:state is null OR c.state = :state) AND (c.time between :startDate and :endDate)")
	List<CovidData> searchByStateDate(@Param("state") String state, @Param("startDate") String startDate, @Param("endDate") String endDate);
}