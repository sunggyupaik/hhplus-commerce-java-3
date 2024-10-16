package com.hhplus.commerce.domain.cart;

import java.util.List;

public interface CartReader {
    List<Cart> getCarts(Long customerId);
}
