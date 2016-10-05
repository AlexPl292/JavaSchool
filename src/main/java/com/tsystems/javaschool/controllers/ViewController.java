package com.tsystems.javaschool.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * Created by alex on 04.10.16.
 */
@Controller
@RequestMapping
public class ViewController {

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/")
    public String home(HttpServletRequest request, Principal principal) {
        return "admin";
    }
}
