package com.tsystems.javaschool.controllers.rest;

import com.tsystems.javaschool.business.services.interfaces.UserService;
import com.tsystems.javaschool.util.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
