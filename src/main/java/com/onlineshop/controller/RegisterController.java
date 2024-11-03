package com.onlineshop.controller;
import com.onlineshop.entity.User;
import com.onlineshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
public class RegisterController {
    @Autowired
    private UserService userService;
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }
    @PostMapping("/register")
    public String register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("fullname") String fullname,
                           @RequestParam("email") String email,
                           @RequestParam("phone") String phone,
                           @RequestParam("address") String address,
                           Model model) {
        if (userService.isUsernameTaken(username)) {
            model.addAttribute("duplicateUsername", "Username already exists");
            return "register";
        }
        User user = new User(username, password, fullname, email, phone, address, null);
        userService.insertUser(user);
        model.addAttribute("registerSuccess", "Register successful");
        return "register";
    }
}