package ru.sber.appointment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.sber.appointment.model.Ticket;
import ru.sber.appointment.model.User;
import ru.sber.appointment.service.ResponseServiceImpl;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    ResponseServiceImpl responseService;
    private String authToken;
    private HttpEntity<String> response;
    private User user;

    @GetMapping("/account")
    public String getAccount(Model model, HttpSession session) throws JsonProcessingException {

        if (responseService.getUnauthorized(session)) {
            return "redirect:/auth/login";
        }
        user = (User) session.getAttribute("user");

        final String url = "http://localhost:8080/api/v1/ticket/get/tickets/user/" + user.getUsername();
        response = responseService.getResponse(session, url, HttpMethod.GET, null);
        String responseBody = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode json = objectMapper.readTree(responseBody);
        List<Ticket> ticketList = Arrays.asList(objectMapper.treeToValue(json, Ticket[].class));
        model.addAttribute("tickets", ticketList);
        model.addAttribute("user", user);
        return "userAccount";
    }
}
