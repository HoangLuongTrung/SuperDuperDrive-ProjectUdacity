package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignUpController {
    private final UserService userService;

    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String signup() {
        return "signup";
    }

    @PostMapping
    public String signUpUser(@ModelAttribute User user, Model model) {
        String error = null;
        /*
         * Check Username is Exist
         */
        if (userService.isUserExist(user.getUsername())) {
            error = "Username is exist, please select another Username!!";
        }

        if (error == null) {
            int data = userService.createNewUser(user);
            model.addAttribute("signUpSuccess", true);
            return "redirect:/login";
        } else {
            model.addAttribute("signUpFail", error);
        }

        return "signup";
    }
}
