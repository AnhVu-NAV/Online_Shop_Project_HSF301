package com.onlineshop.dao;

import com.onlineshop.entity.CartItem;
import com.onlineshop.entity.Order;
import com.onlineshop.entity.OrderDetail;

import java.util.List;

public interface OrderDetailDAO {
    void insert(Order order, CartItem cartItem);
    List<OrderDetail> getOrderDetailsByOrderId(int orderId);
}
