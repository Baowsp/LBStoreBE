package com.group.lbstore.Controller;

import com.group.lbstore.Model.Address;
import com.group.lbstore.Model.AddressType;
import com.group.lbstore.Model.Customer;
import com.group.lbstore.Repository.CustomerRepository;
import com.group.lbstore.Service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;
    private final CustomerRepository customerRepository;

    @PostMapping
    public ResponseEntity<?> createAddress(@RequestBody Map<String, Object> body) {
        try {
            Object customerObj = body.get("customer");
            if (customerObj == null) {
                return ResponseEntity.badRequest().body("customer is required");
            }
            Long customerId = Long.valueOf(((Map<?, ?>) customerObj).get("id").toString());
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found: " + customerId));

            Address address = new Address();
            address.setCustomer(customer);
            address.setReceiverName((String) body.getOrDefault("receiverName", ""));
            address.setReceiverPhone((String) body.getOrDefault("receiverPhone", ""));
            address.setStreetAddress((String) body.getOrDefault("streetAddress", ""));
            address.setProvince((String) body.getOrDefault("province", "Khác"));
            address.setDistrict((String) body.getOrDefault("district", "Khác"));
            address.setWard((String) body.getOrDefault("ward", "Khác"));

            String addressTypeStr = (String) body.getOrDefault("addressType", "HOME");
            try { address.setAddressType(AddressType.valueOf(addressTypeStr)); }
            catch (Exception e) { address.setAddressType(AddressType.HOME); }

            address.setDefault(Boolean.TRUE.equals(body.get("isDefault")));

            Address created = addressService.createAddress(address);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Address>> getAddressesByCustomer(@PathVariable Long customerId) {
        List<Address> addresses = addressService.findByCustomerId(customerId);
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable Long id) {
        // Giả định addressService.getAddressById(id) trả về Optional<Address>
        return addressService.getAddressById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long id, @RequestBody Address addressDetails) {
        // Giả định addressService.updateAddress(...) trả về Optional<Address>
        return addressService.updateAddress(id, addressDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}