package com.group.lbstore.Service;

import com.group.lbstore.DTO.DeliveryEmployeeRequest;
import com.group.lbstore.Model.DeliveryEmployee;
import com.group.lbstore.Model.DeliveryStatus;
import java.util.List;

public interface DeliveryEmployeeService {
    DeliveryEmployee create(DeliveryEmployee deliveryEmployee);
    DeliveryEmployee createWithInfo(DeliveryEmployeeRequest request);
    List<DeliveryEmployee> findAll(DeliveryStatus status);
    DeliveryEmployee findById(Long id);
    void updateLocation(Long id, Double latitude, Double longitude);
    DeliveryEmployee updateStatus(Long id, DeliveryStatus status);
    DeliveryEmployee update(Long id, DeliveryEmployee data);
    void delete(Long id);
}