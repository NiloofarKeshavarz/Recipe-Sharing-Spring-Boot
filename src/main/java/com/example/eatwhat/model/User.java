package com.example.eatwhat.model;

import jdk.jfr.Name;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "eatwhat_user")
@Data
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Name("user_id")
    private long id;

    @NotBlank(message = "User name must not be empty")
    @Size(min = 3, max = 10, message = "User name must be between 3 and 10 characters")
    private String username;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email must not be empty")
    private String userEmail;


    @Transient // db does not save
    private String confirmPassword;



//    @NotBlank(message = "Please fill out the field")

    @Transient // db does not save
    @Pattern(regexp="[a-zA-Z1-9]{6,12}", message = "Please enter your password within 8 to 12, Only alphanumeric characters are allowed")
    private String tempPassword;

    @Column(name="password", length =500)
    private String userPassword;



    private int userPoint;

    private String auth;

    public User(String username, String userEmail,String userPassword, String confirmPassword, int userPoint, String auth,String tempPassword) {
        this.username = username;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.confirmPassword = confirmPassword;
        this.userPoint = userPoint;
        this.auth = auth;
        this.tempPassword   = tempPassword;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> roles = new HashSet<>();
        for (String role : auth.split(",")) {
            roles.add(new SimpleGrantedAuthority(role));
        }
        return roles;
    }

    public long getUserId() {
        return id;
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
