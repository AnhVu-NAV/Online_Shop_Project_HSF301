package com.onlineshop.dao.impl;

import com.onlineshop.dao.ProductDAO;
import com.onlineshop.dao.BrandDAO;
import com.onlineshop.entity.Product;
import com.onlineshop.entity.Brand;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;


// ProductDAO Implementation
@Repository
public class ProductDAOImpl implements ProductDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Product> getAll() {
        Session session = sessionFactory.getCurrentSession();
        Query<Product> query = session.createQuery("from Product", Product.class);
        return query.getResultList();
    }

    @Override
    public Product getProductById(int productId) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Product.class, productId);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        Query<Product> query = session.createQuery("from Product where name like :name", Product.class);
        query.setParameter("name", "%" + name + "%");
        return query.getResultList();
    }

    @Override
    public void updateProduct(Product product) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(product);
    }

    @Override
    public void insertProduct(Product product) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(product);
    }

    @Override
    public void saveProduct(Product product) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(product);
    }

    @Override
    public void deleteProduct(int productId) {
        Session session = sessionFactory.getCurrentSession();
        Product product = session.get(Product.class, productId);
        if (product != null) {
            session.delete(product);
        }
    }
}