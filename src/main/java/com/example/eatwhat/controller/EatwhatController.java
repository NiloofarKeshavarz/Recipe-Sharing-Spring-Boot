package com.example.eatwhat.controller;

import com.example.eatwhat.dao.UserRepository;
import com.example.eatwhat.model.User;
import com.example.eatwhat.service.RecipeCategoryService;
import com.example.eatwhat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class EatwhatController {

    private String loginErrorSite;

    @Autowired
    RecipeCategoryService recipeCategoryService;
    
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    private RequestCache requestCache = new HttpSessionRequestCache();
    
    @GetMapping("/")
    public String home() {
        if (recipeCategoryService.getAll().size() == 0) {
            List<String> catList = Arrays.asList("Italy", "France", "Korea", "Japan", "China", "Mexico");
            catList.forEach(cat -> recipeCategoryService.initCatList(cat));
        }
        
        if (userService.listAll().size() == 0) {
            System.out.println("Make Admin");
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode("admin");
            userService.addAdmin(encodedPassword);
        }
        
        return "index";
    }

    @GetMapping("/home")
    public String recipeBoard(@RequestParam String site, Model model) {
        System.out.println("HOME Recipe Board");
        model.addAttribute("site", site);
        return "redirect:/" + site;
    }

    @GetMapping("/login")
    public String login(
            @AuthenticationPrincipal User user,
            @RequestParam(value = "site", required = false) String site,
            @RequestParam(value = "error", defaultValue = "false") Boolean error,
            HttpServletRequest request,
            Model model
    ) {
        if (user != null && user.isEnabled()) {
            if (user.getAuthorities().contains("ADMIN")) {
                return "redirect:/manager";
            } else if (user.getAuthorities().contains("USER")) {
                return "redirect:/user";
            }
        }
        if (site == null) {
            SavedRequest savedRequest = requestCache.getRequest(request, null);
            if (savedRequest != null) {
                site = estimateSite(savedRequest.getRedirectUrl());
            }
        }
        model.addAttribute("error", error);
        model.addAttribute("site", site);

        return "loginForm";
    }

    private String estimateSite(String referer) {
        if(referer == null)
            return "index.html";
        try {
            URL url = new URL(referer);
            String path = url.getPath();
            if(path != null){
                if(path.startsWith("/user")) return "user";
                if(path.startsWith("/recipe")) return "recipe";
                if(path.startsWith("/manager")) return "manager";
            }
            String query = url.getQuery();
            if(query != null){
                if(path.startsWith("/site=user")) return "user";
                if(path.startsWith("/site=recipe")) return "recipe";
                if(path.startsWith("/site=manager")) return "manager";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return "recipe.html";
    }

    @PostMapping("/login")
    public String loginPost(@RequestParam String site, Model model) {
        System.out.println("login page : " + site);
        model.addAttribute("site", site);
        return "redirect:/" + site;
    }

    @GetMapping("/login/error")
    public String loginError(Model model) {
        System.out.println("Login ERROR site : " + loginErrorSite);
        model.addAttribute("loginError", true);
        model.addAttribute("site", loginErrorSite);
        return "loginForm";
    }

    @GetMapping("/signup")
    public String signUp(
            @RequestParam String site,
            HttpServletRequest request
    ){
        if(site == null) {
            site = estimateSite(request.getParameter("referer"));
        }
        return "redirect:/"+site+"/signup";
    }

    @GetMapping("/register")
    public String signUp(Model model){
        System.out.println("This is user signup method");
        User user = new User();
        model.addAttribute("user", user);

//        List<String> roleList = Arrays.asList("User", "Admin");
//        model.addAttribute("roleList", roleList);
        return "/signup";
    }
    @RequestMapping(value = "/register/save", method = RequestMethod.POST)
    public String saveUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
    
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "/signup";
        }

        //for preventing duplicate id
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUserId(user.getUsername()));
        if(optionalUser.isPresent()){
            bindingResult.rejectValue("username", "error.username", "You cannot use this username!, It's already used");
            return "/signup";
        }

        if(!user.getTempPassword().equals(user.getConfirmPassword())){
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "Please match your password");
            return "/signup";
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getTempPassword());
        user.setUserPassword(encodedPassword);


        System.out.println(user.getAuth());
        if(user.getAuth().equals("Admin")){
            user.setAuth("ROLE_ADMIN,ROLE_USER");
        }else{
            user.setAuth("ROLE_USER");
        }

        userService.save(user);

        return "redirect:/login?site=user";
    }


    @GetMapping("/login/access-denied")
    public String accessDenied(){
        return "/access_denied";
    }
}