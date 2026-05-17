package com.group.lbstore.Service;

import com.group.lbstore.Model.SalesChannel;
import com.group.lbstore.Model.SalesEmployee;
import com.group.lbstore.Repository.SalesEmployeeRepository;
import com.group.lbstore.Service.SalesEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesEmployeeServiceImpl implements SalesEmployeeService {

    private final SalesEmployeeRepository salesEmployeeRepository;

    @Override
    public SalesEmployee create(SalesEmployee salesEmployee) {
        return salesEmployeeRepository.save(salesEmployee);
    }

    @Override
    public List<SalesEmployee> findAll(SalesChannel channel) {
        return channel != null ? salesEmployeeRepository.findBySalesChannel(channel)
                : salesEmployeeRepository.findAll();
    }

    @Override
    public SalesEmployee findById(Long id) {
        return salesEmployeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Sales Employee not found"));
    }

    @Override
    public SalesEmployee updateKpi(Long id, BigDecimal monthlyTarget) {
        // Logic update KPI
        return findById(id); // Placeholder
    }
}