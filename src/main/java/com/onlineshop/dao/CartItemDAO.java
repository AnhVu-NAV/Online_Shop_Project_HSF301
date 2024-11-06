package com.onlineshop.dao;

import com.onlineshop.entity.CartItem;

public interface CartItemDAO {
    CartItem findCartItemByCartAndProduct(long cartId, long productId);
    void saveOrUpdate(CartItem cartItem);
    void delete(CartItem cartItem);
}
