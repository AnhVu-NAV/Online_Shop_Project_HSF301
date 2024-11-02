// ProductDAO Interface
package com.onlineshop.dao;

import com.onlineshop.entity.Product;
import java.util.List;

public interface ProductDAO {
    List<Product> getAll();


    Product getProductById(int productId);
    List<Product> getProductsByName(String name);
    void updateProduct(Product product);
    void insertProduct(Product product);

    void saveProduct(Product product);

    void deleteProduct(int productId);
} 