package com.deta.kopmart_backend.service.impl;

import com.deta.kopmart_backend.entity.ProductInfo;
import com.deta.kopmart_backend.enums.ProductStatusEnum;
import com.deta.kopmart_backend.enums.ResultEnum;
import com.deta.kopmart_backend.exception.MyException;
import com.deta.kopmart_backend.repository.ProductInfoRepository;
import com.deta.kopmart_backend.service.CategoryService;
import com.deta.kopmart_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductInfoRepository productInfoRepository;

    @Autowired
    CategoryService categoryService;

    @Override
    public ProductInfo findOne(String productId) {
        ProductInfo productInfo = productInfoRepository.findByProductId(productId);
        return productInfo;
    }

    @Override
    public void decreaseStock(String productId, Integer count) {
        ProductInfo productInfo = findOne(productId);
        if(productInfo == null) throw new MyException(ResultEnum.PRODUCT_NOT_EXIST);

        int update = productInfo.getProductStock() - count;
        if(update <= 0) throw new MyException(ResultEnum.PRODUCT_NOT_ENOUGH);

        productInfo.setProductStock(update);
        productInfoRepository.save(productInfo);
    }

    @Override
    public Page<ProductInfo> findAllInCategory(Integer categoryType, Pageable pageable) {
        return productInfoRepository.findAllByCategoryTypeOrderByProductIdAsc(categoryType, pageable);
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return productInfoRepository.findAllByOrderByProductId(pageable);
    }

    @Override
    public void delete(String productId) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null) throw new MyException(ResultEnum.PRODUCT_NOT_EXIST);
        productInfoRepository.delete(productInfo);
    }

    @Override
    public ProductInfo update(ProductInfo product) {
        categoryService.findByCategoryType(product.getCategoryType());
        if(product.getProductStatus() > 1) {
            throw new MyException(ResultEnum.PRODUCT_STATUS_ERROR);
        }


        return productInfoRepository.save(product);
    }

    @Override
    public ProductInfo save(ProductInfo product) {
        return update(product);
    }


    @Override
    @Transactional
    public void increaseStock(String productId, int amount) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null) throw new MyException(ResultEnum.PRODUCT_NOT_EXIST);

        int update = productInfo.getProductStock() + amount;
        productInfo.setProductStock(update);
        productInfoRepository.save(productInfo);
    }

    @Override
    public Page<ProductInfo> findAllByNameLike(String name, Pageable pageable) {
        return productInfoRepository.findAllByProductNameLike(name, pageable);
    }

    @Override
    @Transactional
    public ProductInfo offSale(String productId) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null) throw new MyException(ResultEnum.PRODUCT_NOT_EXIST);

        if (productInfo.getProductStatus() == ProductStatusEnum.DOWN.getCode()) {
            throw new MyException(ResultEnum.PRODUCT_STATUS_ERROR);
        }

        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());
        return productInfoRepository.save(productInfo);
    }

    @Override
    @Transactional
    public ProductInfo onSale(String productId) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null) throw new MyException(ResultEnum.PRODUCT_NOT_EXIST);

        if (productInfo.getProductStatus() == ProductStatusEnum.UP.getCode()) {
            throw new MyException(ResultEnum.PRODUCT_STATUS_ERROR);
        }

        productInfo.setProductStatus(ProductStatusEnum.UP.getCode());
        return productInfoRepository.save(productInfo);
    }

}
