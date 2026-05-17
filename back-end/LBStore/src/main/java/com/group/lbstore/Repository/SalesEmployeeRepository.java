package com.group.lbstore.Repository;

import com.group.lbstore.Model.SalesChannel;
import com.group.lbstore.Model.SalesEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalesEmployeeRepository extends JpaRepository<SalesEmployee, Long> {
    // Tìm hồ sơ Sales dựa trên ID nhân viên gốc
    Optional<SalesEmployee> findByEmployeeId(Long employeeId);

    // Tìm nhân viên theo kênh bán hàng (VD: Tìm đội Online để chia lead từ web)
    List<SalesEmployee> findBySalesChannel(SalesChannel salesChannel);
}