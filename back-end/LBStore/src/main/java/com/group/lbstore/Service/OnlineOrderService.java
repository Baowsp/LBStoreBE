package com.group.lbstore.Service;

import com.group.lbstore.Model.OnlineOrder;
import com.group.lbstore.Model.OnlineOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OnlineOrderService {
    OnlineOrder createOnlineOrder(OnlineOrder onlineOrder);
    Optional<OnlineOrder> getOnlineOrderById(UUID id);
    Optional<OnlineOrder> getOnlineOrderByOrderNumber(String orderNumber);
    List<OnlineOrder> getOrdersByCustomer(Long customerId);
    OnlineOrder updateOrderStatus(UUID id, OnlineOrderStatus status);
    OnlineOrder customerConfirmDelivered(UUID id);
    OnlineOrder assignShipper(UUID orderId, Long shipperId);
    Page<OnlineOrder> getAllOnlineOrders(Pageable pageable);

}