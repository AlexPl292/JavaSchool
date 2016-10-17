package com.tsystems.javaschool.business.dto;

import com.tsystems.javaschool.db.entities.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 12.10.16.
 */
public class UserDto {

    private Integer id;

    private String name;

    private String surname;

    private String email;

    private List<String> roles = new ArrayList<>();

    public UserDto() {
    }

    public UserDto(User user) {
        convertToDto(user);
    }

    private void convertToDto(User entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.surname = entity.getSurname();
        this.email = entity.getEmail();
        for (GrantedAuthority auth : entity.getAuthorities()) {
            roles.add(auth.getAuthority());
        }
    }

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

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDto userDto = (UserDto) o;

        if (id != null ? !id.equals(userDto.id) : userDto.id != null) return false;
        if (name != null ? !name.equals(userDto.name) : userDto.name != null) return false;
        if (surname != null ? !surname.equals(userDto.surname) : userDto.surname != null) return false;
        if (email != null ? !email.equals(userDto.email) : userDto.email != null) return false;
        return roles != null ? roles.equals(userDto.roles) : userDto.roles == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (roles != null ? roles.hashCode() : 0);
        return result;
    }
}
