package com.onlineshop.controller;

import com.onlineshop.entity.User;
import com.onlineshop.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session,
                        Model model) {
        User user = userService.getUserByUsernameAndPassword(username, password);
        if (user == null) {
            model.addAttribute("invalidUser", "Username or Password is invalid");
            return "login";
        }

        if (Boolean.TRUE.equals(user.getBanned())) {
            model.addAttribute("error", "Your account has been banned.");
            session.invalidate();
            return "login"; // Hoặc có thể chuyển hướng tới một trang thông báo cụ thể
        }
        System.out.println(user.toString());

        session.setAttribute("user", user);
        session.setAttribute("fullname", user.getFullName());

        if (user.getRole().getId() == 0) {
            return "redirect:/admin";
        } else {
            return "redirect:/customer";
        }
    }
}
