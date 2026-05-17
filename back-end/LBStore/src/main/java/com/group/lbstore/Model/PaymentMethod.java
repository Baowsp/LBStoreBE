package com.group.lbstore.Model;

// Enum Phương thức thanh toán tại cửa hàng
public enum PaymentMethod {
    CASH,           // Tiền mặt
    CREDIT_CARD,    // Quẹt thẻ POS (Visa, Mastercard, JCB)
    BANK_TRANSFER,  // Chuyển khoản (Quét mã QR)
    INSTALLMENT     // Trả góp qua thẻ tín dụng hoặc công ty tài chính
}
