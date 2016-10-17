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
        User user = repository.findOne(id);

        if (user == null)
            return null;


        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        return true;
    }

    @Override
    public Boolean changePasswordWithCode(String email, String code, String newPassword) {
        User user = repository.findByEmail(email);

        if (user == null)
            return null;

        Date now = new Date();
        if (user.getTmpPasswordExpire().before(now))
            return false;

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(code, user.getTmpPassword())) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setTmpPassword("");
        return true;
    }

    @Override
    public Boolean generateTempPassword(String email) {
        long ONE_MINUTE_IN_MILLIS=60000;//millisecs
        Calendar date = Calendar.getInstance();
        long t= date.getTimeInMillis();
        Date afterAddingTenMins=new Date(t + (10 * ONE_MINUTE_IN_MILLIS));

        User user = repository.findByEmail(email);

        if (user == null) {
            return false;
        }

        String tmpPass = RandomStringUtils.random(8, true, true);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setTmpPassword(passwordEncoder.encode(tmpPass));
        user.setTmpPasswordExpire(afterAddingTenMins);
        EmailHelper.Send(user.getEmail(), "Reset password", "Code: " + tmpPass + "\nUse this code to change password or ignore it.\nPassword expires in 10 minutes ");
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = repository.findByEmail(s);
        if (user == null)
            throw new UsernameNotFoundException("User " + s + " not found");
        return user;
    }
}
