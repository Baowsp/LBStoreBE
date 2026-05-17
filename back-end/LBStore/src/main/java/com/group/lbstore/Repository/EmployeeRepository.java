package com.group.lbstore.Repository;

import com.group.lbstore.Model.Department;
import com.group.lbstore.Model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // Tìm nhân viên theo mã nhân viên (VD: EMP-1001)
    Optional<Employee> findByEmployeeCode(String employeeCode);

    // Tìm danh sách nhân viên theo phòng ban
    Page<Employee> findByDepartment(Department department,  Pageable pageable);

    // Tìm nhân viên đang làm việc (chưa nghỉ việc)
    Page<Employee> findByIsActiveTrue(Pageable pageable);
}