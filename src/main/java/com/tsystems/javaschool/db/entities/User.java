package com.tsystems.javaschool.db.entities;

import com.google.gson.annotations.Expose;
import com.tsystems.javaschool.util.EMU;
import com.tsystems.javaschool.util.PassGen;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import javax.persistence.*;

/**
 * Created by alex on 10.09.16.
 *
 * Define a default user
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class User {
    private static final Logger logger = Logger.getLogger(User.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    private Integer id;

    @Basic
    @Expose
    private String name;

    @Basic
    @Expose
    private String surname;

    @Basic
    @Expose
    private String email;

    @Basic
    @Expose
    private String password;

    @Basic
    @Expose
    private String salt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (surname != null ? !surname.equals(user.surname) : user.surname != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        return salt != null ? salt.equals(user.salt) : user.salt == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (salt != null ? salt.hashCode() : 0);
        return result;
    }

    /**
     * Log in new user by email and password
     * @param email email of user
     * @param password password of user
     * @return logged user if positive, null if negative
     */
    public static User login(String email, String password) {

        if (email != null && password != null) {
            User user = EMU.getEntityManager().createQuery("SELECT u FROM User u WHERE u.email = :first", User.class)
                    .setParameter("first", email)
                    .getSingleResult();
            if (user != null) {
                String passwordHash = DigestUtils.sha256Hex(password);
                passwordHash = DigestUtils.sha256Hex(passwordHash + user.getSalt());
                if (passwordHash.equals(user.getPassword())) {
                    EMU.closeEntityManager();
                    logger.info(email+": positive login");
                    return user;
                }
            }
        }
        EMU.closeEntityManager();
        logger.info(email+": negative login");
        return null;
    }

    /**
     * Update password of user
     * @param id id of user
     * @param oldPassword old password to change
     * @param newPassword new password
     * @return "Success!" if positive, error message if negative
     */
    public static String updatePassword(Integer id, String oldPassword, String newPassword) {
        try {
            User user = EMU.getEntityManager().find(User.class, id);

            String hashed = DigestUtils.sha256Hex(oldPassword);
            String usedSalt = user.getSalt();
            String usedPassword = DigestUtils.sha256Hex(hashed + usedSalt);

            if (usedPassword.equals(user.getPassword())) {
                String newHashedPassword = DigestUtils.sha256Hex(newPassword);
                String newSalt = new PassGen(8).nextPassword();
                EMU.beginTransaction();
                user.setPassword(DigestUtils.sha256Hex(newHashedPassword + newSalt));
                user.setSalt(newSalt);
                EMU.commit();
                logger.info("User with id = "+id+": password change!");
                return "Success!";
            } else {
                logger.info("User with id = "+id+": try to change password. Wrong old password!");
                return "You entered wrong current password";
            }
        } catch (RuntimeException re) {
            if (EMU.getEntityManager() != null && EMU.getEntityManager().isOpen())
                EMU.rollback();
            logger.error("User with id = "+id+": try to change password. Exception!", re);
            return "Error while transaction!";
        } finally {
            EMU.closeEntityManager();
        }
    }
}
