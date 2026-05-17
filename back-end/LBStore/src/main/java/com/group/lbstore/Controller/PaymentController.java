package com.group.lbstore.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.PaymentLinkItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PayOS payOS;

    @Value("${payos.return-url}")
    private String returnUrl;

    @Value("${payos.cancel-url}")
    private String cancelUrl;

    /**
     * Tạo link thanh toán PayOS
     * Body: { "orderCode": 123456789, "amount": 1000000, "description": "...", "items": [...] }
     */
    @PostMapping("/create-link")
    public ResponseEntity<?> createPaymentLink(@RequestBody Map<String, Object> body) {
        try {
            // JS numbers are often sent as floats in JSON (e.g. 1.0). Use Double then cast to long for safety.
            long orderCode = Double.valueOf(body.get("orderCode").toString()).longValue();
            long amount = Double.valueOf(body.get("amount").toString()).longValue();
            
            String description = body.getOrDefault("description", "Thanh toan LBStore").toString();
            // PayOS: description max 25 chars
            if (description.length() > 25) description = description.substring(0, 25);

            // Build items
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> rawItems = (List<Map<String, Object>>) body.getOrDefault("items", new ArrayList<>());

            List<PaymentLinkItem> items = new ArrayList<>();
            for (Map<String, Object> i : rawItems) {
                String name = i.getOrDefault("name", "San pham").toString();
                if (name.length() > 50) name = name.substring(0, 50);
                items.add(PaymentLinkItem.builder()
                        .name(name)
                        .quantity(Integer.parseInt(i.getOrDefault("quantity", "1").toString()))
                        .price(Double.valueOf(i.getOrDefault("price", "0").toString()).longValue())
                        .build());
            }

            CreatePaymentLinkRequest paymentData = CreatePaymentLinkRequest.builder()
                    .orderCode(orderCode)
                    .amount(amount)
                    .description(description)
                    .items(items)
                    .returnUrl(returnUrl)
                    .cancelUrl(cancelUrl)
                    .build();

            var paymentLink = payOS.paymentRequests().create(paymentData);
            return ResponseEntity.ok(Map.of(
                    "checkoutUrl", paymentLink.getCheckoutUrl(),
                    "qrCode",      paymentLink.getQrCode() != null ? paymentLink.getQrCode() : "",
                    "paymentLinkId", paymentLink.getPaymentLinkId() != null ? paymentLink.getPaymentLinkId() : "",
                    "orderCode",   paymentLink.getOrderCode()
            ));
        } catch (Exception e) {
            System.err.println("❌ Error creating PayOS payment link: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "PayOS Error: " + (e.getMessage() != null ? e.getMessage() : "Unknown error")));
        }
    }

    /**
     * Webhook callback từ PayOS sau khi khách thanh toán xong
     */
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody Map<String, Object> webhookBody) {
        try {
            String code = webhookBody.getOrDefault("code", "").toString();
            if ("00".equals(code)) {
                System.out.println("✅ PayOS payment confirmed: orderCode=" + webhookBody.get("orderCode"));
                // TODO: Cập nhật trạng thái OnlineOrder trong DB
            }
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
