package com.onlineshop.controller;

import com.onlineshop.dao.BrandDAO;
import com.onlineshop.dao.OrderDAO;
import com.onlineshop.dao.ProductDAO;
import com.onlineshop.entity.Brand;
import com.onlineshop.entity.Order;
import com.onlineshop.entity.Product;
import com.onlineshop.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    private BrandDAO brandDAO;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private OrderDAO orderDAO;

    @GetMapping("/customer")
    public String handleRequest(@RequestParam(required = false) String service,
                                @RequestParam(required = false) String keywords,
                                @RequestParam(required = false) String sortBy,
                                @RequestParam(required = false) String filterByPrice,
                                @RequestParam(required = false) String filterByBrand,
                                @RequestParam(required = false) Integer productId,
                                Model model,
                                HttpSession session) {

        if (session.getAttribute("numberProductsInCart") == null) {
            session.setAttribute("numberProductsInCart", 0);
        }

        List<Brand> brands = brandDAO.getAll();
        model.addAttribute("allBrands", brands);

        if (service == null) {
            service = "listAllProducts";
        }

        switch (service) {
            case "listAllProducts":
                List<Product> products = productDAO.getAll();
                model.addAttribute("allProducts", products);
                return "index";

            case "viewOrders":
                User user = (User) session.getAttribute("user");
                if (user != null) {
                    List<Order> orders = orderDAO.getOrdersByUserId(user.getId());
                    model.addAttribute("orders", orders);
                }
                return "orderHistory";

            case "viewProductDetail":
                if (productId != null) {
                    Product product = productDAO.getProductById(productId);
                    model.addAttribute("product", product);
                }
                return "productDetail";

            case "searchByKeywords":
                if (keywords == null) {
                    keywords = "";
                }
                if (filterByPrice == null) {
                    filterByPrice = "price-all";
                }
                if (filterByBrand == null) {
                    filterByBrand = "brand-all";
                }

                List<Product> productsFiltered = productDAO.getProductsByKeywords(keywords);
                productsFiltered = productDAO.filterByPrice(filterByPrice, productsFiltered);
                productsFiltered = productDAO.filterByBrand(filterByBrand, productsFiltered);

                model.addAttribute("keywords", keywords);
                model.addAttribute("filterByPrice", filterByPrice);
                model.addAttribute("filterByBrand", filterByBrand);

                if (sortBy != null && !sortBy.equals("unsorted")) {
                    productsFiltered = productDAO.sortProducts(productsFiltered, sortBy);
                    model.addAttribute("sortBy", sortBy);
                }

                model.addAttribute("allProducts", productsFiltered);
                return "index";

            default:
                return "redirect:/customer";
        }
    }
}
