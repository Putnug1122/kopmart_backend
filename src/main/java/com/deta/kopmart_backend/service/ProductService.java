package com.deta.kopmart_backend.service;

import com.deta.kopmart_backend.entity.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductInfo findOne(String productId);

    void decreaseStock(String productId, Integer count);

    Page<ProductInfo> findAllInCategory(Integer categoryType, Pageable pageable);

    Page<ProductInfo> findAll(Pageable pageable);

    void delete(String productId);

    ProductInfo update(ProductInfo product);

    ProductInfo save(ProductInfo product);

    ProductInfo offSale(String productId);

    ProductInfo onSale(String productId);

    void increaseStock(String productId, int amount);

    Page<ProductInfo> findAllByNameLike(String name, Pageable pageable);
}
