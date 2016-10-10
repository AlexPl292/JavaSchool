package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.business.services.interfaces.UserService;
import com.tsystems.javaschool.db.entities.User;
import com.tsystems.javaschool.db.repository.UserRepository;
import com.tsystems.javaschool.util.EmailHelper;
import com.tsystems.javaschool.util.PassGen;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by alex on 04.10.16.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    public final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public Boolean changePassword(Integer id, String oldPassword, String newPassword) {
        User user = repository.findOne(id);

        if (user == null)
            return null;

        String hashedOld = DigestUtils.sha256Hex(DigestUtils.sha256Hex(oldPassword) + user.getSalt());

        if (!hashedOld.equals(user.getPassword())) {
            return false;
        }

        String salt = new PassGen(8).nextPassword();
        user.setSalt(salt);
        user.setPassword(DigestUtils.sha256Hex(DigestUtils.sha256Hex(newPassword) + salt));
        return true;
    }

    @Override
    public Boolean disablePassword(String email) {
        User user = repository.findByEmail(email);

        if (user == null) {
            return false;
        }

        String tmpPass = new PassGen(8).nextPassword();

        user.setTmpPassword(DigestUtils.sha256Hex(DigestUtils.sha256Hex(tmpPass) + user.getSalt()));
        EmailHelper.Send(user.getEmail(), "Reset password", "Code: "+tmpPass);
        return true;
    }
}
