package com.group.lbstore.DTO;

import lombok.Data;

/**
 * DTO dùng để Admin tạo mới nhân viên giao hàng mà không cần liên kết Employee trước.
 * Backend tự động tạo User -> Employee -> DeliveryEmployee trong 1 transaction.
 */
@Data
public class DeliveryEmployeeRequest {
    // Thông tin cá nhân
    private String fullName;       // Họ tên đầy đủ
    private String phoneNumber;    // Số điện thoại
    private String email;          // Email (tùy chọn, nếu null sẽ tự sinh)

    // Thông tin phương tiện
    private String vehicleType;    // MOTORBIKE | TRUCK
    private String licensePlate;   // Biển số xe
    private String drivingLicense; // Số bằng lái

    // Trạng thái ban đầu
    private String status;         // OFFLINE | AVAILABLE (mặc định OFFLINE)
}
