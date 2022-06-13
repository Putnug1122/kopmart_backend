package com.deta.kopmart_backend.service.impl;

import com.deta.kopmart_backend.entity.Cart;
import com.deta.kopmart_backend.entity.OrderMain;
import com.deta.kopmart_backend.entity.ProductInOrder;
import com.deta.kopmart_backend.entity.User;
import com.deta.kopmart_backend.enums.ResultEnum;
import com.deta.kopmart_backend.exception.MyException;
import com.deta.kopmart_backend.repository.CartRepository;
import com.deta.kopmart_backend.repository.OrderRepository;
import com.deta.kopmart_backend.repository.ProductInOrderRepository;
import com.deta.kopmart_backend.repository.UserRepository;
import com.deta.kopmart_backend.service.CartService;
import com.deta.kopmart_backend.service.ProductService;
import com.deta.kopmart_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.SecondaryTable;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * @author deta
 * @description Concrete implementation of CartService
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    ProductService productService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductInOrderRepository productInOrderRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    UserService userService;

    /**
     * @param user User who's cart is being retrieved
     * @return Cart object
     * @description Retrieves the cart for the user
     */
    @Override
    public Cart getCart(User user) {
        return user.getCart();
    }

    /**
     * @param productInOrders Collection of ProductInOrder objects
     * @param user User who's cart is being merged
     * @description Merges the local cart with the cart in the database
     */
    @Override
    public void mergeLocalCart(Collection<ProductInOrder> productInOrders, User user) {
        Cart finalCart = user.getCart();
        productInOrders.forEach(productInOrder -> {
            Set<ProductInOrder> set = finalCart.getProducts();
            Optional<ProductInOrder> old = set.stream().filter(e -> e.getProductId().equals(productInOrder.getProductId())).findFirst();
            ProductInOrder prod;
            if (old.isPresent()) {
                prod = old.get();
                prod.setCount(productInOrder.getCount() + prod.getCount());
            } else {
                prod = productInOrder;
                prod.setCart(finalCart);
                finalCart.getProducts().add(prod);
            }
            productInOrderRepository.save(prod);
        });
        cartRepository.save(finalCart);
    }

    /**
     * @param itemId ItemId of the product being deleted
     * @param user User who's cart is being deleted
     * @description Deletes the product from the cart
     */
    @Override
    @Transactional
    public void delete(String itemId, User user) {
        if(itemId.equals("") || user == null) {
            throw new MyException(ResultEnum.ORDER_STATUS_ERROR);
        }

        Optional<ProductInOrder> op = user.getCart().getProducts().stream().filter(e -> itemId.equals(e.getProductId())).findFirst();
        op.ifPresent(productInOrder -> {
            productInOrder.setCart(null);
            productInOrderRepository.deleteById(productInOrder.getId());
        });
    }

    /**
     * @param user User who's cart is being checked out
     * @description Checks out the cart and creates an order
     */
    @Override
    @Transactional
    public void checkout(User user) {
        OrderMain order = new OrderMain(user);
        orderRepository.save(order);

        user.getCart().getProducts().forEach(productInOrder -> {
            productInOrder.setCart(null);
            productInOrder.setOrderMain(order);
            productService.decreaseStock(productInOrder.getProductId(), productInOrder.getCount());
            productInOrderRepository.save(productInOrder);

        });
    }


}
