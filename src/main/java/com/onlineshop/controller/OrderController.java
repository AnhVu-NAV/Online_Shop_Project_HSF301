package com.onlineshop.controller;

import com.onlineshop.dao.OrderDAO;
import com.onlineshop.dao.OrderDetailDAO;
import com.onlineshop.entity.Order;
import com.onlineshop.entity.OrderDetail;
import com.onlineshop.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private OrderDetailDAO orderDetailDAO;

    @GetMapping("/myOrders")
    public String viewMyOrders(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null && user.getRole().getId() == 1) {
            List<Order> orders = orderDAO.getOrdersByUserId(user.getId());
            model.addAttribute("orders", orders);
            return "customerOrders";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/orderDetail")
    public String viewOrderDetail(@RequestParam("orderId") int orderId, Model model) {
        Order order = orderDAO.getOrdersById(orderId);
        if (order == null) {
            model.addAttribute("error", "Order not found.");
            return "redirect:/myOrders";
        }
        List<OrderDetail> orderDetails = orderDetailDAO.getOrderDetailsByOrderId(orderId);
        model.addAttribute("order", order);
        model.addAttribute("orderDetails", orderDetails);
        return "orderDetail";
    }

    @GetMapping("/cancelOrder")
    public String cancelOrder(@RequestParam("orderId") int orderId) {
        orderDAO.deleteOrderById(orderId);
        return "redirect:/myOrders";
    }
}