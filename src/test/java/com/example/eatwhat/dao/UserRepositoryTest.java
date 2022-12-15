package com.example.eatwhat.dao;

import com.example.eatwhat.model.Recipe;
import com.example.eatwhat.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.util.DateUtil.now;


@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    void joinFKTest() {

        givenRecipe();

        User result = recipeRepository.findById(1l)
                .orElseThrow(RuntimeException::new)
                .getUser();

        System.out.println("<<<<Result >>>>>>\n" + result);

    }

    @Test
    private User givenUser() {
        User user = new User();
        user.setUsername("user1");
        user.setUserEmail("user@mail.com");
        user.setUserPassword("password");
        user.setUserPoint(5);

        return userRepository.save(user);
    }

    @Test
    private void givenRecipe() {
        Recipe recipe = new Recipe();
        recipe.setRecipeTitle("recipeTitle");
        recipe.setRecipeDescription("recipeDescription");
        recipe.setUser(givenUser());
        recipeRepository.save(recipe);

    }

}