package com.group.lbstore.Service;

import com.group.lbstore.Model.OnlineOrder;
import com.group.lbstore.Model.OnlineOrderDetail;
import com.group.lbstore.Model.OnlineOrderStatus;
import com.group.lbstore.Repository.OnlineOrderRepository;
import com.group.lbstore.Service.OnlineOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OnlineOrderServiceImpl implements OnlineOrderService {

    private final OnlineOrderRepository onlineOrderRepository;

    @Override
    public OnlineOrder createOnlineOrder(OnlineOrder onlineOrder) {
        // TODO: Logic to validate customer, address, calculate totals, generate order
        // number
        if (onlineOrder.getOrderNumber() == null || onlineOrder.getOrderNumber().isEmpty()) {
            onlineOrder.setOrderNumber("ORD-ONL-" + System.currentTimeMillis());
        }
        onlineOrder.setStatus(OnlineOrderStatus.PENDING);
        return onlineOrderRepository.save(onlineOrder);
    }

    @Override
    public Optional<OnlineOrder> getOnlineOrderByOrderNumber(String orderNumber) {
        return Optional.of(onlineOrderRepository.getOnlineOrdersByOrderNumber(orderNumber));
    }

    @Override
    public Optional<OnlineOrder> getOnlineOrderById(UUID id) {
        return onlineOrderRepository.findById(id);
    }

    @Override
    public List<OnlineOrder> getOrdersByCustomer(Long customerId) {
        return onlineOrderRepository.findByCustomerId(customerId);
    }

    @Override
    public OnlineOrder updateOrderStatus(UUID id, OnlineOrderStatus status) {
        OnlineOrder order = getOnlineOrderById(id).orElseThrow();
        order.setStatus(status);
        // TODO: Add side effects for status changes (e.g., create ExportInvoice when
        // CONFIRMED)
        return onlineOrderRepository.save(order);
    }

    @Override
    public Page<OnlineOrder> getAllOnlineOrders(Pageable pageable) {
        return onlineOrderRepository.findAll(pageable);
    }
}