package com.onlineshop.controller;

import com.onlineshop.dao.BrandDAO;
import com.onlineshop.dao.OrderDAO;
import com.onlineshop.dao.ProductDAO;
import com.onlineshop.entity.*;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;
import java.util.stream.Collectors;

@Controller
public class CustomerController {

    @Autowired
    private BrandDAO brandDAO;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private OrderDAO orderDAO;

    @GetMapping("/customer")
    public String handleRequest(@RequestParam(name = "service", required = false) String service,
                                @RequestParam(name = "keywords", required = false) String keywords,
                                @RequestParam(name = "sortBy", required = false) String sortBy,
                                @RequestParam(name = "filterByPrice", required = false) String filterByPrice,
                                @RequestParam(name = "filterByBrand", required = false) String filterByBrand,
                                @RequestParam(name = "productId", required = false) Integer productId,
                                Model model,
                                HttpSession session) {

        // Initialize cart if not present
        if (session.getAttribute("cart") == null) {
            session.setAttribute("cart", new HashMap<Integer, CartItem>());
        }

        // Add user to model if logged in
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }

        // Get all brands for the sidebar
        List<Brand> brands = brandDAO.getAll();
        model.addAttribute("allBrands", brands);

        if (service == null) {
            service = "listAllProducts";
        }

        switch (service) {
            case "listAllProducts":
                List<Product> products = productDAO.getAll();
                model.addAttribute("allProducts", products);
                break;

            case "viewOrders":
                if (user != null) {
                    List<Order> orders = orderDAO.getOrdersByUserId(user.getId());
                    model.addAttribute("orders", orders);
                    return "orderHistory";
                } else {
                    return "redirect:/login";
                }

            case "viewProductDetail":
                if (productId != null) {
                    Product product = productDAO.getProductById(productId);
                    if (product != null) {
                        model.addAttribute("product", product);
                        return "productDetail";
                    } else {
                        model.addAttribute("error", "Product not found");
                        return "redirect:/customer";
                    }
                }
                break;

            case "searchByKeywords":
            case "filter":
            case "sort":
                keywords = (keywords == null) ? "" : keywords;
                filterByPrice = (filterByPrice == null) ? "price-all" : filterByPrice;
                filterByBrand = (filterByBrand == null) ? "brand-all" : filterByBrand;

                List<Product> productsFiltered = productDAO.getProductsByKeywords(keywords);
                productsFiltered = productDAO.filterByPrice(filterByPrice, productsFiltered);
                productsFiltered = productDAO.filterByBrand(filterByBrand, productsFiltered);

                if (sortBy != null && !sortBy.equals("unsorted")) {
                    productsFiltered = productDAO.sortProducts(productsFiltered, sortBy);
                }

                model.addAttribute("allProducts", productsFiltered);
                model.addAttribute("keywords", keywords);
                model.addAttribute("filterByPrice", filterByPrice);
                model.addAttribute("filterByBrand", filterByBrand);
                model.addAttribute("sortBy", sortBy);
                break;

            default:
                return "redirect:/customer";
        }

        // Add common attributes
        model.addAttribute("service", service);

        // Return the main Thymeleaf template
        return "index";
    }

    @GetMapping("/customerOrders")
    public String viewOrders(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<Order> orders = orderDAO.getOrdersByUserId(user.getId());
        model.addAttribute("orders", orders);
        return "myOrders"; // Trang hiển thị danh sách đơn hàng
    }

    @GetMapping("/orderDetails")
    public String viewOrderDetails(@RequestParam("orderId") Integer orderId, Model model) {
//        Order order = orderDAO.getOrdersById(orderId);
        // Lấy order cùng với orderDetails
        Order order = orderDAO.getOrderByIdWithDetails(orderId);
        if (order == null) {
            model.addAttribute("error", "Order not found.");
            return "redirect:/customerOrders";
        }
        if (order.getTotalAmount() == null) {
            order.setTotalAmount(0.0); // Gán giá trị mặc định
        }

        List<OrderDetail> orderDetails = order.getOrderDetails();
        // Khởi tạo thủ công collection `orderDetails`
//        Hibernate.initialize(order.getOrderDetails());
        model.addAttribute("order", order);
        model.addAttribute("orderDetails", order.getOrderDetails());
        return "orderDetails"; // Trang hiển thị chi tiết đơn hàng
    }
}
