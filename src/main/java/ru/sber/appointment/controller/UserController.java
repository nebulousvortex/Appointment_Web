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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.sber.appointment.model.Ticket;
import ru.sber.appointment.model.User;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private String authToken;
    private ResponseEntity<JsonNode> response;
    private User user;

    @GetMapping("/account")
    public String getPersonal(Model model, HttpSession session) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        if(session.getAttribute("accessToken") != null) {
            authToken = session.getAttribute("accessToken").toString();
            user = (User) session.getAttribute("user");
        } else {
            return "redirect:/auth/login";
        }

        headers.set("Authorization", "Bearer " + authToken);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        final String url = "http://localhost:8080/api/v1/ticket/get/" + user.getUsername() + "/user/tickets";
        RestTemplate restTemplate = new RestTemplate();

        try {
            response = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);
        } catch (HttpClientErrorException.Unauthorized e){
            return "redirect:/auth/login";
        }

        JsonNode json = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        List<Ticket> ticketsList = Arrays.asList(objectMapper.treeToValue(json, Ticket[].class));
        model.addAttribute("tickets", ticketsList);
        model.addAttribute("user", user);
        return "userTickets";
    }
}
