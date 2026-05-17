package com.group.lbstore.Model;

// Enum định nghĩa trạng thái của Shipper trong thời gian thực
public enum DeliveryStatus {
    OFFLINE,        // Không trong ca làm việc
    AVAILABLE,      // Đang rảnh, sẵn sàng nhận đơn mới
    DELIVERING      // Đang trên đường đi giao đơn
}
