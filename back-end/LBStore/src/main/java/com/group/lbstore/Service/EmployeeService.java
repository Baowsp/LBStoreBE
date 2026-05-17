package com.group.lbstore.Service;

import com.group.lbstore.Model.Department;
import com.group.lbstore.Model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Employee createEmployee(Employee employee);
    Page<Employee> findEmployees(Department department, boolean isActive, Pageable pageable);
    Optional<Employee> getEmployeeById(Long id);
    Optional<Employee> updateEmployee(Long id, Employee employeeDetails);
}