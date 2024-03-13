package ru.sber.appointment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User  {

    private Long id;
    private String firstName;
    private String lastName;
    private String surName;
    private String username;
    private String password;
    private String passwordConfirm;
    private String mail;
    private String phone;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
    private Set<Role> roles;
    private Doctor doctor;
    private List<Ticket> tickets = new ArrayList<>();
    private void removeRoles() {
        roles.clear();
    }
    public void setSurName(String surName) {
        this.surName = surName;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public String getSurName() {
        return surName;
    }
    public String getMail() {
        return mail;
    }
    public String getPhone() {
        return phone;
    }
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public boolean isAccountNonExpired() {
        return true;
    }
    public boolean isAccountNonLocked() {
        return true;
    }
    public boolean isCredentialsNonExpired() {
        return true;
    }
    public boolean isEnabled() {
        return true;
    }
    public void setUsername(String police) {
        this.username = police;
    }
    public Collection<?> getAuthorities() {
        return getRoles();
    }
    public String getPassword() {
        return password;
    }
    public String getUsername() {
        return username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPasswordConfirm() {
        return passwordConfirm;
    }
    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
    public Set<Role> getRoles() {
        return roles;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", surName='" + surName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", mail='" + mail + '\'' +
                ", phone='" + phone + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
