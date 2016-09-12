package com.tsystems.javaschool.db.entities;

import com.google.gson.annotations.Expose;
import com.tsystems.javaschool.util.EMU;
import com.tsystems.javaschool.util.PassGen;
import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.*;

/**
 * Created by alex on 10.09.16.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "id", nullable = false)
    @Expose
    private int id;

    @Basic
//    @Column(name = "name", nullable = true, length = 45)
    @Expose
    private String name;

    @Basic
//    @Column(name = "surname", nullable = true, length = 45)
    @Expose
    private String surname;

    @Basic
//    @Column(name = "email", nullable = true, length = 45)
    @Expose
    private String email;

    @Basic
//    @Column(name = "password", nullable = true, length = 64)
    @Expose
    private String password;

    @Basic
//    @Column(name = "salt", nullable = false, length = 45)
    @Expose
    private String salt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

        User that = (User) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (surname != null ? !surname.equals(that.surname) : that.surname != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        return salt != null ? salt.equals(that.salt) : that.salt == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (salt != null ? salt.hashCode() : 0);
        return result;
    }

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
                    return user;
                }
            }
        }
        EMU.closeEntityManager();
        return null;
    }

    public static String updatePassword(Integer id, String oldPassword, String newPassword) {
        try {
            User user = EMU.getEntityManager().find(User.class, id);

            String hashed = DigestUtils.sha256Hex(oldPassword);
            String usedSalt = user.getSalt();
            String usedPassword = DigestUtils.sha256Hex(hashed + usedSalt);

            if (usedPassword.equals(user.getPassword())) {
                newPassword = DigestUtils.sha256Hex(newPassword);
                String newSalt = new PassGen(8).nextPassword();
                EMU.beginTransaction();
                user.setPassword(DigestUtils.sha256Hex(newPassword + newSalt));
                user.setSalt(newSalt);
                EMU.commit();
                return "Success!";
            } else {
                return "You entered wrong current password";
            }
        } catch (RuntimeException re) {
            if (EMU.getEntityManager() != null && EMU.getEntityManager().isOpen())
                EMU.rollback();
            return "Error while transaction!";
        } finally {
            EMU.closeEntityManager();
        }
    }
}
