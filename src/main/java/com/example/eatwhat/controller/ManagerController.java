package com.example.eatwhat.controller;

import com.example.eatwhat.model.Recipe;
import com.example.eatwhat.model.User;
import com.example.eatwhat.service.RecipeService;
import com.example.eatwhat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "/manager")
public class ManagerController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;

    public void initList(Model model) {

        List<Recipe> listRecipes = recipeService.listAll();
        model.addAttribute("listRecipes", listRecipes);

        List<User> listUsers = userService.listAll();
        model.addAttribute("listUsers", listUsers);
    }

    @GetMapping({"", "/"})
    public String index(Model model) {
        System.out.println("manager index : ");
        initList(model);
        return "/manager/index";
    }
    @RequestMapping("/delete/user/{id}")
    public String deleteUser(@PathVariable(name = "id") long id) {
        userService.delete(id);
        return "redirect:/manager";
    }

    @RequestMapping("/delete/recipe/{id}")
    public String deleteRecipe(@PathVariable(name = "id") long id) {
        recipeService.delete(id);
        return "redirect:/manager";
    }
}
