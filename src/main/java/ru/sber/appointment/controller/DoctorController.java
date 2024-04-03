package ru.sber.appointment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.sber.appointment.model.Doctor;
import ru.sber.appointment.model.Ticket;
import ru.sber.appointment.model.User;
import ru.sber.appointment.service.ResponseServiceImpl;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/doctor")
public class DoctorController {
    @Autowired
    ResponseServiceImpl responseService;
    private String authToken;
    private HttpEntity<String> response;
    private User user;

    @Value("${url.rest.doctor.get}") private String getUrl;
    @Value("${url.rest.doctor.patients}") private String patientsUrl;

    @GetMapping("/list")
    public String getDoctorList(@RequestParam(name = "firstName", required = false) String firstName,
                                @RequestParam(name = "lastName", required = false) String lastName,
                                @RequestParam(name = "spec", required = false) String spec,
                                Model model, HttpSession session) throws JsonProcessingException {

        if (responseService.getUnauthorized(session)) {
            return "redirect:/auth/login";
        }
        user = (User) session.getAttribute("user");

        response = responseService.getResponse(session, getUrl, HttpMethod.POST, responseService.generateDoctor(firstName, lastName, spec));
        String responseBody = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode json = objectMapper.readTree(responseBody);
        List<Doctor> doctorsList = Arrays.asList(objectMapper.treeToValue(json, Doctor[].class));
        model.addAttribute("doctors", doctorsList);
        model.addAttribute("user", user);
        return "doctorList";
    }

    @GetMapping("/{username}/patients")
    public String getPatientList(Model model, HttpSession session) throws JsonProcessingException {
        if (responseService.getUnauthorized(session)) {
            return "redirect:/auth/login";
        }
        user = (User) session.getAttribute("user");
        final String url = patientsUrl + user.getUsername();
        response = responseService.getResponse(session, url, HttpMethod.GET, null);
        String responseBody = response.getBody();

        if (response.getBody() != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode json = objectMapper.readTree(responseBody);
            List<Ticket> ticketList = Arrays.asList(objectMapper.treeToValue(json, Ticket[].class));
            model.addAttribute("tickets", ticketList);
            model.addAttribute("user", user);
            return "patientList";
        } else{
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", "Недостаточно прав");
            return "doctorList";
        }
    }
}
