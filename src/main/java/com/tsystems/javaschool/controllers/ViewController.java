package com.tsystems.javaschool.controllers;

import com.tsystems.javaschool.db.entities.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
        if (request.isUserInRole("ROLE_ADMIN"))
            return "admin";
        else {
            User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
            return "redirect:/"+user.getId();
        }

    }

    @RequestMapping("/{id}")
    public String userHome(){
        return "admin";
    }
}
