package com.onlineshop.controller;

import com.onlineshop.dao.ProductDAO;
import com.onlineshop.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {

    @Autowired
    private ProductDAO productDAO;

    @GetMapping("/productDetail")
    public String showProductDetail(@RequestParam("productId") Integer productId, Model model) {
        Product product = productDAO.getProductById(productId);
        model.addAttribute("product", product);
        return "productDetail";
    }
}
