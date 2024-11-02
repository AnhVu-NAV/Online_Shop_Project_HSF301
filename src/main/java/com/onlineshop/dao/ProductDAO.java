package com.onlineshop.dao;

import com.onlineshop.entity.Product;
import java.util.List;

public interface ProductDAO {
    List<Product> getAll();
    Product getProductById(int productId);
    List<Product> getProductsByKeywords(String keywords);
    List<Product> filterByPrice(String filterByPrice, List<Product> products);
    List<Product> filterByBrand(String filterByBrand, List<Product> products);
    List<Product> sortProducts(List<Product> products, String sortBy);
}