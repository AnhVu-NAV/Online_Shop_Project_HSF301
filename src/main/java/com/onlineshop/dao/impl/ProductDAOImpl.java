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
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


// ProductDAO Implementations
@Repository
@Transactional
public class ProductDAOImpl implements ProductDAO {

    @Autowired
    private SessionFactory sessionFactory;
    @PersistenceContext
    private EntityManager entityManager;

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
    public List<Product> getProductsByKeywords(String keywords) {
        return entityManager.createQuery("FROM Product p WHERE p.name LIKE :keywords", Product.class)
                .setParameter("keywords", "%" + keywords + "%").getResultList();
    }


    @Override
    public void updateProduct(Product product) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(product);
        }

    public List<Product> filterByPrice(String filterByPrice, List<Product> products) {
        switch (filterByPrice) {
            case "price-500-750":
                return products.stream().filter(p -> p.getPrice() >= 500 && p.getPrice() <= 750).collect(Collectors.toList());
            case "price-750-1000":
                return products.stream().filter(p -> p.getPrice() > 750 && p.getPrice() <= 1000).collect(Collectors.toList());
            case "price-1000-1500":
                return products.stream().filter(p -> p.getPrice() > 1000 && p.getPrice() <= 1500).collect(Collectors.toList());
            case "price-1500up":
                return products.stream().filter(p -> p.getPrice() > 1500).collect(Collectors.toList());
            default:
                return products;
        }
    }

    @Override
    public void insertProduct(Product product) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(product);
        }
    public List<Product> filterByBrand(String filterByBrand, List<Product> products) {
        if (filterByBrand.equals("brand-all")) {
            return products;
        }
        int brandId = Integer.parseInt(filterByBrand.split("[-]")[1]);
        return products.stream().filter(p -> p.getBrand().getId() == brandId).collect(Collectors.toList());
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
    public List<Product> sortProducts(List<Product> products, String sortBy) {
        if (sortBy.equals("priceLowHigh")) {
            return products.stream().sorted(Comparator.comparing(Product::getPrice)).collect(Collectors.toList());
        }
        if (sortBy.equals("priceHighLow")) {
            return products.stream().sorted(Comparator.comparing(Product::getPrice).reversed()).collect(Collectors.toList());
        }
        if (sortBy.equals("latest")) {
            return products.stream().sorted(Comparator.comparing(Product::getReleaseDate).reversed()).collect(Collectors.toList());
        }
        return products;
    }

    @Override
    public int insertProducts(Product product) {
        Session session = sessionFactory.getCurrentSession();
        session.save(product);
        return product.getId();
    }
}
