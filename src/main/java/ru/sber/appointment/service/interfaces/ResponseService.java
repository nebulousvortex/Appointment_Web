package ru.sber.appointment.service.interfaces;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import ru.sber.appointment.model.Doctor;

import javax.servlet.http.HttpSession;

public interface ResponseService {
    <T> HttpEntity<String> getResponse(HttpSession session, String url, HttpMethod httpMethod, T entityBody);

    boolean getUnauthorized(HttpSession session);

    Doctor generateDoctor(String firstName, String lastName, String spec);
}
