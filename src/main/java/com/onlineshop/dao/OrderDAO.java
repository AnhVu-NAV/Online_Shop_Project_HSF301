package com.onlineshop.dao;

import com.onlineshop.entity.Order;
import java.util.List;
//
public interface OrderDAO {
    List<Order> getOrdersByUserId(int userId);
}