package com.deta.kopmart_backend.controller;

import com.deta.kopmart_backend.entity.Cart;
import com.deta.kopmart_backend.entity.ProductInOrder;
import com.deta.kopmart_backend.entity.ProductInfo;
import com.deta.kopmart_backend.entity.User;
import com.deta.kopmart_backend.form.ItemForm;
import com.deta.kopmart_backend.repository.ProductInOrderRepository;
import com.deta.kopmart_backend.service.CartService;
import com.deta.kopmart_backend.service.ProductInOrderService;
import com.deta.kopmart_backend.service.ProductService;
import com.deta.kopmart_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;

/**
 * @author deta
 * @date 2022/06/13
 * @description Cart Controller class for handling cart related requests from the client side.
 */
@CrossOrigin
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @Autowired
    ProductInOrderService productInOrderService;

    @Autowired
    ProductInOrderRepository productInOrderRepository;

    /**
     * @param productInOrders - Collection of product in order objects.
     * @param principal
     * @return - ResponseEntity object containing the cart object.
     * @description - This method is used to add products to the cart.
     * @throws Exception
     */
    @PostMapping("")
    public ResponseEntity<Cart> mergeCart(@RequestBody Collection<ProductInOrder> productInOrders, Principal principal) {
        User user = userService.findOne(principal.getName());
        try {
            cartService.mergeLocalCart(productInOrders, user);
        } catch (Exception e) {
            ResponseEntity.badRequest().body("Merge Cart Failed");
        }
        return ResponseEntity.ok(cartService.getCart(user));
    }

    /**
     * @param principal - Principal object containing the user property.
     * @return - Cart object by the user property.
     * @description - This method is used to get the cart object by the user property.
     */
    @GetMapping("")
    public Cart getCart(Principal principal) {
        User user = userService.findOne(principal.getName());
        return cartService.getCart(user);
    }

    /**
     * @param form - ItemForm object containing the product id and the quantity.
     * @param principal - Principal object containing the user property.
     * @return - boolean value indicating the success of the operation.
     * @throws Exception - Exception thrown if the operation fails.
     * @description - This method is used to add products to the cart.
     */
    @PostMapping("/add")
    public boolean addToCart(@RequestBody ItemForm form, Principal principal) {
        ProductInfo productInfo = productService.findOne(form.getProductId());
        try {
            mergeCart(Collections.singleton(new ProductInOrder(productInfo, form.getQuantity())), principal);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * @param itemId - String value containing the product id.
     * @param quantity - Integer value containing the quantity.
     * @param principal - Principal object containing the user property.
     * @return - ProductInOrder object containing the product info.
     * @description - This method is used to update the quantity of the product in the cart.
     */
    @PutMapping("/{itemId}")
    public ProductInOrder modifyItem(@PathVariable("itemId") String itemId, @RequestBody Integer quantity, Principal principal) {
        User user = userService.findOne(principal.getName());
        productInOrderService.update(itemId, quantity, user);
        return productInOrderService.findOne(itemId, user);
    }

    /**
     * @param itemId - String value containing the product id.
     * @param principal - Principal object containing the user property.
     * @description - This method is used to remove the product from the cart.
     */
    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable("itemId") String itemId, Principal principal) {
        User user = userService.findOne(principal.getName());
        cartService.delete(itemId, user);
        // flush memory into DB
    }


    /**
     * @param principal - Principal object containing the user property.
     * @return - ResponseEntity object containing the cart object.
     * @description - This method is used to clear the cart.
     */
    @PostMapping("/checkout")
    public ResponseEntity checkout(Principal principal) {
        User user = userService.findOne(principal.getName());// Email as username
        cartService.checkout(user);
        return ResponseEntity.ok(null);
    }
}
