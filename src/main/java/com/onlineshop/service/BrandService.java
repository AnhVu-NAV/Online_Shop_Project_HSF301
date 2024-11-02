package com.onlineshop.service;

import com.onlineshop.entity.Brand;
import java.util.List;

// Interface Definition
public interface BrandService {
    List<Brand> getAllBrands();
    Brand getBrandById(int brandId);
    void saveBrand(Brand brand);
    void deleteBrand(int brandId);
}