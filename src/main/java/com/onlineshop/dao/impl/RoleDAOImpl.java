package com.onlineshop.dao.impl;

// RoleDAOImpl.java
import com.onlineshop.dao.RoleDAO;
import com.onlineshop.entity.Role;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class RoleDAOImpl implements RoleDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Role getRoleById(int id) {
        return entityManager.find(Role.class, id);
    }
}
