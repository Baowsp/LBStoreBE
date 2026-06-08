package com.group.lbstore.Controller;

import com.group.lbstore.DTO.DeliveryEmployeeRequest;
import com.group.lbstore.Model.DeliveryEmployee;
import com.group.lbstore.Model.DeliveryStatus;
import com.group.lbstore.Service.DeliveryEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/delivery-employees")
@RequiredArgsConstructor
public class DeliveryEmployeeController {

    private final DeliveryEmployeeService deliveryEmployeeService;

    @PostMapping
    public ResponseEntity<DeliveryEmployee> createDeliveryEmployee(@RequestBody DeliveryEmployee deliveryEmployee) {
        DeliveryEmployee created = deliveryEmployeeService.create(deliveryEmployee);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * POST /api/v1/delivery-employees/create-with-info
     * Tạo mới nhân viên giao hàng từ form nhập tay (không cần link Employee sẵn có).
     * Backend tự động tạo User → Employee → DeliveryEmployee trong 1 transaction.
     */
    @PostMapping("/create-with-info")
    public ResponseEntity<?> createWithInfo(@RequestBody DeliveryEmployeeRequest request) {
        try {
            DeliveryEmployee created = deliveryEmployeeService.createWithInfo(request);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<DeliveryEmployee>> getAllDeliveryEmployees(
            @RequestParam(required = false) DeliveryStatus status) {
        // Logic: Lấy danh sách Shipper, lọc theo trạng thái (để tìm người đang AVAILABLE)
        List<DeliveryEmployee> employees = deliveryEmployeeService.findAll(status);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryEmployee> getDeliveryEmployeeById(@PathVariable Long id) {
        DeliveryEmployee employee = deliveryEmployeeService.findById(id);
        return ResponseEntity.ok(employee);
    }

    @PatchMapping("/{id}/location")
    public ResponseEntity<Void> updateLocation(
            @PathVariable Long id,
            @RequestParam Double latitude,
            @RequestParam Double longitude) {
        // Logic: Cập nhật vị trí GPS thời gian thực của Shipper
        deliveryEmployeeService.updateLocation(id, latitude, longitude);
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<DeliveryEmployee> updateStatus(@PathVariable Long id, @RequestParam DeliveryStatus status) {
        // Logic: Shipper bật/tắt trạng thái nhận đơn (ONLINE/OFFLINE)
        DeliveryEmployee updated = deliveryEmployeeService.updateStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeliveryEmployee> updateDeliveryEmployee(
            @PathVariable Long id,
            @RequestBody DeliveryEmployee data) {
        DeliveryEmployee updated = deliveryEmployeeService.update(id, data);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeliveryEmployee(@PathVariable Long id) {
        deliveryEmployeeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}