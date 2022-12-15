package com.example.eatwhat.service;


import com.example.eatwhat.dao.UserRepository;
import com.example.eatwhat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService implements UserDetailsService {
  
  @Autowired
  private UserRepository repo;
  
  public List<User> listAll() {
    return repo.findAll();
  }
  
  public void save(User user) {
    repo.save(user);
  }
  
  public User get(long id) {
    return repo.findById(id).get();
  }
  
  public void delete(long id) {
    repo.deleteById(id);
  }
  
  public void addAdmin(String password) {
    repo.save(new User( "admin", "admin@eatwhat.com",password, password, 0, "ROLE_ADMIN,ROLE_USER","password"));
  }
  
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    
    User user = repo.findByUserId(username);
    
    if (user == null) {
      throw new UsernameNotFoundException("User not found");
    }
    return repo.findByUserId(user.getUsername());
  }
}
