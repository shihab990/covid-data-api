package com.shihab.coviddata.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shihab.coviddata.entities.CovidData;
import com.shihab.coviddata.error.CovidDataNotFoundException;
import com.shihab.coviddata.error.TimestampNotValidException;
import com.shihab.coviddata.repo.CovidDataRepository;

@Service
public class CovidDataServiceImpl implements CovidDataService {

	@Autowired
	private CovidDataRepository repository;
	
	// Find All
	@Override
	public List<CovidData> findAll() {
		return repository.findAll();
	}

	// Save
	@Override
	public CovidData saveData(CovidData covidData) {
		covidData.setTime(new Timestamp(System.currentTimeMillis()).toString());
		return repository.save(covidData);
	}

	// Find by id
	@Override
	public CovidData findById(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new CovidDataNotFoundException(id));
	}

	//Search by state, startDate and endDate OR Search by startDate and endDate
	@Override
	public List<CovidData> searchByStateDate(String state, String startDate, String endDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    	Timestamp start;
    	Timestamp end;
		try {
			start = new Timestamp(dateFormat.parse(startDate).getTime());
			end = new Timestamp(dateFormat.parse(endDate).getTime());
			return repository.searchByStateDate(state, startDate, endDate);
		} catch (ParseException e) {
			throw new TimestampNotValidException();
		}

	}

	// Find by state
	@Override
	public List<CovidData> searchByState(String state) {
		return repository.searchByState(state);
	}

	// Update
	@Override
	public CovidData updateData(CovidData covidData, Long id) {
		return repository.findById(id)
                .map(x -> {
                    x.setName(covidData.getName());
                    x.setState(covidData.getState());
                    x.setTime(covidData.getTime());
                    return repository.save(x);
                })
                .orElseGet(() -> {
                	covidData.setId(id);
                    return repository.save(covidData);
                });
	}

	// delete
	@Override
	public void deleteData(Long id) {
		repository.deleteById(id);
	}

}
