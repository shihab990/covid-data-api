package com.shihab.coviddata.service;

import java.util.List;

import com.shihab.coviddata.entities.CovidData;

public interface CovidDataService {
	List<CovidData> findAll();
	CovidData saveData(CovidData covidData);
	CovidData findById(Long id);
	List<CovidData> searchByStateDate(String state, String startDate, String endDate);
	List<CovidData> searchByState(String state);
	CovidData updateData(CovidData covidData, Long id);
	void deleteData(Long id);
}
