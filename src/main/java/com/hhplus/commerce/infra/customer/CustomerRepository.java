package com.hhplus.commerce.infra.customer;

import com.hhplus.commerce.domain.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
