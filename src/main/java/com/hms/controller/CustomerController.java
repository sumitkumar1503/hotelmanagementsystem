package com.hms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/customer")
@Controller
public class CustomerController {
    @RequestMapping("/home")
    public String home(){
        return "home";
    }
}
