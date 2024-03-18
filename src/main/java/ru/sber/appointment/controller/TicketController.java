package ru.sber.appointment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sber.appointment.model.Ticket;
import ru.sber.appointment.model.User;
import ru.sber.appointment.service.ResponseServiceImpl;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ticket")
public class TicketController {
    @Autowired
    ResponseServiceImpl responseService;
    private String authToken;
    private HttpEntity<String> response;
    private User user;

    @GetMapping("/doctor/{doctorId}")
    public String getTicketList(@PathVariable Long doctorId, Model model, HttpSession session) throws JsonProcessingException {
        final String url = "http://localhost:8080/api/v1/ticket/get/tickets/" + doctorId;
        if (responseService.getUnauthorized(session)) {
            return "redirect:/auth/login";
        }

        user = (User) session.getAttribute("user");
        response = responseService.getResponse(session, url, HttpMethod.GET, null);

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode json = objectMapper.readTree(responseBody);
        List<Ticket> ticketsList = Arrays.asList(objectMapper.treeToValue(json, Ticket[].class));

        model.addAttribute("doctorId", doctorId);
        model.addAttribute("tickets", ticketsList);
        model.addAttribute("user", user);
        return "ticketList";
    }

    @PostMapping("/doctor/{doctorId}")
    public String appoint(@RequestParam("ticketId") Long ticketId, Model model, HttpSession session) throws JsonProcessingException {
        final String url = "http://localhost:8080/api/v1/ticket/put/tickets";
        if (responseService.getUnauthorized(session)) {
            return "redirect:/auth/login";
        }

        user = (User) session.getAttribute("user");

        Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("id", ticketId);
        Map<String, Object> userMap = new LinkedHashMap<>();
        userMap.put("username", user.getUsername());
        requestBody.put("user", userMap);

        response = responseService.getResponse(session, url, HttpMethod.PUT, requestBody);

        if(response.getBody() == null) {
            return "redirect:/doctor/list";
        } else {
            return "error";
        }
    }
}
