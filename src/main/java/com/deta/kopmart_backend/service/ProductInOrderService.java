package com.deta.kopmart_backend.service;

import com.deta.kopmart_backend.entity.ProductInOrder;
import com.deta.kopmart_backend.entity.User;

public interface ProductInOrderService {
    void update(String itemId, Integer quantity, User user);

    ProductInOrder findOne(String itemId, User user);
}
