package com.group.lbstore.Controller;

import com.group.lbstore.Model.Department;
import com.group.lbstore.Model.Employee;
import com.group.lbstore.Service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * LƯU Ý: Rất khuyến khích sử dụng DTO. Ví dụ, một EmployeeRequestDTO
     * có thể chứa userId và các thông tin nhân viên khác, và một EmployeeResponseDTO
     * sẽ được trả về cho client.
     */

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        // Request body nên chứa các thông tin cần thiết, như đối tượng 'user' hoặc 'userId'.
        // Tầng service sẽ xử lý việc liên kết với User entity.
        Employee createdEmployee = employeeService.createEmployee(employee);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<Employee>> getAllEmployees(
            @RequestParam(required = false) Department department,
            @RequestParam(defaultValue = "true") boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employees = employeeService.findEmployees(department, isActive, pageable);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails) {
        return employeeService.updateEmployee(id, employeeDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}