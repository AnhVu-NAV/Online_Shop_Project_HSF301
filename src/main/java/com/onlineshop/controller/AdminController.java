package com.onlineshop.controller;

import com.onlineshop.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String showAdminPage(HttpSession session, Model model) {
//        User user = (User) session.getAttribute("user");
//        if (user == null || user.getRole().getId() == 1) {
//            return "redirect:/accessDenied";
//        } else {
//            model.addAttribute("user", user);
            return "adminHome";
//        }
    }

    @PostMapping("/admin")
    public String handleAdminPost(HttpSession session, Model model) {
//        User user = (User) session.getAttribute("user");
//        if (user == null || user.getRole().getId() == 1) {
//            return "redirect:/accessDenied";
//        } else {
//            model.addAttribute("user", user);
            return "adminHome";
//        }
    }
}
