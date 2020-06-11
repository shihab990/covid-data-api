package com.shihab.coviddata;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import com.shihab.coviddata.entities.CovidData;
import com.shihab.coviddata.repo.CovidDataRepository;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@SpringBootApplication
public class StartCovidDataApplication {

    // start everything
    public static void main(String[] args) {
        SpringApplication.run(StartCovidDataApplication.class, args);
    }

    @Profile("demo")
    @Bean
    CommandLineRunner initDatabase(CovidDataRepository repository) {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return args -> {
        	repository.save(new CovidData("Jack", "infected", new Timestamp(dateFormat.parse("2011-10-02 18:48:05").getTime()).toString()));
            repository.save(new CovidData("Michael", "recovered", new Timestamp(dateFormat.parse("2012-10-02 18:48:05").getTime()).toString()));
            repository.save(new CovidData("Cristin", "deceased", new Timestamp(dateFormat.parse("2013-10-02 18:48:05").getTime()).toString()));
        };
    }
}