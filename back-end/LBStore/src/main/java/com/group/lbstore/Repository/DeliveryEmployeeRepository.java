package com.group.lbstore.Repository;

import com.group.lbstore.Model.DeliveryEmployee;
import com.group.lbstore.Model.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryEmployeeRepository extends JpaRepository<DeliveryEmployee, Long> {
    // Tìm hồ sơ Shipper dựa trên ID nhân viên gốc
    Optional<DeliveryEmployee> findByEmployeeId(Long employeeId);

    // Tìm các Shipper đang rảnh (AVAILABLE) để gán đơn
    List<DeliveryEmployee> findByStatus(DeliveryStatus status);
}