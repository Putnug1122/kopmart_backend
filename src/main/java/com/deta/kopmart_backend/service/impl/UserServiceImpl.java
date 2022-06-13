package com.deta.kopmart_backend.service.impl;

import com.deta.kopmart_backend.entity.Cart;
import com.deta.kopmart_backend.entity.User;
import com.deta.kopmart_backend.enums.ResultEnum;
import com.deta.kopmart_backend.exception.MyException;
import com.deta.kopmart_backend.repository.CartRepository;
import com.deta.kopmart_backend.repository.UserRepository;
import com.deta.kopmart_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * @author deta
 * @description Concrete implementation of UserService
 */
@Service
@DependsOn("passwordEncoder")
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CartRepository cartRepository;


    /**
     * @param email String of email
     * @return User
     * @description Get user by email
     */
    @Override
    public User findOne(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * @param user User Object
     * @return User
     * @description Register a new user
     * @throws MyException if the user already exists
     */
    @Override
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            User savedUser = userRepository.save(user);

            // initial Cart
            Cart savedCart = cartRepository.save(new Cart(savedUser));
            savedUser.setCart(savedCart);
            return userRepository.save(savedUser);

        } catch (Exception e) {
            throw new MyException(ResultEnum.VALID_ERROR);
        }
    }

    /**
     * @param user User Object
     * @return User
     * @description Update user
     */
    @Override
    @Transactional
    public User update(User user) {
        User oldUser = userRepository.findByEmail(user.getEmail());
        oldUser.setName(user.getName());
        oldUser.setPhone(user.getPhone());
        oldUser.setAddress(user.getAddress());
        return userRepository.save(oldUser);
    }

    /**
     * @param role String of role
     * @return Collection<User>
     * @description Get all users by role
     */
    @Override
    public Collection<User> findByRole(String role) {
        return userRepository.findAllByRole(role);
    }

}
