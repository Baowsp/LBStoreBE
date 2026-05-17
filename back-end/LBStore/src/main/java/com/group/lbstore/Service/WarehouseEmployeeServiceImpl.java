package com.group.lbstore.Service;

import com.group.lbstore.Model.WarehouseEmployee;
import com.group.lbstore.Model.WarehouseRole;
import com.group.lbstore.Repository.WarehouseEmployeeRepository;
import com.group.lbstore.Service.WarehouseEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WarehouseEmployeeServiceImpl implements WarehouseEmployeeService {

    private final WarehouseEmployeeRepository warehouseEmployeeRepository;

    @Override
    public WarehouseEmployee create(WarehouseEmployee warehouseEmployee) {
        return warehouseEmployeeRepository.save(warehouseEmployee);
    }

    @Override
    public Page<WarehouseEmployee> findAll(WarehouseRole warehouseRole, Pageable pageable) {
        if (warehouseRole != null) {
            return warehouseEmployeeRepository.findByWarehouseRole(warehouseRole, pageable);
        }
        return warehouseEmployeeRepository.findAll(pageable);
    }

    @Override
    public Optional<WarehouseEmployee> findById(Long id) {

        return warehouseEmployeeRepository.findById(id);
    }

    @Override
    public Optional<WarehouseEmployee> update(Long id, WarehouseEmployee details) {
        return warehouseEmployeeRepository.findById(id).map(employee -> {
            // Cập nhật thông tin
            // employee.setName(details.getName());
            return warehouseEmployeeRepository.save(employee);
        });
    }
}