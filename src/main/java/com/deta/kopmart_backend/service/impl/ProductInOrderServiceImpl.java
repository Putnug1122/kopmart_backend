package com.deta.kopmart_backend.service.impl;

import com.deta.kopmart_backend.entity.ProductInOrder;
import com.deta.kopmart_backend.entity.User;
import com.deta.kopmart_backend.repository.ProductInOrderRepository;
import com.deta.kopmart_backend.service.ProductInOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ProductInOrderServiceImpl implements ProductInOrderService {

    @Autowired
    ProductInOrderRepository productInOrderRepository;

    @Override
    @Transactional
    public void update(String itemId, Integer quantity, User user) {

        Optional<ProductInOrder> op = user.getCart().getProducts().stream().filter(p -> itemId.equals(p.getProductId())).findFirst();
        op.ifPresent(productInOrder -> {
            productInOrder.setCount(quantity);
            productInOrderRepository.save(productInOrder);
        });
    }

    @Override
    public ProductInOrder findOne(String itemId, User user){
        Optional<ProductInOrder> op = user.getCart().getProducts().stream().filter(p -> itemId.equals(p.getProductId())).findFirst();
        AtomicReference<ProductInOrder> res = new AtomicReference<>();
        op.ifPresent(res::set);
        return res.get();
    }
}
