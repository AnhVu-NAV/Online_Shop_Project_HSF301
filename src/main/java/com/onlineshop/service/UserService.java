package com.onlineshop.service;

import com.onlineshop.entity.User;
import java.util.List;

public interface UserService {
    List<User> getAllCustomers();
    void banUser(int userId);
    void unbanUser(int userId);
    List<User> getCustomersByName(String name);
    User getUserByUsernameAndPassword(String username, String password);
    boolean isUsernameTaken(String username);
    void insertUser(User user);

}