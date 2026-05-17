package com.group.lbstore.Model;

// Enum định nghĩa vòng đời của một thiết bị vật lý
public enum ItemStatus {
    IN_STOCK,       // Đang nằm trong kho, sẵn sàng bán
    LOCKED,         // Đã có khách đặt hàng (đang chờ thanh toán, tạm khóa không cho người khác mua)
    SOLD,           // Đã bán và giao thành công
    DEFECTIVE,      // Hàng lỗi (DOA - Dead on Arrival), chờ trả về nhà cung cấp
    IN_WARRANTY     // Khách đang mang máy đến gửi bảo hành
}
