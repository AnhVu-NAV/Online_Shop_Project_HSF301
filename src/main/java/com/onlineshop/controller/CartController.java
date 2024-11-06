package com.onlineshop.controller;

import com.onlineshop.dao.*;
import com.onlineshop.entity.*;
import com.onlineshop.service.VietQrService;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

@Controller
public class CartController {

    @Autowired
    private CartDAO cartDAO;

    @Autowired
    private CartItemDAO cartItemDAO;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private OrderDetailDAO orderDetailDAO;

    @Autowired
    private VietQrService vietQrService;

    // Hiển thị giỏ hàng
    @GetMapping("/cart")
    public String showCart(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Cart cart = cartDAO.findCartByUser(user);
        if (cart == null) {
            cart = new Cart(user); // Tạo giỏ hàng mới nếu chưa có
            cartDAO.saveOrUpdate(cart);
        } else {
            // Nạp trước danh sách items để tránh LazyInitializationException
            Hibernate.initialize(cart.getItems());
        }
        double totalAmount = cart.getItems().stream()
                .mapToDouble(i -> i.getQuantity() * i.getProduct().getPrice())
                .sum();
        model.addAttribute("totalAmount", totalAmount);

        model.addAttribute("cart", cart);
        return "cart"; // Tên của template giỏ hàng
    }

    // Thêm sản phẩm vào giỏ hàng
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("productId") Long productId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Cart cart = cartDAO.findCartByUser(user);
        if (cart == null) {
            cart = new Cart(user);
            cartDAO.saveOrUpdate(cart);
        }

        Product product = productDAO.getProductById(Math.toIntExact(productId));
        if (product == null) {
            return "redirect:/customer"; // Xử lý khi sản phẩm không tồn tại
        }

        CartItem cartItem = cartItemDAO.findCartItemByCartAndProduct(cart.getId(), productId);
        if (cartItem == null) {
            cartItem = new CartItem(cart, product, 1); // Tạo mục giỏ hàng mới
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + 1); // Tăng số lượng nếu mục giỏ hàng đã tồn tại
        }

        cartItemDAO.saveOrUpdate(cartItem);
        return "redirect:/cart";
    }

    // Xóa sản phẩm khỏi giỏ hàng
    @GetMapping("/cart/remove")
    public String removeItem(@RequestParam("productId") Long productId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Cart cart = cartDAO.findCartByUser(user);
        if (cart == null) {
            return "redirect:/cart"; // Xử lý khi giỏ hàng không tồn tại
        }

        // Tìm CartItem theo Cart và Product
        CartItem cartItem = cartItemDAO.findCartItemByCartAndProduct(cart.getId(), productId);
        if (cartItem != null) {
            // Xóa CartItem khỏi danh sách items của Cart để tránh cascade
            cart.getItems().remove(cartItem);

            // Xóa CartItem khỏi cơ sở dữ liệu
            cartItemDAO.delete(cartItem);
        }

        return "redirect:/cart";
    }


    // Cập nhật số lượng sản phẩm trong giỏ hàng
    @PostMapping("/cart/update")
    public String updateCart(HttpSession session, @RequestParam("productId") Long productId, @RequestParam("quantity") int quantity) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Cart cart = cartDAO.findCartByUser(user);
        if (cart == null) {
            return "redirect:/cart";
        }

        CartItem cartItem = cartItemDAO.findCartItemByCartAndProduct(cart.getId(), productId);
        if (cartItem != null) {
            cartItem.setQuantity(quantity);
            cartItemDAO.saveOrUpdate(cartItem);
        }

        return "redirect:/cart";
    }

    // Xóa tất cả sản phẩm khỏi giỏ hàng
    @GetMapping("/cart/clear")
    public String clearCart(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Cart cart = cartDAO.findCartByUser(user);
        if (cart != null) {
            cart.getItems().forEach(cartItemDAO::delete);
        }

        return "redirect:/cart";
    }

    // Thực hiện thanh toán
    @PostMapping("/cart/checkout")
    public String checkout(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Cart cart = cartDAO.findCartByUser(user);
        if (cart == null || cart.getItems().isEmpty()) {
            model.addAttribute("error", "Your cart is empty.");
            return "redirect:/cart";
        }

        // Tính tổng tiền
        double totalAmount = cart.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getProduct().getPrice())
                .sum();

        // Tạo mã QR thanh toán
        String qrCodeUrl = vietQrService.createQrCode(totalAmount, "Thông tin thanh toán "+ user.getFullName());
        model.addAttribute("qrCodeUrl", qrCodeUrl);

        model.addAttribute("cart", cart);
        model.addAttribute("totalAmount", totalAmount);
        return "checkout"; // Trang xác nhận thanh toán
    }

    @PostMapping("/cart/process-checkout")
    public String processCheckout(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Cart cart = cartDAO.findCartByUser(user);
        if (cart == null || cart.getItems().isEmpty()) {
            model.addAttribute("error", "Your cart is empty.");
            return "redirect:/cart";
        }

        // Tính tổng giá trị đơn hàng
        double totalAmount = cart.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getProduct().getPrice())
                .sum();

        // Tạo và lưu đơn hàng
        Order order = new Order();
        order.setCreatedDate(new Date(System.currentTimeMillis()));
        order.setTotalAmount(totalAmount);
        order.setUser(user);
        orderDAO.saveOrUpdateOrder(order);

        // Lưu chi tiết đơn hàng
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order); // Gán order đã lưu
            orderDetail.setProduct(cartItem.getProduct());
            orderDetail.setProductQuantity(cartItem.getQuantity());
            orderDetail.setPrice(cartItem.getProduct().getPrice());

            orderDetailDAO.saveOrderDetail(orderDetail); // Lưu từng order detail
            orderDetails.add(orderDetail);
        }

        // Xóa giỏ hàng sau khi thanh toán
        cart.getItems().forEach(cartItemDAO::delete);
        cartDAO.clearCart(cart); // Xóa toàn bộ giỏ hàng của người dùng

        // Thêm các thuộc tính cần thiết vào Model
        model.addAttribute("order", order);
        model.addAttribute("orderDetails", orderDetails);
        model.addAttribute("success", "Payment successful!");

        return "order_confirmation"; // Trang xác nhận đơn hàng thành công
    }

//    // Phương thức phụ để cập nhật số lượng giỏ hàng trong session
//    private void updateCartQuantity() {
//        Cart cart = cartService.getCurrentCart();
//        int numberProductsInCart = cart.getItems().stream().mapToInt(CartItem::getQuantity).sum();
//        session.setAttribute("numberProductsInCart", numberProductsInCart);
//    }

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
