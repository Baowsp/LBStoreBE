package com.group.lbstore.Service;

import com.group.lbstore.Model.Address;
import com.group.lbstore.Service.AddressService;
import com.group.lbstore.Repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;

    @Override
    public Address createAddress(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public List<Address> findByCustomerId(Long customerId) {
        return addressRepository.findByCustomerId(customerId);
    }

    @Override
    public Optional<Address> getAddressById(Long id) {
        return addressRepository.findById(id);
    }
    @Override
    public Optional<Address> updateAddress(Long id, Address addressDetails) {
        Address existingAddress = getAddressById(id).orElseThrow();
        existingAddress.setStreetAddress(addressDetails.getStreetAddress());
        existingAddress.setWard(addressDetails.getWard());
        existingAddress.setDistrict(addressDetails.getDistrict());
        existingAddress.setWard(addressDetails.getWard());
        existingAddress.setProvince(addressDetails.getProvince());
        existingAddress.setAddressType(addressDetails.getAddressType());
        return Optional.of(addressRepository.save(existingAddress));
    }

    @Override
    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
    }
}