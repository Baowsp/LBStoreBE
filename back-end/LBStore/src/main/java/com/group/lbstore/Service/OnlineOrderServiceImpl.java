package com.group.lbstore.Service;

import com.group.lbstore.Model.OnlineOrder;
import com.group.lbstore.Model.OnlineOrderDetail;
import com.group.lbstore.Model.OnlineOrderStatus;
import com.group.lbstore.Model.DeliveryEmployee;
import com.group.lbstore.Model.DeliveryStatus;
import com.group.lbstore.Repository.OnlineOrderRepository;
import com.group.lbstore.Repository.DeliveryEmployeeRepository;
import com.group.lbstore.Service.OnlineOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OnlineOrderServiceImpl implements OnlineOrderService {

    private final OnlineOrderRepository onlineOrderRepository;
    private final DeliveryEmployeeRepository deliveryEmployeeRepository;

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
        OnlineOrderStatus oldStatus = order.getStatus();

        // Không cho phép hủy đơn hàng đã hoàn thành
        if (status == OnlineOrderStatus.CANCELLED && oldStatus == OnlineOrderStatus.DELIVERED) {
            throw new RuntimeException("Không thể hủy đơn hàng đã hoàn thành");
        }

        order.setStatus(status);

        // Nếu chuyển sang CANCELLED → hoàn lại số lượng tồn kho
        if (status == OnlineOrderStatus.CANCELLED && oldStatus != OnlineOrderStatus.CANCELLED) {
            restoreStock(order);
        }

        return onlineOrderRepository.save(order);
    }

    /**
     * Khách hàng xác nhận đã nhận hàng (chỉ được phép khi đang ở trạng thái SHIPPING)
     */
    @Override
    public OnlineOrder customerConfirmDelivered(UUID id) {
        OnlineOrder order = getOnlineOrderById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
        if (order.getStatus() != OnlineOrderStatus.SHIPPING) {
            throw new RuntimeException("Chỉ có thể xác nhận nhận hàng khi đơn đang ở trạng thái Đang giao");
        }
        order.setStatus(OnlineOrderStatus.DELIVERED);
        return onlineOrderRepository.save(order);
    }

    /**
     * Hoàn lại tồn kho cho tất cả sản phẩm trong đơn hàng khi hủy
     */
    private void restoreStock(OnlineOrder order) {
        if (order.getOnlineOrderDetails() == null) return;
        for (OnlineOrderDetail detail : order.getOnlineOrderDetails()) {
            if (detail.getVariantColor() == null) continue;
            var variantColor = detail.getVariantColor();
            int qty = detail.getQuantity() != null ? detail.getQuantity() : 0;

            // Hoàn lại tồn kho ProductVariantColor
            int newColorStock = variantColor.getStockQuantity() + qty;
            variantColor.setStockQuantity(newColorStock);

            // Cập nhật ProductVariant = tổng các màu con
            var productVariant = variantColor.getProductVariant();
            if (productVariant != null) {
                int totalVariantStock = productVariant.getVariantColors().stream()
                        .mapToInt(c -> c.getStockQuantity())
                        .sum();
                productVariant.setStockQuantity(totalVariantStock);

                // Cập nhật Product = tổng các variant con
                var product = productVariant.getProduct();
                if (product != null) {
                    int totalProductStock = product.getVariants().stream()
                            .mapToInt(v -> v.getStockQuantity())
                            .sum();
                    product.setStockQuantity(totalProductStock);
                }
            }
        }
    }

    @Override
    public Page<OnlineOrder> getAllOnlineOrders(Pageable pageable) {
        return onlineOrderRepository.findAll(pageable);
    }

    /**
     * Phân công shipper cho đơn hàng và chuyển trạng thái
     * Đơn hàng: PENDING/CONFIRMED → SHIPPING
     * Shipper: AVAILABLE → DELIVERING
     */
    @Override
    @Transactional
    public OnlineOrder assignShipper(UUID orderId, Long shipperId) {
        OnlineOrder order = onlineOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng ID: " + orderId));

        if (order.getStatus() != OnlineOrderStatus.PENDING && order.getStatus() != OnlineOrderStatus.CONFIRMED) {
            throw new RuntimeException("Đơn hàng " + order.getOrderNumber() +
                    " không ở trạng thái chờ xử lý (hiện tại: " + order.getStatus() + ")");
        }

        DeliveryEmployee shipper = deliveryEmployeeRepository.findById(shipperId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên giao hàng ID: " + shipperId));

        order.setDeliveryEmployee(shipper);
        order.setStatus(OnlineOrderStatus.SHIPPING);

        shipper.setStatus(DeliveryStatus.DELIVERING);
        deliveryEmployeeRepository.save(shipper);

        return onlineOrderRepository.save(order);
    }
}