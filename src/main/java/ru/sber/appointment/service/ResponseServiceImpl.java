package ru.sber.appointment.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.sber.appointment.model.Doctor;
import ru.sber.appointment.model.User;
import ru.sber.appointment.service.interfaces.ResponseService;

import javax.servlet.http.HttpSession;

@Service
public class ResponseServiceImpl implements ResponseService {
    private final HttpHeaders headers = new HttpHeaders();
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public <T> HttpEntity<String> getResponse(HttpSession session, String url, HttpMethod httpMethod, T entityBody){

        String authToken = session.getAttribute("accessToken").toString();
        headers.set("Authorization", "Bearer " + authToken);
        HttpEntity<T> entity = new HttpEntity<>(entityBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, httpMethod , entity, String.class);

        if (response.getBody() != null) {
            return new HttpEntity<>(response.getBody(), null);
        }else {
            return new HttpEntity<>(null, null);
        }
    }

    @Override
    public boolean getUnauthorized(HttpSession session){
        return session == null || session.getAttribute("accessToken") == null;
    }

    @Override
    public Doctor generateDoctor(String firstName, String lastName, String spec){
        Doctor doctor = new Doctor();
        User doctorUser = new User();
        doctorUser.setFirstName(firstName);
        doctorUser.setLastName(lastName);
        doctor.setSpecialization(spec);
        doctor.setUser(doctorUser);
        return doctor;
    }
}