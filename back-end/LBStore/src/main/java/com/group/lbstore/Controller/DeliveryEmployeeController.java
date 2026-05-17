package com.group.lbstore.Controller;

import com.group.lbstore.Model.DeliveryEmployee;
import com.group.lbstore.Model.DeliveryStatus;
import com.group.lbstore.Service.DeliveryEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/delivery-employees")
@RequiredArgsConstructor
public class DeliveryEmployeeController {

    private final DeliveryEmployeeService deliveryEmployeeService;

    @PostMapping
    public ResponseEntity<DeliveryEmployee> createDeliveryEmployee(@RequestBody DeliveryEmployee deliveryEmployee) {
        // Logic: Tạo hồ sơ Shipper mới, liên kết với Employee gốc
        DeliveryEmployee created = deliveryEmployeeService.create(deliveryEmployee);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
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
}