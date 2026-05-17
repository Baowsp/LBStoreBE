package com.group.lbstore.Repository;

import com.group.lbstore.Model.WarehouseEmployee;
import com.group.lbstore.Model.WarehouseRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseEmployeeRepository extends JpaRepository<WarehouseEmployee, Long> {


    Page<WarehouseEmployee> findByWarehouseRole(WarehouseRole warehouseRole, Pageable pageable);
}