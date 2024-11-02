package com.onlineshop.dao.impl;

import com.onlineshop.dao.BrandDAO;
import com.onlineshop.entity.Brand;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BrandDAOImpl implements BrandDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Brand> getAllBrands() {
        Session session = sessionFactory.getCurrentSession();
        Query<Brand> query = session.createQuery("from Brand", Brand.class);
        return query.getResultList();
    }

    @Override
    public Brand getBrandById(int brandId) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Brand.class, brandId);
    }

    @Override
    public void saveBrand(Brand brand) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(brand);
    }

    @Override
    public void deleteBrand(int brandId) {
        Session session = sessionFactory.getCurrentSession();
        Brand brand = session.get(Brand.class, brandId);
        if (brand != null) {
            session.delete(brand);
        }
    }
}
