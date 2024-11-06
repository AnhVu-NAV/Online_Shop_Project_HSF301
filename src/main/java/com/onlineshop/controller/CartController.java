package com.onlineshop.controller;

import com.onlineshop.dao.BillDAO;
import com.onlineshop.dao.OrderDAO;
import com.onlineshop.dao.OrderDetailDAO;
import com.onlineshop.dao.ProductDAO;
import com.onlineshop.entity.BillDetail;
import com.onlineshop.entity.CartItem;
import com.onlineshop.entity.Order;
import com.onlineshop.entity.Product;
import com.onlineshop.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

import java.sql.Date;
import java.util.*;

@Controller
public class CartController {

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private OrderDetailDAO orderDetailDAO;

    @Autowired
    private BillDAO billDAO;

    @GetMapping("/cart")
    public String showCart(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        return "cart"; // Replace "cart" with the actual cart view template name.
    }

    @GetMapping("/cart/add")
    public String addToCart(@RequestParam("productId") int productId, HttpSession session) {
        // Retrieve product from database
        Product product = productDAO.getProductById(productId);
        if (product == null) {
            return "redirect:/customer";
        }

        // Retrieve cart from session or create if doesn't exist
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        // Check if product already exists in cart
        boolean found = false;
        for (CartItem item : cart) {
            if (item.getProduct().getId() == productId) {
                item.setQuantity(item.getQuantity() + 1);
                found = true;
                break;
            }
        }

        // If not found, add new CartItem
        if (!found) {
            cart.add(new CartItem(product, 1));
        }

        // Save updated cart back to session
        session.setAttribute("cart", cart);

        return "redirect:/customer";
    }


    @GetMapping("/cart/remove")
    public String removeItem(@RequestParam("id") String id, HttpSession session) {
        session.removeAttribute(id);
        return "redirect:/cart";
    }

    @GetMapping("/cart/clear")
    public String removeAllItems(HttpSession session) {
        Enumeration<String> en = session.getAttributeNames();
        while (en.hasMoreElements()) {
            String attributeName = en.nextElement();
            if (!attributeName.equals("user") && !attributeName.equals("fullname") && !attributeName.equals("numberProductsInCart")) {
                session.removeAttribute(attributeName);
            }
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/update")
    public String updateCart(HttpSession session, @RequestParam Map<String, String> params) {
        Enumeration<String> en = session.getAttributeNames();
        while (en.hasMoreElements()) {
            String attributeName = en.nextElement();
            if (!attributeName.equals("user") && !attributeName.equals("fullname") && !attributeName.equals("numberProductsInCart")) {
                String paramKey = "p" + attributeName;
                if (params.containsKey(paramKey)) {
                    int quantity = Integer.parseInt(params.get(paramKey));
                    CartItem cartItem = (CartItem) session.getAttribute(attributeName);
                    if (cartItem != null) {
                        cartItem.setQuantity(quantity);
                        session.setAttribute(attributeName, cartItem);
                    }
                }
            }
        }
        return "redirect:/cart";
    }

    @GetMapping("/cart/checkout")
    public String checkout(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        java.util.Date date = new java.util.Date();
        Date currentDate = new Date(date.getTime());
        Order order = new Order(currentDate, user);
        int orderId = orderDAO.insert(order, user);
        Enumeration<String> en = session.getAttributeNames();
        while (en.hasMoreElements()) {
            String attributeName = en.nextElement();
            if (!attributeName.equals("user") && !attributeName.equals("fullname") && !attributeName.equals("numberProductsInCart")) {
                CartItem cartItem = (CartItem) session.getAttribute(attributeName);
                if (cartItem != null) {
                    orderDetailDAO.insert(orderDAO.getOrdersById(orderId), cartItem);
                }
            }
        }
        int billId = billDAO.insert(orderDAO.getOrdersById(orderId), user, "wait");
        removeAllItems(session);
        model.addAttribute("checkOutDone", "checkOutDone");
        model.addAttribute("BillId", billId);
        return "cart"; // Replace with your checkout view template.
    }

    @GetMapping("/cart/billDetails")
    public String showBillDetails(@RequestParam("billId") int billId, Model model) {
        List<BillDetail> billDetails = billDAO.showBillDetail(billId);
        model.addAttribute("billDetails", billDetails);
        model.addAttribute("showBill", "showBill");
        return "cart"; // Replace with your bill details view template.
    }
}
