package com.example.eatwhat.controller;

import com.example.eatwhat.model.Recipe;
import com.example.eatwhat.model.RecipeCategory;
import com.example.eatwhat.model.User;
import com.example.eatwhat.service.RecipeCategoryService;
import com.example.eatwhat.service.RecipeService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@NoArgsConstructor
@RequestMapping(value = "/recipe")

public class RecipeController {

    @Autowired
    private RecipeService recipeService;
    @Autowired
    private RecipeCategoryService recipeCategoryService;

    public void initList(Model model) {
        List<Recipe> listRecipes = recipeService.listAll(); // Need to change with inner join
        model.addAttribute("listRecipes", listRecipes);
    }

    //recipe Search list
    @RequestMapping("/list")
    public String searchList(Model model, @Param("keyword") String keyword) {
        List<Recipe> listRecipes = recipeService.listAllByKeyword(keyword);
        model.addAttribute("listRecipes", listRecipes);
        model.addAttribute("keyword", keyword);
        return "/recipe/index";
    }
    @GetMapping({"", "/"})
    public String index(Model model) {

        initList(model);
        return "/recipe/index";
    }

    @RequestMapping(value = "/register/save", method = RequestMethod.POST)
    public String save(@Valid @ModelAttribute("recipe") Recipe recipe,
                       BindingResult bindingResult,
                       @ModelAttribute("recipeCategories") RecipeCategory recipeCat,
                       Model model) throws IOException {

        // join Recipe category to the recipe
        List<RecipeCategory> recipeCategories = recipeCategoryService.getAll();
        recipeCategories.forEach(item -> {
            if ((recipeCat.getId()) == (item.getId())) {
                recipe.recipeCategory = item;
            }
        });

        if (bindingResult.hasErrors()) {
            model.addAttribute("recipe", recipe);
            model.addAttribute("recipeCategories", recipeCategories);
            return "/recipe/newRecipe";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        recipe.setUser(user);// insert user information


        System.out.println("saving a new recipe in db");
        recipeService.save(recipe);

        return "redirect:/user";
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") long id, Model model) {
        Recipe recipe = recipeService.get(id);
        model.addAttribute("recipe", recipe);

        List<RecipeCategory> recipeCategories = recipeCategoryService.getAll();
        model.addAttribute("recipeCategories", recipeCategories);
        return "/recipe/edit";
    }

    @RequestMapping(value = "/saveEditedRecipe", method = RequestMethod.POST)
    public String saveEditedRecipe(@Valid @ModelAttribute("recipe") Recipe recipe,
                                   BindingResult bindingResult,
                                   @ModelAttribute("recipeCategories") RecipeCategory recipeCat,
                                   Model model)  throws IOException
    {
        System.out.println("before throws for errors");
        List<RecipeCategory> recipeCategories = recipeCategoryService.getAll();

        if (bindingResult.hasErrors()) {
            model.addAttribute("recipe", recipe);
            System.out.println("after throws for errors");
            model.addAttribute("recipeCategories", recipeCategories);

            return "/recipe/edit";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        recipe.setUser(user);// insert user information

        System.out.println("saving modified recipe in db : " + recipe);
        recipeService.save(recipe);

        return "redirect:/user";
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") long id) {
        recipeService.delete(id);

        return "redirect:/user";
    }

    @GetMapping("/showCreateRecipe")
    public String showCreateRecipe(Model model) {
        List<RecipeCategory> recipeCategories = recipeCategoryService.getAll();
        model.addAttribute("recipeCategories", recipeCategories);
        Recipe recipe = new Recipe();
        System.out.println(recipe);
        model.addAttribute("recipe", recipe);
        return "/recipe/newRecipe";
    }
}

