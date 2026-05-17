package com.group.lbstore.Model;

// Enum định nghĩa vòng đời của một phiếu nhập kho
public enum ImportStatus {
    DRAFT,      // Bản nháp (Đang tạo đơn đặt hàng PO gửi nhà cung cấp)
    PENDING,    // Đang chờ hàng về kho
    INSPECTING, // Hàng đã về, nhân viên kho đang kiểm tra QC (Quality Control) và bắn mã vạch
    COMPLETED,  // Đã nhập kho thành công (Lúc này tồn kho mới chính thức tăng lên)
    CANCELLED   // Đơn nhập bị hủy (Nhà cung cấp báo hết hàng)
}
