package com.group.lbstore.Service;
import com.group.lbstore.Model.Supplier;
import com.group.lbstore.Service.SupplierService;
import com.group.lbstore.Repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;

    public Supplier createSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }
    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id).orElseThrow(() -> new RuntimeException("Supplier not found"));
    }
    public Supplier updateSupplier(Long id, Supplier supplierDetails) {
        Supplier existingSupplier = getSupplierById(id);
        existingSupplier.setName(supplierDetails.getName());
        existingSupplier.setContactPerson(supplierDetails.getContactPerson());
        existingSupplier.setPhoneNumber(supplierDetails.getPhoneNumber());
        return supplierRepository.save(existingSupplier);
    }
    public void deleteSupplier(Long id) {
        supplierRepository.deleteById(id);
    }
}