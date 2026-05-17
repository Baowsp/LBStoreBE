package com.group.lbstore.Controller;

import com.group.lbstore.Model.*;
import com.group.lbstore.Repository.*;
import com.group.lbstore.Service.OnlineOrderService;
import com.group.lbstore.Service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/online-orders")
@RequiredArgsConstructor
@Transactional
public class OnlineOrderController {

    private final OnlineOrderService onlineOrderService;
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final ProductVariantColorRepository productVariantColorRepository;
    private final OnlineOrderDetailRepository onlineOrderDetailRepository;
    private final VoucherService voucherService;

    @GetMapping
    public ResponseEntity<Page<OnlineOrder>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(onlineOrderService.getAllOnlineOrders(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OnlineOrder> getOrderById(@PathVariable UUID id) {
        return onlineOrderService.getOnlineOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/track/{orderNumber}")
    public ResponseEntity<OnlineOrder> getOrderByOrderNumber(@PathVariable String orderNumber) {
        return onlineOrderService.getOnlineOrderByOrderNumber(orderNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OnlineOrder>> getOrdersByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(onlineOrderService.getOrdersByCustomer(customerId));
    }

    /**
     * POST /api/online-orders
     * Body gửi lên từ Frontend:
     * {
     *   "customer": { "id": 1 },
     *   "shippingAddress": { "id": 2 },
     *   "totalAmount": 1030000,
     *   "shippingFee": 30000,
     *   "notes": "[COD] Giao buổi tối",
     *   "status": "PENDING",
     *   "onlineOrderDetails": [
     *     { "variantColor": { "id": 5 }, "quantity": 1, "unitPrice": 1000000, "subTotal": 1000000 }
     *   ]
     * }
     */
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> body) {
        try {
            // --- Load Customer ---
            Object custObj = body.get("customer");
            if (custObj == null) return ResponseEntity.badRequest().body("customer is required");
            Long customerId = Long.valueOf(((Map<?, ?>) custObj).get("id").toString());
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found: " + customerId));

            // --- Load Address ---
            Object addrObj = body.get("shippingAddress");
            if (addrObj == null) return ResponseEntity.badRequest().body("shippingAddress is required");
            Long addressId = Long.valueOf(((Map<?, ?>) addrObj).get("id").toString());
            Address shippingAddress = addressRepository.findById(addressId)
                    .orElseThrow(() -> new RuntimeException("Address not found: " + addressId));

            // --- Build OnlineOrder ---
            OnlineOrder order = new OnlineOrder();
            BigDecimal totalAmount = new BigDecimal(body.getOrDefault("totalAmount", 0).toString());
            BigDecimal shippingFee = new BigDecimal(body.getOrDefault("shippingFee", 0).toString());
            BigDecimal finalAmount = totalAmount.add(shippingFee);

            order.setCustomer(customer);
            order.setShippingAddress(shippingAddress);
            String addrText = shippingAddress.getStreetAddress() + ", "
                    + shippingAddress.getWard() + ", "
                    + shippingAddress.getDistrict() + ", "
                    + shippingAddress.getProvince();
            order.setShippingAddressText(addrText);
            order.setShippingName(shippingAddress.getReceiverName());
            order.setShippingPhone(shippingAddress.getReceiverPhone());
            order.setTotalAmount(totalAmount);
            order.setShippingFee(shippingFee);
            order.setFinalAmount(body.containsKey("finalAmount")
                    ? new BigDecimal(body.get("finalAmount").toString()) : finalAmount);
            
            BigDecimal discountAmount = BigDecimal.ZERO;
            String voucherCode = null;
            
            if (body.containsKey("voucherCode")) {
                voucherCode = (String) body.get("voucherCode");
                if (voucherCode != null && !voucherCode.trim().isEmpty()) {
                    Voucher voucher = voucherService.validateAndApplyVoucher(voucherCode, totalAmount);
                    // Giảm theo %
                    BigDecimal discountPercentage = voucher.getDiscountPercentage();
                    discountAmount = totalAmount.multiply(discountPercentage).divide(new BigDecimal("100"));
                    
                    // Giới hạn max
                    if (voucher.getMaxDiscountAmount() != null && discountAmount.compareTo(voucher.getMaxDiscountAmount()) > 0) {
                        discountAmount = voucher.getMaxDiscountAmount();
                    }
                    
                    order.setVoucherCode(voucher.getCode());
                    
                    // Tăng số lượng đã dùng
                    int currentUsed = voucher.getUsedCount() != null ? voucher.getUsedCount() : 0;
                    voucher.setUsedCount(currentUsed + 1);
                }
            }
            order.setDiscountAmount(discountAmount);
            order.setFinalAmount(totalAmount.subtract(discountAmount).add(shippingFee));
            
            order.setNotes((String) body.getOrDefault("notes", ""));

            String notes = (String) body.getOrDefault("notes", "");
            String paymentMethodStr = (String) body.get("paymentMethod");
            String pm;
            
            if (paymentMethodStr != null && !paymentMethodStr.isEmpty()) {
                pm = paymentMethodStr.toUpperCase();
            } else {
                pm = notes.startsWith("[COD]") ? "COD"
                    : notes.startsWith("[CHUYỂN KHOẢN") ? "BANKING"
                    : notes.startsWith("[MOMO]") ? "MOMO"
                    : notes.startsWith("[VIETQR") || notes.startsWith("[PAYOS") ? "VIETQR"
                    : "COD";
            }
            order.setPaymentMethod(pm);
            order.setPaymentStatus("UNPAID");
            order.setStatus(OnlineOrderStatus.PENDING);
            order.setOrderStatus("PENDING");
            order.setOrderNumber("ORD-ONL-" + System.currentTimeMillis());
            order.setOnlineOrderDetails(new ArrayList<>());

            // --- Step 1: Save the parent first ---
            OnlineOrder savedOrder = onlineOrderService.createOnlineOrder(order);
            System.out.println("[DEBUG] Saved Order ID: " + savedOrder.getId());

            // --- Step 2: Extract details from body ---
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> detailsRaw = (List<Map<String, Object>>) body.getOrDefault("onlineOrderDetails", new ArrayList<>());
            System.out.println("[DEBUG] Received " + detailsRaw.size() + " order items in JSON payload");

            List<OnlineOrderDetail> details = new ArrayList<>();
            for (Map<String, Object> d : detailsRaw) {
                Object variantObj = d.get("variant");
                if (variantObj == null) variantObj = d.get("variantColor");
                if (variantObj == null) {
                    System.out.println("[DEBUG] Missing variant object in payload item: " + d);
                    continue;
                }

                Long variantColorId = Long.valueOf(((Map<?, ?>) variantObj).get("id").toString());
                System.out.println("[DEBUG] Looking up variant ID: " + variantColorId);

                ProductVariantColor variantColor = productVariantColorRepository.findById(variantColorId).orElse(null);
                if (variantColor == null) {
                    throw new RuntimeException("Sản phẩm có Variant ID " + variantColorId + " không tồn tại trong hệ thống. Vui lòng cập nhật lại giỏ hàng.");
                }

                int quantity = Integer.parseInt(d.getOrDefault("quantity", "1").toString());
                BigDecimal unitPrice = new BigDecimal(d.getOrDefault("unitPrice", "0").toString());
                BigDecimal subTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));

                // Create and SAVE detail explicitly
                OnlineOrderDetail detail = new OnlineOrderDetail();
                detail.setOnlineOrder(savedOrder); 
                detail.setVariantColor(variantColor);
                detail.setQuantity(quantity);
                detail.setUnitPrice(unitPrice);
                detail.setSubTotal(subTotal);
                detail.setDiscountAmount(BigDecimal.ZERO);
                
                OnlineOrderDetail savedDetail = onlineOrderDetailRepository.save(detail); // EXPLICIT SAVE
                System.out.println("[DEBUG] Successfully saved detail ID: " + savedDetail.getId() + " for order " + savedOrder.getOrderNumber());
                
                details.add(savedDetail);
            }
            
            // Link details back to parent for JSON response
            savedOrder.setOnlineOrderDetails(details);

            // Refetch to ensure EntityGraph is active for response
            return onlineOrderService.getOnlineOrderById(savedOrder.getId())
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.ok(savedOrder));

        } catch (Exception e) {
            System.out.println("[DEBUG] FATAL Error creating order: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage() != null ? e.getMessage() : "Internal error"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<OnlineOrder> updateOrder(@PathVariable UUID id,
                                                    @RequestBody OnlineOrderStatus onlineOrderStatus) {
        return ResponseEntity.ok(onlineOrderService.updateOrderStatus(id, onlineOrderStatus));
    }
}