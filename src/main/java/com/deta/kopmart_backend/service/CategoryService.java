package com.deta.kopmart_backend.service;

import com.deta.kopmart_backend.entity.ProductCategory;

import java.util.List;

public interface CategoryService {
    ProductCategory findByCategoryType(Integer categoryType);

    List<ProductCategory> findAll();

    ProductCategory save(ProductCategory productCategory);

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);
}
