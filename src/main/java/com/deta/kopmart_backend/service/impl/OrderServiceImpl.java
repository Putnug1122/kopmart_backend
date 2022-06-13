package com.deta.kopmart_backend.service.impl;

import com.deta.kopmart_backend.entity.OrderMain;
import com.deta.kopmart_backend.entity.ProductInOrder;
import com.deta.kopmart_backend.entity.ProductInfo;
import com.deta.kopmart_backend.enums.OrderStatusEnum;
import com.deta.kopmart_backend.enums.ResultEnum;
import com.deta.kopmart_backend.exception.MyException;
import com.deta.kopmart_backend.repository.OrderRepository;
import com.deta.kopmart_backend.repository.ProductInOrderRepository;
import com.deta.kopmart_backend.repository.ProductInfoRepository;
import com.deta.kopmart_backend.repository.UserRepository;
import com.deta.kopmart_backend.service.OrderService;
import com.deta.kopmart_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author deta
 * @description Concrete implementation of OrderService
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductInfoRepository productInfoRepository;
    @Autowired
    ProductService productService;
    @Autowired
    ProductInOrderRepository productInOrderRepository;

    /**
     * @param pageable Pageable object
     * @return Page of OrderMain objects
     * @description Get all orders
     */
    @Override
    public Page<OrderMain> findAll(Pageable pageable) {
        return orderRepository.findAllByOrderByOrderStatusAscCreateTimeDesc(pageable);
    }

    /**
     * @param email Email of user
     * @param pageable Pageable object
     * @return Page of OrderMain objects
     * @description Get all orders of user by email
     */
    @Override
    public Page<OrderMain> findByBuyerEmail(String email, Pageable pageable) {
        return orderRepository.findAllByBuyerEmailOrderByOrderStatusAscCreateTimeDesc(email, pageable);
    }

    /**
     * @param status Order status
     * @param pageable Pageable object
     * @return Page of OrderMain objects
     * @description Get all orders by status
     */
    @Override
    public Page<OrderMain> findByStatus(Integer status, Pageable pageable) {
        return orderRepository.findAllByOrderStatusOrderByCreateTimeDesc(status, pageable);
    }

    /**
     * @param phone Phone of user
     * @param pageable Pageable object
     * @return Page of OrderMain objects
     * @description Get all orders of user by phone
     */
    @Override
    public Page<OrderMain> findByBuyerPhone(String phone, Pageable pageable) {
        return orderRepository.findAllByBuyerPhoneOrderByOrderStatusAscCreateTimeDesc(phone, pageable);
    }

    /**
     * @param orderId Order id
     * @return OrderMain object
     * @description Get order by order id
     */
    @Override
    public OrderMain findOne(Long orderId) {
        OrderMain orderMain = orderRepository.findByOrderId(orderId);
        if(orderMain == null) {
            throw new MyException(ResultEnum.ORDER_NOT_FOUND);
        }
        return orderMain;
    }

    /**
     * @param orderId Order id
     * @return OrderMain object
     * @description Confirm order by order id
     * @throws MyException If order not found
     */
    @Override
    @Transactional
    public OrderMain finish(Long orderId) {
        OrderMain orderMain = findOne(orderId);
        if(!orderMain.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            throw new MyException(ResultEnum.ORDER_STATUS_ERROR);
        }

        orderMain.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        orderRepository.save(orderMain);
        return orderRepository.findByOrderId(orderId);
    }

    /**
     * @param orderId Order id
     * @return OrderMain object
     * @description Cancel order by order id
     * @throws MyException If order not found
     */
    @Override
    public OrderMain cancel(Long orderId) {
        OrderMain orderMain = findOne(orderId);
        if(!orderMain.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            throw new MyException(ResultEnum.ORDER_STATUS_ERROR);
        }

        orderMain.setOrderStatus(OrderStatusEnum.CANCELED.getCode());
        orderRepository.save(orderMain);

        // Restore Stock
        Iterable<ProductInOrder> products = orderMain.getProducts();
        for(ProductInOrder productInOrder : products) {
            ProductInfo productInfo = productInfoRepository.findByProductId(productInOrder.getProductId());
            if(productInfo != null) {
                productService.increaseStock(productInOrder.getProductId(), productInOrder.getCount());
            }
        }
        return orderRepository.findByOrderId(orderId);
    }
}
