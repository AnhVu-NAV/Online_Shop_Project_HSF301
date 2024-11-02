package com.onlineshop.controller;

import com.onlineshop.entity.User;
import com.onlineshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
//
@Controller
@RequestMapping("/manageCustomer")
public class ManageCustomerController {

    @Autowired
    private UserService userService;

    @GetMapping("/listAllCustomers")
    public String listAllCustomers(Model model) {
        List<User> customers = userService.getAllCustomers();
        model.addAttribute("manageCustomer", "Yes");
        model.addAttribute("allCustomers", customers);
        return "CustomerManager";
    }

    @GetMapping("/ban")
    public String banCustomer(@RequestParam("id") Integer id) {
        if (id != null) {
            userService.banUser(id);
        }
        return "redirect:/manageCustomer/listAllCustomers";
    }

    @GetMapping("/unban")
    public String unbanCustomer(@RequestParam("id") Integer id) {
        if (id != null) {
            userService.unbanUser(id);
        }
        return "redirect:/manageCustomer/listAllCustomers";
    }

    @GetMapping("/searchByKeywords")
    public String searchCustomers(@RequestParam("keywords") String keywords, Model model) {
        List<User> searchedCustomers = userService.getCustomersByName(keywords);
        model.addAttribute("keywords", keywords);
        model.addAttribute("manageCustomer", "Yes");
        if (searchedCustomers == null || searchedCustomers.isEmpty()) {
            model.addAttribute("notFoundCustomer", "Your keywords do not match with any Customer Name");
            searchedCustomers = userService.getAllCustomers();
        }
        model.addAttribute("allCustomers", searchedCustomers);
        return "redirect:/manageCustomer/listAllCustomers";
    }
}
