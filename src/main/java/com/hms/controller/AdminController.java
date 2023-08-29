package com.hms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
@Controller
public class AdminController {

    @RequestMapping("/home")
    public String home(){
        System.out.println("ddd");
        return "admin/home";
    }
}
