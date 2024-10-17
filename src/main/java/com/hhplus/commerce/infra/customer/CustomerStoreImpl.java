package com.hhplus.commerce.infra.customer;

import com.hhplus.commerce.domain.customer.Customer;
import com.hhplus.commerce.domain.customer.CustomerStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerStoreImpl implements CustomerStore {
    private final CustomerRepository customerRepository;

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }
}
