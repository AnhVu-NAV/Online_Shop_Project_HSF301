package com.onlineshop.dao;

import com.onlineshop.entity.CartItem;
import com.onlineshop.entity.Product;

import java.util.Map;

public interface CartDAO {
    Map<Integer, CartItem> createNewCart();
    void addProductToCart(Map<Integer, CartItem> cart, Product product, int quantity);
    void updateCartQuantities(Map<Integer, CartItem> cart, Map<String, String> allParams);
    void removeProductFromCart(Map<Integer, CartItem> cart, int productId);
}
