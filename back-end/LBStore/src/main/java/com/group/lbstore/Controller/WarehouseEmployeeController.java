package com.group.lbstore.Controller;

import com.group.lbstore.Model.WarehouseEmployee;
import com.group.lbstore.Model.WarehouseRole;
import com.group.lbstore.Service.WarehouseEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/warehouse-employees")
@RequiredArgsConstructor
public class WarehouseEmployeeController {

    private final WarehouseEmployeeService warehouseEmployeeService;

    @PostMapping
    public ResponseEntity<WarehouseEmployee> createWarehouseEmployee(@RequestBody WarehouseEmployee warehouseEmployee) {
        // Logic: Phân bổ nhân viên vào kho
        WarehouseEmployee created = warehouseEmployeeService.create(warehouseEmployee);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<WarehouseEmployee>> getAllWarehouseEmployees(
            @RequestParam(required = false) WarehouseRole role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        // Logic: Lấy danh sách nhân viên kho, lọc theo vai trò (VD: Tìm người kiểm kê)
        Page<WarehouseEmployee> employees = warehouseEmployeeService.findAll(role, pageable);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseEmployee> getWarehouseEmployeeById(@PathVariable Long id) {
        return warehouseEmployeeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseEmployee> updateWarehouseEmployee(
            @PathVariable Long id, 
            @RequestBody WarehouseEmployee details) {
        // Logic: Cập nhật thông tin (đổi ca làm việc, đổi vai trò)
        return warehouseEmployeeService.update(id, details)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}