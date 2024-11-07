package com.onlineshop.dao.impl;

import com.onlineshop.dao.OrderDAO;
import com.onlineshop.entity.Order;
import jakarta.persistence.Query;
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

    @PersistenceContext
    private EntityManager entityManager;

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
        String hql = "FROM Order o WHERE o.user.id = :userId";
        List<Order> orders = entityManager.createQuery(hql, Order.class)
                .setParameter("userId", userId)
                .getResultList();
        return orders;
    }

    @Override
    public void deleteOrderById(int orderId) {
        Session session = sessionFactory.getCurrentSession();
        Order order = session.get(Order.class, orderId);
        if (order != null) {
            session.delete(order);
        }
    }

    @Override
    @Transactional
    public void saveOrUpdateOrder(Order order) {
        if (order.getId() == 0) { // Chỉ sử dụng persist nếu order chưa có ID
            entityManager.persist(order);
        } else { // Sử dụng merge nếu order đã có ID
            entityManager.merge(order);
        }
    }

    @Override
    public Order getOrderById(int orderId) {
        return entityManager.find(Order.class, orderId);
    }

    @Override
    public Order getOrderByIdWithDetails(int orderId) {
        String hql = "SELECT o FROM Order o LEFT JOIN FETCH o.orderDetails WHERE o.id = :orderId";
        Query query = entityManager.createQuery(hql); // Không ép kiểu sang TypedQuery
        query.setParameter("orderId", orderId);
        return (Order) query.getSingleResult(); // Ép kiểu ở đây thay vì ép kiểu Query
    }
}

