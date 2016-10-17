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
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return repository.findByEmail(email);
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
        User user = repository.findByEmail(email);

        if (user == null) {
            return false;
        }

        String tmpPass = RandomStringUtils.random(8, true, true);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setTmpPassword(passwordEncoder.encode(tmpPass));
        EmailHelper.Send(user.getEmail(), "Reset password", "Code: " + tmpPass + "\nUse this code to change password or ignore it.");
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
