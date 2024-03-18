package ru.sber.appointment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultPathController {
    @GetMapping("/")
    public String redirectDefault(){
        return "redirect:/doctor/list";
    }
}
