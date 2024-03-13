package ru.sber.appointment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.sber.appointment.model.Ticket;
import ru.sber.appointment.model.User;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/ticket")
public class TicketController {
    private String authToken;
    private ResponseEntity<JsonNode> response;
    private User user;

    @GetMapping("/{doctorId}")
    public String getDoctorTickets(@PathVariable Long doctorId, Model model, HttpSession session) throws JsonProcessingException {
        final String url = "http://localhost:8080/api/v1/ticket/get/" + doctorId + "/tickets";
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
        List<Ticket> ticketsList = Arrays.asList(objectMapper.treeToValue(json, Ticket[].class));
        model.addAttribute("doctorId", doctorId);
        model.addAttribute("tickets", ticketsList);
        model.addAttribute("user", user);
        return "doctorTickets";
    }

    @PostMapping("/{doctorId}")
    public String appointUser(@RequestParam("ticketId") Long ticketId, Model model, HttpSession session) throws JsonProcessingException {
        final String url = "http://localhost:8080/api/v1/ticket/appointUser";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String authToken = session.getAttribute("accessToken").toString();
        headers.set("Authorization", "Bearer " + authToken);
        user = (User) session.getAttribute("user");
        String jsonString = "{"
                + "\"id\":" + ticketId + ","
                + "\"user\":{"
                + "\"username\":\"" + user.getUsername() + "\""
                + "}"
                + "}";
        HttpEntity<String> entity = new HttpEntity<>(jsonString, headers);
        ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.PUT, entity, JsonNode.class);
        if(response.getStatusCode() == HttpStatus.OK) {
            return "redirect:/doctor/getAll";
        } else {
            return "error";
        }
    }
}
