package com.onlineshop.service.impl;

import com.onlineshop.dao.UserDAO;
import com.onlineshop.entity.User;
import com.onlineshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service//
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    @Transactional
    public List<User> getAllCustomers() {
        return userDAO.getAllCustomers();
    }

    @Override
    @Transactional
    public void banUser(int userId) {
        userDAO.banUser(userId);
    }

    @Override
    @Transactional
    public void unbanUser(int userId) {
        userDAO.unbanUser(userId);
    }

    @Override
    @Transactional
    public List<User> getCustomersByName(String name) {
        return userDAO.getCustomersByName(name);
    }

    @Override
    public User getUserByUsernameAndPassword(String username, String password) {
        return null;
    }

    @Override
    public boolean isUsernameTaken(String username) {
        return false;
    }

    @Override
    public void insertUser(User user) {

    }
}
