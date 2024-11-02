// Brand DAO Interface
package com.onlineshop.dao;

import com.onlineshop.entity.Brand;
import java.util.List;

public interface BrandDAO {
    List<Brand> getAllBrands();
    Brand getBrandById(int brandId);
    void saveBrand(Brand brand);
    void deleteBrand(int brandId);
}
