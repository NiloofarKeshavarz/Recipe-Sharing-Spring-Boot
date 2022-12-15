package com.example.eatwhat.model;

import jdk.jfr.Name;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "eatwhat_recipe")
@Data
@NoArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Name("recipe_id")
    private long id;


    @Size(min = 2, max = 30, message = "Title between 2 to 30 characters")
    private String recipeTitle;

    @Size(min = 5, max = 150, message = "Description between 5 to 150 charactersnk")
    private String recipeDescription;

    private int recipePoint;

    @NotBlank(message = "Cooking time needed")
    private String cookingTime;
    
    //@Column(nullable = true)
    @URL(message = "URL format is necessary http://example.com")
    @NotBlank(message = "URL format is necessary http://example.com")
    private String photos;
    
    @ManyToOne
    private User user;

    @ManyToOne
//    @ManyToOne(targetEntity = RecipeCategory.class, cascade = CascadeType.ALL,fetch= FetchType.EAGER)
//  @JoinColumn(name="category_id", nullable=false)
    public RecipeCategory recipeCategory;


    @Transient
    public String getPhotosImagePath() {
        if (photos == null || id == 0) return null;

        return "/recipe-photos/" + id + "/" + photos;
    }

}
