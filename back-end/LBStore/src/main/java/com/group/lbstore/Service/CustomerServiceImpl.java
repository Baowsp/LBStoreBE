package com.group.lbstore.Service;

import com.group.lbstore.Model.Customer;
import com.group.lbstore.Service.CustomerService;
import com.group.lbstore.Repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final com.group.lbstore.Repository.UserRepository userRepository;

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
    @Override
    public Page<Customer> getAllCustomers(Pageable pageable) {

        return customerRepository.findAll(pageable);
    }
    @Override
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Optional<Customer> getCustomerByUserId(java.util.UUID userId) {
        Optional<Customer> optionalCustomer = customerRepository.findByUserId(userId);
        if (optionalCustomer.isEmpty()) {
            return userRepository.findById(userId).map(user -> {
                Customer newCustomer = new Customer();
                newCustomer.setUser(user);
                return customerRepository.save(newCustomer);
            });
        }
        return optionalCustomer;
    }
    @Override
    public Optional<Customer> updateCustomer(Long id, Customer customerDetails) {
        Customer existingCustomer = getCustomerById(id).orElseThrow();
        // Assuming user details are not updated here, only customer-specific info
        existingCustomer.setUser(customerDetails.getUser());
        existingCustomer.setDateOfBirth(customerDetails.getDateOfBirth());
        existingCustomer.setGender(customerDetails.getGender());
        return Optional.of(customerRepository.save(existingCustomer));
    }
}