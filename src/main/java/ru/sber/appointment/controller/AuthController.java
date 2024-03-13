package ru.sber.appointment.controller;

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

    @GetMapping("/registration")
    public String renderRegistration(){
        return ("registration");
    }
    @PostMapping("/registration")
    public String registerUser(@ModelAttribute User user, Model model){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/api/v1/registration";
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, user, String.class);
        if (responseEntity.getBody() != null) {
            List<String> errorsList = Arrays.asList(responseEntity.getBody().substring(1, responseEntity.getBody().length()-1).split(","));
            model.addAttribute("errorMessage", errorsList);
            return "registration";
        }
        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String renderLogin(){
        return ("login");
    }
    @PostMapping("/login")
    public String loginUser(@ModelAttribute JwtRequest jwtRequest, Model model, HttpSession session){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/api/v1/auth/login";
        ResponseEntity<JwtResponse> responseEntity = restTemplate.postForEntity(url, jwtRequest, JwtResponse.class);
        JwtResponse jwtResponse = responseEntity.getBody();
        if (jwtResponse != null) {
            System.out.println( jwtResponse.getUser().getAuthorities());
            session.setAttribute("accessToken", jwtResponse.getAccessToken());
            session.setAttribute("username", jwtRequest.getLogin());
            session.setAttribute("user", jwtResponse.getUser());
            session.setAttribute("role", jwtResponse.getUser().getRoles().iterator().next());
            return "redirect:/doctor/getAll";
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