package com.deta.kopmart_backend.service.impl;

import com.deta.kopmart_backend.entity.ProductCategory;
import com.deta.kopmart_backend.enums.ResultEnum;
import com.deta.kopmart_backend.exception.MyException;
import com.deta.kopmart_backend.repository.ProductCategoryRepository;
import com.deta.kopmart_backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author deta
 * @description Concrete implementation of CategoryService
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    ProductCategoryRepository productCategoryRepository;

    /**
     * @param categoryType category type
     * @return List of ProductCategory
     * @throws MyException if category type is invalid
     * @description Get all product categories by category type
     */
    @Override
    public ProductCategory findByCategoryType(Integer categoryType) {
        ProductCategory res = productCategoryRepository.findByCategoryType(categoryType);
        if(res == null) throw new MyException((ResultEnum.CATEGORY_NOT_FOUND));
        return res;
    }

    /**
     * @param categoryTypeList category type list
     * @return List of ProductCategory
     * @description Get all product categories by category type list
     */
    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList) {
        List<ProductCategory> res = productCategoryRepository.findByCategoryTypeInOrderByCategoryTypeAsc(categoryTypeList);
        //res.sort(Comparator.comparing(ProductCategory::getCategoryType));
        return res;
    }

    /**
     * @return List of ProductCategory
     * @description Get all product categories
     */
    @Override
    public List<ProductCategory> findAll() {
        List<ProductCategory> res = productCategoryRepository.findAllByOrderByCategoryType();
        //  res.sort(Comparator.comparing(ProductCategory::getCategoryType));
        return res;
    }

    /**
     * @param productCategory product category
     * @return ProductCategory
     * @description Create a new product category
     */
    @Override
    @Transactional
    public ProductCategory save(ProductCategory productCategory) {
        return productCategoryRepository.save(productCategory);
    }


}
