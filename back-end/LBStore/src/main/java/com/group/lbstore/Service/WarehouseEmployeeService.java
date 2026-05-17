package com.group.lbstore.Service;

import com.group.lbstore.Model.WarehouseEmployee;
import com.group.lbstore.Model.WarehouseRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface WarehouseEmployeeService {
    WarehouseEmployee create(WarehouseEmployee warehouseEmployee);

    Page<WarehouseEmployee> findAll(WarehouseRole warehouseRole, Pageable pageable);

    Optional<WarehouseEmployee> findById(Long id);

    Optional<WarehouseEmployee> update(Long id, WarehouseEmployee details);
}