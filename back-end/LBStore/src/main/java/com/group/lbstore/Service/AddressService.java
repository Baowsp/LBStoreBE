package com.group.lbstore.Service;

import com.group.lbstore.Model.Address;
import java.util.List;
import java.util.Optional;

public interface AddressService {
    Address createAddress(Address address);
    List<Address> findByCustomerId(Long customerId);
    Optional<Address> getAddressById(Long id);
    Optional<Address> updateAddress(Long id, Address addressDetails);
    void deleteAddress(Long id);
}