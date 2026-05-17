package com.group.lbstore.Model;

// Enum định nghĩa vòng đời của một phiếu xuất kho
public enum ExportStatus {
    DRAFT,      // Bản nháp (Nhân viên kho đang đi nhặt máy, quét mã vạch IMEI)
    APPROVED,   // Đã được Quản lý duyệt (Chốt số lượng, không được sửa nữa)
    EXPORTED,   // Hàng đã chính thức rời khỏi kho
    CANCELLED   // Hủy phiếu (Khách hủy đơn phút chót khi hàng đang nhặt)
}
