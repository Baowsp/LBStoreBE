package com.group.lbstore.Model;

// Enum Trạng thái thanh toán
public enum PaymentStatus {
    PENDING,
    UNPAID,     // Chưa thanh toán (Đang chờ duyệt trả góp)
    PAID,       // Đã thanh toán
    REFUNDED    // Đã hoàn tiền (Khách trả lại máy)
}
