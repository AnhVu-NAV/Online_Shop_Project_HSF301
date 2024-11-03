package com.onlineshop.dao.impl;

import com.onlineshop.dao.OrderDAO;
import com.onlineshop.entity.Order;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
//
@Repository
@Transactional
public class OrderDAOImpl implements OrderDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Order> getOrdersByUserId(int userId) {
        return entityManager.createQuery("FROM Order o WHERE o.user.id = :userId", Order.class)
                .setParameter("userId", userId).getResultList();
    }
}

