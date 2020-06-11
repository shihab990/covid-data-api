package com.shihab.coviddata.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.shihab.coviddata.entities.CovidData;
import com.shihab.coviddata.service.CovidDataService;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.text.ParseException;
import java.util.List;

@RestController
@Validated
public class CovidDataController {

    @Autowired
    private CovidDataService service;

    // Find All
    @GetMapping("/api/covid")
    List<CovidData> findAll() {
    	return service.findAll();
    }

    // Save
    @PostMapping("/api/covid")
    ResponseEntity<CovidData> saveData(@Valid @RequestBody CovidData codidData) {
    	CovidData saveData = service.saveData(codidData);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveData);   
    }

    // Find by id
    @GetMapping("/api/covid/id/{id}")
    CovidData findById(@PathVariable @Min(1) Long id) {
        return service.findById(id);
    }
    
    
    //Search by state, startDate and endDate OR Search by startDate and endDate
    @GetMapping("/api/covid/search")
    List<CovidData> searchByStateDate(
    		@RequestParam(required = false) String state, 
            @RequestParam String startDate, 
            @RequestParam String endDate) throws ParseException{
    	
        return service.searchByStateDate(state, startDate, endDate);
    }
    
    
    // Find by state
    @GetMapping("/api/covid/state/{state}")
    List<CovidData> searchByState(@PathVariable String state) {
    	return service.searchByState(state);
    }


    // Update
    @PutMapping("/api/covid/{id}")
    CovidData saveOrUpdate(@RequestBody CovidData covidData, @PathVariable Long id) {
    	return service.updateData(covidData, id);
    }


    // delete
    @DeleteMapping("/api/covid/{id}")
    void deleteData(@PathVariable Long id) {
    	service.deleteData(id);
    }

}
