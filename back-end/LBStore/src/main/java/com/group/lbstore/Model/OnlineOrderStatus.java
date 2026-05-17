package com.group.lbstore.Model;

public enum OnlineOrderStatus {
    PENDING, // Chờ xác nhận
    CONFIRMED, // Đã xác nhận
    PACKING, // Đang đóng gói
    SHIPPING, // Đang giao hàng
    DELIVERED, // Đã giao thành công
    CANCELLED, // Đã hủy
    RETURNED // Trả hàng
}
