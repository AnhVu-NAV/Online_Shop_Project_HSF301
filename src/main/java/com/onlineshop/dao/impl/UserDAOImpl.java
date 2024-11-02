package com.onlineshop.dao.impl;

import com.onlineshop.dao.UserDAO;
import com.onlineshop.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
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

    @PersistenceContext
    private EntityManager entityManager;

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
        Query<User> query = session.createQuery("from User where role.id = 1 and fullname like :name", User.class);
        query.setParameter("name", "%" + name + "%");
        return query.getResultList();
    }

    @Override
    public User getOne(String username, String password) {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class);
        query.setParameter("username", username);
        query.setParameter("password", password);
        return query.getResultStream().findFirst().orElse(null);
    }

    @Override
    public void insert(User user) {
        entityManager.persist(user);
    }

    @Override
    public List<User> getAll() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }
}