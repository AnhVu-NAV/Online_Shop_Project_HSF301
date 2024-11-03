package com.onlineshop.dao.impl;

import com.onlineshop.dao.CartDAO;
import com.onlineshop.entity.CartItem;
import com.onlineshop.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@Transactional
public class CartDAOImpl implements CartDAO {
    // Create a new cart (empty)
    @Override
    public Map<Integer, CartItem> createNewCart() {
        return new HashMap<>();
    }

    // Add product to cart
    @Override
    public void addProductToCart(Map<Integer, CartItem> cart, Product product, int quantity) {
        if (cart.containsKey(product.getId())) {
            CartItem existingItem = cart.get(product.getId());
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem(product, quantity);
            cart.put(product.getId(), newItem);
        }
    }

    // Update product quantities in the cart
    @Override
    public void updateCartQuantities(Map<Integer, CartItem> cart, Map<String, String> allParams) {
        allParams.forEach((key, value) -> {
            if (key.startsWith("p") && value.matches("\\d+")) {
                int productId = Integer.parseInt(key.substring(1)); // Remove the 'p' prefix
                int newQuantity = Integer.parseInt(value);

                if (cart.containsKey(productId)) {
                    CartItem item = cart.get(productId);
                    if (newQuantity > 0) {
                        item.setQuantity(newQuantity);
                    } else {
                        cart.remove(productId); // Remove the item if quantity is zero
                    }
                }
            }
        });
    }

    // Remove product from cart
    @Override
    public void removeProductFromCart(Map<Integer, CartItem> cart, int productId) {
        cart.remove(productId);
    }
}
