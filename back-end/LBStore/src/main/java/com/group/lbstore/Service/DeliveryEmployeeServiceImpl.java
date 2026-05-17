package com.group.lbstore.Service;

import com.group.lbstore.Model.DeliveryEmployee;
import com.group.lbstore.Model.DeliveryStatus;
import com.group.lbstore.Repository.DeliveryEmployeeRepository;
import com.group.lbstore.Service.DeliveryEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryEmployeeServiceImpl implements DeliveryEmployeeService {

    private final DeliveryEmployeeRepository deliveryEmployeeRepository;

    @Override
    public DeliveryEmployee create(DeliveryEmployee deliveryEmployee) {
        return deliveryEmployeeRepository.save(deliveryEmployee);
    }

    @Override
    public List<DeliveryEmployee> findAll(DeliveryStatus status) {
        return status != null ? deliveryEmployeeRepository.findByStatus(status) : deliveryEmployeeRepository.findAll();
    }

    @Override
    public DeliveryEmployee findById(Long id) {
        return deliveryEmployeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Shipper not found"));
    }

    @Override
    public void updateLocation(Long id, Double latitude, Double longitude) {
        DeliveryEmployee employee = findById(id);
        employee.setCurrentLatitude(latitude);
        employee.setCurrentLongitude(longitude);
        deliveryEmployeeRepository.save(employee);
    }

    @Override
    public DeliveryEmployee updateStatus(Long id, DeliveryStatus status) {
        DeliveryEmployee employee = findById(id);
        employee.setStatus(status);
        return deliveryEmployeeRepository.save(employee);
    }
}