package com.onlineshop.dao.impl;

import com.onlineshop.dao.CartDAO;
import com.onlineshop.entity.Cart;
import com.onlineshop.entity.CartItem;
import com.onlineshop.entity.Product;
import com.onlineshop.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class CartDAOImpl implements CartDAO {
    @PersistenceContext
    private EntityManager entityManager;
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

    @Override
    public Cart findCartByUser(User user) {
        List<Cart> carts = entityManager.createQuery(
                        "SELECT c FROM Cart c LEFT JOIN FETCH c.items WHERE c.user = :user", Cart.class)
                .setParameter("user", user)
                .getResultList();

        return carts.isEmpty() ? null : carts.get(0);
    }


    @Override
    public void saveOrUpdate(Cart cart) {
        if (cart.getId() == 0) {
            entityManager.persist(cart);
        } else {
            entityManager.merge(cart);
        }    }

    @Override
    public void delete(Cart cart) {
        entityManager.remove(entityManager.contains(cart) ? cart : entityManager.merge(cart));
    }

    @Override
    public void clearCart(Cart cart) {
        for (CartItem item : cart.getItems()) {
            CartItem managedItem = entityManager.find(CartItem.class, item.getId());
            if (managedItem != null) {
                entityManager.remove(managedItem);
            }
        }
        cart.getItems().clear();
    }
}
