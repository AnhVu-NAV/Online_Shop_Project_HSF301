package com.onlineshop.service;

import com.onlineshop.entity.Product;
import com.onlineshop.entity.Brand;

import java.time.LocalDate;
import java.util.List;

// Interface Definition
public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(int productId);
    List<Product> getProductsByName(String name);
    void updateProduct(int id, String name, double price, int quantity, LocalDate releaseDate);
    void insertProduct(String name, double price, int quantity, String description, String imageUrl, int brandId, LocalDate releaseDate);
    boolean deleteProduct(int productId);
    List<Brand> getAllBrands();
}