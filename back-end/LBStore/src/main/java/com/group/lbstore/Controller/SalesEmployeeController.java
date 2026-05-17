package com.group.lbstore.Controller;

import com.group.lbstore.Model.SalesEmployee;
import com.group.lbstore.Model.SalesChannel;
import com.group.lbstore.Service.SalesEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/sales-employees")
@RequiredArgsConstructor
public class SalesEmployeeController {
    private final SalesEmployeeService salesEmployeeService;

    @PostMapping
    public ResponseEntity<SalesEmployee> createSalesEmployee(@RequestBody SalesEmployee salesEmployee) {
        // Logic: Tạo hồ sơ Sales, thiết lập KPI ban đầu
        SalesEmployee created = salesEmployeeService.create(salesEmployee);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SalesEmployee>> getAllSalesEmployees(
            @RequestParam(required = false) SalesChannel channel) {
        // Logic: Lấy danh sách Sales, lọc theo kênh (Online, Showroom, B2B)
        List<SalesEmployee> employees = salesEmployeeService.findAll(channel);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesEmployee> getSalesEmployeeById(@PathVariable Long id) {
        SalesEmployee employee = salesEmployeeService.findById(id);
        return ResponseEntity.ok(employee);
    }

    @PatchMapping("/{id}/kpi")
    public ResponseEntity<SalesEmployee> updateKpi(
            @PathVariable Long id,
            @RequestParam BigDecimal monthlyTarget) {
        SalesEmployee updated = salesEmployeeService.updateKpi(id, monthlyTarget);
        return ResponseEntity.ok(updated);
    }
}