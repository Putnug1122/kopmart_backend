package com.deta.kopmart_backend.controller;

import com.deta.kopmart_backend.entity.ProductCategory;
import com.deta.kopmart_backend.entity.ProductInfo;
import com.deta.kopmart_backend.service.CategoryService;
import com.deta.kopmart_backend.service.ProductService;
import com.deta.kopmart_backend.vo.response.CategoryPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

/**
 * @author deta
 * @description Controller for category
 */
@RestController
@CrossOrigin
public class CategoryController {

    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;

    /**
     * @param categoryType category type
     * @param page page number
     * @param size page size
     * @return category page
     * @description get category type by page
     */
    @GetMapping("/category/{type}")
    public CategoryPage showOne(@PathVariable("type") Integer categoryType, @RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "size", defaultValue = "3") Integer size) {

        ProductCategory cat = categoryService.findByCategoryType(categoryType);
        PageRequest request = PageRequest.of(page - 1, size);
        Page<ProductInfo> productInCategory = productService.findAllInCategory(categoryType, request);
        CategoryPage tmp = new CategoryPage("", productInCategory);
        tmp.setCategory(cat.getCategoryName());
        return tmp;
    }}
