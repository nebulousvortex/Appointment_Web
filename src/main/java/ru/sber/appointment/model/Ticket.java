package ru.sber.appointment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Ticket {


    private Long id;
    private String time;
    private Schedule schedule;
    private User user;

    public Long getId() {
        return id;
    }
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    public String getTime() {
        return time;
    }
    public Schedule getSchedule() {
        return schedule;
    }

    public User getUser() {
        return user;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
