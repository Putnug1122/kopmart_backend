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

/**
 * @author deta
 * @description Concrete implementation of ProductService
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductInfoRepository productInfoRepository;

    @Autowired
    CategoryService categoryService;

    /**
     * @param productId String of productId
     * @return ProductInfo
     * @description Get product by productId
     */
    @Override
    public ProductInfo findOne(String productId) {
        ProductInfo productInfo = productInfoRepository.findByProductId(productId);
        return productInfo;
    }

    /**
     * @param productId String of productId
     * @param count Integer of count
     * @return ProductInfo
     * @description Decreasing product stock when the user buy a product
     */
    @Override
    public void decreaseStock(String productId, Integer count) {
        ProductInfo productInfo = findOne(productId);
        if(productInfo == null) throw new MyException(ResultEnum.PRODUCT_NOT_EXIST);

        int update = productInfo.getProductStock() - count;
        if(update <= 0) throw new MyException(ResultEnum.PRODUCT_NOT_ENOUGH);

        productInfo.setProductStock(update);
        productInfoRepository.save(productInfo);
    }

    /**
     * @param categoryType Integer of categoryType
     * @param pageable Pageable
     * @return Page<ProductInfo>
     * @description Get product list by categoryType
     */
    @Override
    public Page<ProductInfo> findAllInCategory(Integer categoryType, Pageable pageable) {
        return productInfoRepository.findAllByCategoryTypeOrderByProductIdAsc(categoryType, pageable);
    }

    /**
     * @param pageable Pageable
     * @return Page<ProductInfo>
     * @description Get All product list
     */
    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return productInfoRepository.findAllByOrderByProductId(pageable);
    }

    /**
     * @param productId String of productId
     * @description Delete product by productId
     * @throws MyException if product not exist
     */
    @Override
    public void delete(String productId) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null) throw new MyException(ResultEnum.PRODUCT_NOT_EXIST);
        productInfoRepository.delete(productInfo);
    }

    /**
     * @param product ProductInfo
     * @return ProductInfo
     * @description Update product
     * @throws MyException if product not exist
     */
    @Override
    public ProductInfo update(ProductInfo product) {
        categoryService.findByCategoryType(product.getCategoryType());
        if(product.getProductStatus() > 1) {
            throw new MyException(ResultEnum.PRODUCT_STATUS_ERROR);
        }


        return productInfoRepository.save(product);
    }

    /**
     * @param product ProductInfo
     * @return ProductInfo
     * @description Create product
     */
    @Override
    public ProductInfo save(ProductInfo product) {
        return update(product);
    }


    /**
     * @param productId String of productId
     * @param amount Integer of amount
     * @return ProductInfo
     * @description Increase product stock
     */
    @Override
    @Transactional
    public void increaseStock(String productId, int amount) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null) throw new MyException(ResultEnum.PRODUCT_NOT_EXIST);

        int update = productInfo.getProductStock() + amount;
        productInfo.setProductStock(update);
        productInfoRepository.save(productInfo);
    }

    /**
     * @param name String of name
     * @param pageable Pageable
     * @return Page<ProductInfo>
     * @description Get Product list by searched name
     */
    @Override
    public Page<ProductInfo> findAllByNameLike(String name, Pageable pageable) {
        return productInfoRepository.findAllByProductNameContainingIgnoreCase(name, pageable);
    }

    /**
     * @param pageable Pageable
     * @return Page<ProductInfo>
     * @description Get top 4 newest product created
     */
    @Override
    public Page<ProductInfo> findFourNewestProducts(Pageable pageable) {
        return productInfoRepository.findTop4ByOrderByProductIdDesc(pageable);
    }

    /**
     * @param productId String of productId
     * @return ProductInfo
     * @description Off-sale product when the product is out of stock
     */
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

    /**
     * @param productId String of productId
     * @return ProductInfo
     * @description On-sale product when the product stock is not 0
     */
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
