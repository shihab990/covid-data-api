package com.shihab.coviddata.error;

public class CovidDataNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CovidDataNotFoundException(Long id) {
        super("Data id not found : " + id);
    }

}
