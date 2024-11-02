package com.onlineshop.service.impl;

import com.onlineshop.dao.BrandDAO;
import com.onlineshop.entity.Brand;
import com.onlineshop.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandDAO brandDAO;

    @Override
    @Transactional
    public List<Brand> getAllBrands() {
        return brandDAO.getAllBrands();
    }

    @Override
    @Transactional
    public Brand getBrandById(int brandId) {
        return brandDAO.getBrandById(brandId);
    }

    @Override
    @Transactional
    public void saveBrand(Brand brand) {
        brandDAO.saveBrand(brand);
    }

    @Override
    @Transactional
    public void deleteBrand(int brandId) {
        brandDAO.deleteBrand(brandId);
    }
}
