package com.onlineshop.dao.impl;

import com.onlineshop.dao.BrandDAO;
import com.onlineshop.entity.Brand;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class BrandDAOImpl implements BrandDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Brand> getAll() {
        return entityManager.createQuery("FROM Brand", Brand.class).getResultList();
    }
}