package com.group.lbstore.Service;

import com.group.lbstore.Model.Order;
import com.group.lbstore.Service.OrderService;
import com.group.lbstore.Model.OrderDetail;
import com.group.lbstore.Model.OrderStatus;
import com.group.lbstore.Repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    public Order createOrder(Order order) {
        // TODO: Add logic to generate orderNumber, calculate totals, etc.
        if (order.getOrderDetailList() != null) {
            for (OrderDetail detail : order.getOrderDetailList()) {
                detail.setOrder(order);
            }
        }
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(UUID id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public Order updateStatus(UUID id, String status) {
        Order order = getOrderById(id);
        order.setOrderStatus(OrderStatus.valueOf(status.toUpperCase()));
        return orderRepository.save(order);
    }
}