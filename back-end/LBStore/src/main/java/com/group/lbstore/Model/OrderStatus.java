package com.group.lbstore.Model;

// Enum Trạng thái đơn hàng
public enum OrderStatus {
    PROCESSING, // Đang xử lý (Chờ kỹ thuật viên bóc seal, cài win, dán màn hình)
    COMPLETED,  // Đã giao máy cho khách, hoàn tất
    CANCELLED   // Khách hủy ngang không mua nữa
}
