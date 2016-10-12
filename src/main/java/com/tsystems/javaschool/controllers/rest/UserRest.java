package com.tsystems.javaschool.controllers.rest;

import com.tsystems.javaschool.business.services.interfaces.UserService;
import com.tsystems.javaschool.util.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * Created by alex on 06.10.16.
 */
@RestController
@RequestMapping("/rest/users")
public class UserRest {

    private final UserService service;

    @Autowired
    public UserRest(UserService service) {
        this.service = service;
    }

    @PutMapping("/{id}")
    public ResponseEntity changePassword(@PathVariable("id") Integer id,
                                         @RequestParam("oldPassword") String oldPassword,
                                         @RequestParam("newPassword") String newPassword,
                                         @RequestParam("newPasswordRepeat") String newPasswordRepeat) {
        if (!newPassword.equals(newPasswordRepeat)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Message", "New passwords are different"));
        }

        Boolean res = service.changePassword(id, oldPassword, newPassword);

        if (res == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Message", "No user found with 'id' = " + id));
        } else if (!res) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Message", "Wrong old password"));
        } else {
            return ResponseEntity.ok().body("");
        }
    }

    @PostMapping("/reset")
    public ResponseEntity resetPassword(@RequestParam(required = false) String email) {
        if (email == null || email.equals("")) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Message", "Empty email"));
        }
        boolean result = service.disablePassword(email);
        if (!result) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Message", "User not found"));
        } else {
            return ResponseEntity.ok().body("");
        }
    }

    @PostMapping("/password")
    public ResponseEntity changePassword(@RequestParam(required = false) String email,
                                         @RequestParam(required = false) String code,
                                         @RequestParam(required = false) String newPassword,
                                         @RequestParam(required = false) String repeatPassword) {
        if (email == null || email.equals("")||
                code == null || code.equals("")||
                newPassword == null || newPassword.equals("")||
                repeatPassword == null || repeatPassword.equals("")) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Message", "All fields are required"));
        }

        if (!newPassword.equals(repeatPassword)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Message", "Passwords are different"));
        }
        Boolean res = service.changePasswordWithCode(email, code, newPassword);

        if (res == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Message", "No user found"));
        } else if (!res) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Message", "Wrong code"));
        } else {
            return ResponseEntity.ok().body("");
        }
    }

    @GetMapping("/role")
    public ResponseEntity getRole() {
        List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        return ResponseEntity.ok().body(authorities);
    }
}
