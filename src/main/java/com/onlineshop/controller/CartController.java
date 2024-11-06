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
