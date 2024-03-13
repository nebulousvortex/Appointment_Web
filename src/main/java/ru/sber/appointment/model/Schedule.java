package ru.sber.appointment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Schedule {

    private Long id;
    private Doctor doctor;
    private DayType dayType;
    private String date;
    private final List<Ticket> tickets = new ArrayList<>();
    public Long getId() {
        return id;
    }
    public Doctor getDoctor() {
        return doctor;
    }
    public DayType getDayType() {
        return dayType;
    }
    public String getDate() {
        return date;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
    public void setDayType(DayType dayType) {
        this.dayType = dayType;
    }
    public void setDate(String date) {
        this.date = date;
    }
}
