package com.onlineshop.dao.impl;

import com.onlineshop.dao.CartItemDAO;
import com.onlineshop.entity.CartItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class CartItemDAOImpl implements CartItemDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public CartItem findCartItemByCartAndProduct(long cartId, long productId) {
        List<CartItem> items = entityManager.createQuery("SELECT ci FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.product.id = :productId", CartItem.class)
                .setParameter("cartId", cartId)
                .setParameter("productId", productId)
                .getResultList();

        if (items.isEmpty()) {
            return null; // hoặc tạo mới CartItem nếu cần thiết
        } else {
            return items.get(0);
        }
    }

    @Override
    public void saveOrUpdate(CartItem cartItem) {
        if (cartItem.getId() == 0) {
            entityManager.persist(cartItem);
        } else {
            entityManager.merge(cartItem);
        }
    }

    @Override
    public void delete(CartItem cartItem) {
        entityManager.remove(entityManager.contains(cartItem) ? cartItem : entityManager.merge(cartItem));
    }
}
