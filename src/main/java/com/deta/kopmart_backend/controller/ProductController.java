package com.deta.kopmart_backend.controller;

import com.deta.kopmart_backend.entity.ProductInfo;
import com.deta.kopmart_backend.service.CategoryService;
import com.deta.kopmart_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
public class ProductController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    /**
     * @param page page number
     * @param size page size
     * @return List of product in page
     * @description get all product in page
     */
    @GetMapping("/product")
    public Page<ProductInfo> findAll(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                     @RequestParam(value = "size", defaultValue = "3") Integer size) {
        PageRequest request = PageRequest.of(page - 1, size);
        return productService.findAll(request);
    }

    /**
     * @param productId product id
     * @return product detail by product id
     * @description get product detail by product id
     */
    @GetMapping("/product/{productId}")
    public ProductInfo showOne(@PathVariable("productId") String productId) {
        return productService.findOne(productId);
    }

    /**
     * @param product product object
     * @param bindingResult binding result
     * @return ResponseEntity with status code
     * @description method to create new product
     */
    @PostMapping("/seller/product/new")
    public ResponseEntity create(@Valid @RequestBody ProductInfo product,
                                 BindingResult bindingResult) {
        ProductInfo productIdExists = productService.findOne(product.getProductId());
        if (productIdExists != null) {
            bindingResult
                    .rejectValue("productId", "error.product",
                            "There is already a product with the code provided");
        }
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult);
        }
        return ResponseEntity.ok(productService.save(product));
    }

    /**
     * @param productId product id
     * @param product product object
     * @param bindingResult binding result
     * @return ResponseEntity with status code
     * @description method to update product by product id
     */
    @PutMapping("/seller/product/{id}/edit")
    public ResponseEntity edit(@PathVariable("id") String productId,
                               @Valid @RequestBody ProductInfo product,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult);
        }
        if (!productId.equals(product.getProductId())) {
            return ResponseEntity.badRequest().body("Id Not Matched");
        }

        return ResponseEntity.ok(productService.update(product));
    }

    /**
     * @param productId product id
     * @return ResponseEntity with status code
     * @description method to delete product by product id
     */
    @DeleteMapping("/seller/product/{id}/delete")
    public ResponseEntity delete(@PathVariable("id") String productId) {
        productService.delete(productId);
        return ResponseEntity.ok().build();
    }

    /**
     * @param page page number
     * @param size page size
     * @param productName keyword to search
     * @return List of product in page
     * @description get all product in page by product name
     */
//    get all product contain name like keyword
    @GetMapping("/product/search")
    public Page<ProductInfo> search(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                    @RequestParam(value = "size", defaultValue = "3") Integer size,
                                    @RequestParam(value = "name") String productName) {
        PageRequest request = PageRequest.of(page - 1, size);
        return productService.findAllByNameLike(productName, request);
    }

    /**
     * @param page page number
     * @param size page size
     * @return List of product in page
     * @description get top 4 current added product
     */
     @GetMapping("/product/new")
    public Page<ProductInfo> findFourNewestProducts(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                    @RequestParam(value = "size", defaultValue = "4") Integer size) {
         PageRequest request = PageRequest.of(page - 1, size);
         return productService.findFourNewestProducts(request);
     }
}
