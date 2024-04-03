package ru.sber.appointment.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import ru.sber.appointment.model.JwtRequest;
import ru.sber.appointment.model.JwtResponse;
import ru.sber.appointment.model.User;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Value("${url.rest.auth.login}") private String loginUrl;

    @Value("${url.rest.auth.registration}") private String regUrl;

    @GetMapping("/registration")
    public String register(){
        return ("registration");
    }
    @PostMapping("/registration")
    public String register(@ModelAttribute User user, Model model){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(regUrl, user, String.class);
        if (responseEntity.getBody() != null) {
            List<String> errorsList = Arrays.asList(responseEntity.getBody().substring(1, responseEntity.getBody().length()-1).split(","));
            model.addAttribute("errorMessage", errorsList);
            return "registration";
        }
        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String login(){
        return ("login");
    }
    @PostMapping("/login")
    public String login(@ModelAttribute JwtRequest jwtRequest, Model model, HttpSession session){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JwtResponse> responseEntity = restTemplate.postForEntity(loginUrl, jwtRequest, JwtResponse.class);
        JwtResponse jwtResponse = responseEntity.getBody();
        if (jwtResponse != null) {
            session.setAttribute("accessToken", jwtResponse.getAccessToken());
            session.setAttribute("username", jwtRequest.getLogin());
            session.setAttribute("user", jwtResponse.getUser());
            session.setAttribute("role", jwtResponse.getUser().getRoles().iterator().next());
            return "redirect:/doctor/list";
        }
        else {
            model.addAttribute("errorMessage", "Неверные логин или пароль");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/auth/login";
    }
}