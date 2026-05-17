package com.group.lbstore.Service;

import com.group.lbstore.Model.SalesChannel;
import com.group.lbstore.Model.SalesEmployee;
import java.math.BigDecimal;
import java.util.List;

public interface SalesEmployeeService {
    SalesEmployee create(SalesEmployee salesEmployee);
    List<SalesEmployee> findAll(SalesChannel channel);
    SalesEmployee findById(Long id);
    SalesEmployee updateKpi(Long id, BigDecimal monthlyTarget);
}