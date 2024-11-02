package com.onlineshop.dao.impl;

import com.onlineshop.dao.ProductDAO;
import com.onlineshop.entity.Product;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class ProductDAOImpl implements ProductDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Product> getAll() {
        return entityManager.createQuery("FROM Product", Product.class).getResultList();
    }

    @Override
    public Product getProductById(int productId) {
        return entityManager.find(Product.class, productId);
    }

    @Override
    public List<Product> getProductsByKeywords(String keywords) {
        return entityManager.createQuery("FROM Product p WHERE p.name LIKE :keywords", Product.class)
                .setParameter("keywords", "%" + keywords + "%").getResultList();
    }

    @Override
    public List<Product> filterByPrice(String filterByPrice, List<Product> products) {
        // Implement filtering logic based on price range
        // This is a placeholder
        return products;
    }

    @Override
    public List<Product> filterByBrand(String filterByBrand, List<Product> products) {
        // Implement filtering logic based on brand
        // This is a placeholder
        return products;
    }

    @Override
    public List<Product> sortProducts(List<Product> products, String sortBy) {
        // Implement sorting logic based on sortBy parameter
        // This is a placeholder
        return products;
    }
}
