package com.group.lbstore.Model;

// Enum định nghĩa các vai trò chuyên biệt trong kho
public enum WarehouseRole {
    MANAGER,            // Quản lý kho (Duyệt phiếu nhập/xuất)
    INVENTORY_CHECKER,  // Nhân viên kiểm kê (Đếm số lượng tồn kho định kỳ)
    PACKER,             // Nhân viên đóng gói (Chống sốc, dán tem vỡ cho laptop/điện thoại)
    QUALITY_CONTROL     // Nhân viên QC (Kiểm tra ngoại quan máy xem có xước/móp trước khi xuất)
}
