package com.group.lbstore.Service;

import com.group.lbstore.Model.Customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface CustomerService {
    Customer createCustomer(Customer customer);
    Page<Customer> getAllCustomers(Pageable pageable);
    Optional<Customer> getCustomerById(Long id);
    Optional<Customer> getCustomerByUserId(java.util.UUID userId);
    Optional<Customer> updateCustomer(Long id, Customer customerDetails);
}