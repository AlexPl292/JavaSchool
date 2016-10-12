package com.tsystems.javaschool.util;

import com.tsystems.javaschool.business.dto.UserDto;
import com.tsystems.javaschool.business.services.interfaces.UserService;
import com.tsystems.javaschool.db.entities.Staff;
import com.tsystems.javaschool.db.entities.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 04.10.16.
 */
@Component
public class JsAuthenticationProvider implements AuthenticationProvider {

    private final UserService service;

    @Autowired
    JsAuthenticationProvider(UserService service) {
        this.service = service;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
        User user = service.findByEmail(authentication.getName());

        if (user == null)
            return null;

        String password = authentication.getCredentials().toString();
        String hashedPass = DigestUtils.sha256Hex(password);
        hashedPass = DigestUtils.sha256Hex(hashedPass + user.getSalt());
        if (hashedPass.equals(user.getPassword())) {
            grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
            if (user instanceof Staff) {
                grantedAuths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }

            return new UsernamePasswordAuthenticationToken(new UserDto(user), password, grantedAuths);
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass));
    }
}
