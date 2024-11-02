package com.onlineshop.dao;

import com.onlineshop.entity.User;
import java.util.List;

public interface UserDAO {
    List<User> getAllCustomers();
    User getUserById(int userId);
    void banUser(int userId);
    void unbanUser(int userId);
    List<User> getCustomersByName(String name);
    User getOne(String username, String password);
    void insert(User user);
    List<User> getAll();
}