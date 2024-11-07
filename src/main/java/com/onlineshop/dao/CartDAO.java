package com.onlineshop.dao;

import com.onlineshop.entity.Cart;
import com.onlineshop.entity.CartItem;
import com.onlineshop.entity.Product;
import com.onlineshop.entity.User;


import java.util.Map;

public interface CartDAO {
    Map<Integer, CartItem> createNewCart();
    void addProductToCart(Map<Integer, CartItem> cart, Product product, int quantity);
    void updateCartQuantities(Map<Integer, CartItem> cart, Map<String, String> allParams);
    void removeProductFromCart(Map<Integer, CartItem> cart, int productId);
    Cart findCartByUser(User user);
    void saveOrUpdate(Cart cart);
    void delete(Cart cart);
    void clearCart(Cart cart);
}
