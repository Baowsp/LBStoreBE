package com.group.lbstore.Service;
import com.group.lbstore.Model.Supplier;
import java.util.List;
public interface SupplierService {
    Supplier createSupplier(Supplier supplier);
    List<Supplier> getAllSuppliers();
    Supplier getSupplierById(Long id);
    Supplier updateSupplier(Long id, Supplier supplierDetails);
    void deleteSupplier(Long id);
}