package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.business.services.interfaces.UserService;
import com.tsystems.javaschool.db.entities.User;
import com.tsystems.javaschool.db.repository.UserRepository;
import com.tsystems.javaschool.util.EmailHelper;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by alex on 04.10.16.
 *
 * User service implementation
 */
@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    public final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Boolean changePassword(Integer id, String oldPassword, String newPassword) {
        // Search for user
        User user = repository.findOne(id);

        if (user == null)
            return null;

        // Encrypt password with BCrypt
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        return true;
    }

    @Override
    public Boolean changePasswordWithCode(String email, String code, String newPassword) {
        // Search for user
        User user = repository.findByEmail(email);

        if (user == null)
            return null;

        // Check if temp password is not expired
        Date now = new Date();
        if (user.getTmpPasswordExpire().before(now))
            return false;

        // Check temp pasword
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(code, user.getTmpPassword())) {
            return false;
        }

        // Encode and set new password
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setTmpPassword("");
        return true;
    }

    @Override
    public Boolean generateTempPassword(String email) {

        // Set expiration date. Now + 10 minutes
        long ONE_MINUTE_IN_MILLIS=60000;//millisecs
        Calendar date = Calendar.getInstance();
        long t= date.getTimeInMillis();
        Date afterAddingTenMins=new Date(t + (10 * ONE_MINUTE_IN_MILLIS));

        // Search for user
        User user = repository.findByEmail(email);

        if (user == null) {
            return false;
        }

        // Generate temp code
        String tmpPass = RandomStringUtils.random(8, true, true);

        // Encrype tmp code with BCrypt
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setTmpPassword(passwordEncoder.encode(tmpPass));
        user.setTmpPasswordExpire(afterAddingTenMins);

        // Send email about new code
        EmailHelper.Send(user.getEmail(), "Reset password", "Code: " + tmpPass + "\nUse this code to change password or ignore it.\nPassword expires in 10 minutes ");

        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // User by login
        User user = repository.findByEmail(s);
        if (user == null)
            throw new UsernameNotFoundException("User " + s + " not found");
        return user;
    }
}
