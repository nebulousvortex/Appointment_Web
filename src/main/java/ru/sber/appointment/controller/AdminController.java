package ru.sber.appointment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sber.appointment.model.Doctor;
import ru.sber.appointment.model.User;
import ru.sber.appointment.service.ResponseServiceImpl;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    ResponseServiceImpl responseService;
    private User user;
    private HttpEntity<String> response;

    @Value("${url.rest.admin_panel}") private String adminUrl;

    @GetMapping("/{username}")
    public String getAccount(Model model, HttpSession session) throws JsonProcessingException {
        if (responseService.getUnauthorized(session)) {
            return "redirect:/auth/login";
        }

        user = (User) session.getAttribute("user");
        final String url = adminUrl + user.getUsername() + "/get";
        response = responseService.getResponse(session, url, HttpMethod.GET, null);
        if (response.getBody() != null) {
            model.addAttribute("user", user);
            return "adminAccount";
        } else {
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", "Недостаточно прав");
            return "redirect:/doctor/list";
        }
    }

    @PostMapping("/delete/user")
    public String deleteUser(Model model, HttpSession session, @RequestParam Long id){
        if (responseService.getUnauthorized(session)) {
            return "redirect:/auth/login";
        }
        user = (User) session.getAttribute("user");
        final String url = adminUrl + user.getUsername() + "/delete/user";
        User userToDelete = new User();
        userToDelete.setId(id);
        response = responseService.getResponse(session, url, HttpMethod.DELETE, userToDelete);

        if (response.getBody() != null) {
            model.addAttribute("user", user);
            List<String> errorsList = Arrays.asList(response.getBody().substring(1, response.getBody().length()-1).split(","));
            model.addAttribute("errorMessage", errorsList);
            return "adminAccount";
        } else {
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", "Недостаточно прав");
            return "redirect:/doctor/list";
        }
    }

    @PostMapping("/delete/doctor")
    public String deleteDoctor(Model model, HttpSession session, @RequestParam Long id){
        if (responseService.getUnauthorized(session)) {
            return "redirect:/auth/login";
        }

        user = (User) session.getAttribute("user");
        final String url = adminUrl + user.getUsername() + "/delete/doctor";
        Doctor doctorToDelete = new Doctor();
        doctorToDelete.setId(id);
        response = responseService.getResponse(session, url, HttpMethod.DELETE, doctorToDelete);

        if (response.getBody() != null) {
            model.addAttribute("user", user);
            List<String> errorsList = Arrays.asList(response.getBody().substring(1, response.getBody().length()-1).split(","));
            model.addAttribute("errorMessage", errorsList);
            return "adminAccount";
        } else {
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", "Недостаточно прав");
            return "redirect:/doctor/list";
        }
    }

    @PostMapping("/add/doctor")
    public String addDoctor(Model model, HttpSession session, @RequestParam("username") String username, @RequestParam("specialization") String specialization){
        if (responseService.getUnauthorized(session)) {
            return "redirect:/auth/login";
        }

        user = (User) session.getAttribute("user");
        final String url = adminUrl + user.getUsername() + "/post/doctor";
        Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("specialization", specialization);
        Map<String, Object> userMap = new LinkedHashMap<>();
        userMap.put("username", username);
        requestBody.put("user", userMap);
        response = responseService.getResponse(session, url, HttpMethod.POST, requestBody);

        if (response.getBody() != null) {
            model.addAttribute("user", user);
            return "adminAccount";
        } else {
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", "Недостаточно прав");
            return "redirect:/doctor/list";
        }
    }
    @PostMapping("/add/schedule")
    public String addSchedule(Model model, HttpSession session, @RequestParam Long id){
        if (responseService.getUnauthorized(session)) {
            return "redirect:/auth/login";
        }

        final String url = adminUrl + user.getUsername() + "/post/schedule/bulk";
        Doctor doctorSchedule = new Doctor();
        doctorSchedule.setId(id);
        response = responseService.getResponse(session, url, HttpMethod.POST, doctorSchedule);

        if (response.getBody() != null) {
            model.addAttribute("user", user);
            return "adminAccount";
        } else {
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", "Недостаточно прав");
            return "redirect:/doctor/list";
        }
    }
}
