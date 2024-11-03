package com.onlineshop.dao.impl;

import com.onlineshop.dao.OrderDetailDAO;
import com.onlineshop.entity.CartItem;
import com.onlineshop.entity.Order;
import com.onlineshop.entity.OrderDetail;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class OrderDetailDAOImpl implements OrderDetailDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void insert(Order order, CartItem cartItem) {
        Session session = sessionFactory.getCurrentSession();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
        orderDetail.setProduct(cartItem.getProduct());
        orderDetail.setProductQuantity(cartItem.getQuantity());
        orderDetail.setPrice(cartItem.getProduct().getPrice());
        session.save(orderDetail);
    }

    @Override
    public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM OrderDetail od WHERE od.order.id = :orderId";
        TypedQuery<OrderDetail> query = session.createQuery(hql, OrderDetail.class);
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }
}
