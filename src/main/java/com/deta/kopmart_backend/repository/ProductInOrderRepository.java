package com.deta.kopmart_backend.repository;

import com.deta.kopmart_backend.entity.ProductInOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductInOrderRepository extends JpaRepository<ProductInOrder, Long> {
}
