package com.group.lbstore.Service;

import com.group.lbstore.Model.Department;
import com.group.lbstore.Model.Employee;
import com.group.lbstore.Repository.EmployeeRepository;
import com.group.lbstore.Service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Page<Employee> findEmployees(Department department, boolean isActive, Pageable pageable) {
        if (department != null) return employeeRepository.findByDepartment(department, pageable);
        return isActive ? employeeRepository.findByIsActiveTrue(pageable) : employeeRepository.findAll(pageable);
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Optional<Employee> updateEmployee(Long id, Employee employeeDetails) {
        Employee employee = getEmployeeById(id).orElseThrow();
        employee.setPosition(employeeDetails.getPosition());
        employee.setBaseSalary(employeeDetails.getBaseSalary());
        return Optional.of(employeeRepository.save(employee));
    }
}