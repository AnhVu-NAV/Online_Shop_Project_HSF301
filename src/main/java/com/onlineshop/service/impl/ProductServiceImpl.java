package com.onlineshop.service.impl;

import com.onlineshop.entity.Product;
import com.onlineshop.entity.Brand;
import com.onlineshop.service.ProductService;
import com.onlineshop.dao.ProductDAO;
import com.onlineshop.dao.BrandDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

// Class Definition
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private BrandDAO brandDAO;

    @Override
    public List<Product> getAllProducts() {
        return productDAO.getAll();
    }

    @Override
    public Product getProductById(int productId) {
        return productDAO.getProductById(productId);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productDAO.getProductsByName(name);
    }

    @Override
    public void updateProduct(int id, String name, double price, int quantity, LocalDate releaseDate) {
        Product product = productDAO.getProductById(id);
        if (product != null) {
            product.setName(name);
            product.setPrice(price);
            product.setQuantity(quantity);
            product.setReleaseDate(releaseDate);
            productDAO.updateProduct(product);
        }
    }

    @Override
    public void insertProduct(String name, double price, int quantity, String description, String imageUrl, int brandId, LocalDate releaseDate) {
        Brand brand = brandDAO.getBrandById(brandId);
        if (brand != null) {
            Product product = Product.builder()
                    .name(name)
                    .price(price)
                    .quantity(quantity)
                    .description(description)
                    .imageUrl(imageUrl)
                    .brand(brand)
                    .releaseDate(releaseDate)
                    .build();
            productDAO.insertProduct(product);
        }
    }

    @Override
    public boolean deleteProduct(int productId) {
        Product product = productDAO.getProductById(productId);
        if (product != null) {
            productDAO.deleteProduct(productId);
            return true;
        }
        return false;
    }

    @Override
    public List<Brand> getAllBrands() {
        return brandDAO.getAllBrands();
    }
}