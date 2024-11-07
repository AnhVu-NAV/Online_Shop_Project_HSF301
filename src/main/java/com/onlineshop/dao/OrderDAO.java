package com.onlineshop.dao;

import com.onlineshop.entity.Order;
import com.onlineshop.entity.User;

import java.util.List;
//
public interface OrderDAO {
    List<Order> getOrdersByUserId(int userId);
    int insert(Order order, User user);
    Order getOrdersById(int orderId);
    void deleteOrderById(int orderId);
    void saveOrUpdateOrder(Order order);
    Order getOrderById(int orderId);
    Order getOrderByIdWithDetails(int orderId);
}