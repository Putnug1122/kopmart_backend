package com.deta.kopmart_backend.service;

import com.deta.kopmart_backend.entity.Cart;
import com.deta.kopmart_backend.entity.ProductInOrder;
import com.deta.kopmart_backend.entity.User;

import java.util.Collection;
import java.util.Collections;

public interface CartService {
    Cart getCart(User user);

    void mergeLocalCart(Collection<ProductInOrder> productInOrders, User user);

    void delete(String itemId, User user);

    void checkout(User user);
}
