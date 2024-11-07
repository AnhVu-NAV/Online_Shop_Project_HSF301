package com.onlineshop.controller;

import com.onlineshop.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class
AdminController {

    @GetMapping("/admin")
    public String homePage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if(user.getRole().getId()==1){
            return "redirect:/customer";
        }
        return "adminHome";
    }
}
