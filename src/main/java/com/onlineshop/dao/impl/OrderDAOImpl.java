package com.onlineshop.dao.impl;

import com.onlineshop.dao.OrderDAO;
import com.onlineshop.entity.Order;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import com.onlineshop.dao.OrderDAO;
import com.onlineshop.entity.Order;
import com.onlineshop.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;
//
@Repository
@Transactional
public class OrderDAOImpl implements OrderDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public int insert(Order order, User user) {
        Session session = sessionFactory.getCurrentSession();
        order.setUser(user);
        session.save(order);
        return order.getId();
    }

    @Override
    public Order getOrdersById(int orderId) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Order.class, orderId);
    }

    @Override
    public List<Order> getOrdersByUserId(int userId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM Order o WHERE o.user.id = :userId";
        TypedQuery<Order> query = (TypedQuery<Order>) session.createQuery(hql, Order.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public void deleteOrderById(int orderId) {
        Session session = sessionFactory.getCurrentSession();
        Order order = session.get(Order.class, orderId);
        if (order != null) {
            session.delete(order);
        }
    }
}

