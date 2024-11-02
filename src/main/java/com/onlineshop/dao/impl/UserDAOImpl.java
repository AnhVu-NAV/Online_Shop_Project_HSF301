package com.onlineshop.dao.impl;

import com.onlineshop.dao.UserDAO;
import com.onlineshop.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<User> getAllCustomers() {
        Session session = sessionFactory.getCurrentSession();
        Query<User> query = session.createQuery("from User where role.id = 1", User.class);
        return query.getResultList();
    }

    @Override
    public User getUserById(int userId) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(User.class, userId);
    }

    @Override
    public void banUser(int userId) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class, userId);
        if (user != null) {
            user.setBanned(true);
            session.update(user);
        }
    }

    @Override
    public void unbanUser(int userId) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class, userId);
        if (user != null) {
            user.setBanned(false);
            session.update(user);
        }
    }

    @Override
    public List<User> getCustomersByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        Query<User> query = session.createQuery("from User where role.id = 1 and fullName like :name", User.class);
        query.setParameter("name", "%" + name + "%");
        return query.getResultList();
    }
}