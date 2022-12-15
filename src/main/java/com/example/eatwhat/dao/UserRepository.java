package com.example.eatwhat.dao;

import com.example.eatwhat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
  
  @Query("SELECT u FROM User u WHERE u.username = ?1")
  public User findByUserId(String username);
}
