package com.group.lbstore.Service;

import com.group.lbstore.Model.Order;
import java.util.List;
import java.util.UUID;

public interface OrderService {
    Order createOrder(Order order);
    List<Order> getAllOrders();
    Order getOrderById(UUID id);
    Order updateStatus(UUID id, String status);
}