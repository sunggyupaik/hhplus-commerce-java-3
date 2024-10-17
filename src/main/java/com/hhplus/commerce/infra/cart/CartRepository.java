package com.hhplus.commerce.infra.cart;

import com.hhplus.commerce.domain.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByCustomerId(Long customerId);

    boolean existsByCustomerIdAndItemOptionId(Long customerId, Long itemOptionId);

    Optional<Cart> findByCustomerIdAndItemOptionId(Long customerId, Long itemOptionId);

    void deleteCartByCustomerIdAndItemOptionId(Long customerId, Long itemOptionId);
}
