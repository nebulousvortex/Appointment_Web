package ru.sber.appointment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Doctor {
    private Long id;
    private String specialization;
    private User user;
    private List<Schedule> schedules = new ArrayList<>();
    public void setId(Long id) {
        this.id = id;
    }
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public String getSpecialization() {
        return specialization;
    }
    public Long getId() {
        return id;
    }
    public User getUser() {
        return user;
    }
}
