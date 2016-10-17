package com.tsystems.javaschool.controllers.rest;

import com.tsystems.javaschool.business.dto.UserDto;
import com.tsystems.javaschool.business.services.interfaces.UserService;
import com.tsystems.javaschool.db.entities.User;
import com.tsystems.javaschool.util.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Created by alex on 06.10.16.
 *
 *  Rest controller for user
 */
@RestController
@RequestMapping("/rest/users")
public class UserRest {

    private final UserService service;

    @Autowired
    public UserRest(UserService service) {
        this.service = service;
    }

    /**
     * Create new password for user
     * @param id if of user
     * @param oldPassword old password
     * @param newPassword new password
     * @param newPasswordRepeat new password repeat
     * @return response entity
     */
    @PutMapping("/{id}")
    @Secured("ROLE_USER")
    public ResponseEntity changePassword(@PathVariable("id") Integer id,
                                         @RequestParam("oldPassword") String oldPassword,
                                         @RequestParam("newPassword") String newPassword,
                                         @RequestParam("newPasswordRepeat") String newPasswordRepeat) {

        // Check if new password and new password repeat are equals
        // If not, return error
        if (!newPassword.equals(newPasswordRepeat)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Message", "New passwords are different"));
        }

        Boolean res = service.changePassword(id, oldPassword, newPassword);

        // Check if password was changed
        if (res == null) {
            // No user problem
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Message", "No user found with 'id' = " + id));
        } else if (!res) {
            // Old password problem
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Message", "Wrong old password"));
        } else {
            // Everything ok
            return ResponseEntity.ok().body("");
        }
    }

    /**
     * Generate temp code for pasword changing
     * Temp code expires in 10 minutes
     * @param email email of user
     * @return response entity
     */
    @PostMapping("/reset")
    public ResponseEntity resetPassword(@RequestParam(required = false) String email) {
        if (email == null || email.equals("")) {
            // Wrong email
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Message", "Empty email"));
        }
        boolean result = service.generateTempPassword(email);
        if (!result) {
            // User not found
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Message", "User not found"));
        } else {
            // Everything ok
            return ResponseEntity.ok().body("");
        }
    }

    /**
     * Ser new password by temp code
     * @param email email of user
     * @param code code
     * @param newPassword new password
     * @param repeatPassword new password repeat
     * @return response entity
     */
    @PostMapping("/password")
    public ResponseEntity changePassword(@RequestParam(required = false) String email,
                                         @RequestParam(required = false) String code,
                                         @RequestParam(required = false) String newPassword,
                                         @RequestParam(required = false) String repeatPassword) {

        // Input params error
        if (email == null || email.equals("") ||
                code == null || code.equals("") ||
                newPassword == null || newPassword.equals("") ||
                repeatPassword == null || repeatPassword.equals("")) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Message", "All fields are required"));
        }

        // Check if new password and new password repeat are equals
        // If not, return error
        if (!newPassword.equals(repeatPassword)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Message", "Passwords are different"));
        }
        Boolean res = service.changePasswordWithCode(email, code, newPassword);

        if (res == null) {
            // User not found error
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Message", "No user found"));
        } else if (!res) {
            // Wrong code problem
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Message", "Wrong code"));
        } else {
            // OK
            return ResponseEntity.ok().body("");
        }
    }

    /**
     * Load 'me'
     * @param principal principal to detect user
     * @return reponse neityt with user
     */
    @GetMapping("/me")
    @Secured("ROLE_USER")
    public ResponseEntity getMe(Principal principal) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        return ResponseEntity.ok().body(new UserDto(user));
    }
}
