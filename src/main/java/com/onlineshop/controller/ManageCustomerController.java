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

@Controller
public class ManageCustomerController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String manageCustomers(@RequestParam(value = "service", required = false) String service,
                                  @RequestParam(value = "id", required = false) Integer id,
                                  @RequestParam(value = "keywords", required = false) String keywords,
                                  Model model) {

        if (service == null) {
            service = "listAllCustomers";
        }

        switch (service) {
            case "listAllCustomers":
                List<User> customers = userService.getAllCustomers();
                model.addAttribute("manageCustomer", "Yes");
                model.addAttribute("allCustomers", customers);
                return "CustomerManager";

            case "ban":
                if (id != null) {
                    userService.banUser(id);
                }
                return "redirect:/manageCustomer?service=listAllCustomers";

            case "unban":
                if (id != null) {
                    userService.unbanUser(id);
                }
                return "redirect:/manageCustomer?service=listAllCustomers";

            case "searchByKeywords":
                List<User> searchedCustomers = userService.getCustomersByName(keywords);
                model.addAttribute("keywords", keywords);
                model.addAttribute("manageCustomer", "Yes");
                if (searchedCustomers == null || searchedCustomers.isEmpty()) {
                    model.addAttribute("notFoundCustomer", "Your keywords do not match with any Customer Name");
                    searchedCustomers = userService.getAllCustomers();
                }
                model.addAttribute("allCustomers", searchedCustomers);
                return "CustomerManager";

            default:
                return "redirect:/manageCustomer?service=listAllCustomers";
        }
    }
    @GetMapping("/listAllCustomers")
    public String listAllCustomers(Model model) {
        // logic
        return "CustomerManager";
    }
    @GetMapping("/ban")
    public String ban(@RequestParam("id") int id) {
        // logic
        return "redirect:/manageCustomer?service=listAllCustomers";
    }
}