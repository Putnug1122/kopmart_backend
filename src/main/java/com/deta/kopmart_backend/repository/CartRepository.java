package com.deta.kopmart_backend.repository;

import com.deta.kopmart_backend.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
