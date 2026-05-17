package com.group.lbstore.Model;

// Enum định nghĩa trạng thái thanh toán công nợ với nhà cung cấp
public enum ImportPaymentStatus {
    UNPAID,         // Chưa thanh toán (Đang nợ)
    PARTIAL_PAID,   // Đã thanh toán một phần
    PAID            // Đã thanh toán đủ
}
