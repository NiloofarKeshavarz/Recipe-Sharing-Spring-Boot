package com.example.eatwhat.model;

import jdk.jfr.Name;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "eatwhat_recipe_category")
@Data
@NoArgsConstructor
public class RecipeCategory {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Name("category_id")
  private long id;

  private String catDescription;
  
  public RecipeCategory(String catDescription) {
    this.catDescription = catDescription;
  }
}
