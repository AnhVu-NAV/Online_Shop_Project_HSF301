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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public String handleCartRequest(@RequestParam(name = "service", required = false) String service,
                                    @RequestParam(name = "id", required = false) Integer id,
                                    @RequestParam(name = "billId", required = false) Integer billId,
                                    HttpSession session, Model model) {
        switch (service) {
            case "addToCart":
                return addToCart(id, session);
            case "removeItem":
                return removeItem(id, session);
            case "removeAll":
                return removeAllItems(session);
            case "checkOut":
                return checkout(session, model);
            case "showBill":
                return showBillDetails(billId, model);
            default:
                return viewCart(session, model);
        }
    }

    private String viewCart(HttpSession session, Model model) {
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }
        model.addAttribute("cart", cart);

        // Calculate total
        double total = cart.values().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
        model.addAttribute("total", total);

        return "cart";
    }

    private String addToCart(int productId, HttpSession session) {
        Product product = productDAO.getProductById(productId);
        if (product != null) {
            Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
            if (cart == null) {
                cart = new HashMap<>();
            }

            CartItem cartItem = cart.get(productId);
            if (cartItem == null) {
                cart.put(productId, new CartItem(product, 1));
            } else {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
            }

            session.setAttribute("cart", cart);
        }
        return "redirect:/cart";
    }

    private String removeItem(int id, HttpSession session) {
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
        if (cart != null) {
            cart.remove(id);
            session.setAttribute("cart", cart);
        }
        return "redirect:/cart";
    }

    private String removeAllItems(HttpSession session) {
        session.setAttribute("cart", new HashMap<Integer, CartItem>());
        return "redirect:/cart";
    }

    @PostMapping("/cart")
    public String updateCart(HttpSession session, @RequestParam Map<String, String> params) {
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
        if (cart != null) {
            for (Map.Entry<Integer, CartItem> entry : cart.entrySet()) {
                String paramKey = "p" + entry.getKey();
                if (params.containsKey(paramKey)) {
                    int quantity = Integer.parseInt(params.get(paramKey));
                    if (quantity > 0) {
                        entry.getValue().setQuantity(quantity);
                    } else {
                        cart.remove(entry.getKey());
                    }
                }
            }
            session.setAttribute("cart", cart);
        }
        return "redirect:/cart";
    }

    private String checkout(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        java.util.Date date = new java.util.Date();
        Date currentDate = new Date(date.getTime());
        Order order = new Order(currentDate, user);
        int orderId = orderDAO.insert(order, user);

        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
        if (cart != null) {
            for (CartItem cartItem : cart.values()) {
                orderDetailDAO.insert(orderDAO.getOrdersById(orderId), cartItem);
            }
        }
        int billId = billDAO.insert(orderDAO.getOrdersById(orderId), user, "wait");
        removeAllItems(session);
        model.addAttribute("checkOutDone", "checkOutDone");
        model.addAttribute("BillId", billId);
        return "redirect:/cart";
    }

    private String showBillDetails(int billId, Model model) {
        List<BillDetail> billDetails = billDAO.showBillDetail(billId);
        model.addAttribute("billDetails", billDetails);
        model.addAttribute("showBill", "showBill");

        // Calculate total for bill
        double total = billDetails.stream()
                .mapToDouble(BillDetail::getSubTotal)
                .sum();
        model.addAttribute("total", total);

        return "cart";
    }
}
//package com.onlineshop.controller;
//
//import com.onlineshop.dao.BillDAO;
//import com.onlineshop.dao.OrderDAO;
//import com.onlineshop.dao.OrderDetailDAO;
//import com.onlineshop.dao.ProductDAO;
//import com.onlineshop.entity.BillDetail;
//import com.onlineshop.entity.CartItem;
//import com.onlineshop.entity.Order;
//import com.onlineshop.entity.Product;
//import com.onlineshop.entity.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import jakarta.servlet.http.HttpSession;
//
//import java.sql.Date;
//import java.util.Enumeration;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Controller
//public class CartController {
//
//    @Autowired
//    private ProductDAO productDAO;
//
//    @Autowired
//    private OrderDAO orderDAO;
//
//    @Autowired
//    private OrderDetailDAO orderDetailDAO;
//
//    @Autowired
//    private BillDAO billDAO;
//
//    @GetMapping("/cart")
//    public String viewCart(HttpSession session, Model model) {
//        // Retrieve the cart from the session or initialize a new one if not present
//        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
//        if (cart == null) {
//            cart = new HashMap<>();
//            session.setAttribute("cart", cart);
//        }
//        // Add the cart to the model for displaying in the view
//        model.addAttribute("cart", cart);
//        return "cart"; // Returns the view name for the cart page
//    }
//
//    @GetMapping("/cart/add")
//    public String addToCart(@RequestParam("productId") int productId, HttpSession session) {
//        // Retrieve the product from the database using the DAO
//        Product product = productDAO.getProductById(productId);
//        if (product != null) {
//            // Retrieve the cart from the session or initialize a new one if not present
//            Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
//            if (cart == null) {
//                cart = new HashMap<>();
//            }
//
//            // Check if the product is already in the cart
//            CartItem cartItem = cart.get(productId);
//            if (cartItem == null) {
//                // Add a new CartItem if it doesn't exist in the cart
//                cart.put(productId, new CartItem(product, 1));
//            } else {
//                // Increase the quantity of the existing CartItem
//                cartItem.setQuantity(cartItem.getQuantity() + 1);
//            }
//
//            // Update the session with the modified cart
//            session.setAttribute("cart", cart);
//        }
//        return "redirect:/cart"; // Redirects to the cart view
//    }
//
//    @GetMapping("/cart/remove")
//    public String removeItem(@RequestParam("id") int id, HttpSession session) {
//        // Retrieve the cart from the session
//        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
//        if (cart != null) {
//            // Remove the item from the cart if it exists
//            cart.remove(id);
//            session.setAttribute("cart", cart);
//        }
//        return "redirect:/cart";
//    }
//
//    @GetMapping("/cart/clear")
//    public String removeAllItems(HttpSession session) {
//        // Clear the entire cart by setting a new empty cart
//        session.setAttribute("cart", new HashMap<Integer, CartItem>());
//        return "redirect:/cart";
//    }
//
//    @PostMapping("/cart/update")
//    public String updateCart(HttpSession session, @RequestParam Map<String, String> params) {
//        // Retrieve the cart from the session
//        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
//        if (cart != null) {
//            // Iterate over the cart and update quantities based on the request parameters
//            for (Map.Entry<Integer, CartItem> entry : cart.entrySet()) {
//                String paramKey = "p" + entry.getKey();
//                if (params.containsKey(paramKey)) {
//                    int quantity = Integer.parseInt(params.get(paramKey));
//                    if (quantity > 0) {
//                        entry.getValue().setQuantity(quantity);
//                    } else {
//                        // Remove item if the updated quantity is zero or negative
//                        cart.remove(entry.getKey());
//                    }
//                }
//            }
//            // Update the session with the modified cart
//            session.setAttribute("cart", cart);
//        }
//        return "redirect:/cart";
//    }
//
//    @GetMapping("/cart/checkout")
//    public String checkout(HttpSession session, Model model) {
//        User user = (User) session.getAttribute("user");
//        if (user == null) {
//            return "redirect:/login";
//        }
//        // Create a new order with the current date
//        java.util.Date date = new java.util.Date();
//        Date currentDate = new Date(date.getTime());
//        Order order = new Order(currentDate, user);
//        int orderId = orderDAO.insert(order, user);
//
//        // Retrieve the cart from the session
//        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
//        if (cart != null) {
//            // Insert order details for each item in the cart
//            for (CartItem cartItem : cart.values()) {
//                orderDetailDAO.insert(orderDAO.getOrdersById(orderId), cartItem);
//            }
//        }
//        // Create a bill and clear the cart
//        int billId = billDAO.insert(orderDAO.getOrdersById(orderId), user, "wait");
//        removeAllItems(session);
//        model.addAttribute("checkOutDone", "checkOutDone");
//        model.addAttribute("BillId", billId);
//        return "cart"; // Replace with your checkout view template
//    }
//
//    @GetMapping("/cart/billDetails")
//    public String showBillDetails(@RequestParam("billId") int billId, Model model) {
//        List<BillDetail> billDetails = billDAO.showBillDetail(billId);
//        model.addAttribute("billDetails", billDetails);
//        model.addAttribute("showBill", "showBill");
//        return "cart"; // Replace with your bill details view template
//    }
//}
