package com.hhplus.commerce.infra.cart;

import com.hhplus.commerce.domain.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByCustomerId(Long customerId);

    boolean existsByCustomerIdAndItemOptionId(Long customerId, Long itemOptionId);

    Cart deleteCartsByCustomerIdAndItemOptionId(Long customerId, Long itemOptionId);
}
