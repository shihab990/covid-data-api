package com.shihab.coviddata.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@Entity
public class CovidData {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty(message = "Please provide a name")
    private String name;

    @NotEmpty(message = "Please provide a state")
    private String state;

    private String time;

    // avoid this "No default constructor for entity"
    public CovidData() {
    }

    public CovidData(Long id, String name, String state, String time) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.time = time;
    }

    public CovidData(String name, String state, String time) {
        this.name = name;
        this.state = state;
        this.time = time;
    }
    
    public CovidData(String name, String state) {
        this.name = name;
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "CovidData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", state='" + state + '\'' +
                ", time=" + time +
                '}';
    }
}

