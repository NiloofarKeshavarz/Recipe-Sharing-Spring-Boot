package com.example.eatwhat.dao;

import com.example.eatwhat.model.RecipeCategory;
import com.example.eatwhat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RecipeCategoryRepository extends JpaRepository<RecipeCategory, Long> {

}
