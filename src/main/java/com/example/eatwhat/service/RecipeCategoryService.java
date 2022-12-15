package com.example.eatwhat.service;


import com.example.eatwhat.dao.RecipeCategoryRepository;
import com.example.eatwhat.dao.UserRepository;
import com.example.eatwhat.model.RecipeCategory;
import com.example.eatwhat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
//@Transactional
public class RecipeCategoryService {
  
  @Autowired
  private RecipeCategoryRepository repo;
  
  public List<RecipeCategory> getAll() {
    return repo.findAll();
  }
  
  public void save(RecipeCategory recipeCategory) {
    repo.save(recipeCategory);
  }
  
  public RecipeCategory get(long id) {
    return repo.findById(id).get();
  }
  
  public void delete(long id) {
    repo.deleteById(id);
  }
  
  public void initCatList(String category) {
    repo.save(new RecipeCategory(category));
  }
}
