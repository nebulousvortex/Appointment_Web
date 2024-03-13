package ru.sber.appointment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import ru.sber.appointment.model.Doctor;
import ru.sber.appointment.model.Ticket;
import ru.sber.appointment.model.User;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

    private String authToken;
    private ResponseEntity<JsonNode> response;
    private User user;

    @GetMapping("/getAll")
    public String getAllDoctors(Model model, HttpSession session) throws JsonProcessingException {
        final String url = "http://localhost:8080/api/v1/doctors/get";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        if(session.getAttribute("accessToken") != null) {
            authToken = session.getAttribute("accessToken").toString();
            user = (User) session.getAttribute("user");
        } else {
            return "redirect:/auth/login";
        }
        headers.set("Authorization", "Bearer " + authToken);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        response = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);

        JsonNode json = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Doctor> doctorsList = Arrays.asList(objectMapper.treeToValue(json, Doctor[].class));
        model.addAttribute("doctors", doctorsList);
        model.addAttribute("user", user);
        return "doctors";
    }

    @GetMapping("/{username}/patients")
    public String getDoctorPatients(Model model, HttpSession session) throws JsonProcessingException {
        if(session.getAttribute("accessToken") != null) {
            authToken = session.getAttribute("accessToken").toString();
            user = (User) session.getAttribute("user");
        } else {
            return "redirect:/auth/login";
        }
        final String url = "http://localhost:8080/api/v1/ticket/get/" + user.getUsername() + "/doctor/tickets";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authToken);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        response = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);
        if (response.getBody() != null) {
            JsonNode json = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            List<Ticket> ticketsList = Arrays.asList(objectMapper.treeToValue(json, Ticket[].class));
            model.addAttribute("tickets", ticketsList);
            model.addAttribute("user", user);
            return "doctorPatient";
        } else{
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", "Недостаточно прав");
            return "doctors";
        }
    }
}
