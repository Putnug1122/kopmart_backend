package com.deta.kopmart_backend.service;

import com.deta.kopmart_backend.entity.User;

import java.util.Collection;

public interface UserService {

    User findOne(String name);

    User save(User user);

    User update(User user);

    Collection<User> findByRole(String role);
}
